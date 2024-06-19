import 'package:distribute/models/index.dart';
import 'package:distribute/stores/union.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class HomeBodyRelationItem extends StatefulWidget {
  const HomeBodyRelationItem({super.key, required this.type, required this.id});

  final bool type;
  final num id;

  @override
  State<StatefulWidget> createState() => _HomeBodyRelationItemState();
}

class _HomeBodyRelationItemState extends State<HomeBodyRelationItem> {
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
                    NetworkImage("https://files.catbox.moe/nizk28.jpg")),
            const SizedBox(width: 16),
            Consumer(
              builder: (context, ref, child) {
                final AsyncValue<Union?> unionState =
                    ref.watch(unionStateProvider);
                return unionState.when(
                  data: (data) => Text(
                      (widget.type ? data!.groups : data!.friends)
                          .singleWhere((it) => it.id == widget.id)
                          .title),
                  error: (error, stack) => const Text("error"),
                  loading: () => const CircularProgressIndicator(),
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  void onTap() {
    Navigator.pushNamed(context, "/chat");
  }
}
