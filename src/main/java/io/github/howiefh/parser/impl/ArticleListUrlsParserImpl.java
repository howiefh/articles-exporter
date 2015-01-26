package io.github.howiefh.parser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.howiefh.parser.ArticleListParser;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.LinkType;
import io.github.howiefh.renderer.util.JsoupUtil;

public class ArticleListUrlsParserImpl implements ArticleListParser{

	private String[] urls;
	private String selector="";
	
	private String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36";
	private int connectionTimeout=10*1000;
	private int readTimeout =10*1000;
	public ArticleListUrlsParserImpl(){
		
	}
	public ArticleListUrlsParserImpl(String[] urls, String selector){
		this.urls = urls;
		this.selector = selector;
	}
	@Override
	public List<HtmlLink> articleLinkList() throws IOException {
		List<HtmlLink> articleLinks = new ArrayList<HtmlLink>();
		selector = selector + " a[href]";
		int len = urls.length;
		for (int i = 0; i < len; i++) {
			Document doc = JsoupUtil.get(urls[i], userAgent, connectionTimeout, readTimeout);
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
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
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
