// 环境配置
class EnvConfig {
  final String appDomain;

  EnvConfig({
    required this.appDomain,
  });
}

// 获取的配置信息
class Env {
  // 获取到当前环境
  static const appEnv = EnvName.dev;

  // 开发环境
  static final EnvConfig _devConfig = EnvConfig(
    appDomain: "http://119.23.142.66:7896",
  );

  // 发布环境
  static final EnvConfig _releaseConfig = EnvConfig(
    appDomain: "https://www.phase-app.shop",
  );

  static EnvConfig get envConfig => _getEnvConfig();

  static EnvConfig _getEnvConfig() {
    switch (appEnv) {
      case EnvName.dev:
        return _devConfig;
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
  static const String release = "release";
}
