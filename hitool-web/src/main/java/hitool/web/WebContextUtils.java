package hitool.web;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebContextUtils {

	private final static Logger LOG = LoggerFactory.getLogger(WebContextUtils.class);
	
	public final static String getBaseContextPath(HttpServletRequest request){
		if(null == request){
			return "";
		}
		// 构造服务的基本URL，提供给业务系统在构造报表数据集时使用
		StringBuilder contextPath = new StringBuilder(request.getScheme()).append("://").append(
				request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath()).append("/");
		LOG.debug("contextPath = " + contextPath.toString());
		return contextPath.toString();
	}
	
}



