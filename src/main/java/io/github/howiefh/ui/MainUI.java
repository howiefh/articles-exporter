package io.github.howiefh.ui;

import java.awt.Font;
import java.util.Enumeration;

import io.github.howiefh.conf.Config;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.ui.conf.UIConfig;
import io.github.howiefh.ui.conf.UIOptions;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.lang3.SystemUtils;

public class MainUI {
	public static void main(String[] args) {
		Config[] configs = {GeneralConfig.getInstance(),UIConfig.getInstance()};
		ArticleExporter.initConfig(configs);
		ArticleExporter.initRegisters();
        //Set the look and feel.
		try {
			UIManager.setLookAndFeel(UIOptions.getInstance().getLookAndFeel(UIConfig.lookAndFeel));
			if (SystemUtils.IS_OS_WINDOWS) {
				//Windows下设置全局字体，否则weblaf会乱码,其他可供选择的字体Helvetica,Arial,sans-serif
				initGlobalFont(new Font("Helvetica", 0, 12));
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			LogUtil.log().error(e.getMessage());
		}

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setIconImage(IOUtil.loadIcon("icon.png").getImage());
        mainFrame.setVisible(true);
	}

	private static void initGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys
				.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
}
