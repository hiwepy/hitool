/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.web.multipart.request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Multipart form data request adapter for Jakarta Commons Fileupload package.
 */
public class JakartaMultiPartRequest implements MultiPartRequest {

    static final Logger LOG = LoggerFactory.getLogger(JakartaMultiPartRequest.class);

    // maps parameter name -> List of FileItem objects
    protected Map<String, List<FileItem>> files = new HashMap<String, List<FileItem>>();

    // maps parameter name -> List of param values
    protected Map<String, List<String>> params = new HashMap<String, List<String>>();

    // any errors while processing this request
    protected List<String> errors = new ArrayList<String>();

	/*
	 * 编码格式 ，默认： UTF-8
	 */
    protected String encoding = " UTF-8";
    protected long maxSize;
    protected long maxFileSize;
	
    public void setMaxSize(String maxSize) {
        this.maxSize = Long.parseLong(maxSize);
    }

    public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = Long.parseLong(maxFileSize);
	}

	/*
     * Creates a new request wrapper to handle multi-part data using methods adapted from Jason Pell's
     * multipart classes (see class description).
     *
     * @param saveDir the directory to save off the file
     * @param request the request containing the multipart
     * @throws java.io.IOException is thrown if encoding fails.
     */
    public void parse(HttpServletRequest request, String saveDir) throws IOException {
        try {
            processUpload(request, saveDir );
        } catch (FileUploadBase.SizeLimitExceededException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Request exceeded size limit!", e);
            }
            String errorMessage = MessageFormat.format("Request exceeded allowed size limit! Max size allowed is: {0} but request was: {1}!", new Object[]{e.getPermittedSize(), e.getActualSize()});
            if (!errors.contains(errorMessage)) {
                errors.add(errorMessage);
            }
        } catch (Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unable to parse request", e);
            }
            String errorMessage =  MessageFormat.format("Error uploading: {0}!", e.getMessage());
            if (!errors.contains(errorMessage)) {
                errors.add(errorMessage);
            }
        }
    }

    protected void processUpload(HttpServletRequest request, String saveDir) throws FileUploadException, UnsupportedEncodingException {
        for (FileItem item : parseRequest(request, saveDir)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found item " + item.getFieldName());
            }
            if (item.isFormField()) {
                processNormalFormField(item, request.getCharacterEncoding());
            } else {
                processFileField(item);
            }
        }
    }

    protected void processFileField(FileItem item) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Item is a file upload");
        }

        // Skip file uploads that don't have a file name - meaning that no file was selected.
        if (item.getName() == null || item.getName().trim().length() < 1) {
            LOG.debug("No file has been uploaded for the field: " + item.getFieldName());
            return;
        }

        List<FileItem> values;
        if (files.get(item.getFieldName()) != null) {
            values = files.get(item.getFieldName());
        } else {
            values = new ArrayList<FileItem>();
        }

        values.add(item);
        files.put(item.getFieldName(), values);
    }

    protected void processNormalFormField(FileItem item, String charset) throws UnsupportedEncodingException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Item is a normal form field");
        }
        List<String> values;
        if (params.get(item.getFieldName()) != null) {
            values = params.get(item.getFieldName());
        } else {
            values = new ArrayList<String>();
        }

        // note: see http://jira.opensymphony.com/browse/WW-633
        // basically, in some cases the charset may be null, so
        // we're just going to try to "other" method (no idea if this
        // will work)
        if (charset != null) {
            values.add(item.getString(charset));
        } else {
            values.add(item.getString());
        }
        params.put(item.getFieldName(), values);
        item.delete();
    }

    protected List<FileItem> parseRequest(HttpServletRequest servletRequest, String saveDir) throws FileUploadException {
        DiskFileItemFactory fac = createDiskFileItemFactory(saveDir);
        ServletFileUpload upload = createServletFileUpload(fac);
        //7.解析请求
        return upload.parseRequest(createRequestContext(servletRequest));
    }

    protected ServletFileUpload createServletFileUpload(DiskFileItemFactory fac) {
    	//3.创建一个新的文件上传处理对象
    	ServletFileUpload upload = new ServletFileUpload(fac);
        //4.设置最大上传文件SIZE
  		upload.setSizeMax(maxSize);
  		//5.设置单个上传文件最大SIZE
  		upload.setFileSizeMax(maxFileSize);
  		//6.设置编码格式
  		upload.setHeaderEncoding(encoding);
        return upload;
    }

    protected DiskFileItemFactory createDiskFileItemFactory(String saveDir) {
    	//创建文件解析工厂
        DiskFileItemFactory diskFactory = new DiskFileItemFactory();
        //1.设置文件存放的路径;repository 贮藏室，即临时文件目录
        if (saveDir != null) {
            diskFactory.setRepository(new File(saveDir));
        }
        // Make sure that the data is written to file
        //2.设置最大内存大小;threshold 极限、临界值，为了确保文件被写到文件，这里设置不使用缓存 
        diskFactory.setSizeThreshold(0);
        return diskFactory;
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFileParameterNames()
     */
    public Enumeration<String> getFileParameterNames() {
        return Collections.enumeration(files.keySet());
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getContentType(java.lang.String)
     */
    public String[] getContentType(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> contentTypes = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            contentTypes.add(fileItem.getContentType());
        }

        return contentTypes.toArray(new String[contentTypes.size()]);
    }

    public File[] getFile(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<File> fileList = new ArrayList<File>(items.size());
        for (FileItem fileItem : items) {
            File storeLocation = ((DiskFileItem) fileItem).getStoreLocation();
            if (fileItem.isInMemory() && storeLocation != null && !storeLocation.exists()) {
                try {
                    storeLocation.createNewFile();
                } catch (IOException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Cannot write uploaded empty file to disk: " + storeLocation.getAbsolutePath(), e);
                    }
                }
            }
            fileList.add(storeLocation);
        }

        return fileList.toArray(new File[fileList.size()]);
    }

    public String[] getFileNames(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            fileNames.add(getCanonicalName(fileItem.getName()));
        }

        return fileNames.toArray(new String[fileNames.size()]);
    }
    
    @Override
	public String getOriginalFileName(String fieldName){
    	List<FileItem> items = files.get(fieldName);
        if (items == null) {
            return null;
        }
        for (FileItem fileItem : items) {
        	return getCanonicalName(fileItem.getName());
        }
        return null;
	}

    public String[] getFilesystemName(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            fileNames.add(((DiskFileItem) fileItem).getStoreLocation().getName());
        }

        return fileNames.toArray(new String[fileNames.size()]);
    }

    public String getParameter(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return v.get(0);
        }

        return null;
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    public String[] getParameterValues(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return v.toArray(new String[v.size()]);
        }

        return null;
    }

    public List<String> getErrors() {
        return errors;
    }

    /*
     * Returns the canonical name of the given file.
     *
     * @param filename the given file
     * @return the canonical name of the given file
     */
    private String getCanonicalName(String filename) {
        int forwardSlash = filename.lastIndexOf("/");
        int backwardSlash = filename.lastIndexOf("\\");
        if (forwardSlash != -1 && forwardSlash > backwardSlash) {
            filename = filename.substring(forwardSlash + 1, filename.length());
        } else if (backwardSlash != -1 && backwardSlash >= forwardSlash) {
            filename = filename.substring(backwardSlash + 1, filename.length());
        }

        return filename;
    }

    /*
     * Creates a RequestContext needed by Jakarta Commons Upload.
     *
     * @param req the request.
     * @return a new request context.
     */
    protected RequestContext createRequestContext(final HttpServletRequest req) {
        return new RequestContext() {
            public String getCharacterEncoding() {
                return req.getCharacterEncoding();
            }

            public String getContentType() {
                return req.getContentType();
            }

            public int getContentLength() {
                return req.getContentLength();
            }

            public InputStream getInputStream() throws IOException {
                InputStream in = req.getInputStream();
                if (in == null) {
                    throw new IOException("Missing content in the request");
                }
                return req.getInputStream();
            }
        };
    }

    public void cleanUp() {
        Set<String> names = files.keySet();
        for (String name : names) {
            List<FileItem> items = files.get(name);
            for (FileItem item : items) {
                if (LOG.isDebugEnabled()) {
                	LOG.debug("Removing file {0} {1}",new Object[] { name, item });
                }
                if (!item.isInMemory()) {
                    item.delete();
                }
            }
        }
    }

}
