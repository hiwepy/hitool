package hitool.crypto.binary;



import java.math.BigInteger;
import java.util.Random;

/**
 * @author 陈明 E-mail:chenming@sansec.com.cn
 * @version 创建时间：2011-4-15 上午11:03:41
 * @		修改时间：2011-5-31 
 * 
 */
public class SM3Digest {
	
	private static final int BYTE_LENGTH = 32; 					/**SM3值的长度*/
	private static final int BLOCK_LENGTH = 64;					/**SM3分组长度*/
	private static final int BUFFER_LENGTH = BLOCK_LENGTH * 1;	/**缓冲区长度*/
	private byte[] xBuf = new byte[BUFFER_LENGTH];				/**缓冲区*/
	private int xBufOff;										/**缓冲区偏移量*/
	private byte[] V = SM3.iv.clone();							/**初始向量*/
	private int cntBlock = 0; 
	public SM3Digest() {
	}
	
	/**
	 * SM3结果输出
	 * @param out 保存SM3结构的缓冲区
	 * @param outOff 缓冲区偏移量
	 * @return
	 */
	public int doFinal(byte[] out, int outOff) {
		byte[] tmp = doFinal();
		System.arraycopy(tmp, 0, out, 0, tmp.length);
		return BYTE_LENGTH;
	}


	public void reset() {
		xBufOff = 0;
		cntBlock = 0;
		V = SM3.iv.clone();
	}

	/**
	 * 明文输入
	 * @param in 明文输入缓冲区
	 * @param inOff 缓冲区偏移量
	 * @param len 明文长度
	 */
	public void update(byte[] in, int inOff, int len) {
		int partLen = BUFFER_LENGTH - xBufOff;
		int inputLen = len;
		int dPos = inOff;
		if(partLen < inputLen) {
			System.arraycopy(in, dPos, xBuf, xBufOff, partLen);
			inputLen -= partLen;
			dPos += partLen;
			doUpdate();
			while(inputLen > BUFFER_LENGTH) {
				System.arraycopy(in, dPos, xBuf, 0, BUFFER_LENGTH);
				inputLen -= BUFFER_LENGTH;
				dPos += BUFFER_LENGTH;
				doUpdate();
			}
		}
		
		System.arraycopy(in, dPos, xBuf, xBufOff, inputLen);
		xBufOff += inputLen;
	}
	
	private void doUpdate() {
		byte[] B = new byte[BLOCK_LENGTH];
		for(int i=0; i<BUFFER_LENGTH; i +=BLOCK_LENGTH) {
			System.arraycopy(xBuf, i, B, 0, B.length);
			doHash(B);
		}
		xBufOff = 0;
	}
	
	private void doHash(byte[] B) {
		byte[] tmp = SM3.CF(V, B);
		System.arraycopy(tmp, 0, V, 0, V.length);
		cntBlock++;
	}
	
	private byte[] doFinal() {
		byte[] B = new byte[BLOCK_LENGTH];
		byte[] buffer = new byte[xBufOff];
		System.arraycopy(xBuf, 0, buffer, 0, buffer.length);
		byte[] tmp = SM3.padding(buffer, cntBlock);
		for(int i=0; i<tmp.length; i +=BLOCK_LENGTH) {
			System.arraycopy(tmp, i, B, 0, B.length);
			doHash(B);
		}
		
		return V;
	}
	
	private byte[] getSM2Za(byte[] x, byte[] y, byte[] id) {
		byte[] tmp = Util.IntToByte(id.length*8);
		byte[] buffer = new byte[32*6+2+id.length];
		buffer[0] = tmp[1];
		buffer[1] = tmp[0];
		byte[] a = Util.getA();
		byte[] b = Util.getB();
		byte[] gx = Util.getGx();
		byte[] gy = Util.getGy();
		int dPos = 2;
		System.arraycopy(id, 0, buffer, dPos, id.length);
		dPos += id.length;
		System.arraycopy(a, 0, buffer, dPos, 32);
		dPos += 32;
		System.arraycopy(b, 0, buffer, dPos, 32);
		dPos += 32;
		System.arraycopy(gx, 0, buffer, dPos, 32);
		dPos += 32;
		System.arraycopy(gy, 0, buffer, dPos, 32);
		dPos += 32;
		System.arraycopy(x, 0, buffer, dPos, 32);
		dPos += 32;
		System.arraycopy(y, 0, buffer, dPos, 32);
		dPos += 32;
		
		//
		//PrintUtil.printWithHex(buffer);
		SM3Digest digest = new SM3Digest();
		digest.update(buffer, 0, buffer.length);
		byte[] out = new byte[32];
		digest.doFinal(out, 0);
		
		return out;
	}

