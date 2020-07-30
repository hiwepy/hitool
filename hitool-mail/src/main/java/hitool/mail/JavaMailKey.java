package hitool.mail;

/**
 * @see http://www.websina.com/bugzero/kb/javamail-properties.html
 * @see http://blog.csdn.net/fygwfygyiq/article/details/51718311
 */
public class JavaMailKey {

	/**
	 * The initial debug mode. Default is false.
	 */
	public static final String MAIL_DEBUG = "mail.debug";
	/**
	 * The return email address of the current user, used by the InternetAddress
	 * method getLocalAddress.
	 */
	public static final String MAIL_FROM = "mail.from";
	public static final String MAIL_FROM_DESC = "mail.from.desc";
	/**
	 * The default host name of the mail server for both Stores and Transports.
	 * Used if the mail.protocol.host property isn't set.
	 */
	public static final String MAIL_HOST = "mail.host";
	public static final String MAIL_HOST_DESC = "mail.host.desc";
	public static final String MAIL_PORT = "mail.port";

	/**
	 * The default user name to use when connecting to the mail server. 
	 * Used if the mail.protocol.user property isn't set.
	 */
	public static final String MAIL_USER = "mail.user";
	public static final String MAIL_PASSWORD = "mail.password";
	
	/**
	 * Default user name for SMTP.
	 */
	public static final String MAIL_SMTP_USER = "mail.smtp.user";
	/**
	 * Default user password for SMTP.
	 */
	public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
	/**
	 * The SMTP server to connect to.
	 */
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	/**
	 * The SMTP server port to connect to, if the connect() method doesn't
	 * explicitly specify one. Defaults to 25.
	 */
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	/**
	 * Socket connection timeout value in milliseconds. Default is infinite
	 * timeout.
	 */
	public static final String MAIL_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
	/**
	 * Socket I/O timeout value in milliseconds. Default is infinite timeout.
	 */
	public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
	/**
	 * Email address to use for SMTP MAIL command. This sets the envelope return
	 * address. Defaults to msg.getFrom() or InternetAddress.getLocalAddress().
	 * NOTE: mail.smtp.user was previously used for this.
	 */
	public static final String MAIL_SMTP_FROM = "mail.smtp.from";
	/**
	 * Local host name used in the SMTP HELO or EHLO command. Defaults to
	 * InetAddress.getLocalHost().getHostName() . Should not normally need to be
	 * set if your JDK and your name service are configured properly.
	 */
	public static final String MAIL_SMTP_LOCALHOST = "mail.smtp.localhost";
	/**
	 * Local address (host name) to bind to when creating the SMTP socket.
	 * Defaults to the address picked by the Socket class. Should not normally
	 * need to be set, but useful with multi-homed hosts where it's important to
	 * pick a particular local address to bind to.
	 */
	public static final String MAIL_SMTP_LOCALADDRESS = "mail.smtp.localaddress";
	/**
	 * Local port number to bind to when creating the SMTP socket. Defaults to
	 * the port number picked by the Socket class.
	 */
	public static final String MAIL_SMTP_LOCALPORT = "mail.smtp.localport";
	/**
	 * If false, do not attempt to sign on with the EHLO command. Defaults to
	 * true. Normally failure of the EHLO command will fallback to the HELO
	 * command; this property exists only for servers that don't fail EHLO
	 * properly or don't implement EHLO properly.
	 */
	public static final String MAIL_SMTP_EHLO = "mail.smtp.ehlo";
	/**
	 * If true, attempt to authenticate the user using the AUTH command.
	 * Defaults to false.
	 */
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	/**
	 * If set, lists the authentication mechanisms to consider, and the order in which to consider them. 
	 * Only mechanisms supported by the server and supported by the current implementation will be used. 
	 * The default is "LOGIN PLAIN DIGEST-MD5" , which includes all the authentication mechanisms supported by the current implementation.
	 */
	public static final String MAIL_SMTP_AUTH_MECHANISMS = "mail.smtp.auth.mechanisms";
	/**
	 * The submitter to use in the AUTH tag in the MAIL FROM command. Typically
	 * used by a mail relay to pass along information about the original
	 * submitter of the message. See also the setSubmitter method of SMTPMessage . 
	 * Mail clients typically do not use this.
	 */
	public static final String MAIL_SMTP_SUBMITTER = "mail.smtp.submitter";
	/**
	 * The NOTIFY option to the RCPT command. Either NEVER, or some combination of SUCCESS, FAILURE, and DELAY (separated by commas).
	 */
	public static final String MAIL_SMTP_DSN_NOTIFY = "mail.smtp.dsn.notify";
	/**
	 * The RET option to the MAIL command. Either FULL or HDRS.
	 */
	public static final String MAIL_SMTP_DSN_RET = "mail.smtp.dsn.ret";
	/**
	 * If set to true, and the server supports the 8BITMIME extension, text parts of messages that 
	 * use the "quoted-printable" or "base64" encodings are converted to use "8bit" encoding if 
	 * they follow the RFC2045 rules for 8bit text.
	 */
	public static final String MAIL_SMTP_ALLOW8BITMIME = "mail.smtp.allow8bitmime";
	/**
	 * If set to true, and a message has some valid and some invalid addresses, 
	 * send the message anyway, reporting the partial failure with a SendFailedException. 
	 * If set to false (the default), the message is not sent to any of the recipients if there is an invalid recipient address.
	 */
	public static final String MAIL_SMTP_SENDPARTIAL = "mail.smtp.sendpartial";
	/**
	 * The realm to use with DIGEST-MD5 authentication.
	 */
	public static final String MAIL_SMTP_SASL_REALM = "mail.smtp.sasl.realm";
	/**
	 * If set to false, the QUIT command is sent and the connection is immediately closed. 
	 * If set to true (the default), causes the transport to wait for the response to the QUIT command.
	 */
	public static final String MAIL_SMTP_QUITWAIT = "mail.smtp.quitwait";
	/**
	 * If set to true, causes the transport to include an SMTPAddressSucceededException for each address that is successful. 
	 * Note also that this will cause a SendFailedException to be thrown from the sendMessage method 
	 * of SMTPTransport even if all addresses were correct and the message was sent successfully.
	 */
	public static final String MAIL_SMTP_REPORTSUCCESS = "mail.smtp.reportsuccess";
	/**
	 * If set to a class that implements the javax.net.SocketFactory interface, this class will be used to create SMTP sockets. 
	 * Note that this is an instance of a class, not a name, and must be set using the put method, not the setProperty method.
	 */
	public static final String MAIL_SMTP_SOCKETFACTORY = "mail.smtp.socketFactory";
	/**
	 *  If set, specifies the name of a class that implements the javax.net.SocketFactory interface. 
	 *  This class will be used to create SMTP sockets.
	 */
	public static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
	/**
	 * If set to true, failure to create a socket using the specified socket factory class will 
	 * cause the socket to be created using the java.net.Socket class. Defaults to true.
	 */
	public static final String MAIL_SMTP_SOCKETFACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
	/**
	 * Specifies the port to connect to when using the specified socket factory. 
	 * If not set, the default port will be used.
	 */
	public static final String MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
	/**
	 *  If set to true, use SSL to connect and use the SSL port by default. 
	 *  Defaults to false for the "smtp" protocol and true for the "smtps" protocol.
	 */
	public static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
	/**
	 * If set to true, check the server identity as specified by RFC 2595 . 
	 * These additional checks based on the content of the server's certificate are intended to 
	 * prevent man-in-the-middle attacks. Defaults to false.
	 */
	public static final String MAIL_SMTP_SSL_CHECKSERVERIDENTITY = "mail.smtp.ssl.checkserveridentity";
	/**
	 * If set to a class that extends the javax.net.ssl.SSLSocketFactory class, this class will be used to create SMTP SSL sockets. 
	 * Note that this is an instance of a class, not a name, and must be set using the put method, not the setProperty method.
	 */
	public static final String MAIL_SMTP_SSL_SOCKETFACTORY = "mail.smtp.ssl.socketFactory";
	/**
	 * If set, specifies the name of a class that extends the javax.net.ssl.SSLSocketFactory class. 
	 * This class will be used to create SMTP SSL sockets.
	 */
	public static final String MAIL_SMTP_SSL_SOCKETFACTORY_CLASS = "mail.smtp.ssl.socketFactory.class";
	/**
	 *  Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.
	 */
	public static final String MAIL_SMTP_SSL_SOCKETFACTORY_PORT = "mail.smtp.ssl.socketFactory.port";
	/**
	 * Specifies the SSL protocols that will be enabled for SSL connections. 
	 * The property value is a whitespace separated list of tokens acceptable to the javax.net.ssl.SSLSocket.setEnabledProtocols method.
	 */
	public static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
	/**
	 * Specifies the SSL cipher suites that will be enabled for SSL connections. 
	 * The property value is a whitespace separated list of tokens acceptable to the javax.net.ssl.SSLSocket.setEnabledCipherSuites method.
	 */
	public static final String MAIL_SMTP_SSL_CIPHERSUITES = "mail.smtp.ssl.ciphersuites";
	/**
	 * Extension string to append to the MAIL command. 
	 * The extension string can be used to specify standard SMTP service extensions as well as vendor-specific extensions. 
	 * Typically the application should use the SMTPTransport method supportsExtension to verify 
	 * that the server supports the desired service extension. 
	 * See RFC 1869 and other RFCs that define specific extensions.
	 */
	public static final String MAIL_SMTP_MAILEXTENSION = "mail.smtp.mailextension";
	/**
	 * If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection before issuing any login commands. 
	 * Note that an appropriate trust store must configured so that the client will trust the server's certificate. 
	 * Defaults to false.
	 */
	public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	/**
	 * If true, requires the use of the STARTTLS command. 
	 * If the server doesn't support the STARTTLS command, or the command fails, the connect method will fail. Defaults to false.
	 */
	public static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
	/**
	 * If set to true, use the RSET command instead of the NOOP command in the isConnected method. 
	 * In some cases sendmail will respond slowly after many NOOP commands; use of RSET avoids this sendmail issue. 
	 * Defaults to false.
	 */
	public static final String MAIL_SMTP_USERSET = "mail.smtp.userset";

	/**
	 * Specifies the default message access protocol. 
	 * The Session method getTransport() returns a Transport object that implements this protocol.
	 * By default the first Transport provider in the configuration files is returned.
	 */
	public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	/**
	 * Specifies the default message access protocol. 
	 * The Session method getStore() returns a Store object that implements this protocol. 
	 * By default the first Store provider in the configuration files is returned.
	 */
	public static final String MAIL_STORE_PROTOCOL = "mail.store.protocol";

	public static final String MAIL_MIME_CHARSET = "mail.mime.charset";
	public static final String MAIL_MIME_DECODETEXT_STRICT = "mail.mime.decodetext.strict";
	public static final String MAIL_MIME_ENCODEEOL_STRICT = "mail.mime.encodeeol.strict";
	/**
	 * The MimeMessage class uses the InternetAddress method parseHeader to
	 * parse headers in messages. This property controls the strict flag passed
	 * to the parseHeader method. The default is true.
	 */
	public static final String MAIL_MIME_ADDRESS_STRICT = "mail.mime.address.strict";

}
