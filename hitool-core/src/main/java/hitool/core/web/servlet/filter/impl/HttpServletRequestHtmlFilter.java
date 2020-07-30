 package hitool.core.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.core.web.servlet.filter.OncePerRequestFilter;
import hitool.core.web.servlet.http.HttpServletHtmlFilterRequestWrapper;

public class HttpServletRequestHtmlFilter extends OncePerRequestFilter {

	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpServletHtmlFilterRequestWrapper myrequest = new HttpServletHtmlFilterRequestWrapper(httpRequest);
		filterChain.doFilter(myrequest, httpResponse);

	}

}


