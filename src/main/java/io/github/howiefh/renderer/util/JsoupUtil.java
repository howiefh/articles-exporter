package io.github.howiefh.renderer.util;

import io.github.howiefh.util.LogUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupUtil {
	public static Document get(String url, String userAgent, int connectionTimeout, int readTimeout)
			throws IOException {
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 5) {
					// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {
					// ssl握手异常
					return false;
				}
				HttpClientContext clientContext = HttpClientContext
						.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求是幂等的，就再次尝试
					return true;
				}
				return false;
			}

		};
		String html = null;
		// 创建httpClient对象
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRetryHandler(retryHandler).build();
		// 以get方式请求该URL
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", userAgent);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(readTimeout).build();//设置请求和传输超时时间
		httpget.setConfig(requestConfig);
        //得到responce对象
		CloseableHttpResponse responce = httpClient.execute(httpget);
		try {  
	        //返回码
	        int resStatu = responce.getStatusLine().getStatusCode();  
	        //200正常  其他就不对  
	        if (resStatu==HttpStatus.SC_OK) {
	            //获得相应实体  
	            HttpEntity entity = responce.getEntity();  
	            if (entity!=null) {  
	                //获得html源代码
	                html = EntityUtils.toString(entity);  
	                return Jsoup.parse(html, url);
	            }  
	        }  
	    } catch (IOException e) {  
	        LogUtil.log().error("访问["+url+"]出现异常!");
	        throw e;
	    } finally {  
	    	responce.close();
	    }  
		return null;
	}

	public static void removeFontFamily(Document doc) {
		Elements elements = doc.select("[style]");
		for (Element element : elements) {
			// 加？是非贪婪匹配，最短匹配
			element.attr(
					"style",
					element.attr("style").replaceAll(
							"font-family:.*?;|font-family:.*$", ""));
		}
		doc.select("font[face]").removeAttr("face");
	}

	public static void removeTipAttr(Document doc) {
		doc.select("[alt]").removeAttr("alt");
		doc.select("[title]").removeAttr("title");
	}
}
