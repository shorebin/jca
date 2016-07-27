package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.html.HTMLDocument;

public class ThreadFrame extends JInternalFrame implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private int dividerLocation = 100;
	private ThreadDump threadDump = null;
	private ThreadTableModel tt = null;
	private JTextPane ivjStackTextPane = null;
	private JSplitPane ivjThreadSplitPane = null;
	private JScrollPane ivjThreadScrollPane = null;
	private JTable ivjThreadTable = null;
	private JScrollPane ivjStackScrollPane = null;
	private Configuration cfg = null;
	private String summaryText = null;
	private JSplitPane ivjJSplitPane1 = null;
	private JScrollPane ivjListScrollPane = null;
	private JList<String> ivjThreadList = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JList<String> ivjBlockList = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JLabel ivjJLabelBlocking = null;
	private JLabel ivjJLabelWaiting = null;

	public ThreadFrame() {
		initialize();
	}

	public ThreadFrame(ThreadDump td) {
		this.threadDump = td;
		initialize();
	}

	public ThreadFrame(String title) {
		super(title);
		initialize();
	}

	public ThreadFrame(String title, ThreadDump td) {
		super(title);
		this.threadDump = td;
		initialize();
	}

	public ThreadFrame(String title, ThreadDump td, Configuration c) {
		super(title);
		this.threadDump = td;
		this.cfg = c;
		this.summaryText = getSummary(td);

		initialize();
	}

	public ThreadFrame(String title, boolean resizable) {
		super(title, resizable);
		initialize();
	}

	public ThreadFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
		initialize();
	}

	public ThreadFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		initialize();
	}

	public ThreadFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		initialize();
	}

	public void blockList_MouseClicked(MouseEvent mouseEvent) {
		int selectedRow = -1;
		if (getBlockList().getSelectedIndex() != -1) {
			selectedRow = -1;
			for (int i = 0; i < this.threadDump.name.length; i++) {
				if (this.threadDump.name[i].compareTo((String) getBlockList().getSelectedValue()) == 0) {
					selectedRow = i;
					break;
				}
			}
			if (selectedRow == -1)
				return;
			String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
					+ this.threadDump.getName(selectedRow) + "<tr><td><B>State</B></td><td>"
					+ this.threadDump.getState(selectedRow);

			String m1 = this.threadDump.getOwningMonitors(selectedRow);
			String monString = "";
			if (m1 != null)
				monString = "Owns Monitor Lock on " + m1;

			m1 = this.threadDump.getWaitingMonitors(selectedRow);
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
			if (this.threadDump.javaStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>" + this.threadDump.javaStack[selectedRow]
						+ "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
			if (this.threadDump.nativeStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
						+ this.threadDump.nativeStack[selectedRow] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
			if (stackTrace.length() == 0)
				stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
			getStackTextPane().setText(stackTrace);
			getStackTextPane().setCaretPosition(0);
		}
	}

	private void connEtoC1(MouseEvent arg1) {
		try {
			threadTable_MouseClicked(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2(MouseEvent arg1) {
		try {
			threadList_MouseClicked(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC3(MouseEvent arg1) {
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

	public String getSummary(ThreadDump td) {
		if (td != null) {
			long total = td.getTotalThread();
			long runnable = td.getRunnable();
			long condition = td.getWCondition();
			long monitor = td.getWMonitor();
			long suspended = td.getSuspended();
			long wait = td.getOWait();
			long blocked = td.getBlocked();
			long deadlock = td.getDeadlock();
			long parked = td.getParked();
			String summary = "<UL><LI>Thread Status Analysis</a><BR><BR><table border=\"1\"><tr><th>Status</th><th>Number of Threads : "
					+ total + "</th><th>Percentage</th><tr><td bgcolor=\"#" + getHTMLColor(this.cfg.deadlock)
					+ "\"><img src=\"" + getClass().getResource("/deadlock_view.gif") + "\"> Deadlock</td><td>"
					+ deadlock + "</td><td>" + Math.round((float) deadlock * 100.0F / (float) total)
					+ " (%)</td></tr><tr><td bgcolor=\"#" + getHTMLColor(this.cfg.runnable) + "\"><img src=\""
					+ getClass().getResource("/run.gif") + "\"> Runnable</td><td>" + runnable + "</td><td>"
					+ Math.round((float) runnable * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.condition) + "\"><img src=\"" + getClass().getResource("/condition.gif")
					+ "\"> Waiting on condition</td><td>" + condition + "</td><td>"
					+ Math.round((float) condition * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.monitor) + "\"><img src=\"" + getClass().getResource("/monitor_wait.gif")
					+ "\"> Waiting on monitor</td><td>" + monitor + "</td><td>"
					+ Math.round((float) monitor * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.suspended) + "\"><img src=\"" + getClass().getResource("/suspend.gif")
					+ "\"> Suspended</td><td>" + suspended + "</td><td>"
					+ Math.round((float) suspended * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.object) + "\"><img src=\"" + getClass().getResource("/waiting.gif")
					+ "\"> Object.wait()</td><td>" + wait + "</td><td>"
					+ Math.round((float) wait * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.blocked) + "\"><img src=\"" + getClass().getResource("/block.gif")
					+ "\"> Blocked</td><td>" + blocked + "</td><td>"
					+ Math.round((float) blocked * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.park) + "\"><img src=\"" + getClass().getResource("/park.gif")
					+ "\"> Parked</td><td>" + parked + "</td><td>" + Math.round((float) parked * 100.0F / (float) total)
					+ " (%)</td></tr></table></UL>";
			return summary + td.getMethodSummary() + td.getAggregationSummary() + td.warning + td.summary;
		}
		return "";
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

	private JScrollPane getThreadScrollPane() {
		if (this.ivjThreadScrollPane == null) {
			try {
				this.ivjThreadScrollPane = new JScrollPane();
				this.ivjThreadScrollPane.setName("ThreadScrollPane");
				this.ivjThreadScrollPane.setVerticalScrollBarPolicy(22);
				this.ivjThreadScrollPane.setHorizontalScrollBarPolicy(32);
				getThreadScrollPane().setViewportView(getThreadTable());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadScrollPane;
	}

	private JSplitPane getThreadSplitPane() {
		if (this.ivjThreadSplitPane == null) {
			try {
				this.ivjThreadSplitPane = new JSplitPane(1);
				this.ivjThreadSplitPane.setName("ThreadSplitPane");
				this.ivjThreadSplitPane.setDividerLocation(300);
				this.ivjThreadSplitPane.setOneTouchExpandable(true);
				getThreadSplitPane().add(getThreadScrollPane(), "left");
				getThreadSplitPane().add(getJSplitPane1(), "right");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadSplitPane;
	}

	private JTable getThreadTable() {
		if (this.ivjThreadTable == null) {
			try {
				this.ivjThreadTable = new JTable();
				this.ivjThreadTable.setName("ThreadTable");
				getThreadScrollPane().setColumnHeaderView(this.ivjThreadTable.getTableHeader());
				getThreadScrollPane().getViewport().setBackingStoreEnabled(true);
				this.ivjThreadTable.setBounds(0, 0, 64, 46);

				this.ivjThreadTable = new JTable() {
					private static final long serialVersionUID = -369226456371002196L;
					TableCellRenderer renderer = new ThreadTableCellRenderer(ThreadFrame.this.cfg);
					TableCellRenderer renderer2 = new ThreadTableCellRenderer2(ThreadFrame.this.cfg);
					TableCellRenderer renderer3 = new ThreadTableCellRenderer3(ThreadFrame.this.cfg,
							ThreadFrame.this.threadDump);

					public TableCellRenderer getCellRenderer(int row, int column) {
						if (getColumnName(column).compareTo(ThreadTableModel.columnNames[0]) == 0)
							return this.renderer3;
						if (getColumnName(column).compareTo(ThreadTableModel.columnNames[1]) == 0)
							return this.renderer;
						if (getColumnName(column).compareTo(ThreadTableModel.columnNames[2]) == 0) {
							return this.renderer2;
						}
						return super.getCellRenderer(row, column);
					}
				};
				this.tt = new ThreadTableModel(this.threadDump);
				this.ivjThreadTable.setModel(this.tt);
				this.tt.setTableHeader(this.ivjThreadTable.getTableHeader());
				getThreadScrollPane().setViewportView(this.ivjThreadTable);

				ListSelectionModel rowSM = this.ivjThreadTable.getSelectionModel();
				rowSM.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;

						ListSelectionModel lsm = (ListSelectionModel) e.getSource();
						if (lsm.isSelectionEmpty()) {
							ThreadFrame.this.getJSplitPane1().setDividerLocation(1);
							ThreadFrame.this.getStackTextPane().setText(ThreadFrame.this.summaryText);
							ThreadFrame.this.getStackTextPane().setCaretPosition(0);
							return;
						}

						int selectedRow = lsm.getMinSelectionIndex();
						if (ThreadFrame.this.tt.direction == true)
							selectedRow = ThreadFrame.this.threadDump.nameArray.length - selectedRow - 1;

						if (ThreadFrame.this.tt.sortedColumn == 0)
							selectedRow = ThreadFrame.this.threadDump.nameArray[selectedRow];
						else if (ThreadFrame.this.tt.sortedColumn == 1)
							selectedRow = ThreadFrame.this.threadDump.stateArray[selectedRow];
						else {
							selectedRow = ThreadFrame.this.threadDump.currentMethodArray[selectedRow];
						}
						String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
								+ ThreadFrame.this.threadDump.getName(selectedRow) + "<tr><td><B>State</B></td><td>"
								+ ThreadFrame.this.threadDump.getState(selectedRow);

						String m1 = ThreadFrame.this.threadDump.getOwningMonitors(selectedRow);
						String monString = "";
						if (m1 != null)
							monString = "Owns Monitor Lock on " + m1;

						m1 = ThreadFrame.this.threadDump.getWaitingMonitors(selectedRow);
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
						if (ThreadFrame.this.threadDump.javaStack[selectedRow] != null)
							stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
									+ ThreadFrame.this.threadDump.javaStack[selectedRow] + "</td></tr>";
						else
							stackTrace = stackTrace
									+ "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
						if (ThreadFrame.this.threadDump.nativeStack[selectedRow] != null)
							stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
									+ ThreadFrame.this.threadDump.nativeStack[selectedRow] + "</td></tr>";
						else
							stackTrace = stackTrace
									+ "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
						if (stackTrace.length() == 0)
							stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
						ThreadFrame.this.getStackTextPane().setText(stackTrace);
						ThreadFrame.this.getStackTextPane().setCaretPosition(0);

						DefaultListModel<String> listModel = new DefaultListModel<String>();
						ThreadFrame.this.getThreadList().setModel(listModel);

						DefaultListModel<String> listModel2 = new DefaultListModel<String>();
						ThreadFrame.this.getBlockList().setModel(listModel2);

						MonitorDump[] md = ThreadFrame.this.threadDump.mdump;
						if (md == null)
							return;
						List<Long> waitingThreads = null;
						for (int i = 0; i < md.length; i++) {
							if (ThreadFrame.this.threadDump.sys_thread[selectedRow] == md[i].owner) {
								if (waitingThreads == null)
									waitingThreads = new ArrayList<Long>(md[i].waiting);
								else {
									waitingThreads.addAll(md[i].waiting);
								}
							}
						}
						ThreadFrame.this.setJLabelWaiting(0);
						if (waitingThreads != null) {
							if (ThreadFrame.this.getJSplitPane1().getDividerLocation() != 1)
								ThreadFrame.this.dividerLocation = ThreadFrame.this.getJSplitPane1()
										.getDividerLocation();
							if (ThreadFrame.this.dividerLocation == 0)
								ThreadFrame.this.dividerLocation = 100;
							ThreadFrame.this.getJSplitPane1().setDividerLocation(ThreadFrame.this.dividerLocation);
							List<String> nameList = new ArrayList<String>();
							for (int i = 0; i < waitingThreads.size(); i++) {
								for (int j = 0; j < ThreadFrame.this.threadDump.sys_thread.length; j++) {
									if (((Long) waitingThreads.get(i))
											.longValue() == ThreadFrame.this.threadDump.sys_thread[j]) {
										if (!nameList.contains(ThreadFrame.this.threadDump.name[j]))
											nameList.add(ThreadFrame.this.threadDump.name[j]);
									}
								}
							}

							if (nameList.size() != 0) {
								String[] sortedName = (String[]) nameList.toArray(new String[nameList.size()]);
								Arrays.sort(sortedName, 0, sortedName.length);
								for (int i = 0; i < sortedName.length; i++) {
									listModel.addElement(sortedName[i]);
								}
								ThreadFrame.this.setJLabelWaiting(listModel.getSize());
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
											.longValue() == ThreadFrame.this.threadDump.sys_thread[selectedRow]) {
										if (!blockList
												.contains(ThreadFrame.this.threadDump.name[ThreadFrame.this.threadDump
														.getIndexFromSysThread(mdump[i].owner)])) {
											blockList.add(ThreadFrame.this.threadDump.name[ThreadFrame.this.threadDump
													.getIndexFromSysThread(mdump[i].owner)]);
										}
									}
								}
							}
						}

						ThreadFrame.this.setJLabelBlocking(0);
						if (blockList.size() != 0) {
							if (ThreadFrame.this.getJSplitPane1().getDividerLocation() != 1)
								ThreadFrame.this.dividerLocation = ThreadFrame.this.getJSplitPane1()
										.getDividerLocation();
							if (ThreadFrame.this.dividerLocation == 0)
								ThreadFrame.this.dividerLocation = 100;
							ThreadFrame.this.getJSplitPane1().setDividerLocation(ThreadFrame.this.dividerLocation);
							String[] sortedName = (String[]) blockList.toArray(new String[blockList.size()]);
							Arrays.sort(sortedName, 0, sortedName.length);
							for (int i = 0; i < sortedName.length; i++) {
								listModel2.addElement(sortedName[i]);
							}
							ThreadFrame.this.setJLabelBlocking(listModel2.getSize());
							blockList = null;
						} else if (waitingThreads == null) {
							if (ThreadFrame.this.getJSplitPane1().getDividerLocation() != 1)
								ThreadFrame.this.dividerLocation = ThreadFrame.this.getJSplitPane1()
										.getDividerLocation();
							ThreadFrame.this.getJSplitPane1().setDividerLocation(1);
						}

					}

				});
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadTable;
	}

	private void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
	}

	private void initConnections() throws Exception {
		getThreadTable().addMouseListener(this.ivjEventHandler);
		getThreadList().addMouseListener(this.ivjEventHandler);
		getBlockList().addMouseListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("ThreadFrame");
			setIconifiable(true);
			setClosable(true);
			setVisible(true);
			setFrameIcon(new ImageIcon(getClass().getResource("/thread_ob.gif")));
			setSize(964, 560);
			setMaximizable(true);
			setResizable(true);
			setContentPane(getThreadSplitPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}

		getStackTextPane().setText(this.summaryText);
		getStackTextPane().setCaretPosition(0);

		getJSplitPane1().setDividerLocation(0);
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			ThreadFrame aThreadFrame = new ThreadFrame();
			frame.setContentPane(aThreadFrame);
			frame.setSize(aThreadFrame.getSize());
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
			for (int i = 0; i < this.threadDump.name.length; i++) {
				if (this.threadDump.name[i].compareTo((String) getThreadList().getSelectedValue()) == 0) {
					selectedRow = i;
					break;
				}
			}
			if (selectedRow == -1)
				return;
			String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
					+ this.threadDump.getName(selectedRow) + "<tr><td><B>State</B></td><td>"
					+ this.threadDump.getState(selectedRow);

			String m1 = this.threadDump.getOwningMonitors(selectedRow);
			String monString = "";
			if (m1 != null)
				monString = "Owns Monitor Lock on " + m1;

			m1 = this.threadDump.getWaitingMonitors(selectedRow);
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
			if (this.threadDump.javaStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>" + this.threadDump.javaStack[selectedRow]
						+ "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
			if (this.threadDump.nativeStack[selectedRow] != null)
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
						+ this.threadDump.nativeStack[selectedRow] + "</td></tr>";
			else
				stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
			if (stackTrace.length() == 0)
				stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
			getStackTextPane().setText(stackTrace);
			getStackTextPane().setCaretPosition(0);
		}
	}

	public void threadTable_MouseClicked(MouseEvent mouseEvent) {
		int selectedRow = getThreadTable().getSelectedRow();
		if (selectedRow == -1)
			return;
		if (this.tt.direction == true)
			selectedRow = this.threadDump.nameArray.length - selectedRow - 1;

		if (this.tt.sortedColumn == 0)
			selectedRow = this.threadDump.nameArray[selectedRow];
		else if (this.tt.sortedColumn == 1)
			selectedRow = this.threadDump.stateArray[selectedRow];
		else if (this.tt.sortedColumn == 2)
			selectedRow = this.threadDump.idArray[selectedRow];
		else {
			selectedRow = this.threadDump.currentMethodArray[selectedRow];
		}
		String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
				+ this.threadDump.getName(selectedRow) + "<tr><td><B>State</B></td><td>"
				+ this.threadDump.getState(selectedRow);

		String m1 = this.threadDump.getOwningMonitors(selectedRow);
		String monString = "";
		if (m1 != null)
			monString = "Owns Monitor Lock on " + m1;

		m1 = this.threadDump.getWaitingMonitors(selectedRow);
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
		if (this.threadDump.javaStack[selectedRow] != null)
			stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>" + this.threadDump.javaStack[selectedRow]
					+ "</td></tr>";
		else
			stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
		if (this.threadDump.nativeStack[selectedRow] != null)
			stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>" + this.threadDump.nativeStack[selectedRow]
					+ "</td></tr>";
		else
			stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
		if (stackTrace.length() == 0)
			stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
		getStackTextPane().setText(stackTrace);
		getStackTextPane().setCaretPosition(0);

		DefaultListModel<String> listModel = new DefaultListModel<String>();
		getThreadList().setModel(listModel);

		DefaultListModel<String> listModel2 = new DefaultListModel<String>();
		getBlockList().setModel(listModel2);

		MonitorDump[] md = this.threadDump.mdump;
		if (md == null)
			return;
		List<Long> waitingThreads = null;
		for (int i = 0; i < md.length; i++) {
			if (this.threadDump.sys_thread[selectedRow] == md[i].owner) {
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
				for (int j = 0; j < this.threadDump.sys_thread.length; j++) {
					if (((Long) waitingThreads.get(i)).longValue() == this.threadDump.sys_thread[j]) {
						if (!nameList.contains(this.threadDump.name[j]))
							nameList.add(this.threadDump.name[j]);
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
					if (((Long) mdump[i].waiting.get(j)).longValue() == this.threadDump.sys_thread[selectedRow]) {
						if (!blockList.contains(
								this.threadDump.name[this.threadDump.getIndexFromSysThread(mdump[i].owner)])) {
							blockList.add(this.threadDump.name[this.threadDump.getIndexFromSysThread(mdump[i].owner)]);
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

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		int selectedRow = -1;
		if (!e.getValueIsAdjusting()) {
			JList<String> list = ((JList<String>) e.getSource());

			if (list.getSelectedIndex() != -1) {
				selectedRow = -1;
				for (int i = 0; i < this.threadDump.name.length; i++) {
					if (this.threadDump.name[i].compareTo((String) list.getSelectedValue()) == 0) {
						selectedRow = i;
						break;
					}
				}
				if (selectedRow == -1)
					return;
				String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
						+ this.threadDump.getName(selectedRow) + "<tr><td><B>State</B></td><td>"
						+ this.threadDump.getState(selectedRow);

				String m1 = this.threadDump.getOwningMonitors(selectedRow);
				String monString = "";
				if (m1 != null)
					monString = "Owns Monitor Lock on " + m1;

				m1 = this.threadDump.getWaitingMonitors(selectedRow);
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
				if (this.threadDump.javaStack[selectedRow] != null)
					stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>"
							+ this.threadDump.javaStack[selectedRow] + "</td></tr>";
				else
					stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
				if (this.threadDump.nativeStack[selectedRow] != null)
					stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>"
							+ this.threadDump.nativeStack[selectedRow] + "</td></tr>";
				else
					stackTrace = stackTrace
							+ "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
				if (stackTrace.length() == 0)
					stackTrace = "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";
				getStackTextPane().setText(stackTrace);
				getStackTextPane().setCaretPosition(0);
			}
		}
	}

	class IvjEventHandler implements MouseListener {
		IvjEventHandler() {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == ThreadFrame.this.getThreadTable())
				ThreadFrame.this.connEtoC1(e);
			if (e.getSource() == ThreadFrame.this.getThreadList())
				ThreadFrame.this.connEtoC2(e);
			if (e.getSource() == ThreadFrame.this.getBlockList())
				ThreadFrame.this.connEtoC3(e);
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
