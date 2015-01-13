package io.github.howiefh.parser;

import io.github.howiefh.renderer.HtmlLink;

import java.util.List;

import org.jsoup.nodes.Element;

public interface ArticleListParser {
	/**
	 * 获取文章链接链表
	 * @return
	 */
	List<HtmlLink> articleLinkList(); 
	/**
	 * 返回文章链接链表Element
	 * @param links
	 * @return
	 */
	Element articleLinkListDocument(List<HtmlLink> links); 
}
