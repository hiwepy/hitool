/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class SevenZUtils extends CompressUtils {

	protected static final CharSequence EXT = ".7z";

	/*
	 * 文件压缩
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void compress(File file) throws IOException {
		SevenZUtils.compress(file, true);
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
		SevenZUtils.compress(srcFile, destFile);
		if (delete) {
			srcFile.delete();
		}
	}

	public static void compress(File srcFile, File destFile) throws IOException {
		try (SevenZOutputFile output = new SevenZOutputFile(destFile);){
			output.setContentCompression(SevenZMethod.LZMA2);
			SevenZUtils.compress(srcFile, output);
			output.finish();
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
	private static void compress(File srcFile, SevenZOutputFile sevenZOutput) throws IOException {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, sevenZOutput);
		} else {
			compressFile(srcFile, sevenZOutput);
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
	private static void compressDir(File dir, SevenZOutputFile sevenZOutput) throws IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			// 递归归档
			compress(file, sevenZOutput);
		}
	}

	/*
	 * 文件归档
	 * 
	 * @param file：待归档文件
	 * @param taos
	 * @param dir
	 * @throws Exception
	 */
	private static void compressFile(File file, SevenZOutputFile sevenZOutput) throws IOException {
		try (InputStream input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);){
			SevenZArchiveEntry entry = new SevenZArchiveEntry();
			entry.setName(file.getName());
			sevenZOutput.putArchiveEntry(entry);
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			while (-1 != input.read(buffer)) {
				sevenZOutput.write(buffer);
			}
			sevenZOutput.closeArchiveEntry();
		}
	}

	/*
	 * 数据流压缩
	 * 
	 * @param input
	 * @param os
	 * @throws IOException
	 */
	public static void compress(InputStream is, OutputStream os) throws IOException {
		try (InputStream input = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
				GzipCompressorOutputStream output = new GzipCompressorOutputStream(new BufferedOutputStream(os, DEFAULT_BUFFER_SIZE));) {
			IOUtils.copy(input, output);
			output.finish();
			output.flush();
		}
	}

}
