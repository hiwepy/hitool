package hitool.web.multipart;

import java.util.Locale;

public enum Multipart {

	FILEUPLOAD("fileupload"),
	COS("cos");
	
	private final String type;

	private Multipart(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	static Multipart valueOfIgnoreCase(String type) {
		return valueOf(type.toUpperCase(Locale.ENGLISH).trim());
	}
	
}
