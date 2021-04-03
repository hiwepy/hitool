package hitool.web.servlet.http;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/*
 * 使用Decorator模式包装request对象，实现敏感字符过滤功能
 */
public class HttpServletDirtyWordFilterRequestWrapper extends HttpServletRequestWrapper {

	private List<String> dirtyWords ;
	private HttpServletRequest request;

	public HttpServletDirtyWordFilterRequestWrapper(HttpServletRequest request,List<String> dirtyWords) {
		super(request);
		this.request = request;
		this.dirtyWords = dirtyWords;
	}

	/*
	 * 重写getParameter方法，实现对敏感字符的过滤
	 * 
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {

		String value = this.request.getParameter(name);
		if (value == null) {
			return null;
		}

		for (String dirtyWord : dirtyWords) {
			if (value.contains(dirtyWord)) {
				System.out.println("内容中包含敏感词：" + dirtyWord + "，将会被替换成****");
				// 替换敏感字符
				value = value.replace(dirtyWord, "****");
			}
		}
		return value;
	}
}
