package hitool.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
 */
@SuppressWarnings("resource")
public class FileChannelUtils {

	public static FileChannel getInputChannel(File source) throws FileNotFoundException {
		return new FileInputStream(source).getChannel();
	}

	public static FileChannel getOutputChannel(File dest)
			throws FileNotFoundException {
		return new FileOutputStream(dest).getChannel();
	}

	public static FileChannel getChannel(FileInputStream input) {
		return input.getChannel();
	}

	public static FileChannel getChannel(FileOutputStream output) {
		return output.getChannel();
	}

	/*
	 * 
	 * 功能描述：nio文件拷贝
	 * 
	 * @param infilePath
	 *            源文件路径
	 * @param outfilePath
	 *            目标文件路径
	 * @throws IOException
	 */
	public static void copy(String infilePath, String outfilePath)
			throws IOException {
		File srcFile = new File(infilePath);
		File distFile = new File(outfilePath);
		if (distFile.exists()) {
			distFile.delete();
		}
		FileInputStream fin = new FileInputStream(srcFile);
		FileOutputStream fout = new FileOutputStream(distFile);
		copy(fin, fout);
	}

	/*
	 * 
	 * 功能描述：nio文件拷贝
	 * 
	 * @param in
	 *            源文件输入流
	 * @param out
	 *            目标文件输出流
	 * @throws IOException
	 */
	public static void copy(FileInputStream in, FileOutputStream out)
			throws IOException {
		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();
		int ByteBufferSize = 1024 * 100;
		ByteBuffer buff = ByteBuffer.allocate(ByteBufferSize);
		while (inChannel.read(buff) > 0) {
			buff.flip();
			if (inChannel.position() == inChannel.size()) {// 判断是不是最后一段数据
				int lastRead = (int) (inChannel.size() % ByteBufferSize);
				byte[] bytes = new byte[lastRead];
				buff.get(bytes, 0, lastRead);
				outChannel.write(ByteBuffer.wrap(bytes));
				buff.clear();
			} else {
				outChannel.write(buff);
				buff.clear();
			}
		}
		outChannel.close();
		inChannel.close();
		in.close();
		out.close();
	}

	public static void copy2(FileInputStream in, FileOutputStream out)
			throws IOException {
		// 这个使用FileChannel 自带的复制
		copy2(in.getChannel(), out.getChannel());
		in.close();
		out.close();
	}

	public static void copy(File source, File dest) throws IOException {
		File parent = dest.getParentFile();
		if (!parent.isDirectory()) {
			FileUtils.createDir(parent);
		}
		copy(getInputChannel(source), getOutputChannel(dest));
	}

	public static void copy(FileChannel in, FileChannel out) throws IOException {
		try {
			long size = in.size();
			MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0,
					size);
			out.write(buf);
		} finally {
			if (in != null)
				close(in);
			if (out != null)
				close(out);
		}
	}

	public static void copy2(FileChannel in, FileChannel out)
			throws IOException {
		try {
			out.transferFrom(in, 0, in.size());
		} finally {
			if (in != null)
				close(in);
			if (out != null)
				close(out);
		}
	}

	public static void close(FileChannel channel) {
		try {
			if (null != channel)
				channel.close();
		} catch (IOException e) {
			// can't close? not a lot we can do!
			e.printStackTrace();
		}
	}

}