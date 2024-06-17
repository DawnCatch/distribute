import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/domains/authorization.dart';
import 'package:distribute/models/profile.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

import '../main.dart';

class SplashPage extends StatefulWidget {
  const SplashPage({super.key});

  @override
  State<StatefulWidget> createState() => SplashPageState();
}

class SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    _reconnect();
    super.initState();
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
    if (AuthorizationChangeNotifier().haveCookies) {
      Http.get("/user/reconnect").then(
        (res) {
          Result<Profile> result =
              Result.fromJson(res, (json) => Profile.fromJson(json));
          Future.delayed(const Duration(milliseconds: 300), () {
            if (result.status == true) {
              Global.appStore.profile = result.data;
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
