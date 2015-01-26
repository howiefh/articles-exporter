package io.github.howiefh.renderer;

import io.github.howiefh.renderer.util.JsoupUtil;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PreProcesser {
	private String tempPath ="temp/";
	private String beforeHTML= "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
			+ "<title></title>"
			+ "<style type=\"text/css\">"
			+ "body {font-family: SimSun;}"
			+ "</style>"
			+ "</head>"
			+ "<body>";
	private String afterHTML= "</body>"
			+ "</html>";
	
	private String selector="body";
	private String cssPath = "css/";
	private String mediaPath = "media/";
	private String jsPath = "js/";
	private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36";
	private int readTimeout = 10*1000;
	private int connectionTimeout=10*1000;
	public PreProcesser() {
	}
	/**
	 * 获取文章的正文的Document
	 * @param link
	 * @return
	 * @throws IOException 
	 */
	public Document newDocumentFromArticleContent(HtmlLink link) throws IOException {
		try {
			Document doc = JsoupUtil.get(link.getHref(), userAgent, connectionTimeout, readTimeout);
			Element content = doc.select(selector).first();
			String html = beforeHTML+content.toString()+afterHTML;
			Document res = Jsoup.parse(html, doc.baseUri()); 
			res.title(link.getContent());
			return res;
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage()+" : "+link.getContent()+" : "+link.getHref());
			throw e;
		}
	}
	
	/**
	 * 进行预处理，获取文章正文，并将正文中的资源文件保存到本地
	 * @param link
	 * @return
	 * @throws Exception 
	 */
	public Document process(HtmlLink link) throws Exception {
		String url = link.getHref();
		Document doc = null;
		try {
			doc = newDocumentFromArticleContent(link);
			makeHtmlLinkFile(doc, tempPath, connectionTimeout, readTimeout);// 根据网页中css、js、img链接地址及文件名生成本地文件
		    doc.select("html").before("<!-- saved from url=("+new DecimalFormat("0000").format(url.length())+")"+url+" -->");
		    doc.body().prepend("<h1><a name='"+link.getUuid()+"'>"+link.getContent()+"</a></h1>");
		} catch (IllegalArgumentException | IOException e) {
			LogUtil.log().error("访问页面地址 : " + url + "失败."+e.getMessage());
			throw e;
		}
		return doc;
	}
	/**
	 * 根据网页中css、js、img链接地址及文件名生成本地文件
	 * @param doc
	 * @param filepath
	 * @param connectionTimeout
	 * @param readTimeout
	 */
	public void makeHtmlLinkFile(Document doc, String filepath,int connectionTimeout, int readTimeout) {
		List<HtmlLink> list = getHtmlFileLinks(doc);// 根据url抓取网页中找到css样式链接地址及文件名
		for (HtmlLink hLink : list) {
			try {
				if (hLink.getType()==LinkType.DATAURL) {
					IOUtil.convertBase64DataToImage(hLink.getHref(), FilenameUtils.concat(filepath, hLink.getContent()));
					continue;
				}
				IOUtil.copyURLToFile(hLink.getHref()
				, new File(FilenameUtils.concat(filepath, hLink.getContent()))
				, connectionTimeout, readTimeout);
			} catch (MalformedURLException e) {
				LogUtil.log().error("地址 :" + hLink.getHref() + " 链接错误."+e.getMessage());
				continue;
			} catch (IOException e) {
				LogUtil.log().error("地址 :" + hLink.getHref() + " 链接超时."+e.getMessage());
				continue;
			}
		}
	}

	/**
	 * 根据url抓取网页中找到css样式,js,img链接地址及文件名
	 * 
	 * @param doc
	 *            jsoup.nodes.Document
	 * @return List<HtmlLink>
	 */
	public List<HtmlLink> getHtmlFileLinks(Document doc) {
		List<HtmlLink> htmlLinks = new ArrayList<HtmlLink>();
		Elements importcss = doc.select("link[href]");// 找到document中带有link标签的元素
		for (Element link : importcss) {
			if (link.attr("rel").equals("stylesheet")) {// 如果rel属性为HtmlFileLink
				htmlLinks.add(getAndRenameLink(link, this.cssPath, "href"));
			}
		}
		Elements media = doc.select("[src]");
		for (Element link : media) {
			String path = this.mediaPath;
			boolean isRealLink=false;
			if (link.tagName().equals("img")) {
				if (link.attr("src").startsWith("data:")) {
					htmlLinks.add(getAndRenameDataUrl(link, path, "src"));
				} else {
					isRealLink = true;
				}
			}
			if (link.tagName().equals("input")) {
				if (link.attr("type").equals("image")) {
					isRealLink = true;
				}
			}
			if (link.tagName().equals("javascript")
					|| link.tagName().equals("script")) {
				path = this.jsPath;
				isRealLink = true;
			}
			if (link.tagName().equals("iframe")) {
				isRealLink = true;
			}
			if (link.tagName().equals("embed")) {
				isRealLink = true;
			}
			if (isRealLink) {
				htmlLinks.add(getAndRenameLink(link, path, "src"));
			}
		}
		return htmlLinks;
	}

	private HtmlLink getAndRenameLink(Element link, String path, String attr){
		String src = link.attr("abs:"+attr);
		HtmlLink htmlLink = new HtmlLink(src);
		String filename = FilenameUtils.concat(path,htmlLink.getUuid()+"-" + getRealName(src));
		htmlLink.setContent(filename);
		link.attr(attr, filename);
		return htmlLink;
	}
	
	private HtmlLink getAndRenameDataUrl(Element link, String path, String attr){
		String gif = "data:image/gif;base64,";
		String png = "data:image/png;base64,";
		String jpeg = "data:image/jpeg;base64,";
		String icon = "data:image/x-icon;base64,";
		String src = link.attr("src");
		String ext = "";
		if (src.startsWith(gif)) {
			src = src.substring(gif.length());
			ext = ".gif";
		} else if (src.startsWith(png)) {
			src = src.substring(png.length());
			ext = ".png";
		} else if (src.startsWith(jpeg)) {
			src = src.substring(jpeg.length());
			ext = ".jpg";
		} else if (src.startsWith(icon)) {
			src = src.substring(icon.length());
			ext = ".ico";
		}
		HtmlLink htmlLink = new HtmlLink(src,LinkType.DATAURL);
		String filename = FilenameUtils.concat(path,htmlLink.getUuid()+ext);
		htmlLink.setContent(filename);
		link.attr(attr, filename);
		return htmlLink;
	}

	/**
	 * 过滤文件名,得到文件后缀
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	private static String getRealName(String filename) {
		filename = IOUtil.getName(filename);
		filename = StringUtils.substringBefore(filename, "?");
		return filename;
	}
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	public String getBeforeHTML() {
		return beforeHTML;
	}
	public void setBeforeHTML(String beforeHTML) {
		this.beforeHTML = beforeHTML;
	}
	public String getAfterHTML() {
		return afterHTML;
	}
	public void setAfterHTML(String afterHTML) {
		this.afterHTML = afterHTML;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public String getCssPath() {
		return cssPath;
	}
	public void setCssPath(String cssPath) {
		this.cssPath = cssPath;
	}
	public String getMediaPath() {
		return mediaPath;
	}
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	public String getJsPath() {
		return jsPath;
	}
	public void setJsPath(String jsPath) {
		this.jsPath = jsPath;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}
