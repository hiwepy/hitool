package hitool.lrc4j.tar.time;

/**
 * 
 * @className	： Lyric
 * @description	： 单句歌词，包含该句歌词显示的时间点，距下一句歌词的时间
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:37:20 PM
 */
public class Lyric implements Comparable<Lyric> {
	/**
	 * 歌词文本
	 */
	private String txt;
	/**
	 * 当前歌词的时间
	 */
	private long current;
	/**
	 * 距下一句歌词的时间
	 */
	private long timeSize;

	private String currentTimeStr;

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public long getTimeSize() {
		return timeSize;
	}

	public void setTimeSize(long timeSize) {
		this.timeSize = timeSize;
	}

	@Override
	public int compareTo(Lyric o) {
		if (this.current > o.current) {
			return 1;
		} else if (this.current == o.current) {
			return 0;
		} else {// 小于
			return -1;
		}
	}

	public String getCurrentTimeStr() {
		return currentTimeStr;
	}

	public void setCurrentTimeStr(String currentTimeStr) {
		this.currentTimeStr = currentTimeStr;
	}
}