	/**
	 * @param affineX SM2公钥仿射坐标X
	 * @param affineX SM2公钥仿射坐标Y
	 * @param id 
	 */
	public void addId(BigInteger affineX, BigInteger affineY, byte[] id) {
		byte[] x = Util.asUnsigned32ByteArray(affineX);
		byte[] y = Util.asUnsigned32ByteArray(affineY);
		byte[] tmp = getSM2Za(x, y, id);
		//System.out.println("getSM2Za ........................");
		reset();
		System.arraycopy(tmp, 0, xBuf, xBufOff, 32);
		xBufOff = 32;
	}

	public String getAlgorithmName() {
		return "SM3";
	}

	public int getDigestSize() {
		return BYTE_LENGTH;
	}


	public void update(byte in) {
		byte[] buffer = new byte[]{in};
		update(buffer, 0, 1);
	}
}

class SM3 {
	public static final byte[] iv = new BigInteger("7380166f4914b2b9172442d7da8a0600a96f30bc163138aae38dee4db0fb0e4e", 16).toByteArray();
	public static int[] Tj = new int[64];
	static {
		for(int i=0; i<16; i++) {
			Tj[i] = 0x79cc4519;
		}
		for(int i=16; i<64; i++) {
			Tj[i] = 0x7a879d8a;
		}
	}

	public static byte[] CF(byte[] V, byte[] B) {
		int[] v, b;
		v = convert(V);
		b = convert(B);
		
		return convert(CF(v, b));
	}
	
	private static int[] convert(byte[] arr) {
		int[] out = new int[arr.length/4];
		byte[] tmp = new byte[4];
		for(int i=0; i<arr.length; i += 4) {
			System.arraycopy(arr, i, tmp, 0, 4);
			out[i/4] = bigEndianByteToInt(tmp);
		}
		
		return out;
	}
	
	private static byte[] convert(int[] arr) {
		byte[] out = new byte[arr.length*4];
		byte[] tmp = null;
		for(int i=0; i<arr.length; i++) {
			tmp = bigEndianIntToByte(arr[i]);
			System.arraycopy(tmp, 0, out, i*4, 4);
		}
		
		return out;
	}
	
