package io.github.howiefh.parser.impl;

import io.github.howiefh.conf.GeneralOptions;
import io.github.howiefh.parser.ArticleListParser;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.LinkType;
import io.github.howiefh.renderer.util.JsoupUtil;
import io.github.howiefh.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlArticleListParserImpl implements ArticleListParser{
	private int startPage = 1;
	private int pageCount = 1;
	private String selector="";
	public HtmlArticleListParserImpl(){
		
	}
	public HtmlArticleListParserImpl(int startPage,int pageCount,String selector){
		this.startPage = startPage;
		this.pageCount = pageCount;
		this.selector = selector;
	}
	@Override
	public List<HtmlLink> articleLinkList() {
		List<HtmlLink> articleLinks = new ArrayList<HtmlLink>();
		selector = selector + " a[href]";
		for (int i = startPage; i <= pageCount ; i++) {
			Document doc = getListPageDocument(i);
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
	@Override
	public Element articleLinkListDocument(List<HtmlLink> links) {
		Document document = Jsoup.parse("<ol></ol>");
		Element element = document.select("ol").first();
		for (HtmlLink htmlLink : links) {
			element.append("<li><a href='#"+htmlLink.getUuid()+"'>"+htmlLink.getContent()+"</a></li>");
		}
		return element;
	}
	
	public String generateBookmark(List<HtmlLink> links) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<bookmarks>");
		for (HtmlLink htmlLink : links) {
			stringBuilder.append("<bookmark name=\"");
			stringBuilder.append(htmlLink.getContent() + "\"");
			stringBuilder.append(" href=\"#");
			stringBuilder.append(htmlLink.getUuid()+"\"/>");
		}
		stringBuilder.append("</bookmarks>");
		return stringBuilder.toString();
	}
	/**
	 * 获取文章列表页面的Document
	 * @param pageIndex
	 * @return
	 */
	private Document getListPageDocument(int pageIndex){
		try {
			String url = GeneralOptions.getInstance().getArticleListPageLink();
			String newUrl = String.format(url, pageIndex);
			Document doc = JsoupUtil.get(newUrl);
			return doc;
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage());
		}
		return null;
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
	
}
