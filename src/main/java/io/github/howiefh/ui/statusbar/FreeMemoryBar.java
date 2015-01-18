package io.github.howiefh.ui.statusbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.NumberFormat;

import javax.swing.JProgressBar;
import javax.swing.Timer;

public class FreeMemoryBar extends JProgressBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1570224196073650312L;
	private static final int kilo = 1024;
	private static final String mega = "M";
	private static MemoryMXBean memorymbean = ManagementFactory
			.getMemoryMXBean();
	private static NumberFormat format = NumberFormat.getInstance();
	private int delay;

	public FreeMemoryBar() {
		super(0, 0, 100);
		setStringPainted(true);
		setMinimumSize(new Dimension(150,29));
		delay = 2000;
		ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				long usedMemory = memorymbean.getHeapMemoryUsage().getUsed();
				long totalMemory = memorymbean.getHeapMemoryUsage().getMax();
				updateMemoryUsage(usedMemory, totalMemory);
			}

		};
		(new Timer(delay, taskPerformer)).start();
	}

	private void updateMemoryUsage(long usedMemory, long totalMemory) {
		int percent = (int) ((usedMemory * 100L) / totalMemory);
		setValue(percent);
		String usedMega = (new StringBuilder())
				.append(format.format(usedMemory / kilo / kilo)).append(mega)
				.toString();
		String totalMega = (new StringBuilder())
				.append(format.format(totalMemory / kilo / kilo)).append(mega)
				.toString();
		String message = (new StringBuilder()).append(usedMega).append("/")
				.append(totalMega).toString();
		setString(message);
		setToolTipText((new StringBuilder()).append("Memory used ")
				.append(format.format(usedMemory)).append(" of total ")
				.append(format.format(totalMemory)).toString());
	}
	
}
