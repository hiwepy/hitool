package hitool.lrc4j.tar;

/*
 * 
 * @className	： AbstractTar
 * @description	：标签 <p> 其格式为"[标识名:值]"。大小写等价 </p>
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Jan 25, 2016 2:35:05 PM
 */
public abstract class AbstractTar implements Tar {
	
	private String value;

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public abstract String getName();

}
