import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/store.dart';

class Global {
  static late SharedPreferences _prefs;

  static Store appStore = Store();

  static bool get isRelease => const bool.fromEnvironment("dart.vm.product");

  static Future init() async {
    WidgetsFlutterBinding.ensureInitialized();
    _prefs = await SharedPreferences.getInstance();
    var appStore = _prefs.getString("app_store");
    if (appStore != null) {
      try {
        Global.appStore = Store.fromJson(jsonDecode(appStore));
      } catch (e) {
        _prefs.setString("app_store", jsonEncode(Global.appStore.toJson()));
      }
    }
  }

  static save() {
    _prefs.setString("app_store", jsonEncode(appStore.toJson()));
  }
}
