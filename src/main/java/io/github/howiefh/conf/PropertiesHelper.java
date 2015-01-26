package io.github.howiefh.conf;


import io.github.howiefh.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class PropertiesHelper {
	
	public static Map<String, Rule> rules = new HashMap<String, Rule>();
	
	public static void load(){
		Collection<File> files = FileUtils.listFiles(new File(GeneralConfig.CONFIG_PATH), new String[]{"properties"}, false);
		Iterator<File> iterator = files.iterator();
		while (iterator.hasNext()) {
			String baseName = FilenameUtils.getBaseName(iterator.next().getName());
			rules.put(baseName, read(baseName));
		}
	}
	public static void store(String name){
		write(name, rules.get(name));
	}
	public static Rule read(String name) {
		Rule rule = new PropertiesHelper().new Rule();
		try {
			Properties properties = new Properties();
			InputStream inputStream = FileUtils.openInputStream(new File(GeneralConfig.CONFIG_PATH + name + ".properties"));
			properties.load(inputStream);
			rule.setListTitleSelector(properties.getProperty("list.title.selector"));
			rule.setArticleTitleSelector(properties.getProperty("article.title.selector"));
			rule.setArticleContentSelector(properties.getProperty("article.content.selector"));
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		}
		return rule;
	}
	
	public static void write(String name,Rule rule) {
		try {
			Properties properties = new Properties();
			OutputStream outputStream = FileUtils.openOutputStream(new File(GeneralConfig.CONFIG_PATH + name + ".properties"));
			properties.setProperty("list.title.selector", rule.getListTitleSelector());
			properties.setProperty("article.title.selector", rule.getArticleTitleSelector());
			properties.setProperty("article.content.selector", rule.getArticleContentSelector());
			properties.store(outputStream, "");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.log().error(e.getMessage());
		}
	}
	public static String getListTitleSelector(String ruleName) {
		return rules.get(ruleName).getListTitleSelector();
	}
	public static String getArticleTitleSelector(String ruleName) {
		return rules.get(ruleName).getArticleTitleSelector();
	}
	public static String getArticleContentSelector(String ruleName) {
		return rules.get(ruleName).getArticleContentSelector();
	}
	public class Rule {
		private String listTitleSelector;
		private String articleTitleSelector;
		private String articleContentSelector;
		
		public String getListTitleSelector() {
			return listTitleSelector;
		}
		public void setListTitleSelector(String listTitleSelector) {
			this.listTitleSelector = listTitleSelector;
		}
		public String getArticleTitleSelector() {
			return articleTitleSelector;
		}
		public void setArticleTitleSelector(String articleTitleSelector) {
			this.articleTitleSelector = articleTitleSelector;
		}
		public String getArticleContentSelector() {
			return articleContentSelector;
		}
		public void setArticleContentSelector(String articleContentSelector) {
			this.articleContentSelector = articleContentSelector;
		}
		
		@Override
		public String toString() {
			return "Rule [listTitleSelector=" + listTitleSelector
					+ ", articleTitleSelector=" + articleTitleSelector
					+ ", articleContentSelector=" + articleContentSelector+"]";
		}
	}
}
