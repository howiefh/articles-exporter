package io.github.howiefh.ui.statusbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.Timer;

public class FreeStatusTimeLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5904830742327251884L;
	private int delay = 1000;

	public FreeStatusTimeLabel() {
		new Timer(delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				String dateString = dateFormat.format(new Date());
				setText(dateString);
			}

		}).start();
	}
}
