// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'union.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Union _$UnionFromJson(Map<String, dynamic> json) => Union()
  ..follows = (json['follows'] as List<dynamic>)
      .map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..fans = (json['fans'] as List<dynamic>).map((e) => e as num).toList()
  ..groups = (json['groups'] as List<dynamic>)
      .map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..applications = (json['applications'] as List<dynamic>)
      .map((e) => Relation.fromJson(e as Map<String, dynamic>))
      .toList();

Map<String, dynamic> _$UnionToJson(Union instance) => <String, dynamic>{
      'follows': instance.follows,
      'fans': instance.fans,
      'groups': instance.groups,
      'applications': instance.applications,
    };
