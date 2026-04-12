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

    // Javet – Java API
    implementation("com.caoccao.javet:javet:4.1.2")
    // Javet – Node.js native libraries (includes setTimeout, setInterval, process, etc.)
    implementation("com.caoccao.javet:javet-node-windows-x86_64:4.1.2")
    implementation("com.caoccao.javet:javet-node-linux-x86_64:4.1.2")
}

hytale {
    allowOp.set(true)
    patchline.set("release")
    includeLocalMods.set(false)
    manifest {
        version.set(project.version.toString())
    }
}

// No engine-specific JVM flags required for Javet/V8.
tasks.withType<JavaExec>().configureEach {
}

tasks.shadowJar {
    isZip64 = true
    // Use Shadow's standard pipeline so all transformers (mergeServiceFiles, manifest, etc.)
    // are applied correctly to every artifact.
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/INDEX.LIST")
    mergeServiceFiles()
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


