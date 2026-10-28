// Web building blocks shared by every context's web adapter (Spring MVC). Adapters only: core modules must not use it.
plugins {
    id("java-conventions")
}

dependencies {
    api(project(":sample-service-shared-kernel"))
    implementation(libs.spring.boot.starter.web)
}
