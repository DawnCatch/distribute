import 'package:distribute/components/chat/chat_controller.dart';
import 'package:distribute/components/chat/chat_group_item.dart';
import 'package:distribute/models/message.dart';
import 'package:flutter/cupertino.dart';

class ChatGroup extends StatefulWidget {
  const ChatGroup({
    super.key,
    required this.controller,
    required this.map,
  });

  final ChatController controller;
  final List<List<Message>> map;

  @override
  State<StatefulWidget> createState() => _ChatGroupState();
}

class _ChatGroupState extends State<ChatGroup> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final children = <Widget>[];
    num id = -1;
    for (int i = 0; i < widget.map.length; i++) {
      List<Message> messages = widget.map[i];
      if (id == -1 && messages.firstOrNull != null) {
        id = messages.first.id - 0.5;
      }
      children.add(
        ChatGroupItem(
          controller: widget.controller,
          messages: messages,
        ),
      );
    }
    widget.controller.updateHeight(id, 16);
    return Column(
      children: <Widget>[
            SizedBox(
              height: 16,
              child: Center(
                child: Text(getDate()),
              ),
            ),
          ] +
          children,
    );
  }

  String getDate() {
    final milliseconds = widget.map.firstOrNull?.firstOrNull?.date ?? 0;
    final date = DateTime.fromMillisecondsSinceEpoch(milliseconds.toInt()).toString();
    return date.substring(0,date.length - 7);
  }
}
