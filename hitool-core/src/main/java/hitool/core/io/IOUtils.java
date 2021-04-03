/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.input.NonClosingInputStream;
import hitool.core.io.output.NonClosingOutputStream;
import hitool.core.lang3.Assert;

/*
 * 扩展org.apache.commons.io.IOUtils工具对象
 */
public abstract class IOUtils extends org.apache.commons.io.IOUtils {

	public static final int BUFFER_SIZE = 1024 * 4;
	private final static Logger LOG = LoggerFactory.getLogger(IOUtils.class);

	// ---------------------------------------------------------------------
	// Copy methods for java.io.InputStream / java.io.OutputStream
	// ---------------------------------------------------------------------

	/*
	 * Copy the contents of the given InputStream to the given OutputStream. Leaves
	 * both streams open when done.
	 * 
	 * @param input
	 *            the InputStream to copy from
	 * @param output
	 *            the OutputStream to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		Assert.notNull(input, "No InputStream specified");
		Assert.notNull(output, "No OutputStream specified");
		long count = 0;
		byte[] buffer = new byte[bufferSize];
		int n = -1;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
			count += n;
		}
		// Flush
		output.flush();
		return count;
	}

	/*
	 * Copy the contents of the given byte array to the given OutputStream. Closes
	 * the stream when done.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 */
	public static long copy(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		try (ByteArrayInputStream byteIn = new ByteArrayInputStream(in);){
			return copy(byteIn, out);
		}
	} 

