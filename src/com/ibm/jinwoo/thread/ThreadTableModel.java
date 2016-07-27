package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class ThreadTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -6962107502336569922L;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
	public static final boolean DESCENDING = true;
	public static final boolean ASCENDING = false;
	public boolean direction = false;

	public static final String[] columnNames = { "Name", "State", "NativeID", "Method" };
	ThreadDump hi;
	int sortedColumn = 0;
	public JTableHeader tableHeader;
	public long[][] sortedArrary;
	private MouseListener mouseListener;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public ThreadTableModel() {
	}

	public ThreadTableModel(ThreadDump h) {
		this.hi = h;
		this.sortedColumn = 0;
		this.mouseListener = new MouseHandler();
	}

	public ThreadTableModel(ThreadDump h, int col) {
		this.hi = h;
		this.sortedColumn = col;
		this.mouseListener = new MouseHandler();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public String getCurrentMethod(int r) {
		if ((this.hi.javaStack[r] == null) || (this.hi.javaStack[r].length() == 0))
			return null;
		int i = this.hi.javaStack[r].indexOf("<BR>");
		if (i != -1) {
			int j = this.hi.javaStack[r].indexOf("at ");
			if (j != -1)
				return this.hi.javaStack[r].substring(j + 3, i);
		}
		return null;
	}

	public Icon getHeaderIcon(int column, int size) {
		if (column != this.sortedColumn)
			return null;
		return new SortableHeaderIcon(this.direction, size);
	}

	public int getRowCount() {
		if (this.hi == null)
			System.out.println("hi is null");
		if (this.hi.name == null)
			System.out.println("hi.name is null");
		return this.hi.name.length;
	}

	public Object getValueAt(int row, int col) {
		int idx = row;

		if (this.direction)
			idx = this.hi.name.length - row - 1;

		if (this.sortedColumn == 0) {
			if (col == 0)
				return this.hi.getSortedName(idx);
			if (col == 1)
				return this.hi.getState(this.hi.nameArray[idx]);

			if (col == 2)
				return this.hi.getNativeID(this.hi.nameArray[idx]);
			if (col == 3)
				return this.hi.getCurrentMethod(this.hi.nameArray[idx]);

			return null;
		}
		if (this.sortedColumn == 1) {
			if (col == 0)
				return this.hi.name[this.hi.stateArray[idx]];
			if (col == 1)
				return this.hi.getState(this.hi.stateArray[idx]);

			if (col == 2)
				return this.hi.getNativeID(this.hi.stateArray[idx]);
			if (col == 3)
				return getCurrentMethod(this.hi.stateArray[idx]);

			return null;
		}
		if (this.sortedColumn == 2) {
			if (col == 0)
				return this.hi.name[this.hi.idArray[idx]];
			if (col == 1)
				return this.hi.getState(this.hi.idArray[idx]);
			if (col == 2)
				return this.hi.getNativeID(this.hi.idArray[idx]);
			if (col == 3)
				return getCurrentMethod(this.hi.idArray[idx]);

			return null;
		}

		if (col == 0)
			return this.hi.name[this.hi.currentMethodArray[idx]];
		if (col == 1) {
			return this.hi.getState(this.hi.currentMethodArray[idx]);
		}
		if (col == 2)
			return this.hi.getNativeID(this.hi.currentMethodArray[idx]);
		if (col == 3)
			return this.hi.getSortedCurrentMethod(idx);

		return null;
	}

	public void setTableHeader(JTableHeader tableHeader) {
		if (this.tableHeader != null) {
			this.tableHeader.removeMouseListener(this.mouseListener);
			TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
			if ((defaultRenderer instanceof SortableHeaderRenderer)) {
				this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
			}
		}
		this.tableHeader = tableHeader;
		if (this.tableHeader != null) {
			this.tableHeader.addMouseListener(this.mouseListener);
			this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
		}
	}

	public void sortColumn(int column, boolean direction) {
		if (column == this.sortedColumn) {
			this.direction = (!direction);
			fireTableDataChanged();
			return;
		}

		this.sortedColumn = column;
		direction = true;
		fireTableDataChanged();
		if (this.tableHeader != null)
			this.tableHeader.repaint();
	}

	private class MouseHandler extends MouseAdapter {
		private MouseHandler() {
		}

		public void mouseClicked(MouseEvent e) {
			JTableHeader h = (JTableHeader) e.getSource();
			TableColumnModel columnModel = h.getColumnModel();
			int viewColumn = columnModel.getColumnIndexAtX(e.getX());
			int column = columnModel.getColumn(viewColumn).getModelIndex();
			if (column != -1) {
				ThreadTableModel.this.sortColumn(column, ThreadTableModel.this.direction);

				if (ThreadTableModel.this.tableHeader != null)
					ThreadTableModel.this.tableHeader.repaint();
			}
		}
	}

	private class SortableHeaderRenderer implements TableCellRenderer {
		private TableCellRenderer tableCellRenderer;

		public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
			this.tableCellRenderer = tableCellRenderer;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			if ((c instanceof JLabel)) {
				JLabel l = (JLabel) c;
				l.setHorizontalTextPosition(2);
				int modelColumn = table.convertColumnIndexToModel(column);
				l.setIcon(ThreadTableModel.this.getHeaderIcon(modelColumn, l.getFont().getSize()));
			}

			return c;
		}
	}

}
