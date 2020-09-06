package hitool.web.multipart.filepart.comparator;

import java.util.Comparator;
import java.util.Hashtable;

/**
 * 类型比对
 */
public class TypeComparator implements Comparator<Hashtable<String, Object>> {
	
	public int compare(Hashtable<String, Object> hashA,Hashtable<String, Object> hashB) {
		if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
			return 1;
		} else {
			return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
		}
	}
}
