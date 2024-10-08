package hitool.freemarker.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.ServletContext;

import hitool.freemarker.loader.ClasspathTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.utility.HtmlEscape;
import freemarker.template.utility.XmlEscape;

public abstract class ConfigurationUtils {
	
	private static String charset = "UTF-8";
	// 创建 Configuration 实例
	private static Configuration DEFAULT_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_23);
	
	/*
	 * 配置默认配置实例Cofiguration
	 */
	public static Configuration getDefaultConfiguration() {
		
		
		DEFAULT_CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		DEFAULT_CONFIGURATION.setLocalizedLookup(false);
		DEFAULT_CONFIGURATION.setWhitespaceStripping(true);
		
		/*configuration.setAutoFlush(autoFlush)
		configuration.setCustomAttribute(name, value)
		configuration.setDateFormat(dateFormat)
		configuration.setDateTimeFormat(dateTimeFormat)
		configuration.setDefaultEncoding(encoding)
		configuration.setNumberFormat(numberFormat)*/
		DEFAULT_CONFIGURATION.setSharedVariable("fmXmlEscape", new XmlEscape());
		DEFAULT_CONFIGURATION.setSharedVariable("fmHtmlEscape", new HtmlEscape());
		
		DEFAULT_CONFIGURATION.setEncoding(Locale.getDefault(), charset);  
		DEFAULT_CONFIGURATION.setOutputEncoding(charset);
		
		/*configuration.setAPIBuiltinEnabled(value);
		configuration.setAutoFlush(autoFlush);
		configuration.setAutoIncludes(templateNames);
		configuration.setBooleanFormat(booleanFormat);
		configuration.setClassicCompatible(classicCompatibility);
		configuration.setClassicCompatibleAsInt(classicCompatibility);
		configuration.setClassLoaderForTemplateLoading(classLoader, basePackagePath);
		configuration.setCustomAttribute(name, value);
		configuration.setDateFormat(dateFormat);
		configuration.setDateTimeFormat(dateTimeFormat);
		configuration.setDefaultEncoding(encoding);
		configuration.setDirectoryForTemplateLoading(dir);
		configuration.setEncoding(locale, encoding);
		configuration.setEncoding(Locale.getDefault(), Docx4jProperties.getProperty("docx4j.freemarker.input.encoding", Docx4jConstants.DEFAULT_CHARSETNAME));  
		configuration.setLocale(locale);
		configuration.setLocalizedLookup(localizedLookup);
		configuration.setLocalizedLookup(false);
		configuration.setLogTemplateExceptions(value);
		configuration.setNamingConvention(namingConvention);
		configuration.setNewBuiltinClassResolver(newBuiltinClassResolver);
		configuration.setNumberFormat(numberFormat);
		configuration.setObjectWrapper(objectWrapper);
		configuration.setOutputEncoding(outputEncoding);
		configuration.setOutputEncoding(Docx4jProperties.getProperty("docx4j.freemarker.output.encoding", Docx4jConstants.DEFAULT_CHARSETNAME));
		configuration.setServletContextForTemplateLoading(servletContext, path);
		configuration.setSetting(name, value);
		configuration.setSettings(propsIn);
		configuration.setSettings(props);
		configuration.setSharedVariable("fmXmlEscape", new XmlEscape());
		configuration.setSharedVariable("fmHtmlEscape", new HtmlEscape());
		configuration.setSharedVariable(name, value);
		configuration.setSharedVariable(name, tm);
		configuration.setSharedVaribles(map);
		configuration.setShowErrorTips(showTips);
		configuration.setSQLDateAndTimeTimeZone(tz);
		configuration.setStrictBeanModels(strict);
		configuration.setTagSyntax(tagSyntax);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		configuration.setTemplateLoader(templateLoader);
		configuration.setTemplateLookupStrategy(templateLookupStrategy);
		configuration.setTemplateNameFormat(templateNameFormat);
		configuration.setTemplateUpdateDelayMilliseconds(millis);
		configuration.setTimeFormat(timeFormat);
		configuration.setTimeZone(timeZone);
		configuration.setURLEscapingCharset(urlEscapingCharset);
		configuration.setWhitespaceStripping(true);*/
		
		return (Configuration) DEFAULT_CONFIGURATION.clone();
	}
	
	/*
	 * 创建基于【classpath根目录】 的配置实例Cofiguration
	 * @param clazz
	 * @return
	 */
	public static Configuration getClassRootConfiguration(Class<?> clazz) {
		return ConfigurationUtils.getConfiguration( clazz, "/");
	}
	
	/*
	 * 创建基于【指定class所在目录】 的配置实例Cofiguration
	 * @param clazz
	 * @return
	 */
	public static Configuration getClasspathConfiguration(Class<?> clazz) {
		String clazzPath = clazz.getName().replace(".", "/");
		return ConfigurationUtils.getConfiguration( clazz, "/" + clazzPath.substring(0, clazzPath.lastIndexOf("/")));
	}
	
	/*
	 * 创建基于【指定classpath目录】 的配置实例Cofiguration
	 * @param clazz
	 * @param pathPrefix
	 * @return
	 */
	public static Configuration getConfiguration(Class<?> clazz, String pathPrefix) {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(clazz != null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setClassForTemplateLoading(clazz, pathPrefix);
		}
		return configuration;
	}
	
	/*
	 * 创建基于Servlet上下文的配置实例Cofiguration
	 * @param sctxt
	 * @return
	 */
	public static Configuration getContextRootConfiguration(ServletContext sctxt) {
		return ConfigurationUtils.getConfiguration( sctxt, sctxt.getRealPath(""));
	}
	
	/*
	 * 创建基于Servlet上下文的配置实例Cofiguration
	 * @param sctxt
	 * @param pathPrefix
	 * @return
	 */
	public static Configuration getConfiguration(ServletContext sctxt, String pathPrefix) {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(sctxt != null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setServletContextForTemplateLoading(sctxt, pathPrefix);
		}
		return configuration;
	}

	 /*
	  * 
	  * 创建基于文件系统的配置实例Cofiguration
	  * <pre>
	  *（1）src目录下的目录（template在src下）  
      *	cfg.setDirectoryForTemplateLoading(new File("src/template"));  
      *	（2）完整路径（template在src下）  
      *	cfg.setDirectoryForTemplateLoading(new File( "D:/cpic-env/workspace/javaFreemarker/src/template"));  
      *	（3）工程目录下的目录（template/main在工程下）--推荐 
      *	cfg.setDirectoryForTemplateLoading(new File("src/template"));
      * </pre> 
	  * @param directory
	  * @return
	  * @throws IOException
	  */
	public static Configuration getConfiguration(File directory) throws IOException {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(directory!=null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setDirectoryForTemplateLoading(directory);
		}
		return configuration;
	}
	
	public static Configuration getSpringClasspathConfiguration() {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
		// classpath根目录
		configuration.setTemplateLoader(new ClasspathTemplateLoader());
		return configuration;
	}
	
}
