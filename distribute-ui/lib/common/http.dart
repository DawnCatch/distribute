import 'package:dio/dio.dart';
import 'package:distribute/common/env.dart';
import 'package:distribute/common/global.dart';
import 'package:distribute/common/result.dart';

class Http {
  static Dio? dio;

  static String baseUrl = Env.envConfig.appDomain;

  static Dio getInstance() {
    dio ??= Dio();
    return dio!;
  }

  static Future<dynamic> post(String url, Map<String, dynamic> data) async {
    final authorization = Global.appStore.authorization ?? "";
    try {
      Response res = await getInstance().post(baseUrl + url,
          data: data,
          options: Options(headers: {"authorization": authorization}));
      Global.appStore.authorization =
          res.headers.map["authorization"]?[0] ?? "";
      Global.save();
      return res.data;
    } catch (e) {
      Result result = Result()
        ..status = false
        ..message = "";
      return result.toJson();
    }
  }

  static Future<dynamic> get(String url) async {
    final authorization = Global.appStore.authorization ?? "";
    try {
      Response res = await getInstance().get(baseUrl + url,
          options: Options(headers: {"authorization": authorization}));
      Global.appStore.authorization =
          res.headers.map["authorization"]?[0] ?? "";
      Global.save();
      return res.data;
    } catch (e) {
      Result result = Result()
        ..status = false
        ..message = "";
      return result.toJson();
    }
  }
}
