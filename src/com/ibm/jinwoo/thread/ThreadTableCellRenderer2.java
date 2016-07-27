package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ThreadTableCellRenderer2 extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 3128924934578709024L;
	Configuration cfg;

	public ThreadTableCellRenderer2() {
		initialize();
	}

	public ThreadTableCellRenderer2(Configuration c) {
		this.cfg = c;
		initialize();
	}

	public ThreadTableCellRenderer2(String text) {
		super(text);
		initialize();
	}

	public ThreadTableCellRenderer2(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer2(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initialize();
	}

	public ThreadTableCellRenderer2(Icon image) {
		super(image);
		initialize();
	}

	public ThreadTableCellRenderer2(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		initialize();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (!(value instanceof Long)) {
			return null;
		}
		Long cc = (Long) value;

		setHorizontalAlignment(4);

		setText("0x" + Long.toHexString(cc.longValue()));
		return this;
	}

	private void handleException(Throwable exception) {
	}

	private void initialize() {
		try {
			setName("ThreadTableCellRenderer2");
			setText("ThreadTableCellRenderer2");
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
