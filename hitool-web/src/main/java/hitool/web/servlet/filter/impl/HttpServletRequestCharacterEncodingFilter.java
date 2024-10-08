package hitool.web.servlet.filter.impl;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import hitool.web.servlet.http.HttpServletCharacterEncodingRequestWrapper;

/*
 *  此过滤器用来解决解决get、post请求方式下的中文乱码问题
 */
public class HttpServletRequestCharacterEncodingFilter implements Filter {

	private String charset = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 得到在web.xml中配置的字符编码
		charset = filterConfig.getInitParameter("charset");
		if (charset == null) {
			charset = "UTF-8";
		}
	}

	@Override
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
