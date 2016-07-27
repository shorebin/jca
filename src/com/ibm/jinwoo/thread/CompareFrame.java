package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLDocument;

public class CompareFrame extends JInternalFrame implements ListSelectionListener {
	private static final long serialVersionUID = -2575095148815756954L;
	private int dividerLocation = 100;
	private JScrollPane ivjCompareScrollPane = null;
	private JSplitPane ivjCompareSplitPane = null;
	private JTable ivjCompareTable = null;
	private ThreadDump[] threadDumps = null;
	private CompareTableModel tt = null;
	private String compareSummary = "";
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	public String summary = null;
	public Configuration cfg;
	private JList<String> ivjBlockList = null;
	private JLabel ivjJLabelBlocking = null;
	private JLabel ivjJLabelWaiting = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JSplitPane ivjJSplitPane1 = null;
	private JScrollPane ivjListScrollPane = null;
	private JScrollPane ivjStackScrollPane = null;
	private JTextPane ivjStackTextPane = null;
	private JList<String> ivjThreadList = null;

	public CompareFrame() {
		initialize();
	}

	public CompareFrame(ThreadDump[] td) {
		this.threadDumps = td;
		initialize();
	}

	public CompareFrame(ThreadDump[] td, Configuration c) {
		this.threadDumps = td;
		this.cfg = c;
		initialize();
	}

	public CompareFrame(String title) {
		super(title);
		initialize();
	}

	public CompareFrame(String title, ThreadDump[] td, Configuration c) {
		super(title);
		this.threadDumps = td;
		this.cfg = c;
		initialize();
	}

	public CompareFrame(String title, boolean resizable) {
		super(title, resizable);
		initialize();
	}

