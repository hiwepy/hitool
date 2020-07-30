package hitool.core.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import hitool.core.io.FilenameUtils;
import hitool.core.io.support.PathMatchingResourcePatternResolver;
import hitool.core.io.support.PropertiesHolder;
import hitool.core.io.support.ResourcePatternResolver;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;
import hitool.core.resources.DefaultPropertiesPersister;
import hitool.core.resources.PropertiesPersister;
import hitool.core.resources.ResourceLoaderAware;

/**
 */
public class ReloadableBundleMessageSource extends AbstractMessageSource implements ResourceLoaderAware{

	private static final String MERGED_FILENAME = "Merged";
	
	protected static final String PROPERTIES_SUFFIX = ".properties";

	protected static final String XML_SUFFIX = ".xml";
	
	protected String[] basenames = new String[0];
	
	protected String defaultEncoding;

	protected long cacheMillis = -1;

	protected Properties fileEncodings;
	
	protected boolean concurrentRefresh = true;
	
	protected PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	
	protected ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
	
	/** Cache to hold filename lists per filename */
	protected final ConcurrentMap<String, List<String>> cachedFilenames = new ConcurrentHashMap<String, List<String>>();
	
	/** Cache to hold Resource lists per filename */
	protected final ConcurrentMap<String, List<Resource>> cachedResources = new ConcurrentHashMap<String, List<Resource>>();
	
	/** Cache to hold already loaded properties per filename */
	protected final ConcurrentMap<String, PropertiesHolder> cachedProperties = new ConcurrentHashMap<String, PropertiesHolder>();
	