	public static int[] CF(int[] V, int[] B) {
		int a, b, c, d, e, f, g, h;
		int ss1, ss2, tt1, tt2;
		a = V[0];
		b = V[1];
		c = V[2];
		d = V[3];
		e = V[4];
		f = V[5];
		g = V[6];
		h = V[7];
		/*System.out.print("  ");
		System.out.print(Integer.toHexString(a)+" ");
		System.out.print(Integer.toHexString(b)+" ");
		System.out.print(Integer.toHexString(c)+" ");
		System.out.print(Integer.toHexString(d)+" ");
		System.out.print(Integer.toHexString(e)+" ");
		System.out.print(Integer.toHexString(f)+" ");
		System.out.print(Integer.toHexString(g)+" ");
		System.out.print(Integer.toHexString(h)+" ");
		System.out.println();*/
		/*System.out.println("block ...");
		for(int i=0; i<B.length; i++) {
			System.out.print(Integer.toHexString(B[i])+" ");
		}
		System.out.println();
		System.out.println("iv ...");
		for(int i=0; i<V.length; i++) {
			System.out.print(Integer.toHexString(V[i])+" ");
		}
		System.out.println();*/
		
		int[][] arr = expand(B);
		int[] w = arr[0];
		int[] w1 = arr[1];
		/*System.out.println("W");
		print(w);
		System.out.println("W1");
		print(w1);*/
		//System.out.println("---------------------------------------------------------");
		for(int j=0; j<64; j++) {
			ss1 = (bitCycleLeft(a, 12) + e + bitCycleLeft(Tj[j], j));
			ss1 = bitCycleLeft(ss1, 7);
			ss2 = ss1 ^ bitCycleLeft(a, 12);
			tt1 = FFj(a, b, c, j) + d + ss2 + w1[j];
			tt2 = GGj(e, f, g, j) + h + ss1 + w[j];
			d = c;
			c = bitCycleLeft(b, 9);
			b = a;
			a = tt1;
			h = g;
			g = bitCycleLeft(f, 19);
			f = e;
			e = P0(tt2);
			
			/*System.out.print(j+" ");
			System.out.print(Integer.toHexString(a)+" ");
			System.out.print(Integer.toHexString(b)+" ");
			System.out.print(Integer.toHexString(c)+" ");
			System.out.print(Integer.toHexString(d)+" ");
			System.out.print(Integer.toHexString(e)+" ");
			System.out.print(Integer.toHexString(f)+" ");
			System.out.print(Integer.toHexString(g)+" ");
			System.out.print(Integer.toHexString(h)+" ");
			System.out.println();*/
		}
		//System.out.println("*****************************************");
		
		int[] out = new int[8];
		out[0] = a ^ V[0];
		out[1] = b ^ V[1];
		out[2] = c ^ V[2];
		out[3] = d ^ V[3];
		out[4] = e ^ V[4];
		out[5] = f ^ V[5];
		out[6] = g ^ V[6];
		out[7] = h ^ V[7];
		
		return out;
	}
	
	private static int[][] expand(byte[] B) {
		//PrintUtil.printWithHex(B);
		int W[] = new int[68];
		int W1[] = new int[64];
		byte[] tmp = new byte[4];
		for(int i=0; i<B.length; i+=4) {
			for(int j=0; j<4; j++) {
				tmp[j] = B[i+j];
			}
			W[i/4] = bigEndianByteToInt(tmp);
		}
		
		for(int i=16; i<68; i++) {
			W[i] = P1(W[i-16] ^ W[i-9] ^ bitCycleLeft(W[i-3], 15)) ^ bitCycleLeft(W[i-13], 7) ^ W[i-6];
		}	
		
		for(int i=0; i<64; i++) {
			W1[i] = W[i] ^ W[i+4];
		}
		
		int arr[][] = new int[][]{W, W1};
		
		return arr;
	}
	
	private static int[][] expand(int[] B) {
		int W[] = new int[68];
		int W1[] = new int[64];
		for(int i=0; i<B.length; i++) {
			W[i] = B[i];
		}
		
		for(int i=16; i<68; i++) {
			W[i] = P1(W[i-16] ^ W[i-9] ^ bitCycleLeft(W[i-3], 15)) ^ bitCycleLeft(W[i-13], 7) ^ W[i-6];
		}	
		
		for(int i=0; i<64; i++) {
			W1[i] = W[i] ^ W[i+4];
		}
		
		int arr[][] = new int[][]{W, W1};
		
		return arr;
	}
	
	private static byte[] bigEndianIntToByte(int num) {
		return back(Util.IntToByte(num));
	}
	
	private static int bigEndianByteToInt(byte[] bytes) {
		return Util.ByteToInt(back(bytes));
	}
	
	private static int FFj(int X, int Y, int Z, int j) {
		if(j>=0 && j<=15) {
			return FF1j(X, Y, Z);
		} else {
			return FF2j(X, Y, Z);
		}
	}
	private static int GGj(int X, int Y, int Z, int j) {
		if(j>=0 && j<=15) {
			return GG1j(X, Y, Z);
		} else {
			return GG2j(X, Y, Z);
		}
	}
	/***********************************************/
	// 逻辑位运算函数
	private static int FF1j(int X, int Y, int Z) {
		int tmp = X ^ Y ^ Z;
		
		return tmp;
	}
	
	private static int FF2j(int X, int Y, int Z) {
		int tmp = ( (X & Y) | (X & Z) | (Y & Z) );
		
		return tmp;
	}
	
