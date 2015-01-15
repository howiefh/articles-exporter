package io.github.howiefh.export;

import io.github.howiefh.conf.*;
import io.github.howiefh.conf.RendererRegister.RendererTuple;
import io.github.howiefh.parser.ArticleListParser;
import io.github.howiefh.parser.impl.HtmlArticleListParserImpl;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.PreProcesser;
import io.github.howiefh.renderer.api.Renderer;
import io.github.howiefh.renderer.impl.HtmlRendererImpl;
import io.github.howiefh.renderer.impl.MarkdownRendererImpl;
import io.github.howiefh.renderer.impl.PDFRendererImpl;
import io.github.howiefh.renderer.impl.TextRendererImpl;
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

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class ArticleExporterCli {
	public static final String BEFORE_HTML= "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
			+ "<title></title>"
			+ "<style type=\"text/css\">"
			+ "body {font-family: SimSun;}"
			+ "</style>"
			+ "</head>"
			+ "<body>";
	public static final String AFTER_HTML="</body>"
			+ "</html>";
	private static String encoding  = "UTF-8";
	private static ExecutorService executor = Executors.newFixedThreadPool(GeneralConfig.threads);
	private static ArticleListParser htmlArticleListParserImpl;
	private static File tempDir;
	private static List<HtmlLink> htmlLinks = new ArrayList<HtmlLink>();
	private static Map<UUID, String> articles = new HashMap<UUID, String>();
	
	public static void export(String[] args) {
		try {
			Config[] configs = {GeneralConfig.getInstance()};
			initConfig(configs);
			initParsers();
			if (initOptions(args)) {
				articleList();
				deleteTemp();
				process();
			}
			executor.shutdown();
		} catch (Exception e) {
			LogUtil.log().error(e.getMessage());
		}
	}

	public static void initConfig(Config[] configs) {
		ConfigLoader.loadConfig(configs);
		PropertiesHelper.load();
	}
	
	private static void initParsers() {
		RendererRegister.register("html", HtmlRendererImpl.getInstance(),false);
		((MarkdownRendererImpl)MarkdownRendererImpl.getInstance()).setMarkdown(GeneralConfig.markdown);
		RendererRegister.register("md", MarkdownRendererImpl.getInstance(),false);
		RendererRegister.register("text", TextRendererImpl.getInstance(),false);
		RendererRegister.register("pdf", PDFRendererImpl.getInstance());
		RendererRegister.register("shtml", HtmlRendererImpl.getInstance());
		RendererRegister.register("smd", MarkdownRendererImpl.getInstance());
		RendererRegister.register("stext", TextRendererImpl.getInstance());
	}

	private static boolean initOptions(String[] args) {
		// create the command line parser
		CommandLineParser parser = new BasicParser();

		// create the Options
		Options options = new Options();

		options.addOption("h", "help", false, "帮助");
		options.addOption("l", "link", true,
				"链接，页码使用%d代替，例如：http://example.com/p/%d");
		options.addOption("r", "rule", true, "规则,位于conf目录下的文件名");
		options.addOption("s", "start-page", true, "起始页码");
		options.addOption("c", "page-count", true, "总页数");
		options.addOption("t", "type", true, "导出的文件类型，现在支持:"+RendererRegister.types()+"使用英文逗号分隔，中间不要有空格");
		options.addOption("o", "out", true, "输出文件夹");

		CommandLine cl;
		try {
			cl = parser.parse(options, args);
			if (cl.getOptions().length > 0) {
				return parseOptions(cl, options);
			} else {
				System.err.println("请输入参数");
				System.exit(1);
			}
		} catch (ParseException e) {
			LogUtil.log().error(e.getMessage());
		}
		return false;
	}
	private static boolean parseOptions(CommandLine cl, Options options) {
		if (cl.hasOption('h')) {
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("articles-exporter [-l string] [-r string] [-s number] [-c number] \n <-t string> <-o string>", options);
			return false;
		} else {
			//解析起始页数参数
			String optionValue = cl.getOptionValue("s");
			if (StringUtils.isNumeric(optionValue)) {
				GeneralOptions.getInstance().setStartPage(Integer.valueOf(optionValue));
			} else {
				System.err.println("起始页数应该是整数");
				return false;
			}
			//解析总页数参数
			optionValue = cl.getOptionValue("c");
			if (StringUtils.isNumeric(optionValue)) {
				GeneralOptions.getInstance().setPageCount(Integer.valueOf(optionValue));
			} else {
				System.err.println("页码总数应该是整数");
				return false;
			}
			//解析生成规则参数
			optionValue = cl.getOptionValue("r");
			if (PropertiesHelper.rules.containsKey(optionValue)) {
				GeneralOptions.getInstance().setRuleName(cl.getOptionValue("r"));
			} else {
				System.err.println("没有此规则");
				return false;
			}
			//解析链接
			optionValue = cl.getOptionValue("l");
			if (null!=optionValue) {
				GeneralOptions.getInstance().setArticleListPageLink(cl.getOptionValue("l"));
			} else {
				System.err.println("链接不能为空");
				return false;
			}
			//解析输出目录
			optionValue = cl.getOptionValue("o");
			if (null!=optionValue) {
				GeneralOptions.getInstance().setOutDir(FilenameUtils.concat(optionValue, "./"));
			} 
			//解析生成文件类型
			optionValue = cl.getOptionValue("t");
			List<RendererTuple> list = new ArrayList<RendererTuple>();
			if (null!=optionValue) {
				String[] types = StringUtils.split(optionValue, ',');
				for (int i = 0; i < types.length; i++) {
					if (RendererRegister.contains(types[i])) {
						list.add(RendererRegister.getRendererTuple(types[i]));
					} else {
						System.err.println("抱歉，暂不支持此类型:"+types[i]);
					}
				}
			}
			//用户设置的导出的文件类型都不支持，或者没有设置导出类型，将默认导出html
			if (0==list.size()) {
				list.add(RendererRegister.getRendererTuple("html"));
			}
			GeneralOptions.getInstance().setRendererTuples(list);
		}
		return true;
	}
	
	public static List<HtmlLink> articleList() throws IOException {
		//获取列表
		htmlArticleListParserImpl = new HtmlArticleListParserImpl(
				GeneralOptions.getInstance().getStartPage(),
				GeneralOptions.getInstance().getPageCount(),
				PropertiesHelper.getListTitleSelector()
				);
		htmlLinks = htmlArticleListParserImpl.articleLinkList();
		return htmlLinks;
	}

	public static void deleteTemp() {
		//删除临时文件
		tempDir = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),GeneralConfig.TEMP_PATH));
		if (tempDir.exists()) {
			try {
				FileUtils.cleanDirectory(tempDir);
			} catch (IOException e) {
				LogUtil.log().error("删除目录{}失败.{}",tempDir,e.getMessage());
			}
		}
	}

	public static void process() {
		long start = new Date().getTime();
		saveToMultifiles();
		saveToSingleFileAndCopyResources();
		long end = new Date().getTime();
		LogUtil.log().info("执行用时(单位：毫秒) "+(end-start));
	}

	private static void saveToMultifiles() {
		//一篇文章导出到一个文件
		for (HtmlLink htmlLink : htmlLinks) {
			execute(htmlLink);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
        LogUtil.log().info("线程池所有任务执行完成");
	}
	private static void execute(final HtmlLink htmlLink) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				saveToFile(htmlLink);
			}
		};
		executor.execute(runnable);
	}
	private static void saveToFile(HtmlLink htmlLink) {
		Document doc = preProcess(htmlLink);
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
	}
	private static Document preProcess(HtmlLink htmlLink) {
		try {
			PreProcesser preProcesser = new PreProcesser();
			preProcesser.setSelector(PropertiesHelper.getArticleContentSelector());
			preProcesser.setConnectionTimeout(GeneralConfig.connectionTimeout);
			preProcesser.setReadTimeout(GeneralConfig.readTimeout);
			preProcesser.setMediaPath(GeneralConfig.mediaPath);
			preProcesser.setCssPath(GeneralConfig.cssPath);
			preProcesser.setJsPath(GeneralConfig.jsPath);
			preProcesser.setTempPath(tempDir.getAbsolutePath());;
			return preProcesser.process(htmlLink);
		} catch (Exception e) {
			LogUtil.log().error("文档预处理失败."+e.getMessage());
		}
		return null;
	}
	private static void saveToSingleFileAndCopyResources() {
		//后续处理,对生成单文件的生成单文件，拷贝资源文件
		for (RendererTuple rendererTuple : GeneralOptions.getInstance().getRendererTuples()) {
			if (rendererTuple.isSingleFile) {
				String fileName = "out";
				String filePath = FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),
						rendererTuple.renderer.path()+fileName+rendererTuple.renderer.postfix());
				
				Element element = htmlArticleListParserImpl.articleLinkListDocument(htmlLinks);
				StringBuilder res = new StringBuilder();
				res.append(BEFORE_HTML);
				res.append(element.toString());
				for (HtmlLink htmlLink : htmlLinks) {
					res.append(articles.get(htmlLink.getUuid()));
				}
				res.append(AFTER_HTML);
				
				Document document = Jsoup.parse(res.toString());
				document.title(fileName);
				
				if (rendererTuple.renderer instanceof PDFRendererImpl) {
					File pdfBaseURLPath = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(), GeneralConfig.TEMP_PATH)); 
					((PDFRendererImpl)rendererTuple.renderer).setBaseURL(pdfBaseURLPath.toURI().toString());
					document.select("meta").remove();
					document.head().append(((HtmlArticleListParserImpl)htmlArticleListParserImpl).generateBookmark(htmlLinks));
					write(rendererTuple.renderer, document, filePath, encoding);
					document.head().prepend("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
				} else {
					write(rendererTuple.renderer, document, filePath, encoding);
				}
				continue;
			}
			IOUtil.copyDirectory(tempDir.getAbsolutePath(),
					FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),rendererTuple.renderer.path()));
		}
	}
	private static void write(Renderer renderer, Document document, String filePath, String encoding) {
		renderer.write(filePath,document, encoding);
		LogUtil.log().info(filePath);
	}
}
