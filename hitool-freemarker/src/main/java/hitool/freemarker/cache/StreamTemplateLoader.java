package hitool.freemarker.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.TemplateLoader;

public class StreamTemplateLoader implements TemplateLoader {

	protected static Logger LOG = LoggerFactory.getLogger(StreamTemplateLoader.class);
	
	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = (InputStream) templateSource;
		} catch (Exception ex) {
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		
		return null;
	}

	@Override
	public long getLastModified(Object templateSource) {
		return new Date().getTime();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding)throws IOException {
		InputStream inputStream = (InputStream) templateSource;
		try {
			return new InputStreamReader(inputStream, encoding);
		} catch (IOException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Could not find FreeMarker template: " + inputStream.getClass());
			}
			throw ex;
		}
	}

}
