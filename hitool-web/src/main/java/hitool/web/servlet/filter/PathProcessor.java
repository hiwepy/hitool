package hitool.web.servlet.filter;

import jakarta.servlet.Filter;

/*
 *  给过滤器设置路径
 */
public interface PathProcessor {

	Filter processPath(String path);
	
}
