package io.github.howiefh.ui;

import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.util.IOUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
		final int HEIGHT = 521;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
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
		setMinimumSize(new Dimension(0, 0));
		// 人的感光系统能够区分多达每秒48次闪光，设置更新时间10毫秒,windows下20毫秒有些明显抖动，调成10毫秒
		int milliSecond = 10;
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
		if (!UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
			dispose();
			setUndecorated(true);
			setVisible(true);	
		}
		setBounds(x, y+height/2, width, 0);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void updateUI() {
		JRootPane jRootPane = getRootPane();
		//没有这句,有窗体装饰的变为没有窗体装饰(如Nimbus变成Metal)时，会没有装饰，标题栏不显示
		jRootPane.setWindowDecorationStyle(JRootPane.FRAME);
		//没有这句,有窗体装饰的变为没有窗体装饰(如Nimbus变成Metal)时，会有装饰，会有两个标题栏，一个系统自带，一个是laf的。
		//没有这句,没有窗体装饰的变为有窗体装饰(如Metal变成Nimbus)时，会没有装饰，标题栏不显示
		//这句使用laf支持窗体窗体装饰来设置frame的窗体装饰，使窗体能使用laf提供的窗体装饰
		setUndecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
	}
	public static MainFrame getInstance() {
		if (instance==null) {
			instance = new MainFrame();
		}
		return instance;
	}
}
