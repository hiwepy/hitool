package hitool.web.servlet.filter.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.web.servlet.filter.OncePerRequestFilter;
import hitool.web.servlet.http.GenericResponseWrapper;

public class HttpServletJsonpCallbackFilter extends OncePerRequestFilter {

	private static Logger LOG = LoggerFactory.getLogger(HttpServletJsonpCallbackFilter.class);

	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Map<String, String[]> parms = httpRequest.getParameterMap();

		if (parms.containsKey("callback")) {
			if (LOG.isDebugEnabled()){
				LOG.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");
			}
			OutputStream out = httpResponse.getOutputStream();
			GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);
			filterChain.doFilter(request, wrapper);
			// handles the content-size truncation
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(new String(parms.get("callback")[0] + "(").getBytes());
			outputStream.write(wrapper.getData());
			outputStream.write(new String(");").getBytes());
			byte jsonpResponse[] = outputStream.toByteArray();

			wrapper.setContentType("text/javascript;charset=UTF-8");
			wrapper.setContentLength(jsonpResponse.length);

			out.write(jsonpResponse);

			out.close();

		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
