 package hitool.core.web.servlet.filter.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.web.servlet.filter.OncePerRequestFilter;
import hitool.core.web.servlet.http.HttpServletResourceCachedResponseWrapper;
public class HttpServletRequestWebResourceCacheFilter  extends OncePerRequestFilter {
	
	protected transient Logger LOG = LoggerFactory.getLogger(HttpServletRequestWebResourceCacheFilter.class);
	/**
	 * @Field: map 缓存Web资源的Map容器
	 */
	protected Map<String,byte[]> map = new HashMap<String,byte[]>();
	
	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		
		//1.得到用户请求的uri
		String uri = httpRequest.getRequestURI();
		//2.看缓存中有没有uri对应的数据
		byte b[] = map.get(uri);
		//3.如果缓存中有，直接拿缓存的数据打给浏览器，程序返回
		if(b!=null){
		    //根据字节数组和指定的字符编码构建字符串
		    String webResourceHtmlStr = new String(b,response.getCharacterEncoding());
		    LOG.debug("html : " + webResourceHtmlStr);
		    response.getOutputStream().write(b);
		    return;
		}
		//4.如果缓存没有，让目标资源执行，并捕获目标资源的输出
		HttpServletResourceCachedResponseWrapper myresponse = new HttpServletResourceCachedResponseWrapper(httpResponse);
		filterChain.doFilter(request, myresponse);
		//获取缓冲流中的内容的字节数组
		byte out[] = myresponse.getBuffer();
		//5.把资源的数据以用户请求的uri为关键字保存到缓存中
		map.put(uri, out);
		//6.把数据打给浏览器
		response.getOutputStream().write(out);
		
	}

}

