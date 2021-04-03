package hitool.lrc4j.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import hitool.lrc4j.lrc.LRC;
import hitool.lrc4j.tar.TarName;
import hitool.lrc4j.tar.id.Al;
import hitool.lrc4j.tar.id.Ar;
import hitool.lrc4j.tar.id.By;
import hitool.lrc4j.tar.id.Offset;
import hitool.lrc4j.tar.id.Ti;
import hitool.lrc4j.tar.time.TimeTar;

/*
 * @className ： LRCReadUtils
 * @description ： 从文件中读取lrc
 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date ： Jan 25, 2016 2:42:45 PM
 */
public class LRCReadUtils {

	public static LRC readLRC(String location){
		return LRCReadUtils.readLRC(location, "UTF-8");
	}
	
	public static LRC readLRC(String location,String encoding) {
		URL url = getResource(location, LRCReadUtils.class);
		InputStream input = null;
		BufferedReader reader = null;
		try {
			input = (url != null) ? url.openStream() : null;
			if (input == null) {
				input = new FileInputStream(location);
			}
			if (input == null) {
				throw new IllegalArgumentException("[" + location + "] is not found!");
			}
			// br = new BufferedReader(new FileReader(path));
			reader = new BufferedReader(new InputStreamReader(input, encoding));
			StringBuffer sb = new StringBuffer();// 为了兼容不是标签开头的行[可能会影响效率]
			Pattern p;
			Matcher m;
			Al al = new Al();
			Ar ar = new Ar();
			By by = new By();
			Offset offset = new Offset();
			Ti ti = new Ti();
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				p = Pattern.compile("\\[\\w*?:.*?\\]");// 解析标识标签
				m = p.matcher(line);
				if (m.find()) {
					String tar = m.group();
					if (tar.length() > 3) {// 标准标签的必要条件
						if (tar.substring(0, 4).equalsIgnoreCase( "[" + TarName.AL + ":")) {
							al.setValue(tar.substring(4, tar.length() - 1));
						}
						if (tar.substring(0, 4).equalsIgnoreCase( "[" + TarName.AR + ":")) {
							ar.setValue(tar.substring(4, tar.length() - 1));
						}
						if (tar.substring(0, 4).equalsIgnoreCase( "[" + TarName.BY + ":")) {
							by.setValue(tar.substring(4, tar.length() - 1));
						}
						if (tar.substring(0, 4).equalsIgnoreCase( "[" + TarName.TI + ":")) {
							ti.setValue(tar.substring(4, tar.length() - 1));
						}
					}
					if (tar.length() > 7) {// 为时间补偿标签的必要条件
						if (tar.substring(0, 8).equalsIgnoreCase( "[" + TarName.OFFSET + ":")) {
							offset.setValue(tar.substring(8, tar.length() - 1));
						}
					}
				}
				sb.append(line); // 为解析时间标签做准备
			}
			p = Pattern.compile("\\[\\d{2,3}:\\d{2}(.\\d{2})?\\]");// 解析时间标签[负时间标签暂未处理]
			m = p.matcher(sb);
			m.find();
			int prev = m.end();// 上一个匹配的终点
			String prevGroup = m.group();// 上一个匹配的group
			List<TimeTar> timeTars = new ArrayList<TimeTar>();
			while (m.find()) {
				int start = prev;
				int end = m.start();
				prev = m.end();
				String txt = sb.substring(start, end);
				TimeTar timeTar = new TimeTar();
				timeTar.setValue(prevGroup);// 设置值 [xx:xx.xx]
				timeTar.setText(txt.toString());// 设置文本 [xx:xx.xx]后面的东西
				timeTars.add(timeTar);
				prevGroup = m.group();
			}
			TimeTar timeTar = new TimeTar();
			timeTar.setValue(prevGroup);// 设置值 [xx:xx.xx]
			timeTar.setText(sb.substring(prev, sb.length()));// 设置文本
																// [xx:xx.xx]后面的东西
			timeTars.add(timeTar);
			LRC lrc = new LRC();
			lrc.setAl(al);
			lrc.setAr(ar);
			lrc.setBy(by);
			lrc.setOffset(offset);
			lrc.setTi(ti);
			lrc.setTimeTars(timeTars);
			return lrc;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(reader);
		}
		return null;
	}

	/*
	 * Load a given resource.
	 * <p/>
	 * This method will try to load the resource using the following methods (in
	 * order):
	 * <ul>
	 * <li>From {@link Thread#getContextClassLoader()
	 * Thread.currentThread().getContextClassLoader()}
	 * <li>From {@link Class#getClassLoader()
	 * ClassLoaderUtil.class.getClassLoader()}
	 * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader()
	 * }
	 * </ul>
	 * 
	 * @param resourceName
	 *            The name of the resource to load
	 * @param callingClass
	 *            The Class object of the calling object
	 */
	public static URL getResource(String resourceName, Class<?> callingClass) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				resourceName);

		if (url == null) {
			url = LRCReadUtils.class.getClassLoader().getResource(resourceName);
		}

		if (url == null) {
			ClassLoader cl = callingClass.getClassLoader();

			if (cl != null) {
				url = cl.getResource(resourceName);
			}
		}

		if ((url == null)
				&& (resourceName != null)
				&& ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
			return getResource('/' + resourceName, callingClass);
		}

		return url;
	}
}
