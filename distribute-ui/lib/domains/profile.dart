import 'package:distribute/common/global.dart';
import 'package:distribute/domains/store.dart';
import 'package:distribute/models/index.dart';

import '../models/profile.dart';

class ProfileChangeNotifier extends AppStoreChangeNotifier {
  Profile get value => Global.appStore.profile ?? Profile();

  bool get isLogin => value.userId != null;

  set value(Profile profile) {
    if (profile.hashCode != appStore.profile.hashCode) {
      appStore.profile = profile;
      notifyListeners();
    }
  }
}