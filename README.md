# 后端
## 目录结构
```
|-- distribute
    |-- .gitignore
    |-- build.gradle.kts
    |-- gradlew
    |-- gradlew.bat
    |-- settings.gradle.kts
    |-- .idea
    |-- gradle
    |   |-- wrapper
    |       |-- gradle-wrapper.jar
    |       |-- gradle-wrapper.properties
    |-- src
        |-- main
            |-- kotlin
            |   |-- com
            |       |-- zhou03
            |           |-- distribute
            |               |-- DistributeApplication.kt
            |               |-- adapter    //数据转换器
            |               |-- configuration    //spring配置
            |               |-- controller    //控制层
            |               |-- dao    //数据库操作
            |               |-- domain    //数据库模型
            |               |-- dto    //不同层交互的数据模型
            |               |-- interceptor    //拦截器
            |               |-- model    //通用请求模型
            |               |-- service    //业务层(服务层)
            |               |-- util    //工具方法
            |               |-- vo    //前端数据结构
            |-- resources
                |-- application-dev.properties    //开发环境
                |-- application-prop.properties    //测试环境
                |-- application-release.properties    //发布环境
                |-- application.properties
                |-- distribute.dbml    //数据库建模文件
```
