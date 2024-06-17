import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/components/home/home_drawer.dart';
import 'package:distribute/domains/authorization.dart';
import 'package:distribute/domains/profile.dart';
import 'package:distribute/models/index.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

class SignPage extends StatefulWidget {
  const SignPage({super.key});

  @override
  State<StatefulWidget> createState() => _SignPageState();
}

class _SignPageState extends State<SignPage> {
  late ProfileChangeNotifier profileChangeNotifier;

  late SignFormDate _signFormDate;

  @override
  void initState() {
    super.initState();
    // _reconnect();
    _signFormDate = SignFormDate();
  }

  @override
  Widget build(BuildContext context) {
    profileChangeNotifier = Provider.of<ProfileChangeNotifier>(context);
    double maxHeight = MediaQuery.of(context).size.height;
    return Scaffold(
        body: Container(
            height: maxHeight,
            padding: EdgeInsets.only(
                top: MediaQuery.of(context).padding.top, left: 16, right: 16),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Padding(
                  padding: EdgeInsets.only(bottom: 24),
                  child: Text(
                    "登录",
                    style: TextStyle(fontSize: 32),
                  ),
                ),
                TextField(
                    onChanged: (value) => setState(() {
                          _signFormDate.username = value;
                        }),
                    decoration: const InputDecoration(
                      labelText: "Enter your username",
                      border: OutlineInputBorder(),
                    )),
                const SizedBox(height: 16),
                TextField(
                  onChanged: (value) => setState(() {
                    _signFormDate.password = value;
                  }),
                  decoration: const InputDecoration(
                    labelText: "Enter account's password",
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.visiblePassword,
                ),
                const SizedBox(height: 32),
                IconButton(
                    style: ButtonStyle(
                      backgroundColor: WidgetStateProperty.all<Color>(
                          Theme.of(context).colorScheme.onSecondary),
                      padding: WidgetStateProperty.all<EdgeInsets>(
                          const EdgeInsets.symmetric(
                              horizontal: 8, vertical: 8)),
                      textStyle: WidgetStateProperty.all<TextStyle>(
                        const TextStyle(fontSize: 16),
                      ),
                      shape: WidgetStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(56),
                        ),
                      ),
                    ),
                    onPressed: _signFormDate.validate() ? _signIn : null,
                    icon: const Icon(
                      Icons.keyboard_arrow_right,
                      size: 48,
                    ))
              ],
            )));
  }

  void _signIn() {
    Http.post("/user/login", _signFormDate.toJson()).then((res) {
      Result<Profile> result =
          Result.fromJson(res, (json) => Profile.fromJson(json));
      if (result.status == true) {
        profileChangeNotifier.value = result.data ?? Profile();
      }
      Navigator.pushReplacementNamed(context, "/home");
    });
  }

  void _reconnect() {
    if (AuthorizationChangeNotifier().haveCookies) {
      Http.get("/user/reconnect").then((res) {
        Result<Profile> result =
            Result.fromJson(res, (json) => Profile.fromJson(json));
        if (result.status == true) {
          Global.appStore.profile = result.data;
        }
        Navigator.pushReplacementNamed(context, "/home");
      });
    }
  }
}

class SignFormDate {
  late String username;
  late String password;

  SignFormDate() {
    init();
  }

  void init() {
    username = "";
    password = "";
  }

  bool validate() => username != "" && password != "";

  Map<String, dynamic> toJson() =>
      <String, dynamic>{"username": username, "password": password};
}
