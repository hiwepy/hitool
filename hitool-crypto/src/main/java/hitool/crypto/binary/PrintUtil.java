package hitool.crypto.binary;

/**
 * @author 陈明 E-mail:chenming@sansec.com.cn
 * @version 创建时间：2011-2-23 上午09:28:50
 * 
 */
public class PrintUtil {
	
	public  static String toHexString(byte[] data) {
		byte temp;
		int n;
		String str = "";
		for (int i = 1; i <= data.length; i++) {
			temp = data[i-1];
			n = (int) ((temp & 0xf0) >> 4);
			str += IntToHex(n);
			n = (int) ((temp & 0x0f));
			str += IntToHex(n);
			str += " ";
			if (i % 16 == 0) {
				str += "\n";
			}
		}

		return str;
	}
	
	public  static void printWithHex(byte[] data) {
		System.out.println(toHexString(data));
	}

	public static String IntToHex(int n) {
		if (n > 15 || n < 0) {
			return "";
		} else if ((n >= 0) && (n <= 9)) {
			return "" + n;
		} else {
			switch (n) {
			case 10: {
				return "A";
			}
			case 11: {
				return "B";
			}
			case 12: {
				return "C";
			}
			case 13: {
				return "D";
			}
			case 14: {
				return "E";
			}
			case 15: {
				return "F";
			}
			default:
				return "";
			}
		}
	}
}
