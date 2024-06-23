// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'profile.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserProfile _$UserProfileFromJson(Map<String, dynamic> json) => UserProfile()
  ..userId = json['userId'] as num
  ..nickname = json['nickname'] as String;

Map<String, dynamic> _$UserProfileToJson(UserProfile instance) =>
    <String, dynamic>{
      'userId': instance.userId,
      'nickname': instance.nickname,
    };

GroupProfile _$GroupProfileFromJson(Map<String, dynamic> json) => GroupProfile()
  ..groupId = json['groupId'] as num
  ..title = json['title'] as String
  ..visible = json['visible'] as bool
  ..createDate = json['createDate'] as num;

Map<String, dynamic> _$GroupProfileToJson(GroupProfile instance) =>
    <String, dynamic>{
      'groupId': instance.groupId,
      'title': instance.title,
      'visible': instance.visible,
      'createDate': instance.createDate,
    };
