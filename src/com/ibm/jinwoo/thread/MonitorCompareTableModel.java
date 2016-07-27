package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class MonitorCompareTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8719242729056313294L;
	long[] threadArray;
	static SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMM d HH:mm:ss yyyy");
	public static final boolean DESCENDING = true;
	public static final boolean ASCENDING = false;
	public boolean direction = true;
	String[] columnNames = null;
	public String[] threadNames = null;
	public long[] threadID = null;
	ThreadDump[] hi;
	Hashtable<Integer, Long> tidHash = new Hashtable<Integer, Long>();
	int sortedColumn = 0;
	int tidCounter = 0;
	public JTableHeader tableHeader;
	public long[][] sortedArrary;
	private MouseListener mouseListener;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public MonitorCompareTableModel() {
	}

	public MonitorCompareTableModel(ThreadDump[] h) {
		this.hi = h;
		this.sortedColumn = 0;
		this.tidCounter = 0;
		this.columnNames = new String[h.length + 1];
		this.columnNames[0] = "Thread";
		Long newTid = null;
		for (int i = 0; i < h.length; i++) {
			if (h[i].timeStamp != 0L)
				this.columnNames[(i + 1)] = (h[i].fileName + " [" + dateFormatter2.format(new Date(h[i].timeStamp))
						+ "]");
			else
				this.columnNames[(i + 1)] = h[i].fileName;
			h[i].threadHash2 = new Hashtable<Long, Integer>();
			for (int j = 0; j < h[i].mdump.length; j++) {
				if (h[i].mdump[j].owner != -1L) {
					newTid = new Long(h[i].mdump[j].owner);

					h[i].threadHash2.put(newTid, new Integer(j));
					if (!this.tidHash.contains(newTid)) {
						this.tidHash.put(new Integer(this.tidCounter++), newTid);
					}
				}
			}
		}

		this.threadNames = new String[this.tidCounter];
		this.threadID = new long[this.tidCounter];
		this.tidCounter = 0;

		boolean found = false;
		for (int i = 0; i < this.tidHash.size(); i++) {
			found = false;
			Long tid = (Long) this.tidHash.get(new Integer(i));

			for (int j = 0; j < h.length; j++) {
				for (int k = 0; k < h[j].sys_thread.length; k++) {
					if (tid.longValue() == h[j].sys_thread[k]) {
						this.threadID[this.tidCounter] = h[j].sys_thread[k];
						this.threadNames[(this.tidCounter++)] = h[j].name[k];
						found = true;
						break;
					}
				}

				if (found) {
					break;
				}
			}
		}

		this.mouseListener = new MouseHandler();
	}

	public MonitorCompareTableModel(ThreadDump[] h, int col) {
		this.hi = h;
		this.sortedColumn = col;
		this.mouseListener = new MouseHandler();
	}

	public Class<?> getColumnClass(int c) {
		if (c == 0)
			return String.class;
		return CompareCell.class;
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	public String getColumnName(int col) {
		return this.columnNames[col];
	}

	public Icon getHeaderIcon(int column, int size) {
		if (column != this.sortedColumn)
			return null;
		return new SortableHeaderIcon(this.direction, size);
	}

	public int getRowCount() {
		return this.tidHash.size();
	}

	public Object getValueAt(int row, int col) {
		Long tid = (Long) this.tidHash.get(new Integer(row));

		if (col == 0) {
			return this.threadNames[row];
		}

		if (!this.hi[(col - 1)].threadHash2.containsKey(tid))
			return null;
		int idx = ((Integer) this.hi[(col - 1)].threadHash2.get(tid)).intValue();
		for (int i = 0; i < this.hi[(col - 1)].sys_thread.length; i++) {
			if (this.hi[(col - 1)].sys_thread[i] == this.hi[(col - 1)].mdump[idx].owner) {
				idx = i;

				return new MonitorCompareCell(this.hi, col - 1, idx, tid);
			}

		}

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
				MonitorCompareTableModel.this.sortColumn(column, MonitorCompareTableModel.this.direction);

				if (MonitorCompareTableModel.this.tableHeader != null)
					MonitorCompareTableModel.this.tableHeader.repaint();
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
				l.setIcon(MonitorCompareTableModel.this.getHeaderIcon(modelColumn, l.getFont().getSize()));
			}

			return c;
		}
	}

}
