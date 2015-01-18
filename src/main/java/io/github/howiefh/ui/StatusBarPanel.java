package io.github.howiefh.ui;

import io.github.howiefh.export.Message;
import io.github.howiefh.ui.statusbar.FreeGCButton;
import io.github.howiefh.ui.statusbar.FreeMemoryBar;
import io.github.howiefh.ui.statusbar.FreeStatusMessageLabel;
import io.github.howiefh.ui.statusbar.FreeStatusTimeLabel;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;

public class StatusBarPanel extends JPanel  implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1864737774826613549L;
	private boolean undecorated = false;
	
	private FreeStatusMessageLabel lblStatusMessage;
	private FreeMemoryBar memoryBar;
	private FreeGCButton gcButton;
	private FreeStatusTimeLabel timeLabel;

	/**
	 * Create the panel.
	 */
	public StatusBarPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblStatusMessage = new FreeStatusMessageLabel();
		GridBagConstraints gbc_lblStatusMessage = new GridBagConstraints();
		gbc_lblStatusMessage.anchor = GridBagConstraints.WEST;
		gbc_lblStatusMessage.insets = new Insets(5, 5, 5, 5);
		gbc_lblStatusMessage.gridx = 0;
		gbc_lblStatusMessage.gridy = 0;
		add(lblStatusMessage, gbc_lblStatusMessage);

		memoryBar = new FreeMemoryBar();
		GridBagConstraints gbc_memoryBar = new GridBagConstraints();
		gbc_memoryBar.insets = new Insets(5, 0, 5, 5);
		gbc_memoryBar.gridx = 2;
		gbc_memoryBar.gridy = 0;
		add(memoryBar, gbc_memoryBar);

		gcButton = new FreeGCButton();
		GridBagConstraints gbc_gcButton = new GridBagConstraints();
		gbc_gcButton.insets = new Insets(5, 0, 5, 5);
		gbc_gcButton.gridx = 3;
		gbc_gcButton.gridy = 0;
		add(gcButton, gbc_gcButton);

		timeLabel = new FreeStatusTimeLabel();
		GridBagConstraints gbc_timeLabel = new GridBagConstraints();
		gbc_timeLabel.insets = new Insets(5, 0, 5, 5);
		gbc_timeLabel.gridx = 4;
		gbc_timeLabel.gridy = 0;
		add(timeLabel, gbc_timeLabel);

		setPreferredSize(new Dimension(200, 33));
		setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(100, 100, 100)));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Color bottomBgColor = getBackground();
		Color topBgColor = bottomBgColor.brighter();
		if (!undecorated) {
			Graphics2D g2d = (Graphics2D) g;

			g2d.setPaint(new GradientPaint(0, 0, topBgColor, 0, getHeight(),
					bottomBgColor));
			g2d.fillRect(0, 0, getWidth(), getHeight());

			g2d.setPaint(Color.GRAY);
			g2d.drawLine(0, 0, getWidth() - 1, 0);
		}
	}

	@Override
	public void warn(String text) {
		lblStatusMessage.warn(text);
	}

	@Override
	public void info(String text) {
		lblStatusMessage.info(text);
	}

	@Override
	public void error(String text) {
		lblStatusMessage.error(text);
	}

}
