import 'package:distribute/pages/Test.dart';
import 'package:distribute/pages/chat.dart';
import 'package:distribute/pages/sign.dart';
import 'package:distribute/pages/splash.dart';
import 'package:flutter/material.dart';

import '../pages/home.dart';

Map<String, WidgetBuilder> routes = {
  "/splash": (_) => const SplashPage(),
  "/sign": (_) => const SignPage(),
  "/home": (_) => const HomePage(),
  "/chat": (_) => const ChatPage()
  // "home": (_) => const TestPage(),
};
