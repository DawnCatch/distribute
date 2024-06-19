// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'store.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Store _$StoreFromJson(Map<String, dynamic> json) => Store()
  ..profile = json['profile'] == null
      ? null
      : Profile.fromJson(json['profile'] as Map<String, dynamic>)
  ..union = json['union'] == null
      ? null
      : Union.fromJson(json['union'] as Map<String, dynamic>)
  ..profiles = (json['profiles'] as List<dynamic>?)
      ?.map((e) => Profile.fromJson(e as Map<String, dynamic>))
      .toList()
  ..authorization = json['authorization'] as String?;

Map<String, dynamic> _$StoreToJson(Store instance) => <String, dynamic>{
      'profile': instance.profile,
      'union': instance.union,
      'profiles': instance.profiles,
      'authorization': instance.authorization,
    };
