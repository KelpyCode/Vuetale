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

    // Cached ZipFileSystems opened for JAR resources (key = absolute JAR path string)
    private val zipFileSystems = mutableMapOf<String, java.nio.file.FileSystem>()

    @Synchronized
    private fun getOrOpenZipFs(jarPath: Path): java.nio.file.FileSystem {
        val key = jarPath.toAbsolutePath().normalize().toString()
        return zipFileSystems.getOrPut(key) {
            FileSystems.newFileSystem(jarPath, null as ClassLoader?)
        }
    }

    /**
     * Convert a JAR-style URI ("file:/x.jar!/entry" or "jar:file:/x.jar!/entry")
     * into a Path inside the corresponding ZipFileSystem.
     * Returns null if the URI has no "!/" separator (not a JAR URI).
     */
    private fun jarUriToPath(uriStr: String): Path? {
        val bangIdx = uriStr.indexOf("!/")
        if (bangIdx < 0) return null
        // Strip optional leading "jar:" prefix so we always have a "file:..." part
        val fileUriPart = if (uriStr.startsWith("jar:")) uriStr.removePrefix("jar:").substringBefore("!/")
                          else uriStr.substringBefore("!/")
        val entryPart = uriStr.substring(bangIdx + 2)   // everything after "!/"
        val jarPath = Paths.get(URI.create(fileUriPart))
        return getOrOpenZipFs(jarPath).getPath("/$entryPart")
    }

    private fun resolveToPath(path: Path): Path {
        // If the path already belongs to a non-default FS (e.g. ZipFileSystem) it is
        // already resolved – just return it directly so NIO calls work on it.
        if (path.fileSystem !== FileSystems.getDefault()) return path

        val name = path.toString().replace('\\', '/')
        // Candidates to try: with and without .js extension, for each classpath root
        val names = if (name.endsWith(".js")) listOf(name) else listOf(name, "$name.js")
        for (n in names) {
            for (root in classpathRoots) {
                val candidate = if (root.isEmpty()) n else "$root/$n"
                val normalized = candidate.trimStart('/')
                for (loader in loaders) {
                    val url = loader?.getResource(normalized) ?: continue
                    when (url.protocol) {
                        "file" -> {
                            var decoded = URLDecoder.decode(url.path, "UTF-8")
                            if (decoded.length >= 3 && decoded[0] == '/' && decoded[2] == ':') decoded = decoded.substring(1)
                            return Paths.get(decoded)
                        }
                        "jar" -> {
                            // "jar:file:/path/to/jar!/vuetale/core/renderer.js"
                            val urlStr = url.toString()
                            val jarFilePart = urlStr.removePrefix("jar:").substringBefore("!/")
                            val jarPath = Paths.get(URI.create(jarFilePart))
                            return getOrOpenZipFs(jarPath).getPath("/$normalized")
                        }
                    }
                }
            }
        }
        return path
    }

    override fun parsePath(uri: URI): Path {
        // GraalVM resolves ES module imports relative to the source URI.  When the source
        // lives inside a JAR the resolved URI has the form "file:/x.jar!/entry" (or with a
        // leading "jar:" prefix).  Paths.get(uri) treats "!" as a literal filename char and
        // produces a path that doesn't exist.  We intercept that case and return a real
        // ZipFileSystem path instead.
        jarUriToPath(uri.toString())?.let { return it }
        return Paths.get(uri)
    }

    override fun parsePath(path: String): Path = Paths.get(path)

    override fun checkAccess(path: Path, modes: Set<AccessMode>, vararg linkOptions: LinkOption) {
        val real = resolveToPath(path)
        // Use Files.exists() so it works for both default-FS paths and ZipFS paths
        if (!Files.exists(real)) throw NoSuchFileException(path.toString())
    }

    override fun createDirectory(dir: Path, vararg attrs: FileAttribute<*>) {
        Files.createDirectory(resolveToPath(dir), *attrs)
    }

    override fun delete(path: Path) {
        Files.delete(resolveToPath(path))
    }

    override fun newByteChannel(
        path: Path,
        options: Set<OpenOption>,
        vararg attrs: FileAttribute<*>
    ): SeekableByteChannel =
        Files.newByteChannel(resolveToPath(path), options, *attrs)

    override fun newDirectoryStream(dir: Path, filter: DirectoryStream.Filter<in Path>): DirectoryStream<Path> =
        Files.newDirectoryStream(resolveToPath(dir), filter)

    override fun toAbsolutePath(path: Path): Path =
        if (path.isAbsolute) path else resolveToPath(path).toAbsolutePath()

    override fun toRealPath(path: Path, vararg linkOptions: LinkOption): Path {
        val real = resolveToPath(path)
        return try { real.toRealPath(*linkOptions) } catch (_: Exception) { real.normalize() }
    }

    override fun readAttributes(path: Path, attributes: String, vararg options: LinkOption): Map<String, Any> =
        Files.readAttributes(resolveToPath(path), attributes, *options)

    /** Close all cached ZipFileSystems.  Call this when the JSEngine closes. */
    fun closeFileSystems() {
        zipFileSystems.values.forEach { runCatching { it.close() } }
        zipFileSystems.clear()
    }
}

