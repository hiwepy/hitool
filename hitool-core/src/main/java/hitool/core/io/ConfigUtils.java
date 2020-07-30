package hitool.core.io;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public abstract class ConfigUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);
	protected static final String XML_FILE_EXTENSION = ".xml";
	protected static final String ENCODING = "UTF-8";
	protected static ConcurrentMap<String, Properties> cachedProperties = new ConcurrentHashMap<String, Properties>();

	public static <T> void initPropertiesConfig(T config, String location,
			String prefix) {
		if (cachedProperties.get(location) == null) {
			cachedProperties.put(location,
					ConfigUtils.getProperties(config.getClass(), location));
		}
		Properties properties = cachedProperties.get(location);
		if (!properties.isEmpty()) {
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo
						.getPropertyDescriptors();
				for (PropertyDescriptor propDes : propertyDescriptors) {
					String key = prefix + "." + propDes.getName();
					// 如果是class属性 跳过
					if (!properties.containsKey(key)
							|| propDes.getName().equals("class")
							|| propDes.getReadMethod() == null
							|| propDes.getWriteMethod() == null) {
						continue;
					}
					boolean accessible = propDes.getWriteMethod()
							.isAccessible();
					if (!accessible) {
						propDes.getWriteMethod().setAccessible(true);
					}
					propDes.getWriteMethod()
							.invoke(config,
									StringUtils.getSafeStr(properties
											.getProperty(key)));
					propDes.getWriteMethod().setAccessible(accessible);
				}
			} catch (Exception e) {
				LOG.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	public static Properties getProperties(Class clazz, String location) {
		if (location != null) {
			Properties ret = cachedProperties.get(location);
			if (ret != null) {
				return ret;
			}
			ret = new Properties();
			synchronized (clazz) {
				InputStream stream = null;
				try {
					stream = ConfigUtils.getInputStream(clazz, location);
					String filename = FilenameUtils.getName(location);
					if (filename != null
							&& filename.endsWith(XML_FILE_EXTENSION)) {
						stream = ConfigUtils.getInputStream(clazz, location);
						ret.loadFromXML(stream);
					} else {
						ret.load(new InputStreamReader(stream, ENCODING));
					}
				} catch (IOException e) {
					LOG.error(e.getMessage());
				} finally {
					IOUtils.closeQuietly(stream);
				}
			}
			Properties existing = cachedProperties.putIfAbsent(location, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}

	public static Properties getNewProperties(Class clazz, String location) {
		if (location != null) {
			Properties ret = new Properties();
			synchronized (clazz) {
				InputStream stream = null;
				try {
					stream = ConfigUtils.getInputStream(clazz, location);
					String filename = FilenameUtils.getName(location);
					if (filename != null
							&& filename.endsWith(XML_FILE_EXTENSION)) {
						stream = ConfigUtils.getInputStream(clazz, location);
						ret.loadFromXML(stream);
					} else {
						ret.load(new InputStreamReader(stream, ENCODING));
					}
				} catch (IOException e) {
					LOG.error(e.getMessage());
				} finally {
					IOUtils.closeQuietly(stream);
				}
			}
			return ret;
		}
		return null;
	}

	public static Properties getNewProperties(Object lock, File location) {
		if (location != null && location.exists() && location.isFile()) {
			Properties ret = new Properties();
			synchronized (lock) {
				InputStream stream = null;
				try {
					stream = new FileInputStream(location);
					ret.load(new InputStreamReader(stream, ENCODING));
				} catch (IOException e) {
					LOG.error(e.getMessage());
				} finally {
					IOUtils.closeQuietly(stream);
				}
			}
			return ret;
		}
		return null;
	}

	public static InputStream getInputStream(Class clazz, String location)
			throws IOException {
		URL url = ResourceUtils.getResourceAsURL(location, clazz);
		InputStream input = (url != null) ? url.openStream() : null;
		if (input == null) {
			throw new IllegalArgumentException("[" + location
					+ "] is not found!");
		}
		return input;
	}

}
