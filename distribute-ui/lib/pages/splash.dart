import 'package:distribute/stores/own.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../common/env.dart';

class SplashPage extends ConsumerStatefulWidget {
  const SplashPage({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => SplashPageState();
}

class SplashPageState extends ConsumerState<SplashPage> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    _reconnect();
    double width = MediaQuery.of(context).size.width * 0.8;
    return Material(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          SizedBox(
            width: width,
            height: width,
            child: SvgPicture.asset("assets/logo.svg"),
          ),
          const Text(
            "distribute",
            style: TextStyle(fontSize: 48),
          )
        ],
      ),
    );
  }

  void _reconnect() {
    final ownState = ref.watch(ownStateProvider);
    ownState.when(
      data: (data) {
        Future.delayed(const Duration(milliseconds: 300), () {
          if (data == null) {
            Navigator.pushReplacementNamed(context, "/sign");
          } else {
            Navigator.pushReplacementNamed(context, "/home");
          }
        });
      },
      error: (error, stack) {
        Future.delayed(const Duration(milliseconds: 300), () {
          Navigator.pushReplacementNamed(context, "/sign");
        });
      },
      loading: () {},
    );
  }
}
