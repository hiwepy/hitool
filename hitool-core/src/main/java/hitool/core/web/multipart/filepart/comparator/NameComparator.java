package hitool.core.web.multipart.filepart.comparator;

import java.util.Comparator;
import java.util.Hashtable;
/**
 * 按名称排序
 * *******************************************************************
 */
public class NameComparator implements Comparator<Hashtable<String, Object>> {
	
	public int compare(Hashtable<String, Object> hashA,Hashtable<String, Object> hashB) {
		if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
			return 1;
		} else {
			return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
		}
	}
}