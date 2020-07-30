package hitool.core.web.multipart.filepart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import hitool.core.io.FileUtils;
import hitool.core.io.FilenameUtils;
import hitool.core.web.exception.InvalidFileNameException;

/**
 */
public class FilePart {

	
	// ----------------------------------------------------- Manifest constants

    /**
     * Default content charset to be used when no explicit charset
     * parameter is provided by the sender. Media subtypes of the
     * "text" type are defined to have a default charset value of
     * "ISO-8859-1" when received via HTTP.
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * The name of the form field as provided by the browser.
     */
    private String fieldName;


    /**
     * The content type passed by the browser, or <code>null</code> if
     * not defined.
     */
    private String contentType;


    /**
     * Whether or not this item is a simple form field.
     */
    private boolean isFormField;


    /**
     * The original filename in the user's filesystem.
     */
    private String fileName;

    private String filetype = null;

    /**
     * The size of the item, in bytes. This is used to cache the size when a
     * file item is moved from its original location.
     */
    private long size = -1;


    /**
     * The directory in which uploaded files will be stored, if stored on disk.
     */
    private File repository;
    
    private String originalFileName;//原始文件名称
    private String filesystemName;//文件系统名称
    private String fileStorePath;//文件存储路径(绝对访问路径)
    private String fileExtName; //文件扩展名(比如: .jpg)
    private String fileWebPath,fileWebRootPath; //文件的web使用路径

    // ----------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>DiskFileItem</code> instance.
     *
     * @param fieldName     The name of the form field.
     * @param contentType   The content type passed by the browser or
     *                      <code>null</code> if not specified.
     * @param isFormField   Whether or not this item is a plain form field, as
     *                      opposed to a file upload.
     * @param fileName      The original filename in the user's filesystem, or
     *                      <code>null</code> if not specified.
     * @param repository    The data repository, which is the directory in
     *                      which files will be created, should the item size
     *                      exceed the threshold.
     */
    public FilePart(String fieldName, String contentType,String fileName, String originalFileName, File file) {
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.isFormField = isFormField;
        this.fileName = fileName;
        this.repository = repository;
        this.fileWebRootPath = fileWebRootPath;
        this.originalFileName = originalFileName;
        this.filesystemName = filesystemName;
        this.size = repository.length();
    	//文件扩展名(比如: .jpg);
    	this.fileExtName= FilenameUtils.getExtension(this.repository); 
    	//文件存储路径(绝对访问路径);
        this.fileStorePath=this.repository.getAbsolutePath();
        this.fileWebPath= FileUtils.getWebPath(getStoreLocation(),this.fileWebRootPath); //文件的web使用路径;
    }


    // ------------------------------- Methods from javax.activation.DataSource


    /**
     * @throws IOException if an error occurs.
     */
    public InputStream getInputStream() throws IOException {
         return new FileInputStream(this.repository);
    }


    /**
     * Returns the content type passed by the agent or <code>null</code> if
     * not defined.
     *
     * @return The content type passed by the agent or <code>null</code> if
     *         not defined.
     */
    public String getContentType() {
        return contentType;
    }


    /**
     * Returns the original filename in the client's filesystem.
     *
     * @return The original filename in the client's filesystem.
     * @throws InvalidFileNameException The file name contains a NUL character,
     *   which might be an indicator of a security attack. If you intend to
     *   use the file name anyways, catch the exception and use
     *   InvalidFileNameException#getName().
     */
    public String getName() {
        return  fileName;
    }


    // ------------------------------------------------------- FileItem methods

    /**
     * Returns the size of the file.
     *
     * @return The size of the file, in bytes.
     */
    public long getSize() {
        return size;
    }


    /**
     * Returns the contents of the file as an array of bytes.  If the
     * contents of the file were not yet cached in memory, they will be
     * loaded from the disk storage and cached.
     *
     * @return The contents of the file as an array of bytes.
     */
    public byte[] bytes() {
        byte[] fileData = new byte[(int) getSize()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.repository);
            fis.read(fileData);
        } catch (IOException e) {
            fileData = null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return fileData;
    }


    /**
     * Returns the contents of the file as a String, using the specified
     * encoding.  This method uses {@link #get()} to retrieve the
     * contents of the file.
     *
     * @param charset The charset to use.
     *
     * @return The contents of the file, as a string.
     *
     * @throws UnsupportedEncodingException if the requested character
     *                                      encoding is not available.
     */
    public String getString(final String charset)
        throws UnsupportedEncodingException {
        return new String(bytes(), charset);
    }

    /**
     * Deletes the underlying storage for a file item, including deleting any
     * associated temporary disk file. Although this storage will be deleted
     * automatically when the <code>FileItem</code> instance is garbage
     * collected, this method can be used to ensure that this is done at an
     * earlier time, thus preserving system resources.
     */
    public void delete() {
        File outputFile = getStoreLocation();
        if (outputFile != null && outputFile.exists()) {
            outputFile.delete();
        }
    }


    /**
     * Returns the name of the field in the multipart form corresponding to
     * this file item.
     *
     * @return The name of the form field.
     *
     * @see #setFieldName(java.lang.String)
     *
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     * Sets the field name used to reference this file item.
     *
     * @param fieldName The name of the form field.
     *
     * @see #getFieldName()
     *
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Determines whether or not a <code>FileItem</code> instance represents
     * a simple form field.
     *
     * @return <code>true</code> if the instance represents a simple form
     *         field; <code>false</code> if it represents an uploaded file.
     *
     * @see #setFormField(boolean)
     *
     */
    public boolean isFormField() {
        return isFormField;
    }


    /**
     * Specifies whether or not a <code>FileItem</code> instance represents
     * a simple form field.
     *
     * @param state <code>true</code> if the instance represents a simple form
     *              field; <code>false</code> if it represents an uploaded file.
     *
     * @see #isFormField()
     *
     */
    public void setFormField(boolean state) {
        isFormField = state;
    }


    /**
     */
    public File getStoreLocation() {
        return this.repository;
    }


    // ------------------------------------------------------ Protected methods


    /**
     * Removes the file contents from the temporary storage.
     */
    protected void finalize() {
        if ( this.repository != null &&  this.repository.exists()) {
        	 this.repository.delete();
        }
    }


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return "name=" + this.getName()
            + ", StoreLocation="
            + String.valueOf(this.getStoreLocation())
            + ", size="
            + this.getSize()
            + "bytes, "
            + "isFormField=" + isFormField()
            + ", FieldName="
            + this.getFieldName();
    }


	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.repository);
	}


	public void write(File file) throws Exception {
		if(file!=null&&file.exists()){
			InputStream  input =null;
			OutputStream  output =null;
	        try   {
	        	input = new FileInputStream(file);
	            output  = new FileOutputStream(file);
	            IOUtils.copy(input, output);
	         }catch(Exception e){
	        	 output .close();
	         }finally{
	        	 output .flush();
	        	 output .close();
	         } 
		}
		
	}

	public String getFileName() {
		return fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public String getFilesystemName() {
		return filesystemName;
	}

	//文件扩展名(比如: .jpg);
	public String getFileExtName() {
		return  (fileExtName!=null)?fileExtName:FilenameUtils.getExtension(this.repository); 
	}

	//文件存储路径(绝对访问路径);
	public String getFileStorePath() {
		return (fileStorePath!=null)?fileStorePath:this.repository.getAbsolutePath();
	}
	
	//文件的web使用路径
	public String getFileWebPath() {
		return (fileWebPath!=null)?fileWebPath: FileUtils.getWebPath(getStoreLocation(),this.fileWebRootPath); //文件的web使用路径;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	 

}
