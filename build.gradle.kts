plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
    id("app.ultradev.hytalegradle") version "2.0.1"
}

group = "li.kelp"
version = "1.0.23"

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

// Expand manifest placeholders (and other resources) with project properties
tasks.processResources {
    // Use simple token replacement for ${version}
    filesMatching("**/manifest.json") {
        expand("version" to project.version.toString())
    }
}

tasks.withType<JavaExec>().configureEach {
}

tasks.shadowJar {
    isZip64 = true
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/INDEX.LIST")
    mergeServiceFiles()

    // Automatically deploy to run/mods/ after every build
    doLast {
        val dest = rootProject.file("run/mods/${archiveFileName.get()}")
        dest.parentFile.mkdirs()
        archiveFile.get().asFile.copyTo(dest, overwrite = true)
        println("Deployed ${archiveFileName.get()} → run/mods/")
    }
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

// Distribution tasks
tasks.register<Task>("prepareDistribution") {
    group = "distribution"
    description = "Prepare all artifacts for multi-platform distribution"
    dependsOn("shadowJar")

    doLast {
        val distDir = file("build/distribution")
        distDir.mkdirs()

        // Copy shadow JAR
        copy {
            from("build/libs")
            into(distDir)
            include("*-all.jar")
        }

        // Generate plugin info JSON for Nitrado
        val pluginInfoFile = file("$distDir/plugin-info.json")
        pluginInfoFile.writeText(
            """
{
    "name": "Vuetale",
    "version": "${project.version}",
    "description": "Modern Vue.js + TypeScript UI development for Hytale mods",
    "author": "kelpli",
    "main_jar": "Vuetale-${project.version}-all.jar",
    "hytale_version": "release",
    "minimum_hytale_version": "1.0.0",
    "dependencies": {},
    "website": "https://github.com/kelpli/Vuetale",
    "source_code": "https://github.com/kelpli/Vuetale",
    "discord": "https://discord.gg/affkepndn7",
    "opencollective": "https://opencollective.com/vuetale",
    "license": "MIT",
    "tags": ["ui", "vue", "typescript", "framework", "components", "reactive"],
    "screenshots": [],
    "documentation": "https://github.com/kelpli/Vuetale/blob/main/README.md"
}
        """.trimIndent()
        )

        // Copy additional distribution files
        copy {
            from(rootDir)
            into(distDir)
            include("README.md", "CHANGELOG.md", "LICENSE")
        }

        println("Distribution artifacts prepared in build/distribution/")
    }
}

tasks.register<Zip>("createDistributionZip") {
    group = "distribution"
    description = "Create a zip archive with all distribution artifacts"
    dependsOn("prepareDistribution")

    from("build/distribution")
    archiveFileName.set("Vuetale-${project.version}-distribution.zip")
    destinationDirectory.set(file("build/libs"))
}

kotlin {
    jvmToolchain(25)
}


