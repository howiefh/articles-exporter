package io.github.howiefh.ui;

import javax.swing.SwingUtilities;

public class MainFrameTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				MainFrame.getInstance();;
			}
		});
	}

}
