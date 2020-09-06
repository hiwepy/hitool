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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;

public abstract class WebUtils {

	/**
	 * Standard Servlet 2.3+ spec request attributes for include URI and paths.
	 * <p>If included via a RequestDispatcher, the current resource will see the
	 * originating request. Its own URI and paths are exposed as request attributes.
	 */
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
	public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
	public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
	public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>If forwarded to via a RequestDispatcher, the current resource will see its
	 * own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
	public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
	public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
	public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

	/**
	 * Standard Servlet 2.3+ spec request attributes for error pages.
	 * <p>To be exposed to JSPs that are marked as error pages, when forwarding
	 * to them directly rather than through the servlet container's error page
	 * resolution mechanism.
	 */
	public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
	public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
	public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
	public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";


	/**
	 * Prefix of the charset clause in a content type String: ";charset="
	 */
	public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";

	/**
	 * Default character encoding to use when {@code request.getCharacterEncoding}
	 * returns {@code null}, according to the Servlet spec.
	 * @see ServletRequest#getCharacterEncoding
	 */
	public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

	/**
	 * Standard Servlet spec context attribute that specifies a temporary
	 * directory for the current web application, of type {@code java.io.File}.
	 */
	public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

	/**
	 * HTML escape parameter at the servlet context level
	 * (i.e. a context-param in {@code web.xml}): "defaultHtmlEscape".
	 */
	public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";

	/**
	 * Use of response encoding for HTML escaping parameter at the servlet context level
	 * (i.e. a context-param in {@code web.xml}): "responseEncodedHtmlEscape".
	 * @since 4.1.2
	 */
	public static final String RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM = "responseEncodedHtmlEscape";

	/**
	 * Web app root key parameter at the servlet context level
	 * (i.e. a context-param in {@code web.xml}): "webAppRootKey".
	 */
	public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";

	/** Default web app root key: "webapp.root" */
	public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";

	/** Name suffixes in case of image buttons */
	public static final String[] SUBMIT_IMAGE_SUFFIXES = {".x", ".y"};

	/** Key for the mutex session attribute */
	public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";

	/**
	 * Set a system property to the web application root directory.
	 * The key of the system property can be defined with the "webAppRootKey"
	 * context-param in {@code web.xml}. Default is "webapp.root".
	 * <p>Can be used for tools that support substition with {@code System.getProperty}
	 * values, like log4j's "${key}" syntax within log file locations.
	 * @param servletContext the servlet context of the web application
	 * @throws IllegalStateException if the system property is already set,
	 * or if the WAR file is not expanded
	 * @see #WEB_APP_ROOT_KEY_PARAM
	 * @see #DEFAULT_WEB_APP_ROOT_KEY
	 * @see WebAppRootListener
	 * @see Log4jWebConfigurer
	 */
	public static void setWebAppRootSystemProperty(ServletContext servletContext) throws IllegalStateException {
		Assert.notNull(servletContext, "ServletContext must not be null");
		String root = servletContext.getRealPath("/");
		if (root == null) {
			throw new IllegalStateException(
				"Cannot set web app root system property when WAR file is not expanded");
		}
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		String oldValue = System.getProperty(key);
		if (oldValue != null && !StringUtils.pathEquals(oldValue, root)) {
			throw new IllegalStateException(
				"Web app root system property already set to different value: '" +
				key + "' = [" + oldValue + "] instead of [" + root + "] - " +
				"Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
		}
		System.setProperty(key, root);
		servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
	}

	/**
	 * Remove the system property that points to the web app root directory.
	 * To be called on shutdown of the web application.
	 * @param servletContext the servlet context of the web application
	 * @see #setWebAppRootSystemProperty
	 */
	public static void removeWebAppRootSystemProperty(ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext must not be null");
		String param = servletContext.getInitParameter(WEB_APP_ROOT_KEY_PARAM);
		String key = (param != null ? param : DEFAULT_WEB_APP_ROOT_KEY);
		System.getProperties().remove(key);
	}

	/**
	 * Return whether default HTML escaping is enabled for the web application,
	 * i.e. the value of the "defaultHtmlEscape" context-param in {@code web.xml}
	 * (if any). Falls back to {@code false} in case of no explicit default given.
	 * @param servletContext the servlet context of the web application
	 * @return whether default HTML escaping is enabled (default is false)
	 * @deprecated as of Spring 4.1, in favor of {@link #getDefaultHtmlEscape}
	 */
	@Deprecated
	public static boolean isDefaultHtmlEscape(ServletContext servletContext) {
		if (servletContext == null) {
			return false;
		}
		String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
		return Boolean.valueOf(param);
	}

	/**
	 * Return whether default HTML escaping is enabled for the web application,
	 * i.e. the value of the "defaultHtmlEscape" context-param in {@code web.xml}
	 * (if any).
	 * <p>This method differentiates between no param specified at all and
	 * an actual boolean value specified, allowing to have a context-specific
	 * default in case of no setting at the global level.
	 * @param servletContext the servlet context of the web application
	 * @return whether default HTML escaping is enabled (null = no explicit default)
	 */
	public static Boolean getDefaultHtmlEscape(ServletContext servletContext) {
		if (servletContext == null) {
			return null;
		}
		String param = servletContext.getInitParameter(HTML_ESCAPE_CONTEXT_PARAM);
		return (StringUtils.hasText(param) ? Boolean.valueOf(param) : null);
	}

	/**
	 * Return whether response encoding should be used when HTML escaping characters,
	 * thus only escaping XML markup significant characters with UTF-* encodings.
	 * This option is enabled for the web application with a ServletContext param,
	 * i.e. the value of the "responseEncodedHtmlEscape" context-param in {@code web.xml}
	 * (if any).
	 * <p>This method differentiates between no param specified at all and
	 * an actual boolean value specified, allowing to have a context-specific
	 * default in case of no setting at the global level.
	 * @param servletContext the servlet context of the web application
	 * @return whether response encoding is used for HTML escaping (null = no explicit default)
	 * @since 4.1.2
	 */
	public static Boolean getResponseEncodedHtmlEscape(ServletContext servletContext) {
		if (servletContext == null) {
			return null;
		}
		String param = servletContext.getInitParameter(RESPONSE_ENCODED_HTML_ESCAPE_CONTEXT_PARAM);
		return (StringUtils.hasText(param) ? Boolean.valueOf(param) : null);
	}

	/**
	 * Return the temporary directory for the current web application,
	 * as provided by the servlet container.
	 * @param servletContext the servlet context of the web application
	 * @return the File representing the temporary directory
	 */
	public static File getTempDir(ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext must not be null");
		return (File) servletContext.getAttribute(TEMP_DIR_CONTEXT_ATTRIBUTE);
	}

	/**
	 * Return the real path of the given path within the web application,
	 * as provided by the servlet container.
	 * <p>Prepends a slash if the path does not already start with a slash,
	 * and throws a FileNotFoundException if the path cannot be resolved to
	 * a resource (in contrast to ServletContext's {@code getRealPath},
	 * which returns null).
	 * @param servletContext the servlet context of the web application
	 * @param path the path within the web application
	 * @return the corresponding real path
	 * @throws FileNotFoundException if the path cannot be resolved to a resource
	 * @see javax.servlet.ServletContext#getRealPath
	 */
	public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
		Assert.notNull(servletContext, "ServletContext must not be null");
		// Interpret location as relative to the web application root directory.
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		String realPath = servletContext.getRealPath(path);
		if (realPath == null) {
			throw new FileNotFoundException(
					"ServletContext resource [" + path + "] cannot be resolved to absolute file path - " +
					"web application archive not expanded?");
		}
		return realPath;
	}


