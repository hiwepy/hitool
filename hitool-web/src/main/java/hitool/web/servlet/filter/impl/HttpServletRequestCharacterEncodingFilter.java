package hitool.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.web.servlet.http.HttpServletCharacterEncodingRequestWrapper;

/*
 *  此过滤器用来解决解决get、post请求方式下的中文乱码问题
 */
public class HttpServletRequestCharacterEncodingFilter implements Filter {

	private String charset = "UTF-8";

	public void init(FilterConfig filterConfig) throws ServletException {
		// 得到在web.xml中配置的字符编码
		charset = filterConfig.getInitParameter("charset");
		if (charset == null) {
			charset = "UTF-8";
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		response.setContentType("text/html;charset=" + charset);
		HttpServletCharacterEncodingRequestWrapper requestWrapper = new HttpServletCharacterEncodingRequestWrapper(request);
		chain.doFilter(requestWrapper, response);
	}

	public void destroy() {

	}
}
