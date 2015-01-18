package io.github.howiefh.ui;

import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.util.IOUtil;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3533433717430314642L;
	private JPanel contentPane;
	private static MainFrame instance;

	private MainFrame() {
		buildUI();
	}

	private void buildUI() {
		// 设置窗口属性
		final int WIDTH = 770;
		final int HEIGHT = 450;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		setTitle("文章导出工具");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		StatusBarPanel statusBar = new StatusBarPanel();
		contentPane.add(statusBar, BorderLayout.SOUTH);
		
		ArticlesListExporterPanel panel = new ArticlesListExporterPanel(statusBar);
		tabbedPane.addTab("导出文章列表", IOUtil.loadIcon("export.png"), panel, "导出文章列表中的文章到本地");
		
		SettingsPanel settingsPanel = new SettingsPanel(statusBar);
		tabbedPane.addTab("设置", IOUtil.loadIcon("settings.png"), settingsPanel, "设置");
		
		ArticleExporter.setMessage(statusBar);
	}
	public static MainFrame getInstance() {
		if (instance==null) {
			instance = new MainFrame();
		}
		return instance;
	}
}
