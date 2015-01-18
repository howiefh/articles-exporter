package io.github.howiefh.ui.statusbar;

import io.github.howiefh.util.IOUtil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FreeGCButton extends FreeBarButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8275060112060143356L;

	public FreeGCButton()
	{
		super();
		setIcon(IOUtil.loadIcon("gc.png"));
		setMinimumSize(new Dimension(24,24));
		setToolTipText("释放内存");
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.gc();
			}
		});
	}
}
