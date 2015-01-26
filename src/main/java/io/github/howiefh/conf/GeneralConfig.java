/**
 * 
 */
package io.github.howiefh.conf;

import io.github.howiefh.renderer.Markdown;
import io.github.howiefh.util.LogUtil;

import org.dom4j.Element;

import static io.github.howiefh.conf.GeneralConfigValidate.*;

/**
 * 常规设置
 * @author FengHao
 *
 */
public class GeneralConfig extends Config{
	public static final int MAX_THREADS = 10;
	public static final int MIN_THREADS = 1;
	public static final int MAX_READ_TIMEOUT = 30000;
	public static final int MIN_READ_TIMEOUT = 1000;
	public static final int MAX_CONNECTION_TIMEOUT = 30000;
	public static final int MIN_CONNECTION_TIMEOUT = 1000;
	
	public static final String USER_AGENT = "PC";
	public static final int READ_TIMEOUT = 15 * 1000;
	public static final int CONNECTION_TIMEOUT = 10 * 1000;
	public static final String CSS_PATH = "css";
	public static final String MEDIA_PATH = "media";
	public static final String JS_PATH = "js";
	public static final Markdown MARKDOWN = Markdown.GITHUB;
	public static final int THREADS = 8;
	/**
	 * 软件版本号
	 */
	public static String version= "0.1.0";
	public static String userAgent = USER_AGENT;
	public static int readTimeout = READ_TIMEOUT;
	public static int connectionTimeout = CONNECTION_TIMEOUT;
	public static String cssPath = CSS_PATH;
	public static String mediaPath = MEDIA_PATH;
	public static String jsPath = JS_PATH;
	public static Markdown markdown = MARKDOWN;
	public static int threads = THREADS;

	public static final String CONFIG_PATH = "conf/";
	public static final String TEMP_PATH = "temp/";
	public static final String BEFORE_HTML= "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
			+ "<title></title>"
			+ "<style type=\"text/css\">"
			+ "body {font-family: SimSun;}"
			+ "</style>"
			+ "</head>"
			+ "<body>";
	public static final String AFTER_HTML="</body>"
			+ "</html>";
	
	private static GeneralConfig instance = new GeneralConfig();

	private GeneralConfig() {
	}
	
	/* (non-Javadoc)
	 * @see io.github.howiefh.model.domain.Config#generateNodeUnder(org.dom4j.Element)
	 */
	@Override
	public Element generateNodeUnder(Element parentElm) {
		Element generalElm=parentElm.addElement(getElementName());
		
		Element element=generalElm.addElement("version");
		element.setText(version);
		element = generalElm.addElement("userAgent");
		element.setText(userAgent);
		element = generalElm.addElement("readTimeout");
		element.setText(""+readTimeout);
		element = generalElm.addElement("connectionTimeout");
		element.setText(""+connectionTimeout);
		element = generalElm.addElement("cssPath");
		element.setText(cssPath);
		element = generalElm.addElement("mediaPath");
		element.setText(mediaPath);
		element = generalElm.addElement("jsPath");
		element.setText(jsPath);
		element = generalElm.addElement("markdown");
		element.setText(""+markdown);
		element = generalElm.addElement("threads");
		element.setText(""+threads);
		
		return generalElm;
	}

	/* (non-Javadoc)
	 * @see io.github.howiefh.model.domain.Config#fetchFieldValueFromNode(org.dom4j.Element)
	 */
	@Override
	public boolean fetchFieldValueFromNode(Element nodeElm) {
		if (nodeElm == null) {
			return false;
		}
		try {
			version = nodeElm.elementText("version");
			userAgent = nodeElm.elementText("userAgent");
			readTimeout = Integer.valueOf(nodeElm.elementText("readTimeout"));
			connectionTimeout = Integer.valueOf(nodeElm.elementText("connectionTimeout"));
			cssPath = nodeElm.elementText("cssPath");
			mediaPath = nodeElm.elementText("mediaPath");
			jsPath = nodeElm.elementText("jsPath");
			markdown = Markdown.valueOf(nodeElm.elementText("markdown"));
			threads = Integer.valueOf(nodeElm.elementText("threads"));
			return true;
		} catch (Exception e) {
			LogUtil.log().error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see io.github.howiefh.model.domain.Config#getElementName()
	 */
	@Override
	public String getElementName() {
		return "general";
	}
	
	public static GeneralConfig getInstance() {
		return instance;
	}
	@Override
	public String toString() {
		return "GeneralConfig ["
				+ "version=" + version
				+ ", userAgent="+userAgent
				+", readTimeout="+readTimeout
				+", connectionTimeout="+connectionTimeout
				+", cssPath="+cssPath
				+", mediaPath="+mediaPath
				+", jsPath="+jsPath
				+", markdown="+markdown
				+", threads="+threads
				+"]";
	}

	@Override
	public void init() {
		userAgent = USER_AGENT;
		readTimeout = READ_TIMEOUT;
		connectionTimeout = CONNECTION_TIMEOUT;
		cssPath = CSS_PATH;
		mediaPath = MEDIA_PATH;
		jsPath = JS_PATH;
		markdown = MARKDOWN;
		threads = THREADS;
	}

	@Override
	public boolean validate() {
		return validateUserAgent(GeneralConfig.userAgent)&&
				validateConnectionTimeout(GeneralConfig.connectionTimeout)&&
				validateReadTimeout(GeneralConfig.readTimeout)&&
				validateCssPath(GeneralConfig.cssPath)&&
				validateJsPath(GeneralConfig.jsPath)&&
				validateMediaPath(GeneralConfig.mediaPath)&&
				validateMarkdown(GeneralConfig.markdown)&&
				validateThreads(GeneralConfig.threads);
	}
}
