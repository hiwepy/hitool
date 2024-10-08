package hitool.freemarker.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import jakarta.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/*
 * Freemarker框架html格式工具
 */
public abstract class FormatUtils{
	
	private static final Logger LOG = LoggerFactory.getLogger(FormatUtils.class);
	private static String charset = "UTF-8"; 
	
	public static Writer buildWriter(OutputStream outStream) throws UnsupportedEncodingException{
		return new BufferedWriter(new OutputStreamWriter(outStream,charset),2048);
	}
	
	// ====================基于String文本的FreeMarker模板处理==========================================================
	
	/*
	 * 格式化freemarker模板文本内容
	 * @param rootMap
	 * @param templateName
	 * @param templateSource
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static String toTextStatic(Object rootMap,String templateName,String templateSource) throws TemplateException,IOException {
		// 输出对象
		StringBuilderWriter out = new StringBuilderWriter();
		//对模板进行处理
		FormatUtils.toTextStatic(rootMap, templateName, templateSource, out);
		//返回处理后的结果
		return out.toString();
	}
	
	public static void toTextStatic(Object rootMap,String templateName,String templateSource, File dest) throws TemplateException,IOException {
		FormatUtils.toStatic(rootMap, templateName, templateSource, new FileOutputStream(dest));
	}
	
	
	public static void toTextStatic(Object rootMap,String templateName,String templateSource,OutputStream outStream) throws TemplateException,IOException {
		//对模板进行处理
		FormatUtils.toTextStatic(rootMap,templateName, templateSource, FormatUtils.buildWriter(outStream));
	}
	
	public static void toTextStatic(Object rootMap,String templateName,String templateSource,Writer out) throws TemplateException,IOException {
		// 创建 Configuration 实例
		Configuration configuration = ConfigurationUtils.getDefaultConfiguration();
		// 获取模板
		Template template = TemplateUtils.getStringTemplate(configuration,templateName, templateSource);
		//对模板进行处理
		FormatUtils.toStatic(template, rootMap==null ? new Object() : rootMap, out);
	}
	
	// ====================基于文件系统的FreeMarker模板处理==========================================================
	
	/*
	 * 
	 * 将模板生成静态html文件
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateName		: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @return String 返回类型
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static String toStatic(Object rootMap, String templateDir,String templateName) throws TemplateException, IOException {
		// 处理模板
		return FormatUtils.toStatic(rootMap,new File(templateDir) , templateName);
	}
	
	public static String toStatic(Object rootMap, File templateDir,String templateName) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getFileTemplate(templateDir,templateName);
		// 处理模板
		return FormatUtils.toStatic(template , rootMap);
	}
	
	/*
	 * 
	 * 基于文件系统。 比如加载/home/user/template下的模板文件。
	 *				cfg.setDirectoryForTemplateLoading(new File("/home/user/template"));cfg.getTemplate("Base.ftl");
	 *				这样就获得了/home/user/template/Base.ftl这个模板文件。
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateName		: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param dest				: 生成的静态html文件存储路径
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static boolean toStatic(Object rootMap, String templateDir,String templateName, File dest) throws TemplateException, IOException {
		return FormatUtils.toStatic(rootMap, templateDir, templateName, new FileOutputStream(dest));
	}

	public static boolean toStatic(Object rootMap, File templateDir,String templateName, File dest) throws TemplateException, IOException {
		return FormatUtils.toStatic(rootMap, templateDir, templateName, new FileOutputStream(dest));
	}
	
	public static boolean toStatic(Object rootMap, String templateDir,String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			return FormatUtils.toStatic(rootMap, templateDir, templateName, FormatUtils.buildWriter(outStream));
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	public static boolean toStatic(Object rootMap, File templateDir,String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			// 处理模板
			return FormatUtils.toStatic(rootMap , templateDir, templateName, FormatUtils.buildWriter(outStream) );
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	/*
	 * 
	 * 将基于文件系统的模板生成静态html文件
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateName		: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param out				: 生成的静态html文件的Writer输出对象
	 * @return boolean 是否生成成功
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static boolean toStatic(Object rootMap, String templateDir,String templateName, Writer out) throws TemplateException, IOException {
		//对模板进行处理
		return FormatUtils.toStatic( rootMap, new File(templateDir), templateName, out);
	}
	
	public static boolean toStatic(Object rootMap, File templateDir,String templateName, Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getFileTemplate(templateDir,templateName);
		//对模板进行处理
		FormatUtils.toStatic(template, rootMap, out);
		return true;
	}
	
	// ====================基于Servlet上下文的FreeMarker模板处理==========================================================
	
	public static String toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateName) throws TemplateException, IOException {
		// 获取模板
		Template template = ConfigurationUtils.getConfiguration(servletContext, templateDir).getTemplate(templateName);
		//对模板进行处理
		return FormatUtils.toStatic(template, rootMap);
	}
	
	public static boolean toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateName, File dest) throws TemplateException, IOException {
		// 处理模板
		return FormatUtils.toStatic(servletContext, rootMap, templateDir, templateName, new FileOutputStream(dest));
	}
	
	public static boolean toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			// 处理模板
			return FormatUtils.toStatic(servletContext, rootMap, templateDir, templateName, FormatUtils.buildWriter(outStream));
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	/*
	 * 将基于文件系统的模板生成静态html文件：比如： setServletContextForTemplateLoading(context, "/ftl") 就是 /WebRoot/ftl目录。
	 * @param rootMap			: 用于处理模板的属性Object映射
	 * @param templateDir		: 模板的存放目录
	 * @param templateName		: 模板文件名,模板存放目录的相对路径,例如"/view.ftl"
	 * @param out				: 生成的静态html文件的Writer输出对象
	 * @return  boolean 是否生成成功
	 * @throws IOException 
	 * @throws TemplateException   
	 */
	public static boolean toStatic(ServletContext servletContext,Object rootMap,String templateDir,String templateName, Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = ConfigurationUtils.getConfiguration(servletContext, templateDir).getTemplate(templateName);
		//对模板进行处理
		FormatUtils.toStatic(template, rootMap, out);
		return true;
	}
	
