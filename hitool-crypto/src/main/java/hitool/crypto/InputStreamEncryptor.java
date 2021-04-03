package hitool.crypto;

/*
 * @title: InputStreamEncoder.java
 * @package hitool.crypto
 * @fescription: TODO(添加描述)
 */
import java.io.IOException;
import java.io.InputStream;

public interface InputStreamEncryptor {

	public String encode(InputStream plantStream) throws IOException;
	
}
