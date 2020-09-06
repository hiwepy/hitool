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
package hitool.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.Assert;
import hitool.core.lang3.URLUtils;
import hitool.web.cookie.CookieModel;

public abstract class CookieUtils {

	/**
	 * Default path that cookies will be visible to: "/", i.e. the entire server.
	 */
	protected static final String DEFAULT_COOKIE_PATH = "/";

	protected static Logger LOG = LoggerFactory.getLogger(CookieUtils.class);
	
	/**
	 * Retrieve the first cookie with the given name. Note that multiple
	 * cookies can have the same name but different paths or domains.
	 * @param request current servlet request
	 * @param cookieName cookie name
	 * @return the first cookie with the given name, or {@code null} if none is found
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		return getCookie(request, null, cookieName, false);
	}
	
	/**
	 * 获得指定Cookie的值，并删除。
	 * @param name 名称
	 * @return 值
	 */
	public static Cookie getCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		return getCookie(request, response, cookieName, true);
	}
	/**
	 * 获得指定Cookie的值
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param cookieName 名字
	 * @param isRemove 是否移除
	 * @return 值
	 */
	public static Cookie getCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, boolean isRemove) {
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(cookieName, "cookieName must not be null");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					if (isRemove) {
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Add a cookie with the given value to the response, using the cookie descriptor settings of this generator.
	 * <p>Delegates to {@link #createCookie} for cookie creation.
	 * @param response the HTTP response to add the cookie to
	 * @param cookieName
	 * @param cookieDomain
	 * @param cookiePath
	 * @param cookieValue the value of the cookie to add
	 * @param cookieMaxAge
	 * @param cookieSecure
	 * @param cookieHttpOnly
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 * @see #setCookieMaxAge
	 */
	public static void addCookie(HttpServletResponse response, String cookieName,String cookieDomain,String cookiePath,String cookieValue,
			Integer cookieMaxAge,boolean cookieSecure,boolean cookieHttpOnly) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieName, cookieDomain, cookiePath, cookieValue);
		if (cookieMaxAge != null) {
			cookie.setMaxAge(cookieMaxAge);
		}
		if (cookieSecure) {
			cookie.setSecure(true);
		}
		if (cookieHttpOnly) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Added cookie with name [" + cookieName + "] and value [" + cookieValue + "]");
		}
	}
	
	public static void addCookie(HttpServletResponse response, CookieModel cookieModel) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieModel.getCookieName(),cookieModel.getCookieDomain(),cookieModel.getCookiePath(),cookieModel.getCookieValue());
		Integer maxAge = cookieModel.getCookieMaxAge();
		if (maxAge != null) {
			cookie.setMaxAge(maxAge);
		}
		if (cookieModel.isCookieSecure()) {
			cookie.setSecure(true);
		}
		if (cookieModel.isCookieHttpOnly()) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Added cookie with name [" + cookieModel.getCookieName() + "] and value [" + cookieModel.getCookieValue() + "]");
		}
	}
	
	/**
	 * Remove the cookie that this generator describes from the response.
	 * Will generate a cookie with empty value and max age 0.
	 * <p>Delegates to {@link #createCookie} for cookie creation.
	 * @param response the HTTP response to remove the cookie from
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 */
	public static void removeCookie(HttpServletResponse response,String cookieName) {
		Assert.notNull(response, "HttpServletResponse must not be null");
		Cookie cookie = createCookie(cookieName,"","","");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Removed cookie with name [" + cookieName + "]");
		}
	}

	/**
	 * 
	 *  Create a cookie with the given value, using the cookie descriptor settings of this generator (except for "cookieMaxAge").
	 * @param cookieName
	 * @param cookieDomain
	 * @param cookiePath
	 * @param cookieValue the value of the cookie to crate
	 * @return the cookie
	 */
	public static Cookie createCookie(String cookieName,String cookieDomain,String cookiePath,String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		if (cookieDomain != null) {
			cookie.setDomain(cookieDomain);
		}
		cookie.setPath(cookiePath);
		return cookie;
	}
	
	public static Cookie createCookie(CookieModel cookieModel) {
		Cookie cookie = new Cookie(cookieModel.getCookieName(), cookieModel.getCookieValue());
		if (cookieModel.getCookieDomain() != null) {
			cookie.setDomain(cookieModel.getCookieDomain());
		}
		cookie.setPath(cookieModel.getCookiePath());
		return cookie;
	}
	
	/**
	 * 设置 Cookie（生成时间为1天）
	 * @param name 名称
	 * @param value 值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, 60*60*24);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 生存时间（单位秒）
	 * @param uri 路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path) {
		setCookie(response, name, value, path, 60*60*24);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 生存时间（单位秒）
	 * @param uri 路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		setCookie(response, name, value, "/", maxAge);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 生存时间（单位秒）
	 * @param uri 路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		cookie.setValue(URLUtils.escape(value));
		response.addCookie(cookie);
	}

}
