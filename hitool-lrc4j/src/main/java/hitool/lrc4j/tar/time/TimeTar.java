package hitool.lrc4j.tar.time;

import hitool.lrc4j.tar.AbstractTar;

/*
 * 
 * @className	： TimeTar
 * @description	：时间标签 <p>形式为"[mm:ss]"或"[mm:ss.ff]"（分钟数:秒数）。数字须为非负整数，
 * 				 比如"[12:34.5]"是有效的，而"[0x0C:-34.5]"无效。　它可以位于某行歌词中的任意位置。
 * 				一行歌词可以包含多个时间标签（比如歌词中的迭句部分）。根据这些时间标签，用户端程序会按顺序依次高亮显示歌词，从而实现卡拉OK功能。另外，标签无须排序。</p>
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:36:22 PM
 */
public class TimeTar extends AbstractTar {
	private final String name = "time";
	/*
	 * 标签后面的歌词，即当前时刻到下一时刻之前该显示的歌词
	 */
	private String text;

	@Override
	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
