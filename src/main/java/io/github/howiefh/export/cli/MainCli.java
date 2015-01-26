package io.github.howiefh.export.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import io.github.howiefh.conf.Config;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.conf.GeneralOptions;
import io.github.howiefh.conf.PropertiesHelper;
import io.github.howiefh.conf.RendererRegister;
import io.github.howiefh.conf.RendererRegister.RendererTuple;
import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.util.LogUtil;

public class MainCli {
	public static void export(String[] args) {
		try {
			ArticleExporter.initRegisters();
			ArticleExporter articleExporter = new ArticleExporter(GeneralOptions.getInstance());
			Config[] configs = {GeneralConfig.getInstance()};
			ArticleExporter.initConfig(configs);
			if (initOptions(args)) {
				articleExporter.process(ArticleExporter.articleList());
			}
		} catch (Exception e) {
			LogUtil.log().error(e.getMessage());
		}
	}
	private static boolean initOptions(String[] args) {
		// create the command line parser
		CommandLineParser parser = new BasicParser();

		// create the Options
		Options options = new Options();

		options.addOption("h", "help", false, "帮助");
		options.addOption("l", "link", true,
				"链接，页码使用%s代替，例如：http://example.com/p/%s");
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
	
}
