import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "own.g.dart";

@riverpod
class OwnState extends _$OwnState {
  @override
  Future<Profile?> build() async {
    dynamic response = await Http.get("/user/reconnect");
    Result<Profile> unionResult = Result.fromJson(response, Profile.fromJson);
    if (unionResult.status == true && unionResult.data != null) {
      return unionResult.data!;
    }
    return null;
  }

  void set(Profile? profile) {
    if (profile == null) return;
    state = AsyncValue.data(profile);
  }
}
