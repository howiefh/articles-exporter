package io.github.howiefh.ui.statusbar;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class FreeMemoryBarTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 3, 3));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(new FreeMemoryBar());
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}

}
