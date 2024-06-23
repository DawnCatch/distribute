import 'package:distribute/common/global.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "own.g.dart";

@riverpod
class OwnState extends _$OwnState {
  static UserProfile? profile;

  @override
  Future<UserProfile?> build() async {
    if (profile != null) return profile;
    dynamic response = await Http.get("/user/reconnect");
    Result<UserProfile> unionResult =
        Result.fromJson(response, UserProfile.fromJson);
    profile = unionResult.data;
    Global.appStore.profile = profile;
    return profile;
  }

  void set(UserProfile? profile) {
    if (profile == null) return;
    state = AsyncValue.data(profile);
  }
}
