// Greeting context - domain: aggregates, value objects, domain events, repository interfaces. Plain Java only.
plugins {
    id("java-conventions")
}

dependencies {
    api(project(":sample-service-shared-kernel"))
}
