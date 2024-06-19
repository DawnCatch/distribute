import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/profile.dart';
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
    _reconnect();
  }

  @override
  Widget build(BuildContext context) {
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
    final authorization = Global.appStore.authorization;
    if (authorization != null && authorization != "") {
      Http.get("/user/reconnect").then(
        (res) {
          Result<Profile> result = Result.fromJson(res, Profile.fromJson);
          if (result.status == true) {
            Global.appStore.profile = result.data;
            ref.read(ownStateProvider.notifier).set(result.data);
          }
          Future.delayed(const Duration(milliseconds: 300), () {
            if (result.status == true) {
              Navigator.pushReplacementNamed(context, "/home");
            } else {
              Navigator.pushReplacementNamed(context, "/sign");
            }
          });
        },
      );
    } else {
      Future.delayed(const Duration(milliseconds: 500), () {
        Navigator.pushReplacementNamed(context, "/sign");
      });
    }
  }
}
