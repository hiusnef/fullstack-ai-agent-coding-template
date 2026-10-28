package io.hiusnef.sampleservice.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import io.hiusnef.sampleservice.shared.kernel.ddd.AggregateRoot;
import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEvent;
import io.hiusnef.sampleservice.shared.kernel.ddd.ValueObject;
import java.util.Optional;
import java.util.Set;

/**
 * Enforces .ai/rules/10-clean-architecture.md and .ai/rules/12-ddd.md. Gradle module boundaries already block most
 * wrong imports at compile time; these rules catch what the classpath cannot (packages inside a module, naming, DDD
 * building blocks, cross-context access, cycles).
 *
 * <p>Packages: {@code io.hiusnef.sampleservice.<context>.{domain,application,adapter.*}}, plus {@code shared.kernel},
 * {@code shared.web} and the composition root {@code config}.
 */
@AnalyzeClasses(packages = "io.hiusnef.sampleservice", importOptions = ImportOption.DoNotIncludeTests.class)
class CleanArchitectureTest {

    private static final String BASE = "io.hiusnef.sampleservice.";
    private static final String ALL_PROJECT_CLASSES = "io.hiusnef.sampleservice..";
    private static final Set<String> NOT_CONTEXTS = Set.of("shared", "config");

    // ------------------------------------------------------------ Clean Architecture layers

    @ArchTest
    static final ArchRule dependenciesPointInward = onionArchitecture()
            .domainModels("..domain..")
            .applicationServices("..application..")
            .adapter("web", "..adapter.web..")
            .adapter("persistence", "..adapter.persistence..")
            .withOptionalLayers(true)
            // the composition root (bootstrap module) is allowed to see every layer
            .ignoreDependency(resideInAPackage(BASE + "config.."), alwaysTrue());

    @ArchTest
    static final ArchRule coreIsFrameworkFree = noClasses()
            .that()
            .resideInAnyPackage("..shared.kernel..", "..domain..", "..application..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("org.springframework..", "jakarta.persistence..", "jakarta.servlet..")
            .because("shared kernel, domain and application must not know about frameworks");

    @ArchTest
    static final ArchRule sharedKernelDependsOnNothingInProject = noClasses()
            .that()
            .resideInAPackage(BASE + "shared.kernel..")
            .should()
            .dependOnClassesThat(
                    resideInAPackage(ALL_PROJECT_CLASSES).and(not(resideInAPackage(BASE + "shared.kernel.."))))
            .because("the shared kernel is the innermost module; everyone may use it, it uses no one");

    @ArchTest
    static final ArchRule sharedWebIsForAdaptersOnly = noClasses()
            .that()
            .resideInAnyPackage("..domain..", "..application..", "..adapter.persistence..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage(BASE + "shared.web..");

    @ArchTest
    static final ArchRule adaptersUsePortsOnly = noClasses()
            .that()
            .resideInAPackage("..adapter..")
            .should()
            .dependOnClassesThat(
                    resideInAPackage("..application..").and(not(resideInAPackage("..application..port.."))))
            .because("adapters talk to the application only through ports");

    @ArchTest
    static final ArchRule inputPortsAreUseCaseInterfaces = classes()
            .that()
            .resideInAPackage("..port.in..")
            .should()
            .beInterfaces()
            .andShould()
            .haveSimpleNameEndingWith("UseCase");

    @ArchTest
    static final ArchRule outputPortsAreInterfaces = classes()
            .that()
            .resideInAPackage("..port.out..")
            .should()
            .beInterfaces()
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule persistenceClassesAreAdapters = classes()
            .that()
            .resideInAPackage("..adapter.persistence..")
            .and()
            .areNotInterfaces()
            .and()
            .areTopLevelClasses()
            .should()
            .haveSimpleNameEndingWith("Adapter");

    @ArchTest
    static final ArchRule controllersLiveInWebAdapter = classes()
            .that()
            .areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .should()
            .resideInAPackage("..adapter.web..")
            .andShould()
            .haveSimpleNameEndingWith("Controller");

    // ------------------------------------------------------------ DDD building blocks

    @ArchTest
    static final ArchRule aggregatesLiveInDomain = classes()
            .that()
            .areAssignableTo(AggregateRoot.class)
            .and()
            .doNotHaveFullyQualifiedName(AggregateRoot.class.getName())
            .should()
            .resideInAPackage("..domain..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule valueObjectsAndEventsAreImmutable = classes()
            .that()
            .implement(ValueObject.class)
            .or()
            .implement(DomainEvent.class)
            .should()
            .haveOnlyFinalFields()
            .because("value objects and domain events are immutable (use records)")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domainEventsLiveInDomain = classes()
            .that()
            .implement(DomainEvent.class)
            .should()
            .resideInAPackage("..domain..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoriesAreDomainInterfaces = classes()
            .that()
            .haveSimpleNameEndingWith("Repository")
            .should()
            .beInterfaces()
            .andShould()
            .resideInAPackage("..domain..")
            .because("repositories are declared by the domain and implemented by *Adapter classes")
            .allowEmptyShould(true);

    // ------------------------------------------------------------ Bounded contexts

    @ArchTest
    static final ArchRule contextsTalkOnlyThroughInputPorts = classes()
            .that()
            .resideInAPackage(ALL_PROJECT_CLASSES)
            .should(onlyUseOtherContextsThroughTheirInputPorts())
            .because("a bounded context is used by others only via its application.port.in API or domain events");

    @ArchTest
    static final ArchRule noPackageCycles =
            slices().matching(BASE + "(*).(*)..").should().beFreeOfCycles();

    private static ArchCondition<JavaClass> onlyUseOtherContextsThroughTheirInputPorts() {
        return new ArchCondition<>("only use other contexts through their application.port.in") {
            @Override
            public void check(JavaClass origin, ConditionEvents events) {
                Optional<String> own = contextOf(origin);
                if (own.isEmpty()) {
                    return;
                }
                origin.getDirectDependenciesFromSelf().forEach(dependency -> {
                    JavaClass target = dependency.getTargetClass();
                    Optional<String> other = contextOf(target);
                    boolean crossesContext = other.isPresent() && !other.equals(own);
                    if (crossesContext && !target.getPackageName().contains(".application.port.in")) {
                        events.add(SimpleConditionEvent.violated(dependency, dependency.getDescription()));
                    }
                });
            }
        };
    }

    /** Bounded context of a project class: the package segment after the base, unless it is shared/config/root. */
    private static Optional<String> contextOf(JavaClass javaClass) {
        String pkg = javaClass.getPackageName();
        if (!pkg.startsWith(BASE)) {
            return Optional.empty();
        }
        String context = pkg.substring(BASE.length()).split("\\.")[0];
        return NOT_CONTEXTS.contains(context) ? Optional.empty() : Optional.of(context);
    }
}
