package hitool.lrc4j.utils;

import java.util.List;

import hitool.lrc4j.tar.time.Lyric;

public class TimeUtils {

	/**
	 * xx:xx xx:xx.xx格式的时间
	 * 
	 * @return
	 */
	public static long timeToMillis(String timeValue) {
		long time = 0;
		int min;
		int sec;
		int millis = 0;
		String temp[] = timeValue.split(":");
		min = Integer.parseInt(temp[0]);
		if (timeValue.contains(".")) {
			String temp2[] = temp[1].split("\\.");
			sec = Integer.parseInt(temp2[0]);
			millis = Integer.parseInt(temp2[1]) * 10;
		} else {
			sec = Integer.parseInt(temp[1]);
		}
		time = min * 60 * 1000 + sec * 1000 + millis;
		return time;
	}

	public static void addTimeSize(List<Lyric> ls) {
		long prev = 0;
		for (int i = 0; i < ls.size(); i++) {
			if (i > 0) {
				ls.get(i - 1).setTimeSize(ls.get(i).getCurrent() - prev);
			}
			prev = ls.get(i).getCurrent();
		}
	}

	public static void main(String[] args) {
		System.out.println(timeToMillis("01:01"));
	}
}
