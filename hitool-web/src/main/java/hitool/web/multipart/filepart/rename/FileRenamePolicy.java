package hitool.web.multipart.filepart.rename;

import java.io.File;

public interface FileRenamePolicy {

	public abstract File rename(File file);
	
}



