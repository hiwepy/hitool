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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.io.FileUtils;
import hitool.core.lang3.CharsetUtils;

public abstract class ZipUtils extends CompressUtils {

	protected static final CharSequence EXT = ".zip";
	protected static Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

	public static byte[] toCompressedBytes(Collection<File> srcFiles, File destFile) throws IOException {
		if (srcFiles != null) {
			try (ByteArrayOutputStream outzip = toCompressedOutputStream(srcFiles, destFile);) {
				// 获取压缩后的结果
				return outzip.toByteArray();
			}
		}
		return null;
	}

	public static byte[] toCompressedBytes(File srcFiles, File destFile) throws IOException {
		if (srcFiles != null) {
			try (ByteArrayOutputStream outzip = toCompressedOutputStream(srcFiles, destFile);) {
				// 获取压缩后的结果
				return outzip.toByteArray();
			}
		}
		return null;
	}

	public static ByteArrayInputStream toCompressedInputStream(Collection<File> srcFile, File destFile)
			throws IOException {
		return new ByteArrayInputStream(toCompressedBytes(srcFile, destFile));
	}

	public static ByteArrayInputStream toCompressedInputStream(File srcFile, File destFile) throws IOException {
		return new ByteArrayInputStream(toCompressedBytes(srcFile, destFile));
	}

