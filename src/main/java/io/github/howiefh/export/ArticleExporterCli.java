package io.github.howiefh.export;

import io.github.howiefh.conf.ConfigLoader;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.conf.GeneralOptions;
import io.github.howiefh.conf.RendererRegister;
import io.github.howiefh.conf.PropertiesHelper;
import io.github.howiefh.conf.RendererRegister.RendererTuple;
import io.github.howiefh.parser.ArticleListParser;
import io.github.howiefh.parser.impl.HtmlArticleListParserImpl;
import io.github.howiefh.renderer.HtmlLink;
import io.github.howiefh.renderer.PreProcesser;
import io.github.howiefh.renderer.impl.HtmlRendererImpl;
import io.github.howiefh.renderer.impl.MarkdownRendererImpl;
import io.github.howiefh.renderer.impl.PDFRendererImpl;
import io.github.howiefh.renderer.impl.TextRendererImpl;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			+ "<title></title>"
			+ "<style type=\"text/css\">"
			+ "body {font-family: SimSun;}"
			+ "</style>"
			+ "</head>"
			+ "<body>";
	public static final String AFTER_HTML="</body>"
			+ "</html>";
	private static void initConfig() {
		ConfigLoader.loadConfig();
		PropertiesHelper.load();
	}
	
	private static void initParsers() {
		RendererRegister.register("html", HtmlRendererImpl.getInstance(),false);
		((MarkdownRendererImpl)MarkdownRendererImpl.getInstance()).setMarkdown(GeneralConfig.markdown);
		RendererRegister.register("md", MarkdownRendererImpl.getInstance(),false);
		RendererRegister.register("text", TextRendererImpl.getInstance(),false);
		RendererRegister.register("pdf", PDFRendererImpl.getInstance());
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
				parseOptions(cl, options);
				return true;
			} else {
				System.err.println("请输入参数");
				System.exit(1);
			}
		} catch (ParseException e) {
			LogUtil.log().error(e.getMessage());
		}
		return false;
	}
	private static void parseOptions(CommandLine cl, Options options) {
		if (cl.hasOption('h')) {
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("ArticleExporter [-l string] [-r string] [-s number] [-c number] <-t string> <-o string>", options);
		} else {
			//解析起始页数参数
			String optionValue = cl.getOptionValue("s");
			if (StringUtils.isNumeric(optionValue)) {
				GeneralOptions.getInstance().setStartPage(Integer.valueOf(optionValue));
			} else {
				System.err.println("起始页数应该是整数");
				System.exit(1);
			}
			//解析总页数参数
			optionValue = cl.getOptionValue("c");
			if (StringUtils.isNumeric(optionValue)) {
				GeneralOptions.getInstance().setPageCount(Integer.valueOf(optionValue));
			} else {
				System.err.println("页码总数应该是整数");
				System.exit(1);
			}
			//解析生成规则参数
			optionValue = cl.getOptionValue("r");
			if (PropertiesHelper.rules.containsKey(optionValue)) {
				GeneralOptions.getInstance().setRuleName(cl.getOptionValue("r"));
			} else {
				System.err.println("没有此规则");
				System.exit(1);
			}
			//解析链接
			optionValue = cl.getOptionValue("l");
			if (null!=optionValue) {
				GeneralOptions.getInstance().setArticleListPageLink(cl.getOptionValue("l"));
			} else {
				System.err.println("链接不能为空");
				System.exit(1);
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
	}
	public static void process() {
		String encoding = "UTF-8";
		ArticleListParser htmlArticleListParserImpl = new HtmlArticleListParserImpl(
				GeneralOptions.getInstance().getStartPage(),
				GeneralOptions.getInstance().getPageCount(),
				PropertiesHelper.getListTitleSelector()
				);
		List<HtmlLink> htmlLinks = htmlArticleListParserImpl.articleLinkList();
		File tempDir = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),GeneralConfig.TEMP_PATH));
		if (tempDir.exists()) {
			try {
				FileUtils.cleanDirectory(tempDir);
			} catch (IOException e) {
				LogUtil.log().error("删除目录{}失败.{}",tempDir,e.getMessage());
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (HtmlLink htmlLink : htmlLinks) {
			PreProcesser preProcesser = new PreProcesser();
			preProcesser.setSelector(PropertiesHelper.getArticleContentSelector());
			preProcesser.setConnectionTimeout(GeneralConfig.connectionTimeout);
			preProcesser.setReadTimeout(GeneralConfig.readTimeout);
			preProcesser.setMediaPath(GeneralConfig.mediaPath);
			preProcesser.setCssPath(GeneralConfig.cssPath);
			preProcesser.setJsPath(GeneralConfig.jsPath);
			preProcesser.setTempPath(tempDir.getAbsolutePath());;
			Document doc = preProcesser.process(htmlLink);
			
			for (RendererTuple rendererTuple : GeneralOptions.getInstance().getRendererTuples()) {
				if (rendererTuple.isSingleFile) {
					stringBuilder.append(doc.body().html());
					continue;
				}
				String filePath = FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),
						rendererTuple.renderer.path()+IOUtil.cleanInvalidFileName(htmlLink.getContent())+rendererTuple.renderer.postfix());
				rendererTuple.renderer.write(filePath, doc, encoding);
				LogUtil.log().info(filePath);
			}
		}
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
				res.append(stringBuilder);
				res.append(AFTER_HTML);
				
				Document document = Jsoup.parse(res.toString());
				document.title(fileName);
				
				if (rendererTuple.renderer instanceof PDFRendererImpl) {
					File pdfBaseURLPath = new File(FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(), GeneralConfig.TEMP_PATH)); 
					((PDFRendererImpl)rendererTuple.renderer).setBaseURL(pdfBaseURLPath.toURI().toString());
					document.head().append(((HtmlArticleListParserImpl)htmlArticleListParserImpl).generateBookmark(htmlLinks));
				}
				rendererTuple.renderer.write(filePath,document, encoding);
				LogUtil.log().info(filePath);
				continue;
			}
			IOUtil.copyDirectory(tempDir.getAbsolutePath(),
					FilenameUtils.concat(GeneralOptions.getInstance().getOutDir(),rendererTuple.renderer.path()));
		}
	}

	public static void export(String[] args) {
		initConfig();
		initParsers();
		if (initOptions(args)) {
			process();
		}
	}
}
