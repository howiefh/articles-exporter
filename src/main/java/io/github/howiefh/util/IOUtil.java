package io.github.howiefh.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class IOUtil {
	public static String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36";

	public static void copyURLToFile(String source,
            File destination,
            int connectionTimeout,
            int readTimeout)
     throws IOException{
		URL url = new URL(source);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(readTimeout);
		conn.setConnectTimeout(connectionTimeout);
		conn.addRequestProperty("User-Agent", userAgent);
		boolean redirect = false;
		
		// normally, 3xx is redirect
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
		}

		if (redirect) {
			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");
			// open the new connnection again
			conn = (HttpURLConnection) new URL(newUrl).openConnection();
			conn.addRequestProperty("User-Agent", userAgent);
		}

		BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
		FileUtils.copyInputStreamToFile(in, destination);
		in.close();
	}
	public static String getExtension(String filename){
		return FilenameUtils.getExtension(filename);
	}
	public static String getName(String filename){
		return FilenameUtils.getName(filename);
	}
	
	private static String[] InvalidFileNameChars = new String[]{
		"/","\\",":","*","?","\"","<",">","|"," "
	};
	private static String[] replaceChars = new String[]{
		"","","","","","","","","",""
	};

	/**
	 * 过滤非法文件名
	 * @param fileName
	 * @return
	 */
	public static String cleanInvalidFileName(String fileName) {
		return StringUtils.replaceEachRepeatedly(fileName, InvalidFileNameChars, replaceChars);
	}
	/**
	 * 复制目录
	 * @param orgPath
	 * @param destPath
	 */
	public static void copyDirectory(String orgPath, String destPath) {
		File srcDir = new File(orgPath);
		File destDir = new File(destPath);
		if (srcDir.exists()) {
			try {
				FileUtils.copyDirectory(srcDir, destDir);
			} catch (IOException e) {
				LogUtil.log().error("复制目录{}失败。{}", srcDir,e.getMessage());
			}
		}
	}
	/**
	 * 删除临时目录
	 * @param filepath
	 */
	protected static void delTempFile(String filepath) {
		File tempFile = new File(filepath);
		try {
			FileUtils.deleteDirectory(tempFile);
		} catch (IOException e) {
			LogUtil.log().error("删除临时目录失败 {}", e.getMessage());
		}
	}
	
}
