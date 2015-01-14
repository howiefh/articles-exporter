package io.github.howiefh.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1158432266211992494L;
	private JTextField textFieldUserAgent;
	private JTextField textFieldConnectionTimeout;
	private JTextField textFieldReadTimeout;
	private JTextField textFieldThreads;
	private JTextField textFieldMarkdown;
	private JTextField textFieldCssPath;
	private JTextField textFieldMediaPath;
	private JTextField textFieldJsPath;

	/**
	 * Create the panel.
	 */
	public SettingsPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{63, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblUserAgent = new JLabel("UA:");
		GridBagConstraints gbc_lblUserAgent = new GridBagConstraints();
		gbc_lblUserAgent.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserAgent.gridx = 0;
		gbc_lblUserAgent.gridy = 0;
		add(lblUserAgent, gbc_lblUserAgent);
		
		textFieldUserAgent = new JTextField();
		GridBagConstraints gbc_textFieldUserAgent = new GridBagConstraints();
		gbc_textFieldUserAgent.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldUserAgent.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUserAgent.gridx = 1;
		gbc_textFieldUserAgent.gridy = 0;
		add(textFieldUserAgent, gbc_textFieldUserAgent);
		textFieldUserAgent.setColumns(10);
		
		JLabel lblConnectionTimeout = new JLabel("连接超时:");
		GridBagConstraints gbc_lblConnectionTimeout = new GridBagConstraints();
		gbc_lblConnectionTimeout.anchor = GridBagConstraints.EAST;
		gbc_lblConnectionTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_lblConnectionTimeout.gridx = 0;
		gbc_lblConnectionTimeout.gridy = 1;
		add(lblConnectionTimeout, gbc_lblConnectionTimeout);
		
		textFieldConnectionTimeout = new JTextField();
		textFieldConnectionTimeout.setColumns(10);
		GridBagConstraints gbc_textFieldConnectionTimeout = new GridBagConstraints();
		gbc_textFieldConnectionTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldConnectionTimeout.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldConnectionTimeout.gridx = 1;
		gbc_textFieldConnectionTimeout.gridy = 1;
		add(textFieldConnectionTimeout, gbc_textFieldConnectionTimeout);
		
		JLabel lblReadTimeout = new JLabel("读取超时:");
		GridBagConstraints gbc_lblReadTimeout = new GridBagConstraints();
		gbc_lblReadTimeout.anchor = GridBagConstraints.EAST;
		gbc_lblReadTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_lblReadTimeout.gridx = 0;
		gbc_lblReadTimeout.gridy = 2;
		add(lblReadTimeout, gbc_lblReadTimeout);
		
		textFieldReadTimeout = new JTextField();
		textFieldReadTimeout.setColumns(10);
		GridBagConstraints gbc_textFieldReadTimeout = new GridBagConstraints();
		gbc_textFieldReadTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldReadTimeout.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldReadTimeout.gridx = 1;
		gbc_textFieldReadTimeout.gridy = 2;
		add(textFieldReadTimeout, gbc_textFieldReadTimeout);
		
		JLabel lblThreads = new JLabel("线程数:");
		GridBagConstraints gbc_lblThreads = new GridBagConstraints();
		gbc_lblThreads.anchor = GridBagConstraints.EAST;
		gbc_lblThreads.insets = new Insets(0, 0, 5, 5);
		gbc_lblThreads.gridx = 0;
		gbc_lblThreads.gridy = 3;
		add(lblThreads, gbc_lblThreads);
		
		textFieldThreads = new JTextField();
		textFieldThreads.setColumns(10);
		GridBagConstraints gbc_textFieldThreads = new GridBagConstraints();
		gbc_textFieldThreads.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldThreads.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldThreads.gridx = 1;
		gbc_textFieldThreads.gridy = 3;
		add(textFieldThreads, gbc_textFieldThreads);
		
		JLabel lblMarkdown = new JLabel("markdown :");
		GridBagConstraints gbc_lblMarkdown = new GridBagConstraints();
		gbc_lblMarkdown.anchor = GridBagConstraints.EAST;
		gbc_lblMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_lblMarkdown.gridx = 0;
		gbc_lblMarkdown.gridy = 4;
		add(lblMarkdown, gbc_lblMarkdown);
		
		textFieldMarkdown = new JTextField();
		textFieldMarkdown.setColumns(10);
		GridBagConstraints gbc_textFieldMarkdown = new GridBagConstraints();
		gbc_textFieldMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMarkdown.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMarkdown.gridx = 1;
		gbc_textFieldMarkdown.gridy = 4;
		add(textFieldMarkdown, gbc_textFieldMarkdown);
		
		JLabel lblCssPath = new JLabel("css路径:");
		GridBagConstraints gbc_lblCssPath = new GridBagConstraints();
		gbc_lblCssPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblCssPath.gridx = 0;
		gbc_lblCssPath.gridy = 5;
		add(lblCssPath, gbc_lblCssPath);
		
		textFieldCssPath = new JTextField();
		textFieldCssPath.setColumns(10);
		GridBagConstraints gbc_textFieldCssPath = new GridBagConstraints();
		gbc_textFieldCssPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCssPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCssPath.gridx = 1;
		gbc_textFieldCssPath.gridy = 5;
		add(textFieldCssPath, gbc_textFieldCssPath);
		
		JLabel lblJspath = new JLabel("js路径:");
		GridBagConstraints gbc_lblJspath = new GridBagConstraints();
		gbc_lblJspath.anchor = GridBagConstraints.EAST;
		gbc_lblJspath.insets = new Insets(0, 0, 5, 5);
		gbc_lblJspath.gridx = 0;
		gbc_lblJspath.gridy = 6;
		add(lblJspath, gbc_lblJspath);
		
		textFieldJsPath = new JTextField();
		textFieldJsPath.setColumns(10);
		GridBagConstraints gbc_textFieldJsPath = new GridBagConstraints();
		gbc_textFieldJsPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldJsPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldJsPath.gridx = 1;
		gbc_textFieldJsPath.gridy = 6;
		add(textFieldJsPath, gbc_textFieldJsPath);
		
		JLabel lblMediaPath = new JLabel("media路径:");
		GridBagConstraints gbc_lblMediaPath = new GridBagConstraints();
		gbc_lblMediaPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblMediaPath.gridx = 0;
		gbc_lblMediaPath.gridy = 7;
		add(lblMediaPath, gbc_lblMediaPath);
		
		textFieldMediaPath = new JTextField();
		textFieldMediaPath.setColumns(10);
		GridBagConstraints gbc_textFieldMediaPath = new GridBagConstraints();
		gbc_textFieldMediaPath.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMediaPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMediaPath.gridx = 1;
		gbc_textFieldMediaPath.gridy = 7;
		add(textFieldMediaPath, gbc_textFieldMediaPath);

	}

}
