// 环境配置
class EnvConfig {
  final bool isSecurity;
  final String ip;
  final num? port;

  EnvConfig({required this.isSecurity, required this.ip, required this.port});
}

// 获取的配置信息
class Env {
  // 获取到当前环境
  static const appEnv = EnvName.debug;

  // 开发环境
  static final EnvConfig _devConfig = EnvConfig(
    isSecurity: false,
    ip: "192.168.0.102",
    port: 7896,
  );

  static final EnvConfig _debugConfig = EnvConfig(
    isSecurity: false,
    ip: "119.23.142.66",
    port: 7896,
  );

  // 发布环境
  static final EnvConfig _releaseConfig = EnvConfig(
    isSecurity: true,
    ip: "https://www.phase-app.shop",
    port: null,
  );

  static EnvConfig get envConfig => _getEnvConfig();

  static EnvConfig _getEnvConfig() {
    switch (appEnv) {
      case EnvName.dev:
        return _devConfig;
      case EnvName.debug:
        return _debugConfig;
      case EnvName.release:
        return _releaseConfig;
      default:
        return _devConfig;
    }
  }
}

// 声明的环境
class EnvName {
  static const String envKey = "APP_ENV";

  static const String dev = "dev";
  static const String debug = "debug";
  static const String release = "release";
}
