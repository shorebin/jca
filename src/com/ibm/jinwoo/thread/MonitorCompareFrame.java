package com.ibm.jinwoo.thread;

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

public class MonitorCompareFrame extends JInternalFrame implements ListSelectionListener {
	private static final long serialVersionUID = -3855680649126551403L;
	Configuration cfg;
	private int dividerLocation = 100;
	private JScrollPane ivjCompareScrollPane = null;
	private JSplitPane ivjCompareSplitPane = null;
	private JTable ivjCompareTable = null;
	private JScrollPane ivjDetailScrollPane = null;
	private JTextPane ivjDetailTextPane = null;
	public ThreadDump[] threadDumps = null;
	private MonitorCompareTableModel tt = null;
	private JSplitPane ivjDetailSplitPane = null;
	private JList<String> ivjThreadList = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JList<String> ivjBlockList = null;
	private JLabel ivjJLabelBlocking = null;
	private JLabel ivjJLabelWaiting = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JScrollPane ivjListScrollPane = null;

	public MonitorCompareFrame() {
		initialize();
	}

	public MonitorCompareFrame(ThreadDump[] td) {
		this.threadDumps = td;
		initialize();
	}

	public MonitorCompareFrame(String title) {
		super(title);
	}

	public MonitorCompareFrame(String title, ThreadDump[] td) {
		super(title);
		this.threadDumps = td;
		initialize();
	}

	public MonitorCompareFrame(String title, ThreadDump[] td, Configuration c) {
		super(title);
		this.cfg = c;
		this.threadDumps = td;
		initialize();
	}

	public MonitorCompareFrame(String title, boolean resizable) {
		super(title, resizable);
	}

