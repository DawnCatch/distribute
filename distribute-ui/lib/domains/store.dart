import 'package:flutter/material.dart';

import '../common/global.dart';
import '../models/store.dart';

class AppStoreChangeNotifier extends ChangeNotifier {
  Store get appStore => Global.appStore;

  @override
  void notifyListeners() {
    Global.save();
    super.notifyListeners();
  }
}