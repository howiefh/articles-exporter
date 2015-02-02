package io.github.howiefh.ui;

import io.github.howiefh.conf.GeneralConfig;
import io.github.howiefh.conf.PropertiesHelper;
import io.github.howiefh.conf.UrlsOptions;
import io.github.howiefh.export.ArticleExporter;
import io.github.howiefh.export.Message;
import io.github.howiefh.renderer.util.JsoupUtil;
import io.github.howiefh.ui.text.FreeTextArea;
import io.github.howiefh.util.IOUtil;
import io.github.howiefh.util.LogUtil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jsoup.nodes.Document;

public class UrlListExporterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -460502795634776543L;
	private FreeTextArea textAreaLinks;
	private JComboBox<String> comboBoxRule;
	private JButton btnArticlesList;
	private JCheckBox chckbxReversedOrder;
	private MainPanel panel;
	private Message message;
	/**
	 * Create the panel.
	 */
	public UrlListExporterPanel(Message message) {
		this.message = message;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 120, 60, 40, 60, 40, 60, 100, 0, 0};
		int rowHeight = 0;
		gridBagLayout.rowHeights = new int[]{rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, rowHeight, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblLink = new JLabel("链接:");
		GridBagConstraints gbc_lblLink = new GridBagConstraints();
		gbc_lblLink.insets = new Insets(5, 5, 5, 5);
		gbc_lblLink.gridx = 0;
		gbc_lblLink.gridy = 0;
		add(lblLink, gbc_lblLink);
		
		textAreaLinks = new FreeTextArea();
		textAreaLinks.setToolTipText("指定一组列表页面的链接，通过换行符分隔这些链接");
		textAreaLinks.setTabSize(4);
		JScrollPane scrollPane = new JScrollPane(textAreaLinks);
		textAreaLinks.setLineWrap(true);
		GridBagConstraints gbc_textFieldLink = new GridBagConstraints();
		gbc_textFieldLink.gridheight = 2;
		gbc_textFieldLink.gridwidth = 5;
		gbc_textFieldLink.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldLink.fill = GridBagConstraints.BOTH;
		gbc_textFieldLink.gridx = 1;
		gbc_textFieldLink.gridy = 0;
		textAreaLinks.setColumns(10);
		add(scrollPane, gbc_textFieldLink);
		
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
		gbc_chckbxReversedOrder.gridx = 6;
		gbc_chckbxReversedOrder.gridy = 1;
		add(chckbxReversedOrder, gbc_chckbxReversedOrder);
		
		btnArticlesList = new JButton("获取列表");
		GridBagConstraints gbc_btnArticlesList = new GridBagConstraints();
		gbc_btnArticlesList.insets = new Insets(5, 0, 5, 5);
		gbc_btnArticlesList.gridx = 7;
		gbc_btnArticlesList.gridy = 1;
		add(btnArticlesList, gbc_btnArticlesList);
		
		panel = new MainPanel(message,UrlsOptions.getInstance());
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 0, 5, 0);
		gbc_panel.gridwidth = 9;
		gbc_panel.gridheight = 9;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);
		init();
	}
		
	private void init(){
		for (String ruleName : PropertiesHelper.rules.keySet()) {
			comboBoxRule.addItem(ruleName);
		}
		
		btnArticlesList.addActionListener(new ArticlesListUrlsHandler());
	}
	private class ArticlesListUrlsHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					btnArticlesList.setEnabled(false);
					String[] urls = textAreaLinks.getText().split("\n");
					List<String> urlList = new ArrayList<String>();
					for (int i = 0; i < urls.length; i++) {
						if (!urls[i].isEmpty()) {
							urlList.add(urls[i].trim());
						}
					}
					if (urlList.size()==0) {
						return;
					}
					try {
						Document doc = JsoupUtil.get(urlList.get(0), GeneralConfig.userAgent, GeneralConfig.connectionTimeout, GeneralConfig.readTimeout);
						UrlsOptions.getInstance().setTitle(IOUtil.cleanInvalidFileName(doc.title()));
					} catch (IOException e) {
						UrlsOptions.getInstance().setTitle("out");
						LogUtil.log().warn(e.getMessage());
					} catch (Exception e) {
						UrlsOptions.getInstance().setTitle("out");
						LogUtil.log().warn(e.getMessage());
					}
					UrlsOptions.getInstance().setUrls(urlList.toArray(new String[]{}));
					UrlsOptions.getInstance().setRuleName((String)comboBoxRule.getSelectedItem());
					try {
						if (chckbxReversedOrder.isSelected()) {
							panel.fillTable(ArticleExporter.reverseUrlsArticleList());
						} else {
							panel.fillTable(ArticleExporter.urlsArticleList());
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
