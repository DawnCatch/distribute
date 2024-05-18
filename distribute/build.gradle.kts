import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "com.zhou03"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework:spring-jdbc:6.1.3")
    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("org.ktorm:ktorm-jackson:3.6.0")

    implementation("io.jsonwebtoken:jjwt:0.12.5")
    implementation("joda-time:joda-time:2.12.7")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("io.github.deersunny:netty-socketio-spring-boot-starter:1.0.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.coobird:thumbnailator:0.4.8")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
