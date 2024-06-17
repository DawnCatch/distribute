import 'dart:ui';

import 'package:distribute/domains/profile.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:provider/provider.dart';

import '../../common/global.dart';

class HomeDrawer extends StatefulWidget {
  const HomeDrawer({super.key});

  @override
  State<StatefulWidget> createState() => _HomeDrawerState();
}

class _HomeDrawerState extends State<HomeDrawer>
    with SingleTickerProviderStateMixin {
  static const Duration animationDuration = Duration(milliseconds: 200);

  late bool _isExpanded;

  @override
  void initState() {
    super.initState();

    _isExpanded = false;
  }

  void _onTapByDetail() {
    setState(() {
      _isExpanded = !_isExpanded;
    });
  }

  @override
  Widget build(BuildContext context) {
    double containerWidth = MediaQuery.of(context).size.width * .8;
    containerWidth = containerWidth > 384 ? 384 : containerWidth;
    return Container(
      width: containerWidth,
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.surfaceContainer,
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          Container(
            color: Theme.of(context).colorScheme.onSecondary,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Column(
              children: [
                SizedBox(height: MediaQuery.of(context).padding.top),
                Container(
                  padding: const EdgeInsets.only(top: 8, bottom: 4),
                  child: const Row(
                    children: [
                      CircleAvatar(
                          radius: 32,
                          backgroundImage: NetworkImage(
                              "https://files.catbox.moe/nizk28.jpg"))
                    ],
                  ),
                ),
                InkWell(
                  onTap: _onTapByDetail,
                  child: Container(
                      padding: const EdgeInsets.symmetric(vertical: 4),
                      child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Consumer<ProfileChangeNotifier>(
                                builder: (context, provider, child) {
                              return Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    provider.value.nickname ?? "未登录",
                                    style: const TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.w900),
                                  ),
                                  Text(
                                      provider.value.userId?.toString() ??
                                          "-1",
                                      style: TextStyle(
                                          color: Theme.of(context)
                                              .colorScheme
                                              .primary,
                                          fontSize: 12,
                                          fontWeight: FontWeight.w300))
                                ],
                              );
                            }),
                            AnimatedRotation(
                              turns: _isExpanded ? -.5 : 0,
                              duration: animationDuration,
                              child: const Icon(Icons.keyboard_arrow_down,
                                  size: 32),
                            )
                          ])),
                )
              ],
            ),
          ),
          ClipRect(
            child: AnimatedAlign(
                heightFactor: _isExpanded ? 1.0 : 0.0,
                alignment: Alignment.center,
                duration: animationDuration,
                child: const Column(
                  children: [Text("data")],
                )),
          ),
          const Text("data")
        ],
      ),
    );
  }
}
