package hitool.core.web.servlet.http;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpServletMultipartRequestWrapper extends HttpServletRequestWrapper {

	//当前目录
	protected String dir;
	protected String path;//路径
	protected String order;//排序方式
	//图片类型
	protected String[] imageTypes = new String[]{"gif","jpg","jpeg","png","bmp"};
	//可上传和浏览的目录 
	protected String[] dirType = new String[] { "image", "flash", "media", "file" };
   
    // ----------------------------------------------------- Instance Variables

    public HttpServletRequest target;
    public Map<String, List<String>> formItems;
    
	public HttpServletMultipartRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	 
	public <T> T safeGet(T object,T defaultz){
		return (null!=object)? object: defaultz;
	}

	
}
