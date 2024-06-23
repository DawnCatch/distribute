import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "message.g.dart";

@riverpod
class MessageState extends _$MessageState {
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
      return unionResult.data!;
    }
    return [];
  }

  void set(List<Message> messages) {
    state = AsyncValue.data(messages);
  }

  Future<void> add(List<Message> messages, Message message) async {
    set(MessagesUtil.addOrUpdate(messages, message));
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
    sortMessages.sort((a, b) => a.date < b.date ? 0 : 1);
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
    temp.sort((a, b) => a.date < b.date ? 0 : 1);
    return temp.isEmpty ? null : temp.last;
  }

  static Message? lastMessageAsMapByTypeAndId(
    Map<Pair<bool, num>, List<Message>> map,
    bool type,
    num id,
  ) {
    List<Message> temp = map[Pair(first: type, second: id)] ?? [];
    temp.sort((a, b) => a.date < b.date ? 0 : 1);
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
    for (int i = temp.length - 1; i > 0; i--) {
      if (temp[i].from == ownId ||
          (temp[i].from == id && temp[i].observers.contains(ownId))) {
        return temp.length - 1 - i;
      }
    }
    return 0;
  }

  static Map<num, Map<num, List<Message>>> test(
    List<Message> messages,
    bool type,
    num id,
  ) {
    List<Message> temp = byTypeAndId(messages, type, id);
    temp.sort((a, b) => a.date < b.date ? 1 : 0);
    Map<num, Map<num, List<Message>>> map = {};
    for (var message in temp) {
      final from = message.from;
      final date = message.date;
      final key = date ~/ (5 * 60 * 1000);
      map[key] = map[key] ?? {};
      map[key]![from] = map[key]![from] ?? [];
      map[key]![from]!.add(message);
    }
    map.forEach((_,value) {
      value.forEach((_,value) {
        value.sort((a, b) => a.date < b.date ? 0 : 1);
      });
    });
    return map;
  }
}

class Pair<M, N> {
  late M first;
  late N second;

  Pair({required this.first, required this.second});
}
