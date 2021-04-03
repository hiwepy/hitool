package hitool.lrc4j.tar;

/*
 * 
 * @className ： Tar
 * @description ：标签
 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date ： Jan 25, 2016 2:35:28 PM
 */
public interface Tar {
	/*
	 * 
	 * @description ： 得标签值
	 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date ：Jan 25, 2016 2:35:33 PM
	 * @return
	 */
	public String getValue();

	/*
	 * 
	 * @description ： 设置标签值
	 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date ：Jan 25, 2016 2:35:40 PM
	 * @param value
	 */
	public void setValue(String value);

	/*
	 * 
	 * @description ： 得标签名
	 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date ：Jan 25, 2016 2:35:47 PM
	 * @return
	 */
	public abstract String getName();
}
