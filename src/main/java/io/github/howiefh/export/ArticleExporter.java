package io.github.howiefh.export;

import io.github.howiefh.conf.*;
import io.github.howiefh.conf.RendererRegister.RendererTuple;
import io.github.howiefh.parser.impl.HtmlArticleListParserImpl;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.PreProcesser;
import io.github.howiefh.renderer.api.Renderer;
import io.github.howiefh.renderer.impl.*;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ArticleExporter {
	public final static String BEFORE_HTML= "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
			+ "<title></title>"
			+ "<style type=\"text/css\">"
			+ "body {font-family: SimSun;}"
			+ "</style>"
			+ "</head>"
			+ "<body>";
	public final static String AFTER_HTML="</body>"
			+ "</html>";
	public final static String HTML = "html";
	public final static String MARKDOWN = "md";
	public final static String TEXT = "text";
	public final static String PDF = "pdf";
	public final static String SINGLE_HTML = "shtml";
	public final static String SINGLE_MARKDOWN = "smd";
	public final static String SINGLE_TEXT = "stext";
	public final static String SINGLE_FILE_NAME = "out";
	
	private String encoding  = "UTF-8";
	private ExecutorService executor = Executors.newFixedThreadPool(GeneralConfig.threads);
	private File tempDir;
	private List<HtmlLink> htmlLinks = new ArrayList<HtmlLink>();
	private List<HtmlLink> errorLinks= new ArrayList<HtmlLink>();
	private Map<UUID, String> articles = new HashMap<UUID, String>();
	
	private static Message message = new DefaultMessage();
	private static Result result = new DefaultResult();
	
	private static class DefaultResult implements Result{

		@Override
		public void result(int index, String msg) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private static class DefaultMessage implements Message{

		@Override
		public void warn(String text) {
			// TODO Auto-generated method stub
		}

		@Override
		public void info(String text) {
			// TODO Auto-generated method stub
		}

		@Override
		public void error(String text) {
			// TODO Auto-generated method stub
		}
		
	}
	
	public ArticleExporter(){
		
	}
	public ArticleExporter(Message message){
		ArticleExporter.message = message;
	}

	public static void initConfig(Config[] configs) {
		ConfigUtil.loadConfig(configs);
		PropertiesHelper.load();
	}
	
	public static void initRegisters() {
		RendererRegister.register(HTML, HtmlRendererImpl.getInstance(),false);
		((MarkdownRendererImpl)MarkdownRendererImpl.getInstance()).setMarkdown(GeneralConfig.markdown);
		RendererRegister.register(MARKDOWN, MarkdownRendererImpl.getInstance(),false);
		RendererRegister.register(TEXT, TextRendererImpl.getInstance(),false);
		RendererRegister.register(PDF, PDFRendererImpl.getInstance());
		RendererRegister.register(SINGLE_HTML, HtmlRendererImpl.getInstance());
		RendererRegister.register(SINGLE_MARKDOWN, MarkdownRendererImpl.getInstance());
		RendererRegister.register(SINGLE_TEXT, TextRendererImpl.getInstance());
		
		UserAgentsRegister.register("PC", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36");
		UserAgentsRegister.register("Android", "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-cn; MI 2 Build/JRO03L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36 XiaoMi/MiuiBrowser/2.1.1");
		UserAgentsRegister.register("UC", "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-CN; MI 2 Build/JRO03L) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30");
		UserAgentsRegister.register("iPhone", "mozilla/5.0 (iphone; cpu iphone os 7_0_2 like mac os x) applewebkit/537.51.1 (khtml, like gecko) version/7.0 mobile/11a501 safari/9537.53");
	}


	public static List<HtmlLink> articleList() throws Exception {
		//获取列表
		try {
			HtmlArticleListParserImpl htmlArticleListParserImpl = new HtmlArticleListParserImpl(
					GeneralOptions.getInstance().getStartPage(),
					GeneralOptions.getInstance().getPageCount(),
					PropertiesHelper.getListTitleSelector()
					);
			htmlArticleListParserImpl.setConnectionTimeout(GeneralConfig.connectionTimeout);
			htmlArticleListParserImpl.setReadTimeout(GeneralConfig.readTimeout);
			htmlArticleListParserImpl.setUserAgent(GeneralConfig.userAgent);
			message.info("获取文章列表...");
			List<HtmlLink> links = htmlArticleListParserImpl.articleLinkList();
			message.info("共计"+links.size()+"篇文章");
			return links;
		} catch (Exception e) {
			LogUtil.log().error("获取文章列表失败:"+e.getMessage());
			message.error("获取文章列表失败:"+e.getMessage());
			throw e;
		}
	}

	private void deleteTemp() {
		//删除临时文件
		tempDir = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),GeneralConfig.TEMP_PATH));
		if (tempDir.exists()) {
			try {
				FileUtils.cleanDirectory(tempDir);
			} catch (IOException e) {
				LogUtil.log().error("删除目录"+tempDir+"失败."+e.getMessage());
				message.error("删除目录"+tempDir+"失败."+e.getMessage());
			}
		}
	}

	public  void process(List<HtmlLink> links) {
		long start = new Date().getTime();
		this.htmlLinks = links;
		LogUtil.log().info("将导出"+links.size()+"篇文章");
		message.info("将导出"+links.size()+"篇文章");
		deleteTemp();
		saveToMultifiles();
		saveToSingleFileAndCopyResources();
		long end = new Date().getTime();
		int error = errorLinks.size();
		int success = links.size()-error;
		LogUtil.log().info("任务完成，导出"+success+"篇，"+(error==0?"":error+"篇失败，")+"用时："+(end-start)+"毫秒");
		message.info("任务完成，导出"+success+"篇，"+(error==0?"":error+"篇失败，")+"用时："+(end-start)+"毫秒");
	}

	private  void saveToMultifiles() {
		//一篇文章导出到一个文件
		int len = htmlLinks.size();
		for (int i = 0; i < len; i++) {
			execute(htmlLinks.get(i), i);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
	}
	private  void execute(final HtmlLink htmlLink, final int index) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					saveToFile(htmlLink);
					result.result(index, "单篇导出成功");
				} catch (Exception e) {
					result.result(index, "导出失败");
				}
			}
		};
		executor.execute(runnable);
	}
	private  void saveToFile(HtmlLink htmlLink) throws Exception{
		try {
			Document doc = preProcess(htmlLink);
			if (doc == null) {
				throw new NullPointerException("预处理失败");
			}
			boolean isSingleFileAdded = false;
			for (RendererTuple rendererTuple : GeneralOptions.getInstance().getRendererTuples()) {
				if (rendererTuple.isSingleFile) {
					if (!isSingleFileAdded) {
						articles.put(htmlLink.getUuid(), doc.body().html());
						isSingleFileAdded = true;
					}
					continue;
				}
				String filePath = FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),
						rendererTuple.renderer.path()+IOUtil.cleanInvalidFileName(htmlLink.getContent())+rendererTuple.renderer.postfix());
				write(rendererTuple.renderer, doc, filePath, encoding);
			}
		} catch (Exception e) {
			LogUtil.log().error(e.getMessage());
			throw e;
		}
	}
	private  Document preProcess(HtmlLink htmlLink) {
		try {
			PreProcesser preProcesser = new PreProcesser();
			preProcesser.setSelector(PropertiesHelper.getArticleContentSelector());
			preProcesser.setConnectionTimeout(GeneralConfig.connectionTimeout);
			preProcesser.setReadTimeout(GeneralConfig.readTimeout);
			preProcesser.setMediaPath(GeneralConfig.mediaPath);
			preProcesser.setCssPath(GeneralConfig.cssPath);
			preProcesser.setJsPath(GeneralConfig.jsPath);
			preProcesser.setTempPath(tempDir.getAbsolutePath());;
			preProcesser.setUserAgent(UserAgentsRegister.getUserAgent(GeneralConfig.userAgent));
			return preProcesser.process(htmlLink);
		} catch (Exception e) {
			LogUtil.log().error("文档预处理失败."+e.getMessage());
			message.error("文档预处理失败."+e.getMessage());
			synchronized (errorLinks) {
				errorLinks.add(htmlLink);
			}
		}
		return null;
	}
	private  void saveToSingleFileAndCopyResources() {
		//后续处理,对生成单文件的生成单文件，拷贝资源文件
		int size = htmlLinks.size();
		for (RendererTuple rendererTuple : GeneralOptions.getInstance().getRendererTuples()) {
			try {
				if (rendererTuple.isSingleFile) {
					String filePath = FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),
							rendererTuple.renderer.path()+SINGLE_FILE_NAME+rendererTuple.renderer.postfix());
					
					Element element = articleLinkListDocument(htmlLinks);
					StringBuilder res = new StringBuilder();
					res.append(BEFORE_HTML);
					res.append(element.toString());
					for (HtmlLink htmlLink : htmlLinks) {
						res.append(articles.get(htmlLink.getUuid()));
					}
					res.append(AFTER_HTML);
					
					Document document = Jsoup.parse(res.toString());
					document.title(SINGLE_FILE_NAME);
					
					if (rendererTuple.renderer instanceof PDFRendererImpl) {
						File pdfBaseURLPath = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(), GeneralConfig.TEMP_PATH)); 
						((PDFRendererImpl)rendererTuple.renderer).setBaseURL(pdfBaseURLPath.toURI().toString());
						document.select("meta").remove();
						document.head().append(generateBookmark(htmlLinks));
						write(rendererTuple.renderer, document, filePath, encoding);
						document.head().prepend("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
					} else {
						write(rendererTuple.renderer, document, filePath, encoding);
					}
					result.result(size++,rendererTuple.renderer.postfix() + "文件导出成功");
					continue;
				}
				IOUtil.copyDirectory(tempDir.getAbsolutePath(),
						FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),rendererTuple.renderer.path()));
			} catch (Exception e) {
				LogUtil.log().error(e.getMessage());
				result.result(size++,rendererTuple.renderer.postfix() + "文件导出失败");
			}
		}
	}
	/**
	 * 返回文章链接链表Element
	 * @param links
	 * @return
	 */
	public static Element articleLinkListDocument(List<HtmlLink> links) {
		Document document = Jsoup.parse("<ol></ol>");
		Element element = document.select("ol").first();
		for (HtmlLink htmlLink : links) {
			element.append("<li><a href='#"+htmlLink.getUuid()+"'>"+htmlLink.getContent()+"</a></li>");
		}
		return element;
	}
	
	public static String generateBookmark(List<HtmlLink> links) {
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
	private  void write(Renderer renderer, Document document, String filePath, String encoding) {
		try {
			renderer.write(filePath,document, encoding);
			LogUtil.log().info(filePath);
			message.info(filePath);
		} catch (Exception e) {
			LogUtil.log().error(e.getMessage());
			message.error(e.getMessage());
		}
	}

	public static Message getMessage() {
		return message;
	}

	public static void setMessage(Message message) {
		ArticleExporter.message = message;
	}
	public static Result getResult() {
		return result;
	}
	public static void setResult(Result result) {
		ArticleExporter.result = result;
	}
}
