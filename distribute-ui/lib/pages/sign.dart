import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:distribute/stores/own.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class SignPage extends ConsumerStatefulWidget {
  const SignPage({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _SignPageState();
}

class _SignPageState extends ConsumerState<SignPage> {
  late SignFormDate _signFormDate;

  @override
  void initState() {
    super.initState();
    // _reconnect();
    _signFormDate = SignFormDate();
  }

  @override
  Widget build(BuildContext context) {
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
      Result<UserProfile> result = Result.fromJson(res, UserProfile.fromJson);
      if (result.status == true) {
        ref.read(ownStateProvider.notifier).set(result.data);
        Navigator.pushReplacementNamed(context, "/home");
      }
    });
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
