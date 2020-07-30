package hitool.core.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.ResourceUtils;
import hitool.core.io.resource.Resource;
import hitool.core.io.support.EncodedResource;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;

public abstract class PropertiesUtils {
	
	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#setConfigLocation
	 * @see org.springframework.web.context.ContextLoader#CONFIG_LOCATION_PARAM
	 * @see org.springframework.web.servlet.FrameworkServlet#setContextConfigLocation
	 */
	protected static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	protected static Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);
	//初始化配置文件：资源池
	protected static Properties cachedProperties = new Properties();
	
	/**
	 *  读取配置文件到缓存区
	 * @param location
	 * @param encoding
	 */
	public final static void initProperties(String location,String encoding) {
		Assert.notNull(location, " location is null!");
		try {
			LOG.info("Start loading properties file from [" + location + "]");
			//即多个资源文件路径之间用” ,; /t/n”分隔，解析成数组形式
			String[] locations = StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS);
			for (int i = 0; i < locations.length; i++) {
				if(locations[i] != null && locations[i].length() > 0){
					for (String location1 : locations) {
						//处理多个资源文件字符串数组  
						Resource[] resources = ResourceUtils.getResources(location1);
						for (Resource resource : resources) {
							try {
								LOG.info("Loading properties file from URL [" + resource.getFile().getPath() + "]");
								//加载Properties对象
								PropertiesLoaderUtils.fillProperties(cachedProperties, new EncodedResource(resource, encoding));
							}catch (IOException ex) {
								LOG.error("Could not load properties file from URL [" + resource.getFile().getPath() + "] Caused by:  " + ex.getMessage());
							}
						}
					}
				}
			}
			LOG.info("Properties file loaded successfully !");
		} catch (Exception ex) {
			LOG.error("Properties file loaded failed . Caused by:  " + ex.getMessage());
		}
	}
	
	//---------------------------------------------------------------------
	// Get/Set methods for java.util.Properties
	//---------------------------------------------------------------------

	public final static synchronized Properties getCachedProperties(){
		return cachedProperties;
	}
	
	public final static String getProperty(String key){
		return PropertiesUtils.getProperty(key,"");
	}
	
	public final static String getProperty(String key, String defaultValue) {
		Assert.notNull(key, " key is null!");
		//取值
		return getCachedProperties().getProperty(key,defaultValue);
	}

	public final static void setProperty(String key,String value){
		getCachedProperties().put(key, value);
	}

	//---------------------------------------------------------------------
	// Cache methods for java.util.Properties
	//---------------------------------------------------------------------

	
	public final static Properties getProperties(InputStream inStream) {
		Properties properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
			LOG.error("couldn't load properties ！", e);
		}
		return properties;
	}
	
	/**
	 * 根据location路径 获取 Properties；
	 * <p>注意：location 采用 spring 资源路径匹配解析器<ul>
	 * <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	 * <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
	 * </ul>
	 * </p>
	 */
	public final static Properties getProperties(String location) {
		Assert.notNull(location, " location is null!");
		Properties properties = new Properties();
		try {
			Resource[] resources = ResourceUtils.getResources(location);
			for (Resource resource : resources) {
				//加载Properties对象
				properties.putAll(PropertiesUtils.getProperties(resource));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
		return properties;
	}
	
	
	public final static Properties getProperties(Resource resource) {
		return PropertiesUtils.getProperties(resource, "UTF-8");
	}
	
	public final static Properties getProperties(Resource resource,String encoding) {
		Properties properties = new Properties();
		try {
			PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(resource, encoding));
		}catch (IOException ex) {
			LOG.warn("Could not load properties from " + resource.getFilename() + ": " + ex.getMessage());
		}
		return properties;
	}
	
	
	/**
	 * 加载 所有 匹配classpath*:**\\/*.properties的配置文件
	 * @return  Properties 返回类型
	 */
	public final static Properties getRootProperties(){
		return PropertiesUtils.getProperties("classpath*:*.properties");
	}

	/**
	 *  加载 所有 匹配classpath*:**\\/*.properties 配置文件
	 */
	public final static Properties getAllProperties(){
		return PropertiesUtils.getProperties("classpath*:**/*.properties");
	}
	
}



