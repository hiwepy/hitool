package hitool.freemarker.loader;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;
import hitool.core.lang3.wraper.ClassLoaderWrapper;

public class LocationTemplateLoader extends URLTemplateLoader {
   
	private final ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();
	
	protected URL getURL(String name) {
        return classLoaderWrapper.getResourceAsURL(name);
    }
    
}	
