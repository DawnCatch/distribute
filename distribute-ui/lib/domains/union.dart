import 'package:distribute/domains/store.dart';
import 'package:distribute/models/relation.dart';

class UnionChangeNotifier extends AppStoreChangeNotifier {
  List<Relation> get follows => appStore.follows ?? [];

  set follows(List<Relation> follows) {
    if (follows.hashCode != appStore.follows.hashCode) {
      appStore.follows = follows;
      notifyListeners();
    }
  }

  List<int> get fans => appStore.fans?.map((item) => item as int ).toList() ?? [];

  set fans(List<int> fans) {
    if (fans.hashCode != appStore.fans.hashCode) {
      appStore.fans = fans;
      notifyListeners();
    }
  }

  List<Relation> get groups => appStore.groups ?? [];

  set groups(List<Relation> groups) {
    if (groups.hashCode != appStore.groups.hashCode) {
      appStore.groups = groups;
      notifyListeners();
    }
  }

  List<Relation> get applications => appStore.applications ?? [];

  set applications(List<Relation> applications) {
    if (applications.hashCode != appStore.applications.hashCode) {
      appStore.applications = applications;
      notifyListeners();
    }
  }

  List<Relation> get friends => follows.where((item) => fans.contains(item.id)).toList();
}