/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MathUtils {

	private static String part_num = "\\d*|0.\\d*[1-9]\\d*|[1-9]\\d*.\\d*";
	//(±a,±b),(±a,+∞),(-∞,±b)
	private static Pattern open_interval_pattern_1 = Pattern.compile("^\\((?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + ")))\\)$");
	private static Pattern open_interval_pattern_2 = Pattern.compile("^\\((?:([-+]?(?:" + part_num + ")))\\,(?:([+]∞))\\)$");
	private static Pattern open_interval_pattern_3 = Pattern.compile("^\\((?:([-]∞))\\,(?:([-+]?(?:" + part_num + ")))\\)$");
	//[±a,±b]
	private static Pattern closed_interval_pattern = Pattern.compile("^\\[(?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + ")))\\]$");
	//(±a,±b],(-∞,±b]
	private static Pattern left_half_open_interval_pattern = Pattern.compile("^\\((?:([-+]?(?:" + part_num + "|∞)))\\,(?:([-+]?(?:" + part_num + ")))\\]$");
	//[±a,±b),[±a,+∞)
	private static Pattern right_half_open_interval_pattern = Pattern.compile("^\\[(?:([-+]?(?:" + part_num + ")))\\,(?:([-+]?(?:" + part_num + "|∞)))\\)$");
	/*
	 * 正无穷大符号：+∞
	 */
	public static final String POSITIVE_INFINITE = "+∞";
	/*
	 * 负无穷大符号：-∞
	 */
	public static final String NEGATIVE_INFINITE = "-∞";
	
	
	/*
	 * 开区间(±a,±b);不能是 (+∞,-∞) (+∞,+b) (-a,-∞)
	 */
	public static Matcher open_interval(String toMatchStr) {
		Matcher matcher_1 = open_interval_pattern_1.matcher(toMatchStr);
		if(matcher_1.matches()){
			return matcher_1;
		}
		Matcher matcher_2 = open_interval_pattern_2.matcher(toMatchStr);
		if(matcher_2.matches()){
			return matcher_2;
		}
		return open_interval_pattern_3.matcher(toMatchStr);
	}
	
	/*
	 * 闭区间[±a,±b];不能是 [±∞,±∞] [±a,±∞] [±a,±∞] [±∞,±b] [±∞,±b]
	 */ 
	public static Matcher closed_interval(String toMatchStr) {
		return closed_interval_pattern.matcher(toMatchStr);
	}
	
	/*
	 * 左半开半闭区间(±a,±b];不能是(±∞,±∞] (±a,±∞]
	 */
	public static Matcher left_half_open_interval(String toMatchStr) {
		return left_half_open_interval_pattern.matcher(toMatchStr);
	}
	
	/*
	 * 右半开半闭区间[±a,±b);不能是 [±a,±∞) [±∞,±∞) [±∞,±b)
	 */
	public static Matcher right_half_open_interval(String toMatchStr) {
		return right_half_open_interval_pattern.matcher(toMatchStr);
	}
	
	/*
	 * 区间范围检查：
	 * 如检查数字 48 是否在区间[12,+∞)中
	 */
	public static boolean contains(String x,String interval) {
		
		BigDecimal bigValue = new BigDecimal(x);
		// 开区间(±a,±b),(±a,+∞),(-∞,±b)
		Matcher matcher2 = open_interval(interval);
		if (matcher2.matches()) {
			String a = matcher2.group(1), b = matcher2.group(2);
			//(±a,+∞)
			if(POSITIVE_INFINITE.equals(b)){
				//传入的值x必须满足： x > a；才算在该区间内
				return bigValue.compareTo(new BigDecimal(a)) == 1;
			}
			//(-∞,±b)
			else if(NEGATIVE_INFINITE.equals(a)){
				//传入的值x必须满足： x < b；才算在该区间内
				return bigValue.compareTo(new BigDecimal(b)) == -1;
			}
			//(±a,±b)
			else{
				//传入的值x必须满足： a < x < b；才算在该区间内
				return bigValue.compareTo(new BigDecimal(a)) == 1 && bigValue.compareTo(new BigDecimal(b)) == -1;
			}
		}
		
		// 闭区间[±a,±b]
		Matcher matcher1 = closed_interval(interval);
		if (matcher1.matches()) {
			String a = matcher1.group(1), b = matcher1.group(2);
			BigDecimal bigA = new BigDecimal(a);
			BigDecimal bigB = new BigDecimal(b);
			//传入的值x必须满足：a ≤ x ≤ b；才算在该区间内
			return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0) && (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
		}
		
		// 左半开半闭区间(±a,±b],(-∞,±b]
		Matcher matcher3 = left_half_open_interval(interval);
		if (matcher3.matches()) {
			String a = matcher3.group(1), b = matcher3.group(2);
			BigDecimal bigB = new BigDecimal(b);
			//(-∞,±b]
			if(NEGATIVE_INFINITE.equals(a)){
				//传入的值x必须满足： x ≤ b；才算在该区间内
				return (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
			}
			//(±a,±b]
			else{
				BigDecimal bigA = new BigDecimal(a);
				//传入的值x必须满足： a < x ≤ b；才算在该区间内
				return (bigValue.compareTo(bigA) == 1) && (bigValue.compareTo(bigB) == -1 || bigValue.compareTo(bigB) == 0);
			}
		}
		
		// 右半开半闭区间[±a,±b),[±a,+∞)
		Matcher matcher4 = right_half_open_interval(interval);
		if (matcher4.matches()) {
			String a = matcher4.group(1), b = matcher4.group(2);
			BigDecimal bigA = new BigDecimal(a);
			//[±a,+∞)
			if(POSITIVE_INFINITE.equals(b)){
				//传入的值x必须满足： x ≥ a；才算在该区间内
				return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0);
			}
			//[±a,±b)
			else{
				BigDecimal bigB = new BigDecimal(b);
				//传入的值x必须满足： a ≤ x < b；才算在该区间内
				return (bigValue.compareTo(bigA) == 1 || bigValue.compareTo(bigA) == 0) && (bigValue.compareTo(bigB) == -1);
			}
		}
		
		return false;
	}
	
	public static boolean isDigit(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	//分布峰度Ku in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the Ku in an array. </p>
	 * <p>分布峰度Ku </p>
	 * @param array an array, must not be null or empty
	 * @return the Ku in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 **/
	public static byte ku(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	}

	public static short ku(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	}

	public static int ku(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	}

	public static double ku(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	}

	public static float ku(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	}
	
	public static long ku(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Ku
		return 0;
	} 
	
	//分布偏态SK in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the SK in an array. </p>
	 * <p>分布偏态SK=(M-Me) /σ</p>
	 * @param array an array, must not be null or empty
	 * @return the SK in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte sk(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		BigDecimal x = new BigDecimal(avg(array)).subtract(new BigDecimal(me(array)));
		return x.divide(new BigDecimal(σ(array))).byteValue();
	}

	public static short sk(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		BigDecimal x = new BigDecimal(avg(array)).subtract(new BigDecimal(me(array)));
		return x.divide(new BigDecimal(σ(array))).shortValue();
	}

	public static int sk(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		return (avg(array)-me(array))/σ(array);
	}

	public static double sk(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		return (avg(array)-me(array))/σ(array);
	}

	public static float sk(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		return (avg(array)-me(array))/σ(array);
	}
	
	public static long sk(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns SK
		return (avg(array)-me(array))/σ(array);
	}

	//标准差σ in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the σ in an array. </p>
	 * <p>标准差 σ =方差s^2的算术平方根</p>
	 * @param array an array, must not be null or empty
	 * @return the σ in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte σ(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return new Double(Math.sqrt(new Double(s(array)).doubleValue())).byteValue();
	}

	public static short σ(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return new Double(Math.sqrt(new Double(s(array)).doubleValue())).shortValue();
	}

	public static int σ(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return new Double(Math.sqrt(new Double(s(array)).doubleValue())).intValue();
	}

	public static double σ(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return Math.sqrt(s(array));
	}

	public static float σ(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return new Double(Math.sqrt(new Double(s(array)).doubleValue())).floatValue();
	}
	
	public static long σ(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns σ
		return new Double(Math.sqrt(new Double(s(array)).doubleValue())).longValue();
	}
	
	//方差s^2 in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the s^2 in an array. </p>
	 * <p>s^2=[(x1-x)^2 +(x2-x)^2 +......(xn-x)^2]/n 　　(x为平均数) 　　
	 * 例如：4,8,6,2，方差为5. 　</p>
	 * @param array an array, must not be null or empty
	 * @return the Mo in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte s(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		byte avg = avg(array);
		byte sum = 0;
		for (byte s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return new BigDecimal(sum).divide(new BigDecimal(array.length)).byteValue();
		//sum/array.length;
	}

	public static short s(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		short avg = avg(array);
		short sum = 0;
		for (short s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return new BigDecimal(sum).divide(new BigDecimal(array.length)).shortValue();
	}

	public static int s(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		int avg = avg(array);
		int sum = 0;
		for (int s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return sum/array.length;
	}

	public static double s(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		double avg = avg(array);
		double sum = 0;
		for (double s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return sum/array.length;
	}

	public static float s(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		float avg = avg(array);
		float sum = 0;
		for (float s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return sum/array.length;
	}
	
	public static long s(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns s^2
		long avg = avg(array);
		long sum = 0;
		for (float s : array) {
			sum+=(s-avg)*(s-avg);
		}
		return sum/array.length;
	}
	
	
	/*
	 * <p> Returns the Me in an array. </p>
	 * <p>
	 * 	 一般来说，一组数据中，出现次数最多的数就叫这组数据的众数。
	　　例如：1，2，3，3，4的众数是3。
	　　但是，如果有两个或两个以上个数出现次数都是最多的，那么这几个数都是这组数据的众数。
	　　例如：1，2，2，3，3，4 的众数是2和3。
	　　还有，如果所有数据出现的次数都一样，那么这组数据没有众数。
	　　例如：1，2，3，4，5没有众数。
	 * </p>
	 * @param array an array, must not be null or empty
	 * @return the Me in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte[] mo(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		byte[] r_mo = new byte[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new byte[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] = new Float(String.valueOf(object)).byteValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}

	public static short[] mo(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		short[] r_mo = new short[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new short[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] = new Float(String.valueOf(object)).shortValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}

	public static int[] mo(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		int[] r_mo = new int[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
				System.out.println("time:"+times[i]);
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new int[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] =  Integer.valueOf(String.valueOf(object)).intValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}

	public static double[] mo(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		double[] r_mo = new double[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new double[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] =  Double.valueOf(String.valueOf(object)).doubleValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}

	public static float[] mo(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		float[] r_mo = new float[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new float[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] =  Float.valueOf(String.valueOf(object)).floatValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}
	
	public static long[] mo(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		long[] r_mo = new long[0];
		if(array.length>1){
			int[] times = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				times[i] = 0;
				for (int j = 0; j < array.length; j++) {
					if(array[i]==array[j]){
						times[i]++;
					}
				}
			}
			//检测是否每个数字出现的次数都一样，即每个数字出现一次
			boolean eq = false;
			for (int i = 0; i < times.length; i++) {
				if(1 == times[i]){
					eq = true;
				}else{
					eq = false;
					break;
				}
			}
			//如果不是每个数字出现一次,就会存在众数
			if(!eq){
				int maxTime = max(times);
				Set tempList = new HashSet();
				for (int i = 0; i < array.length; i++) {
					if(times[i] == maxTime ){
						tempList.add(array[i]);
					}
				}
				r_mo = new long[tempList.size()];
				int maxIndex = 0;
				for (Object object : tempList) {
					r_mo[maxIndex] =  Long.valueOf(String.valueOf(object)).longValue();
					maxIndex++;
				}
			}
		}
		return r_mo;
	}
	
	
	//中位数 Me in array
	// --------------------------------------------------------------------

	/*
	 * <p> Returns the Me in an array. </p>
	 * <p>当一组数字的个数是偶数的时候，是一组数字顺序排列后中间2位的数字的平均值</p>
	 * <p>当一组数字的个数是奇数的时候，是一组数字顺序排列后中间1位的数字</p>
	 * @param array an array, must not be null or empty
	 * @return the Me in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte me(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Mo
		byte v_mo = 0;
		if(array.length%2==0){
			v_mo = new BigDecimal(array[array.length/2-1]).add(new BigDecimal(array[array.length/2])).divide(new BigDecimal(2)).byteValue();
		}else{
			v_mo = array[(array.length-1)/2];
		}
		return v_mo;
	}
	
	public static short me(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		short v_me = 0;
		if(array.length%2==0){
			v_me = new BigDecimal(array[array.length/2-1]).add(new BigDecimal(array[array.length/2])).divide(new BigDecimal(2)).shortValue();
		}else{
			v_me = array[(array.length-1)/2];
		}
		return v_me;
	}

	public static int me(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		int v_me = 0;
		if(array.length%2==0){
			v_me = (array[array.length/2-1]+array[array.length/2])/2;
		}else{
			v_me = array[(array.length-1)/2];
		}
		return v_me;
	}

	public static double me(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		double v_me = 0;
		if(array.length%2==0){
			v_me = (array[array.length/2-1]+array[array.length/2])/2;
		}else{
			v_me = array[(array.length-1)/2];
		}
		return v_me;
	}

	public static float me(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		float v_me = 0;
		if(array.length%2==0){
			v_me = (array[array.length/2-1]+array[array.length/2])/2;
		}else{
			v_me = array[(array.length-1)/2];
		}
		return v_me;
	}
	
	public static long me(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns Me
		long v_me = 0;
		if(array.length%2==0){
			v_me = (array[array.length/2-1]+array[array.length/2])/2;
		}else{
			v_me = array[(array.length-1)/2];
		}
		return v_me;
	}
	
	// average in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the avg in an array. </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the avg in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte avg(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns avg
		byte sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return new BigDecimal(sum).divide(new BigDecimal(array.length)).byteValue();
	}

	public static short avg(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns avg
		short sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return new BigDecimal(sum).divide(new BigDecimal(array.length)).shortValue();
	}

	public static int avg(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns avg
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return sum/array.length;
	}

	public static double avg(double[] array) {
		// Validates input
		exceptionCheck(array);
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return sum/array.length;
	}

	public static float avg(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns avg
		float sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return sum/array.length;
	}
	
	public static long avg(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns avg
		long sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum+=array[i];
		}
		return sum/array.length;
	}
	
	// count between x an y in array
	// --------------------------------------------------------------------
	public static int num(byte[] array, byte x, byte y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}

	public static int num(short[] array, short x, short y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}

	public static int num(int[] array, int x, int y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}

	public static int num(double[] array, double x, double y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}

	public static int num(float[] array, float x, float y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}
	
	public static int num(long[] array, long x, long y) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns count
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= x && array[i] <= y) {
				count++;
			}
		}
		return count;
	}

	// 极差(D=Xmax - Xmin) in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the 极差D in an array. </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the 极差D in an array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte d(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).byteValue();
	}

	public static short d(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).shortValue();
	}

	public static int d(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).intValue();
	}

	public static long d(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).longValue();
	}

	public static double d(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).doubleValue();
	}

	public static float d(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns 极差D
		return new Float(Math.abs(max(array) - min(array))).floatValue();
	}

	// Min in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the minimum value in an array. </p>
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte min(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		byte min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	public static short min(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		short min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	public static int min(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		int min = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] < min) {
				min = array[j];
			}
		}
		return min;
	}

	public static long min(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		long min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	public static double min(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Double.isNaN(array[i])) {
				return Double.NaN;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	public static float min(float[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns min
		float min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Float.isNaN(array[i])) {
				return Float.NaN;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	// Max in array
	// --------------------------------------------------------------------
	/*
	 * <p> Returns the maximum value in an array. </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static long max(long[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns max
		long max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	public static int max(int[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns max
		int max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}
		return max;
	}

	public static short max(short[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns max
		short max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	public static byte max(byte[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns max
		byte max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	public static double max(double[] array) {
		// Validates input
		exceptionCheck(array);
		// Finds and returns max
		double max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Double.isNaN(array[j])) {
				return Double.NaN;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	public static float max(float[] array) {
		// Validates input
		exceptionCheck(array);

		// Finds and returns max
		float max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Float.isNaN(array[j])) {
				return Float.NaN;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	// 3 param min
	// -----------------------------------------------------------------------
	/*
	 * <p> Gets the minimum of three <code>long</code> values. </p>
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static long min(long a, long b, long c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	public static int min(int a, int b, int c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	public static short min(short a, short b, short c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	public static byte min(byte a, byte b, byte c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}
	
	public static double min(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}

	public static float min(float a, float b, float c) {
		return Math.min(Math.min(a, b), c);
	}

	// 3 param max
	// -----------------------------------------------------------------------
	/*
	 * <p> Gets the maximum of three <code>long</code> values. </p>
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static long max(long a, long b, long c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	public static int max(int a, int b, int c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	public static short max(short a, short b, short c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	public static byte max(byte a, byte b, byte c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	public static double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

	public static float max(float a, float b, float c) {
		return Math.max(Math.max(a, b), c);
	}

	private static void exceptionCheck(byte[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	private static void exceptionCheck(short[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	private static void exceptionCheck(int[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	private static void exceptionCheck(double[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	private static void exceptionCheck(float[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	private static void exceptionCheck(long[] array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (array.length == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}
	
	public static void main(String[] args) {
		//System.out.println("pattern:" + open_interval_pattern_1.pattern());
		//System.out.println("pattern:" + open_interval_pattern_2.pattern());
		//System.out.println("pattern:" + open_interval_pattern_3.pattern());
		//System.out.println("pattern:" + closed_interval_pattern.pattern());
		//System.out.println("pattern:" + left_half_open_interval_pattern.pattern());
		//System.out.println("pattern:" + right_half_open_interval_pattern.pattern());
		System.out.println(MathUtils.contains("78","[15," + POSITIVE_INFINITE + ")"));
		System.out.println(new BigDecimal("-45").toPlainString());
	}
	
}