	/**
	 * Determine the session id of the given request, if any.
	 * @param request current HTTP request
	 * @return the session id, or {@code null} if none
	 */
	public static String getSessionId(HttpServletRequest request) {
		Assert.notNull(request, "Request must not be null");
		HttpSession session = request.getSession(false);
		return (session != null ? session.getId() : null);
	}

	/**
	 * Check the given request for a session attribute of the given name.
	 * Returns null if there is no session or if the session has no such attribute.
	 * Does not create a new session if none has existed before!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @return the value of the session attribute, or {@code null} if not found
	 */
	public static Object getSessionAttribute(HttpServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		HttpSession session = request.getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}

	/**
	 * Check the given request for a session attribute of the given name.
	 * Throws an exception if there is no session or if the session has no such
	 * attribute. Does not create a new session if none has existed before!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @return the value of the session attribute, or {@code null} if not found
	 * @throws IllegalStateException if the session attribute could not be found
	 */
	public static Object getRequiredSessionAttribute(HttpServletRequest request, String name)
			throws IllegalStateException {

		Object attr = getSessionAttribute(request, name);
		if (attr == null) {
			throw new IllegalStateException("No session attribute '" + name + "' found");
		}
		return attr;
	}

	/**
	 * Set the session attribute with the given name to the given value.
	 * Removes the session attribute if value is null, if a session existed at all.
	 * Does not create a new session if not necessary!
	 * @param request current HTTP request
	 * @param name the name of the session attribute
	 * @param value the value of the session attribute
	 */
	public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
		Assert.notNull(request, "Request must not be null");
		if (value != null) {
			request.getSession().setAttribute(name, value);
		}
		else {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(name);
			}
		}
	}

	/**
	 * Get the specified session attribute, creating and setting a new attribute if
	 * no existing found. The given class needs to have a public no-arg constructor.
	 * Useful for on-demand state objects in a web tier, like shopping carts.
	 * @param session current HTTP session
	 * @param name the name of the session attribute
	 * @param clazz the class to instantiate for a new attribute
	 * @return the value of the session attribute, newly created if not found
	 * @throws IllegalArgumentException if the session attribute could not be instantiated
	 */
	public static Object getOrCreateSessionAttribute(HttpSession session, String name, Class<?> clazz)
			throws IllegalArgumentException {

		Assert.notNull(session, "Session must not be null");
		Object sessionObject = session.getAttribute(name);
		if (sessionObject == null) {
			try {
				sessionObject = clazz.newInstance();
			}
			catch (InstantiationException ex) {
				throw new IllegalArgumentException(
					"Could not instantiate class [" + clazz.getName() +
					"] for session attribute '" + name + "': " + ex.getMessage());
			}
			catch (IllegalAccessException ex) {
				throw new IllegalArgumentException(
					"Could not access default constructor of class [" + clazz.getName() +
					"] for session attribute '" + name + "': " + ex.getMessage());
			}
			session.setAttribute(name, sessionObject);
		}
		return sessionObject;
	}

	/**
	 * Return the best available mutex for the given session:
	 * that is, an object to synchronize on for the given session.
	 * <p>Returns the session mutex attribute if available; usually,
	 * this means that the HttpSessionMutexListener needs to be defined
	 * in {@code web.xml}. Falls back to the HttpSession itself
	 * if no mutex attribute found.
	 * <p>The session mutex is guaranteed to be the same object during
	 * the entire lifetime of the session, available under the key defined
	 * by the {@code SESSION_MUTEX_ATTRIBUTE} constant. It serves as a
	 * safe reference to synchronize on for locking on the current session.
	 * <p>In many cases, the HttpSession reference itself is a safe mutex
	 * as well, since it will always be the same object reference for the
	 * same active logical session. However, this is not guaranteed across
	 * different servlet containers; the only 100% safe way is a session mutex.
	 * @param session the HttpSession to find a mutex for
	 * @return the mutex object (never {@code null})
	 * @see #SESSION_MUTEX_ATTRIBUTE
	 * @see HttpSessionMutexListener
	 */
	public static Object getSessionMutex(HttpSession session) {
		Assert.notNull(session, "Session must not be null");
		Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
		if (mutex == null) {
			mutex = session;
		}
		return mutex;
	}


	/**
	 * Return an appropriate request object of the specified type, if available,
	 * unwrapping the given request as far as necessary.
	 * @param request the servlet request to introspect
	 * @param requiredType the desired type of request object
	 * @return the matching request object, or {@code null} if none
	 * of that type is available
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType) {
		if (requiredType != null) {
			if (requiredType.isInstance(request)) {
				return (T) request;
			}
			else if (request instanceof ServletRequestWrapper) {
				return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
			}
		}
		return null;
	}

	/**
	 * Return an appropriate response object of the specified type, if available,
	 * unwrapping the given response as far as necessary.
	 * @param response the servlet response to introspect
	 * @param requiredType the desired type of response object
	 * @return the matching response object, or {@code null} if none
	 * of that type is available
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType) {
		if (requiredType != null) {
			if (requiredType.isInstance(response)) {
				return (T) response;
			}
			else if (response instanceof ServletResponseWrapper) {
				return getNativeResponse(((ServletResponseWrapper) response).getResponse(), requiredType);
			}
		}
		return null;
	}

	/**
	 * Determine whether the given request is an include request,
	 * that is, not a top-level HTTP request coming in from the outside.
	 * <p>Checks the presence of the "javax.servlet.include.request_uri"
	 * request attribute. Could check any request attribute that is only
	 * present in an include request.
	 * @param request current servlet request
	 * @return whether the given request is an include request
	 */
	public static boolean isIncludeRequest(ServletRequest request) {
		return (request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE) != null);
	}

	/**
	 * Expose the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
	 * attributes under the keys defined in the Servlet 2.3 specification, for error pages that
	 * are rendered directly rather than through the Servlet container's error page resolution:
	 * {@code javax.servlet.error.status_code},
	 * {@code javax.servlet.error.exception_type},
	 * {@code javax.servlet.error.message},
	 * {@code javax.servlet.error.exception},
	 * {@code javax.servlet.error.request_uri},
	 * {@code javax.servlet.error.servlet_name}.
	 * <p>Does not override values if already present, to respect attribute values
	 * that have been exposed explicitly before.
	 * <p>Exposes status code 200 by default. Set the "javax.servlet.error.status_code"
	 * attribute explicitly (before or after) in order to expose a different status code.
	 * @param request current servlet request
	 * @param ex the exception encountered
	 * @param servletName the name of the offending servlet
	 */
	public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, String servletName) {
		exposeRequestAttributeIfNotPresent(request, ERROR_STATUS_CODE_ATTRIBUTE, HttpServletResponse.SC_OK);
		exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_TYPE_ATTRIBUTE, ex.getClass());
		exposeRequestAttributeIfNotPresent(request, ERROR_MESSAGE_ATTRIBUTE, ex.getMessage());
		exposeRequestAttributeIfNotPresent(request, ERROR_EXCEPTION_ATTRIBUTE, ex);
		exposeRequestAttributeIfNotPresent(request, ERROR_REQUEST_URI_ATTRIBUTE, request.getRequestURI());
		exposeRequestAttributeIfNotPresent(request, ERROR_SERVLET_NAME_ATTRIBUTE, servletName);
	}

	/**
	 * Expose the specified request attribute if not already present.
	 * @param request current servlet request
	 * @param name the name of the attribute
	 * @param value the suggested value of the attribute
	 */
	private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value) {
		if (request.getAttribute(name) == null) {
			request.setAttribute(name, value);
		}
	}

	/**
	 * Clear the Servlet spec's error attributes as {@link javax.servlet.http.HttpServletRequest}
	 * attributes under the keys defined in the Servlet 2.3 specification:
	 * {@code javax.servlet.error.status_code},
	 * {@code javax.servlet.error.exception_type},
	 * {@code javax.servlet.error.message},
	 * {@code javax.servlet.error.exception},
	 * {@code javax.servlet.error.request_uri},
	 * {@code javax.servlet.error.servlet_name}.
	 * @param request current servlet request
	 */
	public static void clearErrorRequestAttributes(HttpServletRequest request) {
		request.removeAttribute(ERROR_STATUS_CODE_ATTRIBUTE);
		request.removeAttribute(ERROR_EXCEPTION_TYPE_ATTRIBUTE);
		request.removeAttribute(ERROR_MESSAGE_ATTRIBUTE);
		request.removeAttribute(ERROR_EXCEPTION_ATTRIBUTE);
		request.removeAttribute(ERROR_REQUEST_URI_ATTRIBUTE);
		request.removeAttribute(ERROR_SERVLET_NAME_ATTRIBUTE);
	}

	/**
	 * Expose the given Map as request attributes, using the keys as attribute names
	 * and the values as corresponding attribute values. Keys need to be Strings.
	 * @param request current HTTP request
	 * @param attributes the attributes Map
	 */
	public static void exposeRequestAttributes(ServletRequest request, Map<String, ?> attributes) {
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(attributes, "Attributes Map must not be null");
		for (Map.Entry<String, ?> entry : attributes.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Check if a specific input type="submit" parameter was sent in the request,
	 * either via a button (directly with name) or via an image (name + ".x" or
	 * name + ".y").
	 * @param request current HTTP request
	 * @param name name of the parameter
	 * @return if the parameter was sent
	 * @see #SUBMIT_IMAGE_SUFFIXES
	 */
	public static boolean hasSubmitParameter(ServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		if (request.getParameter(name) != null) {
			return true;
		}
		for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
			if (request.getParameter(name + suffix) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtain a named parameter from the given request parameters.
	 * <p>See {@link #findParameterValue(java.util.Map, String)}
	 * for a description of the lookup algorithm.
	 * @param request current HTTP request
	 * @param name the <i>logical</i> name of the request parameter
	 * @return the value of the parameter, or {@code null}
	 * if the parameter does not exist in given request
	 */
	public static String findParameterValue(ServletRequest request, String name) {
		return findParameterValue(request.getParameterMap(), name);
	}

	/**
	 * Obtain a named parameter from the given request parameters.
	 * <p>This method will try to obtain a parameter value using the
	 * following algorithm:
	 * <ol>
	 * <li>Try to get the parameter value using just the given <i>logical</i> name.
	 * This handles parameters of the form <tt>logicalName = value</tt>. For normal
	 * parameters, e.g. submitted using a hidden HTML form field, this will return
	 * the requested value.</li>
	 * <li>Try to obtain the parameter value from the parameter name, where the
	 * parameter name in the request is of the form <tt>logicalName_value = xyz</tt>
	 * with "_" being the configured delimiter. This deals with parameter values
	 * submitted using an HTML form submit button.</li>
	 * <li>If the value obtained in the previous step has a ".x" or ".y" suffix,
	 * remove that. This handles cases where the value was submitted using an
	 * HTML form image button. In this case the parameter in the request would
	 * actually be of the form <tt>logicalName_value.x = 123</tt>. </li>
	 * </ol>
	 * @param parameters the available parameter map
	 * @param name the <i>logical</i> name of the request parameter
	 * @return the value of the parameter, or {@code null}
	 * if the parameter does not exist in given request
	 */
	public static String findParameterValue(Map<String, ?> parameters, String name) {
		// First try to get it as a normal name=value parameter
		Object value = parameters.get(name);
		if (value instanceof String[]) {
			String[] values = (String[]) value;
			return (values.length > 0 ? values[0] : null);
		}
		else if (value != null) {
			return value.toString();
		}
		// If no value yet, try to get it as a name_value=xyz parameter
		String prefix = name + "_";
		for (String paramName : parameters.keySet()) {
			if (paramName.startsWith(prefix)) {
				// Support images buttons, which would submit parameters as name_value.x=123
				for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
					if (paramName.endsWith(suffix)) {
						return paramName.substring(prefix.length(), paramName.length() - suffix.length());
					}
				}
				return paramName.substring(prefix.length());
			}
		}
		// We couldn't find the parameter value...
		return null;
	}

	/**
	 * Return a map containing all parameters with the given prefix.
	 * Maps single values to String and multiple values to String array.
	 * <p>For example, with a prefix of "spring_", "spring_param1" and
	 * "spring_param2" result in a Map with "param1" and "param2" as keys.
	 * @param request HTTP request in which to look for parameters
	 * @param prefix the beginning of parameter names
	 * (if this is null or the empty string, all parameters will match)
	 * @return map containing request parameters <b>without the prefix</b>,
	 * containing either a String or a String array as values
	 * @see javax.servlet.ServletRequest#getParameterNames
	 * @see javax.servlet.ServletRequest#getParameterValues
	 * @see javax.servlet.ServletRequest#getParameterMap
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				}
				else if (values.length > 1) {
					params.put(unprefixed, values);
				}
				else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * Return the target page specified in the request.
	 * @param request current servlet request
	 * @param paramPrefix the parameter prefix to check for
	 * (e.g. "_target" for parameters like "_target1" or "_target2")
	 * @param currentPage the current page, to be returned as fallback
	 * if no target page specified
	 * @return the page specified in the request, or current page if not found
	 */
	public static int getTargetPage(ServletRequest request, String paramPrefix, int currentPage) {
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName.startsWith(paramPrefix)) {
				for (int i = 0; i < WebUtils.SUBMIT_IMAGE_SUFFIXES.length; i++) {
					String suffix = WebUtils.SUBMIT_IMAGE_SUFFIXES[i];
					if (paramName.endsWith(suffix)) {
						paramName = paramName.substring(0, paramName.length() - suffix.length());
					}
				}
				return Integer.parseInt(paramName.substring(paramPrefix.length()));
			}
		}
		return currentPage;
	}


	/**
	 * Extract the URL filename from the given request URL path.
	 * Correctly resolves nested paths such as "/products/view.html" as well.
	 * @param urlPath the request URL path (e.g. "/index.html")
	 * @return the extracted URI filename (e.g. "index")
	 */
	public static String extractFilenameFromUrlPath(String urlPath) {
		String filename = extractFullFilenameFromUrlPath(urlPath);
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex != -1) {
			filename = filename.substring(0, dotIndex);
		}
		return filename;
	}

	/**
	 * Extract the full URL filename (including file extension) from the given request URL path.
	 * Correctly resolves nested paths such as "/products/view.html" as well.
	 * @param urlPath the request URL path (e.g. "/products/index.html")
	 * @return the extracted URI filename (e.g. "index.html")
	 */
	public static String extractFullFilenameFromUrlPath(String urlPath) {
		int end = urlPath.indexOf(';');
		if (end == -1) {
			end = urlPath.indexOf('?');
			if (end == -1) {
				end = urlPath.length();
			}
		}
		int begin = urlPath.lastIndexOf('/', end) + 1;
		return urlPath.substring(begin, end);
	}
	 
	public static String getContextPath(ServletContext context) {
		// cette méthode retourne le contextPath de la webapp
		// en utilisant ServletContext.getContextPath si servlet api 2.5
		// ou en se débrouillant sinon
		// (on n'a pas encore pour l'instant de request pour appeler HttpServletRequest.getContextPath)
		if (context.getMajorVersion() == 2 && context.getMinorVersion() >= 5
				|| context.getMajorVersion() > 2) {
			// api servlet 2.5 (Java EE 5) minimum pour appeler ServletContext.getContextPath
			return context.getContextPath();
		}
		URL webXmlUrl;
		try {
			webXmlUrl = context.getResource("/WEB-INF/web.xml");
		} catch (final MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		String contextPath = webXmlUrl.toExternalForm();
		contextPath = contextPath.substring(0, contextPath.indexOf("/WEB-INF/web.xml"));
		final int indexOfWar = contextPath.indexOf(".war");
		if (indexOfWar > 0) {
			contextPath = contextPath.substring(0, indexOfWar);
		}
		// tomcat peut renvoyer une url commençant pas "jndi:/localhost"
		// (v5.5.28, webapp dans un répertoire)
		if (contextPath.startsWith("jndi:/localhost")) {
			contextPath = contextPath.substring("jndi:/localhost".length());
		}
		final int lastIndexOfSlash = contextPath.lastIndexOf('/');
		if (lastIndexOfSlash != -1) {
			contextPath = contextPath.substring(lastIndexOfSlash);
		}
		return contextPath;
	}
	
	/*static String getParameterByName(String parameterName) {
		assert parameterName != null;
		final String globalName = PARAMETER_SYSTEM_PREFIX + parameterName;
		String result = System.getProperty(globalName);
		if (result != null) {
			return result;
		}
		if (servletContext != null) {
			result = servletContext.getInitParameter(globalName);
			if (result != null) {
				return result;
			}
			// issue 463: in a ServletContextListener, it's also possible to call servletContext.setAttribute("javamelody.log", "true"); for example
			final Object attribute = servletContext.getAttribute(globalName);
			if (attribute instanceof String) {
				return (String) attribute;
			}
		}
		if (filterConfig != null) {
			result = filterConfig.getInitParameter(parameterName);
			if (result != null) {
				return result;
			}
		}
		return null;
	}*/
	
	 /**
     * 
     * <p>方法说明：获取参数<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年10月15日下午2:57:05<p>
     */
    public static String cleanRequestParam(HttpServletRequest request, String parameterName){
    	String out = null;
    	String in = request.getParameter(parameterName);
        if (in != null) {
            out = in.trim();
            if (out.equals("")) {
                out = null;
            }
        }
        return out;
    }
    
    public static boolean isTrue(HttpServletRequest request, String paramName) {
        String value = cleanRequestParam(request, paramName);
        return value != null &&
                (value.equalsIgnoreCase("true") ||
                        value.equalsIgnoreCase("t") ||
                        value.equalsIgnoreCase("1") ||
                        value.equalsIgnoreCase("enabled") ||
                        value.equalsIgnoreCase("y") ||
                        value.equalsIgnoreCase("yes") ||
                        value.equalsIgnoreCase("on"));
    }
    
    /**
     * A convenience method that merely casts the incoming <code>ServletRequest</code> to an
     * <code>HttpServletRequest</code>:
     * <p/>
     * <code>return (HttpServletRequest)request;</code>
     * <p/>
     * Logic could be changed in the future for logging or throwing an meaningful exception in
     * non HTTP request environments (e.g. Portlet API).
     *
     * @param request the incoming ServletRequest
     * @return the <code>request</code> argument casted to an <code>HttpServletRequest</code>.
     */
    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    /**
     * A convenience method that merely casts the incoming <code>ServletResponse</code> to an
     * <code>HttpServletResponse</code>:
     * <p/>
     * <code>return (HttpServletResponse)response;</code>
     * <p/>
     * Logic could be changed in the future for logging or throwing an meaningful exception in
     * non HTTP request environments (e.g. Portlet API).
     *
     * @param response the outgoing ServletResponse
     * @return the <code>response</code> argument casted to an <code>HttpServletResponse</code>.
     */
    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }
    
	/**
     * Return the path within the web application for the given request.
     * Detects include request URL if called within a RequestDispatcher include.
     * <p/>
     * For example, for a request to URL
     * <p/>
     * <code>http://www.somehost.com/myapp/my/url.jsp</code>,
     * <p/>
     * for an application deployed to <code>/mayapp</code> (the application's context path), this method would return
     * <p/>
     * <code>/my/url.jsp</code>.
     *
     * @param request current HTTP request
     * @return the path within the web application
     */
    public static String getPathWithinApplication(HttpServletRequest request) {
        String contextPath = getContextPath(request);
        String requestUri = getRequestUri(request);
        if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
            // Normal case: URI contains context path.
            String path = requestUri.substring(contextPath.length());
            return (StringUtils.isNoneBlank(path) ? path : "/");
        } else {
            // Special case: rather unusual.
            return requestUri;
        }
    }

    /**
     * Return the request URI for the given request, detecting an include request
     * URL if called within a RequestDispatcher include.
     * <p>As the value returned by <code>request.getRequestURI()</code> is <i>not</i>
     * decoded by the servlet container, this method will decode it.
     * <p>The URI that the web container resolves <i>should</i> be correct, but some
     * containers like JBoss/Jetty incorrectly include ";" strings like ";jsessionid"
     * in the URI. This method cuts off such incorrect appendices.
     *
     * @param request current HTTP request
     * @return the request URI
     */
    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return normalize(decodeAndCleanUriString(request, uri));
    }

    /**
     * Normalize a relative URI path that may have relative values ("/./",
     * "/../", and so on ) it it.  <strong>WARNING</strong> - This method is
     * useful only for normalizing application-generated paths.  It does not
     * try to perform security checks for malicious input.
     * Normalize operations were was happily taken from org.apache.catalina.util.RequestUtil in
     * Tomcat trunk, r939305
     *
     * @param path Relative path to be normalized
     * @return normalized path
     */
    public static String normalize(String path) {
        return normalize(path, true);
    }

    /**
     * Normalize a relative URI path that may have relative values ("/./",
     * "/../", and so on ) it it.  <strong>WARNING</strong> - This method is
     * useful only for normalizing application-generated paths.  It does not
     * try to perform security checks for malicious input.
     * Normalize operations were was happily taken from org.apache.catalina.util.RequestUtil in
     * Tomcat trunk, r939305
     *
     * @param path             Relative path to be normalized
     * @param replaceBackSlash Should '\\' be replaced with '/'
     * @return normalized path
     */
    private static String normalize(String path, boolean replaceBackSlash) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        if (replaceBackSlash && normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');

        if (normalized.equals("/."))
            return "/";

        // Add a leading "/" if necessary
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }


    /**
     * Decode the supplied URI string and strips any extraneous portion after a ';'.
     *
     * @param request the incoming HttpServletRequest
     * @param uri     the application's URI string
     * @return the supplied URI string stripped of any extraneous portion after a ';'.
     */
    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }

    /**
     * Return the context path for the given request, detecting an include request
     * URL if called within a RequestDispatcher include.
     * <p>As the value returned by <code>request.getContextPath()</code> is <i>not</i>
     * decoded by the servlet container, this method will decode it.
     *
     * @param request current HTTP request
     * @return the context path
     */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        if ("/".equals(contextPath)) {
            // Invalid case, but happens for includes on Jetty: silently adapt it.
            contextPath = "";
        }
        return decodeRequestString(request, contextPath);
    }
	
    /**
     * Decode the given source string with a URLDecoder. The encoding will be taken
     * from the request, falling back to the default "ISO-8859-1".
     * <p>The default implementation uses <code>URLDecoder.decode(input, enc)</code>.
     *
     * @param request current HTTP request
     * @param source  the String to decode
     * @return the decoded String
     * @see #DEFAULT_CHARACTER_ENCODING
     * @see javax.servlet.ServletRequest#getCharacterEncoding
     * @see java.net.URLDecoder#decode(String, String)
     * @see java.net.URLDecoder#decode(String)
     */
    @SuppressWarnings({"deprecation"})
    public static String decodeRequestString(HttpServletRequest request, String source) {
        String enc = determineEncoding(request);
        try {
            return URLDecoder.decode(source, enc);
        } catch (UnsupportedEncodingException ex) {
            return URLDecoder.decode(source);
        }
    }
    
    /**
     * 
     * <p>方法说明：获取请求主机信息<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年8月8日下午9:09:26<p>
     */
    public static String getDomainName(String requestURL){
    	String token = "://";
		int index = requestURL.indexOf(token);
		String part = requestURL.substring(index + token.length());
		StringBuilder domain = new StringBuilder();

		for (int i = 0; i < part.length(); i++) {
			char character = part.charAt(i);

			if (character == '/' || character == ':') {
				break;
			}

			domain.append(character);
		}

		return domain.toString();
    }
    
    /**
     * 
     * <p>方法说明：获取请求主机信息<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年8月8日下午9:09:26<p>
     */
    public static String getDomainName(HttpServletRequest request){
    	String url = request.getRequestURL().toString();
    	String token = "://";
		int index = url.indexOf(token);
		String part = url.substring(index + token.length());
		StringBuilder domain = new StringBuilder();

		for (int i = 0; i < part.length(); i++) {
			char character = part.charAt(i);

			if (character == '/' || character == ':') {
				break;
			}

			domain.append(character);
		}

		return domain.toString();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
 		return (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())) ;
 	}
    
    /**
     * Determine the encoding for the given request.
     * Can be overridden in subclasses.
     * <p>The default implementation checks the request's
     * {@link ServletRequest#getCharacterEncoding() character encoding}, and if that
     * <code>null</code>, falls back to the {@link #DEFAULT_CHARACTER_ENCODING}.
     *
     * @param request current HTTP request
     * @return the encoding for the request (never <code>null</code>)
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    protected static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = DEFAULT_CHARACTER_ENCODING;
        }
        return enc;
    }
    
    /**
     * Redirects the current request to a new URL based on the given parameters.
     *
     * @param request          the servlet request.
     * @param response         the servlet response.
     * @param url              the URL to redirect the user to.
     * @param queryParams      a map of parameters that should be set as request parameters for the new request.
     * @param contextRelative  true if the URL is relative to the servlet context path, or false if the URL is absolute.
     * @param http10Compatible whether to stay compatible with HTTP 1.0 clients.
     * @throws java.io.IOException if thrown by response methods.
     */
    @SuppressWarnings("rawtypes")
    public static void issueRedirect(ServletRequest request, ServletResponse response, String url, Map queryParams, boolean contextRelative, boolean http10Compatible) throws IOException {
        RedirectView view = new RedirectView(url, contextRelative, http10Compatible);
        view.renderMergedOutputModel(queryParams, toHttp(request), toHttp(response));
    }

    /**
     * Redirects the current request to a new URL based on the given parameters and default values
     * for unspecified parameters.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @param url      the URL to redirect the user to.
     * @throws java.io.IOException if thrown by response methods.
     */
    public static void issueRedirect(ServletRequest request, ServletResponse response, String url) throws IOException {
        issueRedirect(request, response, url, null, true, true);
    }

    /**
     * Redirects the current request to a new URL based on the given parameters and default values
     * for unspecified parameters.
     *
     * @param request     the servlet request.
     * @param response    the servlet response.
     * @param url         the URL to redirect the user to.
     * @param queryParams a map of parameters that should be set as request parameters for the new request.
     * @throws java.io.IOException if thrown by response methods.
     */
    @SuppressWarnings("rawtypes")
	public static void issueRedirect(ServletRequest request, ServletResponse response, String url, Map queryParams) throws IOException {
        issueRedirect(request, response, url, queryParams, true, true);
    }

    /**
     * Redirects the current request to a new URL based on the given parameters and default values
     * for unspecified parameters.
     *
     * @param request         the servlet request.
     * @param response        the servlet response.
     * @param url             the URL to redirect the user to.
     * @param queryParams     a map of parameters that should be set as request parameters for the new request.
     * @param contextRelative true if the URL is relative to the servlet context path, or false if the URL is absolute.
     * @throws java.io.IOException if thrown by response methods.
     */
    @SuppressWarnings("rawtypes")
    public static void issueRedirect(ServletRequest request, ServletResponse response, String url, Map queryParams, boolean contextRelative) throws IOException {
        issueRedirect(request, response, url, queryParams, contextRelative, true);
    }
    
    /**
     * 
     * <p>方法说明：判断是否是ajax请求<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年8月5日下午8:27:38<p>
     */
    public static boolean isAjaxRequest(ServletRequest request){
    	if(! (request instanceof HttpServletRequest)){
    		return false;
    	}
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	return httpRequest.getHeader("X-Requested-With") != null;
    }
    
    public static boolean isGzipInRequest(HttpServletRequest request) {
        return StringUtils.contains(request.getHeader("Accept-Encoding"), "gzip");
    }
    
    //
    private static class RedirectView {

        /**
         * The default encoding scheme: UTF-8
         */
        public static final String DEFAULT_ENCODING_SCHEME = "UTF-8";

        private String url;

        private boolean contextRelative = false;

        private boolean http10Compatible = true;

        private String encodingScheme = DEFAULT_ENCODING_SCHEME;

        /**
         * Constructor for use as a bean.
         */
        
        @SuppressWarnings("unused")
		public RedirectView() {
        }

        /**
         * Create a new RedirectView with the given URL.
         * <p>The given URL will be considered as relative to the web server,
         * not as relative to the current ServletContext.
         *
         * @param url the URL to redirect to
         * @see #RedirectView(String, boolean)
         */
        public RedirectView(String url) {
            setUrl(url);
        }

        /**
         * Create a new RedirectView with the given URL.
         *
         * @param url             the URL to redirect to
         * @param contextRelative whether to interpret the given URL as
         *                        relative to the current ServletContext
         */
        @SuppressWarnings("unused")
		public RedirectView(String url, boolean contextRelative) {
            this(url);
            this.contextRelative = contextRelative;
        }

        /**
         * Create a new RedirectView with the given URL.
         *
         * @param url              the URL to redirect to
         * @param contextRelative  whether to interpret the given URL as
         *                         relative to the current ServletContext
         * @param http10Compatible whether to stay compatible with HTTP 1.0 clients
         */
        public RedirectView(String url, boolean contextRelative, boolean http10Compatible) {
            this(url);
            this.contextRelative = contextRelative;
            this.http10Compatible = http10Compatible;
        }


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Set whether to interpret a given URL that starts with a slash ("/")
         * as relative to the current ServletContext, i.e. as relative to the
         * web application root.
         * <p/>
         * Default is "false": A URL that starts with a slash will be interpreted
         * as absolute, i.e. taken as-is. If true, the context path will be
         * prepended to the URL in such a case.
         *
         * @param contextRelative whether to interpret a given URL that starts with a slash ("/")
         *                        as relative to the current ServletContext, i.e. as relative to the
         *                        web application root.
         * @see javax.servlet.http.HttpServletRequest#getContextPath
         */
        @SuppressWarnings("unused")
		public void setContextRelative(boolean contextRelative) {
            this.contextRelative = contextRelative;
        }

        /**
         * Set whether to stay compatible with HTTP 1.0 clients.
         * <p>In the default implementation, this will enforce HTTP status code 302
         * in any case, i.e. delegate to <code>HttpServletResponse.sendRedirect</code>.
         * Turning this off will send HTTP status code 303, which is the correct
         * code for HTTP 1.1 clients, but not understood by HTTP 1.0 clients.
         * <p>Many HTTP 1.1 clients treat 302 just like 303, not making any
         * difference. However, some clients depend on 303 when redirecting
         * after a POST request; turn this flag off in such a scenario.
         *
         * @param http10Compatible whether to stay compatible with HTTP 1.0 clients.
         * @see javax.servlet.http.HttpServletResponse#sendRedirect
         */
        @SuppressWarnings("unused")
		public void setHttp10Compatible(boolean http10Compatible) {
            this.http10Compatible = http10Compatible;
        }

        /**
         * Set the encoding scheme for this view. Default is UTF-8.
         *
         * @param encodingScheme the encoding scheme for this view. Default is UTF-8.
         */
        @SuppressWarnings("unused")
		public void setEncodingScheme(String encodingScheme) {
            this.encodingScheme = encodingScheme;
        }


        /**
         * Convert model to request parameters and redirect to the given URL.
         *
         * @param model    the model to convert
         * @param request  the incoming HttpServletRequest
         * @param response the outgoing HttpServletResponse
         * @throws java.io.IOException if there is a problem issuing the redirect
         * @see #appendQueryProperties
         * @see #sendRedirect
         */
        @SuppressWarnings("rawtypes")
		public final void renderMergedOutputModel(
                Map model, HttpServletRequest request, HttpServletResponse response) throws IOException {

            // Prepare name URL.
            StringBuilder targetUrl = new StringBuilder();
            if (this.contextRelative && getUrl().startsWith("/")) {
                // Do not apply context path to relative URLs.
                targetUrl.append(request.getContextPath());
            }
            targetUrl.append(getUrl());
            //change the following method to accept a StringBuilder instead of a StringBuilder for Shiro 2.x:
            appendQueryProperties(targetUrl, model, this.encodingScheme);

            sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
        }

        /**
         * Append query properties to the redirect URL.
         * Stringifies, URL-encodes and formats model attributes as query properties.
         *
         * @param targetUrl      the StringBuffer to append the properties to
         * @param model          Map that contains model attributes
         * @param encodingScheme the encoding scheme to use
         * @throws java.io.UnsupportedEncodingException if string encoding failed
         * @see #urlEncode
         * @see #queryProperties
         * @see #urlEncode(String, String)
         */
        @SuppressWarnings("rawtypes")
		protected void appendQueryProperties(StringBuilder targetUrl, Map model, String encodingScheme)
                throws UnsupportedEncodingException {

            // Extract anchor fragment, if any.
            // The following code does not use JDK 1.4's StringBuffer.indexOf(String)
            // method to retain JDK 1.3 compatibility.
            String fragment = null;
            int anchorIndex = targetUrl.toString().indexOf('#');
            if (anchorIndex > -1) {
                fragment = targetUrl.substring(anchorIndex);
                targetUrl.delete(anchorIndex, targetUrl.length());
            }

            // If there aren't already some parameters, we need a "?".
            boolean first = (getUrl().indexOf('?') < 0);
            Map queryProps = queryProperties(model);

            if (queryProps != null) {
                for (Object o : queryProps.entrySet()) {
                    if (first) {
                        targetUrl.append('?');
                        first = false;
                    } else {
                        targetUrl.append('&');
                    }
                    Map.Entry entry = (Map.Entry) o;
                    String encodedKey = urlEncode(entry.getKey().toString(), encodingScheme);
                    String encodedValue =
                            (entry.getValue() != null ? urlEncode(entry.getValue().toString(), encodingScheme) : "");
                    targetUrl.append(encodedKey).append('=').append(encodedValue);
                }
            }

            // Append anchor fragment, if any, to end of URL.
            if (fragment != null) {
                targetUrl.append(fragment);
            }
        }

        /**
         * URL-encode the given input String with the given encoding scheme, using
         * {@link URLEncoder#encode(String, String) URLEncoder.encode(input, enc)}.
         *
         * @param input          the unencoded input String
         * @param encodingScheme the encoding scheme
         * @return the encoded output String
         * @throws UnsupportedEncodingException if thrown by the JDK URLEncoder
         * @see java.net.URLEncoder#encode(String, String)
         * @see java.net.URLEncoder#encode(String)
         */
        protected String urlEncode(String input, String encodingScheme) throws UnsupportedEncodingException {
            return URLEncoder.encode(input, encodingScheme);
        }

        /**
         * Determine name-value pairs for query strings, which will be stringified,
         * URL-encoded and formatted by appendQueryProperties.
         * <p/>
         * This implementation returns all model elements as-is.
         *
         * @param model the model elements for which to determine name-value pairs.
         * @return the name-value pairs for query strings.
         * @see #appendQueryProperties
         */
        @SuppressWarnings("rawtypes")
		protected Map queryProperties(Map model) {
            return model;
        }

        /**
         * Send a redirect back to the HTTP client
         *
         * @param request          current HTTP request (allows for reacting to request method)
         * @param response         current HTTP response (for sending response headers)
         * @param targetUrl        the name URL to redirect to
         * @param http10Compatible whether to stay compatible with HTTP 1.0 clients
         * @throws IOException if thrown by response methods
         */
        protected void sendRedirect(HttpServletRequest request, HttpServletResponse response,
                                    String targetUrl, boolean http10Compatible) throws IOException {
            if (http10Compatible) {
                // Always send status code 302.
                response.sendRedirect(response.encodeRedirectURL(targetUrl));
            } else {
                // Correct HTTP status code is 303, in particular for POST requests.
                response.setStatus(303);
                response.setHeader("Location", response.encodeRedirectURL(targetUrl));
            }
        }

    }
    
}