	public CompareFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
		initialize();
	}

	public CompareFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		initialize();
	}

	public CompareFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		initialize();
	}

	public void blockList_MouseClicked(MouseEvent mouseEvent) {
	}

	public void compareTable_KeyReleased(KeyEvent keyEvent) {
		compareTable_MouseReleased(null);
	}

	public void compareTable_MouseReleased(MouseEvent mouseEvent) {
		int selectedRow = getCompareTable().getSelectedRow();
		int selectedColumn = getCompareTable().getSelectedColumn();

		if (selectedColumn == -1) {
			getStackTextPane().setText(this.compareSummary);
			getStackTextPane().setCaretPosition(0);
			return;
		}
		if (this.tt.direction == true)
			selectedRow = this.tt.threadNames.length - selectedRow - 1;
		if (this.tt.sortedColumn == 0) {
			selectedRow = this.tt.sortedNames[selectedRow];
			if (selectedColumn == 0) {
				getStackTextPane().setText(this.tt.threadNames[selectedRow]);
			} else {
				Long tid = (Long) this.tt.tidHash.get(new Integer(selectedRow));

				if (!this.tt.hi[(selectedColumn - 1)].threadHash.containsKey(tid)) {
					getStackTextPane().setText("The thread does not exist in this thread dump");
					return;
				}
				int idx = ((Integer) this.tt.hi[(selectedColumn - 1)].threadHash.get(tid)).intValue();

				String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
						+ this.tt.hi[(selectedColumn - 1)].getName(idx) + "<tr><td><B>State</B></td><td>"
						+ this.tt.hi[(selectedColumn - 1)].getState(idx);

				String m1 = this.tt.hi[(selectedColumn - 1)].getOwningMonitors(idx);
				String monString = "";
				if (m1 != null)
					monString = monString + "Owns Monitor Lock on " + m1;

				m1 = this.tt.hi[(selectedColumn - 1)].getWaitingMonitors(idx);
				if (m1 != null) {
					if (monString.length() == 0)
						monString = monString + "Waiting for Monitor Lock on " + m1;
					else
						monString = monString + "<BR>Waiting for Monitor Lock on " + m1;
				}
				if (monString.length() == 0)
					stackTrace = stackTrace + "</td></tr>";
				else {
					stackTrace = stackTrace + "<tr><td><B>Monitor</B></td><td>" + monString + "</td></tr>";
				}

				if (this.tt.hi[(selectedColumn - 1)].javaStack[idx] != null)
					stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
							+ this.tt.hi[(selectedColumn - 1)].javaStack[idx] + "</td></tr>";
				else
					stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
				if (this.tt.hi[(selectedColumn - 1)].nativeStack[idx] != null)
					stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
							+ this.tt.hi[(selectedColumn - 1)].nativeStack[idx] + "</td></tr>";
				else
					stackTrace = stackTrace
							+ "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
				getStackTextPane().setText(stackTrace);
				getStackTextPane().setCaretPosition(0);

				DefaultListModel<String> listModel = new DefaultListModel<String>();
				getThreadList().setModel(listModel);

				DefaultListModel<String> listModel2 = new DefaultListModel<String>();
				getBlockList().setModel(listModel2);

				MonitorDump[] md = this.tt.hi[(selectedColumn - 1)].mdump;
				List<Long> waitingThreads = null;
				for (int i = 0; i < md.length; i++) {
					if (this.tt.hi[(selectedColumn - 1)].sys_thread[idx] == md[i].owner) {
						if (waitingThreads == null)
							waitingThreads = new ArrayList<Long>(md[i].waiting);
						else {
							waitingThreads.addAll(md[i].waiting);
						}
					}
				}
				setJLabelWaiting(0);
				if (waitingThreads != null) {
					if (getJSplitPane1().getDividerLocation() != 1)
						this.dividerLocation = getJSplitPane1().getDividerLocation();
					if (this.dividerLocation == 0)
						this.dividerLocation = 100;
					getJSplitPane1().setDividerLocation(this.dividerLocation);
					List<String> nameList = new ArrayList<String>();
					for (int i = 0; i < waitingThreads.size(); i++) {
						for (int j = 0; j < this.tt.hi[(selectedColumn - 1)].sys_thread.length; j++) {
							if (((Long) waitingThreads.get(i))
									.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[j]) {
								if (!nameList.contains(this.tt.hi[(selectedColumn - 1)].name[j]))
									nameList.add(this.tt.hi[(selectedColumn - 1)].name[j]);
							}
						}
					}

					if (nameList.size() != 0) {
						String[] sortedName = (String[]) nameList.toArray(new String[nameList.size()]);
						Arrays.sort(sortedName, 0, sortedName.length);
						for (int i = 0; i < sortedName.length; i++) {
							listModel.addElement(sortedName[i]);
						}
						setJLabelWaiting(listModel.getSize());
						nameList = null;
					}
				}

				MonitorDump[] mdump = md;

				List<String> blockList = new ArrayList<String>();

				for (int i = 0; i < mdump.length; i++) {
					if ((mdump[i] != null) && (mdump[i].waiting != null) && (mdump[i].waiting.size() != 0)
							&& (mdump[i].owner != -1L)) {
						for (int j = 0; j < mdump[i].waiting.size(); j++) {
							if (((Long) mdump[i].waiting.get(j))
									.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[idx]) {
								if (!blockList
										.contains(this.tt.hi[(selectedColumn - 1)].name[this.tt.hi[(selectedColumn - 1)]
												.getIndexFromSysThread(mdump[i].owner)])) {
									blockList.add(this.tt.hi[(selectedColumn - 1)].name[this.tt.hi[(selectedColumn - 1)]
											.getIndexFromSysThread(mdump[i].owner)]);
								}
							}
						}
					}
				}

				setJLabelBlocking(0);
				if (blockList.size() != 0) {
					if (getJSplitPane1().getDividerLocation() != 1)
						this.dividerLocation = getJSplitPane1().getDividerLocation();
					if (this.dividerLocation == 0)
						this.dividerLocation = 100;
					getJSplitPane1().setDividerLocation(this.dividerLocation);
					String[] sortedName = (String[]) blockList.toArray(new String[blockList.size()]);
					Arrays.sort(sortedName, 0, sortedName.length);
					for (int i = 0; i < sortedName.length; i++) {
						listModel2.addElement(sortedName[i]);
					}
					setJLabelBlocking(listModel2.getSize());
					blockList = null;
				} else if (waitingThreads == null) {
					if (getJSplitPane1().getDividerLocation() != 1)
						this.dividerLocation = getJSplitPane1().getDividerLocation();
					getJSplitPane1().setDividerLocation(1);
				}

			}

		} else if (selectedColumn == 0) {
			selectedRow = this.tt.sortedMethod[(this.tt.sortedColumn - 1)][selectedRow];
			getStackTextPane().setText(this.tt.threadNames[selectedRow]);
		} else {
			selectedRow = this.tt.sortedMethod[(this.tt.sortedColumn - 1)][selectedRow];
			Long tid = (Long) this.tt.tidHash.get(new Integer(selectedRow));
			if (!this.tt.hi[(selectedColumn - 1)].threadHash.containsKey(tid)) {
				getStackTextPane().setText("The thread does not exist in this thread dump");
				return;
			}

			int idx = ((Integer) this.tt.hi[(selectedColumn - 1)].threadHash.get(tid)).intValue();

			String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
					+ this.tt.hi[(selectedColumn - 1)].getName(idx) + "<tr><td><B>State</B></td><td>"
					+ this.tt.hi[(selectedColumn - 1)].getState(idx);

			String m1 = this.tt.hi[(selectedColumn - 1)].getOwningMonitors(idx);
			String monString = "";
			if (m1 != null)
				monString = monString + "Owns Monitor Lock on " + m1;

			m1 = this.tt.hi[(selectedColumn - 1)].getWaitingMonitors(idx);
			if (m1 != null) {
				if (monString.length() == 0)
					monString = monString + "Waiting for Monitor Lock on " + m1;
				else
					monString = monString + "<BR>Waiting for Monitor Lock on " + m1;
			}
			if (monString.length() == 0)
				stackTrace = stackTrace + "</td></tr>";
			else {
				stackTrace = stackTrace + "<tr><td><B>Monitor</B></td><td>" + monString + "</td></tr>";
			}

			if (this.tt.hi[(selectedColumn - 1)].javaStack[idx] != null)
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
						+ this.tt.hi[(selectedColumn - 1)].javaStack[idx] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
			if (this.tt.hi[(selectedColumn - 1)].nativeStack[idx] != null)
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
						+ this.tt.hi[(selectedColumn - 1)].nativeStack[idx] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
			getStackTextPane().setText(stackTrace);
			getStackTextPane().setCaretPosition(0);

			DefaultListModel<String> listModel = new DefaultListModel<String>();
			getThreadList().setModel(listModel);

			DefaultListModel<String> listModel2 = new DefaultListModel<String>();
			getBlockList().setModel(listModel2);

			MonitorDump[] md = this.tt.hi[(selectedColumn - 1)].mdump;
			List<Long> waitingThreads = null;
			for (int i = 0; i < md.length; i++) {
				if (this.tt.hi[(selectedColumn - 1)].sys_thread[idx] == md[i].owner) {
					if (waitingThreads == null)
						waitingThreads = md[i].waiting;
					else {
						waitingThreads.addAll(md[i].waiting);
					}
				}
			}
			setJLabelWaiting(0);
			if (waitingThreads != null) {
				if (getJSplitPane1().getDividerLocation() != 1)
					this.dividerLocation = getJSplitPane1().getDividerLocation();
				if (this.dividerLocation == 0)
					this.dividerLocation = 100;
				getJSplitPane1().setDividerLocation(this.dividerLocation);
				List<String> nameList = new ArrayList<String>();
				for (int i = 0; i < waitingThreads.size(); i++) {
					for (int j = 0; j < this.tt.hi[(selectedColumn - 1)].sys_thread.length; j++) {
						if (((Long) waitingThreads.get(i))
								.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[j]) {
							if (!nameList.contains(this.tt.hi[(selectedColumn - 1)].name[j]))
								nameList.add(this.tt.hi[(selectedColumn - 1)].name[j]);
						}
					}
				}

				if (nameList.size() != 0) {
					String[] sortedName = (String[]) nameList.toArray(new String[nameList.size()]);
					Arrays.sort(sortedName, 0, sortedName.length);
					for (int i = 0; i < sortedName.length; i++) {
						listModel.addElement(sortedName[i]);
					}
					setJLabelWaiting(listModel.getSize());
					nameList = null;
				}
			}

			MonitorDump[] mdump = md;

			List<String> blockList = new ArrayList<String>();

			for (int i = 0; i < mdump.length; i++) {
				if ((mdump[i] != null) && (mdump[i].waiting != null) && (mdump[i].waiting.size() != 0)
						&& (mdump[i].owner != -1L)) {
					for (int j = 0; j < mdump[i].waiting.size(); j++) {
						if (((Long) mdump[i].waiting.get(j))
								.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[idx]) {
							if (!blockList
									.contains(this.tt.hi[(selectedColumn - 1)].name[this.tt.hi[(selectedColumn - 1)]
											.getIndexFromSysThread(mdump[i].owner)])) {
								blockList.add(this.tt.hi[(selectedColumn - 1)].name[this.tt.hi[(selectedColumn - 1)]
										.getIndexFromSysThread(mdump[i].owner)]);
							}
						}
					}
				}
			}

			setJLabelBlocking(0);
			if (blockList.size() != 0) {
				if (getJSplitPane1().getDividerLocation() != 1)
					this.dividerLocation = getJSplitPane1().getDividerLocation();
				if (this.dividerLocation == 0)
					this.dividerLocation = 100;
				getJSplitPane1().setDividerLocation(this.dividerLocation);
				String[] sortedName = (String[]) blockList.toArray(new String[blockList.size()]);
				Arrays.sort(sortedName, 0, sortedName.length);
				for (int i = 0; i < sortedName.length; i++) {
					listModel2.addElement(sortedName[i]);
				}
				setJLabelBlocking(listModel2.getSize());
				blockList = null;
			} else if (waitingThreads == null) {
				if (getJSplitPane1().getDividerLocation() != 1)
					this.dividerLocation = getJSplitPane1().getDividerLocation();
				getJSplitPane1().setDividerLocation(1);
			}
		}
	}

	private void connEtoC1(MouseEvent arg1) {
		try {
			compareTable_MouseReleased(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2(KeyEvent arg1) {
		try {
			compareTable_KeyReleased(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC3(MouseEvent arg1) {
		try {
			threadList_MouseClicked(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC4(MouseEvent arg1) {
		try {
			blockList_MouseClicked(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private JList<String> getBlockList() {
		if (this.ivjBlockList == null) {
			try {
				this.ivjBlockList = new JList<String>();
				this.ivjBlockList.setName("BlockList");
				this.ivjBlockList.setBounds(0, 0, 160, 120);

				this.ivjBlockList.addListSelectionListener(this);
				this.ivjBlockList.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjBlockList;
	}

	private JScrollPane getCompareScrollPane() {
		if (this.ivjCompareScrollPane == null) {
			try {
				this.ivjCompareScrollPane = new JScrollPane();
				this.ivjCompareScrollPane.setName("CompareScrollPane");
				this.ivjCompareScrollPane.setVerticalScrollBarPolicy(22);
				this.ivjCompareScrollPane.setHorizontalScrollBarPolicy(32);
				getCompareScrollPane().setViewportView(getCompareTable());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareScrollPane;
	}

	private JSplitPane getCompareSplitPane() {
		if (this.ivjCompareSplitPane == null) {
			try {
				this.ivjCompareSplitPane = new JSplitPane(1);
				this.ivjCompareSplitPane.setName("CompareSplitPane");
				this.ivjCompareSplitPane.setDividerLocation(300);
				this.ivjCompareSplitPane.setOneTouchExpandable(true);
				this.ivjCompareSplitPane.setContinuousLayout(false);
				getCompareSplitPane().add(getCompareScrollPane(), "left");
				getCompareSplitPane().add(getJSplitPane1(), "right");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareSplitPane;
	}

	private JTable getCompareTable() {
		if (this.ivjCompareTable == null) {
			try {
				this.ivjCompareTable = new JTable();
				this.ivjCompareTable.setName("CompareTable");
				getCompareScrollPane().setColumnHeaderView(this.ivjCompareTable.getTableHeader());
				getCompareScrollPane().getViewport().setBackingStoreEnabled(true);
				this.ivjCompareTable.setCellSelectionEnabled(true);
				this.ivjCompareTable.setBounds(0, 0, 200, 200);

				if (this.threadDumps[0].pid != -1L) {
					if (!isSamePID(this.threadDumps))
						this.compareSummary = "<UL><B>Thread Comparison Analysis</B><BR><BR><LI>WARNING!! Thread dumps are taken from different processes.<BR>Further analysis is not meaningful<BR><BR>";
					else
						this.compareSummary = ("<UL><B>Thread Comparison Analysis</B><BR><BR><LI>Process ID : "
								+ this.threadDumps[0].pid + "<BR><BR>");
				}
				long endTime;
				long startTime = endTime = this.threadDumps[0].timeStamp;

				if (startTime != -1L) {
					long endGC;
					long startGC = endGC = this.threadDumps[0].gc;
					long endAF;
					long startAF = endAF = this.threadDumps[0].af;
					for (int i = 0; i < this.threadDumps.length; i++) {
						if (startTime > this.threadDumps[i].timeStamp) {
							startTime = this.threadDumps[i].timeStamp;
							startGC = this.threadDumps[i].gc;
							startAF = this.threadDumps[i].af;
						}
						if (endTime < this.threadDumps[i].timeStamp) {
							endTime = this.threadDumps[i].timeStamp;
							endGC = this.threadDumps[i].gc;
							endAF = this.threadDumps[i].af;
						}
					}
					float min = (float) (endTime - startTime) / 60000.0F;

					this.compareSummary = (this.compareSummary + "<LI>First Dump : " + new Date(startTime)
							+ "<BR><BR><LI>Last Dump : " + new Date(endTime) + "<BR><BR>");

					if (startGC != -1L) {
						if (min != 0.0F) {
							if (this.threadDumps[0].isJ9)
								this.compareSummary = (this.compareSummary + "<LI>Global Collections per Minute : "
										+ (float) (endGC - startGC) / min
										+ "<BR><BR><LI>Scavenge Collections per Minute : "
										+ (float) (endAF - startAF) / min + "<BR><BR>");
							else {
								this.compareSummary = (this.compareSummary + "<LI>Garbage Collections per Minute : "
										+ (float) (endGC - startGC) / min
										+ "<BR><BR><LI>Allocation Failures per Minute : "
										+ (float) (endAF - startAF) / min + "<BR><BR>");
							}
						}
						long t = (endTime - startTime) / 1000L;

						long s = t % 60L;
						long m = t / 60L % 60L;
						long h = t / 60L / 60L % 24L;
						long d = t / 60L / 60L / 24L;

						if ((t != 0L) && (s + m + h + d != 0L))
							this.compareSummary = (this.compareSummary + "<LI>Elapsed Time : "
									+ (d == 0L ? ""
											: new StringBuilder(String.valueOf(d)).append(" Day(s) ").toString())
									+ (h == 0L ? ""
											: new StringBuilder(String.valueOf(h)).append(" Hour(s) ").toString())
									+ (m == 0L ? ""
											: new StringBuilder(String.valueOf(m)).append(" Minute(s) ").toString())
									+ (s == 0L ? ""
											: new StringBuilder(String.valueOf(s)).append(" Second(s)").toString())
									+ "<BR><BR>");

					}

				}

				this.ivjCompareTable.setDragEnabled(true);
				this.ivjCompareTable.setDefaultRenderer(CompareCell.class, new CompareTableCellRenderer(this.cfg));
				this.tt = new CompareTableModel(this.threadDumps);

				this.compareSummary += hangSummary(this.tt);

				this.ivjCompareTable.setModel(this.tt);
				this.tt.setTableHeader(this.ivjCompareTable.getTableHeader());
				getCompareScrollPane().setViewportView(this.ivjCompareTable);

				getStackTextPane().setText(this.compareSummary);
				getStackTextPane().setCaretPosition(0);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareTable;
	}

	public String getHTMLColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		String R;
		if (r <= 15)
			R = "0" + Integer.toHexString(r);
		else
			R = Integer.toHexString(r);
		String G;
		if (g <= 15)
			G = "0" + Integer.toHexString(g);
		else
			G = Integer.toHexString(g);
		String B;
		if (b <= 15)
			B = "0" + Integer.toHexString(b);
		else
			B = Integer.toHexString(b);
		return R + G + B;
	}

	private JLabel getJLabelBlocking() {
		if (this.ivjJLabelBlocking == null) {
			try {
				this.ivjJLabelBlocking = new JLabel();
				this.ivjJLabelBlocking.setName("JLabelBlocking");
				this.ivjJLabelBlocking.setText("Blocked by : 0");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelBlocking;
	}

	private JLabel getJLabelWaiting() {
		if (this.ivjJLabelWaiting == null) {
			try {
				this.ivjJLabelWaiting = new JLabel();
				this.ivjJLabelWaiting.setName("JLabelWaiting");
				this.ivjJLabelWaiting.setText("Waiting Threads : 0");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelWaiting;
	}

	private JPanel getJPanel1() {
		if (this.ivjJPanel1 == null) {
			try {
				this.ivjJPanel1 = new JPanel();
				this.ivjJPanel1.setName("JPanel1");
				this.ivjJPanel1.setLayout(new GridBagLayout());

				GridBagConstraints constraintsJLabelWaiting = new GridBagConstraints();
				constraintsJLabelWaiting.gridx = 1;
				constraintsJLabelWaiting.gridy = 1;
				constraintsJLabelWaiting.insets = new Insets(7, 3, 3, 2);
				getJPanel1().add(getJLabelWaiting(), constraintsJLabelWaiting);

				GridBagConstraints constraintsListScrollPane = new GridBagConstraints();
				constraintsListScrollPane.gridx = 1;
				constraintsListScrollPane.gridy = 2;
				constraintsListScrollPane.fill = 1;
				constraintsListScrollPane.weightx = 1.0D;
				constraintsListScrollPane.weighty = 1.0D;
				constraintsListScrollPane.ipadx = 76;
				constraintsListScrollPane.ipady = 280;
				constraintsListScrollPane.insets = new Insets(3, 0, 4, 0);
				getJPanel1().add(getListScrollPane(), constraintsListScrollPane);

				GridBagConstraints constraintsJScrollPane1 = new GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 4;
				constraintsJScrollPane1.fill = 1;
				constraintsJScrollPane1.weightx = 1.0D;
				constraintsJScrollPane1.weighty = 1.0D;
				constraintsJScrollPane1.ipadx = 76;
				constraintsJScrollPane1.ipady = 137;
				constraintsJScrollPane1.insets = new Insets(3, 0, 9, 0);
				getJPanel1().add(getJScrollPane1(), constraintsJScrollPane1);

				GridBagConstraints constraintsJLabelBlocking = new GridBagConstraints();
				constraintsJLabelBlocking.gridx = 1;
				constraintsJLabelBlocking.gridy = 3;
				constraintsJLabelBlocking.ipadx = 15;
				constraintsJLabelBlocking.insets = new Insets(4, 8, 2, 13);
				getJPanel1().add(getJLabelBlocking(), constraintsJLabelBlocking);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJPanel1;
	}

	private JScrollPane getJScrollPane1() {
		if (this.ivjJScrollPane1 == null) {
			try {
				this.ivjJScrollPane1 = new JScrollPane();
				this.ivjJScrollPane1.setName("JScrollPane1");
				getJScrollPane1().setViewportView(getBlockList());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJScrollPane1;
	}

	private JSplitPane getJSplitPane1() {
		if (this.ivjJSplitPane1 == null) {
			try {
				this.ivjJSplitPane1 = new JSplitPane(1);
				this.ivjJSplitPane1.setName("JSplitPane1");
				this.ivjJSplitPane1.setOneTouchExpandable(true);
				this.ivjJSplitPane1.setDividerLocation(150);
				getJSplitPane1().add(getJPanel1(), "left");
				getJSplitPane1().add(getStackScrollPane(), "right");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJSplitPane1;
	}

	private JScrollPane getListScrollPane() {
		if (this.ivjListScrollPane == null) {
			try {
				this.ivjListScrollPane = new JScrollPane();
				this.ivjListScrollPane.setName("ListScrollPane");
				getListScrollPane().setViewportView(getThreadList());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjListScrollPane;
	}

	private JScrollPane getStackScrollPane() {
		if (this.ivjStackScrollPane == null) {
			try {
				this.ivjStackScrollPane = new JScrollPane();
				this.ivjStackScrollPane.setName("StackScrollPane");
				getStackScrollPane().setViewportView(getStackTextPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStackScrollPane;
	}

	private JTextPane getStackTextPane() {
		if (this.ivjStackTextPane == null) {
			try {
				this.ivjStackTextPane = new JTextPane();
				this.ivjStackTextPane.setName("StackTextPane");
				this.ivjStackTextPane.setDocument(new HTMLDocument());
				this.ivjStackTextPane.setBounds(0, 0, 535, 527);
				this.ivjStackTextPane.setEditable(false);
				this.ivjStackTextPane.setContentType("text/html");

				this.ivjStackTextPane.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStackTextPane;
	}

	private JList<String> getThreadList() {
		if (this.ivjThreadList == null) {
			try {
				this.ivjThreadList = new JList<String>();
				this.ivjThreadList.setName("ThreadList");
				this.ivjThreadList.setBounds(0, 0, 160, 120);

				this.ivjThreadList.addListSelectionListener(this);
				this.ivjThreadList.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadList;
	}

	private void handleException(Throwable exception) {
	}

	private String hangSummary(CompareTableModel cTableModel) {
		String summary = "";

		List<String> threadNameList = Collections.synchronizedList(new ArrayList<String>());

		if ((cTableModel != null) && (cTableModel.hi != null) && (cTableModel.hi.length != 1)) {

			ThreadDump[] hi = cTableModel.hi;
			String[] threads = new String[cTableModel.tidHash.size()];
			boolean[] isMoving = new boolean[cTableModel.tidHash.size()];
			for (int i = 0; i < cTableModel.tidHash.size(); i++) {
				Long tid = (Long) cTableModel.tidHash.get(new Integer(i));

				Integer idx = (Integer) hi[0].threadHash.get(tid);
				if (idx != null) {
					threads[i] = hi[0].javaStack[idx.intValue()];
					if (threads[i] == null) {
						isMoving[i] = true;

						continue;
					}
				} else {
					isMoving[i] = true;
					continue;
				}

				for (int j = 1; j < hi.length; j++) {
					idx = (Integer) hi[j].threadHash.get(tid);
					if (idx != null) {
						if ((hi[j].javaStack[idx.intValue()] == null)
								|| (threads[i].compareTo(hi[j].javaStack[idx.intValue()]) != 0)) {
							isMoving[i] = true;

							break;
						}
					} else {
						isMoving[i] = true;

						break;
					}
				}

				idx = (Integer) hi[0].threadHash.get(tid);
				if ((isMoving[i]) && (idx != null)) {
					if (hi[0].macro[idx.intValue()] != 1)
						threadNameList.add(hi[0].name[idx.intValue()]);

				}

			}

			if (threadNameList.size() != 0) {
				String[] nameString = (String[]) threadNameList.toArray(new String[threadNameList.size()]);
				Arrays.sort(nameString);
				summary = "<LI>Number of hang suspects : " + nameString.length
						+ "<BR><BR><LI>List of hang suspects<BR><BR><table border=\"1\"><tr><th>Thread Name</th><th>State</th><th>Method</th></tr>";
				int index = -1;
				String htmlColor = null;
				String imageName = null;
				for (int i = 0; i < nameString.length; i++) {
					index = hi[0].getIndexFromName(nameString[i]);
					switch (hi[0].state[index]) {
					case 0:
						htmlColor = getHTMLColor(this.cfg.runnable);
						imageName = "/run.gif";
						break;
					case 1:
						htmlColor = getHTMLColor(this.cfg.condition);
						imageName = "/condition.gif";
						break;
					case 2:
						htmlColor = getHTMLColor(this.cfg.monitor);
						imageName = "/monitor_wait.gif";
						break;
					case 3:
						htmlColor = getHTMLColor(this.cfg.suspended);
						imageName = "/suspend.gif";
						break;
					case 4:
						htmlColor = getHTMLColor(this.cfg.object);
						imageName = "/waiting.gif";
						break;
					case 5:
						htmlColor = getHTMLColor(this.cfg.blocked);
						imageName = "/block.gif";
					}

					if (hi[0].isDeadlock[index]) {
						htmlColor = getHTMLColor(this.cfg.deadlock);
						imageName = "/deadlock_view.gif";
					}
					summary = summary + "<tr><td>" + nameString[i] + "</td><td bgcolor=\"#" + htmlColor
							+ "\"><img src=\"" + getClass().getResource(imageName) + "\"> " + hi[0].getState(index)
							+ "</td><td>" + hi[0].getCurrentMethod(index) + "</td></tr>";
				}
				summary = summary + "</table>";
			}
		}

		return summary;
	}

	private void initConnections() throws Exception {
		getCompareTable().addMouseListener(this.ivjEventHandler);
		getCompareTable().addKeyListener(this.ivjEventHandler);
		getThreadList().addMouseListener(this.ivjEventHandler);
		getBlockList().addMouseListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("CompareFrame");
			setIconifiable(true);
			setClosable(true);
			setVisible(true);
			setFrameIcon(new ImageIcon(getClass().getResource("/thread_comp.gif")));
			setSize(923, 688);
			setMaximizable(true);
			setResizable(true);
			setContentPane(getCompareSplitPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private boolean isSamePID(ThreadDump[] td) {
		long pid = td[0].pid;
		for (int i = 1; i < td.length; i++) {
			if (pid != td[i].pid)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			CompareFrame aCompareFrame = new CompareFrame();
			frame.setContentPane(aCompareFrame);
			frame.setSize(aCompareFrame.getSize());
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			frame.show();
			Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right,
					frame.getHeight() + insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of javax.swing.JInternalFrame");
			exception.printStackTrace(System.out);
		}
	}

	private void setJLabelBlocking(int n) {
		this.ivjJLabelBlocking.setText("Blocked by : " + n);
	}

	private void setJLabelWaiting(int n) {
		this.ivjJLabelWaiting.setText("Waiting Threads : " + n);
	}

	public void threadList_MouseClicked(MouseEvent mouseEvent) {
		int selectedRow = -1;

		if (getThreadList().getSelectedIndex() != -1) {
			selectedRow = -1;
			int selectedColumn = getCompareTable().getSelectedColumn();
			for (int i = 0; i < this.tt.hi[(selectedColumn - 1)].name.length; i++) {
				if (this.tt.hi[(selectedColumn - 1)].name[i]
						.compareTo((String) getThreadList().getSelectedValue()) == 0) {
					selectedRow = i;
					break;
				}
			}
			if (selectedRow == -1)
				return;
			String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
					+ this.tt.hi[(selectedColumn - 1)].getName(selectedRow) + "<tr><td><B>State</B></td><td>"
					+ this.tt.hi[(selectedColumn - 1)].getState(selectedRow);

			String m1 = this.tt.hi[(selectedColumn - 1)].getOwningMonitors(selectedRow);
			String monString = "";
			if (m1 != null)
				monString = "Owns Monitor Lock on " + m1;

			m1 = this.tt.hi[(selectedColumn - 1)].getWaitingMonitors(selectedRow);
			if (m1 != null) {
				if (monString.length() == 0)
					monString = monString + "Waiting for Monitor Lock on " + m1;
				else
					monString = monString + "<BR>Waiting for Monitor Lock on " + m1;
			}
			if (monString.length() == 0)
				stackTrace = stackTrace + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Monitor</B></td><td>" + monString + "</td></tr>";
			if (this.tt.hi[(selectedColumn - 1)].javaStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
						+ this.tt.hi[(selectedColumn - 1)].javaStack[selectedRow] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
			if (this.tt.hi[(selectedColumn - 1)].nativeStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
						+ this.tt.hi[(selectedColumn - 1)].nativeStack[selectedRow] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
			if (stackTrace.length() == 0)
				stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
			getStackTextPane().setText(stackTrace);
			getStackTextPane().setCaretPosition(0);
		}
	}

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList<String> list = (JList<String>) e.getSource();

			if (list.getSelectedIndex() != -1) {
				int selectedColumn = getCompareTable().getSelectedColumn();
				for (int i = 0; i < this.tt.hi[(selectedColumn - 1)].name.length; i++)
					if (this.tt.hi[(selectedColumn - 1)].name[i].compareTo((String) list.getSelectedValue()) == 0) {
						String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
								+ this.tt.hi[(selectedColumn - 1)].name[i] + "<tr><td><B>State</B></td><td>"
								+ this.tt.hi[(selectedColumn - 1)].getState(i);

						String monString = "";
						String m1 = this.tt.hi[(selectedColumn - 1)].getOwningMonitor(i);
						if (m1 != null)
							monString = monString + "Owns Monitor Lock on " + m1;
						m1 = this.tt.hi[(selectedColumn - 1)].getWaitingMonitor(i);
						if (m1 != null) {
							if (monString.length() != 0)
								monString = monString + "<BR>";
							monString = monString + "Waiting for Monitor Lock on " + m1;
						}
						if (monString.length() == 0)
							stackTrace = stackTrace + "</td></tr>";
						else {
							stackTrace = stackTrace + "<tr><td><B>Monitor</B></td><td>" + monString + "</td></tr>";
						}

						if (this.tt.hi[(selectedColumn - 1)].javaStack[i] == null) {
							stackTrace = stackTrace
									+ "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
						} else {
							stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
									+ this.tt.hi[(selectedColumn - 1)].javaStack[i] + "</td></tr>";
						}
						if (this.tt.hi[(selectedColumn - 1)].nativeStack[i] != null)
							stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
									+ this.tt.hi[(selectedColumn - 1)].nativeStack[i] + "</td></tr>";
						else
							stackTrace = stackTrace
									+ "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";

						getStackTextPane().setText(stackTrace);
						getStackTextPane().setCaretPosition(0);
					}
			}
		}
	}

	class IvjEventHandler implements KeyListener, MouseListener {
		IvjEventHandler() {
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			if (e.getSource() == CompareFrame.this.getCompareTable())
				CompareFrame.this.connEtoC2(e);
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == CompareFrame.this.getCompareTable())
				CompareFrame.this.connEtoC1(e);
			if (e.getSource() == CompareFrame.this.getThreadList())
				CompareFrame.this.connEtoC3(e);
			if (e.getSource() == CompareFrame.this.getBlockList())
				CompareFrame.this.connEtoC4(e);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}
