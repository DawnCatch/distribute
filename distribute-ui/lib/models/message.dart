import 'package:json_annotation/json_annotation.dart';
import "content.dart";
part 'message.g.dart';

@JsonSerializable()
class Message {
  Message();

  bool? type;
  num? id;
  num? from;
  num? to;
  Content? content;
  num? date;
  List? observers;
  
  factory Message.fromJson(Map<String,dynamic> json) => _$MessageFromJson(json);
  Map<String, dynamic> toJson() => _$MessageToJson(this);
}