	/** Cache to hold merged loaded properties per filename */
	private final ConcurrentMap<String, PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<String, PropertiesHolder>();

	
	public void setBasename(String basename) {
		setBasenames(basename);
	}
	
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			this.basenames = new String[basenames.length];
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				this.basenames[i] = basename.trim();
			}
		}
		else {
			this.basenames = new String[0];
		}
	}

	/**
	 * Set the default charset to use for parsing resource bundle files.
	 * <p>Default is none, using the {@code java.util.ResourceBundle}
	 * default encoding: ISO-8859-1.
	 * @since 3.1.3
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * Set per-file charsets to use for parsing properties files.
	 * <p>Only applies to classic properties files, not to XML files.
	 * @param fileEncodings Properties with filenames as keys and charset
	 * names as values. Filenames have to match the basename syntax,
	 * with optional locale-specific appendices: e.g. "WEB-INF/messages"
	 * or "WEB-INF/messages_en".
	 * @see #setBasenames
	 * @see hitool.core.resources.springframework.util.PropertiesPersister#load
	 */
	public void setFileEncodings(Properties fileEncodings) {
		this.fileEncodings = fileEncodings;
	}
	
	/**
	 * Set the number of seconds to cache loaded properties files.
	 * <ul>
	 * <li>Default is "-1", indicating to cache forever (just like
	 * {@code java.util.ResourceBundle}).
	 * <li>A positive number will cache loaded properties files for the given
	 * number of seconds. This is essentially the interval between refresh checks.
	 * Note that a refresh attempt will first check the last-modified timestamp
	 * of the file before actually reloading it; so if files don't change, this
	 * interval can be set rather low, as refresh attempts will not actually reload.
	 * <li>A value of "0" will check the last-modified timestamp of the file on
	 * every message access. <b>Do not use this in a production environment!</b>
	 * </ul>
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheMillis = (cacheSeconds * 1000);
	}
	
	/**
	 * Specify whether to allow for concurrent refresh behavior, i.e. one thread
	 * locked in a refresh attempt for a specific cached properties file whereas
	 * other threads keep returning the old properties for the time being, until
	 * the refresh attempt has completed.
	 * <p>Default is "true": This behavior is new as of Spring Framework 4.1,
	 * minimizing contention between threads. If you prefer the old behavior,
	 * i.e. to fully block on refresh, switch this flag to "false".
	 * @see #setCacheSeconds
	 */
	public void setConcurrentRefresh(boolean concurrentRefresh) {
		this.concurrentRefresh = concurrentRefresh;
	}

	/**
	 * Set the PropertiesPersister to use for parsing properties files.
	 * <p>The default is a DefaultPropertiesPersister.
	 * @see hitool.core.resources.springframework.util.DefaultPropertiesPersister
	 */
	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
	}
	
	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	@Override
	protected String resolveCodeWithoutArguments(String code) {
		if (this.cacheMillis < 0) {
			PropertiesHolder propHolder = getMergedProperties();
			String result = propHolder.getProperty(code);
			if (result != null) {
				return result;
			}
		}
		else {
			for (String basename : this.basenames) {
				List<String> filenames = calculateAllFilenames(basename);
				for (String filename : filenames) {
					PropertiesHolder propHolder = getProperties(filename);
					String result = propHolder.getProperty(code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected MessageFormat resolveCode(String code) {
		if (this.cacheMillis < 0) {
			PropertiesHolder propHolder = getMergedProperties();
			MessageFormat result = propHolder.getMessageFormat(code);
			if (result != null) {
				return result;
			}
		}
		else {
			for (String basename : this.basenames) {
				List<String> filenames = calculateAllFilenames(basename);
				for (String filename : filenames) {
					PropertiesHolder propHolder = getProperties(filename);
					MessageFormat result = propHolder.getMessageFormat(code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get a PropertiesHolder that contains the actually visible properties
	 * for a Locale, after merging all specified resource bundles.
	 * Either fetches the holder from the cache or freshly loads it.
	 * <p>Only used when caching resource bundle contents forever, i.e.
	 * with cacheSeconds < 0. Therefore, merged properties are always
	 * cached forever.
	 */
	protected PropertiesHolder getMergedProperties() {
		PropertiesHolder mergedHolder = this.cachedMergedProperties.get(MERGED_FILENAME);
		if (mergedHolder != null) {
			return mergedHolder;
		}
		Properties mergedProps = new Properties();
		mergedHolder = new PropertiesHolder(mergedProps, -1);
		for (int i = this.basenames.length - 1; i >= 0; i--) {
			List<String> filenames = calculateAllFilenames(this.basenames[i]);
			for (int j = filenames.size() - 1; j >= 0; j--) {
				String filename = filenames.get(j);
				PropertiesHolder propHolder = getProperties(filename);
				if (propHolder.getProperties() != null) {
					mergedProps.putAll(propHolder.getProperties());
				}
			}
		}
		PropertiesHolder existing = this.cachedMergedProperties.putIfAbsent(MERGED_FILENAME, mergedHolder);
		if (existing != null) {
			mergedHolder = existing;
		}
		return mergedHolder;
	}
	
	protected List<String> calculateAllFilenames(String basename) {
		List<String> locations = this.cachedFilenames.get(basename);
		if (locations != null) {
			return locations;
		}
		locations = new ArrayList<String>(7);
		locations.addAll(calculateFilenamesForBasename(basename));
		if (locations == null) {
			locations = new ArrayList<String>();
			List<String> existing = this.cachedFilenames.putIfAbsent(basename, locations);
			if (existing != null) {
				locations = existing;
			}
		}
		return locations;
	}
	
	protected List<String> calculateFilenamesForBasename(String basename) {
		List<String> result = new ArrayList<String>(3);
		//解析Resource资源
		List<Resource> resourceList = calculateResourcesForBasename(basename);
		//对处理后的路径进行处理
		for (int i = 0; i < resourceList.size(); i++) {
			try {
				Resource resource = resourceList.get(i);
				String filepath = resource.getFile().getAbsolutePath();
				result.add(FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	protected List<Resource> calculateResourcesForBasename(String basename) {
		Assert.hasText(basename, "Basename must not be empty");
		List<Resource> resourceList = this.cachedResources.get(basename);
		if (resourceList != null) {
			return resourceList;
		}
		//解析Resource资源
		resourceList = new ArrayList<Resource>(7);
		//增加表达式路径支持
		if(resourceLoader instanceof ResourcePatternResolver){
			Resource[] resources = null;
			try {
				//spring 资源路径匹配解析器
				//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
				//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
				//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
				ResourcePatternResolver resourceResolver = (ResourcePatternResolver) resourceLoader;
				resources = resourceResolver.getResources(basename + PROPERTIES_SUFFIX);
				if (resources == null || resources.length == 0 ) {
					resources = resourceResolver.getResources(basename + XML_SUFFIX);
				}
			} catch (IOException e) {
				LOG.debug("No properties file found for [" + basename + "] - neither plain properties nor XML");
			}
			for(Resource resource : resources){
				if (resource.exists() && resource.isReadable()) {
					resourceList.add(resource);
				}
			}
		}else{
			Resource resource = this.resourceLoader.getResource(basename + PROPERTIES_SUFFIX);
			if (!resource.exists()) {
				resource = this.resourceLoader.getResource(basename + XML_SUFFIX);
			}
			if (resource.exists() && resource.isReadable()) {
				resourceList.add(resource);
			}
		}
		if (resourceList == null) {
			resourceList = new ArrayList<Resource>();
			List<Resource> existing = this.cachedResources.putIfAbsent(basename, resourceList);
			if (existing != null) {
				resourceList = existing;
			}
		}
		return resourceList;
	}

	
	/**
	 * Get a PropertiesHolder for the given filename, either from the
	 * cache or freshly loaded.
	 * @param filename the bundle filename (basename + Locale)
	 * @return the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder getProperties(String filename) {
		PropertiesHolder propHolder = this.cachedProperties.get(filename);
		long originalTimestamp = -2;
	
		if (propHolder != null) {
			originalTimestamp = propHolder.getRefreshTimestamp();
			if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - this.cacheMillis) {
				// Up to date
				return propHolder;
			}
		}
		else {
			propHolder = new PropertiesHolder();
			PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
			if (existingHolder != null) {
				propHolder = existingHolder;
			}
		}
	
		// At this point, we need to refresh...
		if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0) {
			// A populated but stale holder -> could keep using it.
			if (!propHolder.refreshLock.tryLock()) {
				// Getting refreshed by another thread already ->
				// let's return the existing properties for the time being.
				return propHolder;
			}
		}
		else {
			propHolder.refreshLock.lock();
		}
		try {
			PropertiesHolder existingHolder = this.cachedProperties.get(filename);
			if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
				return existingHolder;
			}
			return refreshProperties(filename, propHolder);
		}
		finally {
			propHolder.refreshLock.unlock();
		}
	}
	
	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be {@code null} if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 * @param filename the bundle filename (basename + Locale)
	 * @param propHolder the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
		long refreshTimestamp = (this.cacheMillis < 0 ? -1 : System.currentTimeMillis());
		
		Resource resource = this.resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
		if (!resource.exists()) {
			resource = this.resourceLoader.getResource(filename + XML_SUFFIX);
		}
		if (resource.exists()) {
			long fileTimestamp = -1;
			if (this.cacheMillis >= 0) {
				// Last-modified timestamp of file will just be read if caching with timeout.
				try {
					fileTimestamp = resource.lastModified();
					if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
						}
						propHolder.setRefreshTimestamp(refreshTimestamp);
						return propHolder;
					}
				}
				catch (IOException ex) {
					// Probably a class path resource: cache it forever.
					if (LOG.isDebugEnabled()) {
						LOG.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
					}
					fileTimestamp = -1;
				}
			}
			try {
				Properties props = loadProperties(resource, filename);
				propHolder = new PropertiesHolder(props, fileTimestamp);
			}
			catch (IOException ex) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
				}
				// Empty holder representing "not valid".
				propHolder = new PropertiesHolder();
			}
		}
	
		else {
			// Resource does not exist.
			if (LOG.isDebugEnabled()) {
				LOG.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
			}
			// Empty holder representing "not found".
			propHolder = new PropertiesHolder();
		}
	
		propHolder.setRefreshTimestamp(refreshTimestamp);
		this.cachedProperties.put(filename, propHolder);
		return propHolder;
	}
	
	/**
	 * Load the properties from the given resource.
	 * @param resource the resource to load from
	 * @param filename the original bundle filename (basename + Locale)
	 * @return the populated Properties instance
	 * @throws IOException if properties loading failed
	 */
	protected Properties loadProperties(Resource resource, String filename) throws IOException {
		InputStream is = resource.getInputStream();
		Properties props = new Properties();
		try {
			if (resource.getFilename().endsWith(XML_SUFFIX)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Loading properties [" + resource.getFilename() + "]");
				}
				this.propertiesPersister.loadFromXml(props, is);
			}
			else {
				String encoding = null;
				if (this.fileEncodings != null) {
					encoding = this.fileEncodings.getProperty(filename);
				}
				if (encoding == null) {
					encoding = this.defaultEncoding;
				}
				if (encoding != null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
					}
					this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
				}
				else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Loading properties [" + resource.getFilename() + "]");
					}
					this.propertiesPersister.load(props, is);
				}
			}
			return props;
		}
		finally {
			is.close();
		}
	}
	
	
	/**
	 * Clear the resource bundle cache.
	 * Subsequent resolve calls will lead to reloading of the properties files.
	 */
	public void clearCache() {
		LOG.debug("Clearing entire resource bundle cache");
		this.cachedProperties.clear();
	}
	
	/**
	 * Clear the resource bundle caches of this MessageSource and all its ancestors.
	 * @see #clearCache
	 */
	public void clearCacheIncludingAncestors() {
		clearCache();
		/*if (getParentMessageSource() instanceof ReloadableResourceBundleMessageSource) {
			((ReloadableResourceBundleMessageSource) getParentMessageSource()).clearCacheIncludingAncestors();
		}*/
	}
	 

	public String getStringOrNull(PropertiesResource resource, String key) {
		try {
			return resource.getString(key);
		}
		catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new PathMatchingResourcePatternResolver());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + ": locations=[" + StringUtils.arrayToCommaDelimitedString(this.basenames) + "]";
	}
	
	 
}
