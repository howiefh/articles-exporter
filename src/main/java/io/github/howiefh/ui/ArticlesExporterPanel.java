package io.github.howiefh.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JComboBox;

public class ArticlesExporterPanel extends JPanel {

	private static final long serialVersionUID = -3096400336620302257L;
	private JTextField textFieldLink;
	private JTextField textFieldStartPage;
	private JTextField textFieldPageCount;

	/**
	 * Create the panel.
	 */
	public ArticlesExporterPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 120, 60, 60, 60, 60, 60, 100, 0};
		int rowHeight = 30;
		gridBagLayout.rowHeights = new int[]{rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblLink = new JLabel("链接:");
		GridBagConstraints gbc_lblLink = new GridBagConstraints();
		gbc_lblLink.insets = new Insets(0, 0, 5, 5);
		gbc_lblLink.gridx = 0;
		gbc_lblLink.gridy = 0;
		add(lblLink, gbc_lblLink);
		
		textFieldLink = new JTextField();
		GridBagConstraints gbc_textFieldLink = new GridBagConstraints();
		gbc_textFieldLink.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLink.gridx = 1;
		gbc_textFieldLink.gridy = 0;
		add(textFieldLink, gbc_textFieldLink);
		textFieldLink.setColumns(10);
		
		JLabel lblStartPage = new JLabel("起始页码:");
		GridBagConstraints gbc_lblStartPage = new GridBagConstraints();
		gbc_lblStartPage.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartPage.anchor = GridBagConstraints.EAST;
		gbc_lblStartPage.gridx = 2;
		gbc_lblStartPage.gridy = 0;
		add(lblStartPage, gbc_lblStartPage);
		
		textFieldStartPage = new JTextField();
		GridBagConstraints gbc_textFieldStartPage = new GridBagConstraints();
		gbc_textFieldStartPage.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldStartPage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldStartPage.gridx = 3;
		gbc_textFieldStartPage.gridy = 0;
		add(textFieldStartPage, gbc_textFieldStartPage);
		textFieldStartPage.setColumns(10);
		
		JLabel lblPageCount = new JLabel("总页数:");
		GridBagConstraints gbc_lblPageCount = new GridBagConstraints();
		gbc_lblPageCount.anchor = GridBagConstraints.EAST;
		gbc_lblPageCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblPageCount.gridx = 4;
		gbc_lblPageCount.gridy = 0;
		add(lblPageCount, gbc_lblPageCount);
		
		textFieldPageCount = new JTextField();
		GridBagConstraints gbc_textFieldPageCount = new GridBagConstraints();
		gbc_textFieldPageCount.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPageCount.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPageCount.gridx = 5;
		gbc_textFieldPageCount.gridy = 0;
		add(textFieldPageCount, gbc_textFieldPageCount);
		textFieldPageCount.setColumns(10);
		
		JLabel lblRule = new JLabel("解析规则:");
		GridBagConstraints gbc_lblRule = new GridBagConstraints();
		gbc_lblRule.anchor = GridBagConstraints.EAST;
		gbc_lblRule.insets = new Insets(0, 0, 5, 5);
		gbc_lblRule.gridx = 6;
		gbc_lblRule.gridy = 0;
		add(lblRule, gbc_lblRule);
		
		JComboBox comboBoxRule = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 0;
		add(comboBoxRule, gbc_comboBox);
		
		MainPanel panel = new MainPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 8;
		gbc_panel.gridheight = 9;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
	}

}
