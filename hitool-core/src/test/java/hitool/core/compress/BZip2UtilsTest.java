package hitool.core.compress;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Assert;
import org.junit.Test;

import hitool.core.compress.BZip2Utils;

public class BZip2UtilsTest {

	private String inputStr = "zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org";

	@Test
	public final void testDataCompress() throws Exception {

		byte[] input = inputStr.getBytes();
		System.err.println("原文:\t" + inputStr);
		System.err.println("长度:\t" + input.length);

		byte[] data = BZip2Utils.compress(input);
		System.err.println("压缩后长度:\t" + data.length);

		byte[] output = BZip2Utils.decompress(data);
		String outputStr = new String(output);
		System.err.println("解压缩后:\t" + outputStr);
		System.err.println("解压缩后长度:\t" + output.length);

		Assert.assertEquals(inputStr, outputStr);

	}

	@Test
	public final void testFileCompress() throws Exception {

		FileOutputStream fos = new FileOutputStream("d:/f.txt");

		fos.write(inputStr.getBytes());
		fos.flush();
		fos.close();

		BZip2Utils.compress("d:/f.txt");

		BZip2Utils.decompress("d:/f.txt.bz2");

		File file = new File("d:/f.txt");

		FileInputStream fis = new FileInputStream(file);

		DataInputStream dis = new DataInputStream(fis);

		byte[] data = new byte[(int) file.length()];
		dis.readFully(data);

		fis.close();

		String outputStr = new String(data);
		Assert.assertEquals(inputStr, outputStr);
	}
}
