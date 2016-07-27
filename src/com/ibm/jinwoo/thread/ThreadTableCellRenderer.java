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

public class ThreadTableCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 7141647098368271975L;
	Configuration cfg;

	public ThreadTableCellRenderer() {
		initialize();
	}

	public ThreadTableCellRenderer(Configuration c) {
		this.cfg = c;
		initialize();
	}

	public ThreadTableCellRenderer(String text) {
		super(text);
		initialize();
	}

	public ThreadTableCellRenderer(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer(Icon image) {
		super(image);
		initialize();
	}

	public ThreadTableCellRenderer(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		initialize();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		String cc = (String) value;

		if (cc != null) {
			setText(cc);
			if (cc.startsWith("Deadlock")) {
				setBackground(this.cfg.deadlock);
				setIcon(new ImageIcon(getClass().getResource("/deadlock_view.gif")));
			} else if (cc.compareTo("Runnable") == 0) {
				setBackground(this.cfg.runnable);
				setIcon(new ImageIcon(getClass().getResource("/run.gif")));
			} else if (cc.compareTo("Waiting on condition") == 0) {
				setBackground(this.cfg.condition);
				setIcon(new ImageIcon(getClass().getResource("/condition.gif")));
			} else if (cc.compareTo("Waiting on monitor") == 0) {
				setBackground(this.cfg.monitor);
				setIcon(new ImageIcon(getClass().getResource("/monitor_wait.gif")));
			} else if (cc.compareTo("Suspended") == 0) {
				setBackground(this.cfg.suspended);
				setIcon(new ImageIcon(getClass().getResource("/suspend.gif")));
			} else if (cc.compareTo("Blocked") == 0) {
				setBackground(this.cfg.blocked);
				setIcon(new ImageIcon(getClass().getResource("/block.gif")));
			} else if (cc.compareTo("Parked") == 0) {
				setBackground(this.cfg.park);
				setIcon(new ImageIcon(getClass().getResource("/park.gif")));
			} else {
				setBackground(this.cfg.object);
				setIcon(new ImageIcon(getClass().getResource("/waiting.gif")));
			}
			return this;
		}
		return null;
	}

	private void handleException(Throwable exception) {
	}

	private void initialize() {
		try {
			setName("ThreadTableCellRenderer");
			setOpaque(true);
			setText("");
			setSize(145, 14);
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
