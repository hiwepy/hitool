package hitool.core.resources;


import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MessageResource {
	
	private ResourceBundle bundle;
	private String location;
	
	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public MessageResource() {
		this.location = "label/word";
		this.bundle = ResourceBundle.getBundle(this.location,Locale.getDefault());
	}
	
	public MessageResource(String location){
		this.location = location;
		this.bundle = ResourceBundle.getBundle(location, Locale.getDefault());
	}
	
	public String getMessage(String key){
		String message = null;
		message = bundle.getString(key);
		return message;
	}
	
	public String getMessage(String key,Object[]params){
		String message = null;
		message = MessageFormat.format(bundle.getString(key), params);
		return message;
	}
	
	public String getMessage(String key,Object[]params,Locale locale){
		String message = null;
		this.bundle = ResourceBundle.getBundle(location, locale);
		message = MessageFormat.format(bundle.getString(key), params);
		return message;
	}
	
	public Properties getProperties(){
		Properties props = new Properties();
		Enumeration<String> keys = this.bundle.getKeys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String value = this.bundle.getString(key);
			props.setProperty(key, value);
		}
		return props;
	}
	
	public static void main(String[] args) {
		MessageResource resource = new MessageResource("com/jeekit/web/plugins/validate/c/label/word");
		String words = resource.getMessage("chinese.word.1");
		System.out.println(words);
	}
}
