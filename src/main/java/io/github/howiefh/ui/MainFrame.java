package io.github.howiefh.ui;

import io.github.howiefh.conf.Config;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.export.ArticleExporterCli;
import io.github.howiefh.ui.conf.UIConfig;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private MainFrame() {
		Config[] configs = {GeneralConfig.getInstance(),UIConfig.getInstance()};
		ArticleExporterCli.initConfig(configs);
		builUI();
	}

	private void builUI() {
		// 设置窗口属性
		final int WIDTH = 770;
		final int HEIGHT = 450;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		setTitle("文章导出工具");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 3, 3));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		ArticlesListExporterPanel panel = new ArticlesListExporterPanel();
		tabbedPane.addTab("导出文章列表", null, panel, null);
		
		SettingsPanel settingsPanel = new SettingsPanel();
		tabbedPane.addTab("设置", null, settingsPanel, null);
	}
	public static MainFrame getInstance() {
		if (instance==null) {
			instance = new MainFrame();
		}
		return instance;
	}
}
