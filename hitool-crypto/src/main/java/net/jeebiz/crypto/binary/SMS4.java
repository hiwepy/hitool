package net.jeebiz.crypto.binary;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import net.jeebiz.crypto.utils.StringUtils;

public class SMS4 {
	
	private static SMS4 instance = null;
	private SMS4(){};
	public static SMS4 getInstance(){
		instance= (instance==null)?instance=new SMS4():instance;
		return  instance;
	}
	
	
	public static final int ENCRYPT = 1;
	public static final int DECRYPT = 0;
	public static final int ROUND=32;
	private static final int BLOCK=16;
	
	private byte[] Sbox={
			(byte) 0xd6,(byte) 0x90,(byte) 0xe9,(byte) 0xfe,(byte) 0xcc,(byte) 0xe1,0x3d,(byte) 0xb7,0x16,(byte) 0xb6,0x14,(byte) 0xc2,0x28,(byte) 0xfb,0x2c,0x05,
			0x2b,0x67,(byte) 0x9a,0x76,0x2a,(byte) 0xbe,0x04,(byte) 0xc3,(byte) 0xaa,0x44,0x13,0x26,0x49,(byte) 0x86,0x06,(byte) 0x99,
			(byte) 0x9c,0x42,0x50,(byte) 0xf4,(byte) 0x91,(byte) 0xef,(byte) 0x98,0x7a,0x33,0x54,0x0b,0x43,(byte) 0xed,(byte) 0xcf,(byte) 0xac,0x62,
			(byte) 0xe4,(byte) 0xb3,0x1c,(byte) 0xa9,(byte) 0xc9,0x08,(byte) 0xe8,(byte) 0x95,(byte) 0x80,(byte) 0xdf,(byte) 0x94,(byte) 0xfa,0x75,(byte) 0x8f,0x3f,(byte) 0xa6,
			0x47,0x07,(byte) 0xa7,(byte) 0xfc,(byte) 0xf3,0x73,0x17,(byte) 0xba,(byte) 0x83,0x59,0x3c,0x19,(byte) 0xe6,(byte) 0x85,0x4f,(byte) 0xa8,
			0x68,0x6b,(byte) 0x81,(byte) 0xb2,0x71,0x64,(byte) 0xda,(byte) 0x8b,(byte) 0xf8,(byte) 0xeb,0x0f,0x4b,0x70,0x56,(byte) 0x9d,0x35,
			0x1e,0x24,0x0e,0x5e,0x63,0x58,(byte) 0xd1,(byte) 0xa2,0x25,0x22,0x7c,0x3b,0x01,0x21,0x78,(byte) 0x87,
			(byte) 0xd4,0x00,0x46,0x57,(byte) 0x9f,(byte) 0xd3,0x27,0x52,0x4c,0x36,0x02,(byte) 0xe7,(byte) 0xa0,(byte) 0xc4,(byte) 0xc8,(byte) 0x9e,
			(byte) 0xea,(byte) 0xbf,(byte) 0x8a,(byte) 0xd2,0x40,(byte) 0xc7,0x38,(byte) 0xb5,(byte) 0xa3,(byte) 0xf7,(byte) 0xf2,(byte) 0xce,(byte) 0xf9,0x61,0x15,(byte) 0xa1,
			(byte) 0xe0,(byte) 0xae,0x5d,(byte) 0xa4,(byte) 0x9b,0x34,0x1a,0x55,(byte) 0xad,(byte) 0x93,0x32,0x30,(byte) 0xf5,(byte) 0x8c,(byte) 0xb1,(byte) 0xe3,
			0x1d,(byte) 0xf6,(byte) 0xe2,0x2e,(byte) 0x82,0x66,(byte) 0xca,0x60,(byte) 0xc0,0x29,0x23,(byte) 0xab,0x0d,0x53,0x4e,0x6f,
			(byte) 0xd5,(byte) 0xdb,0x37,0x45,(byte) 0xde,(byte) 0xfd,(byte) 0x8e,0x2f,0x03,(byte) 0xff,0x6a,0x72,0x6d,0x6c,0x5b,0x51,
			(byte) 0x8d,0x1b,(byte) 0xaf,(byte) 0x92,(byte) 0xbb,(byte) 0xdd,(byte) 0xbc,0x7f,0x11,(byte) 0xd9,0x5c,0x41,0x1f,0x10,0x5a,(byte) 0xd8,
			0x0a,(byte) 0xc1,0x31,(byte) 0x88,(byte) 0xa5,(byte) 0xcd,0x7b,(byte) 0xbd,0x2d,0x74,(byte) 0xd0,0x12,(byte) 0xb8,(byte) 0xe5,(byte) 0xb4,(byte) 0xb0,
			(byte) 0x89,0x69,(byte) 0x97,0x4a,0x0c,(byte) 0x96,0x77,0x7e,0x65,(byte) 0xb9,(byte) 0xf1,0x09,(byte) 0xc5,0x6e,(byte) 0xc6,(byte) 0x84,
			0x18,(byte) 0xf0,0x7d,(byte) 0xec,0x3a,(byte) 0xdc,0x4d,0x20,0x79,(byte) 0xee,0x5f,0x3e,(byte) 0xd7,(byte) 0xcb,0x39,0x48
	};
	
