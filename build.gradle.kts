plugins {
    id("java")
    id("io.qameta.allure") version "2.11.2"
}

group = "org.spribe"
version = "1.0-SNAPSHOT"

val lombokVersion = "1.18.30"
val allureVersion = "2.21.0"
val allure_environment_writer_version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.testng:testng:7.8.0")
    implementation("io.rest-assured:rest-assured:5.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("io.qameta.allure:allure-testng:$allureVersion")
    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    implementation("com.github.automatedowl:allure-environment-writer:$allure_environment_writer_version") {
        exclude("io.qameta.allure", "allure-testng")
    }

}

tasks.register<Test>("apiTests") {
    description = "Run API tests"
    group = "test plans"

    useTestNG {
        ignoreFailures = true
        suiteName = systemProperties["suite_name"] as? String ?: "API Tests"
        includeGroups.add(project.findProperty("test_group") as? String ?: "regression")

    }

    testLogging {
        events("STARTED", "PASSED", "FAILED", "SKIPPED")
    }

    dependsOn("assemble", "testClasses", "compileTestJava")
}
