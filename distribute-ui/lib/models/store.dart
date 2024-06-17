import 'package:json_annotation/json_annotation.dart';
import "profile.dart";
import "message.dart";
import "relation.dart";
part 'store.g.dart';

@JsonSerializable()
class Store {
  Store();

  Profile? profile;
  List<Message>? messages;
  List<Relation>? follows;
  List? fans;
  List<Relation>? groups;
  List<Relation>? applications;
  List<Profile>? profiles;
  String? authorization;
  
  factory Store.fromJson(Map<String,dynamic> json) => _$StoreFromJson(json);
  Map<String, dynamic> toJson() => _$StoreToJson(this);
}
