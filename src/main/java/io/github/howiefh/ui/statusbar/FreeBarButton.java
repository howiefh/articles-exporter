package io.github.howiefh.ui.statusbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class FreeBarButton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 694642626530657555L;
	private int buttonSize;
	private Color roverBorderColor;
	private Border roverBorder;
	private Border emptyBorder;

	public FreeBarButton()
	{
		super();
		buttonSize = 20;
		roverBorderColor = new Color(196, 196, 197);
		roverBorder = new Border() {

			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
			{
				g.setColor(roverBorderColor);
				g.drawRect(x, y, width - 1, height - 1);
			}

			public Insets getBorderInsets(Component c)
			{
				return new Insets(1, 1, 1, 1);
			}

			public boolean isBorderOpaque()
			{
				return true;
			}
		};
		emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		init();
	}

	private void init()
	{
		setVerticalAlignment(0);
		setOpaque(false);
		setBorder(emptyBorder);
		setContentAreaFilled(false);
		setFocusPainted(false);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e)
			{
				setBorder(roverBorder);
			}

			public void mouseExited(MouseEvent e)
			{
				setBorder(emptyBorder);
			}
		});
	}

	@Override
	public void setIcon(Icon icon)
	{
		super.setIcon(icon);
		if (icon == null)
		{
			setPressedIcon(null);
			setRolloverIcon(null);
		} else
		{
			Icon pressedIcon = createMovedIcon(icon);
			setPressedIcon(pressedIcon);
		}
	}
	private Icon createMovedIcon(final Icon icon){
			return createMovedIcon(icon, 1, 1);
	}
	private Icon createMovedIcon(final Icon icon, final int offsetX,
			final int offsetY) {
		return new Icon()
		{
			public void paintIcon(Component c, Graphics g, int x, int y)
			{
				icon.paintIcon(c, g, x + offsetX, y + offsetY);
			}
			public int getIconWidth()
			{
				return icon.getIconWidth();
			}
			public int getIconHeight()
			{
				return icon.getIconHeight();
			}
		};
	}

	@Override
	public Dimension getPreferredSize()
	{
		int width = super.getPreferredSize().width;
		width = Math.max(width, buttonSize);
		int height = buttonSize;
		return new Dimension(width, height);
	}
}
