plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
    id("app.ultradev.hytalegradle") version "2.0.1"
}

group = "li.kelp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Kotlin JUnit5 bridge aligned with project Kotlin version
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.3.0")
    // JUnit Jupiter (API + Engine)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation(kotlin("reflect"))

    implementation("org.graalvm.polyglot:polyglot:25.0.2")

    // The JavaScript engine (includes Truffle and required JS bits)
    // Using the 'js' artifact automatically pulls in necessary transitive deps
    implementation("org.graalvm.polyglot:js:25.0.2")
}

hytale {
    allowOp.set(true)
    patchline.set("release")
    includeLocalMods.set(false)
    manifest {
        version.set(project.version.toString())
    }
}

tasks.shadowJar {
    isZip64 = true
    // Disable Shadow's default configuration processing — we supply sources manually below.
    configurations = emptyList()
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/INDEX.LIST")

    // Use an artifactView that requests only "jar"-typed artifacts.
    // GraalVM polyglot ships POM-only aggregator modules (e.g. org.graalvm.js:js) that
    // Shadow would otherwise try to expand as ZIP archives, causing a build failure.
    // lenient(true) silently skips any artifact that cannot satisfy the "jar" type.
    //
    // Each file is also wrapped in zipTree() so that Shadow merges the class files
    // directly into the fat JAR. Passing plain File references would embed the dependency
    // JARs as nested archives (not merged), which is why kotlin-stdlib was missing before.
    from(
        project.configurations.getByName("runtimeClasspath").incoming
            .artifactView {
                lenient(true)
                attributes {
                    attribute(Attribute.of("artifactType", String::class.java), "jar")
                }
            }
            .files
            .elements
            .map { locations -> locations.map { project.zipTree(it.asFile) } }
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Run li.kelp.vuetale.MainKt standalone (no Hytale server)"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("li.kelp.vuetale.MainKt")
}

kotlin {
    jvmToolchain(25)
}


















