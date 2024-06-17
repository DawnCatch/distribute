import 'package:dio/dio.dart';
import 'package:distribute/domains/authorization.dart';


class Http {
  static Dio? dio;

  static String baseUrl = "http://192.168.0.102:7896";

  static Dio getInstance() {
    dio ??= Dio();
    return dio!;
  }

  static Future<dynamic> post(String url, Map<String, dynamic> data) async {
    final authorizationChangeNotifier = AuthorizationChangeNotifier();
    Response res = await getInstance().post(baseUrl + url,
        data: data,
        options: Options(
            headers: {"authorization": authorizationChangeNotifier.value}));
    authorizationChangeNotifier.value =
        res.headers.map["authorization"]?[0] ?? "";
    // Result result = Result.fromJson(res.data);
    return res.data;
  }

  static Future<dynamic> get(String url) async {
    final authorizationChangeNotifier = AuthorizationChangeNotifier();
    Response res = await getInstance().get(baseUrl + url,
        options: Options(
            headers: {"authorization": authorizationChangeNotifier.value}));
    authorizationChangeNotifier.value =
        res.headers.map["authorization"]?[0] ?? "";
    // Result result = Result.fromJson(res.data);
    return res.data;
  }
}
