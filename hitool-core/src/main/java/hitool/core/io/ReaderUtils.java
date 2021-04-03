package hitool.core.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public abstract class ReaderUtils {

	public static BufferedReader makeBuffered(Reader in) {
		return (in instanceof BufferedReader) ? (BufferedReader) in
				: new BufferedReader(in);
	}

	public static StringBuffer readReader(Reader in) throws IOException {
		StringBuffer ret = new StringBuffer();
		in = makeBuffered(in);

		for (int c = readChar(in); c != -1; c = readChar(in)) {
			ret.append((char) c);
		}

		return ret;
	}

	public static StringBuffer readReader(Reader in, int len)
			throws IOException {
		StringBuffer ret = new StringBuffer();

		for (int i = 0; i < len; ++i) {
			int c = readChar(in);
			// System.err.println("ReaderUtils.readReader(" + len + ") i=" + i +
			// " got char " + c + " (" + (char)c + ")");
			ret.append((char) c);
		}

		return ret;
	}

	private static int readChar(Reader in) throws IOException {
		int i = in.read();
		return i;
	}

	public static StringBuffer readInputStream(InputStream in, String charset)
			throws IOException {
		return readReader(new InputStreamReader(in, charset));
	}

	public static StringBuffer readInputStream(InputStream in, String charset,
			int len) throws IOException {
		return readReader(new InputStreamReader(in, charset), len);
	}

	public static StringBuffer readInputStream(InputStream in)
			throws IOException {
		return readReader(new InputStreamReader(in));
	}

	public static StringBuffer readInputStream(InputStream in, int len)
			throws IOException {
		return readReader(new InputStreamReader(in), len);
	}

	public static void close(Reader reader) {
		try {
			if (reader != null)
				reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从一个String对象中读取数据
	 * 
	 * @param str
	 * @throws IOException
	 */
	public static char[] readerText(String str) throws IOException {
		StringReader in2 = new StringReader(str);
		char[] r = new char[str.length()];
		int c;
		int a = 0;
		while ((c = in2.read()) != -1) {
			r[a] = (char) c;
			a++;
		}
		in2.close();
		return r;
	}

	/**
	 * 从内存取出格式化输入
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static char[] readerBytes(byte[] bytes) throws IOException {
		char[] r = new char[bytes.length];
		int a = 0;
		try {
			DataInputStream in3 = new DataInputStream(new ByteArrayInputStream(
					bytes));
			while (true) {
				r[a] = (char) in3.readByte();
			}
		} catch (EOFException e) {
			System.out.println("End of stream ");
		}
		return r;
	}

	public static StringBuffer readFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String s = new String();
		StringBuffer ret = new StringBuffer();
		while ((s = in.readLine()) != null) {
			ret.append(s);
		}
		in.close();
		return ret;
	}

	/**
	 * 接收键盘的输入
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readerSystemIn() throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("Enter a line:");
		return stdin.readLine();
	}

	public static Reader convertToReader(InputStream in) {
		Reader ret = null;
		if (in != null) {
			ret = new InputStreamReader(in);
		}
		return ret;
	}

	public static Reader getResourceReader(ClassLoader loader, String filename) throws IOException {
		return convertToReader(ResourceUtils.getResourceAsStream(loader, filename));
	}

	public static Reader getResourceReader(Object self, String filename) throws IOException {
		return convertToReader(ResourceUtils.getResourceAsStream(self, filename));
	}
}
