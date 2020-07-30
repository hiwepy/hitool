package hitool.core.io;

import java.io.File;
import java.util.Locale;

public abstract class FilenameUtils extends org.apache.commons.io.FilenameUtils {

	public static String getExtension(File file) {
		return FilenameUtils.getExtension(file.getAbsolutePath());
	}

	public static String getFullExtension(String file) {
		return "." + FilenameUtils.getExtension(file);
	}

	/**
	 * 根据无国际化标识的文件名称获取国际化文件名称
	 * 
	 * @param filename
	 *            : properties文件名称 ；如："message.properties"
	 * @return 带有国际化标识的文件名称 ；如："message_zh_CN.properties"
	 */
	public static String getBundleFileName(String filename) {
		// 获取文件对应的Locale对象
		Locale bundleLocale = getBundleLocale(filename);
		// 判断是否为空
		if (bundleLocale != null) {
			return filename;
		}
		// 获取系统默认的Locale（国家/语言环境）
		Locale currentLocale = Locale.getDefault();
		// 获取基本名称部分
		String baseName = getBaseName(filename);
		// 文件后缀
		String extension = getExtension(filename);
		// 返回带国际化标识的文件名
		return baseName + "_" + currentLocale.toString() + "." + extension;

	}

	/**
	 * 根据国际化文件名称解析无国际化标识的文件名称
	 * 
	 * @param filename
	 *            ： properties文件名称 ；如："message_zh_CN.properties"
	 * @return 无国际化标识的文件名称 ；如："message.properties"
	 */
	public static String getBundleName(String filename) {
		// 获取基本名称部分
		String baseName = getBaseName(filename);
		// 文件后缀
		String extension = getExtension(filename);
		// 获取文件对应的Locale对象
		Locale bundleLocale = getBundleLocale(filename);
		// 判断是否为空
		if (bundleLocale != null) {
			return baseName.substring(0, baseName.length()
					- bundleLocale.toString().length() + 1)
					+ "." + extension;
		}
		return baseName + "." + extension;
	}

	/**
	 * 根据国际化文件名称解析Locale对象
	 * 
	 * @param filename
	 *            ： properties文件名称 ；如："message_zh_CN.properties"
	 * @return java.util.Locale
	 */
	public static Locale getBundleLocale(String filename) {
		// 获取基本名称部分
		String baseName = getBaseName(filename);
		// 获取系统默认的Locale（国家/语言环境）
		Locale currentLocale = Locale.getDefault();
		if (baseName.endsWith(currentLocale.toString())) {
			return currentLocale;
		}
		// 遍历所有可访问的Locale
		Locale[] localeList = Locale.getAvailableLocales();
		for (Locale locale : localeList) {
			if (baseName.endsWith(locale.toString())) {
				return locale;
			}
		}
		return null;
	}

	/**
	 * 判断资源文件名称是否符合国际化资源表达式规范
	 * 
	 * @param filename
	 *            : properties文件名称 ；如："message_zh_CN.properties"
	 */
	public static boolean isBundleFile(String filename) {
		// 获取文件对应的Locale对象
		Locale bundleLocale = getBundleLocale(filename);
		// 判断是否为空
		return bundleLocale != null;
	}

}
