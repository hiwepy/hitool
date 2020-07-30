/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.core.resources;

import java.io.File;
import java.io.IOException;

import hitool.core.io.support.LocationPatternResolver;
import hitool.core.io.support.PathMatchingLocationPatternResolver;

/**
 * 
 */
public class LocationUtils{

	//模拟 spring 资源路径匹配解析器
	//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getLocations(String name)”
	//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected static LocationPatternResolver resolver = new PathMatchingLocationPatternResolver();
	
	/**
	 * “classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * 带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getLocations(String
	 * name)”方法来查找通配符之前的资源，
	 * 然后通过模式匹配来获取匹配的资源。如“classpath:META-INF/*.LIST”将首先加载通配符之前的目录
	 * “META-INF”，然后再遍历路径进行子路径匹配从而获取匹配的资源。
	 * 
	 * @param regString
	 * @return
	 * @throws IOException
	 */
	public static String[] getRelativeLocations(String location) throws IOException {
		String[] rs = LocationUtils.getLocationPaths(location);
		String[] resouceArray = new String[rs.length];
		for (int i = 0; i < rs.length; i++) {
			String xdpath = rs[i].substring(rs[i].indexOf("classes") + 8);
			resouceArray[i] = xdpath;
		}
		return resouceArray;
	}

	public static String[] getLocationPaths(String location) throws IOException {
		String[] resouceArray = null;
		String[] resources = LocationUtils.getLocations(location);
		if (resources == null) {
			return new String[0];
		} else {
			resouceArray = new String[resources.length];
			for (int i = 0; i < resources.length; i++) {
				resouceArray[i] = resouceArray[i].substring(resouceArray[i].indexOf("classes") + 8);
			}
		}
		return resouceArray;
	}

	public static String[] getLocations(String location) throws IOException {
		return resolver.getLocations(location);
	}
	
	public static String[] getPropertiesPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:**/*.properties");
	}
	
	public static String[] getRootPropertiesPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:*.properties");
	}

	public static String[] getRuntimePropertiesPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:**/runtime*.properties");
	}
	
	public static String[] getConfigPropertiesPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:conf/**/*.properties");
	}
	
	public static String[] getLogPropertiesPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:**/logger*.properties");
	}
	
	public static String[] getXMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:**/*.xml");
	}
	
	public static String[] getSpringXMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/spring/**/*.xml");
	}

	public static String[] getHibernateXMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/hibernate/**/*.hbm.xml");
	}

	public static String[] getStruts1XMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/struts1/**/*.xml");
	}

	public static String[] getStruts2XMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/struts2/**/*.xml");
	}

	public static String[] getIbatisXMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/ibatis/**/*.xml");
	}

	public static String[] getMybatisXMLPaths() throws IOException {
		return LocationUtils.getLocationPaths("classpath*:config/mybatis/**/*.xml");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new File("D:\\YNote").toURI().toURL().getFile());
		
		//String[] list = SpringResourceLoader.getInstance().getLocationPaths("classpath*:config/**/*.properties");
		String[] list = LocationUtils.getLogPropertiesPaths();
		for (int i = 0; i < list.length; ++i) {
			System.out.println(list[i]);
		}
		
		//String[] list = ClasspathXMLResourceUtils.getLocationPaths("classpath*:config/**/*.properties");
		String[] list2 = LocationUtils.getXMLPaths();
		for (int i = 0; i < list2.length; ++i) {
			System.out.println(list[i]);
		}
	}
}
