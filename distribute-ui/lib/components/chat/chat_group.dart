import 'package:distribute/components/chat/chat_controller.dart';
import 'package:distribute/components/chat/chat_group_item.dart';
import 'package:distribute/models/message.dart';
import 'package:flutter/cupertino.dart';

class ChatGroup extends StatefulWidget {
  const ChatGroup({
    super.key,
    required this.controller,
    required this.date,
    required this.map,
  });

  final ChatController controller;
  final num date;
  final Map<num, List<Message>> map;

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
    widget.map.forEach((key, value) {
      children.add(
        ChatGroupItem(
          controller: widget.controller,
          messages: value,
        ),
      );
    });
    return Column(
      children: <Widget>[
            // Text(widget.date.toString()),
          ] +
          children,
    );
  }
}
