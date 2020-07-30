package hitool.core.web.multipart.filepart.comparator;

import java.util.Comparator;
import java.util.Hashtable;

public class SizeComparator implements Comparator<Hashtable<String, Object>> {
	
	public int compare(Hashtable<String, Object> hashA,Hashtable<String, Object> hashB) {
		if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
			return 1;
		} else {
			if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
				return 1;
			} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}