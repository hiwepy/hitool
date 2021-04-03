/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils.reflection.access;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.wraper.ClassLoaderWrapper;

public abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

	/*
     * Private internal log instance.
     */
	protected static final Logger LOG = LoggerFactory.getLogger(ExceptionIgnoringAccessor.class);
   
	protected static final ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

    @SuppressWarnings("unchecked")
	public <T> Class<T> loadClass(String fqcn) {
        Class<T> clazz = null;
        try {
            clazz = (Class<T>) classLoaderWrapper.classForName(fqcn, getClassLoader());
        } catch (ClassNotFoundException e) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Unable to load clazz named [" + fqcn + "]", e);
            }
        }
        return clazz;
    }

    public InputStream getResourceStream(String resource) {
        return classLoaderWrapper.getResourceAsStream(resource, getClassLoader());
    }

    protected final ClassLoader getClassLoader() {
        try {
            return doGetClassLoader();
        } catch (Throwable t) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unable to acquire ClassLoader.", t);
            }
        }
        return null;
    }

    protected abstract ClassLoader doGetClassLoader() throws Throwable;
    
}
