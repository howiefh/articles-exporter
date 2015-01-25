package io.github.howiefh.conf;

import io.github.howiefh.util.LogUtil;
import io.github.howiefh.util.XmlUtil;

import java.io.FileNotFoundException;

public class ConfigUtil {
	private static final String CONFIG_PATH = "config.xml";
	
	private static boolean isLoadConfOK;
	private static XmlUtil xmlUtil;
	/**
	 * 加载配置文件
	 * @return
	 */
	public static boolean loadConfig(Config[] configs) {
		isLoadConfOK = false;
		try {
			xmlUtil = new XmlUtil(CONFIG_PATH);
			for (Config config : configs) {
				try {
					xmlUtil.read(config);
					LogUtil.log().info(config.toString());
					if (!config.validate()) {
						initConfig(config);
					}
				} catch (Exception e) {
					initConfig(config);
				}
			}
			isLoadConfOK = true;
		} catch (FileNotFoundException e) {
			LogUtil.log().error(e.getMessage());
		}
		//加载配置文件失败,由程序自己初始化参数
		if (!isLoadConfOK) {
			saveConfig(configs);
		}
		return isLoadConfOK;
	}
	
	public static void initConfig(Config config) {
		try {
			config.init();
			xmlUtil = new XmlUtil(CONFIG_PATH, true);
			xmlUtil.update(config);
		} catch (FileNotFoundException e) {
			LogUtil.log().error(e.getMessage());
		}
	}
	public static void saveConfig(Config[] configs) {
		try {
			for (Config config : configs) {
				xmlUtil = new XmlUtil(CONFIG_PATH, true);
				xmlUtil.update(config);
			}
		} catch (FileNotFoundException e) {
			LogUtil.log().error(e.getMessage());
		}
	}
}
