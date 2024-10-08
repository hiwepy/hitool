 package hitool.web.servlet.filter.impl;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import hitool.web.servlet.filter.OncePerRequestFilter;
import hitool.web.servlet.http.HttpServletHtmlFilterRequestWrapper;

public class HttpServletRequestHtmlFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpServletHtmlFilterRequestWrapper myrequest = new HttpServletHtmlFilterRequestWrapper(httpRequest);
		filterChain.doFilter(myrequest, httpResponse);

	}

}


