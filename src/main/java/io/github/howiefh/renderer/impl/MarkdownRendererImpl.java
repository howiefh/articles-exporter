package io.github.howiefh.renderer.impl;

import io.github.howiefh.renderer.Markdown;
import io.github.howiefh.renderer.api.Renderer;
import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import com.overzealous.remark.Options;
import com.overzealous.remark.convert.DocumentConverter;

public class MarkdownRendererImpl implements Renderer{
	public static final String MARKDOWN_PATH = "markdown/";
	public static final String MARKDOWN_POSTFIX= ".md";

	private Markdown markdown;
	private static MarkdownRendererImpl instance;
	private MarkdownRendererImpl(){}
	
	public static Renderer getInstance() {
		if (null==instance) {
			instance = new MarkdownRendererImpl(); 
		}
		return instance;
	}
	
	@Override
	public String parse(Document doc) {
		Options options=null;
		switch (markdown) {
		case MARKDOWN:
			options = Options.markdown();
			break;
		case PHP_MARKDOWN_EXTRA:
			options = Options.markdownExtra();
			break;
		case MULTI_MARKDOWN:
			options = Options.multiMarkdown();
			break;
		case PEGDOWN_ALL_EXTENSIONS:
			options = Options.pegdownAllExtensions();
			break;
		case PEGDOWN_BASE:
			options = Options.pegdownBase();
			break;
		case GITHUB:
		default:
			options = Options.github();
			break;
		}
		DocumentConverter converter = new DocumentConverter(options);
		return converter.convert(doc);
	}
	@Override
	public void write(String filepath,Document doc, String encoding) throws Exception{
		try {
			File file = new File(filepath);
			FileUtils.writeStringToFile(file, parse(doc), encoding);
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage());
			throw e;
		}
	}

	@Override
	public String path() {
		return MARKDOWN_PATH;
	}

	@Override
	public String postfix() {
		return MARKDOWN_POSTFIX;
	}

	public Markdown getMarkdown() {
		return markdown;
	}

	public void setMarkdown(Markdown markdown) {
		this.markdown = markdown;
	}
}