	public static ByteArrayOutputStream toCompressedOutputStream(Collection<File> srcFiles, File destFile)
			throws IOException {
		if (srcFiles != null) {
			try (ByteArrayOutputStream outzip = new ByteArrayOutputStream();
					ArchiveOutputStream archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP,
							outzip);) {

				ZipUtils.compressFiles(srcFiles, archOuts, File.separator);
				// 获取压缩后的结果
				return outzip;
			} catch (ArchiveException e) {
				throw new IOException(e);
			}
		}
		return null;
	}

	public static ByteArrayOutputStream toCompressedOutputStream(File srcFile, File destFile) throws IOException {
		if (srcFile != null) {
			ByteArrayOutputStream outzip = new ByteArrayOutputStream();
			try (ArchiveOutputStream archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);) {
				ZipUtils.compressFile(srcFile, archOuts, File.separator);
				// 获取压缩后的结果
				return outzip;
			} catch (ArchiveException e) {
				throw new IOException(e);
			}
		}
		return null;
	}

	/**
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
		return CharsetUtils.newStringUtf8(ZipUtils.compress(text.getBytes()));
	}

	/**
	 * 字节压缩
	 * 
	 * @param databytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] databytes) throws IOException {
		try (InputStream input = new ByteArrayInputStream(databytes);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ZipOutputStream zipOutput = new ZipOutputStream(output);) {

			zipOutput.putNextEntry(new ZipEntry("0"));
			IOUtils.copy(input, zipOutput);
			zipOutput.closeEntry();
			// 获取压缩后的结果
			return output.toByteArray();
		}
	}

	/** 用于单文件压缩 */
	public static void compress(File srcFile, File destFile) throws IOException {
		if (srcFile != null && srcFile.isFile()) {
			try (InputStream is = new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE);
					ZipArchiveOutputStream out = new ZipArchiveOutputStream(
							new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));) {
				out.putArchiveEntry(new ZipArchiveEntry(srcFile, srcFile.getName()));
				IOUtils.copy(is, out);
				out.closeArchiveEntry();
			}
		}
	}

	/**
	 * 压缩指定文件夹根目录下指定后缀类型的文件
	 */
	public static void compressDir(File directory, String[] extensions, OutputStream outzip) throws IOException {
		ZipUtils.compressDir(directory, extensions, true, outzip);
	}

	/**
	 * 压缩目录下指定文件后缀的文件
	 * 
	 * @param directory：目录
	 * @param extensions：后缀
	 * @param recursive：是否递归
	 * @param outzip：压缩流的输出流
	 * @throws IOException
	 */
	public static void compressDir(File directory, String[] extensions, boolean recursive, OutputStream outzip)
			throws IOException {
		if (directory != null && directory.isDirectory()) {
			/**
			 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
			 */
			try (ArchiveOutputStream ouput = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);) {
				Collection<File> inputFiles = FileUtils.listFiles(directory, extensions, recursive);
				ZipUtils.compressFiles(inputFiles, ouput, directory.getAbsolutePath());
			} catch (ArchiveException e) {
				throw new IOException(e);
			}
		}
	}

	/**
	 * 处理多文档模式的xls文件打包
	 * 
	 * @param tmpDir
	 * @param sheet_name
	 * @param tempFiles
	 * @param out
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressFiles(Collection<File> tempFiles, OutputStream outzip) throws IOException {
		ZipUtils.compressFiles(tempFiles, outzip, "");
	}

	public static void compressFiles(Collection<File> tempFiles, OutputStream outzip, String basePath)
			throws IOException {
		LOG.info("开始执行zip压缩...");
		try {
			/**
			 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
			 */
			ArchiveOutputStream archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);
			ZipUtils.compressFiles(tempFiles, archOuts, basePath);

			archOuts.close();
			LOG.info("zip压缩完成！");
			LOG.info("开始删除临时文件...");
			for (File file : tempFiles) {
				file.deleteOnExit();
			}
			LOG.info("临时文件删除完成！");
		} catch (ArchiveException e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ArchiveOutputStream
	 */
	private static void compressFiles(Collection<File> inputFiles, ArchiveOutputStream ouputStream, String basePath)
			throws IOException {
		for (File file : inputFiles) {
			compressFile(file, ouputStream, basePath);
		}
		ouputStream.flush();
		ouputStream.finish();
	}

	/**
	 * 根据输入的文件与输出流对文件进行ZIP打包
	 * 
	 * @param inputFile
	 * @param ouputStream
	 * @param basePath
	 * @throws IOException
	 */
	private static void compressFile(File inputFile, ArchiveOutputStream ouputStream, String basePath)
			throws IOException {
		try {
			if (inputFile.exists()) {
				/**
				 * 如果是目录的话这里是不采取操作的， 至于目录的打包正在研究中
				 */
				if (inputFile.isFile()) {
					if (ouputStream instanceof ZipArchiveOutputStream) {

						ZipArchiveOutputStream zipOut = (ZipArchiveOutputStream) ouputStream;
						ZipArchiveEntry zipEntry = new ZipArchiveEntry(inputFile,
								inputFile.getPath().replace(basePath, ""));
						zipOut.putArchiveEntry(zipEntry);
						zipOut.setUseZip64(Zip64Mode.AsNeeded);

						// 向压缩文件中输出数据

						byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
						try (FileInputStream input = new FileInputStream(inputFile);
								BufferedInputStream bufferInput = new BufferedInputStream(input,
										DEFAULT_BUFFER_SIZE);) {
							int nNumber;

							while ((nNumber = bufferInput.read(buffer)) != -1) {
								zipOut.write(buffer, 0, nNumber);
							}
							zipOut.closeArchiveEntry();
						}
					}
				} else {
					try {
						Collection<File> inputFiles = FileUtils.listFiles(inputFile, null, true);
						for (File file : inputFiles) {
							compressFile(file, ouputStream, basePath);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ZipException zipe) {
			LOG.error(inputFile.getName() + "不是一个ZIP文件！文件格式错误");
		}
	}

	public void compress(Collection<File> files, File destFile) throws IOException {
		compress(files, destFile, "UTF-8");
	}

	public void compress(Collection<File> files, File destFile, String encoding) throws IOException {
		if (files != null) {
			Map<String, File> map = new HashMap<String, File>();
			for (File f : files) {
				FileUtils.getFiles(f, null, map);
			}
			if (!map.isEmpty()) {
				ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(destFile);
				zaos.setEncoding(encoding);
				// 执行压缩
				compress(zaos, map);
			}
		}
	}

	public void compress(Collection<File> files, OutputStream out) throws IOException {
		compress(files, out, "UTF-8");
	}

	public void compress(Collection<File> files, OutputStream out, String encoding) throws IOException {
		if (files != null) {
			Map<String, File> map = new HashMap<String, File>();
			for (File f : files) {
				FileUtils.getFiles(f, null, map);
			}
			if (!map.isEmpty()) {
				try {
					ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(out);
					// 设置编码，支持中文
					zaos.setEncoding(encoding);
					// 执行压缩
					compress(zaos, map);
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private void compress(ArchiveOutputStream zaos, Map<String, File> map) throws IOException {
		for (Map.Entry<String, File> entry : map.entrySet()) {
			File file = entry.getValue();
			if (!file.exists()) {
				continue;
			}
			ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, entry.getKey());
			zaos.putArchiveEntry(zipArchiveEntry);
			// folder
			if (zipArchiveEntry.isDirectory()) {
				zaos.closeArchiveEntry();
				continue;
			}
			// file
			InputStream fis = new FileInputStream(file);
			IOUtils.copy(fis, zaos);
			fis.close();
			zaos.closeArchiveEntry();
		}
		zaos.finish();
		zaos.close();
	}

	/**
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
		return CharsetUtils.newStringUtf8(ZipUtils.decompress(text.getBytes()));
	}

	/**
	 * 字节解压缩
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

	public void decompress(File zipFile) throws IOException {
		decompress(zipFile, zipFile);
	}

	public static void decompress(File srcFile, File destDir) throws IOException {
		// 预处理存储目标目录
		destDir = FileUtils.getDestDir(destDir);
		try (ZipArchiveInputStream input = new ZipArchiveInputStream(
				new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE));) {

			ZipArchiveEntry entry = null;
			while ((entry = input.getNextZipEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				} else {
					try (OutputStream output = new BufferedOutputStream(
							new FileOutputStream(new File(destDir, entry.getName())), DEFAULT_BUFFER_SIZE);) {
						IOUtils.copy(input, output);
					}
				}
			}
		}
	}

	public void decompress(File srcFile, File destDir, String encoding) throws IOException {
		// 预处理存储目标目录
		destDir = FileUtils.getDestDir(destDir);
		// 加载zip文件对象
		try (ZipFile file = new ZipFile(srcFile.getAbsolutePath(), encoding);) {

			// 迭代zip文件明细
			Enumeration<ZipArchiveEntry> en = file.getEntries();
			ZipArchiveEntry ze;
			while (en.hasMoreElements()) {
				ze = en.nextElement();
				// 当前明细名称的文件名
				String zipname = ze.getName();
				// 后缀
				String extension = FilenameUtils.getExtension(zipname);
				if ("zip".equalsIgnoreCase(extension)) {
					decompressInnerZip(file, destDir, ze, encoding);
				} else if ("gz".equalsIgnoreCase(extension)) {
					decompressInnerZip(file, destDir, ze, encoding);
				} else {
					// 获得目标目录
					File folder = new File(destDir.getAbsolutePath(), zipname);
					// 创建完整路径
					if (ze.isDirectory()) {
						folder.setWritable(true);
						folder.setReadable(true);
						folder.mkdirs();
						continue;
					} else {
						File parent = folder.getParentFile();
						if (!parent.exists()) {
							parent.setWritable(true);
							parent.setReadable(true);
							parent.mkdirs();
						}
					}
					try (InputStream in = new ZipArchiveInputStream(file.getInputStream(ze), encoding, true);
							OutputStream os = new FileOutputStream(folder);) {
						IOUtils.copy(in, os);
					}
				}
			}
		}
	}

	private void decompressInnerZip(ZipFile file, File destDir, ZipArchiveEntry ze, String encoding)
			throws IOException {
		// 当前明细名称的文件名
		String zipname = ze.getName();
		// 获得目标目录
		File folder = new File(destDir.getAbsolutePath());
		String innerzip = StringUtils.removeEnd(zipname, ".zip");
		File innerfolder = new File(folder + File.separator + innerzip);
		if (!innerfolder.exists()) {
			innerfolder.setWritable(true);
			innerfolder.setReadable(true);
			innerfolder.mkdirs();
		}
		try (ZipArchiveInputStream in = new ZipArchiveInputStream(file.getInputStream(ze), encoding, true);) {
			ZipArchiveEntry innerzae = null;
			while ((innerzae = in.getNextZipEntry()) != null) {
				try (OutputStream fos = new FileOutputStream(
						folder + File.separator + innerzip + File.separator + innerzae.getName());) {
					IOUtils.copy(in, fos);
					fos.flush();
				}
			}
		}
	}

	public void decompress(String zipPath) throws IOException {
		decompress(zipPath, zipPath);
	}

	public void decompress(String zipPath, String destDirPath) throws IOException {
		decompress(zipPath, destDirPath, "Utf-8");
	}

	public void decompress(String zipPath, String destDirPath, String encoding) throws IOException {
		decompress(new File(zipPath), new File(destDirPath), encoding);
	}
}