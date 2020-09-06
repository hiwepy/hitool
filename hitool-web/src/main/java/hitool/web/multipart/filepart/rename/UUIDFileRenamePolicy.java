package hitool.web.multipart.filepart.rename;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import hitool.core.lang3.StringUtils;

public class UUIDFileRenamePolicy implements FileRenamePolicy{
	
	protected String fileSaveName;
	
	public File rename(File file) {
		String fileSaveName = StringUtils.join(new String[] { UUID.randomUUID().toString(), ".",FilenameUtils.getExtension(file.getName()) });
		setFileSaveName(fileSaveName);
		File result = new File(file.getParentFile(), fileSaveName);
		return result;
	}
	
	public String getFileSaveName() {
		return fileSaveName;
	}
	
	private void setFileSaveName(String fileSaveName) {
		this.fileSaveName = fileSaveName;
	}
}