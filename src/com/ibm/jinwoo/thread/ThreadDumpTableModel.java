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

public class ThreadDumpTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3100159133982700067L;

	static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

	static SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMM d HH:mm:ss yyyy");
	public static final boolean DESCENDING = true;
	public static final boolean ASCENDING = false;
	public boolean direction = true;

	String[] columnNames = { "Name", "Timestamp", "Runnable/Total Threads", "Free/Allocated Heap(Free%)",
			"AF(SC)/GC Counter", "Monitor" };
	ThreadInfo hi;
	int sortedColumn = 0;
	public JTableHeader tableHeader;
	public long[][] sortedArrary;
	private MouseListener mouseListener;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public ThreadDumpTableModel() {
	}

	public ThreadDumpTableModel(ThreadInfo h) {
		this.hi = h;
		this.sortedColumn = 0;
		this.mouseListener = new MouseHandler();
	}

	public ThreadDumpTableModel(ThreadInfo h, int col) {
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
		return this.hi.threadDumps.size();
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
		ThreadDump td = (ThreadDump) this.hi.threadDumps.get(row);
		if (col == 0)
			return td.fileName;
		if (col == 1) {
			if (td.timeStamp == -1L)
				return "No Info";

			return dateFormatter2.format(new Date(td.timeStamp));
		}

		if (col == 2)
			return numberFormatter.format(td.numberOfRunnable) + "/" + numberFormatter.format(td.name.length);

		if (col == 3) {
			if ((td.free != -1L) && (td.allocated != -1L) && (td.xmx != -1L)) {
				return numberFormatter.format(td.free) + "/" + numberFormatter.format(td.allocated) + "("
						+ td.free * 100L / td.xmx + "% of Xmx)";
			}
			if ((td.free != -1L) && (td.allocated != -1L)) {
				return numberFormatter.format(td.free) + "/" + numberFormatter.format(td.allocated) + "("
						+ td.free * 100L / td.allocated + "% of allocated size)";
			}

			if ((td.free == -1L) && (td.allocated == -1L)) {
				return "unknown/unknown(unknown)";
			}
			if (td.allocated == -1L) {
				return numberFormatter.format(td.free) + "/unknown(unknown)";
			}
			if (td.free == -1L) {
				return "unknown/" + numberFormatter.format(td.allocated) + "(unknown)";
			}
		} else if (col == 4) {
			if ((td.af != -1L) && (td.gc != -1L))
				return numberFormatter.format(td.af) + "/" + numberFormatter.format(td.gc);
		} else if (col == 5) {
			if (td.deadlock != null)
				return "Deadlock";
			if ((td.mdump != null) && (td.mdump.length != 0))
				return td.mdump.length;
		}
		return "No Info";
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
				ThreadDumpTableModel.this.sortColumn(column, ThreadDumpTableModel.this.direction);

				if (ThreadDumpTableModel.this.tableHeader != null)
					ThreadDumpTableModel.this.tableHeader.repaint();
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
				l.setIcon(ThreadDumpTableModel.this.getHeaderIcon(modelColumn, l.getFont().getSize()));
			}

			return c;
		}
	}

}
