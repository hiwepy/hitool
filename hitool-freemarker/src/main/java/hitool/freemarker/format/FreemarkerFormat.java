package hitool.freemarker.format;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.XmlEscape;

/*
 * Freemarker框架html格式工具
 */
public abstract class FreemarkerFormat{
	
	protected static Logger LOG = LoggerFactory.getLogger(FreemarkerFormat.class);
		
	private static Configuration configuration;
	private static String charset = "UTF-8";
	private static StringTemplateLoader sTmpLoader = new StringTemplateLoader();
	private static Map<String, Object> freemarkerVariables;
	
	/*
	 *  创建配置实例Cofiguration.
	 */
	public static Configuration getConfiguration() {
		if (null == configuration) {
			try {
				// 创建 Configuration 实例
				configuration = new Configuration(Configuration.VERSION_2_3_23);
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
				configuration.setLocalizedLookup(false);
				configuration.setWhitespaceStripping(true);
				freemarkerVariables.put("fmXmlEscape",new XmlEscape());
				configuration.setAllSharedVariables(new SimpleHash(freemarkerVariables,configuration.getObjectWrapper()));
				
				/*configuration.setAutoFlush(autoFlush)
				configuration.setCustomAttribute(name, value)
				configuration.setDateFormat(dateFormat)
				configuration.setDateTimeFormat(dateTimeFormat)
				configuration.setDefaultEncoding(encoding)
				configuration.setNumberFormat(numberFormat)*/
				configuration.setSharedVariable("fmXmlEscape", new XmlEscape());
				
				configuration.setEncoding(Locale.getDefault(), charset);  
				configuration.setOutputEncoding(charset);
			} catch (TemplateModelException e) {
				e.printStackTrace();
			}
		}
		return configuration;
	}
	
	/*
	 *  创建基于基于类路径的配置实例Cofiguration.
	 */
	public static Configuration getConfiguration(Class<?> clazz) {
		if (null == configuration) {
			// 创建 Configuration 实例
			configuration = getConfiguration();
		}
		if(clazz!=null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setClassForTemplateLoading(clazz, "/");
		}
		return configuration;
	}
	
	/*
	 *  创建基于Servlet上下文的配置实例Cofiguration
	 */
	public static Configuration getConfiguration(ServletContext sctxt,String dir) {
		if (null == configuration) {
			// 创建 Configuration 实例
			configuration = getConfiguration();
		}
		if(sctxt!=null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setServletContextForTemplateLoading(sctxt, dir);
		}
		return configuration;
	}
	
