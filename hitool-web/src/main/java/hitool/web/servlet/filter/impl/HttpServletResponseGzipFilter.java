package hitool.web.servlet.filter.impl;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.web.servlet.filter.OncePerRequestFilter;
import hitool.web.servlet.http.HttpServletGzipResponseWrapper;

public class HttpServletResponseGzipFilter extends OncePerRequestFilter {

	protected transient Logger LOG = LoggerFactory.getLogger(HttpServletResponseGzipFilter.class);
	
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String ae = httpRequest.getHeader("accept-encoding");
		if (ae != null && ae.indexOf("gzip") != -1) {
			LOG.info("GZIP supported, compressing.");
			HttpServletGzipResponseWrapper wrappedResponse = new HttpServletGzipResponseWrapper(httpResponse);
			filterChain.doFilter(request, wrappedResponse);
			wrappedResponse.finishResponse();
			return;
		}
		filterChain.doFilter(request, response);

	}

}
