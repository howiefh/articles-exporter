package io.github.howiefh.ui.table;

import io.github.howiefh.ui.table.JCheckBoxHeaderTable.Status;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class JCheckBoxHeaderTable extends JScrollPane{
	public static enum Status {
		SELECTED, DESELECTED, INDETERMINATE
	}
	private static final long serialVersionUID = 1161299044446967160L;
	private Object[] columnNames = {"", "Title", "Title"};  
	private Object[][] data = {};  
	private DefaultTableModel dtm;  
	private JTable table; 
	
	public JCheckBoxHeaderTable(Object[] columnNames,Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
		dtm = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 761958816249939020L;

			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(dtm);
		dtm.addTableModelListener(new HeaderCheckBoxHandler(table));

		TableCellRenderer tCellRenderer = new HeaderRenderer(table.getTableHeader(), 0);
		table.getColumnModel().getColumn(0).setHeaderRenderer(tCellRenderer);
		// <ins>
		TableCellRenderer leftAlign = new LeftAlignHeaderRenderer();
		table.getColumnModel().getColumn(1).setHeaderRenderer(leftAlign);
		table.getColumnModel().getColumn(2).setHeaderRenderer(leftAlign);
		// </ins>
		table.getTableHeader().setReorderingAllowed(false);
		
		// 设置第一列列宽
		int firstColumnWidth = 30;
		TableColumn firstColumn = table.getColumnModel().getColumn(0);
		firstColumn.setPreferredWidth(firstColumnWidth);
		firstColumn.setMaxWidth(firstColumnWidth);
		firstColumn.setMinWidth(firstColumnWidth);
		
		setViewportView(table);
	}
	public void addRow(Object[] rowData) {
		dtm.addRow(rowData);
	}
	public void addColumn(String columnName) {
		dtm.addColumn(columnName);
	}
	public void addColumn(String columnName,Object[] columnData) {
		dtm.addColumn(columnName, columnData);
	}
	public void insertRow(int row, Object[] rowData) {
		dtm.insertRow(row, rowData);
	}
	public void removeRow(int row){
		dtm.removeRow(row);
	}
	public  void setRowCount(int rowCount) {
		dtm.setRowCount(rowCount);
	}
	public void setValueAt(Object aValue, int row,int column) {
		dtm.setValueAt(aValue, row, column);
	}
	public Object getValueAt(int row,int column) {
		return dtm.getValueAt(row, column);
	}
	public TableModel getModel() {
		return dtm;
	}
	public JTable getTable(){
		return table;
	}

	public Object[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(Object[] columnNames) {
		this.columnNames = columnNames;
	}
	public Object[][] getData() {
		return data;
	}
	public void setData(Object[][] data) {
		this.data = data;
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					for (UIManager.LookAndFeelInfo laf : UIManager
							.getInstalledLookAndFeels())
						if ("Nimbus".equals(laf.getName()))
							UIManager.setLookAndFeel(laf.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Object[] columnNames = {Status.INDETERMINATE, "Title", "Title"};  
				Object[][] data = {{true,1,2},{true,1,2}};  
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.getContentPane().add(new JCheckBoxHeaderTable(columnNames,data));
				frame.setSize(320, 240);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}

class HeaderRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 4289281894163425308L;

	public HeaderRenderer(JTableHeader header, final int targetColumnIndex) {
		super((String) null);
		setOpaque(false);
		setFont(header.getFont());

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader header = (JTableHeader) e.getSource();
				JTable table = header.getTable();
				TableColumnModel columnModel = table.getColumnModel();
				int vci = columnModel.getColumnIndexAtX(e.getX());
				int mci = table.convertColumnIndexToModel(vci);
				if (mci == targetColumnIndex) {
					TableColumn column = columnModel.getColumn(vci);
					Object v = column.getHeaderValue();
					boolean b = Status.DESELECTED.equals(v) ? true : false;
					TableModel m = table.getModel();
					for (int i = 0; i < m.getRowCount(); i++)
						m.setValueAt(b, i, mci);
					column.setHeaderValue(b ? Status.SELECTED
							: Status.DESELECTED);
				}
			}
		});
	}

	@Override
	public Component getTableCellRendererComponent(JTable tbl, Object val,
			boolean isS, boolean hasF, int row, int col) {
		if (val instanceof Status) {
			switch ((Status) val) {
			case SELECTED:
				setSelected(true);
				setEnabled(true);
				break;
			case DESELECTED:
				setSelected(false);
				setEnabled(true);
				break;
			case INDETERMINATE:
				setSelected(true);
				setEnabled(false);
				break;
			}
		} else {
			setSelected(true);
			setEnabled(false);
		}
		TableCellRenderer r = tbl.getTableHeader().getDefaultRenderer();
		JLabel lable = (JLabel) r.getTableCellRendererComponent(tbl, null, isS,
				hasF, row, col);

		lable.setIcon(new CheckBoxIcon(this));
		lable.setText(null); // XXX Nimbus LnF ???
		lable.setHorizontalAlignment(JLabel.CENTER);
		return lable;
	}
}

class LeftAlignHeaderRenderer implements TableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable t, Object v,
			boolean isS, boolean hasF, int row, int col) {
		TableCellRenderer r = t.getTableHeader().getDefaultRenderer();
		JLabel l = (JLabel) r.getTableCellRendererComponent(t, v, isS, hasF,
				row, col);
		l.setHorizontalAlignment(JLabel.CENTER);
		return l;
	}
}



class CheckBoxIcon implements Icon {
	private final JCheckBox check;

	public CheckBoxIcon(JCheckBox check) {
		this.check = check;
	}

	@Override
	public int getIconWidth() {
		return check.getPreferredSize().width;
	}

	@Override
	public int getIconHeight() {
		return check.getPreferredSize().height;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		SwingUtilities.paintComponent(g, check, (Container) c, x, y,
				getIconWidth(), getIconHeight());
	}
}

class HeaderCheckBoxHandler implements TableModelListener {
	private final JTable table;

	public HeaderCheckBoxHandler(JTable table) {
		this.table = table;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
			int mci = 0;
			int vci = table.convertColumnIndexToView(mci);
			TableColumn column = table.getColumnModel().getColumn(vci);
			Object title = column.getHeaderValue();
			if (!Status.INDETERMINATE.equals(title)) {
				column.setHeaderValue(Status.INDETERMINATE);
			} else {
				int selected = 0, deselected = 0;
				TableModel m = table.getModel();
				for (int i = 0; i < m.getRowCount(); i++) {
					if (Boolean.TRUE.equals(m.getValueAt(i, mci))) {
						selected++;
					} else {
						deselected++;
					}
				}
				if (selected == 0) {
					column.setHeaderValue(Status.DESELECTED);
				} else if (deselected == 0) {
					column.setHeaderValue(Status.SELECTED);
				} else {
					return;
				}
			}
			table.getTableHeader().repaint();
		}
	}
}