	public MonitorCompareFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
	}

	public MonitorCompareFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
	}

	public MonitorCompareFrame(String title, boolean resizable, boolean closable, boolean maximizable,
			boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
	}

	public void blockList_MouseClicked(MouseEvent mouseEvent) {
		if (getBlockList().getSelectedIndex() != -1) {
			int selectedColumn = getCompareTable().getSelectedColumn();
			for (int i = 0; i < this.tt.hi[(selectedColumn - 1)].name.length; i++)
				if (this.tt.hi[(selectedColumn - 1)].name[i]
						.compareTo((String) getBlockList().getSelectedValue()) == 0) {
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
						stackTrace = stackTrace + "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";

					getDetailTextPane().setText(stackTrace);
					getDetailTextPane().setCaretPosition(0);
				}
		}
	}

	public void compareTable_KeyReleased(KeyEvent keyEvent) {
		compareTable_MouseClicked(null);
	}

	public void compareTable_MouseClicked(MouseEvent mouseEvent) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		getThreadList().setModel(listModel);

		DefaultListModel<String> listModel2 = new DefaultListModel<String>();
		getBlockList().setModel(listModel2);

		getDetailTextPane().setText("");
		int selectedThread = -1;

		int selectedRow = getCompareTable().getSelectedRow();
		int selectedColumn = getCompareTable().getSelectedColumn();
		if (selectedColumn == 0) {
			getDetailTextPane().setText(this.tt.threadNames[selectedRow]);
		} else {
			for (int i = 0; i < this.tt.hi[(selectedColumn - 1)].sys_thread.length; i++) {
				if (this.tt.hi[(selectedColumn - 1)].sys_thread[i] == this.tt.threadID[selectedRow]) {
					String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
							+ this.tt.hi[(selectedColumn - 1)].name[i] + "<tr><td><B>State</B></td><td>"
							+ this.tt.hi[(selectedColumn - 1)].getState(i);
					selectedThread = i;

					String monString = "";
					String m1 = this.tt.hi[(selectedColumn - 1)].getOwningMonitors(i);
					if (m1 != null)
						monString = monString + "Owns Monitor Lock on " + m1;

					m1 = this.tt.hi[(selectedColumn - 1)].getWaitingMonitors(i);
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
								+ "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";

					getDetailTextPane().setText(stackTrace);
					getDetailTextPane().setCaretPosition(0);
					break;
				}

			}

			MonitorDump[] md = this.tt.hi[(selectedColumn - 1)].mdump;

			List<Long> waitingThreads = null;
			for (int i = 0; i < md.length; i++) {
				if (this.tt.threadID[selectedRow] == md[i].owner) {
					if (waitingThreads == null)
						waitingThreads = new ArrayList<Long>(md[i].waiting);
					else
						waitingThreads.addAll(md[i].waiting);
				}
			}
			setJLabelWaiting(0);

			if (waitingThreads != null) {
				if (getDetailSplitPane().getDividerLocation() != 1)
					this.dividerLocation = getDetailSplitPane().getDividerLocation();
				if (this.dividerLocation == 0)
					this.dividerLocation = 100;
				getDetailSplitPane().setDividerLocation(this.dividerLocation);

				List<String> nameList = new ArrayList<String>();
				for (int i = 0; i < waitingThreads.size(); i++) {
					for (int j = 0; j < this.tt.hi[(selectedColumn - 1)].sys_thread.length; j++) {
						if (((Long) waitingThreads.get(i))
								.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[j]) {
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
								.longValue() == this.tt.hi[(selectedColumn - 1)].sys_thread[selectedThread]) {
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
				if (getDetailSplitPane().getDividerLocation() != 1)
					this.dividerLocation = getDetailSplitPane().getDividerLocation();
				if (this.dividerLocation == 0)
					this.dividerLocation = 100;
				getDetailSplitPane().setDividerLocation(this.dividerLocation);
				String[] sortedName = (String[]) blockList.toArray(new String[blockList.size()]);
				Arrays.sort(sortedName, 0, sortedName.length);
				for (int i = 0; i < sortedName.length; i++) {
					listModel2.addElement(sortedName[i]);
				}
				setJLabelBlocking(listModel2.getSize());
				blockList = null;
			} else if (waitingThreads == null) {
				if (getDetailSplitPane().getDividerLocation() != 1)
					this.dividerLocation = getDetailSplitPane().getDividerLocation();
				getDetailSplitPane().setDividerLocation(1);
			}
		}
	}

	private void connEtoC1(MouseEvent arg1) {
		try {
			compareTable_MouseClicked(arg1);
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
				getCompareSplitPane().add(getDetailSplitPane(), "right");
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

				this.ivjCompareTable.setDragEnabled(true);
				this.ivjCompareTable.setDefaultRenderer(CompareCell.class,
						new MonitorCompareTableCellRenderer(this.cfg));
				this.tt = new MonitorCompareTableModel(this.threadDumps);
				this.ivjCompareTable.setModel(this.tt);
				getCompareScrollPane().setViewportView(this.ivjCompareTable);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareTable;
	}

	private JScrollPane getDetailScrollPane() {
		if (this.ivjDetailScrollPane == null) {
			try {
				this.ivjDetailScrollPane = new JScrollPane();
				this.ivjDetailScrollPane.setName("DetailScrollPane");
				getDetailScrollPane().setViewportView(getDetailTextPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailScrollPane;
	}

	private JSplitPane getDetailSplitPane() {
		if (this.ivjDetailSplitPane == null) {
			try {
				this.ivjDetailSplitPane = new JSplitPane(1);
				this.ivjDetailSplitPane.setName("DetailSplitPane");
				this.ivjDetailSplitPane.setOneTouchExpandable(true);
				this.ivjDetailSplitPane.setDividerLocation(70);
				getDetailSplitPane().add(getDetailScrollPane(), "right");
				getDetailSplitPane().add(getJPanel1(), "left");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailSplitPane;
	}

	private JTextPane getDetailTextPane() {
		if (this.ivjDetailTextPane == null) {
			try {
				this.ivjDetailTextPane = new JTextPane();
				this.ivjDetailTextPane.setName("DetailTextPane");
				this.ivjDetailTextPane.setDocument(new HTMLDocument());
				this.ivjDetailTextPane.setBounds(0, 0, 408, 424);
				this.ivjDetailTextPane.setEditable(false);
				this.ivjDetailTextPane.setContentType("text/html");

				this.ivjDetailTextPane.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailTextPane;
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

	private void initConnections() throws Exception {
		getCompareTable().addMouseListener(this.ivjEventHandler);
		getCompareTable().addKeyListener(this.ivjEventHandler);
		getThreadList().addMouseListener(this.ivjEventHandler);
		getBlockList().addMouseListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("MonitorCompareFrame");
			setClosable(true);
			setIconifiable(true);
			setVisible(true);
			setFrameIcon(new ImageIcon(getClass().getResource("/client_types_16.gif")));
			setSize(618, 522);
			setMaximizable(true);
			setResizable(true);
			setContentPane(getCompareSplitPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			MonitorCompareFrame aMonitorCompareFrame = new MonitorCompareFrame();
			frame.setContentPane(aMonitorCompareFrame);
			frame.setSize(aMonitorCompareFrame.getSize());
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
		if (getThreadList().getSelectedIndex() != -1) {
			int selectedColumn = getCompareTable().getSelectedColumn();
			for (int i = 0; i < this.tt.hi[(selectedColumn - 1)].name.length; i++)
				if (this.tt.hi[(selectedColumn - 1)].name[i]
						.compareTo((String) getThreadList().getSelectedValue()) == 0) {
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
						stackTrace = stackTrace + "<tr><td><B>Stack Trace</B></td>No stack trace available</td></tr>";

					getDetailTextPane().setText(stackTrace);
					getDetailTextPane().setCaretPosition(0);
				}
		}
	}

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList<String> list = ((JList<String>) e.getSource());

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

						getDetailTextPane().setText(stackTrace);
						getDetailTextPane().setCaretPosition(0);
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
			if (e.getSource() == MonitorCompareFrame.this.getCompareTable())
				MonitorCompareFrame.this.connEtoC2(e);
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == MonitorCompareFrame.this.getCompareTable())
				MonitorCompareFrame.this.connEtoC1(e);
			if (e.getSource() == MonitorCompareFrame.this.getThreadList())
				MonitorCompareFrame.this.connEtoC3(e);
			if (e.getSource() == MonitorCompareFrame.this.getBlockList())
				MonitorCompareFrame.this.connEtoC4(e);
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
