package io.github.howiefh.ui.conf;

import org.dom4j.Element;

import io.github.howiefh.conf.Config;

public class UIConfig extends Config {
	public static String lookAndFeel = "WebLookAndFeel";
	private static UIConfig instance = new UIConfig();

	@Override
	public Element generateNodeUnder(Element parentElm) {
		Element uiElm=parentElm.addElement(getElementName());
		
		Element element=uiElm.addElement("lookAndFeel");
		element.setText(lookAndFeel);
		
		return uiElm;
	}

	@Override
	public boolean fetchFieldValueFromNode(Element nodeElm) {
		if (nodeElm == null) {
			return false;
		}
		lookAndFeel = nodeElm.elementText("lookAndFeel");
		return true;
	}

	@Override
	public String getElementName() {
		return "ui";
	}
	
	public static UIConfig getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "UIConfig [lookAndFeel=" + lookAndFeel + "]";
	}

}
