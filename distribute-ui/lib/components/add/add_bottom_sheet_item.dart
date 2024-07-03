import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/search_item.dart';
import 'package:distribute/stores/group_profile.dart';
import 'package:distribute/stores/message.dart';
import 'package:distribute/stores/union.dart';
import 'package:distribute/stores/user_profile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class AddBottomSheetItem extends ConsumerStatefulWidget {
  const AddBottomSheetItem(this.item, {super.key});

  final SearchItem item;

  @override
  ConsumerState<ConsumerStatefulWidget> createState() =>
      _AddBottomSheetItemState();
}

class _AddBottomSheetItemState extends ConsumerState<AddBottomSheetItem> {
  late String _mode;

  @override
  void initState() {
    _mode = "关注";
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final unionState = ref.watch(unionStateProvider);
    String title = "";
    if (widget.item.type) {
      title = ref.watch(groupProfileStateProvider
          .call(widget.item.id)
          .select((it) => it.value?.title ?? "None"));
    } else {
      title = ref.watch(userProfileStateProvider
          .call(widget.item.id)
          .select((it) => it.value?.nickname ?? "None"));
    }
    unionState.when(
      data: (data) {
        if (widget.item.type) {
          bool inGroups = data.inGroups(widget.item.id);
          if (inGroups) {
            _mode = "发消息";
          } else {
            _mode = "申请";
          }
        } else {
          bool inFollows = data.inFollows(widget.item.id);
          bool inFans = data.inFans(widget.item.id);
          if (inFans && inFollows) {
            _mode = "发消息";
          } else if (inFans) {
            _mode = "回关";
          } else if (inFollows) {
            _mode = "取关";
          }
        }
        setState(() {});
      },
      error: (error, stack) {},
      loading: () {},
    );

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            children: [
              const CircleAvatar(
                radius: 24,
                backgroundImage:
                    NetworkImage("https://files.catbox.moe/nizk28.jpg"),
              ),
              const SizedBox(
                width: 16,
              ),
              Text(title),
            ],
          ),
          TextButton(
            style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(
                    Theme.of(context).colorScheme.surface)),
            onPressed: onPressed,
            child: Text(_mode),
          )
        ],
      ),
    );
  }

  void onPressed() {
    if (_mode == "关注" || _mode == "回关" || _mode == "取关") {
      Http.post(
        "/relation/user/follow",
        {
          "targetId": widget.item.id,
        },
      ).then((res) {
        Result result = Result.fromJson(res, null);
        final unionState = ref.read(unionStateProvider.notifier);
        if (result.message == "关注成功") {
          unionState.addFollows(widget.item.id);
        } else if (result.message == "取消关注") {
          unionState.removeFollows(widget.item.id);
        }
      });
    } else if (_mode == "发消息") {
      Navigator.pushNamed(
        context,
        "/chat",
        arguments: Pair(first: widget.item.type, second: widget.item.id),
      );
    }
  }
}
