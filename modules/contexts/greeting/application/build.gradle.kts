// Greeting context - application: use cases and ports. Depends only on its own domain; no framework.
plugins {
    id("java-conventions")
}

dependencies {
    api(project(":sample-service-contexts-greeting-domain"))
}
