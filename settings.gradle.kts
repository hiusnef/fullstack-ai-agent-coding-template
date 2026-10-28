pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

rootProject.name = "sample-service"

// Every folder under modules/ that has a build.gradle.kts is a module, named after its path:
//   modules/<parent1>/.../<child>  ->  :sample-service-<parent1>-...-<child>
// e.g. modules/contexts/greeting/adapter/web -> :sample-service-contexts-greeting-adapter-web.
// Adding a module = creating the folder, no edit here.
// Layout: modules/contexts/<bounded-context>/{domain,application,adapter/*}, modules/shared/{kernel,web}, modules/bootstrap.
// Inside a context dependencies point inward (adapter -> application -> domain -> shared/kernel); bootstrap wires all.
val modulesDir = file("modules")
modulesDir
    .walkTopDown()
    .onEnter { it.name !in setOf("build", "src", ".gradle", "node_modules") }
    .filter { it != modulesDir && it.resolve("build.gradle.kts").isFile }
    .sortedBy { it.path }
    .forEach { dir ->
        val name = rootProject.name + "-" + dir.relativeTo(modulesDir).invariantSeparatorsPath.replace('/', '-')
        include(name)
        project(":$name").projectDir = dir
    }
