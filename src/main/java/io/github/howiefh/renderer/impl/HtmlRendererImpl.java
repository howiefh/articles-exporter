package io.github.howiefh.renderer.impl;

import io.github.howiefh.renderer.api.Renderer;
import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

public class HtmlRendererImpl implements Renderer{
	public static final String HTML_PATH = "html/";
	public static final String HTML_POSTFIX= ".html";

	private static HtmlRendererImpl instance;
	private HtmlRendererImpl(){}
	
	public static Renderer getInstance() {
		if (null==instance) {
			instance = new HtmlRendererImpl(); 
		}
		return instance;
	}
	
	@Override
	public String parse(Document doc) {
		return doc.toString();
	}
	@Override
	public void write(String filepath,Document doc, String encoding) {
		try {
			File file = new File(filepath);
			FileUtils.writeStringToFile(file, parse(doc), encoding);
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage());
		}
	}

	@Override
	public String path() {
		return HTML_PATH;
	}

	@Override
	public String postfix() {
		return HTML_POSTFIX;
	}
}
