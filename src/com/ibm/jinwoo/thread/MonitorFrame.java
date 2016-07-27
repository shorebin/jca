package com.ibm.jinwoo.thread;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.html.HTMLDocument;

public class MonitorFrame extends JInternalFrame implements TreeSelectionListener {
	private static final long serialVersionUID = -3945060283803747281L;
	private JPanel ivjJInternalFrameContentPane = null;
	private JScrollPane ivjDetailScrollPane = null;
	private JTextPane ivjDetailTextPane = null;
	private JSplitPane ivjMonitorSplitPane = null;
	private JTree ivjMonitorTree = null;
	private JScrollPane ivjTreeScrollPane = null;
	ThreadDump tdump;

	public MonitorFrame() {
		initialize();
	}

	public MonitorFrame(String title) {
		super(title);
		initialize();
	}

	public MonitorFrame(String title, ThreadDump td) {
		super(title);
		this.tdump = td;
		initialize();
	}

	public MonitorFrame(String title, boolean resizable) {
		super(title, resizable);
		initialize();
	}

	public MonitorFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
		initialize();
	}

	public MonitorFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		initialize();
	}

	public MonitorFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		initialize();
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

	private JTextPane getDetailTextPane() {
		if (this.ivjDetailTextPane == null) {
			try {
				this.ivjDetailTextPane = new JTextPane();
				this.ivjDetailTextPane.setName("DetailTextPane");
				this.ivjDetailTextPane.setDocument(new HTMLDocument());
				this.ivjDetailTextPane.setBounds(0, 0, 11, 6);
				this.ivjDetailTextPane.setEditable(false);
				this.ivjDetailTextPane.setContentType("text/html");

				this.ivjDetailTextPane.setDragEnabled(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDetailTextPane;
	}

	private JPanel getJInternalFrameContentPane() {
		if (this.ivjJInternalFrameContentPane == null) {
			try {
				this.ivjJInternalFrameContentPane = new JPanel();
				this.ivjJInternalFrameContentPane.setName("JInternalFrameContentPane");
				this.ivjJInternalFrameContentPane.setLayout(new BorderLayout());
				getJInternalFrameContentPane().add(getMonitorSplitPane(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJInternalFrameContentPane;
	}

	private JSplitPane getMonitorSplitPane() {
		if (this.ivjMonitorSplitPane == null) {
			try {
				this.ivjMonitorSplitPane = new JSplitPane(1);
				this.ivjMonitorSplitPane.setName("MonitorSplitPane");
				this.ivjMonitorSplitPane.setDividerLocation(300);
				this.ivjMonitorSplitPane.setOneTouchExpandable(true);
				getMonitorSplitPane().add(getTreeScrollPane(), "left");
				getMonitorSplitPane().add(getDetailScrollPane(), "right");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitorSplitPane;
	}

	private JTree getMonitorTree() {
		if (this.ivjMonitorTree == null) {
			try {
				this.ivjMonitorTree = new JTree();
				this.ivjMonitorTree.setName("MonitorTree");
				this.ivjMonitorTree.setBounds(0, 0, 78, 72);

				MonitorModel m = new MonitorModel(this.tdump);
				this.ivjMonitorTree.setModel(m);
				this.ivjMonitorTree.setCellRenderer(new JinwooRenderer(m));
				this.ivjMonitorTree.addTreeSelectionListener(this);
				this.ivjMonitorTree.setDragEnabled(true);
				this.ivjMonitorTree.setTransferHandler(new TreeSelection());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMonitorTree;
	}

	private JScrollPane getTreeScrollPane() {
		if (this.ivjTreeScrollPane == null) {
			try {
				this.ivjTreeScrollPane = new JScrollPane();
				this.ivjTreeScrollPane.setName("TreeScrollPane");
				getTreeScrollPane().setViewportView(getMonitorTree());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTreeScrollPane;
	}

	private void handleException(Throwable exception) {
		System.out.println("Exception in MonitorFrame");
		exception.printStackTrace(System.out);
	}

	private void initialize() {
		try {
			setName("MonitorFrame");
			setIconifiable(true);
			setClosable(true);
			setVisible(true);
			setFrameIcon(new ImageIcon(getClass().getResource("/mag.gif")));
			setSize(452, 427);
			setMaximizable(true);
			setResizable(true);
			setContentPane(getJInternalFrameContentPane());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			MonitorFrame aMonitorFrame = new MonitorFrame();
			frame.setContentPane(aMonitorFrame);
			frame.setSize(aMonitorFrame.getSize());
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

	public void valueChanged(TreeSelectionEvent e) {
		Monitor node = (Monitor) getMonitorTree().getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.owner == -1) {
			getDetailTextPane().setText("");
			return;
		}
		MonitorModel m = (MonitorModel) getMonitorTree().getModel();

		if (m.id[node.owner] == -1) {
			getDetailTextPane().setText("");
			return;
		}

		String stackTrace = "<table border=\"1\"><tr><th><B>Thread Name</B></th><th>"
				+ this.tdump.name[m.id[node.owner]] + "<tr><td><B>State</B></td><td>"
				+ this.tdump.getState(m.id[node.owner]);
		String monString = "";
		if (node.isHeapLock)
			monString = monString + "Owns Heap Lock";
		if (node.waitingHeapLock) {
			if (monString.length() != 0)
				monString = monString + "<BR>";
			monString = monString + "Waiting for Heap Lock";
		}
		if (node.objectName != null) {
			if (monString.length() != 0)
				monString = monString + "<BR>";
			monString = monString + "Waiting for Monitor Lock on " + node.objectName;
		}
		String oo = m.getOwningObjects(node.owner);
		if (oo != null) {
			if (monString.length() != 0)
				monString = monString + "<BR>";
			monString = monString + "Owns Monitor Lock on " + oo;
		}

		if (monString.length() == 0)
			stackTrace = stackTrace + "</td></tr>";
		else
			stackTrace = stackTrace + "<tr><td><B>Monitor</B></td><td>" + monString + "</td></tr>";
		if (this.tdump.javaStack[m.id[node.owner]] == null) {
			stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td>No Java stack trace available</td></tr>";
		} else {
			stackTrace = stackTrace + "<tr><td><B>Java Stack</B></td><td>" + this.tdump.javaStack[m.id[node.owner]]
					+ "</td></tr>";
		}
		if (this.tdump.nativeStack[m.id[node.owner]] != null)
			stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td><td>" + this.tdump.nativeStack[m.id[node.owner]]
					+ "</td></tr>";
		else
			stackTrace = stackTrace + "<tr><td><B>Native Stack</B></td>No Native stack trace available</td></tr>";
		getDetailTextPane().setText(stackTrace);
		getDetailTextPane().setCaretPosition(0);
	}
}
