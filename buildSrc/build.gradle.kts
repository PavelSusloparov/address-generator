import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.spring") version "1.4.0"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "14"
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom", version = "1.4.0")))
    implementation(kotlin("stdlib-jdk8"))
}
