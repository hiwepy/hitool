package hitool.lrc4j.lrc;

import java.util.List;

import hitool.lrc4j.tar.time.Lyric;

/*
 * 
 * @className	： Lyrics
 * @description	： 基于播放，面相播放的【对LRC做进一步处理，便于播放显示】
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:42:07 PM
 */
public class Lyrics {
	private String al;
	private String ar;
	private String by;
	private int offset;//其单位是毫秒
	private String ti;
	private List<Lyric> lyrics;
	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public String getAr() {
		return ar;
	}
	public void setAr(String ar) {
		this.ar = ar;
	}
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getTi() {
		return ti;
	}
	public void setTi(String ti) {
		this.ti = ti;
	}
	public List<Lyric> getLyrics() {
		return lyrics;
	}
	public void setLyrics(List<Lyric> lyrics) {
		this.lyrics = lyrics;
	}
}
