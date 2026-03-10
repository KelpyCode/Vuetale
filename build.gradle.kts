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

}

hytale {
    allowOp.set(true)
    patchline.set("release")
    includeLocalMods.set(false)
    manifest {
        version.set(project.version.toString())
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
}
