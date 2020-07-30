package hitool.freemarker.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.ConfigUtils;

/**
 * Base class for template engines.
 */
public abstract class BaseTemplateEngine implements TemplateEngine {
	
	protected static Logger LOG = LoggerFactory.getLogger(BaseTemplateEngine.class);

    /**
     * The default theme properties file name. Default is 'theme.properties'
     */
    public static final String DEFAULT_THEME_PROPERTIES_FILE_NAME = "theme.properties";

    private final Map<String, Properties> themeProps = new ConcurrentHashMap<String, Properties>();

    public Properties getThemeProps(ServletContext servletContext,Template template) {
        Properties props = themeProps.get(template.getTheme());
        if (props == null) {
            synchronized (themeProps) {
                try {
					props = readNewProperties(servletContext,template);
					themeProps.put(template.getTheme(), props);
				} catch (IOException e) {
				}
            }
        }
        return props;
    }

    private Properties readNewProperties(ServletContext servletContext,Template template) throws IOException {
        String propName = buildPropertyFilename(template);
        return loadProperties(servletContext,propName);
    }

    private Properties loadProperties(ServletContext servletContext,String propName) throws IOException {
        InputStream is = readProperty(servletContext,propName);
        Properties props = new Properties();
        if (is != null) {
            tryToLoadPropertiesFromStream(props, propName, is);
        }
        return props;
    }

    private InputStream readProperty(ServletContext servletContext,String propName) throws IOException {
        InputStream is = tryReadingPropertyFileFromFileSystem(propName);
        if (is == null) {
            is = readPropertyFromClasspath(propName);
        }
        if (is == null) {
            is = readPropertyUsingServletContext(servletContext,propName);
        }
        return is;
    }

    private InputStream readPropertyUsingServletContext(ServletContext servletContext,String propName) {
        String path = propName.startsWith("/") ? propName : "/" + propName;
        if (servletContext != null) {
            return servletContext.getResourceAsStream(path);
        } else {
            LOG.warn("ServletContext is null, cannot obtain " + path);
            return null;
        }
    }

    /**
     * if its not in filesystem. let's try the classpath
     * @throws IOException 
     */
    private InputStream readPropertyFromClasspath(String propName) throws IOException {
        return ConfigUtils.getInputStream(getClass(), propName);
    }

    private void tryToLoadPropertiesFromStream(Properties props, String propName, InputStream is) {
        try {
            props.load(is);
        } catch (IOException e) {
            LOG.error("Could not load " + propName, e);
        } finally {
            tryCloseStream(is);
        }
    }

    private void tryCloseStream(InputStream is) {
        try {
            is.close();
        } catch (IOException io) {
            if (LOG.isWarnEnabled()) {
        	LOG.warn("Unable to close input stream", io);
            }
        }
    }

    private String buildPropertyFilename(Template template) {
        return template.getDir() + "/" + template.getTheme() + "/" + getThemePropertiesFileName();
    }

    /**
     * WW-1292 let's try getting it from the filesystem
     */
    private InputStream tryReadingPropertyFileFromFileSystem(String propName) {
        File propFile = new File(propName);
        try {
            return createFileInputStream(propFile);
        } catch (FileNotFoundException e) {
            if (LOG.isWarnEnabled()) {
        	LOG.warn("Unable to find file in filesystem [" + propFile.getAbsolutePath() + "]");
            }
            return null;
        }
    }

    private InputStream createFileInputStream(File propFile) throws FileNotFoundException {
        InputStream is = null;
        if (propFile.exists()) {
            is = new FileInputStream(propFile);
        }
        return is;
    }

    protected String getFinalTemplateName(Template template) {
        String t = template.toString();
        if (t.indexOf(".") <= 0) {
            return t + "." + getSuffix();
        }
        return t;
    }

    protected String getThemePropertiesFileName() {
        return DEFAULT_THEME_PROPERTIES_FILE_NAME;
    }

    protected abstract String getSuffix();

}
