package io.github.howiefh.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author alex.e.@gmail.com
 */
public class DemoFrame extends JFrame {

	public static void main(String[] args) {
        try {
			UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new DemoFrame().setVisible(true);
		System.out.println(textField.getAlignmentX());
		System.out.println(textField.getAutoscrolls());
		System.out.println(textField.getCaretPosition());
		System.out.println(textField.getColumns());
		System.out.println(textField.getFocusAccelerator());
		System.out.println(textField.getDefaultLocale());
		System.out.println(textField.getVisibleRect());
		System.out.println(textField.getSize());
		System.out.println(textField.getPreferredSize());
		System.out.println(textField.getHorizontalAlignment());
		System.out.println(textField.getSelectionStart());
		System.out.println(textField.getSelectionEnd());
		System.out.println(textField.getInsets());
		System.out.println(textField.getCursor());
		System.out.println(textField.getWidth());
		System.out.println(textField.getBounds());
		System.out.println(textField.getCaret());
	}
	static JTextField textField = new JTextField();
    public DemoFrame() {
        setTitle("Swing Preview");

        // use name to target the frame
        getContentPane().setName("Frame");

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        final DefaultTableModel model = new DefaultTableModel(new String[]{"Key", "Value"},0);
        for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
           model.addRow(new Object[] {
                   entry.getKey(), entry.getValue()
           });
        }
        final JProgressBar bar1 = new JProgressBar() {{
            setStringPainted(true);
        }};
        // animator for the progress bar
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final int i = bar1.getValue() + 1;
                    bar1.setValue(i % bar1.getMaximum());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
	    final List<? extends JComponent> components = Arrays.asList(
                new JButton("Button") {{addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        JOptionPane.showMessageDialog(DemoFrame.this, "OptionPane");
                    }
                });}},
			    new JButton("Button named 'primary'") {{setName("primary");}},
                new JCheckBox("Checkbox") {{setSelected(true);}},
                new JColorChooser(),
                new JComboBox(new String[] {"ComboBox Item 0", "ComboBox Item 1", "ComboBox Item 2"}),
                new JFileChooser("FileChooser"),
                new JEditorPane() {{setText("EditorPane");}},
                new JLabel("Label"),
                new JList(new String[] {"List Item 0", "List Item 1", "List Item 2", "List Item 3"}) {{setSelectedIndex(1);}},
                new JPasswordField("PasswordField"),
                bar1,
			    new JProgressBar() {{
			        setIndeterminate(true);
			        setStringPainted(true);
			        setString("ProgressBar Indeterminate");
			    }},
                new JRadioButton("RadioButton") {{setSelected(true);}},
                new JSlider(),
                new JSpinner(new SpinnerDateModel()),
                new JScrollPane(new JTable(model)),
                new JTextArea("TextArea"),
                new JTextField("TextField"),
                new JToggleButton("ToggleButton"),
                new JToolBar(),
                new JTree()
        );

        for (JComponent c : components) {
            final boolean l = c instanceof JColorChooser || c instanceof JFileChooser || c instanceof JScrollPane;
            final JPanel panel = l ? panel2 : panel1;
            panel.add(c);
        }
        JTextField textField = new JTextField();
        textField.setText("lskjdflsdjlfjsdlfjldskfjs");
        textField.setPreferredSize(new Dimension(40,20));
        panel1.add(textField);
       
        
	    setJMenuBar(new JMenuBar() {{
	        add(new JMenu("Menu") {{
	            add(new JMenuItem("MenuItem"));
	            addSeparator();
		        add(new JMenuItem("MenuItem"));
	        }});
	    }});
        getContentPane().add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(panel1), new JScrollPane(panel2)));

        pack();
    }
}