	// ====================基于classpath的FreeMarker模板处理==========================================================
	
	 
	public static String toStatic(Object rootMap, Class<?> clazz,String templateName) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getClasspathTemplate(clazz,templateName);
		// 处理模板
		return FormatUtils.toStatic(template , rootMap);
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz,String templateName, File dest) throws TemplateException, IOException {
		// 处理模板
		return FormatUtils.toStatic(rootMap , clazz , templateName, new FileOutputStream(dest) );
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz,String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			// 处理模板
			return FormatUtils.toStatic(rootMap, clazz ,templateName, FormatUtils.buildWriter(outStream));
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz,String templateName,Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getClasspathTemplate(clazz,templateName);
		// 处理模板
		FormatUtils.toStatic(template , rootMap , out);
		return true;
	}
	
	public static String toStatic(Object rootMap, Class<?> clazz, String pathPrefix,String templateName) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getClasspathTemplate(clazz, pathPrefix, templateName);
		// 处理模板
		return FormatUtils.toStatic(template , rootMap);
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz, String pathPrefix,String templateName, File dest) throws TemplateException, IOException {
		// 处理模板
		return FormatUtils.toStatic(rootMap , clazz , pathPrefix, templateName, new FileOutputStream(dest) );
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz, String pathPrefix,String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			return FormatUtils.toStatic(rootMap, clazz , pathPrefix, templateName, FormatUtils.buildWriter(outStream));
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	public static boolean toStatic(Object rootMap, Class<?> clazz,String pathPrefix,String templateName,Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getClasspathTemplate(clazz , pathPrefix, templateName);
		// 处理模板
		FormatUtils.toStatic(template , rootMap , out);
		return true;
	}
	
	// ====================基于Spring查找Template的FreeMarker模板处理==========================================================

	public static String toStatic(Object rootMap, String templateName) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getSpringClasspathTemplate(templateName);
		// 处理模板
		return FormatUtils.toStatic(template , rootMap);
	}
	
	public static boolean toStatic(Object rootMap, String templateName, File dest) throws TemplateException, IOException {
		// 处理模板
		return FormatUtils.toStatic(rootMap , templateName, new FileOutputStream(dest) );
	}
	
	public static boolean toStatic(Object rootMap, String templateName, OutputStream outStream) throws TemplateException, IOException {
		try {
			// 处理模板
			return FormatUtils.toStatic(rootMap, templateName, FormatUtils.buildWriter(outStream));
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	public static boolean toStatic(Object rootMap,String templateName,Writer out) throws TemplateException, IOException {
		// 获取模板
		Template template = TemplateUtils.getSpringClasspathTemplate(templateName);
		// 处理模板
		FormatUtils.toStatic(template , rootMap , out);
		return true;
	}
	
	// ====================基于Template的FreeMarker模板处理==========================================================

	public static void toStatic(Template template,Object rootMap,File dest) throws TemplateException,IOException {
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(dest);
			FormatUtils.toStatic(template, rootMap, FormatUtils.buildWriter(outStream));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Error while processing FreeMarker template " + dest.getName() ,e);
		} catch (FileNotFoundException e) {
			LOG.error("Error while processing FreeMarker template " + dest.getName() ,e);
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	} 
	
	@SuppressWarnings("deprecation")
	public static void toStatic(Template template,Object rootMap,Writer out) throws TemplateException,IOException {
		try {
			//设置编码格式
			template.setEncoding(charset);
			template.setOutputEncoding(charset);
			// 处理模版map数据 ,输出流
			template.process(rootMap, out);
			//刷新输出
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}
	} 
	
	@SuppressWarnings("deprecation")
	public static String toStatic(Template template,Object rootMap) throws TemplateException,IOException {
		StringBuilderWriter out = new StringBuilderWriter();
		try {
			//设置编码格式
			template.setEncoding(charset);
			template.setOutputEncoding(charset);
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
