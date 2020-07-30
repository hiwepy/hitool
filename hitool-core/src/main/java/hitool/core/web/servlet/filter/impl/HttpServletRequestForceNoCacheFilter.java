package hitool.core.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.core.web.servlet.filter.OncePerRequestFilter;

/**
 *  设置response,使 Browser 不缓存页面的过滤器
 */
public class HttpServletRequestForceNoCacheFilter extends OncePerRequestFilter {

	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;


		/*<meta http-equiv="Pragma" http-equiv="no-cache" />
		<meta http-equiv="Cache-Control" http-equiv="no-cache, must-revalidate" />
		<meta http-equiv="Expires" http-equiv="0" />*/
		
		//无缓存 
		//response.setHeader("Pragma", "No-cache");
		//response.addHeader( "Cache-Control", "must-revalidate" );
		//response.addHeader( "Cache-Control", "no-cache" );
		//response.addHeader( "Cache-Control", "no-store" );
		//response.setDateHeader("Expires", 0);
		
		httpResponse.setHeader("Pragma", "no-cache");	// HTTP/1.0 
		httpResponse.addHeader( "Cache-Control", "must-revalidate" );	// HTTP/1.1
		httpResponse.addHeader( "Cache-Control", "no-cache" );
		httpResponse.addHeader( "Cache-Control", "no-store" );
		httpResponse.setDateHeader("Expires", -1);
		
		filterChain.doFilter(request, response);
	}

}
