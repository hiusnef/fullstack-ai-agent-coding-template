import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.process.CommandLineArgumentProvider

/** Adds Mockito as a Java agent to a test JVM. A class (not a lambda) so it works with the configuration cache. */
class MockitoAgentArgumentProvider(@get:Classpath val agent: FileCollection) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("-javaagent:${agent.singleFile.absolutePath}", "-Xshare:off")
}
