package hitool.freemarker.exception;


/*
 * ConfigurationException
 */
@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {

    /*
     * Constructs a <code>ConfigurationException</code> with no detail message.
     */
    public ConfigurationException() {
    }

    /*
     * Constructs a <code>ConfigurationException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public ConfigurationException(String s) {
        super(s);
    }
    

    /*
     * Constructs a <code>ConfigurationException</code> with no detail message.
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
    
    /*
     * Constructs a <code>ConfigurationException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public ConfigurationException(String s, Throwable cause) {
        super(s, cause);
    }
    
}
