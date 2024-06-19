import 'package:json_annotation/json_annotation.dart';

part 'relation.g.dart';

@JsonSerializable()
class Relation {
  Relation();

  late bool type;
  late num id;
  late String title;
  late String path;
  
  factory Relation.fromJson(Map<String,dynamic> json) => _$RelationFromJson(json);
  Map<String, dynamic> toJson() => _$RelationToJson(this);
}
