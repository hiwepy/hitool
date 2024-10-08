package hitool.core.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.lang3.Assert;

/*
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

	public static byte[] toByteArray(File in) throws IOException {
		Assert.notNull(in, "No input File specified");
		try (InputStream input = new BufferedInputStream(new FileInputStream(in));) {
			return IOUtils.toByteArray(input);
		}
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
		try (RandomAccessFile fileIn = new RandomAccessFile(file, "rwd");){
			fileIn.write(bytes);
		}
	}

	// ---------------------------------------------------------------------
	// Delete methods for java.io.File
	// ---------------------------------------------------------------------

	/*
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
				LOG.error("could not create directory path for [" + dir.getAbsolutePath() + "]");
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

	/*
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

	/*
	 *  删除指定的文件
	 * @param file
	 */
	public static File delete(File file) {
		if (!file.delete()) {
			System.err.println("could not delete file ["
					+ file.getAbsolutePath() + "]");
		}
		return file;
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

	/*
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

	/*
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

}
