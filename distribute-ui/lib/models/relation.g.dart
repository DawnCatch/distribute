// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'relation.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Relation _$RelationFromJson(Map<String, dynamic> json) => Relation()
  ..type = json['type'] as bool?
  ..id = json['id'] as num?
  ..title = json['title'] as String?
  ..path = json['path'] as String?;

Map<String, dynamic> _$RelationToJson(Relation instance) => <String, dynamic>{
      'type': instance.type,
      'id': instance.id,
      'title': instance.title,
      'path': instance.path,
    };