	private static int GG1j(int X, int Y, int Z) {
		int tmp = X ^ Y ^ Z;
		
		return tmp;
	}
	
	private static int GG2j(int X, int Y, int Z) {
		int tmp = (X & Y) | (~X & Z) ;
		
		return tmp;
	}
	
	private static int P0(int X) {
		int y = rotateLeft(X, 9);
		y = bitCycleLeft(X, 9);
		int z = rotateLeft(X, 17);
		z = bitCycleLeft(X, 17);
		int t = X ^ y ^ z;
		
		return t;
	}
	
	private static int P1(int X) {
		int t = X ^ bitCycleLeft(X, 15) ^ bitCycleLeft(X, 23);
		
		return t;
	}
	
	/**
	 * 对最后一个分组字节数据padding
	 * @param in
	 * @param bLen 分组个数
	 * @return
	 */
	public static byte[] padding(byte[] in, int bLen) {
		//第一bit为1 所以长度=8 * in.length+1 k为所补的bit k+1/8 为需要补的字节
		int k = 448 - (8 * in.length+1) % 512;
		if( k < 0) {
			k = 960 - (8 * in.length+1) % 512;
		}
		k += 1;
		byte[] padd = new byte[k/8];
		padd[0] = (byte)0x80;
		long n = in.length * 8+bLen*512;
		//64/8 字节 长度
		//k/8 字节padding
		byte[] out = new byte[in.length+k/8+64/8];
		int pos = 0;
		System.arraycopy(in, 0, out, 0, in.length);
		pos += in.length;
		System.arraycopy(padd, 0, out, pos, padd.length);
		pos += padd.length;
		byte[] tmp = back(Util.LongToByte(n));
		System.arraycopy(tmp, 0, out, pos, tmp.length);
		
		return out;
	}
	
	/**
	 * 字节数组逆序
	 * @param in
	 * @return
	 */
	private static byte[] back(byte[] in) {
		byte[] out = new byte[in.length];
		for(int i=0; i<out.length; i++) {
			out[i] = in[out.length-i-1];
		}
		
		return out;
	}
	
	
	public static int rotateLeft(int x, int n) {
		return (x<<n) | (x >> (32-n));
		//return (((x) << (n)) | ((x) >> (32-(n))));
	}
	
	
	private static int bitCycleLeft(int n, int bitLen) {
		bitLen %= 32;
		byte[] tmp = bigEndianIntToByte(n);
		int byteLen = bitLen / 8;
		int len = bitLen % 8;
		if( byteLen > 0) {
			tmp = byteCycleLeft(tmp, byteLen);
		}
		
		if(len > 0) {
			tmp = bitSmall8CycleLeft(tmp, len);
		}
		
		return bigEndianByteToInt(tmp);
	}

	private static byte[] bitSmall8CycleLeft(byte[] in, int len) {
		byte[] tmp = new byte[in.length];
		int t1, t2, t3;
		for(int i=0; i<tmp.length; i++) {
			t1 = (byte) ((in[i] & 0x000000ff)<<len);
			t2 = (byte) ((in[(i+1)%tmp.length] & 0x000000ff)>>(8-len));
			t3 = (byte) (t1 | t2);
			tmp[i] = (byte)t3;
		}
		
		
		return tmp;
	}
	
	private static byte[] byteCycleLeft(byte[] in, int byteLen) {
		byte[] tmp = new byte[in.length];
		System.arraycopy(in, byteLen, tmp, 0, in.length-byteLen);
		System.arraycopy(in, 0, tmp, in.length-byteLen, byteLen);
		
		return tmp;
	}
	
