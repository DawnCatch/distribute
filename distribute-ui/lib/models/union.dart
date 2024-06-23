import 'package:json_annotation/json_annotation.dart';
import "relation.dart";

part 'union.g.dart';

@JsonSerializable()
class Union {
  Union() {
    follows = [];
    fans = [];
    groups = [];
    applications = [];
  }

  late List<Relation> follows;
  late List<num> fans;
  late List<Relation> groups;
  late List<Relation> applications;

  factory Union.fromJson(Map<String, dynamic> json) => _$UnionFromJson(json);

  Map<String, dynamic> toJson() => _$UnionToJson(this);

  List<Relation> get friends =>
      follows.where((item) => fans.contains(item.id)).toList();

  List<Relation> get relations => (friends + groups);

  List<String> get paths =>
      relations.map((it) => it.path).toSet().toList()..remove("/");

  List<List<String>> get tabs {
    final List<List<String>> tabs = [
      ["联系人", "群组"],
      ["联系人", "群组", "消息"]
    ];
    tabs[0] += paths.map((it) => it.replaceAll("/", "")).toList();
    return tabs;
  }
}
