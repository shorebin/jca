package com.ibm.jinwoo.thread;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.html.HTMLDocument;

public class Analyzer extends JFrame {
	private static final long serialVersionUID = -1571638078807274598L;
	public static final String home = "https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=2245aa39-fa5c-4475-b891-14c205f7333c";
	public DefaultListModel<String> keywordListModel = new DefaultListModel<String>();
	public Properties keywords = null;
	public static final String PROPERTY_FILE_NAME = "jca.properties.xml";
	public static final String TRACE_LIST = "jca.keyword.list";
	public static final String version = "4.0.1";
	ButtonGroup lookAndFeelGroup = new ButtonGroup();
	static String[] stateString = { "Runnable", "Wait on Condition", "Wait on Monitor", "Suspended", "Object.wait",
			"Blocked", "Hang Suspect", "Deadlock", "Parked" };
	ColorComboBoxRenderer colorComboBoxRenderer = null;

	static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
	public Configuration cfg;
	static final String cfgFile = "tdv.cfg";
	ConsolePrintStream console;
	JDialogProgress jp;
	public ThreadInfo ti = new ThreadInfo();
	public ThreadDumpTableModel threadDumpModel = null;
	public JTable threadDumpTable = null;
	private FileTask task;
	private Timer timer;
	private JMenuItem ivjAbout_BoxMenuItem = null;
	private JMenu ivjAnalysisMenu = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JMenuItem ivjExitMenuItem = null;
	private JMenu ivjFileMenu = null;
	private JMenuBar ivjGCAnalyzerJMenuBar = null;
	private JMenuItem ivjHelp_TopicsMenuItem = null;
	private JMenu ivjHelpMenu = null;
	private JPanel ivjJFrameContentPane = null;
	private JMenuItem ivjOpenMenuItem = null;
	private JPanel ivjStatusBarPane = null;
	private JLabel ivjStatusMsg1 = null;
	private JLabel ivjStatusMsg2 = null;
	private JMenu ivjViewMenu = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JInternalFrame ivjConsoleFrame = null;
	private JTextArea ivjConsoleTextArea = null;
	private JScrollPane ivjConsoleScrollPane = null;
	private JCheckBoxMenuItem ivjConsoleCheckBoxMenuItem = null;
	private JCheckBoxMenuItem ivjStatusbarCheckBoxMenuItem = null;
	private JDesktopPane ivjJDesktopPane1 = null;
	private JPanel ivjJDialogContentPane = null;
	private JButton ivjOkButton = null;
	private JDialog ivjOptionDialog = null;
	private JMenuItem ivjOptionMenuItem = null;
	private JLabel ivjJLabel1 = null;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();
	private JButton ivjBrowseButton = null;
	private JCheckBox ivjVerboseCheckBox = null;
	private JMenuItem ivjClearMenuItem = null;
	private JSeparator ivjJSeparator2 = null;
	private JLabel ivjJLabel2 = null;
	private JCheckBox ivjSaveCheckBox = null;
	private JComboBox<String> ivjColorComboBox = null;
	private JTextField ivjDefaultPath = null;
	private JPanel ivjJInternalFrameContentPane1 = null;
	private JInternalFrame ivjThreadDumpTable = null;
	private JScrollPane ivjThreadListScrollPane = null;
	private JMenuItem ivjDetailMenuItem = null;
	private JPopupMenu ivjThreadTableMenu = null;
	private JMenuItem ivjCompareMenuItem = null;
	private JMenuItem ivjMonitorMenuItem = null;
	private JScrollPane ivjDetailScrollPane = null;
	private JSplitPane ivjThreadDumpSplitPane = null;
	private JMenuItem ivjDeleteMenuItem = null;
	private JMenuItem ivjDeleteAllMenuItem = null;
	private JSeparator ivjJSeparator3 = null;
	private JMenuItem ivjCloseAllMenuItem = null;
	private JMenuItem ivjCloseMenuItem = null;
	private JSeparator ivjJSeparator4 = null;
	private JSeparator ivjJSeparator5 = null;
	private JMenuItem ivjMonitorCompareMenuItem = null;
	private JMenuItem ivjCompareMenuItem1 = null;
	private JMenuItem ivjDetail = null;
	private JMenuItem ivjMonitor = null;
	private JMenuItem ivjMonitorCompareMenuItem1 = null;
	private JPanel ivjLookAndFeelPanel = null;
	private JRadioButton ivjMetalRadioButton = null;
	private JRadioButton ivjMotifRadioButton = null;
	private JRadioButton ivjSystemRadioButton = null;
	private JEditorPane ivjDetailJEditorPane = null;
	private JButton ivjJButton11 = null;
	private JButton ivjJButton12 = null;
	private JButton ivjJButton13 = null;
	private JButton ivjJButton131 = null;
	private JButton ivjJButton132 = null;
	private JButton ivjJButton1321 = null;
	private JButton ivjJButton1322 = null;
	private JButton ivjJButton1323 = null;
	private JButton ivjJButton1324 = null;
	private JButton ivjJButton1325 = null;
	private JToggleButton ivjJToggleButton1 = null;
	private JToggleButton ivjJToggleButton11 = null;
	private JButton ivjJButton13251 = null;
	private JButton ivjJButton13252 = null;
	private JCheckBox ivjJCheckBoxFloatable = null;
	private JToolBar ivjJToolBar1 = null;
	private JTabbedPane ivjBasicTabbedPane = null;
	private JButton ivjJButtonAdd = null;
	private JButton ivjJButtonDelete = null;
	private JButton ivjJButtonNew = null;
	private JButton ivjJButtonUpdate = null;
	private JPanel ivjJDialogContentPane1 = null;
	private JLabel ivjJLabel121 = null;
	private JLabel ivjJLabelDescription = null;
	private JLabel ivjJLabelEntry = null;
	private JLabel ivjJLabelList = null;
	private JLabel ivjJLabelMenu = null;
	private JLabel ivjJLabelName = null;
	private JList<String> ivjJListKeyword = null;
	private JScrollPane ivjJScrollPaneDescription = null;
	private JScrollPane ivjJScrollPaneKeywordList = null;
	private JTextField ivjJTextName = null;
	private JTextArea ivjJTextStack = null;
	private JTextField ivjJTextAreaDescription = null;
	private JTextField ivjJTextCategory = null;
	private JMenuItem ivjNativeMemoryAnalysis;
	private JButton ivjJButton13201;
	private OptionPanel1 option1;

	public Analyzer() {
		initialize();
	}

	public Analyzer(String title) {
		super(title);
	}

