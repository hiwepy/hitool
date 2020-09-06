package hitool.web.multipart.filepart.rename;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import hitool.core.lang3.StringUtils;

public class DateFileRenamePolicy implements FileRenamePolicy {
	
	protected String fileSaveName;
	protected SimpleDateFormat ftm = new SimpleDateFormat("yyyyMMddHHmmss");

	public File rename(File file) {
		String fileSaveName = StringUtils.join(new String[] {
				ftm.format(new Date()), ".",
				FilenameUtils.getExtension(file.getName()) });
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