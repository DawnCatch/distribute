import 'package:json_annotation/json_annotation.dart';

part 'profile.g.dart';

@JsonSerializable()
class UserProfile {
  UserProfile();

  late num userId;
  late String nickname;

  factory UserProfile.fromJson(Map<String, dynamic> json) =>
      _$UserProfileFromJson(json);

  Map<String, dynamic> toJson() => _$UserProfileToJson(this);
}

@JsonSerializable()
class GroupProfile {
  GroupProfile();

  late num groupId;
  late String title;
  late bool visible;
  late num createDate;

  factory GroupProfile.fromJson(Map<String, dynamic> json) =>
      _$GroupProfileFromJson(json);

  Map<String, dynamic> toJson() => _$GroupProfileToJson(this);
}
