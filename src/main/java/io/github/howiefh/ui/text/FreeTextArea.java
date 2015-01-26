package io.github.howiefh.ui.text;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class FreeTextArea extends JTextArea {
	private static final long serialVersionUID = 8190221279052046923L;

	private JPopupMenu pop = null; // 弹出菜单

	private JMenuItem copy = null, paste = null, cut = null,selectAll = null; // 三个功能菜单

	public FreeTextArea() {
		super();
		init();
	}

	private void init() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					copy.setEnabled(isCanCopy());
					paste.setEnabled(isClipboardString());
					cut.setEnabled(isCanCopy());
					selectAll.setEnabled(isCanSelectAll());
					pop.show(FreeTextArea.this, e.getX(), e.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		pop = new JPopupMenu();
		pop.add(copy = new JMenuItem("复制"));
		pop.add(paste = new JMenuItem("粘贴"));
		pop.add(cut = new JMenuItem("剪切"));
		pop.add(selectAll = new JMenuItem("全选"));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		selectAll.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action(e);
			}
		});
	}

	/**
	 * 菜单动作
	 * 
	 * @param e
	 */
	public void action(ActionEvent e) {
		String str = e.getActionCommand();
		if (str.equals(copy.getText())) { // 复制
			this.copy();
		} else if (str.equals(paste.getText())) { // 粘贴
			this.paste();
		} else if (str.equals(cut.getText())) { // 剪切
			this.cut();
		} else if (str.equals(selectAll.getText())) { // 全选
			this.selectAll();
		}
	}

	public JPopupMenu getPop() {
		return pop;
	}

	public void setPop(JPopupMenu pop) {
		this.pop = pop;
	}

	/**
	 * 剪切板中是否有文本数据可供粘贴
	 * 
	 * @return true为有文本数据
	 */
	public boolean isClipboardString() {
		boolean b = false;
		Clipboard clipboard = this.getToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(this);
		try {
			if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
				b = true;
			}
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * 文本组件中是否具备复制的条件
	 * 
	 * @return true为具备
	 */
	public boolean isCanCopy() {
		boolean b = false;
		int start = this.getSelectionStart();
		int end = this.getSelectionEnd();
		if (start != end)
			b = true;
		return b;
	}
	public boolean isCanSelectAll() {
		boolean b = false;
		String text = this.getText();
		if (!text.isEmpty())
			b = true;
		return b;
	}

}
