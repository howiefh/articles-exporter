package io.github.howiefh.ui.statusbar;

import io.github.howiefh.util.IOUtil;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class FreeStatusMessageLabel extends JLabel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 184067070779820798L;
	private static final ImageIcon ICON_ORANGE = IOUtil.loadIcon("statusbar_message_light_orange.png");
	private static final ImageIcon ICON_RED = IOUtil.loadIcon("statusbar_message_light_red.png");
	private static final ImageIcon ICON_GREEN = IOUtil.loadIcon("statusbar_message_light_green.png");

	public FreeStatusMessageLabel(String text)
	{
		info(text);
	}
	public FreeStatusMessageLabel()
	{
		info("");
	}

	public void error(String text)
	{
		setIcon(ICON_RED);
		setText(text);
	}

	public void info(String text)
	{
		setIcon(ICON_GREEN);
		setText(text);
	}

	public void warn(String text)
	{
		setIcon(ICON_ORANGE);
		setText(text);
	}
}
