import 'package:json_annotation/json_annotation.dart';

part 'relation.g.dart';

@JsonSerializable()
class Relation {
  Relation();

  bool? type;
  num? id;
  String? title;
  String? path;
  
  factory Relation.fromJson(Map<String,dynamic> json) => _$RelationFromJson(json);
  Map<String, dynamic> toJson() => _$RelationToJson(this);
}
