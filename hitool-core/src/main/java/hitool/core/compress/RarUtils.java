/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.compress;

import java.io.File;
import java.io.IOException;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

public class RarUtils extends CompressUtils {

	/**
	 * 根据原始rar路径，解压到指定文件夹下.
	 * 
	 * @param srcFile
	 *            原始rar路径
	 * @param destDir
	 *            解压到的文件夹
	 * @throws RarException
	 */
	public static void decompress(File srcFile, File destDir) throws IOException, RarException {
		Junrar.extract(srcFile, destDir);
	}

}
