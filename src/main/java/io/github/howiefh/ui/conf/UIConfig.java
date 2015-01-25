package io.github.howiefh.ui.conf;

import org.dom4j.Element;

import io.github.howiefh.conf.Config;
import static io.github.howiefh.ui.conf.UIConfigValidate.*;

public class UIConfig extends Config {
	public static final String LOOKANDFEEL = "WebLookAndFeel";
	public static String lookAndFeel = LOOKANDFEEL;
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

	@Override
	public void init() {
		lookAndFeel = LOOKANDFEEL;
	}

	@Override
	public boolean validate() {
		return validateLookAndFeel(UIConfig.lookAndFeel);
	}

}
