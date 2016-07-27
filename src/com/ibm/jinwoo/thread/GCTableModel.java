package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class GCTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2907939756570554978L;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
	public static final boolean DESCENDING = true;
	public static final boolean ASCENDING = false;
	public boolean direction = true;
	String[] columnNames = { "Free", "Total", "Needed", "Freed", "Completed", "Since", "Mark", "Sweep", "Compact",
			"GC completed", "Overhead", "Exhausted", "Timestamp" };
	GCInfo hi;
	int sortedColumn = 0;
	public JTableHeader tableHeader;
	public long[][] sortedArrary;
	private MouseListener mouseListener;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public GCTableModel() {
	}

	public GCTableModel(GCInfo h) {
		this.hi = h;
		this.sortedColumn = 0;
		this.mouseListener = new MouseHandler();
	}

	public GCTableModel(GCInfo h, int col) {
		this.hi = h;
		this.sortedColumn = col;
		this.mouseListener = new MouseHandler();
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public String getColumnName(int col) {
		return this.columnNames[col];
	}

	long getCompleted(int idx) {
		if (this.hi.completed[idx] != 0L)
			return this.hi.completed[idx];
		if (this.hi.gccompleted[idx] == 0L)
			return 0L;
		for (int i = idx; i < this.hi.free.length; i++) {
			if (this.hi.completed[i] != 0L)
				return this.hi.completed[i];
		}
		return 0L;
	}

	public Icon getHeaderIcon(int column, int size) {
		if (column != this.sortedColumn)
			return null;
		return new SortableHeaderIcon(this.direction, size);
	}

	public int getRowCount() {
		return this.hi.total.length;
	}

	long getSince(int idx) {
		if (this.hi.since[idx] != 0L)
			return this.hi.since[idx];
		for (int i = idx; i >= 0; i--) {
			if ((this.hi.since[i] != 0L) && (this.hi.af[i] != 0L))
				return this.hi.since[i];
		}
		return 0L;
	}

	public Object getValueAt(int row, int col) {
		int idx = row;

		if (this.sortedColumn == 12) {
			if (!this.direction)
				idx = getRowCount() - row - 1;
		} else if (this.direction)
			idx = getRowCount() - row - 1;

		if (this.sortedColumn != 12)
			idx = (int) this.sortedArrary[1][idx];
		switch (col) {
		case 0:
			return numberFormatter.format(this.hi.free[idx]);
		case 1:
			return numberFormatter.format(this.hi.total[idx]);
		case 2:
			return numberFormatter.format(this.hi.af[idx]);
		case 3:
			return numberFormatter.format(this.hi.freed[idx]);
		case 4:
			return numberFormatter.format(this.hi.completed[idx]);
		case 5:
			return numberFormatter.format(this.hi.since[idx]);
		case 6:
			return numberFormatter.format(this.hi.mark[idx]);
		case 7:
			return numberFormatter.format(this.hi.sweep[idx]);
		case 8:
			return numberFormatter.format(this.hi.compact[idx]);
		case 9:
			return numberFormatter.format(this.hi.gccompleted[idx]);
		case 10:
			if (this.sortedColumn == 10) {
				if (this.direction)
					row = getRowCount() - row - 1;
				return numberFormatter.format(this.sortedArrary[0][row]);
			}

			if ((this.hi.since[idx] == 0L) && (this.hi.af[idx] != 0L))
				return numberFormatter.format(0L);
			return numberFormatter.format(
					Math.round((float) getCompleted(idx) * 100.0F / (float) (getSince(idx) + getCompleted(idx))));
		case 11:
			if (this.hi.outOfHeapSpace == null)
				return "No";
			for (int i = 0; i < this.hi.outOfHeapSpace.length; i++) {
				if (this.hi.outOfHeapSpace[i] == idx)
					return "Yes";
			}
			return "No";
		}

		return dateFormatter.format(new Date(this.hi.timestamp[idx]));
	}

	boolean isStartPoint(int idx) {
		if ((idx >= this.hi.free.length) || (idx < 0))
			return false;
		if (idx == 0)
			return true;
		if (this.hi.ngc[(idx - 1)] >= this.hi.ngc[idx])
			return true;
		return false;
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

		SortThreadGC st = new SortThreadGC(this, column);
		st.start();
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
				GCTableModel.this.sortColumn(column, GCTableModel.this.direction);

				if (GCTableModel.this.tableHeader != null)
					GCTableModel.this.tableHeader.repaint();
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
				l.setIcon(GCTableModel.this.getHeaderIcon(modelColumn, l.getFont().getSize()));
			}

			return c;
		}
	}

}
