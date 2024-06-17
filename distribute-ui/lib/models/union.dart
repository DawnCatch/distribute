import 'package:json_annotation/json_annotation.dart';
import "relation.dart";
part 'union.g.dart';

@JsonSerializable()
class Union {
  Union();

  late List<Relation> follows;
  late List fans;
  late List<Relation> groups;
  late List<Relation> applications;
  
  factory Union.fromJson(Map<String,dynamic> json) => _$UnionFromJson(json);
  Map<String, dynamic> toJson() => _$UnionToJson(this);
}
