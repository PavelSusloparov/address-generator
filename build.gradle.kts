import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Versions.springBoot
    id("io.spring.dependency-management") version Versions.dependencyManagement
    kotlin("jvm") version Versions.kotlinJvm
    kotlin("plugin.spring") version Versions.kotlinJvm
    // Code Coverage plugin
    jacoco
    // Kotlin linter
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlint
}

group = "com.generator"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

// Jacoco Plugin
jacoco {
    toolVersion = Versions.jacoco
    reportsDir = file("$buildDir/reports/jacoco")
}

// Jacoco Coverage Report
val jacocoTestReport by tasks.getting(JacocoReport::class) {
    reports {
        html.isEnabled = true
    }
    doLast {
        println(
            "Jacoco tests coverage report: " +
                project.projectDir.toString() + "/build/reports/jacoco/test/html/index.html"
        )
    }
}

val excludeList = listOf(
    // Application runner
    "com.generator.demo.DemoApplicationKt",
    "com.generator.demo.DemoApplication",
    "com.generator.demo.DemoApplication.*",
    // Data builder
    "com.generator.demo.data.Fixture",
    "com.generator.demo.data.Fixture.Address"
)

// Jacoco Enforce Code Coverage
val jacocoTestCoverageVerification by tasks.getting(JacocoCoverageVerification::class) {
    violationRules {
        // Coverage based on classes. Fail the build if class coverage is less then 100%.
        rule {
            enabled = true
            element = "CLASS"
            includes = listOf("com.generator.*")

            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(1.0)
            }

            excludes = excludeList
        }

        // Coverage based on lines of code. Fail the build if line coverage is less then 100%.
        rule {
            enabled = true
            element = "CLASS"
            includes = listOf("com.generator.*")

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal.valueOf(1.0)
            }

            excludes = excludeList
        }
    }
}

// Tests settings
val test by tasks.getting(Test::class) {
    useJUnitPlatform() // enable Gradle to run JUnit 5 tests
    // log skipped and failed tests
    testLogging {
        events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        showStandardStreams = true
    }
}

// contract tests source set
// Contract tests run the application instance and execute tests against running instance
sourceSets.create("contractTest") {
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("src/contractTest/kotlin")
        resources.srcDirs("src/contractTest/resources")
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

configurations.getByName("contractTestCompile").extendsFrom(
    configurations["compile"]
)
configurations.getByName("contractTestRuntime").extendsFrom(
    configurations["runtime"]
)

val contractTestCompile = configurations.getByName("contractTestCompile")
val contractTestRuntime = configurations.getByName("contractTestCompile")

tasks.register<Test>("contractTest") {
    description = "Runs Cucumber contract tests."
    group = "verification"
    testClassesDirs = sourceSets["contractTest"].output.classesDirs
    classpath = sourceSets["contractTest"].runtimeClasspath
    outputs.upToDateWhen { false }

    testLogging {
        events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        showStandardStreams = true
    }
}

ktlint {
    additionalEditorconfigFile.set(file(".editorconfig"))
}

val ktlintCheck: DefaultTask by tasks

test.dependsOn(ktlintCheck)
test.finalizedBy(jacocoTestCoverageVerification, jacocoTestReport)

// execute linters on check
val check: DefaultTask by tasks
check.dependsOn(ktlintCheck)

dependencies {
    // main source set dependencies
    // Kotlin libraries
    implementation(platform(kotlin("bom", version = Versions.kotlin)))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    // Web service
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}")
    // Healtcheck and many more
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Spring Boot logging request and response
    implementation("org.zalando:logbook-spring-boot-starter:${Versions.logbook}")
    // Create fake names for a variety of things: https://github.com/DiUS/java-faker
    implementation("com.github.javafaker:javafaker:${Versions.faker}")
    // Country code support
    implementation("com.neovisionaries:nv-i18n:${Versions.countryCode}")
    implementation("org.apache.httpcomponents:httpclient:${Versions.httpclient}")
    // test source set dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    // Junit5 Unit tests
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit5}")
    // Mocks for unit tests
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito}")

    // contractTest source set dependencies
    contractTestCompile(platform(kotlin("bom", version = Versions.kotlin)))
    contractTestCompile(kotlin("stdlib-jdk8"))
    contractTestCompile(kotlin("reflect"))
    // Web service
    contractTestCompile("org.springframework.boot:spring-boot-starter")
    contractTestCompile("org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}")
    // Spring Boot logging request and response
    contractTestCompile("org.zalando:logbook-spring-boot-starter:${Versions.logbook}")
    // Create fake names for a variety of things: https://github.com/DiUS/java-faker
    contractTestCompile("com.github.javafaker:javafaker:${Versions.faker}")
    // Country code support
    contractTestCompile("com.neovisionaries:nv-i18n:${Versions.countryCode}")
    // Cucumber Test Driven Development
    contractTestCompile("io.cucumber:cucumber-java:${Versions.cucumber}")
    contractTestCompile("io.cucumber:cucumber-junit:${Versions.cucumber}")
    contractTestCompile("io.cucumber:cucumber-spring:${Versions.cucumber}")
    // Junit4 Contract tests
    contractTestCompile("org.springframework.boot:spring-boot-starter-test")
    contractTestCompile("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
    contractTestCompile("org.apache.httpcomponents:httpclient:${Versions.httpclient}")
}
