/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Apache commons compress CPIO打包
 */
public abstract class CpioUtils extends CompressUtils {

	public static void doCompress(File srcFile, File destFile) throws IOException {
		CpioArchiveOutputStream out = null;
		InputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE);
			out = new CpioArchiveOutputStream(
					new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));
			out.putArchiveEntry(new CpioArchiveEntry(srcFile, srcFile.getName()));
			IOUtils.copy(input, out);
			out.closeArchiveEntry();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
		}
	}

	public static void doDecompress(File srcFile, File destDir) throws IOException {
		CpioArchiveInputStream is = null;
		try {
			is = new CpioArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE));
			CpioArchiveEntry entry = null;
			while ((entry = is.getNextCPIOEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				} else {
					BufferedOutputStream bos = null;
					try {
						byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
						bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())),
								DEFAULT_BUFFER_SIZE);
						int len;
						long size = entry.getSize();
						while (size > 0) {
							if (size < DEFAULT_BUFFER_SIZE) {
								len = is.read(buffer, 0, (int) size);
								size -= len;
							} else {
								len = is.read(buffer);
								size -= len;
							}
							bos.write(buffer, 0, len);
						}
					} finally {
						IOUtils.closeQuietly(bos);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
