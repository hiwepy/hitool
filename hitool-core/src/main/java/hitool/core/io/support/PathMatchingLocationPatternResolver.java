/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.support;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.beanutils.reflection.ClassLoaderUtils;
import hitool.core.beanutils.reflection.ClassUtils;
import hitool.core.io.ResourceUtils;
import hitool.core.io.resource.UrlResource;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;
import hitool.core.regexp.PathMatcher;
import hitool.core.regexp.matcher.AntPathMatcher;

public class PathMatchingLocationPatternResolver implements LocationPatternResolver {
	
	protected static Logger LOG = LoggerFactory.getLogger(PathMatchingLocationPatternResolver.class);
	protected static Method equinoxResolveMethod;

	static {
		try {
			// Detect Equinox OSGi (e.g. on WebSphere 6.1)
			Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator",
					PathMatchingLocationPatternResolver.class.getClassLoader());
			equinoxResolveMethod = fileLocatorClass.getMethod("resolve", URL.class);
			LOG.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
		}
		catch (Throwable ex) {
			equinoxResolveMethod = null;
		}
	}
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * Set the PathMatcher implementation to use for this
	 * location pattern resolver. Default is AntPathMatcher.
	 * @see org.springframework.util.AntPathMatcher
	 */
	public void setPathMatcher(PathMatcher pathMatcher) {
		Assert.notNull(pathMatcher, "PathMatcher must not be null");
		this.pathMatcher = pathMatcher;
	}

	/**
	 * Return the PathMatcher that this location pattern resolver uses.
	 */
	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}
	
	@Override
	public String[] getLocations(String locationPattern) throws IOException {
		Assert.notNull(locationPattern, "Location pattern must not be null");
		if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
			// a class path location (multiple locations for same name possible)
			if (getPathMatcher().isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
				// a class path location pattern
				return findPathMatchingLocations(locationPattern);
			}
			else {
				// all class path locations with the given name
				return findAllClassPathLocations(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
			}
		}
		else {
			// Only look for a pattern after a prefix here
			// (to not get fooled by a pattern symbol in a strange prefix).
			int prefixEnd = locationPattern.indexOf(":") + 1;
			if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd))) {
				// a file pattern
				return findPathMatchingLocations(locationPattern);
			}
			else {
				// a single location with the given name
				return new String[] {locationPattern};
			}
		}
	}
	


	/**
	 * Find all class location locations with the given location via the ClassLoader.
	 * Delegates to {@link #doFindAllClassPathResources(String)}.
	 * @param location the absolute path within the classpath
	 * @return the result as Resource array
	 * @throws IOException in case of I/O errors
	 * @see java.lang.ClassLoader#getResources
	 * @see #convertClassLoaderURL
	 */
	protected String[] findAllClassPathLocations(String location) throws IOException {
		String path = location;
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		Set<String> result = doFindAllClassPathResources(path);
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Find all class location locations with the given path via the ClassLoader.
	 * Called by {@link #findAllClassPathResources(String)}.
	 * @param path the absolute path within the classpath (never a leading slash)
	 * @return a mutable Set of matching Resource instances
	 */
	protected Set<String> doFindAllClassPathResources(String path) throws IOException {
		Set<String> result = new LinkedHashSet<String>(16);
		ClassLoader cl = ClassLoaderUtils.getClassLoader(this.getClass());
		Enumeration<URL> locationUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
		while (locationUrls.hasMoreElements()) {
			URL url = locationUrls.nextElement();
			result.add(convertClassLoaderURL(url));
		}
		if ("".equals(path)) {
			// The above result is likely to be incomplete, i.e. only containing file system references.
			// We need to have pointers to each of the jar files on the classpath as well...
			addAllClassLoaderJarRoots(cl, result);
		}
		return result;
	}

	/**
	 * Convert the given URL as returned from the ClassLoader into a {@link Resource}.
	 * <p>The default implementation simply creates a {@link UrlResource} instance.
	 * @param url a URL as returned from the ClassLoader
	 * @return the corresponding Resource object
	 * @see java.lang.ClassLoader#getResources
	 * @see hitool.core.io.resource.springframework.core.io.Resource
	 */
	protected String convertClassLoaderURL(URL url) {
		if(url.getProtocol().startsWith(ResourceUtils.URL_PROTOCOL_FILE)){
			return url.getPath().substring(1);
		}else if(url.getProtocol().startsWith(ResourceUtils.URL_PROTOCOL_VFS)){
			
		}
		return url.getProtocol() + url.getPath();
	}

	/**
	 * Search all {@link URLClassLoader} URLs for jar file references and add them to the
	 * given set of locations in the form of pointers to the root of the jar file content.
	 * @param classLoader the ClassLoader to search (including its ancestors)
	 * @param result the set of locations to add jar roots to
	 */
	protected void addAllClassLoaderJarRoots(ClassLoader classLoader, Set<String> result) {
		if (classLoader instanceof URLClassLoader) {
			try {
				for (URL url : ((URLClassLoader) classLoader).getURLs()) {
					if (ResourceUtils.isJarFileURL(url)) {
						result.add( ResourceUtils.JAR_URL_PREFIX + url.toString() + ResourceUtils.JAR_URL_SEPARATOR);
					}
				}
			}
			catch (Exception ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + ex);
				}
			}
		}
		if (classLoader != null) {
			try {
				addAllClassLoaderJarRoots(classLoader.getParent(), result);
			}
			catch (Exception ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader +
							"] does not support 'getParent()': " + ex);
				}
			}
		}
	}

	/**
	 * Find all locations that match the given location pattern via the
	 * Ant-style PathMatcher. Supports locations in jar files and zip files
	 * and in the file system.
	 * @param locationPattern the location pattern to match
	 * @return the result as Resource array
	 * @throws IOException in case of I/O errors
	 * @see #doFindPathMatchingJarResources
	 * @see #doFindPathMatchingFileResources
	 * @see org.springframework.util.PathMatcher
	 */
	protected String[] findPathMatchingLocations(String locationPattern) throws IOException {
		String rootDirPath = determineRootDir(locationPattern);
		String subPattern = locationPattern.substring(rootDirPath.length());
		String[] rootDirLocations = getLocations(rootDirPath);
		Set<String> result = new LinkedHashSet<String>(16);
		for (String rootDirLocation : rootDirLocations) {
			if (new URL(rootDirLocation).getProtocol().startsWith(ResourceUtils.URL_PROTOCOL_VFS)) {
				continue;
			}
			else if (isJarResource(rootDirLocation)) {
				result.addAll(doFindPathMatchingJarResources(rootDirLocation, subPattern));
			}
			else {
				result.addAll(doFindPathMatchingFileResources(rootDirLocation, subPattern));
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Resolved location pattern [" + locationPattern + "] to locations " + result);
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Determine the root directory for the given location.
	 * <p>Used for determining the starting point for file matching,
	 * resolving the root directory location to a {@code java.io.File}
	 * and passing it into {@code retrieveMatchingFiles}, with the
	 * remainder of the location as pattern.
	 * <p>Will return "/WEB-INF/" for the pattern "/WEB-INF/*.xml",
	 * for example.
	 * @param location the location to check
	 * @return the part of the location that denotes the root directory
	 * @see #retrieveMatchingFiles
	 */
	protected String determineRootDir(String location) {
		int prefixEnd = location.indexOf(":") + 1;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
			rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
	}

	/**
	 * Return whether the given location handle indicates a jar location
	 * that the {@code doFindPathMatchingJarResources} method can handle.
	 * <p>The default implementation checks against the URL protocols
	 * "jar", "zip" and "wsjar" (the latter are used by BEA WebLogic Server
	 * and IBM WebSphere, respectively, but can be treated like jar files).
	 * @param location the location handle to check
	 * (usually the root directory to start path matching from)
	 * @see #doFindPathMatchingJarResources
	 * @see hitool.core.resources.springframework.util.ResourceUtils#isJarURL
	 */
	protected boolean isJarResource(String location) throws IOException {
		return ResourceUtils.isJarURL(new URL(location));
	}

	/**
	 * Find all locations in jar files that match the given location pattern
	 * via the Ant-style PathMatcher.
	 * @param rootDirResource the root directory as Resource
	 * @param subPattern the sub pattern to match (below the root directory)
	 * @return a mutable Set of matching Resource instances
	 * @throws IOException in case of I/O errors
	 * @see java.net.JarURLConnection
	 * @see org.springframework.util.PathMatcher
	 */
	protected Set<String> doFindPathMatchingJarResources(String rootDirResource, String subPattern)
			throws IOException {

		URLConnection con = new URL(rootDirResource).openConnection();
		JarFile jarFile;
		String jarFileUrl;
		String rootEntryPath;
		boolean newJarFile = false;

		if (con instanceof JarURLConnection) {
			// Should usually be the case for traditional JAR files.
			JarURLConnection jarCon = (JarURLConnection) con;
			ResourceUtils.useCachesIfNecessary(jarCon);
			jarFile = jarCon.getJarFile();
			jarFileUrl = jarCon.getJarFileURL().toExternalForm();
			JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
		}
		else {
			// No JarURLConnection -> need to resort to URL file parsing.
			// We'll assume URLs of the format "jar:path!/entry", with the protocol
			// being arbitrary as long as following the entry format.
			// We'll also handle paths with and without leading "file:" prefix.
			String urlFile = new URL(rootDirResource).getFile();
			int separatorIndex = urlFile.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
			if (separatorIndex != -1) {
				jarFileUrl = urlFile.substring(0, separatorIndex);
				rootEntryPath = urlFile.substring(separatorIndex + ResourceUtils.JAR_URL_SEPARATOR.length());
				jarFile = getJarFile(jarFileUrl);
			}
			else {
				jarFile = new JarFile(urlFile);
				jarFileUrl = urlFile;
				rootEntryPath = "";
			}
			newJarFile = true;
		}

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Looking for matching locations in jar file [" + jarFileUrl + "]");
			}
			if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
				// Root entry path must end with slash to allow for proper matching.
				// The Sun JRE does not return a slash here, but BEA JRockit does.
				rootEntryPath = rootEntryPath + "/";
			}
			Set<String> result = new LinkedHashSet<String>(8);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
				JarEntry entry = entries.nextElement();
				String entryPath = entry.getName();
				if (entryPath.startsWith(rootEntryPath)) {
					String relativePath = entryPath.substring(rootEntryPath.length());
					if (getPathMatcher().match(subPattern, relativePath)) {
						result.add(relativePath);
					}
				}
			}
			return result;
		}
		finally {
			// Close jar file, but only if freshly obtained -
			// not from JarURLConnection, which might cache the file reference.
			if (newJarFile) {
				jarFile.close();
			}
		}
	}

	/**
	 * Resolve the given jar file URL into a JarFile object.
	 */
	protected JarFile getJarFile(String jarFileUrl) throws IOException {
		if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
			try {
				return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
			}
			catch (URISyntaxException ex) {
				// Fallback for URLs that are not valid URIs (should hardly ever happen).
				return new JarFile(jarFileUrl.substring(ResourceUtils.FILE_URL_PREFIX.length()));
			}
		}
		else {
			return new JarFile(jarFileUrl);
		}
	}

	/**
	 * Find all locations in the file system that match the given location pattern
	 * via the Ant-style PathMatcher.
	 * @param rootDirResource the root directory as Resource
	 * @param subPattern the sub pattern to match (below the root directory)
	 * @return a mutable Set of matching Resource instances
	 * @throws IOException in case of I/O errors
	 * @see #retrieveMatchingFiles
	 * @see org.springframework.util.PathMatcher
	 */
	protected Set<String> doFindPathMatchingFileResources(String rootDirResource, String subPattern)
			throws IOException {

		File rootDir;
		try {
			rootDir = new File(rootDirResource).getAbsoluteFile();
		}
		catch (Exception ex) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Cannot search for matching files underneath " + rootDirResource +
						" because it does not correspond to a directory in the file system", ex);
			}
			return Collections.emptySet();
		}
		return doFindMatchingFileSystemResources(rootDir, subPattern);
	}

	/**
	 * Find all locations in the file system that match the given location pattern
	 * via the Ant-style PathMatcher.
	 * @param rootDir the root directory in the file system
	 * @param subPattern the sub pattern to match (below the root directory)
	 * @return a mutable Set of matching Resource instances
	 * @throws IOException in case of I/O errors
	 * @see #retrieveMatchingFiles
	 * @see org.springframework.util.PathMatcher
	 */
	protected Set<String> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Looking for matching locations in directory tree [" + rootDir.getPath() + "]");
		}
		Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
		Set<String> result = new LinkedHashSet<String>(matchingFiles.size());
		for (File file : matchingFiles) {
			result.add(file.getAbsolutePath());
		}
		return result;
	}

	/**
	 * Retrieve files that match the given path pattern,
	 * checking the given directory and its subdirectories.
	 * @param rootDir the directory to start from
	 * @param pattern the pattern to match against,
	 * relative to the root directory
	 * @return a mutable Set of matching Resource instances
	 * @throws IOException if directory contents could not be retrieved
	 */
	protected Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
		if (!rootDir.exists()) {
			// Silently skip non-existing directories.
			if (LOG.isDebugEnabled()) {
				LOG.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
			}
			return Collections.emptySet();
		}
		if (!rootDir.isDirectory()) {
			// Complain louder if it exists but is no directory.
			if (LOG.isWarnEnabled()) {
				LOG.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
			}
			return Collections.emptySet();
		}
		if (!rootDir.canRead()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() +
						"] because the application is not allowed to read the directory");
			}
			return Collections.emptySet();
		}
		String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
		if (!pattern.startsWith("/")) {
			fullPattern += "/";
		}
		fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
		Set<File> result = new LinkedHashSet<File>(8);
		doRetrieveMatchingFiles(fullPattern, rootDir, result);
		return result;
	}

	/**
	 * Recursively retrieve files that match the given pattern,
	 * adding them to the given result list.
	 * @param fullPattern the pattern to match against,
	 * with prepended root directory path
	 * @param dir the current directory
	 * @param result the Set of matching File instances to add to
	 * @throws IOException if directory contents could not be retrieved
	 */
	protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Searching directory [" + dir.getAbsolutePath() +
					"] for files matching pattern [" + fullPattern + "]");
		}
		File[] dirContents = dir.listFiles();
		if (dirContents == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
			}
			return;
		}
		for (File content : dirContents) {
			String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
			if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, currPath + "/")) {
				if (!content.canRead()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Skipping subdirectory [" + dir.getAbsolutePath() +
								"] because the application is not allowed to read the directory");
					}
				}
				else {
					doRetrieveMatchingFiles(fullPattern, content, result);
				}
			}
			if (getPathMatcher().match(fullPattern, currPath)) {
				result.add(content);
			}
		}
	}

}
