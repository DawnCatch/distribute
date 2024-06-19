import 'package:json_annotation/json_annotation.dart';
import "content.dart";
part 'message.g.dart';

@JsonSerializable()
class Message {
  Message();

  late bool type;
  late num id;
  late num from;
  late num to;
  late Content content;
  late num date;
  late List observers;
  
  factory Message.fromJson(Map<String,dynamic> json) => _$MessageFromJson(json);
  Map<String, dynamic> toJson() => _$MessageToJson(this);
}
