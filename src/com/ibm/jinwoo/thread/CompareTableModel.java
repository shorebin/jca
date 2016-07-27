package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class CompareTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -448671715778543683L;
	static SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MMM d HH:mm:ss yyyy");
	public static final boolean DESCENDING = true;
	public static final boolean ASCENDING = false;
	public boolean direction = false;
	String[] columnNames = null;
	public String[] threadNames = null;
	public int[] sortedNames;
	ThreadDump[] hi;
	Hashtable<Integer, Long> tidHash = new Hashtable<Integer, Long>();
	int sortedColumn = 0;
	int tidCounter = 0;
	public JTableHeader tableHeader;
	public long[][] sortedArrary;
	public int[][] sortedMethod;
	private MouseListener mouseListener;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public CompareTableModel() {
	}

	public CompareTableModel(ThreadDump[] h) {
		this.hi = h;
		this.sortedColumn = 0;
		this.columnNames = new String[h.length + 1];
		this.columnNames[0] = "Thread";
		Long newTid = null;
		for (int i = 0; i < h.length; i++) {
			if (h[i].timeStamp != 0L)
				this.columnNames[(i + 1)] = (h[i].fileName + " [" + dateFormatter2.format(new Date(h[i].timeStamp))
						+ "]");
			else
				this.columnNames[(i + 1)] = h[i].fileName;
			h[i].threadHash = new Hashtable<Long, Integer>();
			for (int j = 0; j < h[i].tid.length; j++) {
				newTid = new Long(h[i].tid[j]);
				h[i].threadHash.put(newTid, new Integer(j));
				if (!this.tidHash.contains(newTid)) {
					this.tidHash.put(new Integer(this.tidCounter++), newTid);
				}
			}
		}

		this.threadNames = new String[this.tidCounter];
		this.sortedNames = new int[this.tidCounter];
		this.tidCounter = 0;

		HashMap<String, Integer> nameHash = new HashMap<String, Integer>();

		for (int i = 0; i < this.tidHash.size(); i++) {
			Long tid = (Long) this.tidHash.get(new Integer(i));
			boolean first = true;
			for (int j = 0; j < this.hi.length; j++) {
				Integer idx = (Integer) this.hi[j].threadHash.get(tid);
				if (idx != null) {
					if (first) {
						first = false;
						nameHash.clear();
						nameHash.put(this.hi[j].name[idx.intValue()], Integer.valueOf(0));
						this.threadNames[(this.tidCounter++)] = this.hi[j].name[idx.intValue()];
					} else if (!nameHash.containsKey(this.hi[j].name[idx.intValue()])) {
						nameHash.put(this.hi[j].name[idx.intValue()], Integer.valueOf(0));
						this.threadNames[(this.tidCounter - 1)] = (this.threadNames[(this.tidCounter - 1)] + " => "
								+ this.hi[j].name[idx.intValue()]);
					}

				}

			}

		}

		String[] tempNames = new String[this.tidCounter];
		for (int i = 0; i < this.threadNames.length; i++) {
			tempNames[i] = this.threadNames[i];
			this.sortedNames[i] = i;
		}

		Arrays2.sort(tempNames, this.sortedNames);

		this.sortedMethod = new int[h.length][this.threadNames.length];
		String[] tempString = new String[this.threadNames.length];
		for (int j = 0; j < h.length; j++) {
			for (int i = 0; i < this.tidHash.size(); i++) {
				Long tid = (Long) this.tidHash.get(new Integer(i));
				Integer idx = (Integer) this.hi[j].threadHash.get(tid);
				this.sortedMethod[j][i] = i;
				if (idx != null)
					tempString[i] = this.hi[j].getCurrentMethod(idx.intValue());
				else
					tempString[i] = "";
			}
			Arrays2.sort(tempString, this.sortedMethod[j]);
		}

		this.mouseListener = new MouseHandler();
	}

	public CompareTableModel(ThreadDump[] h, int col) {
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

	public String getStack(int x, int y) {
		x = this.sortedNames[x];
		Long tid = (Long) this.tidHash.get(new Integer(x));

		if (!this.hi[y].threadHash.containsKey(tid)) {
			return "The thread does not exist in this thread dump";
		}
		int idx = ((Integer) this.hi[y].threadHash.get(tid)).intValue();

		String stackTrace = "Thread Name : " + this.hi[y].getName(idx) + "<BR>State : " + this.hi[y].getState(idx)
				+ "<BR>";

		String m1 = this.hi[y].getOwningMonitors(idx);
		if (m1 != null)
			stackTrace = stackTrace + "Owns Monitor Lock on " + m1 + "<BR>";

		m1 = this.hi[y].getWaitingMonitors(idx);
		if (m1 != null)
			stackTrace = stackTrace + "Waiting for Monitor Lock on " + m1 + "<BR>";

		if (this.hi[y].javaStack[idx] != null)
			stackTrace = stackTrace + "Java Stack<BR>" + this.hi[y].javaStack[idx];
		else
			stackTrace = stackTrace + "No Java stack trace available";
		if (this.hi[y].nativeStack[idx] != null)
			stackTrace = stackTrace + "<BR>Native Stack<BR>" + this.hi[y].nativeStack[idx];
		return stackTrace;
	}

	public Object getValueAt(int row, int col) {
		if (this.direction)
			row = this.threadNames.length - row - 1;
		Long tid = (Long) this.tidHash.get(new Integer(row));

		if (this.sortedColumn == 0) {
			if (col == 0) {
				return this.threadNames[this.sortedNames[row]];
			}

			tid = (Long) this.tidHash.get(new Integer(this.sortedNames[row]));
			if (!this.hi[(col - 1)].threadHash.containsKey(tid))
				return null;
			int idx = ((Integer) this.hi[(col - 1)].threadHash.get(tid)).intValue();

			return new CompareCell(this.hi, col - 1, idx, tid);
		}

		if (col == 0) {
			return this.threadNames[this.sortedMethod[(this.sortedColumn - 1)][row]];
		}

		tid = (Long) this.tidHash.get(new Integer(this.sortedMethod[(this.sortedColumn - 1)][row]));
		if (!this.hi[(col - 1)].threadHash.containsKey(tid))
			return null;
		int idx = ((Integer) this.hi[(col - 1)].threadHash.get(tid)).intValue();

		return new CompareCell(this.hi, col - 1, idx, tid);
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
				CompareTableModel.this.sortColumn(column, CompareTableModel.this.direction);

				if (CompareTableModel.this.tableHeader != null)
					CompareTableModel.this.tableHeader.repaint();
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
				l.setIcon(CompareTableModel.this.getHeaderIcon(modelColumn, l.getFont().getSize()));
			}

			return c;
		}
	}

}
