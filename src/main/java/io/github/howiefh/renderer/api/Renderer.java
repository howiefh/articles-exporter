package io.github.howiefh.renderer.api;

import org.jsoup.nodes.Document;

public interface Renderer {
	/**
	 * 解析Document，对应类型的字符串
	 * @param doc
	 * @return
	 */
	String parse(Document doc);
	/**
	 * 解析Document，将解析后的内容写入文件filename中
	 * @param filepath
	 * @param doc
	 */
	void write(String filepath,Document doc, String encoding);
	/**
	 * 对应类型的存放目录
	 * @return
	 */
	String path();
	/**
	 * 对应类型文件的后缀
	 * @return
	 */
	String postfix();
}
