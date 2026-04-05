import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

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
    // Use Shadow's standard pipeline so all transformers (mergeServiceFiles, manifest, etc.)
    // are applied correctly to every artifact.
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/INDEX.LIST")

    // GraalVM Truffle checks that the fat JAR carries Multi-Release: true.
    manifest {
        attributes["Multi-Release"] = "true"
    }

    // GraalVM language implementations (js, regex, …) register themselves via
    // META-INF/services/. Without merging, the last JAR processed silently wins
    // and the others' language registrations are lost → "No language for id regex found".
    mergeServiceFiles()

    // org.graalvm.polyglot:js and org.graalvm.js:js are POM-only aggregator artifacts
    // (no JAR in the cache). Shadow's standard pipeline would try to expand them as ZIP
    // files and crash. Excluding them by coordinate is safe: all their real transitive
    // JARs (js-language, regex, truffle-api, …) remain on the classpath and are merged.
    dependencies {
        exclude(dependency("org.graalvm.polyglot:js"))
        exclude(dependency("org.graalvm.js:js"))
    }

    // Shadow 9.x does not correctly merge META-INF/services files across external JARs:
    // js-language.jar is processed *after* regex.jar (it depends on it), so its copy of
    // META-INF/services/com.oracle.truffle.api.provider.TruffleLanguageProvider silently
    // overwrites regex.jar's copy, dropping RegexLanguageProvider from the registry.
    // Truffle then fails to find the "regex" language at runtime.
    // We patch the output JAR directly after Shadow finishes to guarantee both providers
    // are present in the merged service file.
    val jarFile = archiveFile   // capture Provider<RegularFile> for use inside doLast
    doLast {
        val jar: java.io.File = jarFile.get().asFile
        val servicePath = "META-INF/services/com.oracle.truffle.api.provider.TruffleLanguageProvider"
        val required = listOf(
            "com.oracle.truffle.js.lang.JavaScriptLanguageProvider",
            "com.oracle.truffle.regex.RegexLanguageProvider"
        )
        val tmp = File(jar.parentFile, "patched-shadow.jar")
        tmp.delete()

        val zf = ZipFile(jar)
        val zout = ZipOutputStream(BufferedOutputStream(FileOutputStream(tmp)))
        var patched = false
        try {
            val it = zf.entries()
            while (it.hasMoreElements()) {
                val entry = it.nextElement()
                zout.putNextEntry(ZipEntry(entry.name))
                if (entry.name == servicePath) {
                    patched = true
                    val text = zf.getInputStream(entry).reader().readText()
                    val lines = text.lines().map { ln -> ln.trim() }.filter { ln -> ln.isNotBlank() }.toMutableList()
                    for (p in required) { if (!lines.contains(p)) lines.add(p) }
                    val bytes = lines.joinToString("\n").toByteArray(Charsets.UTF_8)
                    zout.write(bytes, 0, bytes.size)
                } else {
                    val buf = ByteArray(8192)
                    val ins = zf.getInputStream(entry)
                    var n = ins.read(buf)
                    while (n >= 0) { zout.write(buf, 0, n); n = ins.read(buf) }
                    ins.close()
                }
                zout.closeEntry()
            }
            if (!patched) {
                zout.putNextEntry(ZipEntry(servicePath))
                val bytes = required.joinToString("\n").toByteArray(Charsets.UTF_8)
                zout.write(bytes, 0, bytes.size)
                zout.closeEntry()
            }
        } finally {
            zout.close()
            zf.close()
        }
        tmp.copyTo(jar, overwrite = true)
        tmp.delete()
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

kotlin {
    jvmToolchain(25)
}


















