import 'dart:collection';

import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "message.g.dart";

@riverpod
class MessageState extends _$MessageState {
  static List<Message> messages = [];

  @override
  Future<List<Message>> build() async {
    dynamic response =
        await Http.post("/message/history", {"from": "", "to": ""});
    Result<List<Message>> unionResult = Result.fromJsonT(response, (json) {
      return (json as List<dynamic>)
          .map((e) => Message.fromJson(e as Map<String, dynamic>))
          .toList();
    });
    if (unionResult.status == true && unionResult.data != null) {
      messages = unionResult.data!;
    }
    return messages;
  }

  void set(List<Message> messages) {
    state = AsyncValue.data(messages);
  }

  Future<void> add(List<Message> messages, Message message) async {
    set(MessagesUtil.addOrUpdate(messages, message));
  }

  Future<void> updateObservers(num id) async {
    messages
        .where((it) => it.id == id)
        .firstOrNull
        ?.observers
        .add(Global.appStore.profile?.userId ?? -1);
    set(messages);
  }
}

class MessagesUtil {
  static List<Message> byTypeAndId(List<Message> messages, bool type, num id) =>
      messages
          .where((it) =>
              it.type == type &&
              ((type && it.to == id) ||
                  (!type && (it.from == id || it.to == id))))
          .toList();

  static Map<Pair<bool, num>, List<Message>> toMapByTypeAndId(
      List<Message> messages,
      {required num ownId}) {
    List<Message> sortMessages = messages;
    sortMessages.sort((a, b) => a.date < b.date ? -1 : 1);
    Map<Pair<bool, num>, List<Message>> map = {};
    for (var message in sortMessages) {
      (map[Pair(
              first: message.type,
              second:
                  message.from == ownId ? message.to : message.from)] ??= [])
          .add(message);
    }
    return map;
  }

  static Map<Pair<bool, num>, Message> lastMessages(List<Message> messages,
      {required num ownId}) {
    Map<Pair<bool, num>, List<Message>> map =
        toMapByTypeAndId(messages, ownId: ownId);
    Map<Pair<bool, num>, Message> result = {};
    map.forEach((key, value) {
      result[key] = value.first;
    });
    return result;
  }

  static Message? lastMessageByTypeAndId(
    List<Message> messages,
    bool type,
    num id,
  ) {
    List<Message> temp = byTypeAndId(messages, type, id);
    temp.sort((a, b) => a.date < b.date ? -1 : 1);
    return temp.isEmpty ? null : temp.last;
  }

  static Message? lastMessageAsMapByTypeAndId(
    Map<Pair<bool, num>, List<Message>> map,
    bool type,
    num id,
  ) {
    List<Message> temp = map[Pair(first: type, second: id)] ?? [];
    temp.sort((a, b) => a.date < b.date ? -1 : 1);
    return temp.isEmpty ? null : temp.last;
  }

  static List<Message> addOrUpdate(List<Message> messages, Message message) {
    List<Message> temp = messages;
    int index = temp.indexWhere((it) => it.id == message.id);
    if (index != -1) {
      temp[index] = message;
    } else {
      temp.add(message);
    }
    return temp;
  }

  static num getUnreadNumberByTypeAndId(
    List<Message> messages,
    bool type,
    num id,
    num ownId,
  ) {
    List<Message> temp = byTypeAndId(messages, type, id);
    temp.sort((a, b) => a.date < b.date ? -1 : 1);
    for (int i = temp.length - 1; i > 0; i--) {
      if (temp[i].from == ownId ||
          (temp[i].from != ownId && temp[i].observers.contains(ownId))) {
        return temp.length - 1 - i;
      }
    }
    return temp.length;
  }

  static Map<num, List<List<Message>>> test(
    List<Message> messages,
    bool type,
    num id,
  ) {
    List<Message> temp = byTypeAndId(messages, type, id);
    temp.sort((a, b) => a.date < b.date ? -1 : 1);
    Map<num, List<List<Message>>> map = {};
    for (int i = 0; i < temp.length; i++) {
      var message = temp[i];
      final from = message.from;
      final date = message.date;
      final key = date ~/ (5 * 60 * 1000);
      map[key] = map[key] ?? [];
      if (map[key]!.isEmpty) {
        map[key]!.add([message]);
      } else if (temp[i - 1].from == from) {
        int index = map[key]!.length - 1;
        map[key]![index].add(message);
      } else {
        map[key]!.add([message]);
      }
    }
    Map<num, List<List<Message>>> result =
        LinkedHashMap.fromEntries(map.entries.toList().reversed);
    return result;
  }
}

class Pair<M, N> {
  late M first;
  late N second;

  Pair({required this.first, required this.second});
}
