package hitool.freemarker.template;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.freemarker.FreemarkerManager;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;

/**
 * Freemarker based template engine.
 */
public class FreemarkerTemplateEngine extends BaseTemplateEngine {
    
	protected FreemarkerManager freemarkerManager;
    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerTemplateEngine.class);

    public void setFreemarkerManager(FreemarkerManager mgr) {
        this.freemarkerManager = mgr;
    }
    
    public void renderTemplate(TemplateRenderingContext templateContext) throws Exception {
    	// get the various items required from the stack
    	Map<String,Object> stack = templateContext.getStack();
        HttpServletRequest req = templateContext.getRequest();
        HttpServletResponse res = templateContext.getResponse();
        ServletContext servletContext = req.getSession().getServletContext();
        
        // prepare freemarker
        Configuration config = freemarkerManager.getConfiguration(servletContext);

        // get the list of templates we can use
        List<Template> templates = templateContext.getTemplate().getPossibleTemplates(servletContext,this);

        // find the right template
        freemarker.template.Template template = null;
        String templateName = null;
        Exception exception = null;
        for (Template t : templates) {
            templateName = getFinalTemplateName(t);
                try {
                    // try to load, and if it works, stop at the first one
                    template = config.getTemplate(templateName);
                    break;
                } catch (ParseException e) {
                    // template was found but was invalid - always report this.
                    exception = e;
                    break;
                } catch (IOException e) {
                    // FileNotFoundException is anticipated - report the first IOException if no template found
                    if (exception == null) {
                        exception = e;
                    }
                }
        }

        if (template == null) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Could not load the FreeMarker template named '" + templateContext.getTemplate().getName() +"':");
                for (Template t : templates) {
                    LOG.error("Attempted: " + getFinalTemplateName(t));
                }
                LOG.error("The TemplateLoader provided by the FreeMarker Configuration was a: "+config.getTemplateLoader().getClass().getName());
            }
            if (exception != null) {
                throw exception;
            } else {
                return;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Rendering template " + templateName);
        }


        SimpleHash model = freemarkerManager.buildTemplateModel(stack, servletContext, req, res, config.getObjectWrapper());

        model.put("themeProperties", getThemeProps(servletContext,templateContext.getTemplate()));

        // the BodyContent JSP writer doesn't like it when FM flushes automatically --
        // so let's just not do it (it will be flushed eventually anyway)
        Writer writer = templateContext.getWriter();
        final Writer wrapped = writer;
        writer = new Writer() {
            public void write(char cbuf[], int off, int len) throws IOException {
                wrapped.write(cbuf, off, len);
            }

            public void flush() throws IOException {
                // nothing!
            }

            public void close() throws IOException {
                wrapped.close();
            }
        };

        try {
            template.process(model, writer);
        } finally {
        }
    }

    protected String getSuffix() {
        return "ftl";
    }


}