	public static void main(String[] args) {
		Random r = new Random();
		int x = r.nextInt();
		int n = r.nextInt();
		/*while(x < 1) {
			x = r.nextInt();
		}*/
		while(n < 1) {
			n = r.nextInt();
		}
		x = -529020450 ;
		n = 294715530 ;
		System.out.println("x = "+x+" , n = "+n+" , n%32 = " + n%32);
		System.out.println("x = "+Integer.toBinaryString(x));
		int y;
		y = rotateLeft(x, n);
		System.out.println(y);
		y = bitCycleLeft(x, n);
		System.out.println("x = "+Integer.toBinaryString(y));
		System.out.println(y);
	}
	public static void print(int[] arr) {
		for(int i=0; i<arr.length; i++) {
			/*System.out.print(PrintUtil.toHexString(back(ConvertUtil.IntToByte(arr[i]))) + " ");
			if((i+1) % 8 == 0) {
				System.out.println();
			}*/
			System.out.print(Integer.toHexString(arr[i]) + " ");
			if((i+1)%16 == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}
}

class Util {
	private static BigInteger p = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
	private static BigInteger a = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
	private static BigInteger b = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
	private static BigInteger n = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
	private static BigInteger Gx = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
	private static BigInteger Gy = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
	/*private static BigInteger p = new BigInteger("8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3", 16);
	private static BigInteger a = new BigInteger("787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498", 16);
	private static BigInteger b = new BigInteger("63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A", 16);
	private static BigInteger n = new BigInteger("8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7", 16);
	private static BigInteger Gx = new BigInteger("421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D", 16);
	private static BigInteger Gy = new BigInteger("0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2", 16);*/
	public static byte[] getP() {
		return asUnsigned32ByteArray(p);
	}
	public static byte[] getA() {
		return asUnsigned32ByteArray(a);
	}
	public static byte[] getB() {
		return asUnsigned32ByteArray(b);
	}
	public static byte[] getN() {
		return asUnsigned32ByteArray(n);
	}
	public static byte[] getGx() {
		return asUnsigned32ByteArray(Gx);
	}
	public static byte[] getGy() {
		return asUnsigned32ByteArray(Gy);
	}
	/*static {
		System.out.println("p len = " + p.toByteArray().length);
		System.out.println("a len = " + a.toByteArray().length);
		System.out.println("b len = " + b.toByteArray().length);
		System.out.println("n len = " + n.toByteArray().length);
		System.out.println("Gx len = " + Gx.toByteArray().length);
		System.out.println("Gy len = " + Gy.toByteArray().length);
	}*/
	/**
	 * 整形转换成网络传输的字节流（字节数组）型数据
	 * @param num 一个整型数据
	 * @return 4个字节的自己数组
	 */
	public static byte[] IntToByte(int num) {
		byte[] bytes = new byte[4];
		
		bytes[0] = (byte)(0xff&(num>>0));
		bytes[1] = (byte)(0xff&(num>>8));
		bytes[2] = (byte)(0xff&(num>>16));
		bytes[3] = (byte)(0xff&(num>>24));
		
		return bytes;
	}
	
	/**
	 * 四个字节的字节数据转换成一个整形数据
	 * @param bytes 4个字节的字节数组
	 * @return 一个整型数据
	 */
	public static int ByteToInt(byte[] bytes) {
		int num = 0;
		int temp;
		temp = (0x000000ff & (bytes[0]))<<0;
		num = num | temp;
		temp = (0x000000ff & (bytes[1]))<<8;
		num = num | temp;
		temp = (0x000000ff & (bytes[2]))<<16;
		num = num | temp;
		temp = (0x000000ff & (bytes[3]))<<24;
		num = num | temp;
		
		return num;
	}
	
	public static byte[] LongToByte(long num) {
		byte[] bytes = new byte[8];
		
		for(int i=0; i<8; i++) {
			bytes[i] = (byte)(0xff&(num>>(i*8)));
		}
		
		return bytes;
	}
	
	
	public static byte[] asUnsigned32ByteArray(BigInteger n) {
		return asUnsignedNByteArray(n, 32);
	}
	public static byte[] asUnsignedNByteArray(BigInteger x, int length) {
		if(x == null) {
			return null;
		}
		
		byte[] tmp = new byte[length];
		int len = x.toByteArray().length;
		if(len > length+1) {
			return null;
		}
		
		if(len == length+1) {
			if(x.toByteArray()[0] != 0) {
				return null;
			} else {
				System.arraycopy(x.toByteArray(), 1, tmp, 0, length);
				return tmp;
			}
		} else {
			System.arraycopy(x.toByteArray(), 0, tmp, length-len, len);
			return tmp;
		}
		
	}
}
