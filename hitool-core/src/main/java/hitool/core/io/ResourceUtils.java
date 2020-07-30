/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.resource.FileSystemResource;
import hitool.core.io.resource.Resource;
import hitool.core.io.support.PathMatchingResourcePatternResolver;
import hitool.core.io.support.ResourcePatternResolver;
import hitool.core.lang3.iterator.EnumerationIterator;
import hitool.core.lang3.wraper.ClassLoaderWrapper;

/**
 * 用来加载类，classpath下的资源文件，属性文件等。
 *              getExtendResource(StringrelativePath)方法，可以使用../符号来加载
 *              classpath外部的资源。
 */
public abstract class ResourceUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

	/** URL protocol for a file in the file system: "file" */
	public static final String URL_PROTOCOL_FILE = "file";

	/** URL protocol for an entry from a jar file: "jar" */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** URL protocol for an entry from a zip file: "zip" */
	public static final String URL_PROTOCOL_ZIP = "zip";

	/** URL protocol for an entry from a WebSphere jar file: "wsjar" */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** URL protocol for an entry from a JBoss jar file: "vfszip" */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	/** URL protocol for a JBoss file system resource: "vfsfile" */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

	/** URL protocol for a general JBoss VFS resource: "vfs" */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/**
	 * URL prefix for loading from a classpath location, value is <b>
	 * {@code classpath:}</b>
	 */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	/**
	 * URL prefix for loading from a url location, value is <b>{@code url:}</b>
	 */
	public static final String URL_PREFIX = "url:";
	/**
	 * URL prefix for loading from a file system, value is <b>{@code file:}</b>
	 */
	public static final String FILE_URL_PREFIX = "file:";
	/**
	 * URL prefix for loading from a file system, value is <b>{@code "jar:"}
	 */
	public static final String JAR_URL_PREFIX = "jar:";

	/** File extension for a regular jar file: ".jar" */
	public static final String JAR_FILE_EXTENSION = ".jar";

	/** Separator between JAR URL and file path within the JAR: "!/" */
	public static final String JAR_URL_SEPARATOR = "!/";
	/**
	 * Charset to use when calling getResourceAsReader. null means use the
	 * system default.
	 */
	protected static Charset charset;

	protected static ConcurrentMap<URL, URLClassLoader> COMPLIED_URL_CLASSLOADER = new ConcurrentHashMap<URL, URLClassLoader>();

	protected static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

	// spring 资源路径匹配解析器
	//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
	//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	
	/**
	 * Prevent instantiation.
	 */
	public ResourceUtils() {
	}

	/**
	 * Returns the default classloader (may be null).
	 * 
	 * @return The default classloader
	 */
	public static ClassLoader getDefaultClassLoader() {
		return classLoaderWrapper.getDefaultClassLoader();
	}

	/**
	 * Sets the default classloader
	 * 
	 * @param defaultClassLoader
	 *            - the new default ClassLoader
	 */
	public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		classLoaderWrapper.setDefaultClassLoader(defaultClassLoader);
	}

	private static int containSum(String source, String dest) {
		int containSum = 0;
		int destLength = dest.length();
		while (source.contains(dest)) {
			containSum = containSum + 1;
			source = source.substring(destLength);
		}
		return containSum;
	}

	private static String cutLastString(String location, String dest, int num) {
		for (int i = 0; i < num; i++) {
			location = location.substring(0,
					location.lastIndexOf(dest, location.length() - 2) + 1);
		}
		return location;
	}

	/**
	 * 得到指定Class所在的ClassLoader的ClassPath的绝对路径
	 */
	protected static String getAbsoluteClassPath(Class<?> clazz) {
		return clazz.getClassLoader().getResource(".").toString();
	}

	/**
	 * 
	 * 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
	 * @param relativePath
	 * @return
	 * @throws IOException
	 */
	public static URL getRelativeResourceAsURL(String relativePath)
			throws IOException {
		if (!relativePath.contains("../")) {
			return getResourceAsURL(relativePath);
		}
		String classPathAbsolutePath = getAbsoluteClassPath(ResourceUtils.class);
		if (relativePath.substring(0, 1).equals("/")) {
			relativePath = relativePath.substring(1);
		}
		String wildcardString = relativePath.substring(0,
				relativePath.lastIndexOf("../") + 3);
		relativePath = relativePath
				.substring(relativePath.lastIndexOf("../") + 3);
		int containSum = containSum(wildcardString, "../");
		classPathAbsolutePath = cutLastString(classPathAbsolutePath, "/",
				containSum);
		String resourceAbsolutePath = classPathAbsolutePath + relativePath;
		URL resourceAbsoluteURL = null;
		try {
			resourceAbsoluteURL = new URL(resourceAbsolutePath);
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}
		return resourceAbsoluteURL;
	}

	/**
	 * Extract the URL for the actual jar file from the given URL (which may
	 * point to a resource in a jar file or to a jar file itself).
	 * 
	 * @param jarUrl
	 *            the original URL
	 * @return the URL for the actual jar file
	 * @throws MalformedURLException
	 *             if no valid jar file URL could be extracted
	 */
	public static URL getJarResourceAsURL(URL jarUrl)
			throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
		if (separatorIndex != -1) {
			String jarFile = urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			} catch (MalformedURLException ex) {
				// Probably no protocol in original jar URL, like
				// "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file
				// system.
				if (!jarFile.startsWith("/")) {
					jarFile = "/" + jarFile;
				}
				return new URL(FILE_URL_PREFIX + jarFile);
			}
		} else {
			return jarUrl;
		}
	}

	public static URL getResourceAsURL(ClassLoader loader, String resource)
			throws IOException {
		URL url = classLoaderWrapper.getResourceAsURL(resource, loader);
		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return url;
	}

	/**
	 * Resolve the given resource location to a {@code java.net.URL}.
	 * <p>
	 * Does not check whether the URL actually exists; simply returns the URL
	 * that the given location would correspond to.
	 * 
	 * @param resource
	 *            the resource location to resolve: either a "classpath:" pseudo
	 *            URL, a "file:" URL, or a plain file path
	 * @return a corresponding URL object
	 * @throws FileNotFoundException
	 *             if the resource cannot be resolved to a URL
	 */
	public static URL getResourceAsURL(String resource)
			throws FileNotFoundException {
		Validate.notNull(resource, "resource must not be null");
		if (resource.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resource.substring(CLASSPATH_URL_PREFIX.length());
			URL url = getResourceAsURL(path, ResourceUtils.class);
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(
						description
								+ " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			// try URL
			return new URL(resource);
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(resource).toURI().toURL();
			} catch (MalformedURLException ex2) {
				throw new FileNotFoundException("Resource location ["
						+ resource
						+ "] is neither a URL not a well-formed file path");
			}
		}
	}

	/**
	 * Load a given resource.
	 * <p/>
	 * This method will try to load the resource using the following methods (in
	 * order):
	 * <ul>
	 * <li>From {@link Thread#getContextClassLoader()
	 * Thread.currentThread().getContextClassLoader()}
	 * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader()
	 * }
	 * </ul>
	 *
	 * @param resource
	 *            The name of the resource to load
	 * @param callingClass
	 *            The Class object of the calling object
	 */
	public static URL getResourceAsURL(String resource, Class<?> callingClass) {
		return classLoaderWrapper.getResourceAsURL(resource,
				callingClass.getClassLoader());
	}

	/**
	 * Load all resources with a given name, potentially aggregating all results
	 * from the searched classloaders. If no results are found, the resource
	 * name is prepended by '/' and tried again.
	 *
	 * This method will try to load the resources using the following methods
	 * (in order):
	 * <ul>
	 * <li>From Thread.currentThread().getContextClassLoader()
	 * <li>callingClass.getClassLoader()
	 * </ul>
	 *
	 * @param resource
	 *            The name of the resources to load
	 * @param callingClass
	 *            The Class object of the calling object
	 */
	public static Iterator<URL> getResourceAsURLs(String resource,
			Class<?> callingClass, boolean aggregate) throws IOException {
		Iterator<URL> ite = classLoaderWrapper.getResourceAsURLs(resource,
				callingClass.getClassLoader());
		if (ite == null) {
			EnumerationIterator<URL> iterator = (EnumerationIterator<URL>) new EnumerationIterator<URL>();
			if (!iterator.hasNext() || aggregate) {
				iterator.addEnumeration(ResourceUtils.class.getClassLoader()
						.getResources(resource));
			}
			if (!iterator.hasNext() || aggregate) {
				iterator.addEnumeration(ResourceUtils.class.getClassLoader()
						.getResources('/' + resource));
			}
		}
		return ite;
	}

	/**
	 * 
	 * <pre>
	 * URLClassLoader ll = new URLClassLoader(urls);
	 * Class&lt;?&gt; a = ll.loadClass(&quot;test.TestService&quot;);
	 * </pre>
	 * @param jarResource
	 * @return 
	 */
	public static ClassLoader getURLClassLoader(URL jarResource) {
		if (jarResource != null
				&& (isJarFileURL(jarResource) || isJarURL(jarResource))) {
			URLClassLoader ret = COMPLIED_URL_CLASSLOADER.get(jarResource);
			if (ret != null) {
				return ret;
			}
			ret = new URLClassLoader(new URL[] { jarResource });
			URLClassLoader existing = COMPLIED_URL_CLASSLOADER.putIfAbsent(
					jarResource, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}

	public static InputStream getResourceAsStream(Object self, String resource)
			throws IOException {
		return getResourceAsStream(self.getClass().getClassLoader(), resource);
	}

	/**
	 * Returns a resource on the classpath as a Stream object
	 *
	 * @param loader
	 *            The classloader used to fetch the resource
	 * @param resource
	 *            The resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static InputStream getResourceAsStream(ClassLoader loader,
			String resource) throws IOException {
		InputStream in = classLoaderWrapper.getResourceAsStream(resource,
				loader);
		if (in == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return in;
	}

	/**
	 * Returns a resource on the classpath as a Stream object
	 * 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找 classpath外部的资源，需要使用../来查找
	 * 
	 * @param resource
	 *            The resource to find
	 * @return The InputStream of the resource found, or <code>null</code> if
	 *         the resource cannot be found from any of the three mentioned
	 *         ClassLoaders.
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */

	public static InputStream getResourceAsStream(String resource)
			throws IOException {
		InputStream stream = classLoaderWrapper.getResourceAsStream(resource);
		if (stream == null) {
			try {
				stream = getURLAsStream(getRelativeResourceAsURL(resource));
			} catch (IOException e) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Resource [" + resource + "] was not found : ",
							e.getMessage());
				}
			}
		}

		if (stream == null && LOG.isTraceEnabled()) {
			LOG.trace("Resource ["
					+ resource
					+ "] was not found via the thread context, current, or "
					+ "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
		}

		return stream;
	}

	/**
	 * This is a convenience method to load a resource as a stream. The
	 * algorithm used to find the resource is given in getResource()
	 * 
	 * @param resource
	 *            The name of the resource to load
	 * @param callingClass
	 *            The Class object of the calling object
	 */
	public static InputStream getResourceAsStream(String resource,
			Class<?> callingClass) {
		URL url = getResourceAsURL(resource, callingClass);
		try {
			return getURLAsStream(url);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets a URL as an input stream
	 *
	 * @param urlString
	 *            - the URL to get
	 * @return An input stream with the data from the URL
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static InputStream getURLAsStream(String urlString)
			throws IOException {
		return getURLAsStream(new URL(urlString));
	}
	
	/**
	 * 加载URL指定的对象到InputStream
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream getURLAsStream(URL originUrl) throws IOException {
        InputStream inputStream = null;
        URLConnection urlConnection = originUrl.openConnection();
        try {
			if (urlConnection instanceof JarURLConnection) {
			    inputStream = getURLConnectionAsStream((JarURLConnection) urlConnection);
			} else {
			    File originFile = new File(originUrl.getPath());
			    if (originFile.isFile()) {
			        inputStream = originUrl.openStream();
			    }
			}
		} catch (Exception e) {
			 throw new IOException("URLConnection[" + urlConnection.getClass().getSimpleName() +
	                    "] is not a recognized/implemented connection type.");
		}
        return inputStream;
    }

    public static InputStream getURLConnectionAsStream(JarURLConnection jarConnection) throws IOException {
        InputStream inputStream = null;
        JarFile jarFile = jarConnection.getJarFile();
        if (!jarConnection.getJarEntry().isDirectory()) { 
        	//如果jar中内容为目录则不返回inputstream
            inputStream = jarFile.getInputStream(jarConnection.getJarEntry());
        }
        return inputStream;
    }

	public static String getResourceAsString(ClassLoader loader, String filename)
			throws IOException {
		String ret = null;
		InputStream in = getResourceAsStream(loader, filename);
		if (in != null) {
			ret = IOUtils.toString(in, Charset.defaultCharset());
		}
		return ret;
	}

	public static String getResourceAsRaw(Object self, String filename)
			throws IOException {
		return getResourceAsRaw(self.getClass().getClassLoader(), filename);
	}

	public static String getResourceAsStr(ClassLoader loader, String filename) {
		try {
			return StringUtils.trimToEmpty(getResourceAsRaw(loader, filename));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getResourceAsStr(Object self, String filename) {
		return getResourceAsStr(self.getClass().getClassLoader(), filename);
	}

	/**
	 * for use during constructors, when 'this' is not available // // makes a
	 * guess that a dummy object is in the same classloader as the caller //
	 * this assumprion may be somewhat dodgy, use with care ...
	 */
	public static String getResourceAsStr(String filename) {
		return getResourceAsStr(getDefaultClassLoader(), filename);
	}

	/**
	 * Returns a resource on the classpath as a Properties object
	 *
	 * @param resource
	 *            The resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Properties getResourceAsProperties(String resource)
			throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource);
		props.load(in);
		in.close();
		return props;
	}

	/**
	 * Returns a resource on the classpath as a Properties object
	 *
	 * @param loader
	 *            The classloader used to fetch the resource
	 * @param resource
	 *            The resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Properties getResourceAsProperties(ClassLoader loader,
			String resource) throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(loader, resource);
		props.load(in);
		in.close();
		return props;
	}

	/**
	 * Returns a resource on the classpath as a Reader object
	 *
	 * @param resource
	 *            The resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Reader getResourceAsReader(String resource)
			throws IOException {
		Reader reader;
		if (charset == null) {
			reader = new InputStreamReader(getResourceAsStream(resource));
		} else {
			reader = new InputStreamReader(getResourceAsStream(resource),
					charset);
		}
		return reader;
	}

	/**
	 * Returns a resource on the classpath as a Reader object
	 *
	 * @param loader
	 *            The classloader used to fetch the resource
	 * @param resource
	 *            The resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Reader getResourceAsReader(ClassLoader loader, String resource)
			throws IOException {
		Reader reader;
		if (charset == null) {
			reader = new InputStreamReader(
					getResourceAsStream(loader, resource));
		} else {
			reader = new InputStreamReader(
					getResourceAsStream(loader, resource), charset);
		}
		return reader;
	}

	/**
	 * Returns a resource on the classpath as a File object
	 *
	 * @param loader
	 *            - the classloader used to fetch the resource
	 * @param resource
	 *            - the resource to find
	 * @return The resource
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static File getResourceAsFile(ClassLoader loader, String resource)
			throws IOException {
		URL url = getResourceAsURL(loader, resource);
		return url == null ? null : new File(url.getFile());
	}

	/**
	 * Resolve the given resource location to a {@code java.io.File}, i.e. to a
	 * file in the file system.
	 * <p>
	 * Does not check whether the file actually exists; simply returns the File
	 * that the given location would correspond to.
	 * 
	 * @param resourceLocation
	 *            the resource location to resolve: either a "classpath:" pseudo
	 *            URL, a "file:" URL, or a plain file path
	 * @return a corresponding File object
	 * @throws FileNotFoundException
	 *             if the resource cannot be resolved to a file in the file
	 *             system
	 */
	public static File getResourceAsFile(String resourceLocation)
			throws FileNotFoundException {
		Validate.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			URL url = classLoaderWrapper.getResourceAsURL(path);
			if (url == null) {
				throw new FileNotFoundException(
						description
								+ " cannot be resolved to absolute file path because it does not exist");
			}
			return getResourceAsFile(url, description);
		}
		try {
			// try URL
			return getResourceAsFile(new URL(resourceLocation));
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}

	

	/**
	 * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file
	 * in the file system.
	 * 
	 * @param resourceUri
	 *            the resource URI to resolve
	 * @return a corresponding File object
	 * @throws FileNotFoundException
	 *             if the URL cannot be resolved to a file in the file system
	 */
	public static File getResourceAsFile(URI resourceUri)
			throws FileNotFoundException {
		return getResourceAsFile(resourceUri, "URI");
	}

	/**
	 * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file
	 * in the file system.
	 * 
	 * @param resourceUri
	 *            the resource URI to resolve
	 * @param description
	 *            a description of the original resource that the URI was
	 *            created for (for example, a class path location)
	 * @return a corresponding File object
	 * @throws FileNotFoundException
	 *             if the URL cannot be resolved to a file in the file system
	 */
	public static File getResourceAsFile(URI resourceUri, String description)
			throws FileNotFoundException {
		Validate.notNull(resourceUri, "Resource URI must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
			throw new FileNotFoundException(description
					+ " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: "
					+ resourceUri);
		}
		return new File(resourceUri.getSchemeSpecificPart());
	}

	/**
	 * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file
	 * in the file system.
	 * 
	 * @param resourceUrl
	 *            the resource URL to resolve
	 * @return a corresponding File object
	 * @throws FileNotFoundException
	 *             if the URL cannot be resolved to a file in the file system
	 */
	public static File getResourceAsFile(URL resourceUrl)
			throws FileNotFoundException {
		return getResourceAsFile(resourceUrl, "URL");
	}

	/**
	 * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file
	 * in the file system.
	 * 
	 * @param resourceUrl
	 *            the resource URL to resolve
	 * @param description
	 *            a description of the original resource that the URL was
	 *            created for (for example, a class path location)
	 * @return a corresponding File object
	 * @throws FileNotFoundException
	 *             if the URL cannot be resolved to a file in the file system
	 */
	public static File getResourceAsFile(URL resourceUrl, String description)
			throws FileNotFoundException {
		Validate.notNull(resourceUrl, "Resource URL must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(description
					+ " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: "
					+ resourceUrl);
		}
		try {
			return new File(toURI(resourceUrl).getSchemeSpecificPart());
		} catch (URISyntaxException ex) {
			// Fallback for URLs that are not valid URIs (should hardly ever
			// happen).
			return new File(resourceUrl.getFile());
		}
	}


	/**
	 * Gets a URL as a Reader
	 *
	 * @param urlString
	 *            - the URL to get
	 * @return A Reader with the data from the URL
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Reader getUrlAsReader(String urlString) throws IOException {
		Reader reader;
		if (charset == null) {
			reader = new InputStreamReader(getURLAsStream(urlString));
		} else {
			reader = new InputStreamReader(getURLAsStream(urlString), charset);
		}
		return reader;
	}

	/**
	 * Gets a URL as a Properties object
	 *
	 * @param urlString
	 *            - the URL to get
	 * @return A Properties object with the data from the URL
	 * @throws java.io.IOException
	 *             If the resource cannot be found or read
	 */
	public static Properties getUrlAsProperties(String urlString)
			throws IOException {
		Properties props = new Properties();
		InputStream input = null;
		try {
			input = getURLAsStream(urlString);
			props.load(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return props;
	}

	/*
	 * Loads a class
	 * 
	 * @param className - the class to fetch
	 * 
	 * @return The loaded class
	 * 
	 * @throws ClassNotFoundException If the class cannot be found (duh!)
	 */
	public static Class<?> classForName(String className)
			throws ClassNotFoundException {
		return classLoaderWrapper.classForName(className);
	}

	public static Charset getCharset() {
		return charset;
	}

	public static void setCharset(Charset charset) {
		ResourceUtils.charset = charset;
	}

	/**
	 * Return whether the given resource location is a URL: either a special
	 * "classpath" pseudo URL or a standard URL.
	 * 
	 * @param resourceLocation
	 *            the location String to check
	 * @return whether the location qualifies as a URL
	 * @see #CLASSPATH_URL_PREFIX
	 * @see java.net.URL
	 */
	public static boolean isURL(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}

	/**
	 * Determine whether the given URL points to a resource in the file system,
	 * that is, has protocol "file", "vfsfile" or "vfs".
	 * 
	 * @param url
	 *            the URL to check
	 * @return whether the URL has been identified as a file system URL
	 */
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol)
				|| URL_PROTOCOL_VFSFILE.equals(protocol) || URL_PROTOCOL_VFS
					.equals(protocol));
	}

	/**
	 * Determine whether the given URL points to a resource in a jar file, that
	 * is, has protocol "jar", "zip", "vfszip" or "wsjar".
	 * 
	 * @param url
	 *            the URL to check
	 * @return whether the URL has been identified as a JAR URL
	 */
	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol)
				|| URL_PROTOCOL_ZIP.equals(protocol)
				|| URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR
					.equals(protocol));
	}

	/**
	 * Determine whether the given URL points to a jar file itself, that is, has
	 * protocol "file" and ends with the ".jar" extension.
	 * 
	 * @param url
	 *            the URL to check
	 * @return whether the URL has been identified as a JAR file URL
	 * @since 4.1
	 */
	public static boolean isJarFileURL(URL url) {
		return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && url.getPath()
				.toLowerCase().endsWith(JAR_FILE_EXTENSION));
	}

	/**
	 * Returns {@code true} if the resource path is not null and starts with one
	 * of the recognized resource prefixes ({@link #CLASSPATH_URL_PREFIX
	 * CLASSPATH_PREFIX}, {@link #URL_PREFIX URL_PREFIX}, or
	 * {@link #FILE_URL_PREFIX FILE_PREFIX}), {@code false} otherwise.
	 *
	 * @param resourcePath
	 *            the resource path to check
	 * @return {@code true} if the resource path is not null and starts with one
	 *         of the recognized resource prefixes, {@code false} otherwise.
	 * @since 0.9
	 */
	public static boolean hasResourcePrefix(String resourcePath) {
		return resourcePath != null
				&& (resourcePath.startsWith(CLASSPATH_URL_PREFIX)
						|| resourcePath.startsWith(URL_PREFIX) || resourcePath
							.startsWith(FILE_URL_PREFIX));
	}

	/**
	 * Returns {@code true} if the resource at the specified path exists,
	 * {@code false} otherwise. This method supports scheme prefixes on the path
	 * as defined in {@link #getInputStreamForPath(String)}.
	 *
	 * @param resourcePath
	 *            the path of the resource to check.
	 * @return {@code true} if the resource at the specified path exists,
	 *         {@code false} otherwise.
	 * @since 0.9
	 */
	public static boolean resourceExists(String resourcePath) {
		InputStream stream = null;
		boolean exists = false;

		try {
			stream = getInputStreamForPath(resourcePath);
			exists = true;
		} catch (IOException e) {
			stream = null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ignored) {
				}
			}
		}

		return exists;
	}

	/**
	 * Returns the InputStream for the resource represented by the specified
	 * path, supporting scheme prefixes that direct how to acquire the input
	 * stream ({@link #CLASSPATH_URL_PREFIX CLASSPATH_PREFIX},
	 * {@link #URL_PREFIX URL_PREFIX}, or {@link #FILE_URL_PREFIX FILE_PREFIX}).
	 * If the path is not prefixed by one of these schemes, the path is assumed
	 * to be a file-based path that can be loaded with a {@link FileInputStream
	 * FileInputStream}.
	 *
	 * @param resourcePath
	 *            the String path representing the resource to obtain.
	 * @return the InputStraem for the specified resource.
	 * @throws IOException
	 *             if there is a problem acquiring the resource at the specified
	 *             path.
	 */
	public static InputStream getInputStreamForPath(String resourcePath)
			throws IOException {

		InputStream is;
		if (resourcePath.startsWith(CLASSPATH_URL_PREFIX)) {
			is = loadFromClassPath(stripPrefix(resourcePath));

		} else if (resourcePath.startsWith(URL_PREFIX)) {
			is = loadFromUrl(stripPrefix(resourcePath));

		} else if (resourcePath.startsWith(FILE_URL_PREFIX)) {
			is = loadFromFile(stripPrefix(resourcePath));

		} else {
			is = loadFromFile(resourcePath);
		}

		if (is == null) {
			throw new IOException("Resource [" + resourcePath
					+ "] could not be found.");
		}

		return is;
	}

	private static InputStream loadFromFile(String path) throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Opening file [" + path + "]...");
		}
		return new FileInputStream(path);
	}

	private static InputStream loadFromUrl(String urlPath) throws IOException {
		LOG.debug("Opening url {}", urlPath);
		URL url = new URL(urlPath);
		return url.openStream();
	}

	private static InputStream loadFromClassPath(String path) throws IOException {
		LOG.debug("Opening resource from class path [{}]", path);
		return getResourceAsStream(path);
	}

	private static String stripPrefix(String resourcePath) {
		return resourcePath.substring(resourcePath.indexOf(":") + 1);
	}

	/**
	 * Convenience method that closes the specified {@link InputStream
	 * InputStream}, logging any {@link IOException IOException} that might
	 * occur. If the {@code InputStream} argument is {@code null}, this method
	 * does nothing. It returns quietly in all cases.
	 *
	 * @param is
	 *            the {@code InputStream} to close, logging any
	 *            {@code IOException} that might occur.
	 */
	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				LOG.warn("Error closing input stream.", e);
			}
		}
	}

	/**
	 * Create a URI instance for the given URL, replacing spaces with "%20" URI
	 * encoding first.
	 * <p>
	 * Furthermore, this method works on JDK 1.4 as well, in contrast to the
	 * {@code URL.toURI()} method.
	 * 
	 * @param url
	 *            the URL to convert into a URI instance
	 * @return the URI instance
	 * @throws URISyntaxException
	 *             if the URL wasn't a valid URI
	 * @see java.net.URL#toURI()
	 */
	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	/**
	 * Create a URI instance for the given location String, replacing spaces
	 * with "%20" URI encoding first.
	 * 
	 * @param location
	 *            the location String to convert into a URI instance
	 * @return the URI instance
	 * @throws URISyntaxException
	 *             if the location wasn't a valid URI
	 */
	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtils.replace(location, " ", "%20"));
	}

	/**
	 * Set the {@link URLConnection#setUseCaches "useCaches"} flag on the given
	 * connection, preferring {@code false} but leaving the flag at {@code true}
	 * for JNLP based resources.
	 * 
	 * @param con
	 *            the URLConnection to set the flag on
	 */
	public static void useCachesIfNecessary(URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}

	/**
	 * “classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * 带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String
	 * name)”方法来查找通配符之前的资源，
	 * 然后通过模式匹配来获取匹配的资源。如“classpath:META-INF/*.LIST”将首先加载通配符之前的目录
	 * “META-INF”，然后再遍历路径进行子路径匹配从而获取匹配的资源。
	 * 
	 * @param regString
	 * @return
	 * @throws IOException
	 */
	public static String[] getRelativeResources(String location) throws IOException {
		String[] rs = ResourceUtils.getResourcePaths(location);
		String[] resouceArray = new String[rs.length];
		for (int i = 0; i < rs.length; i++) {
			String xdpath = rs[i].substring(rs[i].indexOf("classes") + 8);
			resouceArray[i] = xdpath;
		}
		return resouceArray;
	}

	public static String[] getResourcePaths(String location) throws IOException {
		String[] resouceArray = null;
		Resource[] resources = ResourceUtils.getResources(location);
		if (resources == null) {
			return new String[0];
		} else {
			resouceArray = new String[resources.length];
			for (int i = 0; i < resources.length; i++) {
				resouceArray[i] = resources[i].getURL().getPath();
				resouceArray[i] = resouceArray[i].substring(resouceArray[i].indexOf("classes") + 8);
			}
		}
		return resouceArray;
	}

	public static Resource getFileSystemResource(String location) {
		return new FileSystemResource(location);
	}

	public static Resource getResource(String location) {
		return resolver.getResource(location);
	}

	public static Resource[] getResources(String location) throws IOException {
		return resolver.getResources(location);
	}
	
	/**
	 *  匹配classpath*:**\\/*.properties的配置文件
	 */
	public static Resource[] getProperties() throws IOException {
		return ResourceUtils.getResources("classpath*:**/*.properties");
	}

	public static void main(String[] args) throws Exception {

		System.out.println(getRelativeResourceAsURL("../spring/dao.xml"));
		System.out
				.println(getRelativeResourceAsURL("../../../src/log4j.properties"));
		System.out.println(getRelativeResourceAsURL("log4j.properties"));
		System.out.println(getResourceAsURL("log4j.properties"));
		System.out.println(getResourceAsURL("log4j.properties",
				ResourceUtils.class));
	}

}