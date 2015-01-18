/**
 * 
 */
package io.github.howiefh.conf;

import io.github.howiefh.renderer.Markdown;

import org.dom4j.Element;

/**
 * 常规设置
 * @author FengHao
 *
 */
public class GeneralConfig extends Config{
	/**
	 * 软件版本号
	 */
	public static String version="1.0";
	public static String userAgent = "PC";
	public static int readTimeout = 10 * 1000;
	public static int connectionTimeout = 10 * 1000;
	public static String cssPath = "css/";
	public static String mediaPath = "media/";
	public static String jsPath = "js/";
	public static Markdown markdown = Markdown.GITHUB;
	public static int threads = 8;

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
}