class JSEngine : AutoCloseable {

    private val resourceLoader: ClassLoader?
    private val resourceBasePath: String
    private val context: Context
    private val bridge: VueBridge = VueBridge()
    private lateinit var fs: ClasspathAwareFileSystem

    var loaderCtx: Value

    companion object {
        val instance: JSEngine by lazy { JSEngine() }
    }

    constructor() : this(null, "vuetale")

    constructor(loader: ClassLoader?, basePath: String) {
        this.resourceLoader = loader
        this.resourceBasePath = basePath.trim('/').ifEmpty { "vuetale" }

        val loaders = listOf(loader, Thread.currentThread().contextClassLoader, JSEngine::class.java.classLoader)
        fs = ClasspathAwareFileSystem(classpathRoots = listOf("vuetale/core", ""), loaders = loaders)

        context = Context.newBuilder("js")
            .allowAllAccess(true)
            .allowIO(IOAccess.newBuilder().fileSystem(fs).build())
            .allowHostAccess(HostAccess.ALL)
            .option("js.esm-eval-returns-exports", "true")
            .build()

        val bindings = context.getBindings("js")

        bindings.putMember("ktBridge", bridge)


        fun loadResource(url: URL, globalName: String = "<vuetale>", module: Boolean = false) {
            val reader = InputStreamReader(url.openStream(), Charsets.UTF_8)

            context.eval(
                Source.newBuilder("js", reader, globalName)
                    .mimeType("application/javascript" + if (module) "+module" else "")
                    .build()
            )
        }

        fun loadResource(data: String, globalName: String = "<vuetale>", module: Boolean = false) {
            context.eval(
                Source.newBuilder("js", StringReader(data), globalName)
                    .mimeType("application/javascript" + if (module) "+module" else "")
                    .build()
            )
        }

        // Load the Vue IIFE as a plain script → populates globalThis.Vue
        // The vuetale/core/vue.js shim then re-exports from globalThis.Vue for ESM consumers.
        val vueIifeUrl = resolveClasspathResource("vue.js")
            ?: throw IllegalStateException("vue.js not found on classpath")
        loadResource(vueIifeUrl, "<vue-iife>")

        val vueUrl = resolveCoreResource("vue.js")
            ?: throw IllegalStateException("vuetale/core/vue.js not found on classpath")
        loadResource(vueUrl, "vue", true)

        // Make the IIFE result (last expression value) available as globalThis.Vue
        loadResource(
            "globalThis.Vue = Vue;", "<vue-global>"
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

        loadResource(
            domMock, "<dom-mock>"
        )


        loaderCtx = this.evalModuleResource("loader.js")


    }

    fun evalModuleResource(path: String): Value {
        val url = resolveCoreResource(path) ?: throw IllegalStateException("Core resource not found: $path")
        return context.eval(buildModuleSource(url, url.path))
    }

    fun createUserApp(id: String) {
        context.eval(
            Source.newBuilder("js", StringReader("createUserApp('$id')"), "<createUserApp>")
                .mimeType("application/javascript").build()
        )
    }

    fun evalScript(script: String): Value =
        context.eval(
            Source.newBuilder("js", StringReader(script), "<evalScript>").mimeType("application/javascript").build()
        )

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
        val loaders =
            listOf(resourceLoader, Thread.currentThread().contextClassLoader, JSEngine::class.java.classLoader)
        for (loader in loaders) {
            loader?.getResource(normalized)?.let { return it }
        }
        JSEngine::class.java.getResource("/$normalized")?.let { return it }
        File(normalized).takeIf { it.exists() }?.let { return it.toURI().toURL() }
        return null
    }

    fun getResourceURL(path: String): URL =
        resolveClasspathResource(path) ?: throw IllegalStateException("Resource not found: $path")

    override fun close() {
        try { context.close() } catch (_: Exception) {}
        try { fs.closeFileSystems() } catch (_: Exception) {}
    }
}
