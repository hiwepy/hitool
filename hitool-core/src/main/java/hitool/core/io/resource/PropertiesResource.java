/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.io.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import hitool.core.io.PropertiesLoaderUtils;
import hitool.core.io.support.EncodedResource;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;

public class PropertiesResource extends AbstractResource implements WritableResource {

	public static final PropertiesResource EMPTY_RESOURCE = new PropertiesResource();
	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final String XML_SUFFIX = ".xml";
	private Properties properties;
	private String path;
	private File file;
	private ClassLoader classLoader;
	private String encoding;
	
	public PropertiesResource() {
		this.properties = new Properties();
	}
	
	public PropertiesResource(Properties properties) {
		Assert.notNull(properties, "Properties must not be null");
		this.properties = properties;
	}
	
	public PropertiesResource(File file) {
		Assert.notNull(file, "File must not be null");
		this.file = file;
		this.path = StringUtils.cleanPath(file.getPath());
	}
	
	public PropertiesResource(File file,String encoding) {
		Assert.notNull(file, "File must not be null");
		this.file = file;
		this.path = StringUtils.cleanPath(file.getPath());
		this.encoding = encoding;
	}

	public PropertiesResource(String path) {
		Assert.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = StringUtils.cleanPath(path);
	}
	
	public PropertiesResource(String path,String encoding) {
		Assert.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = StringUtils.cleanPath(path);
		this.encoding = encoding;
	}

	public PropertiesResource(String path,ClassLoader bundleClassLoader) {
		Assert.notNull(path, "ResourceName must not be null");
		this.path = path;
		this.classLoader = bundleClassLoader;
	}
	
	public PropertiesResource(String path, String defaultEncoding,ClassLoader bundleClassLoader) {
		Assert.notNull(path, "location must not be null");
		this.path = path;
		this.encoding = defaultEncoding;
		this.classLoader = bundleClassLoader;
	}

	/**
	 * Return the file location for this resource.
	 */
	public final String getPath() {
		return this.path;
	}


	/**
	 * This implementation returns whether the underlying file exists.
	 * @see java.io.File#exists()
	 */
	@Override
	public boolean exists() {
		return this.file.exists();
	}

	/**
	 * This implementation checks whether the underlying file is marked as readable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.io.File#canRead()
	 * @see java.io.File#isDirectory()
	 */
	@Override
	public boolean isReadable() {
		return (this.file.canRead() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileInputStream for the underlying file.
	 * @see java.io.FileInputStream
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	/**
	 * This implementation returns a URL for the underlying file.
	 * @see java.io.File#toURI()
	 */
	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

	/**
	 * This implementation returns a URI for the underlying file.
	 * @see java.io.File#toURI()
	 */
	@Override
	public URI getURI() throws IOException {
		return this.file.toURI();
	}

	/**
	 * This implementation returns the underlying File reference.
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * This implementation returns the underlying File's length.
	 */
	@Override
	public long contentLength() throws IOException {
		return this.file.length();
	}

	/**
	 * This implementation creates a FileSystemResource, applying the given location
	 * relative to the location of the underlying file of this resource descriptor.
	 * @see org.springframework.util.StringUtils#applyRelativePath(String, String)
	 */
	@Override
	public Resource createRelative(String relativePath) {
		String locationToUse = StringUtils.applyRelativePath(this.path, relativePath);
		return new PropertiesResource(locationToUse);
	}

	/**
	 * This implementation returns the name of the file.
	 * @see java.io.File#getName()
	 */
	@Override
	public String getFilename() {
		return this.file.getName();
	}

	/**
	 * This implementation returns a description that includes the absolute
	 * location of the file.
	 * @see java.io.File#getAbsolutePath()
	 */
	@Override
	public String getDescription() {
		return "file [" + this.file.getAbsolutePath() + "]";
	}


	// implementation of WritableResource

	/**
	 * This implementation checks whether the underlying file is marked as writable
	 * (and corresponds to an actual file with content, not to a directory).
	 * @see java.io.File#canWrite()
	 * @see java.io.File#isDirectory()
	 */
	@Override
	public boolean isWritable() {
		return (this.file.canWrite() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileOutputStream for the underlying file.
	 * @see java.io.FileOutputStream
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.file);
	}


	/**
	 * This implementation compares the underlying File references.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
			(obj instanceof PropertiesResource && this.path.equals(((PropertiesResource) obj).path)));
	}

	/**
	 * This implementation returns the hash code of the underlying File reference.
	 */
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

	public Properties getProperties() {
		if(properties == null){
			try {
				if(path != null && encoding == null){
					if(classLoader == null){
						properties = PropertiesLoaderUtils.loadAllProperties(path, classLoader);
					}else{
						properties = PropertiesLoaderUtils.loadAllProperties(path);
					}
				}else if(path != null && encoding != null){
					properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(this,this.encoding));
				}else{
					properties = PropertiesLoaderUtils.loadProperties(this);
				}
			} catch (IOException e) {
				e.printStackTrace();
				properties = new Properties();
			} 
		}
		return properties;
	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public String getString(String key,String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static PropertiesResource getBundle(String basename,String defaultEncoding, ClassLoader bundleClassLoader) {
		PropertiesResource resource = new PropertiesResource(basename + PROPERTIES_SUFFIX,defaultEncoding,bundleClassLoader);
		if(!resource.exists()){
			resource = new PropertiesResource(basename + XML_SUFFIX, defaultEncoding,bundleClassLoader);
		}
		return resource;
	}
	
	public static PropertiesResource getBundle(String location,ClassLoader bundleClassLoader) {
		return new PropertiesResource(location,bundleClassLoader);
	}
	
}
