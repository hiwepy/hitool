package hitool.web.multipart.request;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oreilly.servlet.MultipartRequest;

/**
 * Multipart form data request adapter for oreilly package.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class CosMultiPartRequest implements MultiPartRequest {

	protected static Logger LOG = LoggerFactory.getLogger(CosMultiPartRequest.class);
	protected MultipartRequest multi;
	private String defaultEncoding;
	private boolean maxSizeProvided;
	private int maxSize;

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public void setMaxSize(String maxSize) {
		this.maxSizeProvided = true;
		this.maxSize = Integer.parseInt(maxSize);
	}

	@Override
	public String[] getContentType(String fieldName) {
		return new String[] { multi.getContentType(fieldName) };
	}
	
	@Override
	public List getErrors() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public File[] getFile(String fieldName) {
		//获取已经上传完毕的文件对象
		return new File[] { multi.getFile(fieldName) };
	}

	@Override
	public String[] getFileNames(String fieldName) {
		return new String[] { multi.getFile(fieldName).getName() };
	}

	@Override
	public String getOriginalFileName(String fieldName){
		return multi.getOriginalFileName(fieldName);
	}
	
	@Override
	public Enumeration<String> getFileParameterNames() {
		return multi.getFileNames();
	}

	@Override
	public String[] getFilesystemName(String name) {
		return new String[] { multi.getFilesystemName(name) };
	}

	@Override
	public String getParameter(String name) {
		return multi.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return multi.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return multi.getParameterValues(name);
	}

	@Override
	public void parse(HttpServletRequest request, String saveDir) throws IOException {
		if (maxSizeProvided) {
			multi = new MultipartRequest(request, saveDir, maxSize, defaultEncoding);
		} else {
			multi = new MultipartRequest(request, saveDir, defaultEncoding);
		}
	}

	@Override
	public void cleanUp() {
		Enumeration fileParameterNames = multi.getFileNames();
		while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
			String inputValue = (String) fileParameterNames.nextElement();
			File[] files = getFile(inputValue);
			for (File currentFile : files) {
				if (LOG.isInfoEnabled()) {
					LOG.info("Removing file {0} {1}",new Object[] { inputValue, currentFile });
				}
				if ((currentFile != null) && currentFile.isFile()) {
					if (!currentFile.delete()) {
						if (LOG.isWarnEnabled()) {
							LOG.warn("Resource Leaking:  Could not remove uploaded file [#0]", currentFile.getAbsolutePath());
						}
					}
				}
			}
		}
	}

}