	private int[] CK={
			0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
			0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
			0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
			0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
			0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
			0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
			0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
			0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279
	};
	
	private int Rotl(int x,int y)
	{
		return x<<y|x>>>(32-y);
	}
	
	private int ByteSub(int A)
	{
		return (Sbox[A>>>24&0xFF]&0xFF)<<24|(Sbox[A>>>16&0xFF]&0xFF)<<16|(Sbox[A>>>8&0xFF]&0xFF)<<8|(Sbox[A&0xFF]&0xFF);
	}

	private int L1(int B)
	{
		return B^Rotl(B,2)^Rotl(B,10)^Rotl(B,18)^Rotl(B,24);
	//	return B^(B<<2|B>>>30)^(B<<10|B>>>22)^(B<<18|B>>>14)^(B<<24|B>>>8);
	}
	
	private int L2(int B)
	{
		return B^Rotl(B,13)^Rotl(B,23);
	//	return B^(B<<13|B>>>19)^(B<<23|B>>>9);
	}
	
	
	void SMS4Crypt(byte[] Input,byte[] Output,int[] rk)
	{
		int r,mid,x0,x1,x2,x3;
		int[] x= new int[4];
		int[] tmp = new int[4];
		for(int i=0;i<4;i++)
		{
			tmp[0]=Input[0+4*i]&0xff;
			tmp[1]=Input[1+4*i]&0xff;
			tmp[2]=Input[2+4*i]&0xff;
			tmp[3]=Input[3+4*i]&0xff;
			x[i]=tmp[0]<<24|tmp[1]<<16|tmp[2]<<8|tmp[3];
		//	x[i]=(Input[0+4*i]<<24|Input[1+4*i]<<16|Input[2+4*i]<<8|Input[3+4*i]);
		}
		for(r=0;r<32;r+=4)
		{
			mid=x[1]^x[2]^x[3]^rk[r+0];
			mid=ByteSub(mid);
			x[0]=x[0]^L1(mid);   //x4
			
			mid=x[2]^x[3]^x[0]^rk[r+1];
			mid=ByteSub(mid);
			x[1]=x[1]^L1(mid);	//x5
			
			mid=x[3]^x[0]^x[1]^rk[r+2];
			mid=ByteSub(mid);
			x[2]=x[2]^L1(mid);	//x6
			
			mid=x[0]^x[1]^x[2]^rk[r+3];
			mid=ByteSub(mid);
			x[3]=x[3]^L1(mid);	//x7
		}
		
		//Reverse
		for(int j=0;j<16;j+=4)
		{
			Output[j]  =(byte) (x[3-j/4]>>>24&0xFF);
			Output[j+1]=(byte) (x[3-j/4]>>>16&0xFF);
			Output[j+2]=(byte) (x[3-j/4]>>>8&0xFF);
			Output[j+3]=(byte) (x[3-j/4]&0xFF);
		}
	}
	
	private void SMS4KeyExt(byte[] Key,int[] rk,int CryptFlag)
	{
		int r,mid;
		int[] x= new int[4];
		int[] tmp =new int[4];
		for(int i=0;i<4;i++)
		{
			tmp[0]=Key[0+4*i]&0xFF;
			tmp[1]=Key[1+4*i]&0xff;
			tmp[2]=Key[2+4*i]&0xff;
			tmp[3]=Key[3+4*i]&0xff;
			x[i]=tmp[0]<<24|tmp[1]<<16|tmp[2]<<8|tmp[3];
		//	x[i]=Key[0+4*i]<<24|Key[1+4*i]<<16|Key[2+4*i]<<8|Key[3+4*i];
		}
		x[0]^=0xa3b1bac6;
		x[1]^=0x56aa3350;
		x[2]^=0x677d9197;
		x[3]^=0xb27022dc;
		for(r=0;r<32;r+=4)
		{
			mid=x[1]^x[2]^x[3]^CK[r+0];
			mid=ByteSub(mid);
			rk[r+0]=x[0]^=L2(mid);		//rk0=K4
			
			mid=x[2]^x[3]^x[0]^CK[r+1];
			mid=ByteSub(mid);
			rk[r+1]=x[1]^=L2(mid);		//rk1=K5
			
			mid=x[3]^x[0]^x[1]^CK[r+2];
			mid=ByteSub(mid);
			rk[r+2]=x[2]^=L2(mid);		//rk2=K6
			
			mid=x[0]^x[1]^x[2]^CK[r+3];
			mid=ByteSub(mid);
			rk[r+3]=x[3]^=L2(mid);		//rk3=K7
		}
		
		//解密时轮密钥使用顺序：rk31,rk30,...,rk0
		if(CryptFlag==DECRYPT)
		{
			for(r=0;r<16;r++)
			{
				mid=rk[r];
				rk[r]=rk[31-r];
				rk[31-r]=mid;
			}
		}
	}
	
