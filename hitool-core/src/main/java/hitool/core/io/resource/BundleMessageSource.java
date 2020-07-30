package hitool.core.io.resource;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import hitool.core.beanutils.reflection.ClassUtils;
import hitool.core.io.FilenameUtils;
import hitool.core.io.ResourceUtils;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;
import hitool.core.resources.BeanClassLoaderAware;

public class BundleMessageSource extends AbstractMessageSource implements BeanClassLoaderAware{

	protected static final String PROPERTIES_SUFFIX = ".properties";

	protected static final String XML_SUFFIX = ".xml";
	
	private String[] basenames = new String[0];
	
	private String defaultEncoding;

	private ClassLoader bundleClassLoader;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	
	/**
	 * Cache to hold loaded ResourceBundles.
	 * This Map is keyed with the bundle basename, which holds a Map that is
	 * keyed with the Locale and in turn holds the ResourceBundle instances.
	 * This allows for very efficient hash lookups, significantly faster
	 * than the ResourceBundle class's own cache.
	 */
	private final ConcurrentMap<String, PropertiesResource> cachedPropertiesResources = new ConcurrentHashMap<String, PropertiesResource>();

	/**
	 * Cache to hold already generated MessageFormats.
	 * This Map is keyed with the ResourceBundle, which holds a Map that is
	 * keyed with the message code, which in turn holds a Map that is keyed
	 * with the Locale and holds the MessageFormat values. This allows for
	 * very efficient hash lookups without concatenated keys.
	 * @see #getMessageFormat
	 */
	private final ConcurrentMap<PropertiesResource, Map<String, MessageFormat>> cachedBundleMessageFormats = new ConcurrentHashMap<PropertiesResource, Map<String, MessageFormat>>();

	public void setBasename(String basename) {
		setBasenames(basename);
	}
	
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			//解析资源basename
			List<String> basenameList = new ArrayList<String>();
			for(String basename : basenames){
				Assert.hasText(basename, "Basename must not be empty");
				//解析资源basename
				basenameList.addAll(calculateFilenamesForBasename(basename));
			}
			//对处理后的路径进行处理
			this.basenames = new String[basenameList.size()];
			for (int i = 0; i < basenameList.size(); i++) {
				this.basenames[i] = basenameList.get(i);
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
	 * Set the ClassLoader to load resource bundles with.
	 * <p>Default is the containing BeanFactory's
	 * {@link hitool.core.resources.springframework.beans.factory.BeanClassLoaderAware bean ClassLoader},
	 * or the default ClassLoader determined by
	 * {@link org.springframework.util.ClassUtils#getDefaultClassLoader()}
	 * if not running within a BeanFactory.
	 */
	public void setBundleClassLoader(ClassLoader classLoader) {
		this.bundleClassLoader = classLoader;
	}

	/**
	 * Return the ClassLoader to load resource bundles with.
	 * <p>Default is the containing BeanFactory's bean ClassLoader.
	 * @see #setBundleClassLoader
	 */
	protected ClassLoader getBundleClassLoader() {
		return (this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader);
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
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
		Map<String,Resource> resourceMap = new HashMap<String,Resource>(7); 
		//解析Resource资源
		Resource[] resources = null;
		try {
			//spring 资源路径匹配解析器
			//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
			//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
			//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
			resources = ResourceUtils.getResources(basename + PROPERTIES_SUFFIX);
			if (resources == null || resources.length == 0 ) {
				resources = ResourceUtils.getResources(basename + XML_SUFFIX);
			}
		} catch (IOException e) {
			LOG.debug("No properties file found for [" + basename + "] - neither plain properties nor XML");
		}
		for(Resource resource : resources){
			try {
				if (resource.exists() && resource.isReadable()) {
					resourceMap.put(resource.getFile().getAbsolutePath(), resource);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (resourceMap == null) {
			resourceMap = new HashMap<String,Resource>(7); 
		}
		return new ArrayList<Resource>(resourceMap.values());
	}
	
	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	@Override
	protected String resolveCodeWithoutArguments(String code) {
		String result = null;
		for (String basename : basenames) {
			PropertiesResource resource = getPropertiesResource(basename);
			if (resource != null) {
				result = getStringOrNull(resource, code);
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Resolves the given message code as key in the registered resource bundles,
	 * using a cached MessageFormat instance per message code.
	 */
	@Override
	protected MessageFormat resolveCode(String code) {
		MessageFormat messageFormat = null;
		for (int i = 0; messageFormat == null && i < this.basenames.length; i++) {
			PropertiesResource resource = getPropertiesResource(this.basenames[i]);
			if (resource != null) {
				messageFormat = getMessageFormat(resource, code);
			}
		}
		return messageFormat;
	}

	protected PropertiesResource getPropertiesResource(String basename) {
		PropertiesResource resource = this.cachedPropertiesResources.get(basename);
		if (resource != null) {
			return resource;
		}
		try {
			resource = doGetResource(basename);
			if (resource == null) {
				resource = PropertiesResource.EMPTY_RESOURCE;
				this.cachedPropertiesResources.put(basename, resource);
			}
			return resource;
		}
		catch (MissingResourceException ex) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("PropertiesResource [" + basename + "] not found for MessageSource: " + ex.getMessage());
			}
			// Assume bundle not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}

	protected PropertiesResource doGetResource(String basename) throws MissingResourceException {
		if ((this.defaultEncoding != null && !"ISO-8859-1".equals(this.defaultEncoding))) {
			return PropertiesResource.getBundle(basename, this.defaultEncoding, getBundleClassLoader());
		}else {
			return PropertiesResource.getBundle(basename, getBundleClassLoader());
		}
	}

	protected MessageFormat getMessageFormat(PropertiesResource resource, String code) throws MissingResourceException {
		Map<String, MessageFormat> codeMap = this.cachedBundleMessageFormats.get(resource);
		if (codeMap != null) {
			MessageFormat result = codeMap.get(code);
			if (result != null) {
				return result;
			}
		}
		String msg = getStringOrNull(resource, code);
		if (msg != null) {
			if (codeMap == null) {
				codeMap = new HashMap<String, MessageFormat>();
				this.cachedBundleMessageFormats.put(resource, codeMap);
			}
			MessageFormat result = messageFormatHolder.createMessageFormat(msg);
			codeMap.put(code, result);
			return result;
		}
		return null;
	}

	protected String getStringOrNull(PropertiesResource resource, String key) {
		try {
			return resource.getString(key);
		}
		catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent message source.
			return null;
		}
	}

	/**
	 * Show the configuration of this MessageSource.
	 */
	@Override
	public String toString() {
		return getClass().getName() + ": basenames=[" + StringUtils.arrayToCommaDelimitedString(this.basenames) + "]";
	}
	
}
