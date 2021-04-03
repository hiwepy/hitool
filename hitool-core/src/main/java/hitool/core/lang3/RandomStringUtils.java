/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.util.Random;

public abstract class RandomStringUtils extends org.apache.commons.lang3.RandomStringUtils {

	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/*
	 * 生成定长的随机纯小写字母字符串(只包含小写字母、数字)
	 */
	public static String randomLower(int pwd_len) {
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(LETTERCHAR.length())); // 生成的数最大为36-1
			if (i >= 0 && i < LETTERCHAR.length()) {
				pwd.append(LETTERCHAR.charAt(i));
				count++;
			}
		}
		return pwd.toString();
	}

	/*
	 * 生成定长的随机纯大写字母字符串(只包含大写字母、数字)
	 */
	public static String randomUpper(int length) {
		return randomLower(length).toUpperCase();
	}

	/*
	 * 生成定长的随机字符串(可能包含大小写字母、数字)
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String randomMix(int pwd_len) {
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(ALLCHAR.length())); // 生成的数最大为36-1
			if (i >= 0 && i < ALLCHAR.length()) {
				pwd.append(ALLCHAR.charAt(i));
				count++;
			}
		}
		return pwd.toString();
	}

	/*
	 * 这是典型的随机洗牌算法。 流程是从备选数组中选择一个放入目标数组中，将选取的数组从备选数组移除（放至最后，并缩小选择区域）
	 * 算法时间复杂度O（n） @return 随机8为不重复数组
	 */
	public static String randomNum(int pwd_len) {
		String no = "";
		// 初始化备选数组
		int[] defaultNums = new int[10];
		for (int i = 0; i < defaultNums.length; i++) {
			defaultNums[i] = i;
		}
		Random random = new Random();
		int[] nums = new int[pwd_len];
		// 默认数组中可以选择的部分长度
		int canBeUsed = 10;
		// 填充目标数组
		for (int i = 0; i < nums.length; i++) {
			// 将随机选取的数字存入目标数组
			int index = random.nextInt(canBeUsed);
			nums[i] = defaultNums[index];
			// 将已用过的数字扔到备选数组最后，并减小可选区域
			swap(index, canBeUsed - 1, defaultNums);
			canBeUsed--;
		}
		if (nums.length > 0) {
			for (int i = 0; i < nums.length; i++) {
				no += nums[i];
			}
		}
		return no;
	}

	/*
	 * 交换方法
	 * 
	 * @param i
	 *            交换位置
	 * @param j
	 *            互换的位置
	 * @param nums
	 *            数组
	 */
	private static void swap(int i, int j, int[] nums) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			// System.out.println(RadomUtils.genUpperRandomStr(6));
			// System.out.println(RadomUtils.genLowerRandomStr(6));
			// System.out.println(RadomUtils.genMixRandomStr(6));
			System.out.println(RandomStringUtils.randomNum(6));
		}

	}

}