	public byte[] encrypt(byte[] input,byte[] key){
		byte[] out = new byte[input.length];
		int inLen = input.length;
		sms4(input, inLen, key, out, ENCRYPT);
		return out;
	}
	
	public byte[] decrypt(byte[] input,byte[] key){
		byte[] out = new byte[input.length];
		int inLen = input.length;
		sms4(input, inLen, key, out, DECRYPT);
		return out;
	}
	
	private int sms4(byte[] in,int inLen,byte[] key,byte[] out,int CryptFlag){
		int point = 0;
		int[] round_key = new int[ROUND]; 
		SMS4KeyExt(key,round_key,CryptFlag);
		byte[] input = new byte[16];
		byte[] output = new byte[16];
		
		while(inLen >= BLOCK) {
			input = Arrays.copyOfRange(in, point, point+16);
		//	output=Arrays.copyOfRange(out, point, point+16);
			SMS4Crypt(input,output,round_key);
			System.arraycopy(output, 0, out, point, BLOCK);
			inLen-=BLOCK;
			point+=BLOCK;
		}
		return 0;
	}
	
	/**
	 * @param args
	 * @throws GeneralSecurityException 
	 */
	public static void main(String[] args) throws GeneralSecurityException {
		
		/*
		 修改前：
		 
		 加密1个分组执行时间： 79927ns
		加密结果：
		68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	
		解密结果：
		1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	

		多分组加密结果：
		68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	
		多分组解密结果：
		1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	

		1000000次加密执行时间： 1030ms
		加密结果：
		59	52	98	c7	c6	fd	27	1f	4	2	f8	4	c3	3d	3f	66	
		
		*/
		
		/*
		 * 修改后：
		加密1个分组执行时间： 89518ns
		加密结果：
		68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	
		解密结果：
		1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	

		多分组加密结果：
		68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	68	1e	df	34	d2	6	96	5e	86	b3	e9	4f	53	6e	42	46	
		多分组解密结果：
		1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	1	23	45	67	89	ab	cd	ef	fe	dc	ba	98	76	54	32	10	

		1000000次加密执行时间： 1045ms
		加密结果：
		59	52	98	c7	c6	fd	27	1f	4	2	f8	4	c3	3d	3f	66	*/
		
		// TODO Auto-generated method stub
		byte[] in = "4G USIM卡备卡是为您原卡制作的一张备用卡，相比原卡，安全性更高、容量更大、建议您尽快激活".getBytes();
		
		
		//byte[] key = "300061103172120170".getBytes();
		
		byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
				(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		System.out.println("Key：");
		for (int i = 0; i < key.length; i++){
			System.out.print(Integer.toHexString(key[i] & 0xff) + "");
		}
		
		byte[] out = null;
		long starttime;
		System.out.println();
		System.out.println("加密前：" + StringUtils.newStringUtf8(in));
		System.out.println("Hex：" );
		for (int i = 0; i < in.length; i++){
			System.out.print(Integer.toHexString(in[i] & 0xff) + "");
		}
		
		// 加密 128bit
		starttime = System.nanoTime();
		out = SMS4.getInstance().encrypt(in , key);
		System.out.println();
		System.out.println("加密1个分组执行时间： " + (System.nanoTime() - starttime) + "ns");
		System.out.print("加密后：");
		for (int i = 0; i < out.length; i++){
			System.out.print(Integer.toHexString(out[i]) + "");
		}
		
		// 解密 128bit
		System.out.println();
		in = SMS4.getInstance().decrypt(out, key);
		
		System.out.println("解密后："+ StringUtils.newStringUtf8(in));
		System.out.println("Hex：" );
		for (int i = 0; i < in.length; i++){
			System.out.print(Integer.toHexString(in[i] & 0xff) + "");
		}
		
		//================================================================
		
		/*// 加密多个分组
		System.out.println();
		
		
		out1 = SMS4Codec.getInstance().encrypt(in1,  key);
		System.out.println("\r多分组加密结果：");
		for (int i = 0; i < 32; i++){
			System.out.print(Integer.toHexString(out1[i] & 0xff) + "");
		}
		// 解密多个分组
		System.out.println();
		in1 = SMS4Codec.getInstance().decrypt(out1,  key);
		System.out.println("多分组解密结果：");
		for (int i = 0; i < 32; i++){
			System.out.print(Integer.toHexString(in1[i] & 0xff) + "");
		}
		
		//================================================================
		
		// 1,000,000次加密
		System.out.println();
		starttime = System.currentTimeMillis();
		for (int i = 1; i < 1000000; i++) {
			in = SMS4Codec.getInstance().encrypt(in,  key);
		}
		out = SMS4Codec.getInstance().encrypt(in, key);
		System.out.println("\r1000000次加密执行时间： " + (System.currentTimeMillis() - starttime) + "ms");
		System.out.println("加密结果：");
		for (int i = 0; i < 16; i++){
			System.out.print(Integer.toHexString(out[i] & 0xff) + "");
		}*/
		
		
	}
}
