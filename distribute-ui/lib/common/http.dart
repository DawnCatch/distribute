import 'package:dio/dio.dart';
import 'package:distribute/common/env.dart';
import 'package:distribute/common/global.dart';
import 'package:distribute/common/result.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

class Http {
  static Dio? dio;

  static String httpBaseUrl =
      "http${Env.envConfig.isSecurity ? 's' : ''}://${Env.envConfig.ip}${Env.envConfig.port != null ? ":${Env.envConfig.port}" : ''}";

  static String socketBaseUrl =
      "ws${Env.envConfig.isSecurity ? 's' : ''}://${Env.envConfig.ip}${Env.envConfig.port != null ? ":${Env.envConfig.port}" : ''}";

  static Dio getInstance() {
    dio ??= Dio();
    return dio!;
  }

  static Future<dynamic> post(String url, Map<String, dynamic> data) async {
    final authorization = Global.appStore.authorization ?? "";
    try {
      Response res = await getInstance().post(httpBaseUrl + url,
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
      Response res = await getInstance().get(httpBaseUrl + url,
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

  static Stream socket(String url) {
    final authorization = Global.appStore.authorization ?? "";
    final channel = WebSocketChannel.connect(Uri.parse(socketBaseUrl + url),
        protocols: [authorization]);
    return channel.stream;
  }
}
