package net.jeebiz.javamail.authc;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {
	
	private String user = "";
	private String password = "";

	public ProxyAuthenticator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password.toCharArray());
	}
}