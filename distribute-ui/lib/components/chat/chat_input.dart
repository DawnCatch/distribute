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

  late bool _isExpanded;

  @override
  void initState() {
    super.initState();
    _inputController = TextEditingController();
    _inputFocusNode = FocusNode();

    _leadController = AnimationController(
        duration: const Duration(milliseconds: 500), vsync: this);

    _isExpanded = false;

    _inputFocusNode.addListener(() {
      if (_inputFocusNode.hasFocus) {
        _leadController.reverse();
        _isExpanded = false;
        setState(() {});
      }
    });
  }

  void onLeadPressed() {
    if (_leadController.isCompleted) {
      FocusScope.of(context).requestFocus(_inputFocusNode);
      _leadController.forward();
      _isExpanded = false;
    } else {
      _inputFocusNode.unfocus();
      _leadController.reverse();
      _isExpanded = true;
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    final args = ModalRoute.of(context)!.settings.arguments as Pair<bool, num>;
    return Container(
      color: Theme.of(context).colorScheme.onSecondary,
      child: Column(
        children: [
          Row(
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
                      fontSize: 16,
                      color: Theme.of(context).colorScheme.secondary),
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
          buildEmojiBox(),
        ],
      ),
    );
  }

  Widget buildEmojiBox() {
    final emoji = ["😀", "😀", "😀", "😀"];
    return ClipRect(
      child: AnimatedAlign(
        heightFactor: _isExpanded ? 1.0 : 0.0,
        alignment: Alignment.center,
        duration: _isExpanded
            ? const Duration(milliseconds: 40)
            : const Duration(milliseconds: 0),
        child: SizedBox(
          height: 240,
          child: GridView.count(
            crossAxisCount: 3,
            children: emoji.map((it) => Center(child: Text(it,style: TextStyle(fontSize: 24),))).toList(),
          ),
        ),
      ),
    );
  }
}
