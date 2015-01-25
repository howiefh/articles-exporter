package io.github.howiefh.conf;

import io.github.howiefh.renderer.Markdown;
import io.github.howiefh.util.IOUtil;

public class GeneralConfigValidate {

	public static boolean validateUserAgent(String name) {
		return UserAgentsRegister.contains(name);
	}
	public static boolean validateReadTimeout(int readTimeout) {
		return readTimeout<=GeneralConfig.MAX_READ_TIMEOUT&&readTimeout>=GeneralConfig.MIN_READ_TIMEOUT;
	}
	public static boolean validateConnectionTimeout(int connectionTimeout) {
		return connectionTimeout<=GeneralConfig.MAX_CONNECTION_TIMEOUT&&connectionTimeout>=GeneralConfig.MIN_CONNECTION_TIMEOUT;
	}
	public static boolean validateCssPath(String css) {
		return validateFilename(css);
	}
	public static boolean validateJsPath(String js) {
		return validateFilename(js);
	}
	public static boolean validateMediaPath(String media) {
		return validateFilename(media);
	}
	private static boolean validateFilename(String filename) {
		return !filename.isEmpty()&&IOUtil.validFileName(filename);
	}
	public static boolean validateMarkdown(Markdown markdown) {
		for (Markdown md: Markdown.values()) {
			if (md.equals(markdown)) {
				return true;
			}
		}
		return false;
	}
	public static boolean validateThreads(int threads) {
		return threads<=GeneralConfig.MAX_THREADS&&threads>=GeneralConfig.MIN_THREADS;
	}
}
