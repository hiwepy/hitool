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

import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/*
 * Apache commons compress AR打包
 */
public abstract class ArUtils extends CompressUtils {

	protected static final CharSequence EXT = ".bz2";

	/*
	 * 文件压缩
	 * 
	 * @param file
	 * @param delete
	 *            是否删除原始文件
	 * @throws IOException
	 */
	public static void compress(File file) throws IOException {
		ArUtils.compress(file, true);
	}

	/*
	 * 文件压缩
	 * 
	 * @param srcFile
	 * @param delete：是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(File srcFile, boolean delete) throws IOException {
		File destFile = new File(srcFile.getParentFile(), FilenameUtils.getBaseName(srcFile.getName()) + EXT);
		// 压缩
		ArUtils.compress(srcFile, destFile);
		if (delete) {
			srcFile.delete();
		}
	}

	public static void compress(File srcFile, File destFile) throws IOException {
		// 压缩
		ArUtils.compress(srcFile, destFile, BASE_DIR);
	}

	public static void compress(File srcFile, File destFile, String basePath) throws IOException {
		try (ArArchiveOutputStream output = new ArArchiveOutputStream(
				new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));) {
			// 压缩
			ArUtils.compress(srcFile, destFile, basePath);
		}
	}

	public static void compress(File[] srcFiles, File destFile) throws IOException {
		// 压缩
		ArUtils.compress(srcFiles, destFile, BASE_DIR);
	}

	public static void compress(File[] srcFiles, File destFile, String basePath) throws IOException {
		try (ArArchiveOutputStream output = new ArArchiveOutputStream(
				new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));) {
			for (File srcFile : srcFiles) {
				ArUtils.compress(srcFile, output, basePath);
			}
		}
	}

	/*
	 * 文件压缩
	 * 
	 * @param srcFile
	 *            源路径
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 *            归档包内相对路径
	 * @throws Exception
	 */
	private static void compress(File srcFile, ArArchiveOutputStream taos, String basePath) throws IOException {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, taos, basePath);
		} else {
			compressFile(srcFile, taos, basePath);
		}
	}

	/*
	 * 目录 归档
	 * 
	 * @param dir
	 * @param taos
	 * @param basePath
	 * @throws Exception
	 */
	private static void compressDir(File dir, ArArchiveOutputStream taos, String basePath) throws IOException {
		File[] files = dir.listFiles();
		if (files.length < 1) {
			ArArchiveEntry entry = new ArArchiveEntry(basePath + dir.getName() + File.separator, dir.length());
			taos.putArchiveEntry(entry);
			taos.closeArchiveEntry();
		}
		for (File file : files) {
			// 递归归档
			compress(file, taos, basePath + dir.getName() + File.separator);
		}
	}

	/*
	 * 文件归档
	 * 
	 * @param file：待归档文件
	 * @param output
	 * @param dir
	 * @throws Exception
	 */
	private static void compressFile(File file, ArArchiveOutputStream output, String basePath) throws IOException {
		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);) {
			ArArchiveEntry entry = new ArArchiveEntry(basePath + file.getName(), file.length());
			output.putArchiveEntry(entry);
			IOUtils.copy(input, output);
			output.closeArchiveEntry();
		}
	}

	/*
	 * 文件压缩
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void compress(String filePath) throws IOException {
		ArUtils.compress(filePath, true);
	}

	/*
	 * 文件压缩
	 * 
	 * @param filePath
	 * @param delete
	 *            ：是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(String filePath, boolean delete) throws IOException {
		ArUtils.compress(new File(filePath), delete);
	}

	/*
	 * 文件解压缩
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void decompress(File srcFile) throws IOException {
		ArUtils.decompress(srcFile, true);
	}

	/*
	 * 文件解压缩
	 * 
	 * @param srcFile
	 * @param delete
	 *            ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File srcFile, boolean delete) throws IOException {
		// 解压缩
		ArUtils.decompress(srcFile, srcFile.getParentFile());
		if (delete) {
			srcFile.delete();
		}
	}

	public static void decompress(File srcFile, File destDir) throws IOException {
		try(ArArchiveInputStream input = new ArArchiveInputStream(
				new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE));) {
			// 解压
			ArUtils.decompress(input, destDir);
		}
	}

	@SuppressWarnings("deprecation")
	public static void decompress(ArArchiveInputStream input, File destDir) throws IOException {
		try {
			ArArchiveEntry entry = null;
			while ((entry = input.getNextArEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				} else {
					try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())),
							DEFAULT_BUFFER_SIZE);){
						byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
						
						int len;
						long size = entry.getSize();
						while (size > 0) {
							if (size < DEFAULT_BUFFER_SIZE) {
								len = input.read(buffer, 0, (int) size);
								size -= len;
							} else {
								len = input.read(buffer);
								size -= len;
							}
							bos.write(buffer, 0, len);
						}
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/*
	 * 文件解压缩
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void decompress(String filePath) throws Exception {
		ArUtils.decompress(filePath, true);
	}

	/*
	 * 文件解压缩
	 * 
	 * @param filePath
	 * @param delete
	 *            ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(String filePath, boolean delete) throws IOException {
		ArUtils.decompress(new File(filePath), delete);
	}

}
