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
    // GraalVM polyglot dependencies include POM-packaged artifacts that Shadow JAR
    // mistakenly tries to expand as ZIP archives — exclude them explicitly.
    exclude("*.pom")
    isZip64 = true

    // Only include actual JAR files from the runtime classpath, not POM/BOM files.
    // This prevents Shadow JAR from trying to open .pom files as ZIP archives.
    val runtimeJarsOnly = project.configurations.getByName("runtimeClasspath")
        .resolvedConfiguration.resolvedArtifacts
        .filter { it.extension == "jar" }
        .map { it.file }
    from(runtimeJarsOnly)
    configurations = emptyList()
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


















