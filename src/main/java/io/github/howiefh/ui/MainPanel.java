package io.github.howiefh.ui;

import io.github.howiefh.ui.table.JCheckBoxHeaderTable;
import io.github.howiefh.ui.table.JCheckBoxHeaderTable.Status;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JCheckBox;

import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JButton;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8803153475858796941L;
	private JTextField textFieldOutDir;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblOutType = new JLabel("导出类型:");
		GridBagConstraints gbc_lblOutType = new GridBagConstraints();
		gbc_lblOutType.insets = new Insets(0, 5, 5, 5);
		gbc_lblOutType.gridx = 0;
		gbc_lblOutType.gridy = 0;
		add(lblOutType, gbc_lblOutType);
		
		JCheckBox chckbxHtml = new JCheckBox("html");
		GridBagConstraints gbc_chckbxHtml = new GridBagConstraints();
		gbc_chckbxHtml.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxHtml.gridx = 1;
		gbc_chckbxHtml.gridy = 0;
		add(chckbxHtml, gbc_chckbxHtml);
		
		JCheckBox chckbxMarkdown = new JCheckBox("markdown");
		GridBagConstraints gbc_chckbxMarkdown = new GridBagConstraints();
		gbc_chckbxMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxMarkdown.gridx = 2;
		gbc_chckbxMarkdown.gridy = 0;
		add(chckbxMarkdown, gbc_chckbxMarkdown);
		
		JCheckBox chckbxText = new JCheckBox("text");
		GridBagConstraints gbc_chckbxText = new GridBagConstraints();
		gbc_chckbxText.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxText.gridx = 3;
		gbc_chckbxText.gridy = 0;
		add(chckbxText, gbc_chckbxText);
		
		JCheckBox chckbxPdf = new JCheckBox("pdf");
		GridBagConstraints gbc_chckbxPdf = new GridBagConstraints();
		gbc_chckbxPdf.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxPdf.gridx = 4;
		gbc_chckbxPdf.gridy = 0;
		add(chckbxPdf, gbc_chckbxPdf);
		
		JCheckBox chckbxSingleHtml = new JCheckBox("single html");
		GridBagConstraints gbc_chckbxSingleHtml = new GridBagConstraints();
		gbc_chckbxSingleHtml.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSingleHtml.gridx = 5;
		gbc_chckbxSingleHtml.gridy = 0;
		add(chckbxSingleHtml, gbc_chckbxSingleHtml);
		
		JCheckBox chckbxSingleMarkdown = new JCheckBox("single markdown");
		GridBagConstraints gbc_chckbxSingleMarkdown = new GridBagConstraints();
		gbc_chckbxSingleMarkdown.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSingleMarkdown.gridx = 6;
		gbc_chckbxSingleMarkdown.gridy = 0;
		add(chckbxSingleMarkdown, gbc_chckbxSingleMarkdown);
		
		JCheckBox chckbxSingleText = new JCheckBox("single text");
		GridBagConstraints gbc_chckbxSingleText = new GridBagConstraints();
		gbc_chckbxSingleText.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSingleText.gridx = 7;
		gbc_chckbxSingleText.gridy = 0;
		add(chckbxSingleText, gbc_chckbxSingleText);
		
		JLabel lblOutDir = new JLabel("导出目录:");
		GridBagConstraints gbc_lblOutDir = new GridBagConstraints();
		gbc_lblOutDir.anchor = GridBagConstraints.EAST;
		gbc_lblOutDir.insets = new Insets(0, 5, 5, 5);
		gbc_lblOutDir.gridx = 0;
		gbc_lblOutDir.gridy = 1;
		add(lblOutDir, gbc_lblOutDir);
		
		textFieldOutDir = new JTextField();
		GridBagConstraints gbc_textFieldOutDir = new GridBagConstraints();
		gbc_textFieldOutDir.gridwidth = 5;
		gbc_textFieldOutDir.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldOutDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOutDir.gridx = 1;
		gbc_textFieldOutDir.gridy = 1;
		add(textFieldOutDir, gbc_textFieldOutDir);
		textFieldOutDir.setColumns(10);
		
		Object[] columnNames = {Status.SELECTED,"文章标题","结果"};
		Object[][] data = {};
		
		JButton btnChooseDir = new JButton("选择目录");
		GridBagConstraints gbc_btnChooseDir = new GridBagConstraints();
		gbc_btnChooseDir.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseDir.gridx = 6;
		gbc_btnChooseDir.gridy = 1;
		add(btnChooseDir, gbc_btnChooseDir);
		
		JButton btnExport = new JButton("导出");
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.insets = new Insets(0, 0, 5, 5);
		gbc_btnExport.gridx = 7;
		gbc_btnExport.gridy = 1;
		add(btnExport, gbc_btnExport);
		
		JCheckBoxHeaderTable scrollTablePane = new JCheckBoxHeaderTable(columnNames, data);
		GridBagConstraints gbc_scrollTablePane = new GridBagConstraints();
		gbc_scrollTablePane.insets = new Insets(0, 5, 0, 5);
		gbc_scrollTablePane.gridwidth = 9;
		gbc_scrollTablePane.fill = GridBagConstraints.BOTH;
		gbc_scrollTablePane.gridx = 0;
		gbc_scrollTablePane.gridy = 2;
		add(scrollTablePane, gbc_scrollTablePane);
	}
}
