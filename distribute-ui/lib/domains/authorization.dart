import 'package:distribute/domains/store.dart';

import '../common/global.dart';

class AuthorizationChangeNotifier extends AppStoreChangeNotifier {
  String get value => Global.appStore.authorization ?? "";

  bool get haveCookies => value != "";

  set value(String authorization) {
    if (authorization != appStore.authorization) {
      appStore.authorization = authorization;
      notifyListeners();
    }
  }
}
