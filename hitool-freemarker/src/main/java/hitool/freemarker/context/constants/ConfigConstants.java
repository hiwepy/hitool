package hitool.freemarker.context.constants;

public class ConfigConstants {
	
	//---------Freemarker 初始化参数Key ---------------------------------------
	/**
	 * Key[template_dir] : pdf 临时文件存储路径 ，默认 ：template_dir
	 */
	public static final String TEMPLATE_DIR_KEY = "template_dir";
	
	public static final String TEMPLATE_CLASSPATH_KEY = "template_dir";
	
	/**
	 * Key[out_dir] : Freemarker 生成的html文件存储路径 ，默认 ：out_dir
	 */
	public static final String OUT_DIR_KEY = "out_dir";
	/**
	 * Key[freemarker.config.path] : FreemarkerContext 初始化properties文件路径 .  默认 ： freemarker-config.properties
	 */
	public static final String CONFIG_FILE_PATH_KEY = "config_file_path";
	public static final String DEFAULT_ENCODING_KEY = "default_encoding"; 
	public static final String LOCALIZED_LOOKUP_KEY = "localized_lookup";
	public static final String STRICT_SYNTAX_KEY = "strict_syntax";
	public static final String WHITESPACE_STRIPPING_KEY = "whitespace_stripping";
	public static final String CACHE_STORAGE_KEY = "cache_storage";
	public static final String TEMPLATE_UPDATE_DELAY_KEY = "template_update_delay";
    /** @since 2.3.17 */
    public static final String AUTO_FLUSH_KEY = "auto_flush";
	public static final String AUTO_IMPORT_KEY = "auto_import";
	public static final String AUTO_INCLUDE_KEY = "auto_include";
	public static final String TAG_SYNTAX_KEY = "tag_syntax";
	public static final String OUT_SUFFIX_KEY = "out_suffix";

	public static final String LOCALE_KEY = "locale";
    public static final String NUMBER_FORMAT_KEY = "number_format";
    public static final String TIME_FORMAT_KEY = "time_format";
    public static final String DATE_FORMAT_KEY = "date_format";
    public static final String DATETIME_FORMAT_KEY = "datetime_format";
    public static final String TIME_ZONE_KEY = "time_zone";
    public static final String CLASSIC_COMPATIBLE_KEY = "classic_compatible";
    public static final String TEMPLATE_EXCEPTION_HANDLER_KEY = "template_exception_handler";
    public static final String ARITHMETIC_ENGINE_KEY = "arithmetic_engine";
    public static final String OBJECT_WRAPPER_KEY = "object_wrapper";
    public static final String BOOLEAN_FORMAT_KEY = "boolean_format";
    public static final String OUTPUT_ENCODING_KEY = "output_encoding";
    public static final String URL_ESCAPING_CHARSET_KEY = "url_escaping_charset";
    public static final String STRICT_BEAN_MODELS = "strict_bean_models";
    /** @since 2.3.17 */
    public static final String NEW_BUILTIN_CLASS_RESOLVER_KEY = "new_builtin_class_resolver";
}