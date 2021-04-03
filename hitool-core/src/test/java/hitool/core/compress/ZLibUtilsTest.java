package hitool.core.compress;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.junit.Test;

import hitool.core.compress.ZLibUtils;

/*
 * ZLib压缩测试用例
 */
public class ZLibUtilsTest {

	private String inputStr = "zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org,zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org";
	
	@Test
	public final void testBytes() {
		System.err.println("字节压缩／解压缩测试");
		
		System.err.println("输入字符串:\t" + inputStr);
		byte[] input = inputStr.getBytes();
		System.err.println("输入字节长度:\t" + input.length);

		byte[] data = ZLibUtils.compress(input);
		System.err.println("压缩后字节长度:\t" + data.length);

		byte[] output = ZLibUtils.decompress(data);
		System.err.println("解压缩后字节长度:\t" + output.length);
		String outputStr = new String(output);
		System.err.println("输出字符串:\t" + outputStr);

		assertEquals(inputStr, outputStr);
	}

	@Test
	public final void testFile() {
		String filename = "zlib";
		File file = new File(filename);
		System.err.println("文件压缩／解压缩测试");
		String inputStr = "snowolf@zlex.org;dongliang@zlex.org;zlex.dongliang@zlex.org";
		System.err.println("输入字符串:\t" + inputStr);
		byte[] input = inputStr.getBytes();
		System.err.println("输入字节长度:\t" + input.length);

		try {

			FileOutputStream fos = new FileOutputStream(file);
			ZLibUtils.compress(input, fos);
			fos.close();
			System.err.println("压缩后字节长度:\t" + file.length());
		} catch (Exception e) {
			fail(e.getMessage());
		}

		byte[] output = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			output = ZLibUtils.decompress(fis);
			fis.close();

		} catch (Exception e) {
			fail(e.getMessage());
		}
		System.err.println("解压缩后字节长度:\t" + output.length);
		String outputStr = new String(output);
		System.err.println("输出字符串:\t" + outputStr);

		assertEquals(inputStr, outputStr);
	}
	@Test
	public void tsss(){
		 try {
			String t = "Das ist ein kleiner Test!";
			 SevenZOutputFile sevenZOutput = new SevenZOutputFile(new File("D:/test.7z"));
			    sevenZOutput.setContentCompression(SevenZMethod.LZMA2);
			    SevenZArchiveEntry entry = new SevenZArchiveEntry();
			    entry.setName("test");
			    sevenZOutput.putArchiveEntry(entry);
			    sevenZOutput.write(t.getBytes());
			    sevenZOutput.closeArchiveEntry();
			    entry = new SevenZArchiveEntry();
			    entry.setContentMethods(Arrays.asList(new SevenZMethodConfiguration(SevenZMethod.COPY)));	
			    entry.setName("test2");
			    sevenZOutput.putArchiveEntry(entry);
			    sevenZOutput.write(t.getBytes());
			    sevenZOutput.closeArchiveEntry();
			    sevenZOutput.finish();	
			    sevenZOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

