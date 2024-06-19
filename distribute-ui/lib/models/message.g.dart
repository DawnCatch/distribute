// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'message.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Message _$MessageFromJson(Map<String, dynamic> json) => Message()
  ..type = json['type'] as bool
  ..id = json['id'] as num
  ..from = json['from'] as num
  ..to = json['to'] as num
  ..content = Content.fromJson(json['content'] as Map<String, dynamic>)
  ..date = json['date'] as num
  ..observers = json['observers'] as List<dynamic>;

Map<String, dynamic> _$MessageToJson(Message instance) => <String, dynamic>{
      'type': instance.type,
      'id': instance.id,
      'from': instance.from,
      'to': instance.to,
      'content': instance.content,
      'date': instance.date,
      'observers': instance.observers,
    };
