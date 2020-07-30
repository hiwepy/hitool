/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.support;

import java.io.IOException;

import hitool.core.io.ResourceUtils;


public interface LocationPatternResolver {
	

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

	/**
	 * Pseudo URL prefix for all matching resources from the class path: "classpath*:"
	 * This differs from ResourceLoader's classpath URL prefix in that it
	 * retrieves all matching resources for a given name (e.g. "/beans.xml"),
	 * for example in the root of all deployed JAR files.
	 * @see hitool.core.io.resource.springframework.core.io.ResourceLoader#CLASSPATH_URL_PREFIX
	 */
	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	 
	/**
	 *  根据给出的locationPattern解析出真实的路径值数组
	 * @param locationPattern ： 解析出来的路径
	 * @return
	 */
	String[] getLocations(String locationPattern) throws IOException;
	
}
