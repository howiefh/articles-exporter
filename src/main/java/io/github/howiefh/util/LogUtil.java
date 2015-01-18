package io.github.howiefh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	private static Logger logger = init();
	
	private static Logger init() {
		System.setProperty("log4j.configurationFile",IOUtil.getResource("log4j2.xml").toString());
		return LoggerFactory.getLogger("");
	}
	public static Logger log() {
		return logger;
	}
}
