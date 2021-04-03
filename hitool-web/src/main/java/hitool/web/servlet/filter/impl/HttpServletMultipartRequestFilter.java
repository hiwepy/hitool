package hitool.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.web.MultipartRequestUtils;
import hitool.web.servlet.filter.OncePerRequestFilter;
import hitool.web.servlet.http.HttpServletMultipartRequestWrapper;
/*
 * 文件上传请求的拦截器
 */
public class HttpServletMultipartRequestFilter extends OncePerRequestFilter {

	protected String multipartType = "COS";

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		// 得到在web.xml中配置的multipart支出类型
		multipartType = filterConfig.getInitParameter("multipart");
		if (multipartType == null) {
			multipartType = "COS";
		}
	}
	
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 判断是multipart请求
		if (MultipartRequestUtils.isMultipartRequest(httpRequest)) {
			HttpServletMultipartRequestWrapper multipartRequestWrapper = new HttpServletMultipartRequestWrapper(httpRequest);
			filterChain.doFilter(multipartRequestWrapper, httpResponse);
			return;
		}
		// 继续原有的请求
		filterChain.doFilter(request, response);
		
	}

	public void destroy() {
		super.destroy();
		this.sc = null;
		this.filterConfig = null;
	}
}
