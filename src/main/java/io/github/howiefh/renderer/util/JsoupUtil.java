package io.github.howiefh.renderer.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {
	public static Document get(String url, String userAgent, int timeout) throws IOException {
		return Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();
	}
	public static void removeFontFamily(Document doc) {
		Elements elements = doc.select("[style]");
		for (Element element : elements) {
			//加？是非贪婪匹配，最短匹配
			element.attr("style",element.attr("style").replaceAll("font-family:.*?;|font-family:.*$", ""));
		}
		doc.select("font[face]").removeAttr("face");
	}
	public static void removeTipAttr(Document doc) {
		doc.select("[alt]").removeAttr("alt");
		doc.select("[title]").removeAttr("title");
	}
}
