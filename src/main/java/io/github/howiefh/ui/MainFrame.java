package io.github.howiefh.ui;

import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.util.IOUtil;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		
		//Frame居中显示
		setLocationRelativeTo(null);  

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				effectsOnClosing();
				System.exit(0);
			}
		});
	}
	private void effectsOnClosing(){
		// 人的感光系统能够区分多达每秒48次闪光，设置更新时间20毫秒
		int milliSecond = 20;
		// 需要渐进的缩小的次数
		int count = 450 / milliSecond;
		int height = getHeight();
		int heightPerMinus = height / count; 
		int width = getWidth();
		int x = getLocation().x;
		int y = getLocation().y;
		if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			setExtendedState(JFrame.NORMAL);
			setBounds(x, y, width, height);
		}
		for (int i = 0; i < count; i++) {
			height -= heightPerMinus;
			y += heightPerMinus/2;
			setBounds(x, y, width, height);
			try {
				Thread.sleep(milliSecond);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		setBounds(x, y+height/2, width, 0);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void updateUI() {
		if (isDisplayable())
			dispose();
		setUndecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
		JRootPane jRootPane = getRootPane();
		jRootPane.setWindowDecorationStyle(jRootPane.getWindowDecorationStyle());
		if (!isDisplayable())
			setVisible(true);	
	}
	public static MainFrame getInstance() {
		if (instance==null) {
			instance = new MainFrame();
		}
		return instance;
	}
}
