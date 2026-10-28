// Shared kernel: cross-cutting, framework-free building blocks every module may use (errors, DDD building blocks,
// small utilities).
// Innermost of all: depends on nothing but the JDK. No business logic, nothing feature-specific.
plugins {
    id("java-conventions")
}
