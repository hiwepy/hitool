package hitool.freemarker.template;


import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Context used when rendering templates.
 */
public class TemplateRenderingContext {
    Template template;
    HttpServletRequest request;
    HttpServletResponse response;
    Map<String,Object> stack;
    Map parameters;
    Writer writer;

    /**
     * Constructor
     *
     * @param template  the template.
     * @param writer    the writer.
     * @param stack     OGNL value stack.
     * @param params    parameters to this template.
     * @param tag       the tag UI component.
     */
    public TemplateRenderingContext(HttpServletRequest request,HttpServletResponse response,Template template, Writer writer, Map<String,Object> stack,Map params) {
    	this.request = request;
    	this.response = response;
    	this.template = template;
        this.writer = writer;
        this.stack = stack;
        this.parameters = params;
    }

    public Template getTemplate() {
        return template;
    }

    public Map<String,Object> getStack() {
        return stack;
    }

    public Map getParameters() {
        return parameters;
    }

    public Writer getWriter() {
        return writer;
    }

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
    
}
