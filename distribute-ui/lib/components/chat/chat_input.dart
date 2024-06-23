import 'package:animated_icon_button/animated_icon_button.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/stores/message.dart';
import 'package:flutter/material.dart';

class ChatInput extends StatefulWidget {
  const ChatInput({super.key});

  @override
  State<StatefulWidget> createState() => _ChatInputState();
}

class _ChatInputState extends State<ChatInput> with TickerProviderStateMixin {
  late TextEditingController _inputController;
  late FocusNode _inputFocusNode;

  late AnimationController _leadController;

  late bool _mode;

  @override
  void initState() {
    super.initState();
    _inputController = TextEditingController();
    _inputFocusNode = FocusNode();

    _leadController = AnimationController(
        duration: const Duration(milliseconds: 500), vsync: this);

    _mode = true;

    _inputFocusNode.addListener(() {
      if (_inputFocusNode.hasFocus) {
        _leadController.reverse();
      }
    });
  }

  void leadAnimated() {
    if (_leadController.isCompleted) {
      _leadController.forward();
    } else {
      _leadController.reverse();
    }
  }

  void onLeadPressed() {
    if (_leadController.isCompleted) {
      FocusScope.of(context).requestFocus(_inputFocusNode);
      _leadController.forward();
      //打开表情输入
    } else {
      _inputFocusNode.unfocus();
      _leadController.reverse();
    }
  }

  @override
  Widget build(BuildContext context) {
    final args = ModalRoute.of(context)!.settings.arguments as Pair<bool, num>;
    return Container(
      color: Theme.of(context).colorScheme.onSecondary,
      child: Row(
        children: [
          AnimatedIconButton(
            animationController: _leadController,
            onPressed: onLeadPressed,
            icons: const [
              AnimatedIconItem(icon: Icon(Icons.tag_faces)),
              AnimatedIconItem(icon: Icon(Icons.keyboard)),
            ],
          ),
          Expanded(
            child: TextField(
              focusNode: _inputFocusNode,
              controller: _inputController,
              style: TextStyle(
                  fontSize: 16, color: Theme.of(context).colorScheme.secondary),
              maxLines: 6,
              minLines: 1,
              onChanged: (text) {
                setState(() {});
              },
              onSubmitted: (text) {
                if (args.first) {
                  Http.post("/message/group/send", {
                    "to": args.second,
                    "content": {"type": "TEXT", "value": text}
                  });
                } else {
                  Http.post("/message/user/send", {
                    "to": args.second,
                    "content": {"type": "TEXT", "value": text}
                  });
                }
                _inputController.clear();
              },
              textInputAction: TextInputAction.done,
              decoration: InputDecoration(
                border: InputBorder.none,
                fillColor: Theme.of(context).colorScheme.onSecondary,
                filled: true,
                isCollapsed: true,
                contentPadding:
                    const EdgeInsets.symmetric(horizontal: 8, vertical: 10),
              ),
            ),
          ),
          IconButton(
            onPressed: () {},
            icon: Icon(
              Icons.attach_file,
              color: Theme.of(context).colorScheme.primary,
            ),
            iconSize: 28,
          ),
        ],
      ),
    );
  }
}
