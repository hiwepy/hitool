package hitool.core.compress;

import static org.junit.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

import hitool.core.compress.GZipUtils;

public class GZipUtilsTest {
	private String inputStr = "zlex@zlex.org,snowolf@zlex.org,zlex.snowolf@zlex.org";  
	  
    @Test  
    public final void testDataCompress() throws Exception {  
  
        System.err.println("原文:\t" + inputStr);  
  
        byte[] input = inputStr.getBytes();  
        System.err.println("长度:\t" + input.length);  
  
        byte[] data = GZipUtils.compress(input);  
        System.err.println("压缩后:\t");  
        System.err.println("长度:\t" + data.length);  
  
        byte[] output = GZipUtils.decompress(data);  
        String outputStr = new String(output);  
        System.err.println("解压缩后:\t" + outputStr);  
        System.err.println("长度:\t" + output.length);  
  
        assertEquals(inputStr, outputStr);  
  
    }  
  
    @Test  
    public final void testFileCompress() throws Exception {  
  
        FileOutputStream fos = new FileOutputStream("d:/f.txt");  
  
        fos.write(inputStr.getBytes());  
        fos.flush();  
        fos.close();  
  
        GZipUtils.compress("d:/f.txt", false);  
  
        GZipUtils.decompress("d:/f.txt.gz", false);  
  
        File file = new File("d:/f.txt");  
  
        FileInputStream fis = new FileInputStream(file);  
  
        DataInputStream dis = new DataInputStream(fis);  
  
        byte[] data = new byte[(int) file.length()];  
        dis.readFully(data);  
  
        fis.close();  
  
        String outputStr = new String(data);  
        assertEquals(inputStr, outputStr);  
    }  
}
