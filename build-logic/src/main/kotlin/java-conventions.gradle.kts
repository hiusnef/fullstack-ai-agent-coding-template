// Applied by every backend module: Java 25, Spring Boot BOM for versions, JUnit 5, Spotless.
plugins {
    `java-library`
    id("com.diffplug.spotless")
}

group = "io.hiusnef"

val libs = the<VersionCatalogsExtension>().named("libs")
fun lib(alias: String) = libs.findLibrary(alias).get()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

// Mockito is loaded as a Java agent: JDK 21+ warns on (and will block) dynamic self-attach.
val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    val bom = platform(lib("spring-boot-dependencies"))
    implementation(bom)
    testImplementation(bom)
    mockitoAgent(bom)

    testImplementation(lib("junit-jupiter"))
    testImplementation(lib("assertj-core"))
    testImplementation(lib("mockito-core"))
    testImplementation(lib("mockito-junit-jupiter"))
    testRuntimeOnly(lib("junit-platform-launcher"))
    mockitoAgent(lib("mockito-core")) { isTransitive = false }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgumentProviders.add(MockitoAgentArgumentProvider(mockitoAgent))
}

spotless {
    java {
        palantirJavaFormat()
        removeUnusedImports()
    }
}
