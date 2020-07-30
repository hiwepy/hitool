package hitool.freemarker.model;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;

public class LookupModel {

	public Class<?> clazz;
	public ClassLoader classLoader;
	public List<File> baseDirList;
	public ServletContext servletContext;
	
	/**
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * @param classLoader the classLoader to set
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @return the baseDirList
	 */
	public List<File> getBaseDirList() {
		return baseDirList;
	}

	/**
	 * @param baseDirList
	 *            the baseDirList to set
	 */
	public void setBaseDirList(List<File> baseDirList) {
		this.baseDirList = baseDirList;
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	

}
