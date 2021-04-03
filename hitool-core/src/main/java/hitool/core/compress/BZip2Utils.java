/** 
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

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import hitool.core.lang3.CharsetUtils;

/*
 * .bz2文件压缩解压工具
 */
public abstract class BZip2Utils extends CompressUtils {

	protected static final CharSequence EXT = ".bz2";

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
		return CharsetUtils.newStringUtf8(BZip2Utils.compress(text.getBytes()));
	}

	/*
	 * 压缩字节码
	 * 
	 * @param databytes
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] databytes) throws IOException {
		byte[] outBytes = null;
		try (InputStream input = new ByteArrayInputStream(databytes);
				ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			// 压缩
			BZip2Utils.compress(input, output);
			// 获取压缩后的结果
			outBytes = output.toByteArray();
		}
		return outBytes;
	}

	/*
	 * 文件压缩
	 * 
	 * @param file
	 * @param delete
	 *            是否删除原始文件
	 * @throws IOException
	 */
	public static void compress(File file) throws IOException {
		BZip2Utils.compress(file, true);
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
		BZip2Utils.compress(srcFile, destFile);
		if (delete) {
			srcFile.delete();
		}
	}

	public static void compress(File srcFile, File destFile) throws IOException {
		try (InputStream input = new FileInputStream(srcFile);
				OutputStream output = new FileOutputStream(destFile);) {
			// 压缩
			BZip2Utils.compress(input, output);
		}
	}

	public static void compress(InputStream in, OutputStream out) throws IOException {
		try (InputStream input = new BufferedInputStream(in, DEFAULT_BUFFER_SIZE);
				BZip2CompressorOutputStream output = new BZip2CompressorOutputStream(new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE));) {
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
		BZip2Utils.compress(filePath, true);
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
		BZip2Utils.compress(new File(filePath), delete);
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
			BZip2Utils.decompress(input, output);
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
		BZip2Utils.decompress(file, true);
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
		BZip2Utils.decompress(srcFile, srcFile.getParentFile());
		if (delete) {
			srcFile.delete();
		}
	}

	public static void decompress(File srcFile, File destDir) throws IOException {
		File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.getName()) + EXT);
		try(InputStream input = new FileInputStream(srcFile);
				OutputStream output = new FileOutputStream(destFile);) {
			BZip2Utils.decompress(input, output);
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
		try (InputStream input = new BZip2CompressorInputStream(new BufferedInputStream(in, DEFAULT_BUFFER_SIZE));
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
		BZip2Utils.decompress(filePath, true);
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
		BZip2Utils.decompress(new File(filePath), delete);
	}

}
