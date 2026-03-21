package li.kelp.vuetale.javascript

import org.graalvm.polyglot.*
import org.graalvm.polyglot.io.FileSystem
import org.graalvm.polyglot.io.IOAccess
import java.io.File
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.nio.channels.SeekableByteChannel
import java.nio.file.*
import java.nio.file.attribute.FileAttribute

internal class ClasspathAwareFileSystem(
    private val classpathRoots: List<String>,
    private val loaders: List<ClassLoader?>
) : FileSystem {

    private fun resolveToPath(path: Path): Path {
        val name = path.toString().replace('\\', '/')
        // Candidates to try: with and without .js extension, for each classpath root
        val names = if (name.endsWith(".js")) listOf(name) else listOf(name, "$name.js")
        for (n in names) {
            for (root in classpathRoots) {
                val candidate = if (root.isEmpty()) n else "$root/$n"
                val normalized = candidate.trimStart('/')
                for (loader in loaders) {
                    val url = loader?.getResource(normalized) ?: continue
                    if (url.protocol == "file") {
                        var decoded = URLDecoder.decode(url.path, "UTF-8")
                        if (decoded.length >= 3 && decoded[0] == '/' && decoded[2] == ':') decoded = decoded.substring(1)
                        return Paths.get(decoded)
                    }
                }
            }
        }
        return path
    }

    override fun parsePath(uri: URI): Path = Paths.get(uri)
    override fun parsePath(path: String): Path = Paths.get(path)

    override fun checkAccess(path: Path, modes: Set<AccessMode>, vararg linkOptions: LinkOption) {
        val real = resolveToPath(path)
        if (!real.toFile().exists()) throw NoSuchFileException(path.toString())
    }

    override fun createDirectory(dir: Path, vararg attrs: FileAttribute<*>) {
        Files.createDirectory(resolveToPath(dir), *attrs)
    }

    override fun delete(path: Path) {
        Files.delete(resolveToPath(path))
    }

    override fun newByteChannel(path: Path, options: Set<OpenOption>, vararg attrs: FileAttribute<*>): SeekableByteChannel =
        Files.newByteChannel(resolveToPath(path), options, *attrs)

    override fun newDirectoryStream(dir: Path, filter: DirectoryStream.Filter<in Path>): DirectoryStream<Path> =
        Files.newDirectoryStream(resolveToPath(dir), filter)

    override fun toAbsolutePath(path: Path): Path =
        if (path.isAbsolute) path else resolveToPath(path).toAbsolutePath()

    override fun toRealPath(path: Path, vararg linkOptions: LinkOption): Path =
        resolveToPath(path).toRealPath(*linkOptions)

    override fun readAttributes(path: Path, attributes: String, vararg options: LinkOption): Map<String, Any> =
        Files.readAttributes(resolveToPath(path), attributes, *options)
}

class JSEngine : AutoCloseable {

    private val resourceLoader: ClassLoader?
    private val resourceBasePath: String
    private val context: Context

    constructor() : this(null, "vuetale")

    constructor(loader: ClassLoader?, basePath: String) {
        this.resourceLoader = loader
        this.resourceBasePath = basePath.trim('/').ifEmpty { "vuetale" }

        val loaders = listOf(loader, Thread.currentThread().contextClassLoader, JSEngine::class.java.classLoader)
        val fs = ClasspathAwareFileSystem(classpathRoots = listOf("vuetale/core", ""), loaders = loaders)

        context = Context.newBuilder("js")
            .allowAllAccess(true)
            .allowIO(IOAccess.newBuilder().fileSystem(fs).build())
            .allowHostAccess(HostAccess.ALL)
            .option("js.esm-eval-returns-exports", "true")
            .build()

        // Load the Vue IIFE as a plain script → populates globalThis.Vue
        // The vuetale/core/vue.js shim then re-exports from globalThis.Vue for ESM consumers.
        val vueUrl = resolveClasspathResource("vue.js")
            ?: throw IllegalStateException("vue.js not found on classpath")
        val vueReader = InputStreamReader(vueUrl.openStream(), Charsets.UTF_8)
        context.eval(
            Source.newBuilder("js", vueReader, "vue-iife")
                .mimeType("application/javascript")
                .build()
        )


        // Make the IIFE result (last expression value) available as globalThis.Vue
        context.eval(
            Source.newBuilder("js", StringReader("globalThis.Vue = Vue;"), "<vue-global>")
                .mimeType("application/javascript")
                .build()
        )

        // Provide a minimal mock DOM so Vue's mount(selector) works in headless GraalVM.
        // Vue calls document.querySelector(selector) to get the mount root element.
        val domMock = """
            if (typeof document === 'undefined') {
                globalThis.document = {
                    querySelector: function(sel) {
                        return { tag: 'div', selector: sel, children: null };
                    },
                    createElement: function(tag) {
                        return { tag: tag, children: null };
                    }
                };
            }
        """.trimIndent()
        context.eval(
            Source.newBuilder("js", StringReader(domMock), "<dom-mock>")
                .mimeType("application/javascript")
                .build()
        )
    }

    fun evalModuleResource(path: String): Value {
        val url = resolveCoreResource(path) ?: throw IllegalStateException("Core resource not found: $path")
        return context.eval(buildModuleSource(url, path))
    }

    fun createUserApp(id: String) {
        context.eval(
            Source.newBuilder("js", StringReader("createUserApp('$id')"), "<createUserApp>")
                .mimeType("application/javascript").build()
        )
    }

    fun evalScript(script: String): Value =
        context.eval(Source.newBuilder("js", StringReader(script), "<evalScript>").mimeType("application/javascript").build())

    private fun buildModuleSource(url: URL, name: String): Source {
        val reader = InputStreamReader(url.openStream(), Charsets.UTF_8)
        return Source.newBuilder("js", reader, name)
            .mimeType("application/javascript+module")
            .uri(toSafeUri(url))
            .build()
    }

    private fun toSafeUri(url: URL): URI {
        if (url.protocol != "file") return url.toURI()
        var p = URLDecoder.decode(url.path, "UTF-8")
        if (p.length >= 3 && p[0] == '/' && p[2] == ':') p = p.substring(1)
        return File(p).toURI()
    }

    private fun resolveCoreResource(path: String): URL? =
        resolveClasspathResource("vuetale/core/" + path.trimStart('/'))

    private fun resolveClasspathResource(candidate: String): URL? {
        val normalized = candidate.trimStart('/')
        val loaders = listOf(resourceLoader, Thread.currentThread().contextClassLoader, JSEngine::class.java.classLoader)
        for (loader in loaders) { loader?.getResource(normalized)?.let { return it } }
        JSEngine::class.java.getResource("/$normalized")?.let { return it }
        File(normalized).takeIf { it.exists() }?.let { return it.toURI().toURL() }
        return null
    }

    fun getResourceURL(path: String): URL =
        resolveClasspathResource(path) ?: throw IllegalStateException("Resource not found: $path")

    override fun close() { try { context.close() } catch (_: Exception) {} }
}
