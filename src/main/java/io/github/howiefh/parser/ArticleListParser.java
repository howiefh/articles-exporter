package io.github.howiefh.parser;

import io.github.howiefh.renderer.HtmlLink;

import java.io.IOException;
import java.util.List;

public interface ArticleListParser {
	/**
	 * 获取文章链接链表
	 * @return
	 * @throws IOException 
	 */
	List<HtmlLink> articleLinkList() throws IOException; 
}