	/*
	 * Copy the contents of the given String to the given output OutputStream.
	 * Leaves the stream open when done.
	 * 
	 * @param in
	 *            the String to copy from
	 * @param charset
	 *            the Charset
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(String in, Charset charset, OutputStream out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(charset, "No charset specified");
		Assert.notNull(out, "No OutputStream specified");
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}

	// ---------------------------------------------------------------------
	// Reader methods for System.in
	// ---------------------------------------------------------------------

	/*
	 * 接收键盘的输入
	 */
	public static String systemIn() throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter a line:");
		return stdin.readLine();
	}

	// ---------------------------------------------------------------------
	// Copy methods for java.io.Reader / java.io.Writer
	// ---------------------------------------------------------------------

	/*
	 * Copy the contents of the given Reader to the given Writer. Closes both when
	 * done.
	 * 
	 * @param in
	 *            the Reader to copy from
	 * @param out
	 *            the Writer to copy to
	 * @return the number of characters copied
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 */
	public static int copy(Reader in, Writer out, int bufferSize) throws IOException {
		Assert.notNull(in, "No Reader specified");
		Assert.notNull(out, "No Writer specified");
		try {
			int count = 0;
			char[] buffer = new char[bufferSize];
			int n = -1;
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
			return count;
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/*
	 * Copy the contents of the given String to the given output Writer. Closes the
	 * write when done.
	 * 
	 * @param in
	 *            the String to copy from
	 * @param out
	 *            the Writer to copy to
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 */
	public static void copy(String in, Writer out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(out, "No Writer specified");
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/*
	 * Copy the contents of the given Reader into a String. Closes the reader when
	 * done.
	 * 
	 * @param in
	 *            the reader to copy from
	 * @return the String that has been copied to
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}

	public static void copyResourcesRecursively(URL originUrl, File destination) throws Exception {
		URLConnection urlConnection = originUrl.openConnection();
		try {
			if (urlConnection instanceof JarURLConnection) {
				copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
			} else {
				FileUtils.copyDirectory(new File(originUrl.getPath()), destination); // 如果不是jar则采用目录copy
				if (LOG.isDebugEnabled()) {
					LOG.debug("copy dir '" + originUrl.getPath() + "' --> '" + destination.getPath() + "'");
				}
			}
		} catch (Exception e) {
			throw new Exception("URLConnection[" + urlConnection.getClass().getSimpleName()
					+ "] is not a recognized/implemented connection type.");
		}
	}

	public static void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection)
			throws IOException {
		JarFile jarFile = jarConnection.getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) { // 遍历jar内容逐个copy
			JarEntry entry = entries.nextElement();
			if (entry.getName().startsWith(jarConnection.getEntryName())) {
				String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
				File destFile = new File(destination, fileName);
				if (!entry.isDirectory()) {
					InputStream entryInputStream = jarFile.getInputStream(entry);
					FileUtils.copyInputStreamToFile(entryInputStream, destFile);
					if (LOG.isDebugEnabled()) {
						LOG.debug("copy jarfile to file '" + entry.getName() + "' --> '" + destination.getPath() + "'");
					}
				} else {
					FileUtils.forceMkdir(destFile);
					if (LOG.isDebugEnabled()) {
						LOG.debug("create dir '" + destFile.getPath() + "'");
					}
				}
			}
		}
	}

	// ---------------------------------------------------------------------
	// Stream Warp methods for java.io.InputStream / java.io.OutputStream
	// ---------------------------------------------------------------------

	public static InputStream toBufferedInputStream(File localFile, int bufferSize) throws IOException {
		// 包装文件输入流
		return toBufferedInputStream(new FileInputStream(localFile), bufferSize);
	}

	public static InputStream toBufferedInputStream(InputStream input) throws IOException {
		if (isBuffered(input)) {
			return (BufferedInputStream) input;
		} else {
			return org.apache.commons.io.output.ByteArrayOutputStream.toBufferedInputStream(input);
		}
	}

	public static InputStream toBufferedInputStream(InputStream input, int bufferSize) throws IOException {
		if (isBuffered(input)) {
			return (BufferedInputStream) input;
		} else {
			if (bufferSize > 0) {
				return new BufferedInputStream(input, bufferSize);
			}
			return new BufferedInputStream(input);
		}
	}

	public static OutputStream toBufferedOutputStream(OutputStream output) throws IOException {
		if (isBuffered(output)) {
			return (BufferedOutputStream) output;
		} else {
			return toBufferedOutputStream(output, BUFFER_SIZE);
		}
	}

	public static OutputStream toBufferedOutputStream(OutputStream output, int bufferSize) throws IOException {
		if (isBuffered(output)) {
			return (BufferedOutputStream) output;
		} else {
			if (bufferSize > 0) {
				return new BufferedOutputStream(output, bufferSize);
			}
			return new BufferedOutputStream(output);
		}
	}

	public static boolean isBuffered(InputStream input) {
		return input instanceof BufferedInputStream;
	}

	public static boolean isBuffered(OutputStream output) {
		return output instanceof BufferedOutputStream;
	}

	public static BufferedReader toBufferedReader(InputStream input) {
		return new BufferedReader(new InputStreamReader(input));
	}

	public static BufferedWriter toBufferedWriter(OutputStream output) {
		return new BufferedWriter(new OutputStreamWriter(output));
	}

	public static boolean isPrint(OutputStream output) {
		return output instanceof PrintStream;
	}

	public static InputStream toByteArrayInputStream(byte[] inputBytes) {
		return new ByteArrayInputStream(inputBytes);
	}

	public static InputStream toByteArrayInputStream(String text) {
		return toByteArrayInputStream(text.getBytes());
	}

	public static DataInputStream toDataInputStream(InputStream input) {
		return isDataInput(input) ? (DataInputStream) input : new DataInputStream(input);
	}

	private static boolean isDataInput(InputStream input) {
		return input instanceof DataInputStream;
	}

	public static DataOutputStream toDataOutputStream(OutputStream output) {
		return isDataOutput(output) ? (DataOutputStream) output : new DataOutputStream(output);
	}

	private static boolean isDataOutput(OutputStream output) {
		return output instanceof DataOutputStream;
	}

	public static FileInputStream toFileInputStream(File file) throws IOException {
		return new FileInputStream(file);
	}

	/*
	 * 获得一个FileOutputStream对象
	 * 
	 * @param file
	 * @param append
	 *            ： true:向文件尾部追见数据; false:清楚旧数据
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileOutputStream toFileOutputStream(File file, boolean append) throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	/*
	 * Returns a variant of the given {@link InputStream} where calling
	 * {@link InputStream#close() close()} has no effect.
	 * 
	 * @param input
	 *            the InputStream to decorate
	 * @return a version of the InputStream that ignores calls to close
	 */
	public static InputStream toNonClosingInputStream(InputStream input) {
		Assert.notNull(input, "No InputStream specified");
		return new NonClosingInputStream(input);
	}

	/*
	 * Returns a variant of the given {@link OutputStream} where calling
	 * {@link OutputStream#close() close()} has no effect.
	 * 
	 * @param output
	 *            the OutputStream to decorate
	 * @return a version of the OutputStream that ignores calls to close
	 */
	public static OutputStream toNonClosingOutputStream(OutputStream output) {
		Assert.notNull(output, "No OutputStream specified");
		return new NonClosingOutputStream(output);
	}

	public static PrintStream toPrintStream(File file) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file, true);
			return toPrintStream(stream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static PrintStream toPrintStream(OutputStream output) {
		return isPrint(output) ? (PrintStream) output : new PrintStream(output);
	}

	/*
	 * 跳过指定的长度,实现断点续传
	 * 
	 * @param input
	 * @param offset
	 * @throws IOException
	 */
	public static long skip(InputStream input, long offset) throws IOException {
		long at = offset;
		while (at > 0) {
			long amt = input.skip(at);
			if (amt == -1) {
				throw new EOFException(
						"offset [" + offset + "] larger than the length of input stream : unexpected EOF");
			}
			at -= amt;
		}
		return at;
	}

	/*
	 * 跳过指定的长度,实现断点续传
	 */
	public static void skip(FileChannel channel, long offset) throws IOException {
		if (offset > channel.size()) {
			throw new EOFException("offset [" + offset + "] larger than the length of file : unexpected EOF");
		}
		// 通过调用position()方法跳过已经存在的长度
		channel.position(Math.max(0, offset));
	}

}
