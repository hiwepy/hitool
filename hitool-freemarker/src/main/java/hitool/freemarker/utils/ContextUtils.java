package hitool.freemarker.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

/**
 * Value Stack's Context related Utilities.
 *
 */
public class ContextUtils {
	
    public static final String REQUEST = "request";
    public static final String REQUEST2 = "request";
    public static final String RESPONSE = "response";
    public static final String RESPONSE2 = "response";
    public static final String SESSION = "session";
    public static final String BASE = "base";
    public static final String STACK = "stack";
    public static final String OGNL = "ognl";
    public static final String STRUTS = "struts";
    public static final String ACTION = "action";
    
    public static Map<String,Object> getStandardContext(Map<String,Object> stack, HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put(REQUEST, req);
        map.put(REQUEST2, req);
        map.put(RESPONSE, res);
        map.put(RESPONSE2, res);
        map.put(SESSION, req.getSession(false));
        map.put(BASE, req.getContextPath());
        map.put(STACK, stack);
        return map;
    }

    /**
     * Returns a String for overriding the default templateSuffix if templateSuffix is on the stack
     * @param context stack's context
     * @return String
     */
    public static String getTemplateSuffix(Map<String,Object> context) {
        return context.containsKey("templateSuffix") ? (String) context.get("templateSuffix") : null;
    }
}
