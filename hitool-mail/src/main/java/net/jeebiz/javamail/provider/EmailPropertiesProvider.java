package net.jeebiz.javamail.provider;

import java.util.Properties;

public interface EmailPropertiesProvider {

	public Properties props();
	
	public void setProps(Properties props);
	
}
