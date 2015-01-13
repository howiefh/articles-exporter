package io.github.howiefh.conf;

import io.github.howiefh.util.LogUtil;
import io.github.howiefh.util.XmlUtil;

import java.io.FileNotFoundException;

public class ConfigLoader {
	private static final String CONFIG_PATH = "config.xml";
	
	private static boolean isLoadConfOK;
	private static XmlUtil xmlUtil;
	/**
	 * 加载配置文件
	 * @return
	 */
	public static boolean loadConfig() {
		isLoadConfOK = false;
		GeneralConfig gConfig = GeneralConfig.getInstance();
		try {
			xmlUtil = new XmlUtil(CONFIG_PATH);
			xmlUtil.read(gConfig);
			LogUtil.log().info(gConfig.toString());
		} catch (FileNotFoundException e) {
			isLoadConfOK = false;
		}
		
		//加载配置文件失败,由程序自己初始化参数
		if (!isLoadConfOK) {
			initConfig();
		}
		return isLoadConfOK;
	}
	
	private static void initConfig() {
		try {
			xmlUtil = new XmlUtil(CONFIG_PATH, true);
			xmlUtil.update(GeneralConfig.getInstance());
		} catch (FileNotFoundException e) {
			LogUtil.log().error(e.getMessage());
		}
	}
}