	public void aFMenuItem_ActionPerformed(ActionEvent actionEvent) {
		JInternalFrame[] jf = getJDesktopPane1().getAllFrames();
		for (int i = 0; i < jf.length; i++) {
			if ((jf[i].isSelected()) && ((jf[i] instanceof HeapFrame))) {
				GCInfo hi = ((HeapFrame) jf[i]).hi;
				if (hi == null) {
					JOptionPane.showMessageDialog(this, "Please select a gc window", "Information", 1);
					return;
				}

				HeapFrame hf = new HeapFrame(((HeapFrame) jf[i]).hi.file.getName() + " Allocation Failure Summary",
						((HeapFrame) jf[i]).hi);
				hf.fileName = ((HeapFrame) jf[i]).fileName;

				long[] min = new long[3];
				long[] max = new long[3];
				long[] timestamp = new long[12];
				long[] sum = new long[3];
				long tmp164_163 = hi.af[0];
				max[0] = tmp164_163;
				min[0] = tmp164_163;
				int minAF = 0;
				for (i = 0; i < hi.free.length; i++)
					if (hi.af[i] != 0L) {
						long tmp200_199 = hi.since[i];
						max[1] = tmp200_199;
						min[1] = tmp200_199;
						minAF = hi.naf[i];
						break;
					}
				long tmp241_240 = hi.completed[0];
				max[2] = tmp241_240;
				min[2] = tmp241_240;

				int numberOfAF = 0;
				int nAF = 999999999;
				int numberOfSet = 0;
				for (i = 0; i < hi.free.length; i++) {
					if (hi.af[i] != 0L) {
						if (hi.naf[i] < nAF) {
							numberOfSet++;
						}
						nAF = hi.naf[i];
						if (hi.af[i] > max[0]) {
							max[0] = hi.af[i];
							timestamp[0] = hi.timestamp[i];
						}
						if (hi.af[i] < min[0])
							min[0] = hi.af[i];
						sum[0] += hi.af[i];

						if (hi.since[i] > max[1]) {
							max[1] = hi.since[i];
						}

						if ((minAF == 1) || (hi.since[i] < min[1])) {
							if (hi.naf[i] != 1) {
								min[1] = hi.since[i];
								timestamp[1] = hi.timestamp[i];
								minAF = hi.naf[i];
							}
						}

						sum[1] += hi.since[i];

						if (hi.completed[i] > max[2]) {
							max[2] = hi.completed[i];
							timestamp[2] = hi.timestamp[i];
						}
						if (hi.completed[i] < min[2])
							min[2] = hi.completed[i];
						sum[2] += hi.completed[i];
						numberOfAF++;
					}
				}
				Object[] header = { "", "Requested avg", "min", "max", "timestamp of max", "Since avg", "min", "max",
						"timestamp of min", "Completed avg", "min", "max", "timestamp of max" };
				Object[][] data = new Object[numberOfSet + 1][13];
				data[0][0] = "Overall";
				for (i = 0; i < 3; i++) {
					data[0][(i * 4 + 1)] = numberFormatter.format(sum[i] / (numberOfAF - numberOfSet));
					data[0][(i * 4 + 2)] = numberFormatter.format(min[i]);
					data[0][(i * 4 + 3)] = numberFormatter.format(max[i]);
					data[0][(i * 4 + 4)] = dateFormatter.format(new Date(timestamp[i]));
				}

				long[] minRequested = new long[numberOfAF];
				long[] maxRequested = new long[numberOfAF];
				long[] sumRequested = new long[numberOfAF];
				long[] timeRequested = new long[numberOfAF];
				long[] countRequested = new long[numberOfAF];
				long[] minSince = new long[numberOfAF];
				long[] maxSince = new long[numberOfAF];
				long[] sumSince = new long[numberOfAF];
				long[] timeSince = new long[numberOfAF];
				long[] countSince = new long[numberOfAF];
				long[] minCompleted = new long[numberOfAF];
				long[] maxCompleted = new long[numberOfAF];
				long[] sumCompleted = new long[numberOfAF];
				long[] timeCompleted = new long[numberOfAF];
				long[] countCompleted = new long[numberOfAF];

				nAF = 999999999;
				int index = -1;
				for (i = 0; i < hi.free.length; i++) {
					if (hi.af[i] != 0L) {
						if (hi.naf[i] < nAF) {
							index++;
							long tmp934_933 = hi.af[i];
							maxRequested[index] = tmp934_933;
							minRequested[index] = tmp934_933;
							long tmp952_951 = hi.since[i];
							maxSince[index] = tmp952_951;
							minSince[index] = tmp952_951;
							minAF = hi.naf[i];
							long tmp979_978 = hi.completed[i];
							maxCompleted[index] = tmp979_978;
							minCompleted[index] = tmp979_978;
						}
						nAF = hi.naf[i];

						if (hi.af[i] > maxRequested[index]) {
							maxRequested[index] = hi.af[i];
							timeRequested[index] = hi.timestamp[i];
						}
						if (hi.af[i] < minRequested[index])
							minRequested[index] = hi.af[i];
						sumRequested[index] += hi.af[i];
						countRequested[index] += 1L;

						if (hi.since[i] > maxSince[index]) {
							maxSince[index] = hi.since[i];
						}
						if ((minAF == 1) || (hi.since[i] < minSince[index])) {
							if (hi.naf[i] != 1) {
								minSince[index] = hi.since[i];
								timeSince[index] = hi.timestamp[i];
								minAF = hi.naf[i];
							}
						}
						sumSince[index] += hi.since[i];
						countSince[index] += 1L;

						if (hi.completed[i] > maxCompleted[index]) {
							maxCompleted[index] = hi.completed[i];
							timeCompleted[index] = hi.timestamp[i];
						}
						if (hi.completed[i] < minCompleted[index])
							minCompleted[index] = hi.completed[i];
						sumCompleted[index] += hi.completed[i];
						countCompleted[index] += 1L;
					}

				}

				for (i = 1; i < numberOfSet + 1; i++) {
					data[i][0] = ("# " + i);

					data[i][1] = numberFormatter.format(sumRequested[(i - 1)] / countRequested[(i - 1)]);
					data[i][2] = numberFormatter.format(minRequested[(i - 1)]);
					data[i][3] = numberFormatter.format(maxRequested[(i - 1)]);
					data[i][4] = dateFormatter.format(new Date(timeRequested[(i - 1)]));

					if (countSince[(i - 1)] < 2L)
						data[i][5] = new Float(0.0F);
					else {
						data[i][5] = numberFormatter.format(sumSince[(i - 1)] / (countSince[(i - 1)] - 1L));
					}
					data[i][6] = numberFormatter.format(minSince[(i - 1)]);
					data[i][7] = numberFormatter.format(maxSince[(i - 1)]);
					data[i][8] = dateFormatter.format(new Date(timeSince[(i - 1)]));

					data[i][9] = numberFormatter.format(sumCompleted[(i - 1)] / countCompleted[(i - 1)]);
					data[i][10] = numberFormatter.format(minCompleted[(i - 1)]);
					data[i][11] = numberFormatter.format(maxCompleted[(i - 1)]);
					data[i][12] = dateFormatter.format(new Date(timeCompleted[(i - 1)]));
				}
				JTable jt = new JTable(data, header) {
					private static final long serialVersionUID = -6366642338271372740L;

					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						return c;
					}
				};
				jt.setDragEnabled(true);
				hf.JScrollPaneSetViewportView(jt);
				getJDesktopPane1().add(hf);
				getJDesktopPane1().getDesktopManager().activateFrame(hf);
				try {
					hf.setSelected(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}

				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Please select any gc window", "Information", 1);
	}

	public void browseButton_ActionPerformed(ActionEvent actionEvent) {
		JFileChooser jf;
		if (this.cfg.workingDir == null) {
			String curDir = System.getProperty("user.dir");
			jf = new JFileChooser(curDir);
		} else {
			jf = new JFileChooser(this.cfg.workingDir);
		}
		jf.setFileSelectionMode(1);

		JDialog jd = new JDialog(this);
		jf.showDialog(jd, "Default directory");
		File f = jf.getSelectedFile();

		if (f == null) {
			return;
		}
		if (!f.exists()) {
			JOptionPane.showMessageDialog(this, "Directory not found:" + f.getAbsoluteFile(), "Directory Open Error",
					2);
			return;
		}
		this.cfg.workingDir = jf.getSelectedFile();
		getDefaultPath().setText(this.cfg.workingDir.getAbsolutePath());
	}

	public void colorComboBox_ActionPerformed(ActionEvent actionEvent) {
		Color selectedColor = this.cfg.runnable;
		int selected = getColorComboBox().getSelectedIndex();
		switch (selected) {
		case 0:
			selectedColor = this.cfg.runnable;
			break;
		case 1:
			selectedColor = this.cfg.condition;
			break;
		case 2:
			selectedColor = this.cfg.monitor;
			break;
		case 3:
			selectedColor = this.cfg.suspended;
			break;
		case 4:
			selectedColor = this.cfg.object;
			break;
		case 5:
			selectedColor = this.cfg.blocked;
			break;
		case 6:
			selectedColor = this.cfg.hang;
			break;
		case 7:
			selectedColor = this.cfg.deadlock;
			break;
		case 8:
			selectedColor = this.cfg.park;
		}

		Color selectedColor2 = JColorChooser.showDialog(this, "Choose Color for " + stateString[selected],
				selectedColor);
		if (selectedColor2 != null) {
			switch (selected) {
			case 0:
				this.cfg.runnable = selectedColor2;
				break;
			case 1:
				this.cfg.condition = selectedColor2;
				break;
			case 2:
				this.cfg.monitor = selectedColor2;
				break;
			case 3:
				this.cfg.suspended = selectedColor2;
				break;
			case 4:
				this.cfg.object = selectedColor2;
				break;
			case 5:
				this.cfg.blocked = selectedColor2;
				break;
			case 6:
				this.cfg.hang = selectedColor2;
				break;
			case 7:
				this.cfg.deadlock = selectedColor2;
				break;
			case 8:
				this.cfg.park = selectedColor2;
			}
		}
	}

	public void compareMenuItem_ActionPerformed(ActionEvent actionEvent) {
		int[] selectedRows = this.threadDumpTable.getSelectedRows();

		if (selectedRows.length == 0L) {
			JOptionPane.showMessageDialog(this, "Please select  thread dumps", "Information", 1);
			return;
		}
		ThreadDump[] td = new ThreadDump[selectedRows.length];
		for (int i = 0; i < selectedRows.length; i++) {
			td[i] = ((ThreadDump) this.ti.threadDumps.get(selectedRows[i]));
		}

		String selectedThreads = "";
		for (int i = 0; i < selectedRows.length; i++) {
			selectedThreads = selectedThreads + this.threadDumpTable.getValueAt(selectedRows[i], 0) + " ";
		}

		CompareFrame tf = new CompareFrame("Compare Threads : " + selectedThreads, td, this.cfg);
		getJDesktopPane1().add(tf);
		getJDesktopPane1().getDesktopManager().activateFrame(tf);
	}

	private void connEtoC1(ActionEvent arg1) {
		try {
			help_TopicsMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC10(ActionEvent arg1) {
		try {
			openMenuItem_ActionPerformed();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC11(ActionEvent arg1) {
		try {
			colorComboBox_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC12(ActionEvent arg1) {
		try {
			deleteMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC13(ActionEvent arg1) {
		try {
			browseButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC14(WindowEvent arg1) {
		try {
			gCAnalyzer_WindowClosing(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC15(ActionEvent arg1) {
		try {
			gCAnalyzer_WindowClosing(null);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC16(ActionEvent arg1) {
		try {
			detailMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC17(ActionEvent arg1) {
		try {
			monitorMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC18(ActionEvent arg1) {
		try {
			deleteMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC19(ActionEvent arg1) {
		try {
			compareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2(ActionEvent arg1) {
		try {
			viewStatusBar();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC20(ActionEvent arg1) {
		try {
			deleteAllMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC21(ActionEvent arg1) {
		try {
			deleteAllMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC22(ActionEvent arg1) {
		try {
			deleteMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC23(ActionEvent arg1) {
		try {
			monitorCompareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC24(ActionEvent arg1) {
		try {
			deleteAllMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC25(ActionEvent arg1) {
		try {
			gCAnalyzer_WindowClosing(null);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC26(ActionEvent arg1) {
		try {
			detailMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC27(ActionEvent arg1) {
		try {
			monitorMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC28(ActionEvent arg1) {
		try {
			compareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC29(ActionEvent arg1) {
		try {
			monitorCompareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC3(ActionEvent arg1) {
		try {
			showAboutBox();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC30(ActionEvent arg1) {
		try {
			viewStatusBar();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC31(ActionEvent arg1) {
		try {
			viewConsole();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC32(ActionEvent arg1) {
		try {
			help_TopicsMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC33(ActionEvent arg1) {
		try {
			showAboutBox();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC34(ListSelectionEvent arg1) {
		try {
			jListKeyword_ValueChanged(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC35(ActionEvent arg1) {
		try {
			jButtonDelete_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC36(ActionEvent arg1) {
		try {
			jButtonAdd_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC37(ActionEvent arg1) {
		try {
			jButtonUpdate_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC4(ActionEvent arg1) {
		try {
			openMenuItem_ActionPerformed();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC5(ActionEvent arg1) {
		try {
			viewConsole();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC6(ActionEvent arg1) {
		try {
			detailMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC7(ActionEvent arg1) {
		try {
			monitorMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC8(ActionEvent arg1) {
		try {
			compareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC9(ActionEvent arg1) {
		try {
			monitorCompareMenuItem_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM1(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Open");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM10(MouseEvent arg1) {
		try {
			getStatusMsg2().setText("Exit");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM11(MouseEvent arg1) {
		try {
			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM12(ActionEvent arg1) {
		try {
			getOptionDialog().show();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM13(ActionEvent arg1) {
		try {
			getConsoleTextArea().setText("");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM14(ItemEvent arg1) {
		try {
			getJToolBar1().setFloatable(false);

			if (getJCheckBoxFloatable().isSelected())
				getJToolBar1().setFloatable(true);
			else {
				getJToolBar1().setFloatable(false);
			}
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM15(ActionEvent arg1) {
		try {
			getJTextName().setText("");

			getJTextStack().setText("");
			getJTextCategory().setText("");
			getJTextAreaDescription().setText("");
			getJListKeyword().clearSelection();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM2(ActionEvent arg1) {
		try {
			Dimension dialogSize = new Dimension(600, 600);
			Dimension frameSize = getSize();
			Point loc = getLocation();
			getOptionDialog().setLocation((frameSize.width - dialogSize.width) / 4 + loc.x,
					(frameSize.height - dialogSize.height) / 6 + loc.y);

			getOptionDialog().show();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM22(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Option");

			getStatusMsg2().setText("Change options");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM23(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM24(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Clear Console");

			getStatusMsg2().setText("Clear content of console");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM25(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM26(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Status bar");

			getStatusMsg2().setText("Hide/Show Status bar");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM27(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM28(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Console");

			getStatusMsg2().setText("Hide/Show Console");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM29(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM3(ActionEvent arg1) {
		try {
			this.cfg.verbose = getVerboseCheckBox().isSelected();
			this.cfg.save = getSaveCheckBox().isSelected();
			if (getMetalRadioButton().isSelected())
				this.cfg.lookAndFeel = 1;
			if (getMotifRadioButton().isSelected())
				this.cfg.lookAndFeel = 0;
			if (getSystemRadioButton().isSelected())
				this.cfg.lookAndFeel = 2;

			getOptionDialog().dispose();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM30(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Help");

			getStatusMsg2().setText("Read tutorial");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM31(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM32(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("About");

			getStatusMsg2().setText("About");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM33(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");

			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM4(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM5(ActionEvent arg1) {
		try {
			getConsoleTextArea().setText("");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM6(MouseEvent arg1) {
		try {
			getStatusMsg2().setText("Open Javacore or Thread Dump");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM7(MouseEvent arg1) {
		try {
			getStatusMsg2().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM8(MouseEvent arg1) {
		try {
			getStatusMsg1().setText("Exit");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoM9(MouseEvent arg1) {
		try {
			getStatusMsg1().setText(" ");
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public void deleteAllMenuItem_ActionPerformed(ActionEvent actionEvent) {
		this.ti.threadDumps.clear();
		this.threadDumpModel.fireTableDataChanged();
		this.threadDumpTable.updateUI();
		getDetailJEditorPane().setText("");
	}

	public void deleteMenuItem_ActionPerformed(ActionEvent actionEvent) {
		int[] selectedRow = this.threadDumpTable.getSelectedRows();

		if (selectedRow.length != 0) {
			ThreadDump[] dump = new ThreadDump[selectedRow.length];
			for (int i = 0; i < selectedRow.length; i++) {
				dump[i] = ((ThreadDump) this.ti.threadDumps.get(selectedRow[i]));
			}
			for (int i = 0; i < dump.length; i++) {
				this.ti.threadDumps.remove(dump[i]);
			}
			this.threadDumpModel.fireTableDataChanged();
			this.threadDumpTable.updateUI();
			getDetailJEditorPane().setText("");
		}
	}

	public void detailMenuItem_ActionPerformed(ActionEvent actionEvent) {
		int[] selectedRows = this.threadDumpTable.getSelectedRows();

		if (selectedRows.length == 0L) {
			JOptionPane.showMessageDialog(this, "Please select a thread dump", "Information", 1);
			return;
		}

		for (int i = 0; i < selectedRows.length; i++) {
			ThreadFrame tf = new ThreadFrame("Thread Detail : " + this.threadDumpTable.getValueAt(selectedRows[i], 0),
					(ThreadDump) this.ti.threadDumps.get(selectedRows[i]), this.cfg);
			getJDesktopPane1().add(tf, "aaa");
			getJDesktopPane1().getDesktopManager().activateFrame(tf);

		}
	}

	public void detailTextPaneSetText(String t) {
		getDetailJEditorPane().setText(t);
	}

	public void displaySummary() {
		int selectedRow = this.threadDumpTable.getSelectedRow();

		ThreadDump td = (ThreadDump) this.ti.threadDumps.get(selectedRow);
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

			String summary = "<LI>Thread Status Analysis</a><BR><BR><table border=\"1\"><tr><th>Status</th><th>Number of Threads : "
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
					+ " (%)</td></tr></table></UL>" + td.getMethodSummary() + td.getAggregationSummary();

			if (td.gcHistory != null) {
				String gcDetail = gcDetail(td.gcHistory);
				if (gcDetail != null)
					getDetailJEditorPane().setText(
							td.warning + td.summary + summary + "<BR><LI>Last Garbage Collection Detail<BR><BR>"
									+ gcDetail + "<BR><LI>Garbage Collection History<BR><BR>" + td.gcHistory);
				else
					getDetailJEditorPane().setText(td.warning + td.summary + summary
							+ "<BR>Garbage Collection History<BR><BR>" + td.gcHistory);
			} else {
				getDetailJEditorPane().setText(td.warning + td.summary + summary);
			}
			getDetailJEditorPane().setCaretPosition(0);
		}
	}

	public void durationMenuItem_ActionPerformed(ActionEvent actionEvent) {
		JInternalFrame[] jf = getJDesktopPane1().getAllFrames();
		for (int i = 0; i < jf.length; i++) {
			if ((jf[i].isSelected()) && ((jf[i] instanceof HeapFrame))) {
				GCInfo hi = ((HeapFrame) jf[i]).hi;
				if (hi == null) {
					JOptionPane.showMessageDialog(this, "Please select a gc window", "Information", 1);
					return;
				}

				HeapFrame hf = new HeapFrame(((HeapFrame) jf[i]).hi.file.getName() + " GC Usage Summary",
						((HeapFrame) jf[i]).hi);
				hf.fileName = ((HeapFrame) jf[i]).fileName;

				long[] min = new long[3];
				long[] max = new long[3];
				long[] timestamp = new long[12];
				long[] sum = new long[3];
				long tmp164_163 = hi.free[0];
				max[0] = tmp164_163;
				min[0] = tmp164_163;
				long tmp180_179 = hi.total[0];
				max[1] = tmp180_179;
				min[1] = tmp180_179;
				long tmp196_195 = hi.freed[0];
				max[2] = tmp196_195;
				min[2] = tmp196_195;

				for (i = 0; i < hi.free.length; i++) {
					if (hi.free[i] > max[0]) {
						max[0] = hi.free[i];
						timestamp[0] = hi.timestamp[i];
					}
					if (hi.free[i] < min[0])
						min[0] = hi.free[i];
					sum[0] += hi.free[i];

					if (hi.total[i] > max[1]) {
						max[1] = hi.total[i];
						timestamp[1] = hi.timestamp[i];
					}
					if (hi.total[i] < min[1])
						min[1] = hi.total[i];
					sum[1] += hi.total[i];

					if (hi.freed[i] > max[2]) {
						max[2] = hi.freed[i];
						timestamp[2] = hi.timestamp[i];
					}
					if (hi.freed[i] < min[2])
						min[2] = hi.freed[i];
					sum[2] += hi.freed[i];
				}

				Object[] header = { "", "Free avg", "min", "max", "timestamp of max", "Total avg", "min", "max",
						"timestamp of max", "Freed avg", "min", "max", "timestamp of max" };
				Object[][] data = new Object[hi.numberOfSet + 1][13];
				data[0][0] = "Overall";
				for (i = 0; i < 3; i++) {
					data[0][(i * 4 + 1)] = numberFormatter.format(sum[i] / hi.free.length);
					data[0][(i * 4 + 2)] = numberFormatter.format(min[i]);
					data[0][(i * 4 + 3)] = numberFormatter.format(max[i]);
					data[0][(i * 4 + 4)] = dateFormatter.format(new Date(timestamp[i]));
				}

				long[] minFree = new long[hi.numberOfSet];
				long[] maxFree = new long[hi.numberOfSet];
				long[] sumFree = new long[hi.numberOfSet];
				long[] timeFree = new long[hi.numberOfSet];
				int[] countFree = new int[hi.numberOfSet];
				long[] minTotal = new long[hi.numberOfSet];
				long[] maxTotal = new long[hi.numberOfSet];
				long[] sumTotal = new long[hi.numberOfSet];
				long[] timeTotal = new long[hi.numberOfSet];
				int[] countTotal = new int[hi.numberOfSet];
				long[] minFreed = new long[hi.numberOfSet];
				long[] maxFreed = new long[hi.numberOfSet];
				long[] sumFreed = new long[hi.numberOfSet];
				long[] timeFreed = new long[hi.numberOfSet];
				int[] countFreed = new int[hi.numberOfSet];

				int nGC = 999999999;
				int index = -1;
				for (i = 0; i < hi.free.length; i++) {
					if (hi.ngc[i] < nGC) {
						index++;
						long tmp850_849 = hi.free[i];
						maxFree[index] = tmp850_849;
						minFree[index] = tmp850_849;
						long tmp868_867 = hi.total[i];
						maxTotal[index] = tmp868_867;
						minTotal[index] = tmp868_867;
						long tmp886_885 = hi.freed[i];
						maxFreed[index] = tmp886_885;
						minFreed[index] = tmp886_885;
					}
					nGC = hi.ngc[i];

					if (hi.free[i] > maxFree[index]) {
						maxFree[index] = hi.free[i];
						timeFree[index] = hi.timestamp[i];
					}
					if (hi.free[i] < minFree[index])
						minFree[index] = hi.free[i];
					sumFree[index] += hi.free[i];
					countFree[index] += 1;

					if (hi.total[i] > maxTotal[index]) {
						maxTotal[index] = hi.total[i];
						timeTotal[index] = hi.timestamp[i];
					}
					if (hi.total[i] < minTotal[index])
						minTotal[index] = hi.total[i];
					sumTotal[index] += hi.total[i];
					countTotal[index] += 1;

					if (hi.freed[i] > maxFreed[index]) {
						maxFreed[index] = hi.freed[i];
						timeFreed[index] = hi.timestamp[i];
					}
					if (hi.freed[i] < minFreed[index])
						minFreed[index] = hi.freed[i];
					sumFreed[index] += hi.freed[i];
					countFreed[index] += 1;
				}

				for (i = 1; i < hi.numberOfSet + 1; i++) {
					data[i][0] = ("# " + i);

					data[i][1] = numberFormatter.format(sumFree[(i - 1)] / countFree[(i - 1)]);
					data[i][2] = numberFormatter.format(minFree[(i - 1)]);
					data[i][3] = numberFormatter.format(maxFree[(i - 1)]);
					data[i][4] = dateFormatter.format(new Date(timeFree[(i - 1)]));

					data[i][5] = numberFormatter.format(sumTotal[(i - 1)] / countTotal[(i - 1)]);
					data[i][6] = numberFormatter.format(minTotal[(i - 1)]);
					data[i][7] = numberFormatter.format(maxTotal[(i - 1)]);
					data[i][8] = dateFormatter.format(new Date(timeTotal[(i - 1)]));

					data[i][9] = numberFormatter.format(sumFreed[(i - 1)] / countFreed[(i - 1)]);
					data[i][10] = numberFormatter.format(minFreed[(i - 1)]);
					data[i][11] = numberFormatter.format(maxFreed[(i - 1)]);
					data[i][12] = dateFormatter.format(new Date(timeFreed[(i - 1)]));
				}
				JTable jt = new JTable(data, header) {
					private static final long serialVersionUID = 2376940768795561099L;

					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						return c;
					}
				};
				jt.setDragEnabled(true);
				hf.JScrollPaneSetViewportView(jt);
				getJDesktopPane1().add(hf);
				getJDesktopPane1().getDesktopManager().activateFrame(hf);
				try {
					hf.setSelected(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}

				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Please select any gc window", "Information", 1);
	}

	public void gC_ListMenuItem_ActionPerformed() {
		JInternalFrame[] jf = getJDesktopPane1().getAllFrames();
		for (int i = 0; i < jf.length; i++) {
			if ((jf[i].isSelected()) && ((jf[i] instanceof HeapFrame))) {
				GCInfo hi = ((HeapFrame) jf[i]).hi;
				if (hi == null) {
					JOptionPane.showMessageDialog(this, "Please select a gc window", "Information", 1);
					return;
				}

				HeapFrame hf = new HeapFrame(((HeapFrame) jf[i]).hi.file.getName() + " GC View",
						((HeapFrame) jf[i]).hi);
				hf.fileName = ((HeapFrame) jf[i]).fileName;
				JTable jt = new JTable() {
					private static final long serialVersionUID = 5190801908050756750L;

					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						return c;
					}
				};
				jt.setDragEnabled(true);
				jt.getTableHeader().setToolTipText("Click to sort ; Click again to sort in reverse order");

				hf.JScrollPaneSetViewportView(jt);
				getJDesktopPane1().add(hf);
				getJDesktopPane1().getDesktopManager().activateFrame(hf);
				try {
					hf.setSelected(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}

				GCTableThread at = new GCTableThread(jt, hi, this);
				at.start();

				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Please select any gc window", "Information", 1);
	}

	public void gCAnalyzer_WindowClosing(WindowEvent windowEvent) {
		if (JOptionPane.showConfirmDialog(this, "Do you want to exit ?", "Exit", 0) == 1)
			return;
		if (this.cfg.save)
			saveConfiguration();
		System.exit(0);
	}

	public String gcDetail(String str) {
		String detail = null;
		String oldspaceSignature = "oldspace=";
		String globalSignature = "globalcount=";
		String scaSignature = "scavengecount=";

		long free = -1L;
		long total = -1L;
		int i = str.indexOf("newspace=");
		if (i >= 0) {
			String newspace = str.substring(i + "newspace=".length());
			int j = newspace.indexOf("/");
			if (j >= 0) {
				free = Long.parseLong(newspace.substring(0, j));
				int k = newspace.indexOf(" ");
				if (k >= 0) {
					int newp = -1;
					total = Long.parseLong(newspace.substring(j + 1, k));
					if (total != 0L)
						newp = (int) (free * 100L / total);
					detail = new String();
					if (newp == -1)
						detail = detail + "Nursery Area Free : " + numberFormatter.format(free) + " bytes Total : "
								+ numberFormatter.format(total) + " bytes<BR>";
					else {
						detail = detail + "Nursery Area Free : " + numberFormatter.format(free) + " bytes Total : "
								+ numberFormatter.format(total) + " bytes " + newp + " % free<BR>";
					}
				}
			}
		}
		free = -1L;
		total = -1L;
		i = str.indexOf(oldspaceSignature);
		if (i >= 0) {
			String oldspace = str.substring(i + oldspaceSignature.length());
			int j = oldspace.indexOf("/");
			if (j >= 0) {
				free = Long.parseLong(oldspace.substring(0, j));
				int k = oldspace.indexOf(" ");
				if (k >= 0) {
					if (detail == null)
						detail = new String();
					try {
						total = Long.parseLong(oldspace.substring(j + 1, k));
						int oldp = -1;
						if (total != 0L)
							oldp = (int) (free * 100L / total);
						if (oldp == -1)
							detail = detail + "Tenured Area Free : " + numberFormatter.format(free) + " bytes Total : "
									+ numberFormatter.format(total) + " bytes<BR>";
						else
							detail = detail + "Tenured Area Free : " + numberFormatter.format(free) + " bytes Total : "
									+ numberFormatter.format(total) + " bytes " + oldp + "% free <BR>";
					} catch (NumberFormatException e) {
						detail = detail + "Tenured Area Free : " + numberFormatter.format(free) + " bytes<BR>";
					}
				}
			}
		}

		free = -1L;
		total = -1L;
		i = str.indexOf("loa=");
		if (i >= 0) {
			String loaspace = str.substring(i + "loa=".length());
			int j = loaspace.indexOf("/");
			if (j >= 0) {
				free = Long.parseLong(loaspace.substring(0, j));

				int k = loaspace.indexOf(" ");
				int m = loaspace.indexOf("\n");

				if (k < 0) {
					k = m;
				} else {
					int o = Math.min(k, m);
					k = o;
				}
				if (k >= 0) {
					int loap = -1;
					total = Long.parseLong(loaspace.substring(j + 1, k));
					if (total != 0L)
						loap = (int) (free * 100L / total);
					if (detail == null)
						detail = new String();
					if (loap == -1)
						detail = detail + "Large Object Area Free : " + numberFormatter.format(free) + " bytes Total : "
								+ numberFormatter.format(total) + " bytes<BR>";
					else {
						detail = detail + "Large Object Area Free : " + numberFormatter.format(free) + " bytes Total : "
								+ numberFormatter.format(total) + " bytes " + loap + " % free<BR>";
					}
				}
			}

		}

		free = -1L;
		i = str.indexOf(globalSignature);
		if (i >= 0) {
			String global = str.substring(i + globalSignature.length());
			int j = global.indexOf(" ");
			if (j >= 0) {
				free = Long.parseLong(global.substring(0, j));
				if (detail == null)
					detail = new String();
				detail = detail + "Global Garbage Collector Counter : " + numberFormatter.format(free) + "<BR>";
			}
		}

		free = -1L;
		i = str.indexOf(scaSignature);
		if (i >= 0) {
			String sca = str.substring(i + scaSignature.length());
			int j = sca.indexOf(" ");

			int m = sca.indexOf("\n");
			if (j < 0) {
				j = m;
			} else {
				int o = Math.min(j, m);
				j = o;
			}
			if (j >= 0) {
				free = Long.parseLong(sca.substring(0, j));
				if (detail == null)
					detail = new String();
				detail = detail + "Scavenge Garbage Collector Counter : " + numberFormatter.format(free) + "<BR>";
			}
		}
		return detail;
	}

	private JMenuItem getAbout_BoxMenuItem() {
		if (this.ivjAbout_BoxMenuItem == null) {
			try {
				this.ivjAbout_BoxMenuItem = new JMenuItem();
				this.ivjAbout_BoxMenuItem.setName("About_BoxMenuItem");
				this.ivjAbout_BoxMenuItem.setIcon(new ImageIcon(getClass().getResource("/about.gif")));
				this.ivjAbout_BoxMenuItem.setMnemonic('A');
				this.ivjAbout_BoxMenuItem.setText("About IBM Thread and Monitor Dump Analyzer for Java Technology");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjAbout_BoxMenuItem;
	}

	private JMenu getAnalysisMenu() {
		if (this.ivjAnalysisMenu == null) {
			try {
				this.ivjAnalysisMenu = new JMenu();
				this.ivjAnalysisMenu.setName("AnalysisMenu");
				this.ivjAnalysisMenu.setMnemonic('A');
				this.ivjAnalysisMenu.setText("Analysis");
				this.ivjAnalysisMenu.add(getNativeMemoryAnalysis());
				this.ivjAnalysisMenu.add(getDetail());
				this.ivjAnalysisMenu.add(getMonitor());
				this.ivjAnalysisMenu.add(getCompareMenuItem1());
				this.ivjAnalysisMenu.add(getMonitorCompareMenuItem1());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjAnalysisMenu;
	}

	private JMenuItem getNativeMemoryAnalysis() {
		if (this.ivjNativeMemoryAnalysis == null) {
			try {
				this.ivjNativeMemoryAnalysis = new JMenuItem();
				this.ivjNativeMemoryAnalysis.setName("Native");
				this.ivjNativeMemoryAnalysis.setIcon(new ImageIcon(getClass().getResource("/ha.gif")));
				this.ivjNativeMemoryAnalysis.setMnemonic('N');
				this.ivjNativeMemoryAnalysis.setText("Native Memory Analysis");

				this.ivjNativeMemoryAnalysis.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Analyzer.this.displayNativeMemoryAnalysis();
					}

				});
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjNativeMemoryAnalysis;
	}

	protected void displayNativeMemoryAnalysis() {
		int[] selectedRows = this.threadDumpTable.getSelectedRows();

		if (selectedRows.length == 0L) {
			JOptionPane.showMessageDialog(this, "Please select a thread dump", "Information", 1);
			return;
		}

		Dimension dim = new Dimension(200, 200);

		int w = getJDesktopPane1().getSize().width;
		int h = getJDesktopPane1().getSize().height;

		int x = (w - dim.width) / 2;
		int y = (h - dim.height) / 2;
		for (int i = 0; i < selectedRows.length; i++) {
			JInternalFrame tf = new JInternalFrame(
					"Native Memory Analysis : " + this.threadDumpTable.getValueAt(selectedRows[i], 0));
			ThreadDump td = (ThreadDump) this.ti.threadDumps.get(selectedRows[i]);
			if ((td != null) && (td.nativeMemoryTree != null)) {
				tf.setVisible(true);
				tf.setIconifiable(true);
				tf.setClosable(true);
				tf.setFrameIcon(new ImageIcon(getClass().getResource("/ha.gif")));
				tf.setMaximizable(true);
				tf.setResizable(true);

				tf.setSize(dim);
				tf.setLocation(x, y);
				x += 20;
				y += 20;

				JScrollPane panel = new JScrollPane(td.nativeMemoryTree);

				tf.setContentPane(panel);
				getJDesktopPane1().add(tf);
				getJDesktopPane1().getDesktopManager().activateFrame(tf);
			} else {
				JOptionPane.showMessageDialog(this, "Please select a thread dump that has native memory information",
						"Information", 1);
				return;
			}
		}
	}

	private JTabbedPane getBasicTabbedPane() {
		if (this.ivjBasicTabbedPane == null) {
			try {
				this.ivjBasicTabbedPane = new JTabbedPane();
				this.ivjBasicTabbedPane.setName("BasicTabbedPane");

				if (this.option1 == null)
					this.option1 = new OptionPanel1(this.cfg);

				this.ivjBasicTabbedPane.insertTab("Basic", null, getJDialogContentPane(), null, 0);
				this.ivjBasicTabbedPane.insertTab("Advanced", null, getJDialogContentPane1(), null, 1);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjBasicTabbedPane;
	}

	private JButton getBrowseButton() {
		if (this.ivjBrowseButton == null) {
			try {
				this.ivjBrowseButton = new JButton();
				this.ivjBrowseButton.setName("BrowseButton");
				this.ivjBrowseButton.setMnemonic('B');
				this.ivjBrowseButton.setText("Browse");
				this.ivjBrowseButton.setBounds(461, 72, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjBrowseButton;
	}

	private JMenuItem getClearMenuItem() {
		if (this.ivjClearMenuItem == null) {
			try {
				this.ivjClearMenuItem = new JMenuItem();
				this.ivjClearMenuItem.setName("ClearMenuItem");
				this.ivjClearMenuItem.setIcon(new ImageIcon(getClass().getResource("/clear.gif")));
				this.ivjClearMenuItem.setMnemonic('C');
				this.ivjClearMenuItem.setText("Clear Console");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjClearMenuItem;
	}

	private JMenuItem getCloseAllMenuItem() {
		if (this.ivjCloseAllMenuItem == null) {
			try {
				this.ivjCloseAllMenuItem = new JMenuItem();
				this.ivjCloseAllMenuItem.setName("CloseAllMenuItem");
				this.ivjCloseAllMenuItem.setIcon(new ImageIcon(getClass().getResource("/remove_all.gif")));
				this.ivjCloseAllMenuItem.setMnemonic('A');
				this.ivjCloseAllMenuItem.setText("Close All Thread Dumps");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCloseAllMenuItem;
	}

	private JMenuItem getCloseMenuItem() {
		if (this.ivjCloseMenuItem == null) {
			try {
				this.ivjCloseMenuItem = new JMenuItem();
				this.ivjCloseMenuItem.setName("CloseMenuItem");
				this.ivjCloseMenuItem.setIcon(new ImageIcon(getClass().getResource("/remove.gif")));
				this.ivjCloseMenuItem.setMnemonic('C');
				this.ivjCloseMenuItem.setText("Close Thread Dumps");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCloseMenuItem;
	}

	private JComboBox<String> getColorComboBox() {
		if (this.ivjColorComboBox == null) {
			try {
				this.ivjColorComboBox = new JComboBox<String>();
				this.ivjColorComboBox.setName("ColorComboBox");
				this.ivjColorComboBox.setMaximumRowCount(8);
				this.ivjColorComboBox.setBounds(180, 164, 243, 25);
				this.ivjColorComboBox.setSelectedIndex(-1);

				this.colorComboBoxRenderer = new ColorComboBoxRenderer(this.cfg, getColorComboBox());
				this.ivjColorComboBox.setRenderer(this.colorComboBoxRenderer);

				this.ivjColorComboBox.addItem("Runnable");
				this.ivjColorComboBox.addItem("Wait on Condition");
				this.ivjColorComboBox.addItem("Wait on Monitor");
				this.ivjColorComboBox.addItem("Suspended");
				this.ivjColorComboBox.addItem("Object.wait");
				this.ivjColorComboBox.addItem("Blocked");
				this.ivjColorComboBox.addItem("Hang Suspect");
				this.ivjColorComboBox.addItem("Deadlock");
				this.ivjColorComboBox.addItem("Parked");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjColorComboBox;
	}

	private JMenuItem getCompareMenuItem() {
		if (this.ivjCompareMenuItem == null) {
			try {
				this.ivjCompareMenuItem = new JMenuItem();
				this.ivjCompareMenuItem.setName("CompareMenuItem");
				this.ivjCompareMenuItem.setIcon(new ImageIcon(getClass().getResource("/thread_comp.gif")));
				this.ivjCompareMenuItem.setMnemonic('C');
				this.ivjCompareMenuItem.setText("Compare Threads");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareMenuItem;
	}

	private JMenuItem getCompareMenuItem1() {
		if (this.ivjCompareMenuItem1 == null) {
			try {
				this.ivjCompareMenuItem1 = new JMenuItem();
				this.ivjCompareMenuItem1.setName("CompareMenuItem1");
				this.ivjCompareMenuItem1.setIcon(new ImageIcon(getClass().getResource("/thread_comp.gif")));
				this.ivjCompareMenuItem1.setMnemonic('C');
				this.ivjCompareMenuItem1.setText("Compare Threads");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompareMenuItem1;
	}

	private JCheckBoxMenuItem getConsoleCheckBoxMenuItem() {
		if (this.ivjConsoleCheckBoxMenuItem == null) {
			try {
				this.ivjConsoleCheckBoxMenuItem = new JCheckBoxMenuItem();
				this.ivjConsoleCheckBoxMenuItem.setName("ConsoleCheckBoxMenuItem");
				this.ivjConsoleCheckBoxMenuItem.setSelected(true);
				this.ivjConsoleCheckBoxMenuItem.setIcon(new ImageIcon(getClass().getResource("/console_view.gif")));
				this.ivjConsoleCheckBoxMenuItem.setMnemonic('n');
				this.ivjConsoleCheckBoxMenuItem.setText("Console");
				this.ivjConsoleCheckBoxMenuItem.setActionCommand("Console");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjConsoleCheckBoxMenuItem;
	}

	private JInternalFrame getConsoleFrame() {
		if (this.ivjConsoleFrame == null) {
			try {
				this.ivjConsoleFrame = new JInternalFrame();
				this.ivjConsoleFrame.setName("ConsoleFrame");
				this.ivjConsoleFrame.setTitle("Console");
				this.ivjConsoleFrame.setIconifiable(true);
				this.ivjConsoleFrame.setVisible(true);
				this.ivjConsoleFrame.setFrameIcon(new ImageIcon(getClass().getResource("/console_view.gif")));
				this.ivjConsoleFrame.setBounds(7, 431, 717, 129);
				this.ivjConsoleFrame.setMaximizable(true);
				this.ivjConsoleFrame.setResizable(true);
				getConsoleFrame().setContentPane(getJInternalFrameContentPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjConsoleFrame;
	}

	private JScrollPane getConsoleScrollPane() {
		if (this.ivjConsoleScrollPane == null) {
			try {
				this.ivjConsoleScrollPane = new JScrollPane();
				this.ivjConsoleScrollPane.setName("ConsoleScrollPane");
				getConsoleScrollPane().setViewportView(getConsoleTextArea());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjConsoleScrollPane;
	}

	private JTextArea getConsoleTextArea() {
		if (this.ivjConsoleTextArea == null) {
			try {
				this.ivjConsoleTextArea = new JTextArea();
				this.ivjConsoleTextArea.setName("ConsoleTextArea");
				this.ivjConsoleTextArea.setBounds(0, 0, 245, 107);
				this.ivjConsoleTextArea.setEditable(false);

				this.ivjConsoleTextArea.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjConsoleTextArea;
	}

	private JTextField getDefaultPath() {
		if (this.ivjDefaultPath == null) {
			try {
				this.ivjDefaultPath = new JTextField();
				this.ivjDefaultPath.setName("DefaultPath");
				this.ivjDefaultPath.setBounds(180, 72, 243, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDefaultPath;
	}

	private JMenuItem getDeleteAllMenuItem() {
		if (this.ivjDeleteAllMenuItem == null) {
			try {
				this.ivjDeleteAllMenuItem = new JMenuItem();
				this.ivjDeleteAllMenuItem.setName("DeleteAllMenuItem");
				this.ivjDeleteAllMenuItem.setIcon(new ImageIcon(getClass().getResource("/remove_all.gif")));
				this.ivjDeleteAllMenuItem.setMnemonic('A');
				this.ivjDeleteAllMenuItem.setText("Close All Thread Dumps");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDeleteAllMenuItem;
	}

	private JMenuItem getDeleteMenuItem() {
		if (this.ivjDeleteMenuItem == null) {
			try {
				this.ivjDeleteMenuItem = new JMenuItem();
				this.ivjDeleteMenuItem.setName("DeleteMenuItem");
				this.ivjDeleteMenuItem.setIcon(new ImageIcon(getClass().getResource("/remove.gif")));
				this.ivjDeleteMenuItem.setMnemonic('l');
				this.ivjDeleteMenuItem.setText("Close Thread Dumps");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDeleteMenuItem;
	}

	private JMenuItem getDetail() {
		if (this.ivjDetail == null) {
			try {
				this.ivjDetail = new JMenuItem();
				this.ivjDetail.setName("Detail");
				this.ivjDetail.setIcon(new ImageIcon(getClass().getResource("/thread_ob.gif")));
				this.ivjDetail.setMnemonic('T');
				this.ivjDetail.setText("Thread Detail");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetail;
	}

	private JEditorPane getDetailJEditorPane() {
		if (this.ivjDetailJEditorPane == null) {
			try {
				this.ivjDetailJEditorPane = new JEditorPane();
				this.ivjDetailJEditorPane.setName("DetailJEditorPane");
				this.ivjDetailJEditorPane.setDocument(new HTMLDocument());
				this.ivjDetailJEditorPane.setText("");
				this.ivjDetailJEditorPane.setBounds(0, 0, 11, 6);
				this.ivjDetailJEditorPane.setEditable(false);
				this.ivjDetailJEditorPane.setContentType("text/html");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailJEditorPane;
	}

	private JMenuItem getDetailMenuItem() {
		if (this.ivjDetailMenuItem == null) {
			try {
				this.ivjDetailMenuItem = new JMenuItem();
				this.ivjDetailMenuItem.setName("DetailMenuItem");
				this.ivjDetailMenuItem.setIcon(new ImageIcon(getClass().getResource("/thread_ob.gif")));
				this.ivjDetailMenuItem.setMnemonic('T');
				this.ivjDetailMenuItem.setText("Thread Detail");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailMenuItem;
	}

	private JScrollPane getDetailScrollPane() {
		if (this.ivjDetailScrollPane == null) {
			try {
				this.ivjDetailScrollPane = new JScrollPane();
				this.ivjDetailScrollPane.setName("DetailScrollPane");
				getDetailScrollPane().setViewportView(getDetailJEditorPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailScrollPane;
	}

	private JMenuItem getExitMenuItem() {
		if (this.ivjExitMenuItem == null) {
			try {
				this.ivjExitMenuItem = new JMenuItem();
				this.ivjExitMenuItem.setName("ExitMenuItem");
				this.ivjExitMenuItem.setIcon(new ImageIcon(getClass().getResource("/close.gif")));
				this.ivjExitMenuItem.setMnemonic('x');
				this.ivjExitMenuItem.setText("Exit");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjExitMenuItem;
	}

	private JMenu getFileMenu() {
		if (this.ivjFileMenu == null) {
			try {
				this.ivjFileMenu = new JMenu();
				this.ivjFileMenu.setName("FileMenu");
				this.ivjFileMenu.setMnemonic('F');
				this.ivjFileMenu.setText("File");
				this.ivjFileMenu.add(getOpenMenuItem());
				this.ivjFileMenu.add(getJSeparator5());
				this.ivjFileMenu.add(getCloseMenuItem());
				this.ivjFileMenu.add(getCloseAllMenuItem());
				this.ivjFileMenu.add(getJSeparator4());
				this.ivjFileMenu.add(getExitMenuItem());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjFileMenu;
	}

	private JMenuBar getGCAnalyzerJMenuBar() {
		if (this.ivjGCAnalyzerJMenuBar == null) {
			try {
				this.ivjGCAnalyzerJMenuBar = new JMenuBar();
				this.ivjGCAnalyzerJMenuBar.setName("GCAnalyzerJMenuBar");
				this.ivjGCAnalyzerJMenuBar.add(getFileMenu());
				this.ivjGCAnalyzerJMenuBar.add(getAnalysisMenu());
				this.ivjGCAnalyzerJMenuBar.add(getViewMenu());
				this.ivjGCAnalyzerJMenuBar.add(getHelpMenu());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjGCAnalyzerJMenuBar;
	}

	private JMenuItem getHelp_TopicsMenuItem() {
		if (this.ivjHelp_TopicsMenuItem == null) {
			try {
				this.ivjHelp_TopicsMenuItem = new JMenuItem();
				this.ivjHelp_TopicsMenuItem.setName("Help_TopicsMenuItem");
				this.ivjHelp_TopicsMenuItem.setIcon(new ImageIcon(getClass().getResource("/help.gif")));
				this.ivjHelp_TopicsMenuItem.setMnemonic('H');
				this.ivjHelp_TopicsMenuItem.setText("Help Topics");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjHelp_TopicsMenuItem;
	}

	private JMenu getHelpMenu() {
		if (this.ivjHelpMenu == null) {
			try {
				this.ivjHelpMenu = new JMenu();
				this.ivjHelpMenu.setName("HelpMenu");
				this.ivjHelpMenu.setMnemonic('H');
				this.ivjHelpMenu.setText("Help");
				this.ivjHelpMenu.add(getHelp_TopicsMenuItem());
				this.ivjHelpMenu.add(getAbout_BoxMenuItem());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjHelpMenu;
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

	public static InputStream getInputStream(File file) throws IOException {
		if (file.getName().endsWith(".gz")) {
			try {
				return new GZIPInputStream(new FileInputStream(file));
			} catch (FileNotFoundException ex) {
				throw ex;
			} catch (IOException e) {
				return new FileInputStream(file);
			}
		}
		return new FileInputStream(file);
	}

	private JButton getJButton11() {
		if (this.ivjJButton11 == null) {
			try {
				this.ivjJButton11 = new JButton();
				this.ivjJButton11.setName("JButton11");
				this.ivjJButton11.setToolTipText("Close Thread Dumps");
				this.ivjJButton11.setText("");
				this.ivjJButton11.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton11.setIcon(new ImageIcon(getClass().getResource("/remove.gif")));
				this.ivjJButton11.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton11.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton11;
	}

	private JButton getJButton12() {
		if (this.ivjJButton12 == null) {
			try {
				this.ivjJButton12 = new JButton();
				this.ivjJButton12.setName("JButton12");
				this.ivjJButton12.setToolTipText("Close All Thread Dumps");
				this.ivjJButton12.setText("");
				this.ivjJButton12.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton12.setIcon(new ImageIcon(getClass().getResource("/remove_all.gif")));
				this.ivjJButton12.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton12.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton12;
	}

	private JButton getJButton13() {
		if (this.ivjJButton13 == null) {
			try {
				this.ivjJButton13 = new JButton();
				this.ivjJButton13.setName("JButton13");
				this.ivjJButton13.setToolTipText("Exit");
				this.ivjJButton13.setText("");
				this.ivjJButton13.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton13.setIcon(new ImageIcon(getClass().getResource("/close.gif")));
				this.ivjJButton13.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton13.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton13;
	}

	private JButton getJButton131() {
		if (this.ivjJButton131 == null) {
			try {
				this.ivjJButton131 = new JButton();
				this.ivjJButton131.setName("JButton131");
				this.ivjJButton131.setToolTipText("Open Thread Dumps");
				this.ivjJButton131.setText("");
				this.ivjJButton131.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton131.setIcon(new ImageIcon(getClass().getResource("/open.gif")));
				this.ivjJButton131.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton131.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton131;
	}

	private JButton getJButton132() {
		if (this.ivjJButton132 == null) {
			try {
				this.ivjJButton132 = new JButton();
				this.ivjJButton132.setName("JButton132");
				this.ivjJButton132.setToolTipText("Thread Detail");
				this.ivjJButton132.setText("");
				this.ivjJButton132.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton132.setIcon(new ImageIcon(getClass().getResource("/thread_ob.gif")));
				this.ivjJButton132.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton132.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton132;
	}

	private JButton getJButton1321() {
		if (this.ivjJButton1321 == null) {
			try {
				this.ivjJButton1321 = new JButton();
				this.ivjJButton1321.setName("JButton1321");
				this.ivjJButton1321.setToolTipText("Monitor Detail");
				this.ivjJButton1321.setText("");
				this.ivjJButton1321.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton1321.setIcon(new ImageIcon(getClass().getResource("/mag.gif")));
				this.ivjJButton1321.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton1321.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton1321;
	}

	private JButton getJButton1322() {
		if (this.ivjJButton1322 == null) {
			try {
				this.ivjJButton1322 = new JButton();
				this.ivjJButton1322.setName("JButton1322");
				this.ivjJButton1322.setToolTipText("Compare Threads");
				this.ivjJButton1322.setText("");
				this.ivjJButton1322.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton1322.setIcon(new ImageIcon(getClass().getResource("/thread_comp.gif")));
				this.ivjJButton1322.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton1322.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton1322;
	}

	private JButton getJButton1323() {
		if (this.ivjJButton1323 == null) {
			try {
				this.ivjJButton1323 = new JButton();
				this.ivjJButton1323.setName("JButton1323");
				this.ivjJButton1323.setToolTipText("Compare Monitors");
				this.ivjJButton1323.setText("");
				this.ivjJButton1323.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton1323.setIcon(new ImageIcon(getClass().getResource("/client_types_16.gif")));
				this.ivjJButton1323.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton1323.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton1323;
	}

	private JButton getJButton1324() {
		if (this.ivjJButton1324 == null) {
			try {
				this.ivjJButton1324 = new JButton();
				this.ivjJButton1324.setName("JButton1324");
				this.ivjJButton1324.setToolTipText("Option");
				this.ivjJButton1324.setText("");
				this.ivjJButton1324.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton1324.setIcon(new ImageIcon(getClass().getResource("/options.gif")));
				this.ivjJButton1324.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton1324.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton1324;
	}

	private JButton getJButton1325() {
		if (this.ivjJButton1325 == null) {
			try {
				this.ivjJButton1325 = new JButton();
				this.ivjJButton1325.setName("JButton1325");
				this.ivjJButton1325.setToolTipText("Clear Console");
				this.ivjJButton1325.setText("");
				this.ivjJButton1325.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton1325.setIcon(new ImageIcon(getClass().getResource("/clear.gif")));
				this.ivjJButton1325.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton1325.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton1325;
	}

	private JButton getJButton13251() {
		if (this.ivjJButton13251 == null) {
			try {
				this.ivjJButton13251 = new JButton();
				this.ivjJButton13251.setName("JButton13251");
				this.ivjJButton13251.setToolTipText("Help");
				this.ivjJButton13251.setText("");
				this.ivjJButton13251.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton13251.setIcon(new ImageIcon(getClass().getResource("/help.gif")));
				this.ivjJButton13251.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton13251.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton13251;
	}

	private JButton getJButton13201() {
		if (this.ivjJButton13201 == null) {
			try {
				this.ivjJButton13201 = new JButton();
				this.ivjJButton13201.setName("JButton13201");
				this.ivjJButton13201.setToolTipText("Native Memory Analysis");
				this.ivjJButton13201.setText("");
				this.ivjJButton13201.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton13201.setIcon(new ImageIcon(getClass().getResource("/ha.gif")));
				this.ivjJButton13201.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton13201.setMinimumSize(new Dimension(27, 27));

				this.ivjJButton13201.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Analyzer.this.displayNativeMemoryAnalysis();
					}

				});
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton13201;
	}

	private JButton getJButton13252() {
		if (this.ivjJButton13252 == null) {
			try {
				this.ivjJButton13252 = new JButton();
				this.ivjJButton13252.setName("JButton13252");
				this.ivjJButton13252.setToolTipText("About");
				this.ivjJButton13252.setText("");
				this.ivjJButton13252.setMaximumSize(new Dimension(27, 27));
				this.ivjJButton13252.setIcon(new ImageIcon(getClass().getResource("/about.gif")));
				this.ivjJButton13252.setPreferredSize(new Dimension(27, 27));
				this.ivjJButton13252.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButton13252;
	}

	private JButton getJButtonAdd() {
		if (this.ivjJButtonAdd == null) {
			try {
				this.ivjJButtonAdd = new JButton();
				this.ivjJButtonAdd.setName("JButtonAdd");
				this.ivjJButtonAdd.setMnemonic('a');
				this.ivjJButtonAdd.setText("Add");
				this.ivjJButtonAdd.setBounds(251, 435, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButtonAdd;
	}

	private JButton getJButtonDelete() {
		if (this.ivjJButtonDelete == null) {
			try {
				this.ivjJButtonDelete = new JButton();
				this.ivjJButtonDelete.setName("JButtonDelete");
				this.ivjJButtonDelete.setMnemonic('d');
				this.ivjJButtonDelete.setText("Delete");
				this.ivjJButtonDelete.setBounds(456, 24, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButtonDelete;
	}

	private JButton getJButtonNew() {
		if (this.ivjJButtonNew == null) {
			try {
				this.ivjJButtonNew = new JButton();
				this.ivjJButtonNew.setName("JButtonNew");
				this.ivjJButtonNew.setMnemonic('n');
				this.ivjJButtonNew.setText("New");
				this.ivjJButtonNew.setBounds(83, 435, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButtonNew;
	}

	private JButton getJButtonUpdate() {
		if (this.ivjJButtonUpdate == null) {
			try {
				this.ivjJButtonUpdate = new JButton();
				this.ivjJButtonUpdate.setName("JButtonUpdate");
				this.ivjJButtonUpdate.setMnemonic('u');
				this.ivjJButtonUpdate.setText("Update");
				this.ivjJButtonUpdate.setBounds(419, 435, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJButtonUpdate;
	}

	private JCheckBox getJCheckBoxFloatable() {
		if (this.ivjJCheckBoxFloatable == null) {
			try {
				this.ivjJCheckBoxFloatable = new JCheckBox();
				this.ivjJCheckBoxFloatable.setName("JCheckBoxFloatable");
				this.ivjJCheckBoxFloatable.setSelected(true);
				this.ivjJCheckBoxFloatable.setToolTipText("Floatable Tool Bar");
				this.ivjJCheckBoxFloatable.setText("Floatable");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJCheckBoxFloatable;
	}

	public JDesktopPane getJDesktopPane1() {
		if (this.ivjJDesktopPane1 == null) {
			try {
				this.ivjJDesktopPane1 = new JDesktopPane();
				this.ivjJDesktopPane1.setName("JDesktopPane1");
				getJDesktopPane1().add(getConsoleFrame(), getConsoleFrame().getName());
				getJDesktopPane1().add(getThreadDumpTable(), getThreadDumpTable().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJDesktopPane1;
	}

	private JPanel getJDialogContentPane() {
		if (this.ivjJDialogContentPane == null) {
			try {
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[5];
				gridBagLayout.rowHeights = new int[7];
				gridBagLayout.columnWeights = new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 4.9E-324D };
				gridBagLayout.rowWeights = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 4.9E-324D };

				this.ivjJDialogContentPane = new JPanel();
				this.ivjJDialogContentPane.setName("JDialogContentPane");
				this.ivjJDialogContentPane.setLayout(gridBagLayout);

				GridBagConstraints gbc_btnOk = new GridBagConstraints();
				gbc_btnOk.insets = new Insets(0, 0, 10, 5);
				gbc_btnOk.gridx = 1;
				gbc_btnOk.gridy = 5;
				this.ivjJDialogContentPane.add(getOkButton(), gbc_btnOk);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(10, 0, 5, 5);
				gbc_textField.fill = 2;
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 0;

				this.ivjJDialogContentPane.add(getDefaultPath(), gbc_textField);

				GridBagConstraints gbc_lblDefaultDirectory = new GridBagConstraints();
				gbc_lblDefaultDirectory.fill = 2;
				gbc_lblDefaultDirectory.insets = new Insets(10, 10, 5, 5);
				gbc_lblDefaultDirectory.gridx = 0;
				gbc_lblDefaultDirectory.gridy = 0;
				this.ivjJDialogContentPane.add(getJLabel1(), gbc_lblDefaultDirectory);

				GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
				gbc_btnBrowse.fill = 2;
				gbc_btnBrowse.insets = new Insets(10, 0, 5, 10);
				gbc_btnBrowse.gridx = 2;
				gbc_btnBrowse.gridy = 0;
				this.ivjJDialogContentPane.add(getBrowseButton(), gbc_btnBrowse);

				GridBagConstraints gbc_chckbxVerboseMode = new GridBagConstraints();
				gbc_chckbxVerboseMode.anchor = 13;
				gbc_chckbxVerboseMode.insets = new Insets(0, 10, 5, 5);
				gbc_chckbxVerboseMode.gridx = 0;
				gbc_chckbxVerboseMode.gridy = 2;
				this.ivjJDialogContentPane.add(getVerboseCheckBox(), gbc_chckbxVerboseMode);

				GridBagConstraints gbc_lblColor = new GridBagConstraints();
				gbc_lblColor.anchor = 17;
				gbc_lblColor.insets = new Insets(0, 10, 5, 5);
				gbc_lblColor.gridx = 0;
				gbc_lblColor.gridy = 1;
				this.ivjJDialogContentPane.add(getJLabel2(), gbc_lblColor);

				GridBagConstraints gbc_chckbxSaveOption = new GridBagConstraints();
				gbc_chckbxSaveOption.anchor = 17;
				gbc_chckbxSaveOption.insets = new Insets(0, 5, 5, 5);
				gbc_chckbxSaveOption.gridx = 1;
				gbc_chckbxSaveOption.gridy = 2;
				this.ivjJDialogContentPane.add(getSaveCheckBox(), gbc_chckbxSaveOption);

				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.insets = new Insets(0, 0, 5, 5);
				gbc_comboBox.fill = 2;
				gbc_comboBox.gridx = 1;
				gbc_comboBox.gridy = 1;
				this.ivjJDialogContentPane.add(getColorComboBox(), gbc_comboBox);

				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.gridwidth = 3;
				gbc_panel.insets = new Insets(0, 10, 5, 10);
				gbc_panel.fill = 1;
				gbc_panel.gridx = 0;
				gbc_panel.gridy = 4;
				this.ivjJDialogContentPane.add(getLookAndFeelPanel(), gbc_panel);

				switch (this.cfg.lookAndFeel) {
				case 1:
					getMetalRadioButton().setSelected(true);
					break;
				case 0:
					getMotifRadioButton().setSelected(true);
					break;
				case 2:
					getSystemRadioButton().setSelected(true);
				}

				getDefaultPath().setText(this.cfg.workingDir.getAbsolutePath());
				getVerboseCheckBox().setSelected(this.cfg.verbose);
				getSaveCheckBox().setSelected(this.cfg.save);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJDialogContentPane;
	}

	private JPanel getJDialogContentPane1() {
		if (this.ivjJDialogContentPane1 == null) {
			try {
				this.ivjJDialogContentPane1 = new JPanel();
				this.ivjJDialogContentPane1.setName("JDialogContentPane1");

				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[4];
				gridBagLayout.rowHeights = new int[8];
				gridBagLayout.columnWeights = new double[] { 0.0D, 1.0D, 0.0D, 4.9E-324D };
				gridBagLayout.rowWeights = new double[] { 1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 4.9E-324D };
				this.ivjJDialogContentPane1.setLayout(gridBagLayout);

				GridBagConstraints gbc_lblNewLabelid = new GridBagConstraints();
				gbc_lblNewLabelid.anchor = 17;
				gbc_lblNewLabelid.insets = new Insets(0, 10, 5, 5);
				gbc_lblNewLabelid.gridx = 0;
				gbc_lblNewLabelid.gridy = 1;
				this.ivjJDialogContentPane1.add(getJLabelName(), gbc_lblNewLabelid);

				GridBagConstraints gbc_lblNewLabel_2st = new GridBagConstraints();
				gbc_lblNewLabel_2st.anchor = 17;
				gbc_lblNewLabel_2st.insets = new Insets(0, 10, 5, 5);
				gbc_lblNewLabel_2st.gridx = 0;
				gbc_lblNewLabel_2st.gridy = 3;
				this.ivjJDialogContentPane1.add(getJLabelDescription(), gbc_lblNewLabel_2st);

				GridBagConstraints gbc_d = new GridBagConstraints();
				gbc_d.anchor = 17;
				gbc_d.insets = new Insets(0, 10, 5, 5);
				gbc_d.gridx = 0;
				gbc_d.gridy = 4;
				this.ivjJDialogContentPane1.add(getJLabelEntry(), gbc_d);

				GridBagConstraints gbc_textFieldid = new GridBagConstraints();
				gbc_textFieldid.gridwidth = 2;
				gbc_textFieldid.insets = new Insets(0, 0, 5, 10);
				gbc_textFieldid.fill = 2;
				gbc_textFieldid.gridx = 1;
				gbc_textFieldid.gridy = 1;
				this.ivjJDialogContentPane1.add(getJTextName(), gbc_textFieldid);

				GridBagConstraints gbc_textField_2des = new GridBagConstraints();
				gbc_textField_2des.gridwidth = 2;
				gbc_textField_2des.insets = new Insets(0, 0, 5, 10);
				gbc_textField_2des.fill = 2;
				gbc_textField_2des.gridx = 1;
				gbc_textField_2des.gridy = 4;
				this.ivjJDialogContentPane1.add(getJTextAreaDescription(), gbc_textField_2des);

				GridBagConstraints gbc_lblIdList = new GridBagConstraints();
				gbc_lblIdList.anchor = 17;
				gbc_lblIdList.insets = new Insets(10, 10, 5, 5);
				gbc_lblIdList.gridx = 0;
				gbc_lblIdList.gridy = 0;
				this.ivjJDialogContentPane1.add(getJLabelList(), gbc_lblIdList);

				JPanel panel = new JPanel();
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.insets = new Insets(0, 0, 5, 0);
				gbc_panel.gridwidth = 3;
				gbc_panel.fill = 1;
				gbc_panel.gridx = 0;
				gbc_panel.gridy = 5;
				this.ivjJDialogContentPane1.add(panel, gbc_panel);

				GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
				gbc_btnNewButton.anchor = 11;
				gbc_btnNewButton.insets = new Insets(10, 0, 5, 10);
				gbc_btnNewButton.gridx = 2;
				gbc_btnNewButton.gridy = 0;
				this.ivjJDialogContentPane1.add(getJButtonDelete(), gbc_btnNewButton);

				GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
				gbc_scrollPane_1.gridwidth = 2;
				gbc_scrollPane_1.insets = new Insets(0, 0, 5, 10);
				gbc_scrollPane_1.fill = 1;
				gbc_scrollPane_1.gridx = 1;
				gbc_scrollPane_1.gridy = 3;
				this.ivjJDialogContentPane1.add(getJScrollPaneDescription(), gbc_scrollPane_1);

				GridBagConstraints gbc_lblNewLabel_1sn = new GridBagConstraints();
				gbc_lblNewLabel_1sn.anchor = 17;
				gbc_lblNewLabel_1sn.insets = new Insets(0, 10, 5, 5);
				gbc_lblNewLabel_1sn.gridx = 0;
				gbc_lblNewLabel_1sn.gridy = 2;
				this.ivjJDialogContentPane1.add(getJLabelMenu(), gbc_lblNewLabel_1sn);

				GridBagConstraints gbc_textField_1sn = new GridBagConstraints();
				gbc_textField_1sn.gridwidth = 2;
				gbc_textField_1sn.insets = new Insets(0, 0, 5, 10);
				gbc_textField_1sn.fill = 2;
				gbc_textField_1sn.gridx = 1;
				gbc_textField_1sn.gridy = 2;

				this.ivjJDialogContentPane1.add(getJTextCategory(), gbc_textField_1sn);

				GridBagConstraints gbc_scrollPane = new GridBagConstraints();
				gbc_scrollPane.insets = new Insets(10, 0, 5, 5);
				gbc_scrollPane.fill = 1;
				gbc_scrollPane.gridx = 1;
				gbc_scrollPane.gridy = 0;
				this.ivjJDialogContentPane1.add(getJScrollPaneKeywordList(), gbc_scrollPane);

				panel.add(getJButtonNew(), getJButtonNew().getName());
				panel.add(getJButtonAdd(), getJButtonAdd().getName());
				panel.add(getJButtonUpdate(), getJButtonUpdate().getName());

				GridBagConstraints gbc_lblWarning = new GridBagConstraints();
				gbc_lblWarning.gridwidth = 3;
				gbc_lblWarning.insets = new Insets(0, 10, 10, 10);
				gbc_lblWarning.gridx = 0;
				gbc_lblWarning.gridy = 6;
				this.ivjJDialogContentPane1.add(getJLabel121(), gbc_lblWarning);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJDialogContentPane1;
	}

	public Container getJFrame() {
		return this;
	}

	private JPanel getJFrameContentPane() {
		if (this.ivjJFrameContentPane == null) {
			try {
				this.ivjJFrameContentPane = new JPanel();
				this.ivjJFrameContentPane.setName("JFrameContentPane");
				this.ivjJFrameContentPane.setLayout(new BorderLayout());
				getJFrameContentPane().add(getStatusBarPane(), "South");
				getJFrameContentPane().add(getJDesktopPane1(), "Center");
				getJFrameContentPane().add(getJToolBar1(), "North");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJFrameContentPane;
	}

	private JPanel getJInternalFrameContentPane() {
		if (this.ivjJInternalFrameContentPane == null) {
			try {
				this.ivjJInternalFrameContentPane = new JPanel();
				this.ivjJInternalFrameContentPane.setName("JInternalFrameContentPane");
				this.ivjJInternalFrameContentPane.setLayout(new BorderLayout());
				getJInternalFrameContentPane().add(getConsoleScrollPane(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJInternalFrameContentPane;
	}

	private JPanel getJInternalFrameContentPane1() {
		if (this.ivjJInternalFrameContentPane1 == null) {
			try {
				this.ivjJInternalFrameContentPane1 = new JPanel();
				this.ivjJInternalFrameContentPane1.setName("JInternalFrameContentPane1");
				this.ivjJInternalFrameContentPane1.setLayout(new BorderLayout());
				getJInternalFrameContentPane1().add(getThreadDumpSplitPane(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJInternalFrameContentPane1;
	}

	private JLabel getJLabel1() {
		if (this.ivjJLabel1 == null) {
			try {
				this.ivjJLabel1 = new JLabel();
				this.ivjJLabel1.setName("JLabel1");
				this.ivjJLabel1.setText("Default directory");
				this.ivjJLabel1.setBounds(38, 72, 104, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel1;
	}

	private JLabel getJLabel121() {
		if (this.ivjJLabel121 == null) {
			try {
				this.ivjJLabel121 = new JLabel();
				this.ivjJLabel121.setName("JLabel121");
				this.ivjJLabel121.setText("NOTE : Thread dumps should be reopened after patterns are updated or added");
				this.ivjJLabel121.setBounds(44, 482, 475, 17);
				this.ivjJLabel121.setForeground(Color.red);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel121;
	}

	private JLabel getJLabel2() {
		if (this.ivjJLabel2 == null) {
			try {
				this.ivjJLabel2 = new JLabel();
				this.ivjJLabel2.setName("JLabel2");
				this.ivjJLabel2.setText("Color");
				this.ivjJLabel2.setBounds(38, 169, 65, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel2;
	}

	private JLabel getJLabelDescription() {
		if (this.ivjJLabelDescription == null) {
			try {
				this.ivjJLabelDescription = new JLabel();
				this.ivjJLabelDescription.setName("JLabelDescription");
				this.ivjJLabelDescription.setText("Stack Trace");
				this.ivjJLabelDescription.setBounds(42, 256, 82, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelDescription;
	}

	private JLabel getJLabelEntry() {
		if (this.ivjJLabelEntry == null) {
			try {
				this.ivjJLabelEntry = new JLabel();
				this.ivjJLabelEntry.setName("JLabelEntry");
				this.ivjJLabelEntry.setText("Description");
				this.ivjJLabelEntry.setBounds(42, 383, 83, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelEntry;
	}

	private JLabel getJLabelList() {
		if (this.ivjJLabelList == null) {
			try {
				this.ivjJLabelList = new JLabel();
				this.ivjJLabelList.setName("JLabelList");
				this.ivjJLabelList.setText("ID List");
				this.ivjJLabelList.setBounds(42, 24, 94, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelList;
	}

	private JLabel getJLabelMenu() {
		if (this.ivjJLabelMenu == null) {
			try {
				this.ivjJLabelMenu = new JLabel();
				this.ivjJLabelMenu.setName("JLabelMenu");
				this.ivjJLabelMenu.setText("Stack Name");
				this.ivjJLabelMenu.setBounds(42, 215, 83, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelMenu;
	}

	private JLabel getJLabelName() {
		if (this.ivjJLabelName == null) {
			try {
				this.ivjJLabelName = new JLabel();
				this.ivjJLabelName.setName("JLabelName");
				this.ivjJLabelName.setText("ID(No spaces)");
				this.ivjJLabelName.setBounds(42, 171, 83, 14);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabelName;
	}

	private JList<String> getJListKeyword() {
		if (this.ivjJListKeyword == null) {
			try {
				this.ivjJListKeyword = new JList<String>();
				this.ivjJListKeyword.setName("JListKeyword");
				this.ivjJListKeyword.setBounds(0, 0, 256, 117);
				this.ivjJListKeyword.setSelectionMode(0);

				String all = this.keywords.getProperty("jca.keyword.list");

				this.keywordListModel.clear();
				this.ivjJListKeyword.setModel(this.keywordListModel);
				if (all != null) {
					StringTokenizer st = new StringTokenizer(all, " ");

					while (st.hasMoreTokens()) {
						this.keywordListModel.addElement(st.nextToken());
					}
				}

			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJListKeyword;
	}

	private JScrollPane getJScrollPaneDescription() {
		if (this.ivjJScrollPaneDescription == null) {
			try {
				this.ivjJScrollPaneDescription = new JScrollPane();
				this.ivjJScrollPaneDescription.setName("JScrollPaneDescription");
				this.ivjJScrollPaneDescription.setBounds(167, 256, 382, 100);
				getJScrollPaneDescription().setViewportView(getJTextStack());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJScrollPaneDescription;
	}

	private JScrollPane getJScrollPaneKeywordList() {
		if (this.ivjJScrollPaneKeywordList == null) {
			try {
				this.ivjJScrollPaneKeywordList = new JScrollPane();
				this.ivjJScrollPaneKeywordList.setName("JScrollPaneKeywordList");
				this.ivjJScrollPaneKeywordList.setBounds(167, 24, 259, 120);
				getJScrollPaneKeywordList().setViewportView(getJListKeyword());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJScrollPaneKeywordList;
	}

	private JSeparator getJSeparator2() {
		if (this.ivjJSeparator2 == null) {
			try {
				this.ivjJSeparator2 = new JSeparator();
				this.ivjJSeparator2.setName("JSeparator2");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJSeparator2;
	}

	private JSeparator getJSeparator3() {
		if (this.ivjJSeparator3 == null) {
			try {
				this.ivjJSeparator3 = new JSeparator();
				this.ivjJSeparator3.setName("JSeparator3");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJSeparator3;
	}

	private JSeparator getJSeparator4() {
		if (this.ivjJSeparator4 == null) {
			try {
				this.ivjJSeparator4 = new JSeparator();
				this.ivjJSeparator4.setName("JSeparator4");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJSeparator4;
	}

	private JSeparator getJSeparator5() {
		if (this.ivjJSeparator5 == null) {
			try {
				this.ivjJSeparator5 = new JSeparator();
				this.ivjJSeparator5.setName("JSeparator5");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJSeparator5;
	}

	private JTextField getJTextAreaDescription() {
		if (this.ivjJTextAreaDescription == null) {
			try {
				this.ivjJTextAreaDescription = new JTextField();
				this.ivjJTextAreaDescription.setName("JTextAreaDescription");
				this.ivjJTextAreaDescription.setBounds(167, 380, 382, 20);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJTextAreaDescription;
	}

	private JTextField getJTextCategory() {
		if (this.ivjJTextCategory == null) {
			try {
				this.ivjJTextCategory = new JTextField();
				this.ivjJTextCategory.setName("JTextCategory");
				this.ivjJTextCategory.setBounds(167, 212, 201, 20);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJTextCategory;
	}

	private JTextField getJTextName() {
		if (this.ivjJTextName == null) {
			try {
				this.ivjJTextName = new JTextField();
				this.ivjJTextName.setName("JTextName");
				this.ivjJTextName.setBounds(167, 168, 201, 20);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJTextName;
	}

	private JTextArea getJTextStack() {
		if (this.ivjJTextStack == null) {
			try {
				this.ivjJTextStack = new JTextArea();
				this.ivjJTextStack.setName("JTextStack");
				this.ivjJTextStack.setBounds(0, 0, 348, 97);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJTextStack;
	}

	private JToggleButton getJToggleButton1() {
		if (this.ivjJToggleButton1 == null) {
			try {
				this.ivjJToggleButton1 = new JToggleButton();
				this.ivjJToggleButton1.setName("JToggleButton1");
				this.ivjJToggleButton1.setToolTipText("Status Bar");
				this.ivjJToggleButton1.setText("");
				this.ivjJToggleButton1.setMaximumSize(new Dimension(27, 27));
				this.ivjJToggleButton1.setIcon(new ImageIcon(getClass().getResource("/statusbar.gif")));
				this.ivjJToggleButton1.setPreferredSize(new Dimension(27, 27));
				this.ivjJToggleButton1.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJToggleButton1;
	}

	private JToggleButton getJToggleButton11() {
		if (this.ivjJToggleButton11 == null) {
			try {
				this.ivjJToggleButton11 = new JToggleButton();
				this.ivjJToggleButton11.setName("JToggleButton11");
				this.ivjJToggleButton11.setToolTipText("Console");
				this.ivjJToggleButton11.setText("");
				this.ivjJToggleButton11.setMaximumSize(new Dimension(27, 27));
				this.ivjJToggleButton11.setIcon(new ImageIcon(getClass().getResource("/console_view.gif")));
				this.ivjJToggleButton11.setPreferredSize(new Dimension(27, 27));
				this.ivjJToggleButton11.setMinimumSize(new Dimension(27, 27));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJToggleButton11;
	}

	private JToolBar getJToolBar1() {
		if (this.ivjJToolBar1 == null) {
			try {
				this.ivjJToolBar1 = new JToolBar();
				this.ivjJToolBar1.setName("JToolBar1");
				getJToolBar1().add(getJButton131(), getJButton131().getName());
				getJToolBar1().add(getJButton11(), getJButton11().getName());
				getJToolBar1().add(getJButton12(), getJButton12().getName());
				getJToolBar1().add(getJButton13(), getJButton13().getName());
				this.ivjJToolBar1.addSeparator();
				getJToolBar1().add(getJButton13201(), getJButton13201().getName());
				getJToolBar1().add(getJButton132(), getJButton132().getName());
				getJToolBar1().add(getJButton1321(), getJButton1321().getName());
				getJToolBar1().add(getJButton1322(), getJButton1322().getName());
				getJToolBar1().add(getJButton1323(), getJButton1323().getName());
				this.ivjJToolBar1.addSeparator();
				getJToolBar1().add(getJButton1324(), getJButton1324().getName());
				getJToolBar1().add(getJButton1325(), getJButton1325().getName());
				this.ivjJToolBar1.addSeparator();
				getJToolBar1().add(getJToggleButton1(), getJToggleButton1().getName());
				getJToolBar1().add(getJToggleButton11(), getJToggleButton11().getName());
				this.ivjJToolBar1.addSeparator();
				getJToolBar1().add(getJButton13251(), getJButton13251().getName());
				getJToolBar1().add(getJButton13252(), getJButton13252().getName());

				this.ivjJToolBar1.addSeparator();
				getJToolBar1().add(getJCheckBoxFloatable(), getJCheckBoxFloatable().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJToolBar1;
	}

	private JPanel getLookAndFeelPanel() {
		if (this.ivjLookAndFeelPanel == null) {
			try {
				this.ivjLookAndFeelPanel = new JPanel();
				this.ivjLookAndFeelPanel.setName("LookAndFeelPanel");

				getLookAndFeelPanel().add(getMetalRadioButton(), getMetalRadioButton().getName());
				getLookAndFeelPanel().add(getMotifRadioButton(), getMotifRadioButton().getName());
				getLookAndFeelPanel().add(getSystemRadioButton(), getSystemRadioButton().getName());

				Border raisedetched = BorderFactory.createEtchedBorder(0);
				this.ivjLookAndFeelPanel.setBorder(BorderFactory.createTitledBorder(raisedetched, " Look and Feel "));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjLookAndFeelPanel;
	}

	private JRadioButton getMetalRadioButton() {
		if (this.ivjMetalRadioButton == null) {
			try {
				this.ivjMetalRadioButton = new JRadioButton();
				this.ivjMetalRadioButton.setName("MetalRadioButton");
				this.ivjMetalRadioButton.setMnemonic('M');
				this.ivjMetalRadioButton.setText("Metal");
				this.ivjMetalRadioButton.setBounds(39, 24, 83, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMetalRadioButton;
	}

	private JMenuItem getMonitor() {
		if (this.ivjMonitor == null) {
			try {
				this.ivjMonitor = new JMenuItem();
				this.ivjMonitor.setName("Monitor");
				this.ivjMonitor.setIcon(new ImageIcon(getClass().getResource("/mag.gif")));
				this.ivjMonitor.setMnemonic('M');
				this.ivjMonitor.setText("Monitor Detail");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitor;
	}

	private JMenuItem getMonitorCompareMenuItem() {
		if (this.ivjMonitorCompareMenuItem == null) {
			try {
				this.ivjMonitorCompareMenuItem = new JMenuItem();
				this.ivjMonitorCompareMenuItem.setName("MonitorCompareMenuItem");
				this.ivjMonitorCompareMenuItem.setIcon(new ImageIcon(getClass().getResource("/client_types_16.gif")));
				this.ivjMonitorCompareMenuItem.setMnemonic('o');
				this.ivjMonitorCompareMenuItem.setText("Compare Monitors");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitorCompareMenuItem;
	}

	private JMenuItem getMonitorCompareMenuItem1() {
		if (this.ivjMonitorCompareMenuItem1 == null) {
			try {
				this.ivjMonitorCompareMenuItem1 = new JMenuItem();
				this.ivjMonitorCompareMenuItem1.setName("MonitorCompareMenuItem1");
				this.ivjMonitorCompareMenuItem1.setIcon(new ImageIcon(getClass().getResource("/client_types_16.gif")));
				this.ivjMonitorCompareMenuItem1.setMnemonic('o');
				this.ivjMonitorCompareMenuItem1.setText("Compare Monitors");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitorCompareMenuItem1;
	}

	private JMenuItem getMonitorMenuItem() {
		if (this.ivjMonitorMenuItem == null) {
			try {
				this.ivjMonitorMenuItem = new JMenuItem();
				this.ivjMonitorMenuItem.setName("MonitorMenuItem");
				this.ivjMonitorMenuItem.setIcon(new ImageIcon(getClass().getResource("/mag.gif")));
				this.ivjMonitorMenuItem.setMnemonic('M');
				this.ivjMonitorMenuItem.setText("Monitor Detail");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitorMenuItem;
	}

	private JRadioButton getMotifRadioButton() {
		if (this.ivjMotifRadioButton == null) {
			try {
				this.ivjMotifRadioButton = new JRadioButton();
				this.ivjMotifRadioButton.setName("MotifRadioButton");
				this.ivjMotifRadioButton.setMnemonic('t');
				this.ivjMotifRadioButton.setText("Nimbus");
				this.ivjMotifRadioButton.setBounds(161, 24, 83, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMotifRadioButton;
	}

	private JButton getOkButton() {
		if (this.ivjOkButton == null) {
			try {
				this.ivjOkButton = new JButton();
				this.ivjOkButton.setName("OkButton");
				this.ivjOkButton.setMnemonic('O');
				this.ivjOkButton.setText("OK");
				this.ivjOkButton.setBounds(260, 486, 85, 25);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOkButton;
	}

	private JMenuItem getOpenMenuItem() {
		if (this.ivjOpenMenuItem == null) {
			try {
				this.ivjOpenMenuItem = new JMenuItem();
				this.ivjOpenMenuItem.setName("OpenMenuItem");
				this.ivjOpenMenuItem.setIcon(new ImageIcon(getClass().getResource("/open.gif")));
				this.ivjOpenMenuItem.setMnemonic('O');
				this.ivjOpenMenuItem.setText("Open Thread Dumps");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOpenMenuItem;
	}

	private JDialog getOptionDialog() {
		if (this.ivjOptionDialog == null) {
			try {
				this.ivjOptionDialog = new JDialog();
				this.ivjOptionDialog.setName("OptionDialog");
				this.ivjOptionDialog.setDefaultCloseOperation(2);
				this.ivjOptionDialog.setResizable(true);
				this.ivjOptionDialog.setBounds(983, 157, 592, 610);
				this.ivjOptionDialog.setModal(true);
				this.ivjOptionDialog.setTitle("Option");
				this.ivjOptionDialog.getContentPane().setLayout(new BorderLayout());
				this.ivjOptionDialog.getContentPane().add(getBasicTabbedPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOptionDialog;
	}

	private JMenuItem getOptionMenuItem() {
		if (this.ivjOptionMenuItem == null) {
			try {
				this.ivjOptionMenuItem = new JMenuItem();
				this.ivjOptionMenuItem.setName("OptionMenuItem");
				this.ivjOptionMenuItem.setIcon(new ImageIcon(getClass().getResource("/options.gif")));
				this.ivjOptionMenuItem.setMnemonic('O');
				this.ivjOptionMenuItem.setText("Option");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOptionMenuItem;
	}

	public Properties getProperties() {
		Properties p = new Properties();
		try {
			InputStream is = new FileInputStream("jca.properties.xml");

			p.loadFromXML(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return p;
	}

	private JCheckBox getSaveCheckBox() {
		if (this.ivjSaveCheckBox == null) {
			try {
				this.ivjSaveCheckBox = new JCheckBox();
				this.ivjSaveCheckBox.setName("SaveCheckBox");
				this.ivjSaveCheckBox.setSelected(true);
				this.ivjSaveCheckBox.setMnemonic('S');
				this.ivjSaveCheckBox.setText("Save option");
				this.ivjSaveCheckBox.setBounds(330, 255, 150, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSaveCheckBox;
	}

	private JCheckBoxMenuItem getStatusbarCheckBoxMenuItem() {
		if (this.ivjStatusbarCheckBoxMenuItem == null) {
			try {
				this.ivjStatusbarCheckBoxMenuItem = new JCheckBoxMenuItem();
				this.ivjStatusbarCheckBoxMenuItem.setName("StatusbarCheckBoxMenuItem");
				this.ivjStatusbarCheckBoxMenuItem.setSelected(true);
				this.ivjStatusbarCheckBoxMenuItem.setIcon(new ImageIcon(getClass().getResource("/statusbar.gif")));
				this.ivjStatusbarCheckBoxMenuItem.setMnemonic('S');
				this.ivjStatusbarCheckBoxMenuItem.setText("Statusbar");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStatusbarCheckBoxMenuItem;
	}

	private JPanel getStatusBarPane() {
		if (this.ivjStatusBarPane == null) {
			try {
				this.ivjStatusBarPane = new JPanel();
				this.ivjStatusBarPane.setName("StatusBarPane");
				this.ivjStatusBarPane.setLayout(new BorderLayout());
				getStatusBarPane().add(getStatusMsg1(), "West");
				getStatusBarPane().add(getStatusMsg2(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStatusBarPane;
	}

	private JLabel getStatusMsg1() {
		if (this.ivjStatusMsg1 == null) {
			try {
				this.ivjStatusMsg1 = new JLabel();
				this.ivjStatusMsg1.setName("StatusMsg1");
				this.ivjStatusMsg1.setBorder(new EtchedBorder());
				this.ivjStatusMsg1.setText("");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStatusMsg1;
	}

	private JLabel getStatusMsg2() {
		if (this.ivjStatusMsg2 == null) {
			try {
				this.ivjStatusMsg2 = new JLabel();
				this.ivjStatusMsg2.setName("StatusMsg2");
				this.ivjStatusMsg2.setBorder(new EtchedBorder());
				this.ivjStatusMsg2.setText("Status");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjStatusMsg2;
	}

	private JRadioButton getSystemRadioButton() {
		if (this.ivjSystemRadioButton == null) {
			try {
				this.ivjSystemRadioButton = new JRadioButton();
				this.ivjSystemRadioButton.setName("SystemRadioButton");
				this.ivjSystemRadioButton.setMnemonic('y');
				this.ivjSystemRadioButton.setText("System");
				this.ivjSystemRadioButton.setBounds(283, 25, 83, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSystemRadioButton;
	}

	private JSplitPane getThreadDumpSplitPane() {
		if (this.ivjThreadDumpSplitPane == null) {
			try {
				this.ivjThreadDumpSplitPane = new JSplitPane(0);
				this.ivjThreadDumpSplitPane.setName("ThreadDumpSplitPane");
				this.ivjThreadDumpSplitPane.setDividerLocation(100);
				this.ivjThreadDumpSplitPane.setOneTouchExpandable(true);
				getThreadDumpSplitPane().add(getThreadListScrollPane(), "top");
				getThreadDumpSplitPane().add(getDetailScrollPane(), "bottom");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadDumpSplitPane;
	}

	public JInternalFrame getThreadDumpTable() {
		if (this.ivjThreadDumpTable == null) {
			try {
				this.ivjThreadDumpTable = new JInternalFrame();
				this.ivjThreadDumpTable.setName("ThreadDumpTable");
				this.ivjThreadDumpTable.setTitle("Thread Dump List");
				this.ivjThreadDumpTable.setIconifiable(true);
				this.ivjThreadDumpTable.setVisible(true);
				this.ivjThreadDumpTable.setFrameIcon(new ImageIcon(getClass().getResource("/results_view.gif")));
				this.ivjThreadDumpTable.setBounds(7, 11, 717, 401);
				this.ivjThreadDumpTable.setMaximizable(true);
				this.ivjThreadDumpTable.setResizable(true);
				getThreadDumpTable().setContentPane(getJInternalFrameContentPane1());

				this.threadDumpTable = new JTable() {
					private static final long serialVersionUID = -3473552483748545890L;

					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						return c;
					}
				};
				MouseListener popupListener = new PopupListener(getThreadTableMenu(), this.threadDumpTable, this);
				this.threadDumpTable.addMouseListener(popupListener);
				this.threadDumpTable.setDragEnabled(true);
				this.threadDumpModel = new ThreadDumpTableModel(this.ti);
				this.threadDumpTable.setModel(this.threadDumpModel);
				getThreadListScrollPane().setViewportView(this.threadDumpTable);

				ListSelectionModel rowSM = this.threadDumpTable.getSelectionModel();
				rowSM.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;

						ListSelectionModel lsm = (ListSelectionModel) e.getSource();
						if (lsm.isSelectionEmpty()) {
							return;
						}

						Analyzer.this.displaySummary();
					}

				});
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadDumpTable;
	}

	private JScrollPane getThreadListScrollPane() {
		if (this.ivjThreadListScrollPane == null) {
			try {
				this.ivjThreadListScrollPane = new JScrollPane();
				this.ivjThreadListScrollPane.setName("ThreadListScrollPane");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadListScrollPane;
	}

	private JPopupMenu getThreadTableMenu() {
		if (this.ivjThreadTableMenu == null) {
			try {
				this.ivjThreadTableMenu = new JPopupMenu();
				this.ivjThreadTableMenu.setName("ThreadTableMenu");
				this.ivjThreadTableMenu.add(getDetailMenuItem());
				this.ivjThreadTableMenu.add(getMonitorMenuItem());
				this.ivjThreadTableMenu.add(getCompareMenuItem());
				this.ivjThreadTableMenu.add(getMonitorCompareMenuItem());
				this.ivjThreadTableMenu.add(getJSeparator3());
				this.ivjThreadTableMenu.add(getDeleteMenuItem());
				this.ivjThreadTableMenu.add(getDeleteAllMenuItem());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjThreadTableMenu;
	}

	private JCheckBox getVerboseCheckBox() {
		if (this.ivjVerboseCheckBox == null) {
			try {
				this.ivjVerboseCheckBox = new JCheckBox();
				this.ivjVerboseCheckBox.setName("VerboseCheckBox");
				this.ivjVerboseCheckBox.setSelected(true);
				this.ivjVerboseCheckBox.setMnemonic('V');
				this.ivjVerboseCheckBox.setText("Verbose Mode");
				this.ivjVerboseCheckBox.setBounds(107, 255, 116, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjVerboseCheckBox;
	}

	private JMenu getViewMenu() {
		if (this.ivjViewMenu == null) {
			try {
				this.ivjViewMenu = new JMenu();
				this.ivjViewMenu.setName("ViewMenu");
				this.ivjViewMenu.setMnemonic('V');
				this.ivjViewMenu.setText("View");
				this.ivjViewMenu.add(getOptionMenuItem());
				this.ivjViewMenu.add(getClearMenuItem());
				this.ivjViewMenu.add(getJSeparator2());
				this.ivjViewMenu.add(getStatusbarCheckBoxMenuItem());
				this.ivjViewMenu.add(getConsoleCheckBoxMenuItem());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjViewMenu;
	}

	public void graph_ViewMenuItem_ActionPerformed() {
		JInternalFrame[] jf = getJDesktopPane1().getAllFrames();
		for (int i = 0; i < jf.length; i++) {
			if ((jf[i].isSelected()) && ((jf[i] instanceof HeapFrame))) {
				GCInfo hi = ((HeapFrame) jf[i]).hi;
				if (hi == null) {
					JOptionPane.showMessageDialog(this, "Please select a gc window", "Information", 1);
					return;
				}

				HeapFrame hf = new HeapFrame(((HeapFrame) jf[i]).hi.file.getName() + " Chart View",
						((HeapFrame) jf[i]).hi);
				hf.fileName = ((HeapFrame) jf[i]).fileName;

				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());

				ChartPanel cp = new ChartPanel(hi, this);
				panel.add(cp, "Center");
				panel.add(new ControlPanel(cp), "East");
				hf.JScrollPaneSetViewportView(panel);
				getJDesktopPane1().add(hf);
				getJDesktopPane1().getDesktopManager().activateFrame(hf);
				try {
					hf.setSelected(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}

				return;
			}
		}

		JOptionPane.showMessageDialog(this, "Please select any gc window", "Information", 1);
	}

	public void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
	}

	public void help_TopicsMenuItem_ActionPerformed(ActionEvent actionEvent) {
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		URL helpURL = getClass().getResource("/readme.htm");

		if (helpURL != null) {
			try {
				textPane.setPage(helpURL);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Couldn't read file: readme.htm", "Information", 1);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Couldn't find file: readme.htm", "Information", 1);
			return;
		}

		HeapFrame hf = new HeapFrame("Help");
		hf.JScrollPaneSetViewportView(textPane);
		getJDesktopPane1().add(hf);
		getJDesktopPane1().getDesktopManager().activateFrame(hf);
		try {
			hf.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	private void initConnections() throws Exception {
		getStatusbarCheckBoxMenuItem().addActionListener(this.ivjEventHandler);
		getAbout_BoxMenuItem().addActionListener(this.ivjEventHandler);
		getOpenMenuItem().addActionListener(this.ivjEventHandler);
		getConsoleCheckBoxMenuItem().addActionListener(this.ivjEventHandler);
		getOptionMenuItem().addActionListener(this.ivjEventHandler);
		getOkButton().addActionListener(this.ivjEventHandler);
		getClearMenuItem().addActionListener(this.ivjEventHandler);
		getColorComboBox().addActionListener(this.ivjEventHandler);
		getBrowseButton().addActionListener(this.ivjEventHandler);
		addWindowListener(this.ivjEventHandler);
		getExitMenuItem().addActionListener(this.ivjEventHandler);
		getOpenMenuItem().addMouseListener(this.ivjEventHandler);
		getExitMenuItem().addMouseListener(this.ivjEventHandler);
		getOptionMenuItem().addMouseListener(this.ivjEventHandler);
		getClearMenuItem().addMouseListener(this.ivjEventHandler);
		getStatusbarCheckBoxMenuItem().addMouseListener(this.ivjEventHandler);
		getConsoleCheckBoxMenuItem().addMouseListener(this.ivjEventHandler);
		getHelp_TopicsMenuItem().addMouseListener(this.ivjEventHandler);
		getAbout_BoxMenuItem().addMouseListener(this.ivjEventHandler);
		getHelp_TopicsMenuItem().addActionListener(this.ivjEventHandler);
		getDetailMenuItem().addActionListener(this.ivjEventHandler);
		getCompareMenuItem().addActionListener(this.ivjEventHandler);
		getMonitorMenuItem().addActionListener(this.ivjEventHandler);
		getDeleteMenuItem().addActionListener(this.ivjEventHandler);
		getDeleteAllMenuItem().addActionListener(this.ivjEventHandler);
		getCloseAllMenuItem().addActionListener(this.ivjEventHandler);
		getCloseMenuItem().addActionListener(this.ivjEventHandler);
		getMonitorCompareMenuItem().addActionListener(this.ivjEventHandler);
		getDetail().addActionListener(this.ivjEventHandler);
		getMonitor().addActionListener(this.ivjEventHandler);
		getCompareMenuItem1().addActionListener(this.ivjEventHandler);
		getMonitorCompareMenuItem1().addActionListener(this.ivjEventHandler);
		getJButton131().addActionListener(this.ivjEventHandler);
		getJButton11().addActionListener(this.ivjEventHandler);
		getJButton12().addActionListener(this.ivjEventHandler);
		getJButton13().addActionListener(this.ivjEventHandler);
		getJButton132().addActionListener(this.ivjEventHandler);
		getJButton1321().addActionListener(this.ivjEventHandler);
		getJButton1322().addActionListener(this.ivjEventHandler);
		getJButton1323().addActionListener(this.ivjEventHandler);
		getJButton1324().addActionListener(this.ivjEventHandler);
		getJButton1325().addActionListener(this.ivjEventHandler);
		getJToggleButton1().addActionListener(this.ivjEventHandler);
		getJToggleButton11().addActionListener(this.ivjEventHandler);
		getJButton13251().addActionListener(this.ivjEventHandler);
		getJButton13252().addActionListener(this.ivjEventHandler);
		getJCheckBoxFloatable().addItemListener(this.ivjEventHandler);
		getJListKeyword().addListSelectionListener(this.ivjEventHandler);
		getJButtonNew().addActionListener(this.ivjEventHandler);
		getJButtonDelete().addActionListener(this.ivjEventHandler);
		getJButtonAdd().addActionListener(this.ivjEventHandler);
		getJButtonUpdate().addActionListener(this.ivjEventHandler);
	}

	private void initialize() {
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
			public String[] doInBackground() {
				try {
					URL traHomePage = new URL(
							"https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=2245aa39-fa5c-4475-b891-14c205f7333c");
					URLConnection yc = traHomePage.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

					String prefix = "VERSION";
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						if (inputLine.contains(prefix)) {
							int s = inputLine.indexOf(prefix);

							String version = inputLine.substring(s + prefix.length() + 1);
							inputLine = inputLine.substring(s);

							s = version.indexOf(' ');
							version = version.substring(0, s);

							if (version.compareTo(GCAnalyzerAboutBox.getVersionInfo()) > 0) {
								s = inputLine.indexOf("<");
								if (s >= 0)
									inputLine = inputLine.substring(0, s);
								String[] returnInfo = new String[2];
								returnInfo[0] = version;
								returnInfo[1] = inputLine;
								return returnInfo;
							}
							return null;
						}

					}

					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			public void done() {
				String[] str = (String[]) null;
				try {
					str = (String[]) get();
				} catch (InterruptedException localInterruptedException) {
				} catch (ExecutionException e) {
					String why = null;
					Throwable cause = e.getCause();
					if (cause != null)
						why = cause.getMessage();
					else {
						why = e.getMessage();
					}
					System.err.println("Error retrieving version information: " + why);
				}
				if (str != null) {
					int n = JOptionPane.showConfirmDialog(Analyzer.this,
							str[1] + "\nWould you like to download version " + str[0] + "?",
							"Version " + str[0] + " is available", 0);
					if ((n == 0) && (Desktop.isDesktopSupported()))
						try {
							Desktop.getDesktop().browse(new URI(
									"https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=2245aa39-fa5c-4475-b891-14c205f7333c"));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
				}
			}
		};
		worker.execute();
		try {
			if (System.getProperty("java.specification.version").compareTo("1.4") == 0) {
				System.out.println("Please use Java Runtime Environment 5 or higher");
				System.exit(0);
			}
			ImageIcon icon = new ImageIcon(getClass().getResource("/thread_and_monitor_view.gif"));

			setIconImage(icon.getImage());
			this.keywords = getProperties();

			setName("GCAnalyzer");
			setDefaultCloseOperation(0);
			setJMenuBar(getGCAnalyzerJMenuBar());
			setSize(729, 644);
			setTitle("IBM Thread and Monitor Dump Analyzer for Java ");
			setContentPane(getJFrameContentPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}

		this.lookAndFeelGroup.add(getMetalRadioButton());
		this.lookAndFeelGroup.add(getMotifRadioButton());
		this.lookAndFeelGroup.add(getSystemRadioButton());

		LookAndFeelListener lListener = new LookAndFeelListener();
		getMetalRadioButton().addItemListener(lListener);
		getMotifRadioButton().addItemListener(lListener);
		getSystemRadioButton().addItemListener(lListener);

		this.console = new ConsolePrintStream(getConsoleTextArea());
		System.setOut(this.console);
		System.setErr(this.console);

		File file = new File("tdv.cfg");
		ObjectInputStream ois;
		if (!file.exists()) {
			this.cfg = new Configuration();
		} else {
			try {
				GZIPInputStream gs = new GZIPInputStream(new FileInputStream(file));
				ois = new ObjectInputStream(gs);
				this.cfg = ((Configuration) ois.readObject());
				ois.close();
				gs.close();
			} catch (Exception e) {
				handleException(e);
				this.cfg = new Configuration();
				JOptionPane.showMessageDialog(this, "Cannot understand configuration file :" + file.getAbsoluteFile(),
						"File Format Error", 2);
			}

		}

		try {
			switch (this.cfg.lookAndFeel) {
			case 0:
				UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo;
				int j = (arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels()).length;
				for (int i = 0; i < j; i++) {
					UIManager.LookAndFeelInfo info = arrayOfLookAndFeelInfo[i];
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}

				break;
			case 1:
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

				break;
			case 2:
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println(
					"Saved Look and Feel is not supported on this platform. Trying Cross Platform Look and Feel.");
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				System.out.println("Cross Platform Look and Feel is applied.");
			} catch (Exception ex2) {
				ex2.printStackTrace();
				System.out.println("Cross Platform Look and Feel failed.");
			}
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(getJFrame());
		this.colorComboBoxRenderer.cfg = this.cfg;
		getConsoleTextArea().setText(this.cfg.consoleText);

		this.jp = new JDialogProgress(this);
		this.timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Analyzer.this.jp.getJProgressBar1().setValue(Analyzer.this.task.getCurrent());
				Analyzer.this.jp.getOverallProgressBar().setValue(Analyzer.this.task.getOverall());
				String s = Analyzer.this.task.getMessage();
				if (s != null) {
					Analyzer.this.jp.getJLabel1().setText(s);
				}
				if (Analyzer.this.task.isDone()) {
					Toolkit.getDefaultToolkit().beep();
					Analyzer.this.timer.stop();
					Analyzer.this.jp.getJProgressBar1().setValue(Analyzer.this.jp.getJProgressBar1().getMaximum());
					Analyzer.this.jp.getOverallProgressBar()
							.setValue(Analyzer.this.jp.getOverallProgressBar().getMaximum());
				}
			}
		});
	}

	public static boolean isHeadLess(String[] args) {
		if (args == null)
			return false;
		if (args.length <= 1)
			return false;
		if (args[0] == null)
			return false;
		if (args.length >= 2)
			return true;
		return false;
	}

	public void jButtonAdd_ActionPerformed(ActionEvent actionEvent) {
		String newID = getJTextName().getText();
		String all = this.keywords.getProperty("jca.keyword.list");
		if (all != null) {
			StringTokenizer st = new StringTokenizer(all, " ");
			while (st.hasMoreTokens()) {
				if (newID.compareTo(st.nextToken()) == 0) {
					JOptionPane.showMessageDialog(this, "Please use new ID", "ID already exists", 2);
					return;
				}
			}
			this.keywords.setProperty("jca.keyword.list", all + " " + newID);
		} else {
			this.keywords.setProperty("jca.keyword.list", newID);
		}

		this.keywords.setProperty(newID + ".desc", getJTextAreaDescription().getText());
		this.keywords.setProperty(newID + ".stack", getJTextStack().getText());

		this.keywords.setProperty(newID + ".name", getJTextCategory().getText());
		saveProperties(this.keywords);
		this.keywords = getProperties();
		all = this.keywords.getProperty("jca.keyword.list");
		this.keywordListModel.clear();
		if (all != null) {
			StringTokenizer st = new StringTokenizer(all, " ");
			while (st.hasMoreTokens()) {
				this.keywordListModel.addElement(st.nextToken());
			}
		}
		getJListKeyword().updateUI();
	}

	public void jButtonDelete_ActionPerformed(ActionEvent actionEvent) {
		int selected = getJListKeyword().getSelectedIndex();
		if (selected == -1) {
			JOptionPane.showMessageDialog(this, "Please select an ID", "ID not selected", 2);
			return;
		}
		String key = (String) this.keywordListModel.getElementAt(selected);
		if (key == null) {
			System.out.println("key is null");
			return;
		}

		this.keywords.remove(key + ".desc");
		this.keywords.remove(key + ".stack");
		this.keywords.remove(key + ".name");

		String all = this.keywords.getProperty("jca.keyword.list");
		String newAll = "";
		String s = null;
		if (all != null) {
			StringTokenizer st = new StringTokenizer(all, " ");

			while (st.hasMoreTokens()) {
				s = st.nextToken();
				if (key.compareTo(s) != 0)
					newAll = newAll + s + " ";
			}
		}
		this.keywords.setProperty("jca.keyword.list", newAll);
		getJTextAreaDescription().setText("");
		getJTextStack().setText("");
		getJTextName().setText("");
		getJTextCategory().setText("");

		saveProperties(this.keywords);
		this.keywords = getProperties();
		all = this.keywords.getProperty("jca.keyword.list");
		this.keywordListModel.clear();
		if (all != null) {
			StringTokenizer st = new StringTokenizer(all, " ");
			while (st.hasMoreTokens()) {
				this.keywordListModel.addElement(st.nextToken());
			}
		}
		getJListKeyword().updateUI();
	}

	public void jButtonUpdate_ActionPerformed(ActionEvent actionEvent) {
		boolean found = false;
		String newID = getJTextName().getText();
		String all = this.keywords.getProperty("jca.keyword.list");
		if (all != null) {
			StringTokenizer st = new StringTokenizer(all, " ");

			while (st.hasMoreTokens()) {
				if (newID.compareTo(st.nextToken()) == 0) {
					found = true;

					break;
				}
			}
		}
		if (!found) {
			JOptionPane.showMessageDialog(this, "Please use existing ID to update", "ID Not Found", 2);
			return;
		}

		this.keywords.setProperty(newID + ".desc", getJTextAreaDescription().getText());
		this.keywords.setProperty(newID + ".stack", getJTextStack().getText());
		this.keywords.setProperty(newID + ".name", getJTextCategory().getText());
		saveProperties(this.keywords);
		this.keywords = getProperties();
	}

	public void jListKeyword_ValueChanged(ListSelectionEvent listSelectionEvent) {
		int selected = getJListKeyword().getSelectedIndex();
		if (selected == -1)
			return;
		String key = (String) this.keywordListModel.getElementAt(selected);

		String name = this.keywords.getProperty(key + ".desc");
		if (name != null)
			getJTextAreaDescription().setText(name);
		else
			getJTextAreaDescription().setText("");
		name = this.keywords.getProperty(key + ".stack");

		if (name != null)
			getJTextStack().setText(name);
		else
			getJTextStack().setText("");
		name = this.keywords.getProperty(key + ".name");
		if (name != null)
			getJTextCategory().setText(name);
		else
			getJTextCategory().setText("");
		getJTextName().setText(key);
	}

	public static void main(String[] args) {
		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			Graphics2D g = splash.createGraphics();
			if (g != null) {
				Font font = new Font(g.getFont().getName(), 1, 12);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setFont(font);
				g.setComposite(AlphaComposite.Clear);
				g.setPaintMode();
				g.setColor(new Color(42, 42, 42));
				g.drawString(GCAnalyzerAboutBox.getVersionInfo(), 62, 287);
				splash.update();
			}
		}

		if (isHeadLess(args)) {
			System.out.println("Generating analysis ...");
			runHeadLess(args);
			System.out.println("Completed analysis.");
			return;
		}

		try {
			Analyzer aGCAnalyzer = new Analyzer();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = aGCAnalyzer.getSize();
			if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
			if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width;
			aGCAnalyzer.setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);

			aGCAnalyzer.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					System.exit(0);
				}
			});
			aGCAnalyzer.setVisible(true);
			if ((args != null) && (args.length != 0))
				aGCAnalyzer.openBatch(args);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of IBM Thread and Monitor Dump Analyzer");
			exception.printStackTrace(System.out);
		}
	}

	public void monitorCompareMenuItem_ActionPerformed(ActionEvent actionEvent) {
		int[] selectedRows = this.threadDumpTable.getSelectedRows();
		if (selectedRows.length == 0L) {
			JOptionPane.showMessageDialog(this, "Please select  thread dumps", "Information", 1);
			return;
		}

		int mdumpCount = 0;

		for (int i = 0; i < selectedRows.length; i++) {
			ThreadDump t1 = (ThreadDump) this.ti.threadDumps.get(selectedRows[i]);
			if (t1.mdump != null)
				mdumpCount++;
		}

		if (mdumpCount == 0) {
			JOptionPane.showMessageDialog(this, "No monitor information available", "No monitor information", 2);
			return;
		}

		ThreadDump[] td = new ThreadDump[mdumpCount];

		String selectedThreads = "";

		int count = 0;
		for (int i = 0; i < selectedRows.length; i++) {
			ThreadDump t1 = (ThreadDump) this.ti.threadDumps.get(selectedRows[i]);
			if (t1.mdump != null) {
				selectedThreads = selectedThreads + this.threadDumpTable.getValueAt(selectedRows[i], 0) + " ";
				td[(count++)] = t1;
			} else {
				JOptionPane.showMessageDialog(this,
						"No monitor information available in " + this.threadDumpTable.getValueAt(selectedRows[i], 0),
						"No monitor information", 2);
			}

		}

		MonitorCompareFrame tf = new MonitorCompareFrame("Compare Monitors : " + selectedThreads, td, this.cfg);
		getJDesktopPane1().add(tf);
		getJDesktopPane1().getDesktopManager().activateFrame(tf);
	}

	public void monitorMenuItem_ActionPerformed(ActionEvent actionEvent) {
		int[] selectedRows = this.threadDumpTable.getSelectedRows();

		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this, "Please select a thread dump", "Information", 1);
			return;
		}

		for (int i = 0; i < selectedRows.length; i++)
			if (((ThreadDump) this.ti.threadDumps.get(selectedRows[i])).mdump == null) {
				JOptionPane.showMessageDialog(this,
						"No monitor information available in " + this.threadDumpTable.getValueAt(selectedRows[i], 0),
						"No monitor information", 2);
			} else {
				MonitorFrame tf = new MonitorFrame(
						"Monitor Detail : " + this.threadDumpTable.getValueAt(selectedRows[i], 0),
						(ThreadDump) this.ti.threadDumps.get(selectedRows[i]));
				getJDesktopPane1().add(tf);
				getJDesktopPane1().getDesktopManager().activateFrame(tf);
			}
	}

	public void openBatch(String[] args) {
		if ((args != null) && (args[0] != null)) {
			String separator = System.getProperty("path.separator");
			StringTokenizer st = new StringTokenizer(args[0], separator);
			int numberOfFiles = st.countTokens();
			File[] f = new File[numberOfFiles];
			for (int i = 0; (i < numberOfFiles) && (st.hasMoreTokens()); i++) {
				f[i] = new File(st.nextToken());
			}

			if (f.length == 0)
				return;
			Dimension dialogSize = this.jp.getPreferredSize();
			Dimension frameSize = getSize();
			Point loc = getLocation();
			this.jp.setLocation((frameSize.width - dialogSize.width) / 2 + loc.x,
					(frameSize.height - dialogSize.height) / 2 + loc.y);
			this.jp.show();
			this.task = new FileTask(this, f, this.jp, this.cfg);
			this.task.go();
			this.timer.start();
		}
	}

	public void openMenuItem_ActionPerformed() {
		JFileChooser jf;
		if (this.cfg.workingDir == null) {
			String curDir = System.getProperty("user.dir");
			jf = new JFileChooser(curDir);
		} else {
			jf = new JFileChooser(this.cfg.workingDir);
		}
		jf.setMultiSelectionEnabled(true);

		JDialog jd = new JDialog(this);
		jf.showDialog(jd, "Open");
		File[] f = jf.getSelectedFiles();
		if (f.length == 0)
			return;
		this.cfg.workingDir = jf.getCurrentDirectory();

		Dimension dialogSize = this.jp.getPreferredSize();
		Dimension frameSize = getSize();
		Point loc = getLocation();
		this.jp.setLocation((frameSize.width - dialogSize.width) / 2 + loc.x,
				(frameSize.height - dialogSize.height) / 2 + loc.y);
		this.jp.show();
		this.task = new FileTask(this, f, this.jp, this.cfg);
		this.task.go();
		this.timer.start();
	}

	public static void runHeadLess(String[] args) {
		int start = 0;

		String separator = System.getProperty("path.separator");

		StringTokenizer st = new StringTokenizer(args[start], separator);
		int numberOfFiles = st.countTokens();
		String[] inputFiles = new String[numberOfFiles];
		for (int i = 0; (i < numberOfFiles) && (st.hasMoreTokens()); i++) {
			inputFiles[i] = st.nextToken();
		}

		Properties p = new Properties();

		AnalyzerHeadless hl = new AnalyzerHeadless(args[(start + 1)]);
		hl.readConfiguration("tdv.cfg");
		hl.threadAnalysis(inputFiles, p, args[(start + 1)]);
	}

	void saveConfiguration() {
		if (this.cfg.verbose)
			System.out.println(new Date() + " Saving configuration file.");
		this.cfg.consoleText = getConsoleTextArea().getText();
		try {
			BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream("tdv.cfg"));
			GZIPOutputStream gz = new GZIPOutputStream(bo);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(this.cfg);
			oos.flush();
			oos.close();
			bo.close();
		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
		}
	}

	public void saveProperties(Properties p) {
		try {
			OutputStream os = new FileOutputStream("jca.properties.xml");

			p.storeToXML(os, "IBM Thread and Monitor Dump Analyzer for Java Technology by Jinwoo Hwang");

			os.close();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void showAboutBox() {
		GCAnalyzerAboutBox aGCAnalyzerAboutBox = new GCAnalyzerAboutBox("4.0.1", this);
		Dimension dialogSize = aGCAnalyzerAboutBox.getPreferredSize();
		Dimension frameSize = getSize();
		Point loc = getLocation();
		aGCAnalyzerAboutBox.setLocation((frameSize.width - dialogSize.width) / 2 + loc.x,
				(frameSize.height - dialogSize.height) / 2 + loc.y);
		aGCAnalyzerAboutBox.setModal(true);
		aGCAnalyzerAboutBox.show();
	}

	public void usageMenuItem_ActionPerformed(ActionEvent actionEvent) {
		JInternalFrame[] jf = getJDesktopPane1().getAllFrames();
		for (int i = 0; i < jf.length; i++) {
			if ((jf[i].isSelected()) && ((jf[i] instanceof HeapFrame))) {
				GCInfo hi = ((HeapFrame) jf[i]).hi;
				if (hi == null) {
					JOptionPane.showMessageDialog(this, "Please select a gc window", "Information", 1);
					return;
				}

				HeapFrame hf = new HeapFrame(((HeapFrame) jf[i]).hi.file.getName() + " GC Duration Summary",
						((HeapFrame) jf[i]).hi);
				hf.fileName = ((HeapFrame) jf[i]).fileName;

				long[] min = new long[4];
				long[] max = new long[4];
				long[] timestamp = new long[4];
				long[] sum = new long[4];
				long tmp163_162 = hi.mark[0];
				max[0] = tmp163_162;
				min[0] = tmp163_162;
				long tmp179_178 = hi.sweep[0];
				max[1] = tmp179_178;
				min[1] = tmp179_178;
				long tmp195_194 = hi.compact[0];
				max[2] = tmp195_194;
				min[2] = tmp195_194;
				long tmp211_210 = hi.gccompleted[0];
				max[3] = tmp211_210;
				min[3] = tmp211_210;

				for (i = 0; i < hi.free.length; i++) {
					if (hi.mark[i] > max[0]) {
						max[0] = hi.mark[i];
						timestamp[0] = hi.timestamp[i];
					}
					if (hi.mark[i] < min[0])
						min[0] = hi.mark[i];
					sum[0] += hi.mark[i];

					if (hi.sweep[i] > max[1]) {
						max[1] = hi.sweep[i];
						timestamp[1] = hi.timestamp[i];
					}
					if (hi.sweep[i] < min[1])
						min[1] = hi.sweep[i];
					sum[1] += hi.sweep[i];

					if (hi.compact[i] > max[2]) {
						max[2] = hi.compact[i];
						timestamp[2] = hi.timestamp[i];
					}
					if (hi.compact[i] < min[2])
						min[2] = hi.compact[i];
					sum[2] += hi.compact[i];

					if (hi.gccompleted[i] > max[3]) {
						max[3] = hi.gccompleted[i];
						timestamp[3] = hi.timestamp[i];
					}
					if (hi.gccompleted[i] < min[3])
						min[3] = hi.gccompleted[i];
					sum[3] += hi.gccompleted[i];
				}

				Object[] header = { "", "Mark avg", "min", "max", "timestamp of max", "Sweep avg", "min", "max",
						"timestamp of max", "Compact avg", "min", "max", "timestamp of max", "Overall avg", "min",
						"max", "timestamp of max" };
				Object[][] data = new Object[hi.numberOfSet + 1][17];
				data[0][0] = "Overall";
				for (i = 0; i < min.length; i++) {
					data[0][(i * 4 + 1)] = numberFormatter.format(sum[i] / hi.free.length);
					data[0][(i * 4 + 2)] = numberFormatter.format(min[i]);
					data[0][(i * 4 + 3)] = numberFormatter.format(max[i]);
					data[0][(i * 4 + 4)] = dateFormatter.format(new Date(timestamp[i]));
				}

				long[] minMark = new long[hi.numberOfSet];
				long[] maxMark = new long[hi.numberOfSet];
				long[] sumMark = new long[hi.numberOfSet];
				long[] timeMark = new long[hi.numberOfSet];
				int[] countMark = new int[hi.numberOfSet];
				long[] minSweep = new long[hi.numberOfSet];
				long[] maxSweep = new long[hi.numberOfSet];
				long[] sumSweep = new long[hi.numberOfSet];
				long[] timeSweep = new long[hi.numberOfSet];
				int[] countSweep = new int[hi.numberOfSet];
				long[] minCompact = new long[hi.numberOfSet];
				long[] maxCompact = new long[hi.numberOfSet];
				long[] sumCompact = new long[hi.numberOfSet];
				long[] timeCompact = new long[hi.numberOfSet];
				int[] countCompact = new int[hi.numberOfSet];

				long[] minComplete = new long[hi.numberOfSet];
				long[] maxComplete = new long[hi.numberOfSet];
				long[] sumComplete = new long[hi.numberOfSet];
				long[] timeComplete = new long[hi.numberOfSet];
				int[] countComplete = new int[hi.numberOfSet];

				int nGC = 999999999;
				int index = -1;
				for (i = 0; i < hi.free.length; i++) {
					if (hi.ngc[i] < nGC) {
						index++;
						long tmp1017_1016 = hi.mark[i];
						maxMark[index] = tmp1017_1016;
						minMark[index] = tmp1017_1016;
						long tmp1035_1034 = hi.sweep[i];
						maxSweep[index] = tmp1035_1034;
						minSweep[index] = tmp1035_1034;
						long tmp1053_1052 = hi.compact[i];
						maxCompact[index] = tmp1053_1052;
						minCompact[index] = tmp1053_1052;
						long tmp1071_1070 = hi.gccompleted[i];
						maxComplete[index] = tmp1071_1070;
						minComplete[index] = tmp1071_1070;
					}
					nGC = hi.ngc[i];

					if (hi.mark[i] > maxMark[index]) {
						maxMark[index] = hi.mark[i];
						timeMark[index] = hi.timestamp[i];
					}
					if (hi.mark[i] < minMark[index])
						minMark[index] = hi.mark[i];
					sumMark[index] += hi.mark[i];
					countMark[index] += 1;

					if (hi.sweep[i] > maxSweep[index]) {
						maxSweep[index] = hi.sweep[i];
						timeSweep[index] = hi.timestamp[i];
					}
					if (hi.sweep[i] < minSweep[index])
						minSweep[index] = hi.sweep[i];
					sumSweep[index] += hi.sweep[i];
					countSweep[index] += 1;

					if (hi.compact[i] > maxCompact[index]) {
						maxCompact[index] = hi.compact[i];
						timeCompact[index] = hi.timestamp[i];
					}
					if (hi.compact[i] < minCompact[index])
						minCompact[index] = hi.compact[i];
					sumCompact[index] += hi.compact[i];
					countCompact[index] += 1;

					if (hi.gccompleted[i] > maxComplete[index]) {
						maxComplete[index] = hi.gccompleted[i];
						timeComplete[index] = hi.timestamp[i];
					}
					if (hi.gccompleted[i] < minComplete[index])
						minComplete[index] = hi.gccompleted[i];
					sumComplete[index] += hi.gccompleted[i];
					countComplete[index] += 1;
				}

				for (i = 1; i < hi.numberOfSet + 1; i++) {
					data[i][0] = ("# " + i);

					data[i][1] = numberFormatter.format(sumMark[(i - 1)] / countMark[(i - 1)]);
					data[i][2] = numberFormatter.format(minMark[(i - 1)]);
					data[i][3] = numberFormatter.format(maxMark[(i - 1)]);
					data[i][4] = dateFormatter.format(new Date(timeMark[(i - 1)]));

					data[i][5] = numberFormatter.format(sumSweep[(i - 1)] / countSweep[(i - 1)]);
					data[i][6] = numberFormatter.format(minSweep[(i - 1)]);
					data[i][7] = numberFormatter.format(maxSweep[(i - 1)]);
					data[i][8] = dateFormatter.format(new Date(timeSweep[(i - 1)]));

					data[i][9] = numberFormatter.format(sumCompact[(i - 1)] / countCompact[(i - 1)]);
					data[i][10] = numberFormatter.format(minCompact[(i - 1)]);
					data[i][11] = numberFormatter.format(maxCompact[(i - 1)]);
					data[i][12] = dateFormatter.format(new Date(timeCompact[(i - 1)]));

					data[i][13] = numberFormatter.format(sumComplete[(i - 1)] / countComplete[(i - 1)]);
					data[i][14] = numberFormatter.format(minComplete[(i - 1)]);
					data[i][15] = numberFormatter.format(maxComplete[(i - 1)]);
					data[i][16] = dateFormatter.format(new Date(timeComplete[(i - 1)]));
				}
				JTable jt = new JTable(data, header) {
					private static final long serialVersionUID = 7922570789209227378L;

					public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
						Component c = super.prepareRenderer(renderer, row, column);

						return c;
					}
				};
				jt.setDragEnabled(true);
				hf.JScrollPaneSetViewportView(jt);
				getJDesktopPane1().add(hf);
				getJDesktopPane1().getDesktopManager().activateFrame(hf);
				try {
					hf.setSelected(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}

				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Please select any gc window", "Information", 1);
	}

	public void viewConsole() {
		try {
			getConsoleFrame().setIcon(!getConsoleFrame().isIcon());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public void viewStatusBar() {
		getStatusBarPane().setVisible(!getStatusBarPane().isVisible());
	}

	class IvjEventHandler
			implements ActionListener, ItemListener, MouseListener, WindowListener, ListSelectionListener {
		IvjEventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == Analyzer.this.getStatusbarCheckBoxMenuItem())
				Analyzer.this.connEtoC2(e);
			if (e.getSource() == Analyzer.this.getAbout_BoxMenuItem())
				Analyzer.this.connEtoC3(e);
			if (e.getSource() == Analyzer.this.getOpenMenuItem())
				Analyzer.this.connEtoC4(e);
			if (e.getSource() == Analyzer.this.getConsoleCheckBoxMenuItem())
				Analyzer.this.connEtoC5(e);
			if (e.getSource() == Analyzer.this.getOptionMenuItem())
				Analyzer.this.connEtoM2(e);
			if (e.getSource() == Analyzer.this.getOkButton())
				Analyzer.this.connEtoM3(e);
			if (e.getSource() == Analyzer.this.getClearMenuItem())
				Analyzer.this.connEtoM5(e);
			if (e.getSource() == Analyzer.this.getColorComboBox())
				Analyzer.this.connEtoC11(e);
			if (e.getSource() == Analyzer.this.getBrowseButton())
				Analyzer.this.connEtoC13(e);
			if (e.getSource() == Analyzer.this.getExitMenuItem())
				Analyzer.this.connEtoC15(e);
			if (e.getSource() == Analyzer.this.getHelp_TopicsMenuItem())
				Analyzer.this.connEtoC1(e);
			if (e.getSource() == Analyzer.this.getDetailMenuItem())
				Analyzer.this.connEtoC16(e);
			if (e.getSource() == Analyzer.this.getCompareMenuItem())
				Analyzer.this.connEtoC19(e);
			if (e.getSource() == Analyzer.this.getMonitorMenuItem())
				Analyzer.this.connEtoC17(e);
			if (e.getSource() == Analyzer.this.getDeleteMenuItem())
				Analyzer.this.connEtoC18(e);
			if (e.getSource() == Analyzer.this.getDeleteAllMenuItem())
				Analyzer.this.connEtoC20(e);
			if (e.getSource() == Analyzer.this.getCloseAllMenuItem())
				Analyzer.this.connEtoC21(e);
			if (e.getSource() == Analyzer.this.getCloseMenuItem())
				Analyzer.this.connEtoC22(e);
			if (e.getSource() == Analyzer.this.getMonitorCompareMenuItem())
				Analyzer.this.connEtoC23(e);
			if (e.getSource() == Analyzer.this.getDetail())
				Analyzer.this.connEtoC6(e);
			if (e.getSource() == Analyzer.this.getMonitor())
				Analyzer.this.connEtoC7(e);
			if (e.getSource() == Analyzer.this.getCompareMenuItem1())
				Analyzer.this.connEtoC8(e);
			if (e.getSource() == Analyzer.this.getMonitorCompareMenuItem1())
				Analyzer.this.connEtoC9(e);
			if (e.getSource() == Analyzer.this.getJButton131())
				Analyzer.this.connEtoC10(e);
			if (e.getSource() == Analyzer.this.getJButton11())
				Analyzer.this.connEtoC12(e);
			if (e.getSource() == Analyzer.this.getJButton12())
				Analyzer.this.connEtoC24(e);
			if (e.getSource() == Analyzer.this.getJButton13())
				Analyzer.this.connEtoC25(e);
			if (e.getSource() == Analyzer.this.getJButton132())
				Analyzer.this.connEtoC26(e);
			if (e.getSource() == Analyzer.this.getJButton1321())
				Analyzer.this.connEtoC27(e);
			if (e.getSource() == Analyzer.this.getJButton1322())
				Analyzer.this.connEtoC28(e);
			if (e.getSource() == Analyzer.this.getJButton1323())
				Analyzer.this.connEtoC29(e);
			if (e.getSource() == Analyzer.this.getJButton1324())
				Analyzer.this.connEtoM12(e);
			if (e.getSource() == Analyzer.this.getJButton1325())
				Analyzer.this.connEtoM13(e);
			if (e.getSource() == Analyzer.this.getJToggleButton1())
				Analyzer.this.connEtoC30(e);
			if (e.getSource() == Analyzer.this.getJToggleButton11())
				Analyzer.this.connEtoC31(e);
			if (e.getSource() == Analyzer.this.getJButton13251())
				Analyzer.this.connEtoC32(e);
			if (e.getSource() == Analyzer.this.getJButton13252())
				Analyzer.this.connEtoC33(e);
			if (e.getSource() == Analyzer.this.getJButtonNew())
				Analyzer.this.connEtoM15(e);
			if (e.getSource() == Analyzer.this.getJButtonDelete())
				Analyzer.this.connEtoC35(e);
			if (e.getSource() == Analyzer.this.getJButtonAdd())
				Analyzer.this.connEtoC36(e);
			if (e.getSource() == Analyzer.this.getJButtonUpdate())
				Analyzer.this.connEtoC37(e);
		}

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == Analyzer.this.getJCheckBoxFloatable())
				Analyzer.this.connEtoM14(e);
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == Analyzer.this.getOpenMenuItem())
				Analyzer.this.connEtoM1(e);
			if (e.getSource() == Analyzer.this.getOpenMenuItem())
				Analyzer.this.connEtoM6(e);
			if (e.getSource() == Analyzer.this.getExitMenuItem())
				Analyzer.this.connEtoM8(e);
			if (e.getSource() == Analyzer.this.getExitMenuItem())
				Analyzer.this.connEtoM10(e);
			if (e.getSource() == Analyzer.this.getOptionMenuItem())
				Analyzer.this.connEtoM22(e);
			if (e.getSource() == Analyzer.this.getClearMenuItem())
				Analyzer.this.connEtoM24(e);
			if (e.getSource() == Analyzer.this.getStatusbarCheckBoxMenuItem())
				Analyzer.this.connEtoM26(e);
			if (e.getSource() == Analyzer.this.getConsoleCheckBoxMenuItem())
				Analyzer.this.connEtoM28(e);
			if (e.getSource() == Analyzer.this.getHelp_TopicsMenuItem())
				Analyzer.this.connEtoM30(e);
			if (e.getSource() == Analyzer.this.getAbout_BoxMenuItem())
				Analyzer.this.connEtoM32(e);
		}

		public void mouseExited(MouseEvent e) {
			if (e.getSource() == Analyzer.this.getOpenMenuItem())
				Analyzer.this.connEtoM4(e);
			if (e.getSource() == Analyzer.this.getOpenMenuItem())
				Analyzer.this.connEtoM7(e);
			if (e.getSource() == Analyzer.this.getExitMenuItem())
				Analyzer.this.connEtoM9(e);
			if (e.getSource() == Analyzer.this.getExitMenuItem())
				Analyzer.this.connEtoM11(e);
			if (e.getSource() == Analyzer.this.getOptionMenuItem())
				Analyzer.this.connEtoM23(e);
			if (e.getSource() == Analyzer.this.getClearMenuItem())
				Analyzer.this.connEtoM25(e);
			if (e.getSource() == Analyzer.this.getStatusbarCheckBoxMenuItem())
				Analyzer.this.connEtoM27(e);
			if (e.getSource() == Analyzer.this.getConsoleCheckBoxMenuItem())
				Analyzer.this.connEtoM29(e);
			if (e.getSource() == Analyzer.this.getHelp_TopicsMenuItem())
				Analyzer.this.connEtoM31(e);
			if (e.getSource() == Analyzer.this.getAbout_BoxMenuItem())
				Analyzer.this.connEtoM33(e);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == Analyzer.this.getJListKeyword())
				Analyzer.this.connEtoC34(e);
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			if (e.getSource() == Analyzer.this)
				Analyzer.this.connEtoC14(e);
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}
	}

	class LookAndFeelListener implements ItemListener {
		LookAndFeelListener() {
		}

		public void itemStateChanged(ItemEvent e) {
			JRadioButton rb = (JRadioButton) e.getSource();
			try {
				if ((rb.isSelected()) && (rb.getText().equals("System"))) {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getJFrame());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getOptionDialog());
				} else if ((rb.isSelected()) && (rb.getText().equals("Nimbus"))) {
					for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getJFrame());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getOptionDialog());
				} else if ((rb.isSelected()) && (rb.getText().equals("Metal"))) {
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getJFrame());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getOptionDialog());
				}
			} catch (UnsupportedLookAndFeelException ex) {
				rb.setEnabled(false);
				System.out.println(rb.getText()
						+ " Look and Feel is not supported on this platform. Trying Cross Platform Look and Feel.");
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getJFrame());
					SwingUtilities.updateComponentTreeUI(Analyzer.this.getOptionDialog());
					System.out.println("Cross Platform Look and Feel is applied.");
				} catch (Exception ex2) {
					ex2.printStackTrace();
					System.out.println("Cross Platform Look and Feel failed.");
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}
}
