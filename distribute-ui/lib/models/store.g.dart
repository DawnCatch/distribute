// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'store.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Store _$StoreFromJson(Map<String, dynamic> json) => Store()
  ..profile = json['profile'] == null
      ? null
      : Profile.fromJson(json['profile'] as Map<String, dynamic>)
  ..messages = (json['messages'] as List<dynamic>?)
      ?.map((e) => Message.fromJson(e as Map<String, dynamic>))
      .toList()
  ..follows = (json['follows'] as List<dynamic>?)
      ?.map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..fans = json['fans'] as List<dynamic>?
  ..groups = (json['groups'] as List<dynamic>?)
      ?.map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..applications = (json['applications'] as List<dynamic>?)
      ?.map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..profiles = (json['profiles'] as List<dynamic>?)
      ?.map((e) => Profile.fromJson(e as Map<String, dynamic>))
      .toList()
  ..authorization = json['authorization'] as String?;

Map<String, dynamic> _$StoreToJson(Store instance) => <String, dynamic>{
      'profile': instance.profile,
      'messages': instance.messages,
      'follows': instance.follows,
      'fans': instance.fans,
      'groups': instance.groups,
      'applications': instance.applications,
      'profiles': instance.profiles,
      'authorization': instance.authorization,
    };
