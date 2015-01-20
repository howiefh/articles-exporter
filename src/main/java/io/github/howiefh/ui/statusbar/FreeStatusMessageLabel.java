package io.github.howiefh.ui.statusbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import io.github.howiefh.util.IOUtil;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class FreeStatusMessageLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 184067070779820798L;
	private static final ImageIcon ICON_ORANGE = IOUtil
			.loadIcon("statusbar_message_light_orange.png");
	private static final ImageIcon ICON_RED = IOUtil
			.loadIcon("statusbar_message_light_red.png");
	private static final ImageIcon ICON_GREEN = IOUtil
			.loadIcon("statusbar_message_light_green.png");
	private int delay;
	private boolean isAutoClear = false;
	private Timer timer;

	public FreeStatusMessageLabel(String text, int delay, boolean isAutoClear) {
		this.delay = delay;
		this.isAutoClear = isAutoClear;
		if (isAutoClear) {
			ActionListener taskPerformer = new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					normal("");
				}

			};
			timer = new Timer(delay, taskPerformer);
			timer.setRepeats(false);
		}
		normal(text);
	}

	public FreeStatusMessageLabel() {
		this("", 8000, true);
	}

	public void error(String text) {
		setIcon(ICON_RED);
		setText(text);
	}

	public void info(String text) {
		setIcon(ICON_GREEN);
		setText(text);
	}

	public void warn(String text) {
		setIcon(ICON_ORANGE);
		setText(text);
	}

	public void normal(String text) {
		super.setText(text);
		setIcon(null);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (isAutoClear) {
			timer.start();
		}
	};

	public int getDelay() {
		return delay;
	}

	public boolean isAutoClear() {
		return isAutoClear;
	}

}
