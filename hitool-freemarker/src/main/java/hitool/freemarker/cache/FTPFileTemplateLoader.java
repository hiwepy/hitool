package hitool.freemarker.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import freemarker.cache.FileTemplateLoader;

public class FTPFileTemplateLoader extends FileTemplateLoader {
	
    private static final boolean SEP_IS_SLASH = File.separatorChar == '/';
    
	public FTPFileTemplateLoader(File baseDir) throws IOException {
		super(baseDir);
	}
	
	public FTPFileTemplateLoader(File baseDir,boolean disableCanonicalPathCheck) throws IOException {
		super(baseDir,disableCanonicalPathCheck);
	}
	

	@Override
	public Object findTemplateSource(final String name) throws IOException {
		try {
            return AccessController.<File>doPrivileged(new PrivilegedExceptionAction<File>() {
                public File run() throws IOException {
                    File source = new File(baseDir, SEP_IS_SLASH ? name : name.replace('/', File.separatorChar));
                    
                    //ftp download file
                    
                    
                    if(!source.isFile()) {
                        return null;
                    }
                    
                    return source;
                }
            });
        }
        catch(PrivilegedActionException e){
            throw (IOException)e.getException();
        }
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return ((AccessController.doPrivileged(new PrivilegedAction<Long>(){
            public Long run(){
                return new Long(((File)templateSource).lastModified());
            }
        }))).longValue();
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) throws IOException {
		try{
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Reader>(){
                public Reader run() throws  IOException {
                    if (!(templateSource instanceof File)) {
                        throw new IllegalArgumentException("templateSource wasn't a File, but a: " +   templateSource.getClass().getName());
                    }
                    return new InputStreamReader(new FileInputStream((File) templateSource), encoding);
                }
            });
        }catch(PrivilegedActionException e){
            throw (IOException)e.getException();
        }
	}

}
