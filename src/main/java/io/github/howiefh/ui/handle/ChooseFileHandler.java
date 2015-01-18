package io.github.howiefh.ui.handle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class ChooseFileHandler implements ActionListener{
	private JComponent component;
	
	public ChooseFileHandler(JComponent component){
		this.component = component;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (component instanceof JTextField) {
			final JTextField textField = (JTextField)component;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showOpenDialog(textField.getRootPane()); 
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().toString());
			}
		}
		
	}

}
