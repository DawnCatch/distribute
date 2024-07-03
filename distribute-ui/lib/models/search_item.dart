import 'package:json_annotation/json_annotation.dart';

part 'search_item.g.dart';

@JsonSerializable()
class SearchItem {
  SearchItem();

  late bool type;
  late num id;

  factory SearchItem.fromJson(Map<String,dynamic> json) => _$SearchItemFromJson(json);
  Map<String, dynamic> toJson() => _$SearchItemToJson(this);
}
