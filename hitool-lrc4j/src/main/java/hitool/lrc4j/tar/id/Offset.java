package hitool.lrc4j.tar.id;

import hitool.lrc4j.tar.AbstractTar;
/**
 * 
 * @className	： Offset
 * @description	： 时间补偿 <p> 其单位是毫秒，正值表示整体提前，负值相反。这是用于总体调整显示快慢的。</p>
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:40:30 PM
 */
public class Offset extends AbstractTar {
	private final String name = "offset";

	@Override
	public String getName() {
		return name;
	}
}
