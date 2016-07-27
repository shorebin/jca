package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ThreadTableCellRenderer3 extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 301019733145597028L;
	Configuration cfg;
	ThreadDump threadDump;

	public ThreadTableCellRenderer3() {
		initialize();
	}

	public ThreadTableCellRenderer3(Configuration c) {
		this.cfg = c;
		initialize();
	}

	public ThreadTableCellRenderer3(Configuration c, ThreadDump d) {
		this.threadDump = d;
		this.cfg = c;
		initialize();
	}

	public ThreadTableCellRenderer3(String text) {
		super(text);
		initialize();
	}

	public ThreadTableCellRenderer3(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer3(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer3(Icon image) {
		super(image);
		initialize();
	}

	public ThreadTableCellRenderer3(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		initialize();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		String cc = (String) value;
		if (cc != null) {
			setText(cc);
			if (this.threadDump != null) {
				for (int i = 0; i < this.threadDump.name.length; i++) {
					if (this.threadDump.name[i].compareTo(cc) == 0) {
						if (this.threadDump.getWaitingMonitor(i) != null)
							setIcon(new ImageIcon(getClass().getResource("/monitor_wait.gif")));
						else if (this.threadDump.getOwningMonitors(i) != null)
							setIcon(new ImageIcon(getClass().getResource("/thread_and_monitor_view.gif")));
						else
							setIcon(null);
					}
				}
			}

			return this;
		}
		return null;
	}

	private void handleException(Throwable exception) {
	}

	private void initialize() {
		try {
			setName("ThreadTableCellRenderer3");
			setText("ThreadTableCellRenderer3");
			setSize(152, 14);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			CompareTableCellRenderer aCompareTableCellRenderer = new CompareTableCellRenderer();
			frame.setContentPane(aCompareTableCellRenderer);
			frame.setSize(aCompareTableCellRenderer.getSize());
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
			System.err.println("Exception occurred in main() of com.ibm.jinwoo.thread.CompareTableCellRenderer");
			exception.printStackTrace(System.out);
		}
	}
}
