package hitool.lrc4j.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hitool.lrc4j.lrc.LRC;
import hitool.lrc4j.lrc.Lyrics;
import hitool.lrc4j.tar.time.Lyric;
import hitool.lrc4j.tar.time.TimeTar;

public class LRCParseUtils {
	
	public static Lyrics parseLRC(LRC lrc) {
		Lyrics lyrics = new Lyrics();
		lyrics.setAl(lrc.getAl().getValue());
		lyrics.setAr(lrc.getAr().getValue());
		lyrics.setBy(lrc.getBy().getValue());
		try {
			lyrics.setOffset(Integer.parseInt(lrc.getOffset().getValue()));
		} catch (NumberFormatException e) {
			
		}
		lyrics.setTi(lrc.getTi().getValue());
		lyrics.setLyrics(timeTarToLyric(lrc.getTimeTars()));
		return lyrics;
	}

	static List<Lyric> timeTarToLyric(List<TimeTar> timeTars) {
		List<Lyric> ls = new ArrayList<Lyric>();
		for (TimeTar t : timeTars) {
			long time = TimeUtils.timeToMillis(t.getValue().substring(1, t.getValue().length() - 1));
			Lyric l = new Lyric();
			l.setCurrent(time);
			l.setTxt(t.getText());
			l.setCurrentTimeStr(t.getValue().substring(1, t.getValue().length() - 1));
			ls.add(l);
		}
		Collections.sort(ls);
		TimeUtils.addTimeSize(ls);
		return ls;
	}
}
