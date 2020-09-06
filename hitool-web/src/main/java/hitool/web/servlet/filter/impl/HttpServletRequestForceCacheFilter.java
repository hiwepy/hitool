package hitool.web.servlet.filter.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hitool.core.lang3.StringUtils;
import hitool.core.lang3.time.DateUtils;
import hitool.web.servlet.filter.OncePerRequestFilter;
/**
 * 设置response,使 Browser 缓存页面的过滤器
 */
public class HttpServletRequestForceCacheFilter extends OncePerRequestFilter {

	private String defaultPattern = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
	private String expiresDatePattern;
	private SimpleDateFormat sdf;
	private int cacheSeconds;

	public HttpServletRequestForceCacheFilter() {
		sdf = new SimpleDateFormat(expiresDatePattern, Locale.US);
	}

	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

	public void setExpiresDatePattern(String expiresDatePattern) {
		this.expiresDatePattern  =  StringUtils.getSafeStr(expiresDatePattern,defaultPattern);
		sdf = new SimpleDateFormat(expiresDatePattern, Locale.US);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		setExpiresDatePattern(filterConfig.getInitParameter("expiresDatePattern"));
		setCacheSeconds(StringUtils.getSafeInt(filterConfig.getInitParameter("cacheSeconds"),"0"));
	}
	
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//设置有限时间的缓存 
		httpResponse.setHeader("Pragma", "Pragma"); // HTTP/1.0 
		httpResponse.setHeader("Cache-Control", "max-age=".concat(String.valueOf(cacheSeconds)));// HTTP/1.1 
		httpResponse.setHeader("Expires", sdf.format(DateUtils.addSeconds(new Date(), cacheSeconds)));
		
		//无缓存 
		//httpResponse.setHeader("Pragma", "No-cache");
		//httpResponse.setHeader("Cache-Control", "no-cache");
		//httpResponse.setDateHeader("Expires", 0);
		
		filterChain.doFilter(request, response);
	}
}
