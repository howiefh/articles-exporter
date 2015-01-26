package io.github.howiefh.ui;

import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.conf.GeneralOptions;
import io.github.howiefh.conf.PropertiesHelper;
import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.export.Message;
import io.github.howiefh.renderer.util.JsoupUtil;
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
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;

import org.jsoup.nodes.Document;

public class ArticlesListExporterPanel extends JPanel {
	private static final long serialVersionUID = -3096400336620302257L;
	
	private static final int MAX_START_PAGE = 300;
	private static final int MIN_START_PAGE = 0;
	private static final int MAX_PAGE_COUNT = 300;
	private static final int MIN_PAGE_COUNT = 1;
	
	private FreeTextField textFieldLink;
	private SpinnerNumberModel startPageNumberModel; 
	private SpinnerNumberModel pageCountNumberModel;
	private JComboBox<String> comboBoxRule;
	private JButton btnArticlesList;
	private JCheckBox chckbxReversedOrder;
	private MainPanel panel;
	
	private Message message;
	/**
	 * Create the panel.
	 */
	public ArticlesListExporterPanel(Message message) {
		this.message = message;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 120, 60, 40, 60, 40, 60, 100, 0, 60, 0};
		int rowHeight = 0;
		gridBagLayout.rowHeights = new int[]{rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblLink = new JLabel("链接:");
		GridBagConstraints gbc_lblLink = new GridBagConstraints();
		gbc_lblLink.insets = new Insets(5, 5, 5, 5);
		gbc_lblLink.gridx = 0;
		gbc_lblLink.gridy = 0;
		add(lblLink, gbc_lblLink);
		
		textFieldLink = new FreeTextField();
		GridBagConstraints gbc_textFieldLink = new GridBagConstraints();
		gbc_textFieldLink.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLink.gridx = 1;
		gbc_textFieldLink.gridy = 0;
		add(textFieldLink, gbc_textFieldLink);
		textFieldLink.setColumns(10);
		
		JLabel lblStartPage = new JLabel("起始页码:");
		GridBagConstraints gbc_lblStartPage = new GridBagConstraints();
		gbc_lblStartPage.insets = new Insets(5, 0, 5, 5);
		gbc_lblStartPage.anchor = GridBagConstraints.EAST;
		gbc_lblStartPage.gridx = 2;
		gbc_lblStartPage.gridy = 0;
		add(lblStartPage, gbc_lblStartPage);
		
		JSpinner spinnerStartPage = new JSpinner();
		spinnerStartPage.setPreferredSize(new Dimension(80,30));
		GridBagConstraints gbc_spinnerStartPage = new GridBagConstraints();
		gbc_spinnerStartPage.insets = new Insets(5, 0, 5, 5);
		gbc_spinnerStartPage.gridx = 3;
		gbc_spinnerStartPage.gridy = 0;
		add(spinnerStartPage, gbc_spinnerStartPage);
		startPageNumberModel = new SpinnerNumberModel(1, MIN_START_PAGE, MAX_START_PAGE, 1);
		spinnerStartPage.setModel(startPageNumberModel);
		
		JLabel lblPageCount = new JLabel("总页数:");
		GridBagConstraints gbc_lblPageCount = new GridBagConstraints();
		gbc_lblPageCount.anchor = GridBagConstraints.EAST;
		gbc_lblPageCount.insets = new Insets(5, 0, 5, 5);
		gbc_lblPageCount.gridx = 4;
		gbc_lblPageCount.gridy = 0;
		add(lblPageCount, gbc_lblPageCount);
		
		JSpinner spinnerPageCount = new JSpinner();
		spinnerPageCount.setPreferredSize(new Dimension(80,30));
		GridBagConstraints gbc_spinnerPageCount = new GridBagConstraints();
		gbc_spinnerPageCount.insets = new Insets(5, 0, 5, 5);
		gbc_spinnerPageCount.gridx = 5;
		gbc_spinnerPageCount.gridy = 0;
		add(spinnerPageCount, gbc_spinnerPageCount);
		pageCountNumberModel = new SpinnerNumberModel(1, MIN_PAGE_COUNT, MAX_PAGE_COUNT, 1);
		spinnerPageCount.setModel(pageCountNumberModel);
		
		JLabel lblRule = new JLabel("解析规则:");
		GridBagConstraints gbc_lblRule = new GridBagConstraints();
		gbc_lblRule.anchor = GridBagConstraints.EAST;
		gbc_lblRule.insets = new Insets(5, 0, 5, 5);
		gbc_lblRule.gridx = 6;
		gbc_lblRule.gridy = 0;
		add(lblRule, gbc_lblRule);
		
		comboBoxRule = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 0;
		add(comboBoxRule, gbc_comboBox);
		
		chckbxReversedOrder = new JCheckBox("逆序");
		chckbxReversedOrder.setSelected(true);
		GridBagConstraints gbc_chckbxReversedOrder = new GridBagConstraints();
		gbc_chckbxReversedOrder.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxReversedOrder.gridx = 8;
		gbc_chckbxReversedOrder.gridy = 0;
		add(chckbxReversedOrder, gbc_chckbxReversedOrder);
		
		btnArticlesList = new JButton("获取列表");
		GridBagConstraints gbc_btnArticlesList = new GridBagConstraints();
		gbc_btnArticlesList.insets = new Insets(5, 0, 5, 0);
		gbc_btnArticlesList.gridx = 9;
		gbc_btnArticlesList.gridy = 0;
		add(btnArticlesList, gbc_btnArticlesList);
		
		panel = new MainPanel(message, GeneralOptions.getInstance());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 0, 0, 0);
		gbc_panel.gridwidth = 10;
		gbc_panel.gridheight = 9;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		init();
	}
	
	private void init(){
		for (String ruleName : PropertiesHelper.rules.keySet()) {
			comboBoxRule.addItem(ruleName);
		}
		
		btnArticlesList.addActionListener(new ArticlesListHandler());
	}

	private class ArticlesListHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					btnArticlesList.setEnabled(false);
					try {
						Document doc = JsoupUtil.get(String.format(textFieldLink.getText(), "1"), GeneralConfig.userAgent, GeneralConfig.connectionTimeout, GeneralConfig.readTimeout);
						GeneralOptions.getInstance().setTitle(IOUtil.cleanInvalidFileName(doc.title()));
					} catch (IOException e) {
						GeneralOptions.getInstance().setTitle("out");
						LogUtil.log().warn(e.getMessage());
					} catch (Exception e) {
						GeneralOptions.getInstance().setTitle("out");
						LogUtil.log().warn(e.getMessage());
					}
					GeneralOptions.getInstance().setArticleListPageLink(textFieldLink.getText());
					GeneralOptions.getInstance().setStartPage((Integer)startPageNumberModel.getValue());
					GeneralOptions.getInstance().setPageCount((Integer)pageCountNumberModel.getValue());
					GeneralOptions.getInstance().setRuleName((String)comboBoxRule.getSelectedItem());
					try {
						if (chckbxReversedOrder.isSelected()) {
							panel.fillTable(ArticleExporter.reverseArticleList());
						} else {
							panel.fillTable(ArticleExporter.articleList());
						}
					} catch (Exception e1) {
						LogUtil.log().error(e1.getMessage());
					} finally {
						btnArticlesList.setEnabled(true);
					}
				}
			}).start();
		}
		
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
