package io.github.howiefh.ui;

import io.github.howiefh.conf.Config;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.ui.conf.UIConfig;
import io.github.howiefh.ui.conf.UIOptions;
import io.github.howiefh.util.LogUtil;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainUI {
	public static void main(String[] args) {
		Config[] configs = {GeneralConfig.getInstance(),UIConfig.getInstance()};
		ArticleExporter.initConfig(configs);
		ArticleExporter.initRegisters();
        //Set the look and feel.
		try {
			UIManager.setLookAndFeel(UIOptions.getInstance().getLookAndFeel(UIConfig.lookAndFeel));
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			LogUtil.log().error(e.getMessage());
		}

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setVisible(true);
	}
}
