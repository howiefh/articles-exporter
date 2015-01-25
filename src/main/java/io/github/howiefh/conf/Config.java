/**
 * 
 */
package io.github.howiefh.conf;

import org.dom4j.Element;

/**
 * 对象转化为XML节点和从节点取值到对象字段的基类
 * @author FengHao
 *
 */
public abstract class Config {
	// ID
	private final int ID = 1000;
		
	/**
	 * 对象转化为XML节点的方法
	 * @param parentElm :父节点
	 * @return 对象生成的节点
	 */
	public abstract Element generateNodeUnder(Element parentElm);
	
	/**
	 * 从节点中取值传入对象的字段
	 * @param nodeElm:对象对应的节点
	 */
	public abstract boolean fetchFieldValueFromNode(Element nodeElm);
	public abstract void init();
	public abstract boolean validate();
	
	/**
	 * 获取当前元素的名称
	 * @return
	 */
	public abstract String getElementName(); 
	
	public int getId() {
		return ID;
	}
}
