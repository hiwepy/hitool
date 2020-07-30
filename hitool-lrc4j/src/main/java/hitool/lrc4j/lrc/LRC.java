package hitool.lrc4j.lrc;

import java.util.List;

import hitool.lrc4j.tar.id.Al;
import hitool.lrc4j.tar.id.Ar;
import hitool.lrc4j.tar.id.By;
import hitool.lrc4j.tar.id.Offset;
import hitool.lrc4j.tar.id.Ti;
import hitool.lrc4j.tar.time.TimeTar;

/**
 * 
 * @className	： LRC
 * @description	： 歌词对象，基于标签，面相底层的
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:41:49 PM
 */
public class LRC {
	
	private Al al;
	private Ar ar;
	private By by;
	private Offset offset;
	private Ti ti;
	private List<TimeTar> timeTars;

	public Al getAl() {
		return al;
	}

	public void setAl(Al al) {
		this.al = al;
	}

	public Ar getAr() {
		return ar;
	}

	public void setAr(Ar ar) {
		this.ar = ar;
	}

	public By getBy() {
		return by;
	}

	public void setBy(By by) {
		this.by = by;
	}

	public Offset getOffset() {
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	public Ti getTi() {
		return ti;
	}

	public void setTi(Ti ti) {
		this.ti = ti;
	}

	public List<TimeTar> getTimeTars() {
		return timeTars;
	}

	public void setTimeTars(List<TimeTar> timeTars) {
		this.timeTars = timeTars;
	}
}
