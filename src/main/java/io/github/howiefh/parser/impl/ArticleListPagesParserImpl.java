package io.github.howiefh.parser.impl;

import io.github.howiefh.parser.ArticleListParser;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.LinkType;
import io.github.howiefh.renderer.util.JsoupUtil;
import io.github.howiefh.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleListPagesParserImpl implements ArticleListParser{
	private int startPage = 1;
	private int pageCount = 1;
	private String selector="";
	private String url="";
	
	private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36";
	private int connectionTimeout=10*1000;
	private int readTimeout =10*1000;
	public ArticleListPagesParserImpl(){
		
	}
	public ArticleListPagesParserImpl(String url,int startPage,int pageCount,String selector){
		this.url = url;
		this.startPage = startPage;
		this.pageCount = pageCount;
		this.selector = selector;
	}
	@Override
	public List<HtmlLink> articleLinkList() throws IOException {
		List<HtmlLink> articleLinks = new ArrayList<HtmlLink>();
		selector = selector + " a[href]";
		for (int i = 0; i < pageCount ; i++) {
			Document doc = getListPageDocument(i+startPage);
			Elements links = doc.select(selector);
			for (Element link : links) {
	            HtmlLink articleLink = new HtmlLink(
	            		link.attr("abs:href"),
	            		link.text(),
	            		LinkType.LINK
	            		);
	            articleLinks.add(articleLink);
	        }
		}
		return articleLinks;
	}
	
	/**
	 * 获取文章列表页面的Document
	 * @param pageIndex
	 * @return
	 * @throws IOException 
	 */
	private Document getListPageDocument(int pageIndex) throws IOException{
		try {
			String newUrl = String.format(url, pageIndex+"");
			Document doc = JsoupUtil.get(newUrl, userAgent, connectionTimeout, readTimeout);
			return doc;
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage());
			throw e;
		}
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
}
