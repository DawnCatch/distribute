import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/profile.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "group_profile.g.dart";

@riverpod
class GroupProfileState extends _$GroupProfileState {
  List<GroupProfile> groups = [];

  @override
  Future<GroupProfile?> build(num id) async {
    for (int i = 0; i < groups.length; i++) {
      if (groups[i].groupId == id) return groups[i];
    }
    final response = await Http.get("/group/get/$id");
    Result<GroupProfile> result =
        Result.fromJson(response, GroupProfile.fromJson);
    if (result.data != null) groups.add(result.data!);
    return result.data;
  }
}
