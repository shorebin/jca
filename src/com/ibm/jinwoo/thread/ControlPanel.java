package com.ibm.jinwoo.thread;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 8017676624671943520L;
	Cursor zoomInCursor;
	Cursor zoomOutCursor;
	private JToggleButton ivjCompactButton = null;
	private JToggleButton ivjCompletedButton = null;
	private JToggleButton ivjFreeButton = null;
	private JToggleButton ivjFreedButton = null;
	private JToggleButton ivjMarkButton = null;
	private JToggleButton ivjRequestedButton = null;
	private JToggleButton ivjSinceButton = null;
	private JToggleButton ivjSweepButton = null;
	private JToggleButton ivjTotalButton = null;
	ChartPanel cp = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JToggleButton ivjGCButton = null;
	private JToggleButton ivjZoomInButton = null;
	private JToggleButton ivjZoomOutButton = null;
	private JToggleButton ivjCenterButton = null;
	private JToggleButton ivjSelectButton = null;
	private JToggleButton ivjUsedButton = null;
	private JToggleButton ivjOverheadButton = null;

	public ControlPanel() {
		initialize();
	}

	public ControlPanel(ChartPanel cp) {
		this.cp = cp;
		initialize();
	}

	public ControlPanel(LayoutManager layout) {
		super(layout);
	}

	public ControlPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ControlPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public void centerButton_ActionPerformed(ActionEvent actionEvent) {
		if (getCenterButton().isSelected()) {
			this.cp.setCursor(new Cursor(1));
			this.cp.zoomCenter = true;
		} else {
			this.cp.setCursor(new Cursor(0));
			this.cp.zoomCenter = false;
		}

		if (getZoomInButton().isSelected()) {
			getZoomInButton().setSelected(false);
			this.cp.zoomIn = false;
		}

		if (getZoomOutButton().isSelected()) {
			getZoomOutButton().setSelected(false);
			this.cp.zoomOut = false;
		}

		if (getSelectButton().isSelected()) {
			getSelectButton().setSelected(false);
			this.cp.select = false;
		}
	}

	public void compactButton_ActionPerformed(ActionEvent actionEvent) {
		if (getCompactButton().isSelected())
			this.cp.compact = true;
		else
			this.cp.compact = false;
		this.cp.repaint();
	}

	public void completedButton_ActionPerformed(ActionEvent actionEvent) {
		if (getCompletedButton().isSelected())
			this.cp.completed = true;
		else
			this.cp.completed = false;
		this.cp.repaint();
	}

	private void connEtoC1(ActionEvent arg1) {
		try {
			totalButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC10(ActionEvent arg1) {
		try {
			gCButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC11(ActionEvent arg1) {
		try {
			zoomInButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC12(ActionEvent arg1) {
		try {
			zoomOutButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC13(ActionEvent arg1) {
		try {
			centerButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC14(ActionEvent arg1) {
		try {
			selectButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC15(ActionEvent arg1) {
		try {
			usedButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC16(ActionEvent arg1) {
		try {
			overheadButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC2(ActionEvent arg1) {
		try {
			freeButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC3(ActionEvent arg1) {
		try {
			requestedButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC4(ActionEvent arg1) {
		try {
			freedButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC5(ActionEvent arg1) {
		try {
			sinceButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC6(ActionEvent arg1) {
		try {
			completedButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC7(ActionEvent arg1) {
		try {
			markButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC8(ActionEvent arg1) {
		try {
			sweepButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void connEtoC9(ActionEvent arg1) {
		try {
			compactButton_ActionPerformed(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public void freeButton_ActionPerformed(ActionEvent actionEvent) {
		if (getFreeButton().isSelected())
			this.cp.free = true;
		else
			this.cp.free = false;
		this.cp.repaint();
	}

	public void freedButton_ActionPerformed(ActionEvent actionEvent) {
		if (getFreedButton().isSelected())
			this.cp.freed = true;
		else
			this.cp.freed = false;
		this.cp.repaint();
	}

	public void gCButton_ActionPerformed(ActionEvent actionEvent) {
		if (getGCButton().isSelected())
			this.cp.gccompleted = true;
		else
			this.cp.gccompleted = false;
		this.cp.repaint();
	}

	private JToggleButton getCenterButton() {
		if (this.ivjCenterButton == null) {
			try {
				this.ivjCenterButton = new JToggleButton();
				this.ivjCenterButton.setName("CenterButton");
				this.ivjCenterButton.setText("Center");
				this.ivjCenterButton.setMaximumSize(new Dimension(60, 21));
				this.ivjCenterButton.setPreferredSize(new Dimension(60, 21));
				this.ivjCenterButton.setFont(new Font("Arial", 1, 10));
				this.ivjCenterButton.setMinimumSize(new Dimension(60, 21));
				this.ivjCenterButton.setMargin(new Insets(2, 0, 2, 0));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCenterButton;
	}

	private JToggleButton getCompactButton() {
		if (this.ivjCompactButton == null) {
			try {
				this.ivjCompactButton = new JToggleButton();
				this.ivjCompactButton.setName("CompactButton");
				this.ivjCompactButton.setText("Compact");
				this.ivjCompactButton.setMaximumSize(new Dimension(60, 21));
				this.ivjCompactButton.setPreferredSize(new Dimension(60, 21));
				this.ivjCompactButton.setFont(new Font("Arial", 1, 10));
				this.ivjCompactButton.setMinimumSize(new Dimension(60, 21));
				this.ivjCompactButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjCompactButton.setForeground(this.cp.gca.cfg.compact);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompactButton;
	}

	private JToggleButton getCompletedButton() {
		if (this.ivjCompletedButton == null) {
			try {
				this.ivjCompletedButton = new JToggleButton();
				this.ivjCompletedButton.setName("CompletedButton");
				this.ivjCompletedButton.setText("Completed");
				this.ivjCompletedButton.setMaximumSize(new Dimension(60, 21));
				this.ivjCompletedButton.setPreferredSize(new Dimension(60, 21));
				this.ivjCompletedButton.setFont(new Font("Arial", 1, 10));
				this.ivjCompletedButton.setMinimumSize(new Dimension(60, 21));
				this.ivjCompletedButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjCompletedButton.setForeground(this.cp.gca.cfg.completed);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjCompletedButton;
	}

	private BoxLayout getControlPanelBoxLayout() {
		BoxLayout ivjControlPanelBoxLayout = null;
		try {
			ivjControlPanelBoxLayout = new BoxLayout(this, 1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		return ivjControlPanelBoxLayout;
	}

	private JToggleButton getFreeButton() {
		if (this.ivjFreeButton == null) {
			try {
				this.ivjFreeButton = new JToggleButton();
				this.ivjFreeButton.setName("FreeButton");
				this.ivjFreeButton.setHorizontalTextPosition(2);
				this.ivjFreeButton.setSelected(true);
				this.ivjFreeButton.setVerticalAlignment(0);
				this.ivjFreeButton.setMinimumSize(new Dimension(60, 21));
				this.ivjFreeButton.setMargin(new Insets(2, 0, 2, 0));
				this.ivjFreeButton.setText("Free");
				this.ivjFreeButton.setMaximumSize(new Dimension(60, 21));
				this.ivjFreeButton.setVerticalTextPosition(0);
				this.ivjFreeButton.setPreferredSize(new Dimension(60, 21));
				this.ivjFreeButton.setFont(new Font("Arial", 1, 10));
				this.ivjFreeButton.setHorizontalAlignment(0);

				this.ivjFreeButton.setForeground(this.cp.gca.cfg.free);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjFreeButton;
	}

	private JToggleButton getFreedButton() {
		if (this.ivjFreedButton == null) {
			try {
				this.ivjFreedButton = new JToggleButton();
				this.ivjFreedButton.setName("FreedButton");
				this.ivjFreedButton.setText("Freed");
				this.ivjFreedButton.setMaximumSize(new Dimension(60, 21));
				this.ivjFreedButton.setHorizontalTextPosition(2);
				this.ivjFreedButton.setPreferredSize(new Dimension(60, 21));
				this.ivjFreedButton.setFont(new Font("Arial", 1, 10));
				this.ivjFreedButton.setMinimumSize(new Dimension(60, 21));
				this.ivjFreedButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjFreedButton.setForeground(this.cp.gca.cfg.freed);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjFreedButton;
	}

	private JToggleButton getGCButton() {
		if (this.ivjGCButton == null) {
			try {
				this.ivjGCButton = new JToggleButton();
				this.ivjGCButton.setName("GCButton");
				this.ivjGCButton.setText("GC");
				this.ivjGCButton.setMaximumSize(new Dimension(60, 21));
				this.ivjGCButton.setPreferredSize(new Dimension(60, 21));
				this.ivjGCButton.setFont(new Font("Arial", 1, 10));
				this.ivjGCButton.setMinimumSize(new Dimension(60, 21));
				this.ivjGCButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjGCButton.setForeground(this.cp.gca.cfg.gccompleted);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjGCButton;
	}

	private JToggleButton getMarkButton() {
		if (this.ivjMarkButton == null) {
			try {
				this.ivjMarkButton = new JToggleButton();
				this.ivjMarkButton.setName("MarkButton");
				this.ivjMarkButton.setText("Mark");
				this.ivjMarkButton.setMaximumSize(new Dimension(60, 21));
				this.ivjMarkButton.setPreferredSize(new Dimension(60, 21));
				this.ivjMarkButton.setFont(new Font("Arial", 1, 10));
				this.ivjMarkButton.setMinimumSize(new Dimension(60, 21));

				this.ivjMarkButton.setForeground(this.cp.gca.cfg.mark);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjMarkButton;
	}

	private JToggleButton getOverheadButton() {
		if (this.ivjOverheadButton == null) {
			try {
				this.ivjOverheadButton = new JToggleButton();
				this.ivjOverheadButton.setName("OverheadButton");
				this.ivjOverheadButton.setToolTipText("AF overhead(%)");
				this.ivjOverheadButton.setHorizontalTextPosition(2);
				this.ivjOverheadButton.setSelected(false);
				this.ivjOverheadButton.setVerticalAlignment(0);
				this.ivjOverheadButton.setMinimumSize(new Dimension(60, 21));
				this.ivjOverheadButton.setMargin(new Insets(2, 0, 2, 0));
				this.ivjOverheadButton.setText("Overhead");
				this.ivjOverheadButton.setMaximumSize(new Dimension(60, 21));
				this.ivjOverheadButton.setVerticalTextPosition(0);
				this.ivjOverheadButton.setPreferredSize(new Dimension(60, 21));
				this.ivjOverheadButton.setFont(new Font("Arial", 1, 10));
				this.ivjOverheadButton.setHorizontalAlignment(0);

				this.ivjOverheadButton.setForeground(this.cp.gca.cfg.overhead);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOverheadButton;
	}

	private JToggleButton getRequestedButton() {
		if (this.ivjRequestedButton == null) {
			try {
				this.ivjRequestedButton = new JToggleButton();
				this.ivjRequestedButton.setName("RequestedButton");
				this.ivjRequestedButton.setText("Requested");
				this.ivjRequestedButton.setMaximumSize(new Dimension(60, 21));
				this.ivjRequestedButton.setHorizontalTextPosition(0);
				this.ivjRequestedButton.setPreferredSize(new Dimension(60, 21));
				this.ivjRequestedButton.setFont(new Font("Arial", 1, 10));
				this.ivjRequestedButton.setMinimumSize(new Dimension(60, 21));
				this.ivjRequestedButton.setHorizontalAlignment(0);
				this.ivjRequestedButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjRequestedButton.setForeground(this.cp.gca.cfg.requested);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRequestedButton;
	}

	private JToggleButton getSelectButton() {
		if (this.ivjSelectButton == null) {
			try {
				this.ivjSelectButton = new JToggleButton();
				this.ivjSelectButton.setName("SelectButton");
				this.ivjSelectButton.setText("Select");
				this.ivjSelectButton.setMaximumSize(new Dimension(60, 21));
				this.ivjSelectButton.setPreferredSize(new Dimension(60, 21));
				this.ivjSelectButton.setFont(new Font("Arial", 1, 10));
				this.ivjSelectButton.setMinimumSize(new Dimension(60, 21));
				this.ivjSelectButton.setMargin(new Insets(2, 0, 2, 0));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSelectButton;
	}

	private JToggleButton getSinceButton() {
		if (this.ivjSinceButton == null) {
			try {
				this.ivjSinceButton = new JToggleButton();
				this.ivjSinceButton.setName("SinceButton");
				this.ivjSinceButton.setText("Since");
				this.ivjSinceButton.setMaximumSize(new Dimension(60, 21));
				this.ivjSinceButton.setPreferredSize(new Dimension(60, 21));
				this.ivjSinceButton.setFont(new Font("Arial", 1, 10));
				this.ivjSinceButton.setMinimumSize(new Dimension(60, 21));
				this.ivjSinceButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjSinceButton.setForeground(this.cp.gca.cfg.since);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSinceButton;
	}

	private JToggleButton getSweepButton() {
		if (this.ivjSweepButton == null) {
			try {
				this.ivjSweepButton = new JToggleButton();
				this.ivjSweepButton.setName("SweepButton");
				this.ivjSweepButton.setText("Sweep");
				this.ivjSweepButton.setMaximumSize(new Dimension(60, 21));
				this.ivjSweepButton.setPreferredSize(new Dimension(60, 21));
				this.ivjSweepButton.setFont(new Font("Arial", 1, 10));
				this.ivjSweepButton.setMinimumSize(new Dimension(60, 21));
				this.ivjSweepButton.setMargin(new Insets(2, 0, 2, 0));

				this.ivjSweepButton.setForeground(this.cp.gca.cfg.sweep);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjSweepButton;
	}

	private JToggleButton getTotalButton() {
		if (this.ivjTotalButton == null) {
			try {
				this.ivjTotalButton = new JToggleButton();
				this.ivjTotalButton.setName("TotalButton");
				this.ivjTotalButton.setText("Total");
				this.ivjTotalButton.setMaximumSize(new Dimension(60, 21));
				this.ivjTotalButton.setPreferredSize(new Dimension(60, 21));
				this.ivjTotalButton.setFont(new Font("Arial", 1, 10));
				this.ivjTotalButton.setContentAreaFilled(true);
				this.ivjTotalButton.setMinimumSize(new Dimension(60, 21));

				this.ivjTotalButton.setForeground(this.cp.gca.cfg.total);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTotalButton;
	}

	private JToggleButton getUsedButton() {
		if (this.ivjUsedButton == null) {
			try {
				this.ivjUsedButton = new JToggleButton();
				this.ivjUsedButton.setName("UsedButton");
				this.ivjUsedButton.setHorizontalTextPosition(2);
				this.ivjUsedButton.setSelected(false);
				this.ivjUsedButton.setVerticalAlignment(0);
				this.ivjUsedButton.setMinimumSize(new Dimension(60, 21));
				this.ivjUsedButton.setMargin(new Insets(2, 0, 2, 0));
				this.ivjUsedButton.setText("Used");
				this.ivjUsedButton.setMaximumSize(new Dimension(60, 21));
				this.ivjUsedButton.setVerticalTextPosition(0);
				this.ivjUsedButton.setPreferredSize(new Dimension(60, 21));
				this.ivjUsedButton.setFont(new Font("Arial", 1, 10));
				this.ivjUsedButton.setHorizontalAlignment(0);

				this.ivjUsedButton.setForeground(this.cp.gca.cfg.used);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjUsedButton;
	}

	private JToggleButton getZoomInButton() {
		if (this.ivjZoomInButton == null) {
			try {
				this.ivjZoomInButton = new JToggleButton();
				this.ivjZoomInButton.setName("ZoomInButton");
				this.ivjZoomInButton.setText("Zoom In");
				this.ivjZoomInButton.setMaximumSize(new Dimension(60, 21));
				this.ivjZoomInButton.setPreferredSize(new Dimension(60, 21));
				this.ivjZoomInButton.setFont(new Font("Arial", 1, 10));
				this.ivjZoomInButton.setMinimumSize(new Dimension(60, 21));
				this.ivjZoomInButton.setMargin(new Insets(2, 0, 2, 0));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjZoomInButton;
	}

	private JToggleButton getZoomOutButton() {
		if (this.ivjZoomOutButton == null) {
			try {
				this.ivjZoomOutButton = new JToggleButton();
				this.ivjZoomOutButton.setName("ZoomOutButton");
				this.ivjZoomOutButton.setText("Zoom Out");
				this.ivjZoomOutButton.setMaximumSize(new Dimension(60, 21));
				this.ivjZoomOutButton.setPreferredSize(new Dimension(60, 21));
				this.ivjZoomOutButton.setFont(new Font("Arial", 1, 10));
				this.ivjZoomOutButton.setMinimumSize(new Dimension(60, 21));
				this.ivjZoomOutButton.setMargin(new Insets(2, 0, 2, 0));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjZoomOutButton;
	}

	private void handleException(Throwable exception) {
	}

	private void initConnections() throws Exception {
		getTotalButton().addActionListener(this.ivjEventHandler);
		getFreeButton().addActionListener(this.ivjEventHandler);
		getRequestedButton().addActionListener(this.ivjEventHandler);
		getFreedButton().addActionListener(this.ivjEventHandler);
		getSinceButton().addActionListener(this.ivjEventHandler);
		getCompletedButton().addActionListener(this.ivjEventHandler);
		getMarkButton().addActionListener(this.ivjEventHandler);
		getSweepButton().addActionListener(this.ivjEventHandler);
		getCompactButton().addActionListener(this.ivjEventHandler);
		getGCButton().addActionListener(this.ivjEventHandler);
		getZoomInButton().addActionListener(this.ivjEventHandler);
		getZoomOutButton().addActionListener(this.ivjEventHandler);
		getCenterButton().addActionListener(this.ivjEventHandler);
		getSelectButton().addActionListener(this.ivjEventHandler);
		getUsedButton().addActionListener(this.ivjEventHandler);
		getOverheadButton().addActionListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("ControlPanel");
			setFont(new Font("dialog", 0, 10));
			setLayout(getControlPanelBoxLayout());
			setSize(62, 360);
			add(getFreeButton(), getFreeButton().getName());
			add(getUsedButton(), getUsedButton().getName());
			add(getTotalButton(), getTotalButton().getName());
			add(getRequestedButton(), getRequestedButton().getName());
			add(getFreedButton(), getFreedButton().getName());
			add(getSinceButton(), getSinceButton().getName());
			add(getCompletedButton(), getCompletedButton().getName());
			add(getMarkButton(), getMarkButton().getName());
			add(getSweepButton(), getSweepButton().getName());
			add(getCompactButton(), getCompactButton().getName());
			add(getGCButton(), getGCButton().getName());
			add(getOverheadButton(), getOverheadButton().getName());
			add(getZoomInButton(), getZoomInButton().getName());
			add(getZoomOutButton(), getZoomOutButton().getName());
			add(getCenterButton(), getCenterButton().getName());
			add(getSelectButton(), getSelectButton().getName());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			ControlPanel aControlPanel = new ControlPanel();
			frame.setContentPane(aControlPanel);
			frame.setSize(aControlPanel.getSize());
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
			System.err.println("Exception occurred in main() of javax.swing.JPanel");
			exception.printStackTrace(System.out);
		}
	}

	public void markButton_ActionPerformed(ActionEvent actionEvent) {
		if (getMarkButton().isSelected())
			this.cp.mark = true;
		else
			this.cp.mark = false;
		this.cp.repaint();
	}

	public void overheadButton_ActionPerformed(ActionEvent actionEvent) {
		if (getOverheadButton().isSelected())
			this.cp.overhead = true;
		else
			this.cp.overhead = false;
		this.cp.repaint();
	}

	public void requestedButton_ActionPerformed(ActionEvent actionEvent) {
		if (getRequestedButton().isSelected())
			this.cp.requested = true;
		else
			this.cp.requested = false;
		this.cp.repaint();
	}

	public void selectButton_ActionPerformed(ActionEvent actionEvent) {
		if (getSelectButton().isSelected()) {
			this.cp.setCursor(new Cursor(12));
			this.cp.select = true;
		} else {
			this.cp.setCursor(new Cursor(0));
			this.cp.select = false;
		}

		if (getCenterButton().isSelected()) {
			getCenterButton().setSelected(false);
			this.cp.zoomCenter = false;
		}

		if (getZoomOutButton().isSelected()) {
			getZoomOutButton().setSelected(false);
			this.cp.zoomOut = false;
		}

		if (getZoomInButton().isSelected()) {
			getZoomInButton().setSelected(false);
			this.cp.zoomIn = false;
		}
	}

	public void sinceButton_ActionPerformed(ActionEvent actionEvent) {
		if (getSinceButton().isSelected())
			this.cp.since = true;
		else
			this.cp.since = false;
		this.cp.repaint();
	}

	public void sweepButton_ActionPerformed(ActionEvent actionEvent) {
		if (getSweepButton().isSelected())
			this.cp.sweep = true;
		else
			this.cp.sweep = false;
		this.cp.repaint();
	}

	public void totalButton_ActionPerformed(ActionEvent actionEvent) {
		if (getTotalButton().isSelected())
			this.cp.total = true;
		else
			this.cp.total = false;
		this.cp.repaint();
	}

	public void usedButton_ActionPerformed(ActionEvent actionEvent) {
		if (getUsedButton().isSelected())
			this.cp.used = true;
		else
			this.cp.used = false;
		this.cp.repaint();
	}

	public void zoomInButton_ActionPerformed(ActionEvent actionEvent) {
		if (getZoomInButton().isSelected()) {
			this.cp.setCursor(new Cursor(1));

			this.cp.zoomIn = true;
		} else {
			this.cp.zoomIn = false;
			this.cp.setCursor(new Cursor(0));
		}

		if (getCenterButton().isSelected()) {
			getCenterButton().setSelected(false);
			this.cp.zoomCenter = false;
		}

		if (getZoomOutButton().isSelected()) {
			getZoomOutButton().setSelected(false);
			this.cp.zoomOut = false;
		}

		if (getSelectButton().isSelected()) {
			getSelectButton().setSelected(false);
			this.cp.select = false;
		}
	}

	public void zoomOutButton_ActionPerformed(ActionEvent actionEvent) {
		if (getZoomOutButton().isSelected()) {
			this.cp.setCursor(new Cursor(1));
			this.cp.zoomOut = true;
		} else {
			this.cp.setCursor(new Cursor(0));
			this.cp.zoomOut = false;
		}
		if (getZoomInButton().isSelected()) {
			getZoomInButton().setSelected(false);
			this.cp.zoomIn = false;
		}

		if (getCenterButton().isSelected()) {
			getCenterButton().setSelected(false);
			this.cp.zoomCenter = false;
		}
		if (getSelectButton().isSelected()) {
			getSelectButton().setSelected(false);
			this.cp.select = false;
		}
	}

	class IvjEventHandler implements ActionListener {
		IvjEventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ControlPanel.this.getTotalButton())
				ControlPanel.this.connEtoC1(e);
			if (e.getSource() == ControlPanel.this.getFreeButton())
				ControlPanel.this.connEtoC2(e);
			if (e.getSource() == ControlPanel.this.getRequestedButton())
				ControlPanel.this.connEtoC3(e);
			if (e.getSource() == ControlPanel.this.getFreedButton())
				ControlPanel.this.connEtoC4(e);
			if (e.getSource() == ControlPanel.this.getSinceButton())
				ControlPanel.this.connEtoC5(e);
			if (e.getSource() == ControlPanel.this.getCompletedButton())
				ControlPanel.this.connEtoC6(e);
			if (e.getSource() == ControlPanel.this.getMarkButton())
				ControlPanel.this.connEtoC7(e);
			if (e.getSource() == ControlPanel.this.getSweepButton())
				ControlPanel.this.connEtoC8(e);
			if (e.getSource() == ControlPanel.this.getCompactButton())
				ControlPanel.this.connEtoC9(e);
			if (e.getSource() == ControlPanel.this.getGCButton())
				ControlPanel.this.connEtoC10(e);
			if (e.getSource() == ControlPanel.this.getZoomInButton())
				ControlPanel.this.connEtoC11(e);
			if (e.getSource() == ControlPanel.this.getZoomOutButton())
				ControlPanel.this.connEtoC12(e);
			if (e.getSource() == ControlPanel.this.getCenterButton())
				ControlPanel.this.connEtoC13(e);
			if (e.getSource() == ControlPanel.this.getSelectButton())
				ControlPanel.this.connEtoC14(e);
			if (e.getSource() == ControlPanel.this.getUsedButton())
				ControlPanel.this.connEtoC15(e);
			if (e.getSource() == ControlPanel.this.getOverheadButton())
				ControlPanel.this.connEtoC16(e);
		}
	}
}
