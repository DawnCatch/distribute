import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/profile.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "user_profile.g.dart";

@riverpod
class UserProfileState extends _$UserProfileState {
  static List<UserProfile> users = [];

  @override
  Future<UserProfile?> build(num id) async {
    for (int i = 0; i < users.length; i++) {
      if (users[i].userId == id) return users[i];
    }
    final response = await Http.get("/profile/get/$id");
    Result<UserProfile> result =
        Result.fromJson(response, UserProfile.fromJson);
    if (result.data != null) users.add(result.data!);
    return result.data;
  }
}
