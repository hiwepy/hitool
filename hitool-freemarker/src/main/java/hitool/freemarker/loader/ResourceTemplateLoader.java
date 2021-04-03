package hitool.freemarker.loader;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.utility.NullArgumentException;
import freemarker.template.utility.StringUtil;
import hitool.core.lang3.wraper.ClassLoaderWrapper;
import hitool.freemarker.utils.TemplateLoaderUtils;

public class ResourceTemplateLoader extends URLTemplateLoader {
	
	private final ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();
	private final Class<?> resourceLoaderClass;
    private final ClassLoader classLoader;
    private final String basePackagePath;

    /*
     * Creates a template loader that will use the {@link Class#getResource(String)} method of the specified class to
     * load the resources, and the specified base package path (absolute or relative).
     *
     * <p>
     * Examples:
     * <ul>
     * <li>Relative base path (will load from the {@code com.example.myapplication.templates} package):<br>
     * {@code new ClassTemplateLoader(com.example.myapplication.SomeClass.class, "templates")}
     * <li>Absolute base path:<br>
     * {@code new ClassTemplateLoader(somepackage.SomeClass.class, "/com/example/myapplication/templates")}
     * </ul>
     *
     * @param resourceLoaderClass
     *            The class whose {@link Class#getResource(String)} method will be used to load the templates. Be sure
     *            that you chose a class whose defining class-loader sees the templates. This parameter can't be
     *            {@code null}.
     * @param basePackagePath
     *            The package that contains the templates, in path ({@code /}-separated) format. If it doesn't start
     *            with a {@code /} then it's relative to the path (package) of the {@code resourceLoaderClass} class. If
     *            it starts with {@code /} then it's relative to the root of the package hierarchy. Note that path
     *            components should be separated by forward slashes independently of the separator character used by the
     *            underlying operating system. This parameter can't be {@code null}.
     * 
     * @see #ClassTemplateLoader(ClassLoader, String)
     */
    public ResourceTemplateLoader(Class<?> resourceLoaderClass, String basePackagePath) {
        this(resourceLoaderClass, false, null, basePackagePath);
    }

    /*
     * Similar to {@link #ClassTemplateLoader(Class, String)}, but instead of {@link Class#getResource(String)} it uses
     * {@link ClassLoader#getResource(String)}. Because a {@link ClassLoader} isn't bound to any Java package, it
     * doesn't mater if the {@code basePackagePath} starts with {@code /} or not, it will be always relative to the root
     * of the package hierarchy
     * 
     * @since 2.3.22
     */
    public ResourceTemplateLoader(ClassLoader classLoader, String basePackagePath) {
        this(null, true, classLoader, basePackagePath);
    }

	public ResourceTemplateLoader(Class<?> resourceLoaderClass, boolean allowNullBaseClass, ClassLoader classLoader, String basePackagePath) {
        if (!allowNullBaseClass) {
            NullArgumentException.check("resourceLoaderClass", resourceLoaderClass);
        }
        NullArgumentException.check("basePackagePath", basePackagePath);

        // Either set a non-null resourceLoaderClass or a non-null classLoader, not both:
        this.resourceLoaderClass = classLoader == null ? (resourceLoaderClass == null ? this.getClass() : resourceLoaderClass) : null;
        if (this.resourceLoaderClass == null && classLoader == null) {
            throw new NullArgumentException("classLoader");
        }
        this.classLoader = classLoader;

        String canonBasePackagePath = canonicalizePrefix(basePackagePath);
        if (this.classLoader != null && canonBasePackagePath.startsWith("/")) {
            canonBasePackagePath = canonBasePackagePath.substring(1);
        }
        this.basePackagePath = canonBasePackagePath;
    }

    @Override
    protected URL getURL(String name) {
        String fullPath = basePackagePath + name;
        URL url = classLoaderWrapper.getResourceAsURL(fullPath, resourceLoaderClass != null ? resourceLoaderClass.getClassLoader() : classLoader);
        return url;
    }

    /*
     * Show class name and some details that are useful in template-not-found errors.
     * 
     * @since 2.3.21
     */
    public String toString() {
        return TemplateLoaderUtils.getClassNameForToString(this) + "("
                + (resourceLoaderClass != null
                        ? "resourceLoaderClass=" + resourceLoaderClass.getName()
                        : "classLoader=" + StringUtil.jQuote(classLoader))
                + ", basePackagePath"
                + "="
                + StringUtil.jQuote(basePackagePath)
                + (resourceLoaderClass != null
                        ? (basePackagePath.startsWith("/") ? "" : " /* relatively to resourceLoaderClass pkg */")
                        : ""
                )
                + ")";
    }

    /*
     * See the similar parameter of {@link #ClassTemplateLoader(Class, String)}; {@code null} when other mechanism is
     * used to load the resources.
     * 
     * @since 2.3.22
     */
    public Class<?> getResourceLoaderClass() {
        return resourceLoaderClass;
    }

    /*
     * See the similar parameter of {@link #ClassTemplateLoader(ClassLoader, String)}; {@code null} when other mechanism
     * is used to load the resources.
     * 
     * @since 2.3.22
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /*
     * See the similar parameter of {@link #ClassTemplateLoader(ClassLoader, String)}; note that this is a normalized
     * version of what was actually passed to the constructor.
     * 
     * @since 2.3.22
     */
    public String getBasePackagePath() {
        return basePackagePath;
    }
	
}