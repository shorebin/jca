package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MonitorCompareTableCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = -4495596158180689991L;
	Configuration cfg;

	public MonitorCompareTableCellRenderer() {
		initialize();
	}

	public MonitorCompareTableCellRenderer(Configuration c) {
		this.cfg = c;
		initialize();
	}

	public MonitorCompareTableCellRenderer(String text) {
		super(text);
		initialize();
	}

	public MonitorCompareTableCellRenderer(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initialize();
	}

	public MonitorCompareTableCellRenderer(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initialize();
	}

	public MonitorCompareTableCellRenderer(Icon image) {
		super(image);
		initialize();
	}

	public MonitorCompareTableCellRenderer(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		initialize();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		MonitorCompareCell cc = (MonitorCompareCell) value;
		Color borderColor = table.getBackground();
		boolean left = false;
		boolean right = false;
		setBackground(table.getBackground());
		setText("");
		if (cc != null) {
			int state = cc.threadDump[cc.index].state[cc.threadIndex];
			if (state == 0) {
				borderColor = this.cfg.runnable;
				setIcon(new ImageIcon(getClass().getResource("/run.gif")));
			} else if (state == 1) {
				borderColor = this.cfg.condition;
				setIcon(new ImageIcon(getClass().getResource("/condition.gif")));
			} else if (state == 2) {
				borderColor = this.cfg.monitor;
				setIcon(new ImageIcon(getClass().getResource("/monitor_wait.gif")));
			} else if (state == 3) {
				borderColor = this.cfg.suspended;
				setIcon(new ImageIcon(getClass().getResource("/suspend.gif")));
			} else if (state == 4) {
				borderColor = this.cfg.object;
				setIcon(new ImageIcon(getClass().getResource("/waiting.gif")));
			} else if (state == 5) {
				borderColor = this.cfg.blocked;
				setIcon(new ImageIcon(getClass().getResource("/block.gif")));
			} else if (state == 6) {
				borderColor = this.cfg.park;
				setIcon(new ImageIcon(getClass().getResource("/park.gif")));
			}
			if (cc.threadDump[cc.index].isDeadlock[cc.threadIndex]) {
				borderColor = this.cfg.deadlock;
				setIcon(new ImageIcon(getClass().getResource("/deadlock_view.gif")));
			}

		}

		if ((cc != null) && (cc.threadDump[cc.index].javaStack[cc.threadIndex] != null)) {
			if (cc.index != 0) {
				if (cc.threadDump[(cc.index - 1)].threadHash2.containsKey(cc.tid)) {
					int idx = -1;
					for (int i = 0; i < cc.threadDump[(cc.index - 1)].sys_thread.length; i++) {
						if (cc.threadDump[(cc.index - 1)].sys_thread[i] == cc.tid.longValue()) {
							idx = i;
							break;
						}
					}
					if (cc.threadDump[(cc.index - 1)].javaStack[idx] != null) {
						if (cc.threadDump[(cc.index - 1)].javaStack[idx]
								.compareTo(cc.threadDump[cc.index].javaStack[cc.threadIndex]) == 0) {
							setBackground(this.cfg.hang);

							left = true;
						}
					}
				}
			}
			if (cc.index < cc.threadDump.length - 1) {
				if (cc.threadDump[(cc.index + 1)].threadHash2.containsKey(cc.tid)) {
					int idx = -1;
					for (int i = 0; i < cc.threadDump[(cc.index + 1)].sys_thread.length; i++) {
						if (cc.threadDump[(cc.index + 1)].sys_thread[i] == cc.tid.longValue()) {
							idx = i;
							break;
						}
					}
					if (cc.threadDump[(cc.index + 1)].javaStack[idx] != null) {
						if (cc.threadDump[(cc.index + 1)].javaStack[idx]
								.compareTo(cc.threadDump[cc.index].javaStack[cc.threadIndex]) == 0) {
							setBackground(this.cfg.hang);

							right = true;
						}
					}
				}
			}

			setText(cc.threadDump[cc.index].getCurrentMethod(cc.threadIndex));
		}

		if (isSelected) {
			setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, table.getSelectionBackground()));
		} else if ((right) && (left))
			setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, borderColor));
		else if (right)
			setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, borderColor));
		else if (left)
			setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, borderColor));
		else {
			setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, borderColor));
		}

		return this;
	}

	private void handleException(Throwable exception) {
	}

	private void initialize() {
		try {
			setName("MonitorCompareTableCellRenderer");
			setOpaque(true);
			setText("MonitorCompareTableCellRenderer");
			setSize(200, 14);
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
