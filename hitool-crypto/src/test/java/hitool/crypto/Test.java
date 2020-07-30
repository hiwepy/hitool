package hitool.crypto;
import java.io.FileInputStream;
import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;

import hitool.crypto.binary.PrintUtil;
import hitool.crypto.binary.SM3Digest;


/**
 * @author 陈明 E-mail:chenming@sansec.com.cn
 * @version 创建时间：2011-5-25 上午08:43:26
 * 
 */
public class Test {
	static String data = "abc1232131abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc12321314343abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143abc123213143";
	static {
		int len = 63;
		data = "abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc1abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc1223213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc1abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc1223213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12213143abc123213143abc12abc1232131abc123213143abc123213143abc123213143abc12";
		data = data.substring(0, 200);
		data = "abcd";
		byte[] tmp = new byte[32];
		for(int i=1; i<32; i++) {
			tmp[i] = 0x0a;
		}
		data = new String(tmp);
		//data = "hello";
	}
	static byte[] dataInput = data.getBytes();
	static {
		//dataInput = new byte[data.getBytes().length+1];
		//System.arraycopy(data.getBytes(), 0, dataInput, 0, data.getBytes().length);
	}
	public static void main(String[] args) throws Exception {
		//loadData();
		//testSm3();
		//System.out.println(data);
		testSm2Sign();
	}
	static void loadData() throws Exception {
		String inName = "input.txt";
		FileInputStream fis = new FileInputStream(inName);
		dataInput = new byte[fis.available()];
		fis.read(dataInput);
		fis.close();
	}
	static void testSm3() {
		SM3Digest digest = new SM3Digest();
		digest.update(dataInput, 0, dataInput.length);
		//digest.update(dataInput, 0, dataInput.length);
		byte[] out = new byte[32];
		digest.doFinal(out, 0);
		PrintUtil.printWithHex(out);
		System.out.println(dataInput.length);
	}
	
	static void testSm2Sign() throws Exception {
		SM3Digest digest = new SM3Digest();
		// 1. 设置ID
		BigInteger affineX = new BigInteger("78c3808e94daf6778ccc14718de43d6fe277dd6de687f0092f512bf026d71490", 16);
		BigInteger affineY = new BigInteger("6a678fc314097a382dee3cb864dd827cf53494bcfb7c2ab70203b3c625230398", 16);
		
		byte[] buffer = Base64.decodeBase64("xvsrJs1PjZIC5Ibvpw1bZjp1L4oMc6oV7Jq0sBe12hXbaDusRQkAhA40AmZXsp1cWr1WEumOjDpW8AluKKU/ow==");
		byte[] x = new byte[32];
		byte[] y = new byte[32];
		/*System.arraycopy(buffer, 0, x, 0, 32);
		System.arraycopy(buffer, 32, y, 0, 32);
		affineX = new BigInteger(x);
		affineY = new BigInteger(y);
		PrintUtil.printWithHex(affineX.toByteArray());
		PrintUtil.printWithHex(affineY.toByteArray());
		FileOutputStream fos = new FileOutputStream("x.dat");
		fos.write(affineX.toByteArray());
		fos.close();
		fos = new FileOutputStream("y.dat");
		fos.write(affineY.toByteArray());
		fos.close();*/
		byte[] ID = "012345678ABCDEF012345678ABCDEF".getBytes();
		FileInputStream fis = new FileInputStream("pubkey.dat");
		buffer = new byte[64];
		fis.read(buffer);
		System.arraycopy(buffer, 0, x, 0, 32);
		System.arraycopy(buffer, 32, y, 0, 32);
		fis = new FileInputStream("id.dat");
		fis.read(ID);
		fis = new FileInputStream("data.dat");
		buffer = new byte[fis.available()];
		fis.read(buffer);
		byte[] hash = new byte[32];
		fis = new FileInputStream("sm3.dat");
		fis.read(hash);
		fis.close();
		affineX = new BigInteger(x);
		affineY = new BigInteger(y);
		
		digest.addId(affineX, affineY, ID);
		PrintUtil.printWithHex(buffer);
		// 2. update 明文
		digest.update(buffer, 0, buffer.length);
		byte[] out = new byte[32];
		digest.doFinal(out, 0);
		System.out.println("soft");
		PrintUtil.printWithHex(out);
		System.out.println("hard");
		PrintUtil.printWithHex(hash);
	}
	
}
