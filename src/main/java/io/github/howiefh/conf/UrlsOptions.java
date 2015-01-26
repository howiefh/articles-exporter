package io.github.howiefh.conf;

public class UrlsOptions extends BasicOptions{
	private String[] urls;
	private static UrlsOptions instance;
	public static UrlsOptions getInstance() {
		if (null==instance) {
			instance = new UrlsOptions();
		}
		return instance;
	}
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
}
