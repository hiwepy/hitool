package hitool.freemarker.utils;

import java.io.File;
import java.io.IOException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class TemplateUtils {

	/**
	 * String文本模板加载器
	 */
	private static StringTemplateLoader sTmpLoader = new StringTemplateLoader();

	/**
	 * 获取以string文本为模板的FreeMarker模板对象
	 * @param templateName	：模板名称
	 * @param templateSource：模板内容
	 * @return
	 * @throws IOException
	 */
	public static Template getStringTemplate(String templateName,String templateSource) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getDefaultConfiguration();
		// 获取模板
		return TemplateUtils.getStringTemplate(configuration,templateName,templateSource);
	}
	
	/**
	 * 获取以string文本为模板的FreeMarker模板对象
	 * @param configuration	： Configuration对象
	 * @param templateName	：模板名称
	 * @param templateSource：模板内容
	 * @return
	 * @throws IOException
	 */
	public static Template getStringTemplate(Configuration configuration,String templateName,String templateSource) throws IOException{
		//添加字符内容模板
		sTmpLoader.putTemplate(templateName, templateSource);
		//设置模板加载方式
		configuration.setTemplateLoader(sTmpLoader);
		// 获取模板
		return configuration.getTemplate(templateName);
	}
	
	/**
	 * 以文件路径模板装载方式获取【指定文件目录】下的FreeMarker模板对象
	 * @param templateDir
	 * @param templateName
	 * @return
	 * @throws IOException
	 */
	public static Template getFileTemplate(File templateDir,String templateName) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getConfiguration(templateDir);
		// 获取模板
		return configuration.getTemplate(templateName);
	}
	
	/**
	 * 以classpath 模板装载方式获取【classpath根目录】下的FreeMarker模板对象
	 * @param clazz			： class对象
	 * @param pathPrefix	：模板文件所在目录
	 * @param templateName	：模板名称
	 * @return
	 * @throws IOException
	 */
	public static Template getClassRootTemplate(Class<?> clazz, String templateName) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getClassRootConfiguration(clazz);
		// 获取模板
		return configuration.getTemplate(templateName);
	}
	
	/**
	 * 以classpath 模板装载方式获取【指定class所在目录】下的FreeMarker模板对象
	 * @param clazz			： class对象
	 * @param templateName	：模板名称
	 * @return
	 * @throws IOException
	 */
	public static Template getClasspathTemplate(Class<?> clazz, String templateName) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getClasspathConfiguration(clazz);
		// 获取模板
		return configuration.getTemplate(templateName);
	}
	
	/**
	 *  以classpath 模板装载方式获取【指定classpath目录】下的FreeMarker模板对象
	 * @param clazz			： class对象
	 * @param pathPrefix	：模板文件所在目录
	 * @param templateName	：模板名称
	 * @return
	 * @throws IOException
	 */
	public static Template getClasspathTemplate(Class<?> clazz, String pathPrefix,String templateName) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getConfiguration(clazz,pathPrefix);
		// 获取模板
		return configuration.getTemplate(templateName);
	}
	
	public static Template getSpringClasspathTemplate(String resource) throws IOException{
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getSpringClasspathConfiguration();
		// 获取模板
		return configuration.getTemplate(resource);
	}
	
}
