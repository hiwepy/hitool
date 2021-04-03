/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import hitool.core.lang3.CharsetUtils;

/*
 * .gz 文件压缩解压工具
 */
public abstract class GZipUtils extends CompressUtils {

	protected static final CharSequence EXT = ".gz";

	/*
	 * 压缩字符串
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static String compressString(String text) throws IOException {
		if (text == null || text.length() == 0) {
			return text;
		}
		return CharsetUtils.newStringUtf8(GZipUtils.compress(text.getBytes()));
	}

	/*
	 * 字节压缩
	 * 
	 * @param databytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] databytes) throws IOException {
		byte[] outBytes = null;
		try (InputStream input = new ByteArrayInputStream(databytes);
				ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			// 压缩
			GZipUtils.compress(input, output);
			// 获取压缩后的结果
			outBytes = output.toByteArray();
		}
		return outBytes;
	}

	/*
	 * 文件压缩
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void compress(File file) throws IOException {
		GZipUtils.compress(file, true);
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
		GZipUtils.compress(srcFile, destFile);
		if (delete) {
			srcFile.delete();
		}
	}

	public static void compress(File srcFile, File destFile) throws IOException {
		try (InputStream input = new FileInputStream(srcFile);
				OutputStream output = new FileOutputStream(destFile);) {
			// 压缩
			GZipUtils.compress(input, output);
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

	/*
	 * 文件压缩
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void compress(String filePath) throws IOException {
		GZipUtils.compress(filePath, true);
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
		GZipUtils.compress(new File(filePath), delete);
	}

	/*
	 * 解压缩字符串
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static String decompressString(String text) throws IOException {
		if (text == null || text.length() == 0) {
			return text;
		}
		return CharsetUtils.newStringUtf8(GZipUtils.decompress(text.getBytes()));
	}

	/*
	 * 字节解压缩
	 * 
	 * @param databytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] databytes) throws IOException {
		byte[] outBytes = null;
		try (InputStream input = new ByteArrayInputStream(databytes);
				ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			// 解压缩
			GZipUtils.decompress(input, output);
			// 获取解压后的数据
			outBytes = output.toByteArray();
		}
		return outBytes;
	}

	/*
	 * 文件解压缩
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void decompress(File file) throws IOException {
		GZipUtils.decompress(file, true);
	}

	/*
	 * 文件解压缩
	 * 
	 * @param srcFile
	 * @param delete  ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File srcFile, boolean delete) throws IOException {
		// 解压缩
		GZipUtils.decompress(srcFile, srcFile.getParentFile());
		if (delete) {
			srcFile.delete();
		}
	}
	
	public static void decompress(File srcFile, File destDir) throws IOException {
		File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.getName()) + EXT);
		try (InputStream input = new FileInputStream(srcFile);
				OutputStream output = new FileOutputStream(destFile);){
			GZipUtils.decompress(input, output);
		}
	}

	/*
	 * 数据流解压缩
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public static void decompress(InputStream in, OutputStream out) throws IOException {
		try (InputStream input = new GzipCompressorInputStream(new BufferedInputStream(in, DEFAULT_BUFFER_SIZE));
				OutputStream output = new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE);) {
			IOUtils.copy(input, output);
			output.flush();
		}
	}

	/*
	 * 文件解压缩
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void decompress(String filePath) throws Exception {
		GZipUtils.decompress(filePath, true);
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
		GZipUtils.decompress(new File(filePath), delete);
	}

}
