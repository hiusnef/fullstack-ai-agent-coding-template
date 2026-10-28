// Greeting context - driving adapter: HTTP -> input ports. Must not depend on other adapters/contexts.
plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":sample-service-contexts-greeting-application"))
    implementation(project(":sample-service-shared-web"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)

    testImplementation(libs.spring.boot.starter.webmvc.test)
}
