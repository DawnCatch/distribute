import "package:distribute/models/index.dart";
import 'package:json_annotation/json_annotation.dart';

part 'store.g.dart';

@JsonSerializable()
class Store {
  Store();

  Profile? profile;
  Union? union;
  List<Profile>? profiles;
  String? authorization;

  factory Store.fromJson(Map<String, dynamic> json) => _$StoreFromJson(json);

  Map<String, dynamic> toJson() => _$StoreToJson(this);
}
