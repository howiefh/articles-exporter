/**
 * 
 */
package io.github.howiefh.util;

import io.github.howiefh.conf.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 操作xml工具类
 * @author FengHao
 *
 */
public class XmlUtil {
	// XML文件名
	private String xmlFile;
	// XML 文档对象
	private Document document;
	// XML 文档根节点
	private Element root;
	
	public XmlUtil(String xmlFile) throws FileNotFoundException {
		initial(xmlFile);
	}
	
	public XmlUtil(String xmlFile, boolean isCreateNewFile) throws FileNotFoundException {
		File file = new File(xmlFile);
		if (isCreateNewFile == true && !file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LogUtil.log().error("创建文件 {} 失败", xmlFile);
			}
		}
		initial(xmlFile);
	}
	
	public void initial(String xmlFile) throws FileNotFoundException{
		this.xmlFile=xmlFile;
		
		File file = new File(xmlFile);
		try {
			// 判断文件的存在
			if (file.exists()) {
				// 文件存在,直接从文件读取文档对象
				SAXReader reader = new SAXReader();
				document = reader.read(file);
				root = document.getRootElement();
				if (root == null) {
					LogUtil.log().debug("root is null");
					root = document.addElement("settings");  
				} 
			} else {
				// 文件不存在
				LogUtil.log().error("找不到文件:{}",xmlFile);
				throw new FileNotFoundException(String.format("找不到文件:" + xmlFile));
			}
		} catch (DocumentException  ex) {
//			ex.printStackTrace();
			LogUtil.log().error("无法读取文件,{}", ex.toString());
			//配置文件为空,主动创建document对象
	        document = DocumentHelper.createDocument();
	        root = document.addElement("settings");  //创建根节点
		}	
	}
	
	/**
	 * 更新一个对象对应的XML节点
	 * @param config
	 */
	public void update(Config config) {
		if (config == null) {
			LogUtil.log().info("config为空");
			return;
		}
		//获取config子元素
		Element element = root.element(config.getElementName());
		//如果没有此节点,直接加入新的节点
		if (element == null) {
			config.generateNodeUnder(root);
			saveDocumentToFile();
			LogUtil.log().info("原配置中{}为空，新配置已写入:{}",config.getElementName(), config.toString());
			return;
		}
		//删除原来的
		element.detach();
//		root.remove(element);
		//加入新的
		config.generateNodeUnder(root);
		//保存
		saveDocumentToFile();
		LogUtil.log().info("更新配置文件:{}", config.toString());
	}
	
	/**
	 * 按照xpath路径更改节点内容
	 * @param xpathExpression xpath路径
	 * @param newText 新的节点内容
	 * @return
	 */
	public boolean update(String xpathExpression,String newText) {
        //获得需要修改的节点
        Node node = document.selectSingleNode(xpathExpression);
        if (node == null) {
			LogUtil.log().error("没有匹配的xpath:{}", xpathExpression);
			return false;
		} else {
	        //将节点强制转换成元素
	        Element element = (Element)node;
	        element.setText(newText);
		}
		//保存
		saveDocumentToFile();
        return true;
	}
	
	public boolean read(Config config) {
		//获取config子元素
		Element element = root.element(config.getElementName());
        if (element== null) {
			LogUtil.log().error("找不到元素:{}", config.getElementName());
			return false;
		}
        return config.fetchFieldValueFromNode(element);
	}
	
	/**
	 * 如果config不存在则添加到配置文件
	 * @param config 配置类
	 * @return
	 */
	public boolean add(Config config) {
		//获取config子元素
		if(null == document.selectSingleNode("/" + root.getName() + "/" + config.getElementName()))
		{
			config.generateNodeUnder(root);
			saveDocumentToFile();
			return true;
		}
		return false;
	}
	
	/**
	 * 将document写回文件
	 * 
	 */
	private void saveDocumentToFile() {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8"); // 指定XML编码
			XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);

			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.log().error("不能保存XML文件:{}", ex.getMessage());
		}
	}

}
