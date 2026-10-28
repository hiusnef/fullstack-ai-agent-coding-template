// Greeting context - driven adapter: implements the domain repositories. Must not depend on other adapters/contexts.
plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":sample-service-contexts-greeting-domain"))
    implementation(libs.spring.context)
}
