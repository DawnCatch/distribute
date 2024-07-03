import 'package:distribute/common/global.dart';
import 'package:distribute/models/index.dart';
import 'package:distribute/stores/group_profile.dart';
import 'package:distribute/stores/message.dart';
import 'package:distribute/stores/union.dart';
import 'package:distribute/stores/user_profile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class HomeBodyRelationItem extends ConsumerStatefulWidget {
  const HomeBodyRelationItem({super.key, required this.type, required this.id});

  final bool type;
  final num id;

  @override
  ConsumerState<ConsumerStatefulWidget> createState() =>
      _HomeBodyRelationItemState();
}

class _HomeBodyRelationItemState extends ConsumerState<HomeBodyRelationItem> {
  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 16),
        child: Row(
          children: [
            const CircleAvatar(
              radius: 32,
              backgroundImage:
                  NetworkImage("https://files.catbox.moe/nizk28.jpg"),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  widget.type ? buildGroupTitle() : buildUserTitle(),
                  buildTips(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildUserTitle() {
    final AsyncValue<Union> unionState = ref.watch(unionStateProvider);
    final profileState = ref.watch(userProfileStateProvider.call(widget.id));
    String title = unionState.when(
        data: (data) =>
            data.friends.where((it) => it.id == widget.id).firstOrNull?.title ??
            "",
        error: (error, stack) => "",
        loading: () => "");
    if (title == "") {
      title = profileState.when(
          data: (data) => data?.nickname ?? "None",
          error: (error, stack) => "None",
          loading: () => "None");
    }
    return buildTitle(title);
  }

  Widget buildGroupTitle() {
    final AsyncValue<Union> unionState = ref.watch(unionStateProvider);
    final groupState = ref.watch(groupProfileStateProvider.call(widget.id));
    String title = unionState.when(
        data: (data) =>
            data.groups.where((it) => it.id == widget.id).firstOrNull?.title ??
            "",
        error: (error, stack) => "",
        loading: () => "");
    if (title == "") {
      title = groupState.when(
          data: (data) => data?.title ?? "None",
          error: (error, stack) => "None",
          loading: () => "None");
    }
    return buildTitle(title);
  }

  Widget buildTitle(String title) {
    final titleStyle = TextStyle(
        fontSize: 24,
        fontWeight: FontWeight.w800,
        color: Theme.of(context).colorScheme.primary);
    final AsyncValue<List<Message>> messagesState =
        ref.watch(messageStateProvider);
    Message? last = messagesState.when(
        data: (data) =>
            MessagesUtil.lastMessageByTypeAndId(data, widget.type, widget.id),
        error: (error, stack) => null,
        loading: () => null);
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: titleStyle,
        ),
        buildNews(last),
      ],
    );
  }

  Widget buildNews(Message? message) {
    if (message != null) {
      final id = message.from;
      final unionState = ref.watch(unionStateProvider);
      final userProfileState = ref.watch(userProfileStateProvider.call(id));
      String value = "";
      unionState.when(
        data: (data) => value =
            data.friends.where((it) => it.id == id).firstOrNull?.title ?? "",
        error: (err, stack) => value = "",
        loading: () => value = "",
      );
      if (value == "") {
        userProfileState.when(
          data: (data) => value = data?.nickname ?? "None",
          error: (err, stack) => value = "None",
          loading: () => value = "None",
        );
      }
      return Text("$value:${message.content.value}");
    } else {
      return const SizedBox();
    }
  }

  Widget buildTips() {
    final AsyncValue<List<Message>> messagesState =
        ref.watch(messageStateProvider);
    return messagesState.when(
        data: (data) {
          var num = MessagesUtil.getUnreadNumberByTypeAndId(data, widget.type,
              widget.id, Global.appStore.profile?.userId ?? -1);
          return num != 0 ? Text(num.toString()) : const SizedBox();
        },
        error: (error, stack) => const Text("error"),
        loading: () => const SizedBox());
  }

  void onTap() {
    Navigator.pushNamed(context, "/chat",
        arguments: Pair(first: widget.type, second: widget.id));
  }
}
