package hitool.mail.authc;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 密码验证器
 */
public class EmailAuthenticator extends Authenticator {

	protected String username = null;
	protected String password = null;

	public EmailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(getUsername(), getPassword());
	}
	
}
