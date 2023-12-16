plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.allopen") version "1.9.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.0"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-container-image-docker")
    implementation("io.quarkus:quarkus-websockets")
    implementation("io.quarkus:quarkus-jacoco")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-spring-di")
    implementation("io.quarkus:quarkus-spring-web")
    implementation("io.quarkus:quarkus-agroal")
    implementation("io.quarkus:quarkus-spring-data-jpa")
    implementation("io.quarkiverse.jdbc:quarkus-jdbc-sqlite:3.0.7")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.6.2")
    testImplementation("net.datafaker:datafaker:1.8.1")
    implementation("com.github.oshi:oshi-core-java11:6.4.9")
    implementation("io.quarkus:quarkus-scheduler")

    implementation(project(":kluster-base"))
}

group = "org.isk"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
