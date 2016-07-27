package com.ibm.jinwoo.thread;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HeapFrame extends JInternalFrame {
	private static final long serialVersionUID = 4322164862947551074L;
	private static int TOTAL = 0;
	private JPanel ivjJInternalFrameContentPane = null;
	public JScrollPane ivjJScrollPane1 = null;

	public String fileName = null;
	public GCInfo hi;
	public JMenuBar menuBar = null;
	public JMenu menu = null;
	public JMenu removeMenu = null;
	public JMenu leakMenu = null;

	private long id = 0L;
	public Analyzer ha = null;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public HeapFrame() {
		initialize();
	}

	public HeapFrame(String title) {
		super(title, true, true, true, true);
		TOTAL += 1;
		initialize();
	}

	public HeapFrame(String title, GCInfo h) {
		super(title, true, true, true, true);
		TOTAL += 1;
		initialize();
		this.hi = h;
	}

	public HeapFrame(String title, GCInfo h, Analyzer ha) {
		super(title, true, true, true, true);
		TOTAL += 1;
		initialize();
		this.hi = h;
		this.ha = ha;
	}

	public HeapFrame(String title, boolean resizable) {
		super(title, resizable);
	}

	public HeapFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
	}

	public HeapFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
	}

	public HeapFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
	}

	private JPanel getJInternalFrameContentPane() {
		if (this.ivjJInternalFrameContentPane == null) {
			try {
				this.ivjJInternalFrameContentPane = new JPanel();
				this.ivjJInternalFrameContentPane.setName("JInternalFrameContentPane");
				this.ivjJInternalFrameContentPane.setLayout(new BorderLayout());
				getJInternalFrameContentPane().add(getJScrollPane1(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJInternalFrameContentPane;
	}

	private JScrollPane getJScrollPane1() {
		if (this.ivjJScrollPane1 == null) {
			try {
				this.ivjJScrollPane1 = new JScrollPane();
				this.ivjJScrollPane1.setName("JScrollPane1");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJScrollPane1;
	}

	private void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
		JOptionPane.showMessageDialog(this, exception.toString(), "Exception", 2);
	}

	private void initialize() {
		try {
			setName("HeapFrame");
			setDefaultCloseOperation(2);
			setClosable(true);
			setIconifiable(true);
			setVisible(true);
			setSelected(true);
			setSize(404, 381);
			setMaximizable(true);
			setResizable(true);
			setContentPane(getJInternalFrameContentPane());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}

		setLocation(TOTAL % 10 * 30 + TOTAL / 10 * 30, TOTAL % 10 * 30 + 30 * (TOTAL / 10));
	}

	public void JScrollPaneSetViewportView(Component view) {
		getJScrollPane1().setViewportView(view);
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			HeapFrame aHeapFrame = new HeapFrame();
			frame.setContentPane(aHeapFrame);
			frame.setSize(aHeapFrame.getSize());
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

	public synchronized long nextID() {
		this.id += 1L;
		return this.id;
	}
}
