package hitool.freemarker.cache;


import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletContext;

import hitool.freemarker.template.Template;
import hitool.freemarker.template.TemplateEngine;

import freemarker.cache.TemplateLoader;

/**
 * When loading a template, if sees theme token in path, does a template search through
 * theme hierarchy for template, starting at the theme name after the token.
 */
public class FreemarkerThemeTemplateLoader implements TemplateLoader{
   
	private TemplateLoader parentTemplateLoader;
    private String themeExpansionToken;
    private TemplateEngine templateEngine;
    private ServletContext servletContext;
    
    public FreemarkerThemeTemplateLoader(ServletContext servletContext){
    	this.servletContext = servletContext;
    }
    
    
    /**
     * Initialize the loader for the given parent.
     */
    public void init(TemplateLoader parent) {
        this.parentTemplateLoader = parent;
    }

    /** {@inheritDoc} */
    public Object findTemplateSource(String name) throws IOException {
        int tokenIndex = (name == null) ? -1 : name.indexOf(themeExpansionToken);
        if (tokenIndex < 0) {
            return parentTemplateLoader.findTemplateSource(name);
        }

        int themeEndIndex = name.lastIndexOf('/');
        if (themeEndIndex < 0) {
            return parentTemplateLoader.findTemplateSource(name);
        }

        Template template = new Template(
            name.substring(0, tokenIndex - 1), 
            name.substring(tokenIndex + themeExpansionToken.length(), themeEndIndex), 
            name.substring(themeEndIndex + 1));
        
        List<Template> possibleTemplates = template.getPossibleTemplates(servletContext,templateEngine);
        for (Template possibleTemplate : possibleTemplates) {
            Object templateSource = parentTemplateLoader.findTemplateSource(
                    possibleTemplate.toString().substring(1));
            if (templateSource != null) {
                return templateSource;
            }
        }
        String parentTheme = (String) templateEngine.getThemeProps(servletContext,template).get("parent");
        if (parentTheme == null) {
            // no parent theme, no way to fetch parent template
            return null;
        }
        String parentName = "/" + template.getDir() + "/" + themeExpansionToken + parentTheme + "/" + template.getName();
        return this.findTemplateSource(parentName);
    }
    
    /** {@inheritDoc} */
    public long getLastModified(Object templateSource) {
        return parentTemplateLoader.getLastModified(templateSource);
    }

    /** {@inheritDoc} */
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return parentTemplateLoader.getReader(templateSource, encoding);
    }

    /** {@inheritDoc} */
    public void closeTemplateSource(Object templateSource) throws IOException {
        parentTemplateLoader.closeTemplateSource(templateSource);
    }

    public void setUIThemeExpansionToken(String token) {
        themeExpansionToken = token;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public TemplateLoader getParentTemplateLoader() {
        return parentTemplateLoader;
    }
}
