import 'package:distribute/components/chat/chat_controller.dart';
import 'package:distribute/components/chat/chat_group.dart';
import 'package:distribute/components/chat/chat_input.dart';
import 'package:distribute/stores/group_profile.dart';
import 'package:distribute/stores/message.dart';
import 'package:distribute/stores/union.dart';
import 'package:distribute/stores/user_profile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ChatPage extends StatefulWidget {
  const ChatPage({super.key});

  @override
  State<StatefulWidget> createState() => _ChatPageState();
}

class _ChatPageState extends State<ChatPage> {
  late ChatController _controller;

  @override
  void initState() {
    super.initState();
    _controller = ChatController();
  }

  @override
  Widget build(BuildContext context) {
    final args = ModalRoute.of(context)!.settings.arguments as Pair<bool, num>;
    return Scaffold(
      appBar: AppBar(
        surfaceTintColor: Theme.of(context).colorScheme.onSecondary,
        foregroundColor: Theme.of(context).colorScheme.onSurfaceVariant,
        backgroundColor: Theme.of(context).colorScheme.surfaceBright,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        title: Consumer(
          builder: (context, ref, child) {
            final relations = ref.watch(unionStateProvider.select(
                (it) => args.first ? it.value?.groups : it.value?.friends));
            String title = relations
                    ?.where((it) => it.id == args.second)
                    .firstOrNull
                    ?.title ??
                "";
            if (args.first) {
              title = ref.watch(groupProfileStateProvider
                  .call(args.second)
                  .select((it) => it.value?.title ?? "None"));
            } else {
              title = ref.watch(userProfileStateProvider
                  .call(args.second)
                  .select((it) => it.value?.nickname ?? "None"));
            }
            if (relations == null) {
              return const Text("chat");
            } else {
              return Row(
                children: [
                  const CircleAvatar(
                    radius: 22,
                    backgroundImage:
                        NetworkImage("https://files.catbox.moe/nizk28.jpg"),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left: 16),
                    child: Text(title),
                  )
                ],
              );
            }
          },
        ),
        actions: <Widget>[
          IconButton(
            icon: const Icon(Icons.more_horiz),
            onPressed: () {},
          )
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: buildBody(),
          ),
          const ChatInput(),
        ],
      ),
    );
  }

  Widget buildBody() {
    final args = ModalRoute.of(context)!.settings.arguments as Pair<bool, num>;
    return Consumer(
      builder: (context, ref, child) {
        final messageState = ref.watch(messageStateProvider);
        return messageState.when(
          data: (data) {
            final group = MessagesUtil.test(data, args.first, args.second);
            final children = <Widget>[];
            group.forEach((key, value) {
              children.add(
                ChatGroup(
                  key: ValueKey(value.hashCode),
                  controller: _controller,
                  date: key,
                  map: value,
                ),
              );
            });
            return ListView(
              reverse: true,
              controller: _controller,
              children: children,
            );
          },
          error: (error, stack) => const Center(
            child: Text("error"),
          ),
          loading: () => const Center(
            child: CircularProgressIndicator(),
          ),
        );
      },
    );
  }
}
