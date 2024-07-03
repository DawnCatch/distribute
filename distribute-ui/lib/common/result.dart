class Result<T> {
  Result();

  late bool status;
  late String message;
  T? data;

  factory Result.fromJson(Map<String, dynamic> json,
          T Function(Map<String, dynamic>)? fromJson) =>
      Result()
        ..status = json["status"] as bool
        ..message = json["message"] as String
        ..data = (json["status"] as bool) && fromJson != null
            ? fromJson(json["data"])
            : null;

  factory Result.fromJsonT(
          Map<String, dynamic> json, T Function(dynamic) fromJson) =>
      Result()
        ..status = json["status"] as bool
        ..message = json["message"] as String
        ..data = (json["status"] as bool) ? fromJson(json["data"]) : null;

  Map<String, dynamic> toJson() => <String, dynamic>{
        "status": this.status,
        "message": this.message,
        "data": this.data
      };
}
