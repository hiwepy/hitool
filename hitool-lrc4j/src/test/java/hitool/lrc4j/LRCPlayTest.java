package hitool.lrc4j;

import hitool.lrc4j.lrc.LRC;
import hitool.lrc4j.lrc.Lyrics;
import hitool.lrc4j.tar.time.Lyric;
import hitool.lrc4j.utils.LRCParseUtils;
import hitool.lrc4j.utils.LRCReadUtils;

public class LRCPlayTest {
	
	public static void main(String[] args) throws InterruptedException {
		LRC lrc = LRCReadUtils.readLRC("张碧晨-一吻之间 (电视剧《青年医生》插曲).lrc");
		Lyrics ls = LRCParseUtils.parseLRC(lrc);
		playTest(ls);
	}

	static void playTest(Lyrics ls) throws InterruptedException {
		System.out.println("艺术家：" + ls.getAr());
		System.out.println("专辑：" + ls.getAl());
		System.out.println("歌曲：" + ls.getTi());
		System.out.println("歌词制作：" + ls.getBy());
		// Thread.sleep(ls.getOffset());//时间补偿暂未处理
		for (Lyric l : ls.getLyrics()) {
			System.out.println("时间:" + l.getCurrentTimeStr() + "    " + "lrc:" + l.getTxt());
			// System.out.println(l.getTxt());
			Thread.sleep(l.getTimeSize());
		}
	}
}
