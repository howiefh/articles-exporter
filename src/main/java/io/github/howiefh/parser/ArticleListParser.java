package io.github.howiefh.parser;

import io.github.howiefh.renderer.HtmlLink;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Element;

public interface ArticleListParser {
	/**
	 * 获取文章链接链表
	 * @return
	 * @throws IOException 
	 */
	List<HtmlLink> articleLinkList() throws IOException; 
	/**
	 * 返回文章链接链表Element
	 * @param links
	 * @return
	 */
	Element articleLinkListDocument(List<HtmlLink> links); 
}
