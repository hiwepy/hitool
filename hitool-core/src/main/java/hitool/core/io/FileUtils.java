package hitool.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.Assert;

/**
 *   文件操作工具
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	protected static String fileSeparator = System
			.getProperty("file.separator");

	public static final int BUFFER_SIZE = 4096;

	// ---------------------------------------------------------------------
	// read toByteArray
	// ---------------------------------------------------------------------

	/**
	 * Copy the contents of the given input File into a new byte array.
	 * 将给定的文件类读出成byte数组格式并返回
	 * 
	 * @param in
	 *            the file to copy from <br/>
	 *            将被赢取的文件
	 * @return the new byte array that has been copied to <br/>
	 *         给定的文件的内容的byte格式
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 */
	public static byte[] toByteArray(File in) throws IOException {
		Assert.notNull(in, "No input File specified");
		InputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] bytes = null;
		try {
			input = new BufferedInputStream(new FileInputStream(in));
			output = new ByteArrayOutputStream();
			IOUtils.copy(input, output);
			bytes = output.toByteArray();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return bytes;
	}

	public static byte[] toByteArray(String destDir, String fileName)
			throws IOException {
		Assert.notNull(destDir, "No destDir specified");
		Assert.notNull(fileName, "No fileName specified");
		File file = new File(destDir + fileSeparator + fileName);
		return toByteArray(file);
	}

	public static byte[] toByteArray(String filePath) throws IOException {
		Assert.notNull(filePath, "No filePath specified");
		File file = FileUtils.getFile(filePath);
		return toByteArray(file);
	}

	public static void toByteArray(String filePath, byte[] bytes)
			throws IOException {
		Assert.notNull(filePath, "No filePath specified");
		Assert.notNull(bytes, "No byte Array specified");
		File file = FileUtils.getFile(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		RandomAccessFile fileIn = null;
		try {
			fileIn = new RandomAccessFile(file, "rwd");
			fileIn.write(bytes);
		} finally {
			IOUtils.closeQuietly(fileIn);
		}
	}

	// ---------------------------------------------------------------------
	// Write methods for java.io.File
	// ---------------------------------------------------------------------

	/**
	 * 输出到文件
	 * 
	 * @param str
	 * @param outPath
	 * @throws IOException
	 */
	public static void writeToFile(String text, String outPath)
			throws IOException {
		PrintWriter out1 = null;
		BufferedReader in4 = null;
		try {
			in4 = new BufferedReader(new StringReader(text));
			out1 = new PrintWriter(new BufferedWriter(new FileWriter(outPath)));
			while ((text = in4.readLine()) != null) {
				out1.println(text);
			}

		} catch (EOFException ex) {
			LOG.error("End of stream", ex.getCause());
		} finally {
			IOUtils.closeQuietly(out1);
			IOUtils.closeQuietly(in4);
		}
	}

	// ---------------------------------------------------------------------
	// Delete methods for java.io.File
	// ---------------------------------------------------------------------

	/**
	 * Delete the supplied {@link java.io.File} - for directories, recursively
	 * delete any nested directories or files as well. <br/>
	 * 删除给定的file文件,如果是文件夹,递归的删除内部的文件夹或文件
	 * 
	 * @param root
	 *            the root <code>File</code> to delete <br/>
	 *            被指定的要删除的文件夹或文件
	 * @return <code>true</code> if the <code>File</code> was deleted, otherwise
	 *         <code>false</code> <br/>
	 *         如果文件删除了,返回true,否则false
	 */
	public static boolean deleteRecursively(File root) {
		if (root != null && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (children != null) {
					for (File child : children) {
						deleteRecursively(child);
					}
				}
			}
			return root.delete();
		}
		return false;
	}

	public static File createDir(String destDir) throws IOException {
		File dir = FileUtils.getFile(destDir);
		if (dir.exists()) {
			LOG.error("could not create directory path for ["
					+ dir.getAbsolutePath() + "], dir is exists ");
		}
		if (!destDir.endsWith(File.separator)) {
			destDir = destDir + File.separator;
		}
		// 创建单个目录
		createDir(dir);
		return dir;
	}

	public static File createDir(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				LOG.error("could not create directory path for ["
						+ dir.getAbsolutePath() + "]");
			}
		}
		return dir;
	}

	public static File createFile(File root, String name) throws IOException {
		File file = new File(root, name);
		file.createNewFile();
		return file;
	}

	public static File createFile(File root) throws IOException {
		root.deleteOnExit();
		root.createNewFile();
		return root;
	}

	public static File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		file.deleteOnExit();
		file.createNewFile();
		return file;
	}

	public final static void clearDir(File file) throws IOException {
		cleanDirectory(file);
	}

	/**
	 *   删除指定路径的文件
	 * @param path
	 */
	public static void delete(String path) {
		File file = new File(path);
		if (file.exists()) {
			delete(file);
		} else {
			System.err.println("could not found file ["
					+ file.getAbsolutePath() + "]");
		}
	}

	/**
	 *  删除指定的文件
	 * @param file
	 */
	public static void delete(File file) {
		if (!file.delete()) {
			System.err.println("could not delete file ["
					+ file.getAbsolutePath() + "]");
		}
	}

	public static String getWebPath(File path, String split) {
		split = split.replaceAll("\\\\", "");
		split = split.replaceAll("/", "");
		String filePath = path.getAbsolutePath();
		int strIndex = filePath.indexOf(fileSeparator + split + fileSeparator);
		if (strIndex != -1) {
			return filePath.substring(strIndex).replaceAll("\\\\", "/");
		} else {
			return filePath;
		}
	}

	/**
	 * extract the local path information from a URL in a form suitable for
	 * contructing a File
	 * 
	 * @return a string with the '/' replacd by local separators
	 */
	public static String getLocalPath(String path) {
		String ret = path;
		if (File.separatorChar != '/' && path.indexOf("/") >= 0) {
			StringBuffer buf = new StringBuffer(path.length());
			CharacterIterator it = new StringCharacterIterator(path);
			for (char c = it.first(); c != CharacterIterator.DONE; c = it
					.next()) {
				if (c == '/') {
					c = File.separatorChar;
				}
				buf.append(c);
			}
			ret = buf.toString();
		}

		return ret;
	}

	public static File getFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			try {
				// file = new
				// File(ResourceUtils.getResourceAsURL(path).getPath());
				file = new File(ResourceUtils.getRelativeResourceAsURL(path).toURI());
			} catch (URISyntaxException e) {
				throw new FileNotFoundException(
						"the Path refers to the file does not exist or is not a file !");
			}
		}
		return file;
	}

	public static File getFile(URI uri) throws IOException {
		File file = new File(uri);
		if (!file.exists()) {
			throw new FileNotFoundException(
					"the uri refers to the file does not exist or is not a file !");
		}
		return file;
	}

	public static List<File> getFiles(String path) throws IOException {
		List<File> l = new ArrayList<File>();
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory() && file.list().length > 0) {
				for (File f : file.listFiles()) {
					if (f.isFile()) {
						l.add(f);
					}
				}
			} else if (file.isFile()) {
				l.add(file);
			}
			return l;
		} else {
			throw new FileNotFoundException(
					"the Path refers to the file does not exist !");
		}
	}

	public static void getFiles(File f, String parent, Map<String, File> map) {
		String name = f.getName();
		if (parent != null) {
			name = parent + File.separator + name;// 设置在zip包里的相对路径
		}
		if (f.isFile()) {
			map.put(name, f);
		} else if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				getFiles(file, name, map);
			}
		}
	}

	public static File getDestDir(File destDir) throws IOException {
		// 判断目标目录是否不存在
		if (!destDir.exists()) {
			// 不存在，则创建目录
			destDir.setWritable(true);
			destDir.setReadable(true);
			destDir.mkdirs();
		} else {
			// 存在，则判断是否不是目录
			if (!destDir.isDirectory()) {
				// 不是目录则获得父级目录。判断是不存在
				if (!destDir.getParentFile().exists()) {
					// 不存在，则创建父级目录
					FileUtils.forceMkdir(destDir.getParentFile());
				}
				// 父级目录,作为目标目录
				destDir = destDir.getParentFile();
				destDir.setWritable(true);
				destDir.setReadable(true);
			}
		}
		return destDir;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 *  文件探针 ；当父目录不存在时，创建目录！
	 * @param dirFile
	 * @throws IOException
	 */
	public static void forceProber(File dirFile) throws IOException {
		File directory = dirFile.getParentFile();
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				directory.setWritable(true);
				directory.setReadable(true);
				// Double-check that some other thread or process hasn't made
				// the directory in the background
				if (!directory.isDirectory()) {
					String message = "Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	public static File rename(File sourceFile) {
		if (sourceFile.exists()) {
			String filePath = sourceFile.getAbsolutePath();
			String newFilePath = FilenameUtils.getFullPath(filePath)
					+ FileUtils.getUUID() + "."
					+ FilenameUtils.getExtension(filePath);
			File newFile = new File(newFilePath);
			if (sourceFile.renameTo(newFile)) {
				return newFile;
			} else {
				return sourceFile;
			}
		}
		return sourceFile;
	}

	public static boolean isExists(String path) throws FileNotFoundException {
		File f = new File(path);
		if (!f.exists()) {
			LOG.info(" the file " + path + " does not exist ");
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		File sourceFile = new File(
				"D:\\DeveloperTomcat6\\work\\Catalina/localhost/WebOA/org/apache/jsp/editor/jsp/upload_jsp.class");
		System.out.println(FileUtils.getWebPath(sourceFile, "localhost"));

		/*
		 * String infilePath = "E:\\项目\\workplace.rar"; String outfilePath2 =
		 * "E:\\workplace2.rar"; String outfilePath3 = "E:\\workplace3.rar";
		 * long a = System.currentTimeMillis(); copyIO(infilePath,
		 * outfilePath2); long b = System.currentTimeMillis();
		 * System.out.println(b - a); copyNIO(infilePath, outfilePath3); long c
		 * = System.currentTimeMillis(); System.out.println(c - b);
		 */
		// writeToFile("dsgsdfgsdfg的分公司的后果","F:\\TestIO.txt");
		// System.out.println(readerByline("cityData/cityData.txt"));
		// System.out.println(systemInPath());
		// System.out.println(readerStr("asdasfds"));
		// System.out.println(readerBytes("dfsdf ".getBytes()) );
		/*
		 * try { BufferedInputStream buff = new
		 * BufferedInputStream(ClassLoaderUtil
		 * .getStream("cityData/cityData.txt")); byte[] bytes = new byte[1024];
		 * while (buff.read(bytes) != -1) {
		 * 
		 * } ByteBuffer ss = ByteBuffer.wrap(bytes);
		 * System.out.println(ss.toString()); } catch (MalformedURLException e)
		 * { e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
		 * }
		 */

	}

}