	 /*
	  * 
	  * 创建基于文件系统的配置实例Cofiguration
	  * <pre>
	  *	（1）src目录下的目录（template在src下）  
	  *	  cfg.setDirectoryForTemplateLoading(new File("src/template"));  
	  *	 （2）完整路径（template在src下）  
	  *	 	cfg.setDirectoryForTemplateLoading(new File( "D:/cpic-env/workspace/javaFreemarker/src/template"));  
	  *	 （3）工程目录下的目录（template/main在工程下）--推荐 
	  *		cfg.setDirectoryForTemplateLoading(new File("src/template"));
	  * </pre>  
	  */
	public static Configuration getConfiguration(File directory) throws IOException {
		if (null == configuration) {
			// 创建 Configuration 实例
			configuration = getConfiguration();
		}
		if(directory!=null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setDirectoryForTemplateLoading(directory);
		}
		return configuration;
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/*
	 *  格式化freemarker模板文本内容
	 */
	public static String toTextStatic(Object rootMap,String templateID,String templateSource) throws TemplateException,IOException {
		StringBuilderWriter out = new StringBuilderWriter();
		//添加字符内容模板
		sTmpLoader.putTemplate(templateID, templateSource);
		//设置模板加载方式
		getConfiguration().setTemplateLoader(sTmpLoader);
		// 获取模板
		Template template = getConfiguration().getTemplate(templateID);
		//对模板进行处理
		FreemarkerFormat.toStatic(template, rootMap==null ? new Object() : rootMap, out);
		//返回处理后的结果
		return out.toString();
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/*
	 * 
	 *  将模板生成静态html文件
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateFileName	: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param targetPath		: 生成的静态html文件存储路径
	 * @return boolean 是否生成成功
	 * @throws IOException 
	 * @throws TemplateException  
	 */
	public static boolean toStatic(Object rootMap, String templateDir,String templateFileName, String dest) throws TemplateException, IOException {
		return FreemarkerFormat.toStatic(rootMap, templateDir, templateFileName, new File(dest));
	}
	
	/*
	 * 基于文件系统。 比如加载/home/user/template下的模板文件。
	 *		cfg.setDirectoryForTemplateLoading(new File("/home/user/template"));cfg.getTemplate("Base.ftl");
	 *		这样就获得了/home/user/template/Base.ftl这个模板文件。
	 *@param rootMap
	 *@param templateDir
	 *@param templateFileName
	 *@param dest
	 *@return
	 *@throws TemplateException
	 *@throws IOException
	 */
	public static boolean toStatic(Object rootMap, String templateDir,String templateFileName, File dest) throws TemplateException, IOException {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
			return FreemarkerFormat.toStatic(rootMap, templateDir, templateFileName, out);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Error while processing FreeMarker template " + templateFileName,e);
			return false;
		} catch (FileNotFoundException e) {
			LOG.error("Error while processing FreeMarker template " + templateFileName,e);
			return false;
		}
	}
	
	/*
	 * 将基于文件系统的模板生成静态html文件
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateFileName	: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param out				: 生成的静态html文件的Writer输出对象
	 * @return
	 * @return  boolean 是否生成成功
	 * @throws IOException 
	 * @throws TemplateException   
	 */
	public static boolean toStatic(Object rootMap, String templateDir,String templateFileName, Writer out) throws TemplateException, IOException {
		//对模板进行处理
		return FreemarkerFormat.toStatic( rootMap,new File(templateDir), templateFileName, out);
	}
	
	public static boolean toStatic(Object rootMap, File templateDir,String templateFileName, Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = getConfiguration(templateDir).getTemplate(templateFileName);
		//对模板进行处理
		FreemarkerFormat.toStatic(template, rootMap, out);
		return true;
	}
	
	//------------------------------------------------------------------------------------------------------
	
	/*
	 * 
	 * 将模板生成静态html文件
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateFileName	: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @return
	 * @return  String 返回类型
	 * @throws IOException 
	 * @throws TemplateException  
	 */
	public static String toStatic(Object rootMap, String templateDir,String templateFileName) throws TemplateException, IOException {
		// 处理模板
		return FreemarkerFormat.toStatic(rootMap,new File(templateDir) , templateFileName);
	}
	
	public static String toStatic(Object rootMap, File templateDir,String templateFileName) throws TemplateException, IOException {
		// 获取模板
		Template template = getConfiguration(templateDir).getTemplate(templateFileName);
		// 处理模板
		return FreemarkerFormat.toStatic(template , rootMap);
	}
	
	public static String toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateFileName) throws TemplateException, IOException {
		// 获取模板
		Template template = getConfiguration(servletContext, templateDir).getTemplate(templateFileName);
		//对模板进行处理
		return FreemarkerFormat.toStatic(template, rootMap);
	}
	
	//------------------------------------------------------------------------------------------------------
	/*
	 * 将基于文件系统的模板生成静态html文件：比如： setServletContextForTemplateLoading(context, "/ftl") 就是 /WebRoot/ftl目录。
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateFileName	: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param out				: 生成的静态html文件的Writer输出对象
	 * @return
	 * @return  boolean 是否生成成功
	 * @throws IOException 
	 * @throws TemplateException   
	 */
	public static boolean toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateFileName, Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = getConfiguration(servletContext, templateDir).getTemplate(templateFileName);
		//对模板进行处理
		FreemarkerFormat.toStatic(template, rootMap, out);
		return true;
	}
	
	public static boolean toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateFileName, File dest) throws TemplateException, IOException {
		
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
			return FreemarkerFormat.toStatic(servletContext, rootMap, templateDir, templateFileName, out);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Error while processing FreeMarker template " + templateFileName,e);
			return false;
		} catch (FileNotFoundException e) {
			LOG.error("Error while processing FreeMarker template " + templateFileName,e);
			return false;
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	
	public static void toStatic(Template template,Object rootMap,File dest) throws TemplateException,IOException {
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
			//设置编码格式
			template.setEncoding(charset);
			// 处理模版map数据 ,输出流
			template.process(rootMap, out);
			//刷新输出
			out.flush();
		} catch (UnsupportedEncodingException e) {
			LOG.error("Error while processing FreeMarker template " + dest.getName() ,e);
		} catch (FileNotFoundException e) {
			LOG.error("Error while processing FreeMarker template " + dest.getName() ,e);
		}finally {
			IOUtils.closeQuietly(out);
		}
	} 
	
	public static void toStatic(Template template,Object rootMap,Writer out) throws TemplateException,IOException {
		try {
			//设置编码格式
			template.setEncoding(charset);
			// 处理模版map数据 ,输出流
			template.process(rootMap, out);
			//刷新输出
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}
	} 
	
	public static String toStatic(Template template,Object rootMap) throws TemplateException,IOException {
		StringBuilderWriter out = new StringBuilderWriter();
		try {
			//设置编码格式
			template.setEncoding(charset);
			// 处理模版map数据 ,输出流
			template.process(rootMap, out);
			//刷新输出
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}
		return out.toString();
	} 

}
