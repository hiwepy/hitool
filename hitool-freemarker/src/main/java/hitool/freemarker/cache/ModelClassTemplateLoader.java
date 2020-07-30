package hitool.freemarker.cache;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;
import hitool.core.io.ResourceUtils;

public class ModelClassTemplateLoader extends URLTemplateLoader {
   
	protected URL getURL(String name) {
    	return ResourceUtils.getResourceAsURL(name, getClass());
    }
    
}