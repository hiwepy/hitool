package net.jeebiz.crypto;

/**
 * @title: InputStreamEncoder.java
 * @package net.jeebiz.crypto
 * @fescription: TODO(添加描述)
 */
import java.io.IOException;
import java.io.InputStream;

public interface InputStreamEncryptor {

	public String encode(InputStream plantStream) throws IOException;
	
}
