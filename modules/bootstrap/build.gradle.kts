// Composition root: the only module that sees every context and layer. Wires use cases and runs the app.
plugins {
    id("spring-app-conventions")
}

dependencies {
    // greeting context
    implementation(project(":sample-service-contexts-greeting-application"))
    implementation(project(":sample-service-contexts-greeting-adapter-web"))
    implementation(project(":sample-service-contexts-greeting-adapter-persistence"))

    implementation(libs.spring.boot.starter.actuator)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.archunit.junit5)
}

tasks.bootJar {
    archiveFileName = "sample-service.jar"
}
