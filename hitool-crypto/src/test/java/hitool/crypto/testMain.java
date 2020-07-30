package hitool.crypto;

import hitool.crypto.algorithm.SMS4;

public class testMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] in={0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,(byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10};
		byte[] in1={
				0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,(byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10,
				0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,(byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10
				};
		byte[] key={0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,(byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10};
		SMS4 sm4=new SMS4();
		int inLen=16,ENCRYPT=1,DECRYPT=0,inlen1=32;
		byte[] out=new byte[16];
		byte[] out1=new byte[32];
		long starttime;
		
		//加密 128bit
		starttime=System.nanoTime();
		sm4.sms4(in, inLen, key, out, ENCRYPT);
		System.out.println("加密1个分组执行时间： "+(System.nanoTime()-starttime)+"ns");
		System.out.println("加密结果：");
		for(int i=0;i<16;i++)
			System.out.print(Integer.toHexString(out[i]&0xff)+"\t");
		//解密 128bit
		System.out.println();
		sm4.sms4(out, inLen, key, in, DECRYPT);
		System.out.println("解密结果：");
		for(int i=0;i<16;i++)
			System.out.print(Integer.toHexString(in[i]&0xff)+"\t");
		//加密多个分组
		System.out.println();
		sm4.sms4(in1, inlen1, key, out1, ENCRYPT);
		System.out.println("\r多分组加密结果：");
		for(int i=0;i<32;i++)
			System.out.print(Integer.toHexString(out1[i]&0xff)+"\t");
		//解密多个分组
		System.out.println();
		sm4.sms4(out1, inlen1, key, in1, DECRYPT);
		System.out.println("多分组解密结果：");
		for(int i=0;i<32;i++)
			System.out.print(Integer.toHexString(in1[i]&0xff)+"\t");
		//1,000,000次加密
		System.out.println();
		starttime=System.currentTimeMillis();
		for(int i=1;i<1000000;i++)
		{
			sm4.sms4(in, inLen, key, out, ENCRYPT);
			in=out;
		}
		sm4.sms4(in, inLen, key, out, ENCRYPT);
		System.out.println("\r1000000次加密执行时间： "+(System.currentTimeMillis()-starttime)+"ms");
		System.out.println("加密结果：");
		for(int i=0;i<16;i++)
			System.out.print(Integer.toHexString(out[i]&0xff)+"\t");
		
	}

}
