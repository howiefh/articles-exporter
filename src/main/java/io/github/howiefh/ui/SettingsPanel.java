package io.github.howiefh.ui;

import io.github.howiefh.conf.Config;
import io.github.howiefh.conf.ConfigUtil;
import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.conf.UserAgentsRegister;
import io.github.howiefh.export.Message;
import io.github.howiefh.renderer.Markdown;
import io.github.howiefh.ui.conf.UIConfig;
import io.github.howiefh.ui.conf.UIOptions;
import io.github.howiefh.ui.text.FreeTextField;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang3.SystemUtils;

public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1158432266211992494L;

	
	private JComboBox<String> comboBoxTheme;
	private JComboBox<String> comboBoxUA;
	private SpinnerNumberModel spinnerModelConnectionTimeout; 
	private SpinnerNumberModel spinnerModelReadTimeout; 
	private FreeTextField textFieldCssPath;
	private FreeTextField textFieldMediaPath;
	private FreeTextField textFieldJsPath;
	private SpinnerNumberModel spinnerModelThread; 
	private JComboBox<String> comboBoxMarkdown;
	private JButton btnSettingOk;
	
	private Message message;

	/**
	 * Create the panel.
	 */
	public SettingsPanel(Message message) {
		this.message = message;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 200, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTheme = new JLabel("外观:");
		GridBagConstraints gbc_lblTheme = new GridBagConstraints();
		gbc_lblTheme.anchor = GridBagConstraints.EAST;
		gbc_lblTheme.insets = new Insets(0, 0, 5, 5);
		gbc_lblTheme.gridx = 1;
		gbc_lblTheme.gridy = 2;
		add(lblTheme, gbc_lblTheme);
		
		comboBoxTheme = new JComboBox<String>();
		comboBoxTheme.setToolTipText("选择外观,建议选择WebLookAndFeel");
		GridBagConstraints gbc_comboBoxTheme = new GridBagConstraints();
		gbc_comboBoxTheme.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxTheme.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTheme.gridx = 2;
		gbc_comboBoxTheme.gridy = 2;
		add(comboBoxTheme, gbc_comboBoxTheme);
		
		JLabel lblUserAgent = new JLabel("UA:");
		GridBagConstraints gbc_lblUserAgent = new GridBagConstraints();
		gbc_lblUserAgent.anchor = GridBagConstraints.EAST;
		gbc_lblUserAgent.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserAgent.gridx = 1;
		gbc_lblUserAgent.gridy = 3;
		add(lblUserAgent, gbc_lblUserAgent);
		
		comboBoxUA = new JComboBox<String>();
		comboBoxUA.setToolTipText("设置浏览器标识");
		GridBagConstraints gbc_comboBoxUA = new GridBagConstraints();
		gbc_comboBoxUA.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxUA.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxUA.gridx = 2;
		gbc_comboBoxUA.gridy = 3;
		add(comboBoxUA, gbc_comboBoxUA);
		
		JLabel lblConnectionTimeout = new JLabel("连接超时:");
		lblConnectionTimeout.setToolTipText("");
		GridBagConstraints gbc_lblConnectionTimeout = new GridBagConstraints();
		gbc_lblConnectionTimeout.anchor = GridBagConstraints.EAST;
		gbc_lblConnectionTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_lblConnectionTimeout.gridx = 1;
		gbc_lblConnectionTimeout.gridy = 4;
		add(lblConnectionTimeout, gbc_lblConnectionTimeout);
		
		JSpinner spinnerConnectionTimeout = new JSpinner();
		spinnerConnectionTimeout.setToolTipText("设置连接超时时间，单位毫秒");
		spinnerConnectionTimeout.setPreferredSize(new Dimension(100,30));
		GridBagConstraints gbc_spinnerConnectionTimeout = new GridBagConstraints();
		gbc_spinnerConnectionTimeout.anchor = GridBagConstraints.WEST;
		gbc_spinnerConnectionTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerConnectionTimeout.gridx = 2;
		gbc_spinnerConnectionTimeout.gridy = 4;
		add(spinnerConnectionTimeout, gbc_spinnerConnectionTimeout);
		spinnerModelConnectionTimeout= new SpinnerNumberModel();
		spinnerModelConnectionTimeout.setMaximum(GeneralConfig.MAX_CONNECTION_TIMEOUT);
		spinnerModelConnectionTimeout.setMinimum(GeneralConfig.MIN_CONNECTION_TIMEOUT);
		spinnerModelConnectionTimeout.setStepSize(100);
		spinnerConnectionTimeout.setModel(spinnerModelConnectionTimeout);
		
		JLabel lblReadTimeout = new JLabel("传输超时:");
		GridBagConstraints gbc_lblReadTimeout = new GridBagConstraints();
		gbc_lblReadTimeout.anchor = GridBagConstraints.EAST;
		gbc_lblReadTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_lblReadTimeout.gridx = 1;
		gbc_lblReadTimeout.gridy = 5;
		add(lblReadTimeout, gbc_lblReadTimeout);
		
		JSpinner spinnerReadTimeout = new JSpinner();
		spinnerReadTimeout.setToolTipText("设置传输超时时间，单位毫秒");
		spinnerReadTimeout.setPreferredSize(new Dimension(100,30));
		GridBagConstraints gbc_spinnerReadTimeout = new GridBagConstraints();
		gbc_spinnerReadTimeout.anchor = GridBagConstraints.WEST;
		gbc_spinnerReadTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerReadTimeout.gridx = 2;
		gbc_spinnerReadTimeout.gridy = 5;
		add(spinnerReadTimeout, gbc_spinnerReadTimeout);
		spinnerModelReadTimeout = new SpinnerNumberModel();
		spinnerModelReadTimeout.setMaximum(GeneralConfig.MAX_READ_TIMEOUT);
		spinnerModelReadTimeout.setMinimum(GeneralConfig.MIN_READ_TIMEOUT);
		spinnerModelReadTimeout.setStepSize(100);
		spinnerReadTimeout.setModel(spinnerModelReadTimeout);
		
		JLabel lblThreads = new JLabel("线程数:");
		GridBagConstraints gbc_lblThreads = new GridBagConstraints();
		gbc_lblThreads.anchor = GridBagConstraints.EAST;
		gbc_lblThreads.insets = new Insets(0, 0, 5, 5);
		gbc_lblThreads.gridx = 1;
		gbc_lblThreads.gridy = 6;
		add(lblThreads, gbc_lblThreads);
		
		JSpinner spinnerThreads = new JSpinner();
		spinnerThreads.setToolTipText("设置导出文章时开启的线程数");
		spinnerThreads.setPreferredSize(new Dimension(100,30));
		GridBagConstraints gbc_spinnerThreads = new GridBagConstraints();
		gbc_spinnerThreads.anchor = GridBagConstraints.WEST;
		gbc_spinnerThreads.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerThreads.gridx = 2;
		gbc_spinnerThreads.gridy = 6;
		add(spinnerThreads, gbc_spinnerThreads);
		spinnerModelThread = new SpinnerNumberModel();
		spinnerModelThread.setMaximum(GeneralConfig.MAX_THREADS);
		spinnerModelThread.setMinimum(GeneralConfig.MIN_THREADS);
		spinnerThreads.setModel(spinnerModelThread);
		
		JLabel lblMarkdown = new JLabel("markdown :");
		GridBagConstraints gbc_lblMarkdown = new GridBagConstraints();
		gbc_lblMarkdown.anchor = GridBagConstraints.EAST;
		gbc_lblMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_lblMarkdown.gridx = 1;
		gbc_lblMarkdown.gridy = 7;
		add(lblMarkdown, gbc_lblMarkdown);
		
		comboBoxMarkdown = new JComboBox<String>();
		comboBoxMarkdown.setToolTipText("设置导出的markdown主题");
		GridBagConstraints gbc_comboBoxMarkdown = new GridBagConstraints();
		gbc_comboBoxMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxMarkdown.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMarkdown.gridx = 2;
		gbc_comboBoxMarkdown.gridy = 7;
		add(comboBoxMarkdown, gbc_comboBoxMarkdown);
		
		JLabel lblCssPath = new JLabel("css路径:");
		GridBagConstraints gbc_lblCssPath = new GridBagConstraints();
		gbc_lblCssPath.anchor = GridBagConstraints.EAST;
		gbc_lblCssPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblCssPath.gridx = 1;
		gbc_lblCssPath.gridy = 8;
		add(lblCssPath, gbc_lblCssPath);
		
		textFieldCssPath = new FreeTextField();
		textFieldCssPath.setToolTipText("设置导出的css文件存放目录名");
		textFieldCssPath.setColumns(10);
		GridBagConstraints gbc_textFieldCssPath = new GridBagConstraints();
		gbc_textFieldCssPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCssPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCssPath.gridx = 2;
		gbc_textFieldCssPath.gridy = 8;
		add(textFieldCssPath, gbc_textFieldCssPath);
		
		JLabel lblJspath = new JLabel("js路径:");
		GridBagConstraints gbc_lblJspath = new GridBagConstraints();
		gbc_lblJspath.anchor = GridBagConstraints.EAST;
		gbc_lblJspath.insets = new Insets(0, 0, 5, 5);
		gbc_lblJspath.gridx = 1;
		gbc_lblJspath.gridy = 9;
		add(lblJspath, gbc_lblJspath);
		
		textFieldJsPath = new FreeTextField();
		textFieldJsPath.setToolTipText("设置导出的javascript文件存放目录名");
		textFieldJsPath.setColumns(10);
		GridBagConstraints gbc_textFieldJsPath = new GridBagConstraints();
		gbc_textFieldJsPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldJsPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldJsPath.gridx = 2;
		gbc_textFieldJsPath.gridy = 9;
		add(textFieldJsPath, gbc_textFieldJsPath);
		
		JLabel lblMediaPath = new JLabel("media路径:");
		GridBagConstraints gbc_lblMediaPath = new GridBagConstraints();
		gbc_lblMediaPath.anchor = GridBagConstraints.EAST;
		gbc_lblMediaPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblMediaPath.gridx = 1;
		gbc_lblMediaPath.gridy = 10;
		add(lblMediaPath, gbc_lblMediaPath);
		
		textFieldMediaPath = new FreeTextField();
		textFieldMediaPath.setToolTipText("设置导出的多媒体文件存放目录名");
		textFieldMediaPath.setColumns(10);
		GridBagConstraints gbc_textFieldMediaPath = new GridBagConstraints();
		gbc_textFieldMediaPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMediaPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMediaPath.gridx = 2;
		gbc_textFieldMediaPath.gridy = 10;
		add(textFieldMediaPath, gbc_textFieldMediaPath);
		
		btnSettingOk = new JButton("确定");
		GridBagConstraints gbc_btnSettingOk = new GridBagConstraints();
		gbc_btnSettingOk.insets = new Insets(0, 0, 5, 5);
		gbc_btnSettingOk.gridx = 2;
		gbc_btnSettingOk.gridy = 11;
		add(btnSettingOk, gbc_btnSettingOk);

		init();
	}
	
	private void init() {
		for (String lookAndFeel : UIOptions.getInstance().getLookAndFeelNames()) {
			comboBoxTheme.addItem(lookAndFeel);
		}
		comboBoxTheme.setSelectedItem(UIConfig.lookAndFeel);
		for (String userAgent: UserAgentsRegister.names()) {
			comboBoxUA.addItem(userAgent);
		}
		comboBoxUA.setSelectedItem(GeneralConfig.userAgent);
		
		spinnerModelConnectionTimeout.setValue(GeneralConfig.connectionTimeout);
		spinnerModelReadTimeout.setValue(GeneralConfig.readTimeout);
		spinnerModelThread.setValue(GeneralConfig.threads);
		
		textFieldCssPath.setText(GeneralConfig.cssPath);
		textFieldJsPath.setText(GeneralConfig.jsPath);
		textFieldMediaPath.setText(GeneralConfig.mediaPath);
		
		for (Markdown markdown: Markdown.values()) {
			comboBoxMarkdown.addItem(markdown.name());
		}
		comboBoxMarkdown.setSelectedItem(GeneralConfig.markdown.toString());
		
		
		btnSettingOk.addActionListener(new SaveSettingHandler());
	}
	
	private class SaveSettingHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					settingsChange();
				}
			}).start();
		}

	}
	
	private void settingsChange() {
		boolean isChanged = false;
		boolean isLookAndFeelChanged = false;
		String oldValue = UIConfig.lookAndFeel;
		UIConfig.lookAndFeel = (String)comboBoxTheme.getSelectedItem();
		if (!oldValue.equals(UIConfig.lookAndFeel)) {
			if (SystemUtils.IS_OS_WINDOWS) {
				isLookAndFeelChanged = true;
			} else {
				final String lookAndFeelName = UIOptions.getInstance().getLookAndFeel(UIConfig.lookAndFeel);
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						try {
							UIManager.setLookAndFeel(lookAndFeelName);
						} catch (ClassNotFoundException | InstantiationException
								| IllegalAccessException
								| UnsupportedLookAndFeelException e) {
							LogUtil.log().error(e.getMessage());
						}
						// 必须先dispose后setvisible，否则updateUI中不能setUndecorated
						MainFrame.getInstance().dispose();
						// Windows下，从weblaf切换到其他外观updateUI中setUndecorated会抛异常，现在Windows下采用重启程序的方法
			            MainFrame.getInstance().updateUI();
			            SwingUtilities.updateComponentTreeUI(MainFrame.getInstance()); 
			            MainFrame.getInstance().setVisible(true);
					}
				});
			}
			isChanged = true;
		}
		
		oldValue = GeneralConfig.userAgent;
		GeneralConfig.userAgent = (String)comboBoxUA.getSelectedItem();
		if (!oldValue.equals(GeneralConfig.userAgent)) {
			isChanged = true;
		}
		
		int oldIntValue = GeneralConfig.connectionTimeout;
		GeneralConfig.connectionTimeout = (Integer)spinnerModelConnectionTimeout.getValue();
		if (oldIntValue != GeneralConfig.connectionTimeout) {
			isChanged = true;
		}
		oldIntValue = GeneralConfig.readTimeout;
		GeneralConfig.readTimeout = (Integer)spinnerModelReadTimeout.getValue();
		if (oldIntValue != GeneralConfig.readTimeout) {
			isChanged = true;
		}
		oldIntValue = GeneralConfig.threads;
		GeneralConfig.threads = (Integer)spinnerModelThread.getNumber();
		if (oldIntValue != GeneralConfig.threads) {
			isChanged = true;
		}
		
		oldValue = GeneralConfig.cssPath;
		GeneralConfig.cssPath = IOUtil.cleanInvalidFileName(textFieldCssPath.getText());
		if (!oldValue.equals(GeneralConfig.cssPath)) {
			isChanged = true;
		}
		oldValue = GeneralConfig.jsPath;
		GeneralConfig.jsPath = IOUtil.cleanInvalidFileName(textFieldJsPath.getText());
		if (!oldValue.equals(GeneralConfig.jsPath)) {
			isChanged = true;
		}
		oldValue = GeneralConfig.mediaPath;
		GeneralConfig.mediaPath = IOUtil.cleanInvalidFileName(textFieldMediaPath.getText());
		if (!oldValue.equals(GeneralConfig.mediaPath)) {
			isChanged = true;
		}
		
		Markdown markdown = GeneralConfig.markdown;
		GeneralConfig.markdown = Markdown.valueOf((String)comboBoxMarkdown.getSelectedItem());
		if (markdown != GeneralConfig.markdown) {
			isChanged = true;
		}
		
		if (isChanged) {
			Config[] configs = {GeneralConfig.getInstance(), UIConfig.getInstance()};
			ConfigUtil.saveConfig(configs);
			message.info("配置已更改");
		} else {
			message.info("配置未更改");
		}
		
		if (isLookAndFeelChanged) {
			//构造进程生成器
			ProcessBuilder pb = new ProcessBuilder("articles-exporter.exe");
			try {
				pb.start();
			} catch (IOException e) {
				LogUtil.log().error(e.getMessage());
			}
			System.exit(0);
		}
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
