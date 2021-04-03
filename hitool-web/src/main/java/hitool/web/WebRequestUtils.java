package hitool.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.Assert;

/*
 * Web请求处理工具类
 */
public abstract class WebRequestUtils {
	
	protected static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static Logger LOG = LoggerFactory.getLogger(WebRequestUtils.class);
	
	/*
	 * 得到ServletContext中的变量,相当于request.getSession().getServletContext().getAttribute()
	 * @param servletContext
	 * @param prefix
	 * @param filters
	 * @return		：
	 */
	public static Map<String, Object> getApplicationMap(ServletContext servletContext,String prefix,String ...filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<String> enu = servletContext.getAttributeNames();
		while(enu.hasMoreElements()){
			String key = (String) enu.nextElement();
			if (prefix != null && ("".equals(prefix) || key.startsWith(prefix))) {
				attributeMap.put(key, servletContext.getAttribute(key));
			}else{
				attributeMap.put(key, servletContext.getAttribute(key));
			}
		}
		//过滤不需要的参数
		if(null != filters){
			for (String string : filters) {
				attributeMap.remove(string);
			}
		}
		return attributeMap;
	}
	
	/*
	 * 得到ServletContext中的变量,相当于request.getSession().getServletContext().getAttribute()
	 * @param servletContext
	 * @param filters
	 * @return		：
	 */
	public static Map<String, Object> getApplicationMap(ServletContext servletContext,String ...filters) {
		return getApplicationMap(servletContext, null, filters);
	}
	
	/*
	 * 得到ServletRequest中的变量,相当于request.getAttribute()
	 * @param request
	 * @param prefix
	 * @param filters
	 * @return		：
	 */
	public static Map<String, Object> getContextMap(ServletRequest request, String prefix, String ...filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<String> enu = request.getAttributeNames();
		while(enu != null && enu.hasMoreElements()){
			String key = enu.nextElement();
			if (prefix != null || key.startsWith(prefix)) {
				attributeMap.put(key, request.getAttribute(key));
			}
		}
		//过滤不需要的参数
		if(null != filters){
			for (String key : filters) {
				attributeMap.remove(key);
			}
		}
		return attributeMap;
	}
	
	/*
	 * 得到ServletRequest中的变量,相当于request.getAttribute()
	 * @param request
	 * @param filters
	 * @return		：
	 */
	public static Map<String, Object> getContextMap(ServletRequest request,String ...filters) {
		return getContextMap(request, null, filters);
	}
	
	/*
	 * 得到主机 应用 http请求路径： 如：http://127.0.0.1:8080/webapp_name
	 * @return
	 */
	public static String getHostPath(HttpServletRequest request){
		try {
			String[] hostInfo = getLocalHostInfo(request);
			return "http://"+hostInfo[0]+":"+hostInfo[0]+ request.getServletContext().getContextPath();
		} catch (UnknownHostException e1) {
			return null;
		}
	} 
	
	/*
	 * 获得本地主机信息【本机IP,端口,本机名称】
	 * @return
	 * @throws UnknownHostException
	 */
	public static String[] getLocalHostInfo(HttpServletRequest request) throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { inet.getHostAddress().toString(), request.getLocalPort()+ "", inet.getHostName().toString()};
	}
	 
	public static Map<String, String[]> getRequestMap(ServletRequest request, String ...filters) {
		return getRequestMap(request, null, filters);
	}
	
	public static Map<String, String[]> getRequestMap(ServletRequest request, String prefix, String ...filters) {
		Assert.notNull(request, "Request must not be null");
		Enumeration<String> enu = request.getParameterNames();
		Map<String, String[]> requestMap = new HashMap<String, String[]>();
		while (enu != null && enu.hasMoreElements()) {
			String key = enu.nextElement();
			if (prefix == null || key.startsWith(prefix)) {
				requestMap.put(key, request.getParameterValues(key));
			}
		}
		//过滤不需要的参数
		if(null != filters){
			for (String key : filters) {
				requestMap.remove(key);
			}
		}
		return requestMap;
	}
	
	public static <M> M getRequestObject(ServletRequest request,Class<M> clazz) {
		M object = null;
		try {
			// 获取类属性
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			object = clazz.newInstance();// 创建JavaBean对象
			Enumeration<String> enu = request.getParameterNames();
			while (enu != null && enu.hasMoreElements()) {
				String parameterName = enu.nextElement();
				// 给JavaBean对象的属性赋值
				for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
					String propertyName = descriptor.getName();
					try {
						if (parameterName.equals(propertyName)) {
							Class<?> fieldType = descriptor.getPropertyType();
							String fieldValue = request.getParameter(propertyName);
							Method writeMethod = descriptor.getWriteMethod();
							LOG.info("fieldType:" + fieldType + "fieldValue:"+ fieldValue  );
							if (fieldType.equals(Date.class)) {
								writeMethod.invoke(object, parameterName,FORMAT.parse(fieldValue));
							} else {
								writeMethod.invoke(object, parameterName, fieldValue);
							}
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(),e.getCause());
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.getCause());
		}
		return object;
	}
	
	
	
	/*
     * Retrieves the current request servlet path.
     * Deals with differences between servlet specs (2.2 vs 2.3+)
     * @param request the request
     * @return the servlet path
     */
    public static String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }
        
        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }
        
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }
        return requestUri.substring(startIndex, endIndex);
    }
    
    /*
     *  得到Session中的变量Map,相当于request.getSession().getAttribute()
     * @param session
     * @param filters
     * @return		：
     */
	public static Map<String, Object> getSessionMap(HttpSession session,String ...filters) {
		return getSessionMap(session, null, filters);
	}
	
	/*
	 * 得到Session中的变量Map,相当于request.getSession().getAttribute()
	 * @param session
	 * @param prefix
	 * @param filters
	 * @return		：
	 */
	public static Map<String, Object> getSessionMap(HttpSession session, String prefix,String ...filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<String> enu = session.getAttributeNames();
		while(enu != null && enu.hasMoreElements()){
			String key= enu.nextElement();
			if (prefix == null || key.startsWith(prefix)) {
				attributeMap.put(key, session.getAttribute(key));
			}
		}
		//过滤不需要的参数
		if(null != filters){
			for (String key : filters) {
				attributeMap.remove(key);
			}
		}
		return attributeMap;
	}
	
	/*
	 *  获得请求的客户端信息【ip,port,name】
	 * @param request
	 * @return
	 */
	public static String[] getRemoteInfo(HttpServletRequest request) {
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { RemoteAddrUtils.getRemoteAddr(request), request.getRemotePort() + "", request.getRemoteHost()};
	}
	
	public static String getURI(HttpServletRequest request) {
        String uri = getServletPath(request);
        if (uri != null && !"".equals(uri)) {
            return uri;
        }

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

	/*
	 * 判断请求是否是Ajax 请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String requested = request.getHeader("x-requested-with");
		 //判断是否ajax请求
		if(requested !=null && requested.equalsIgnoreCase("XMLHttpRequest")){ 
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isMultipartRequest(HttpServletRequest request){
		 String content_type = request.getContentType();
         if (content_type != null && content_type.startsWith("multipart/")) {
        	 return true;
         }
         return false;
	}
	
	/*
	 * 在request中获取异常类
	 * @param request
	 * @return 
	 */
	public static Throwable getThrowable(HttpServletRequest request){
		Throwable ex = null;
		if (request.getAttribute("exception") != null) {
			ex = (Throwable) request.getAttribute("exception");
		} else if (request.getAttribute("javax.servlet.error.exception") != null) {
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}
	
}
