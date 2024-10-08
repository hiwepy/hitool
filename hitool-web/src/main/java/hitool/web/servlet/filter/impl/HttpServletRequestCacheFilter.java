package hitool.web.servlet.filter.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import hitool.web.servlet.filter.OncePerRequestFilter;
import hitool.web.servlet.http.HttpServletCacheResponseWrapper;

public class HttpServletRequestCacheFilter extends OncePerRequestFilter {

	long cacheTimeout = Long.MAX_VALUE;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		String ct = filterConfig.getInitParameter("cacheTimeout");
		if (ct != null) {
			cacheTimeout = 60 * 1000 * Long.parseLong(ct);
		}
		this.sc = filterConfig.getServletContext();
	}

	@Override
	@SuppressWarnings({ "unused", "resource" })
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// check if was a resource that shouldn't be cached.
		String r = sc.getRealPath("");
		String path = filterConfig.getInitParameter(httpRequest.getRequestURI());
		if (path != null && path.equals("nocache")) {
			filterChain.doFilter(request, response);
			return;
		}
		path = r + path;

		// customize to match parameters
		String id = httpRequest.getRequestURI() + httpRequest.getQueryString();
		// optionally append i18n sensitivity
		String localeSensitive = filterConfig.getInitParameter("locale-sensitive");
		if (localeSensitive != null) {
			StringWriter ldata = new StringWriter();
			Enumeration<Locale> locales = request.getLocales();
			while (locales.hasMoreElements()) {
				Locale locale = (Locale) locales.nextElement();
				ldata.write(locale.getISO3Language());
			}
			id = id + ldata.toString();
		}
		File tempDir = (File) sc.getAttribute("jakarta.servlet.context.tempdir");

		// get possible cache
		String temp = tempDir.getAbsolutePath();
		File file = new File(temp + id);

		// get current resource
		if (path == null) {
			path = sc.getRealPath(httpRequest.getRequestURI());
		}
		File current = new File(path);

		try {
			long now = Calendar.getInstance().getTimeInMillis();
			// set timestamp check
			if (!file.exists()
					|| (file.exists() && current.lastModified() > file.lastModified())
					|| cacheTimeout < now - file.lastModified()) {

				String name = file.getAbsolutePath();
				name = name.substring(0, name.lastIndexOf("/") == -1 ? 0 : name.lastIndexOf("/"));
				new File(name).mkdirs();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				HttpServletCacheResponseWrapper wrappedResponse = new HttpServletCacheResponseWrapper(httpResponse, baos);
				filterChain.doFilter(request, wrappedResponse);

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
			}
		} catch (ServletException e) {
			if (!file.exists()) {
				throw new ServletException(e);
			}
		} catch (IOException e) {
			if (!file.exists()) {
				throw e;
			}
		}

		FileInputStream fis = new FileInputStream(file);
		String mt = sc.getMimeType(httpRequest.getRequestURI());
		response.setContentType(mt);
		ServletOutputStream sos = response.getOutputStream();
		for (int i = fis.read(); i != -1; i = fis.read()) {
			sos.write((byte) i);
		}
	}

	public void destroy() {
		super.destroy();
		this.sc = null;
		this.filterConfig = null;
	}
}
