package hitool.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import hitool.core.lang3.Assert;

/*
 */
@SuppressWarnings("rawtypes")
public abstract class FiletypeUtils {

	protected static final String MIMETYPES_PROPERTIES = "fileTypes.properties";
	protected static final String DEFAULT_TYPE = "null";
	protected static Properties mFileTypes;
	
	static{
		try {
			mFileTypes = new Properties();
			mFileTypes.load(FilemimeUtils.class.getResourceAsStream(MIMETYPES_PROPERTIES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileType(File file) {
		if (file == null) {
			return DEFAULT_TYPE;
		}
        if(!file.exists() || file.length()<11){
            return DEFAULT_TYPE;
        }
        String header = get10ByteHeader(file);
        String fileSuffix = mFileTypes.getProperty(header);
		/* 
		 * 优化处理：在不同的设备上同样类型的文件，文件头前面内容未必一致，可能只有前几个一致，后面就不同了
		 * （例如：jpg类型文件，在不同手机上，lennovo k900前10个是一致的，但是MI3只有前5个字符一致，后面是不一样的，所有一些情况进行特殊处理）当整个头文件失败后，
		 * 在进行前5个字符截取对比处理，优化具体如下：
		 */
        if(StringUtils.isEmpty(fileSuffix)){
        	
			Iterator keyList = mFileTypes.keySet().iterator();
        	//并不是所有的文件格式前10 byte（jpg）都一致，前五个byte一致即可
            String key,keySearchPrefix = header.substring(0,5);
            while (keyList.hasNext()){
                key = (String) keyList.next();
                if(key.contains(keySearchPrefix)) {
                    fileSuffix = mFileTypes.getProperty(key);
                    break;
                }
            }
        }
        
        //前5个字符截取对比处理没有找到，则进行特殊处理
        if(StringUtils.isEmpty(fileSuffix)){
        	header = get3ByteHeader(file);
        	fileSuffix = mFileTypes.getProperty(header);
        }
		
		return fileSuffix;
	}

	public static String getFileType(byte[] bytes) {
		if (bytes == null || bytes.length < 11) {
			return DEFAULT_TYPE;
		}
		
		String header = bytesToHexString(ArrayUtils.subarray(bytes, 0, 10));
		String fileSuffix = mFileTypes.getProperty(header);
		/* 
		 * 优化处理：在不同的设备上同样类型的文件，文件头前面内容未必一致，可能只有前几个一致，后面就不同了
		 * （例如：jpg类型文件，在不同手机上，lennovo k900前10个是一致的，但是MI3只有前5个字符一致，后面是不一样的，所有一些情况进行特殊处理）当整个头文件失败后，
		 * 在进行前5个字符截取对比处理，优化具体如下：
		 */
        if(StringUtils.isEmpty(fileSuffix)){
        	Iterator keyList = mFileTypes.keySet().iterator();
        	//并不是所有的文件格式前10 byte（jpg）都一致，前五个byte一致即可
            String key,keySearchPrefix = header.substring(0,5);
            while (keyList.hasNext()){
                key = (String) keyList.next();
                if(key.contains(keySearchPrefix)) {
                    fileSuffix = mFileTypes.getProperty(key);
                    break;
                }
            }
        }
        
        //前5个字符截取对比处理没有找到，则进行特殊处理
        if(StringUtils.isEmpty(fileSuffix)){
        	header = bytesToHexString(ArrayUtils.subarray(bytes, 0, 3));
        	fileSuffix = mFileTypes.getProperty(header);
        }
		
		return fileSuffix;
		
	}
	
	
	private static String get10ByteHeader(File file) {
        String value = null;
        try (InputStream input = new FileInputStream(file);){
            byte[] b = new byte[10];
            input.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        }
        return value;
    }
	
	private static String get3ByteHeader(File file) {
        String value = null;
        try (InputStream input = new FileInputStream(file);){
            byte[] b = new byte[3];
            input.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        }
        return value;
    }

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
	
	

	public static boolean isXls(String filePath) {
		return isXls(new File(filePath));
	}
	
	public static boolean isXls(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "xls".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	public static boolean isXlsx(String filePath) {
		return isXlsx(new File(filePath));
	}
	
	public static boolean isXlsx(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "xlsx".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	public static boolean isDoc(String filePath) {
		return isDoc(new File(filePath));
	}
	
	public static boolean isDoc(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "doc".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	public static boolean isDocx(String filePath) {
		return isDocx(new File(filePath));
	}
	
	public static boolean isDocx(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "docx".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
}
