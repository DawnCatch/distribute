// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'store.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Store _$StoreFromJson(Map<String, dynamic> json) => Store()
  ..profile = json['profile'] == null
      ? null
      : UserProfile.fromJson(json['profile'] as Map<String, dynamic>)
  ..union = json['union'] == null
      ? null
      : Union.fromJson(json['union'] as Map<String, dynamic>)
  ..authorization = json['authorization'] as String?;

Map<String, dynamic> _$StoreToJson(Store instance) => <String, dynamic>{
      'profile': instance.profile,
      'union': instance.union,
      'authorization': instance.authorization,
    };
