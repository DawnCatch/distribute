import 'dart:async';

import 'package:after_layout/after_layout.dart';
import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/components/chat/chat_controller.dart';
import 'package:distribute/models/index.dart';
import 'package:distribute/models/message.dart';
import 'package:distribute/stores/message.dart';
import 'package:distribute/stores/union.dart';
import 'package:distribute/stores/user_profile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:visibility_detector/visibility_detector.dart';

class ChatGroupItem extends ConsumerStatefulWidget {
  const ChatGroupItem({
    super.key,
    required this.controller,
    required this.messages,
  });

  final ChatController controller;

  final List<Message> messages;

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _ChatGroupItem();
}

class _ChatGroupItem extends ConsumerState<ChatGroupItem>
    with AfterLayoutMixin<ChatGroupItem> {
  late GlobalKey _globalKey;

  late double _height;
  late double _bottom;

  late bool _type;
  late num _from;

  @override
  void initState() {
    super.initState();
    _type = widget.messages.first.type;
    _from = widget.messages.first.from;
    _height = 0;
    _bottom = 0;
    _globalKey = GlobalKey();
    widget.controller.addListener(() {
      if (mounted && _type) {
        var scrollOffset = widget.controller.offset;
        var offset = 0.0;
        final id = widget.messages.firstOrNull?.id;
        if (id != null) {
          widget.controller.heights.forEach((key, value) {
            if (key > id) offset += value;
          });
        }
        _bottom = scrollOffset - offset;
        if (_bottom > _height - 64) {
          _bottom = _height - 64;
        } else if (_bottom < 0) {
          _bottom = 0;
        }
        setState(() {});
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final children = <Widget>[];
    if (Global.appStore.profile?.userId != _from && _type) {
      children.add(buildAvatar());
      children.add(buildGroupChat());
    } else {
      children.add(buildUserChat());
    }
    return Stack(
      key: _globalKey,
      children: children,
    );
  }

  Widget buildAvatar() {
    return Positioned(
      bottom: _bottom,
      child: const Padding(
        padding: EdgeInsets.all(8),
        child: CircleAvatar(
          radius: 24,
          backgroundImage: NetworkImage("https://files.catbox.moe/nizk28.jpg"),
        ),
      ),
    );
  }

  Widget buildUserChat() {
    List<Widget> widgets = [
      Padding(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: Global.appStore.profile?.userId != _from
              ? CrossAxisAlignment.start
              : CrossAxisAlignment.end,
          children: buildMessage(),
        ),
      ),
    ];
    return Row(
      mainAxisAlignment: Global.appStore.profile?.userId != _from
          ? MainAxisAlignment.start
          : MainAxisAlignment.end,
      children: widgets,
    );
  }

  Widget buildGroupChat() {
    List<Widget> widgets = [
      Container(
        width: 64,
      ),
      Padding(
        padding: const EdgeInsets.symmetric(vertical: 4),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: buildMessage(),
        ),
      )
    ];
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: widgets,
    );
  }

  void visible(int id) {
    Message message = widget.messages[id];
    if (message.observers.contains(Global.appStore.profile?.userId)) return;
    Http.post("/message/read", {"id": message.id}).then((res) {
      Result result = Result.fromJson(res, (json) => null);
      if (result.status) {
        ref.read(messageStateProvider.notifier).updateObservers(id);
      }
    });
  }

  List<Widget> buildMessage() {
    final unionState = ref.watch(unionStateProvider);
    final profileState = ref.watch(userProfileStateProvider.call(_from));
    List<Widget> widgets = [];
    for (int i = 0; i < widget.messages.length; i++) {
      var index = 0;
      if (i == widget.messages.length - 1 && i == 0) {
        index = 1;
      } else if (i == widget.messages.length - 1) {
        index = 3;
      } else if (i != 0) {
        index = 2;
      }
      if (i == 0 && Global.appStore.profile?.userId != _from && _type) {
        String title = unionState.when(
            data: (data) =>
                data.groups.where((it) => it.id == _from).firstOrNull?.title ??
                "",
            error: (error, stack) => "",
            loading: () => "");
        if (title == "") {
          title = profileState.when(
              data: (data) => data?.nickname ?? "None",
              error: (error, stack) => "Error",
              loading: () => "Loading");
        }
        widgets.add(getCard(
            child: VisibilityDetector(
              key: ValueKey(widget.messages[i].id),
              onVisibilityChanged: (info) => visible(i),
              child: Container(
                padding: const EdgeInsets.all(8),
                height: 72,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: const TextStyle(fontWeight: FontWeight.w800),
                    ),
                    Text(
                      widget.messages[i].content.value,
                      style: const TextStyle(
                        fontSize: 24,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            index: index));
      } else {
        widgets.add(getCard(
            child: VisibilityDetector(
              key: ValueKey(widget.messages[i].id),
              onVisibilityChanged: (info) => visible(i),
              child: Padding(
                padding: const EdgeInsets.all(8),
                child: Text(
                  widget.messages[i].content.value ?? "",
                  style: const TextStyle(
                    fontSize: 24,
                  ),
                ),
              ),
            ),
            index: index));
      }
    }
    return widgets;
  }

  BorderRadius getBorderRadius(List<double> value, {bool reverse = false}) {
    return BorderRadius.only(
      topLeft: Radius.circular(reverse ? value[1] : value[0]),
      topRight: Radius.circular(reverse ? value[0] : value[1]),
      bottomRight: Radius.circular(reverse ? value[3] : value[2]),
      bottomLeft: Radius.circular(reverse ? value[2] : value[3]),
    );
  }

  Widget getCard({required Widget child, required int index}) {
    final reverse = Global.appStore.profile?.userId == _from;
    List<List<double>> mode = [
      [16, 16, 16, 8],
      [16, 16, 16, 2],
      [8, 16, 16, 8],
      [8, 16, 8, 2],
    ];
    return Card(
      shape: RoundedRectangleBorder(
        // borderRadius: BorderRadius.circular(8),
        borderRadius: getBorderRadius(mode[index], reverse: reverse),
      ),
      clipBehavior: Clip.antiAlias,
      child: child,
    );
  }

  @override
  FutureOr<void> afterFirstLayout(BuildContext context) {
    if (_globalKey.currentContext != null && mounted && _height == 0 && _type) {
      RenderBox renderBox =
          _globalKey.currentContext!.findRenderObject() as RenderBox;
      _height = renderBox.size.height;
      widget.controller.updateHeight(widget.messages.firstOrNull?.id, _height);
    }
  }
}
