import 'package:json_annotation/json_annotation.dart';

@JsonSerializable()
class Result<T> {
  Result();

  bool? status;
  String? message;
  T? data;

  factory Result.fromJson(Map<String, dynamic> json,
      T Function(Map<String, dynamic>) fromJson) =>
      Result()
        ..status = json["status"] as bool
        ..message = json["message"] as String
        ..data = fromJson(json["data"]);

  Map<String, dynamic> toJson() => <String, dynamic>{
    "status": this.status,
    "message": this.message,
    "data": this.data
  };
}
