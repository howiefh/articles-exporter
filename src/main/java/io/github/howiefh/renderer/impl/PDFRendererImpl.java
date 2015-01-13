package io.github.howiefh.renderer.impl;

import io.github.howiefh.renderer.api.Renderer;
import io.github.howiefh.renderer.util.JsoupUtil;
import io.github.howiefh.util.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.Parser;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class PDFRendererImpl implements Renderer{
	public static final String PDF_PATH = "pdf/";
	public static final String PDF_POSTFIX= ".pdf";
	
	private ITextRenderer renderer;
	private ITextFontResolver fontResolver;
	private String baseURL;

	private static PDFRendererImpl instance;
	private PDFRendererImpl(){
        try {
        	// 费时
			renderer = new ITextRenderer();  
			
	        // 解决中文支持问题       
	        fontResolver = renderer.getFontResolver();      
	        File fontFile = new File("fonts/simsun.ttc");
			fontResolver.addFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			LogUtil.log().error(e.getMessage());
		} catch (IOException e) {
			LogUtil.log().error(e.getMessage());
		}   
	}
	
	public static Renderer getInstance() {
		if (null==instance) {
			instance = new PDFRendererImpl(); 
		}
		return instance;
	}
	
	@Override
	public String parse(Document doc) {
		doc = Parser.xmlParser().parseInput(doc.toString(), "");
		JsoupUtil.removeFontFamily(doc);
		//title或alt属性含有的某些字符如‘<’会导致抛出异常，例如
		//<a title="Permanent Links to 关于<hr />在各浏览器中的问题" href="http://sofish.de/1328">关于&lt;hr /&gt;在各浏览器中的问题</a>
		JsoupUtil.removeTipAttr(doc);
		// Adjust escape mode escape gt, lt ...
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		return doc.toString();
	}
	@Override
	public void write(String filepath,Document doc, String encoding) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			org.w3c.dom.Document pdfdoc = builder.parse(new ByteArrayInputStream(parse(doc)
					.getBytes(encoding)));
			
			File file = new File(filepath);
			if (file != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			OutputStream os = new FileOutputStream(filepath);       

	        renderer.setDocument(pdfdoc,null);
		    // 解决图片的相对路径问题  
	        renderer.getSharedContext().setBaseURL(baseURL);  
	        renderer.layout();      
	        renderer.createPDF(os);
	  
	        os.flush();  
	        os.close();  
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		} catch (DocumentException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		}
	}
	

	@Override
	public String path() {
		return PDF_PATH;
	}

	@Override
	public String postfix() {
		return PDF_POSTFIX;
	}
	

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}	
}
