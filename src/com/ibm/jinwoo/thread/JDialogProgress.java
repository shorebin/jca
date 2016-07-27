package com.ibm.jinwoo.thread;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class JDialogProgress extends JDialog {
	private static final long serialVersionUID = -3240582524771230620L;
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjJLabel1 = null;
	private JProgressBar ivjJProgressBar1 = null;
	private JLabel ivjJLabel2 = null;
	private JLabel ivjJLabel3 = null;
	private JProgressBar ivjOverallProgressBar = null;

	public JDialogProgress() {
		initialize();
	}

	public JDialogProgress(Dialog owner) {
		super(owner);
	}

	public JDialogProgress(Dialog owner, String title) {
		super(owner, title);
	}

	public JDialogProgress(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public JDialogProgress(Dialog owner, boolean modal) {
		super(owner, modal);
	}

	public JDialogProgress(Frame owner) {
		super(owner);
		initialize();
	}

	public JDialogProgress(Frame owner, String title) {
		super(owner, title);
	}

	public JDialogProgress(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public JDialogProgress(Frame owner, boolean modal) {
		super(owner, modal);
	}

	private JPanel getJDialogContentPane() {
		if (this.ivjJDialogContentPane == null) {
			try {
				this.ivjJDialogContentPane = new JPanel();
				this.ivjJDialogContentPane.setName("JDialogContentPane");
				this.ivjJDialogContentPane.setLayout(new GridBagLayout());

				GridBagConstraints constraintsJLabel1 = new GridBagConstraints();
				constraintsJLabel1.gridx = 1;
				constraintsJLabel1.gridy = 3;
				constraintsJLabel1.ipadx = 241;
				constraintsJLabel1.insets = new Insets(4, 21, 8, 21);
				getJDialogContentPane().add(getJLabel1(), constraintsJLabel1);

				GridBagConstraints constraintsJProgressBar1 = new GridBagConstraints();
				constraintsJProgressBar1.gridx = 1;
				constraintsJProgressBar1.gridy = 2;
				constraintsJProgressBar1.fill = 2;
				constraintsJProgressBar1.weightx = 1.0D;
				constraintsJProgressBar1.ipadx = 374;
				constraintsJProgressBar1.ipady = 1;
				constraintsJProgressBar1.insets = new Insets(6, 21, 4, 21);
				getJDialogContentPane().add(getJProgressBar1(), constraintsJProgressBar1);

				GridBagConstraints constraintsOverallProgressBar = new GridBagConstraints();
				constraintsOverallProgressBar.gridx = 1;
				constraintsOverallProgressBar.gridy = 5;
				constraintsOverallProgressBar.fill = 2;
				constraintsOverallProgressBar.weightx = 1.0D;
				constraintsOverallProgressBar.ipadx = 374;
				constraintsOverallProgressBar.ipady = 1;
				constraintsOverallProgressBar.insets = new Insets(5, 21, 43, 21);
				getJDialogContentPane().add(getOverallProgressBar(), constraintsOverallProgressBar);

				GridBagConstraints constraintsJLabel2 = new GridBagConstraints();
				constraintsJLabel2.gridx = 1;
				constraintsJLabel2.gridy = 4;
				constraintsJLabel2.ipadx = 38;
				constraintsJLabel2.insets = new Insets(8, 21, 5, 272);
				getJDialogContentPane().add(getJLabel2(), constraintsJLabel2);

				GridBagConstraints constraintsJLabel3 = new GridBagConstraints();
				constraintsJLabel3.gridx = 1;
				constraintsJLabel3.gridy = 1;
				constraintsJLabel3.ipadx = 24;
				constraintsJLabel3.insets = new Insets(14, 21, 5, 304);
				getJDialogContentPane().add(getJLabel3(), constraintsJLabel3);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJDialogContentPane;
	}

	public JLabel getJLabel1() {
		if (this.ivjJLabel1 == null) {
			try {
				this.ivjJLabel1 = new JLabel();
				this.ivjJLabel1.setName("JLabel1");
				this.ivjJLabel1.setText("Loading Thread Dump ...");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel1;
	}

	private JLabel getJLabel2() {
		if (this.ivjJLabel2 == null) {
			try {
				this.ivjJLabel2 = new JLabel();
				this.ivjJLabel2.setName("JLabel2");
				this.ivjJLabel2.setText("Overall progress");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel2;
	}

	private JLabel getJLabel3() {
		if (this.ivjJLabel3 == null) {
			try {
				this.ivjJLabel3 = new JLabel();
				this.ivjJLabel3.setName("JLabel3");
				this.ivjJLabel3.setText("Unit progress");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJLabel3;
	}

	public JProgressBar getJProgressBar1() {
		if (this.ivjJProgressBar1 == null) {
			try {
				this.ivjJProgressBar1 = new JProgressBar();
				this.ivjJProgressBar1.setName("JProgressBar1");
				this.ivjJProgressBar1.setStringPainted(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJProgressBar1;
	}

	public JProgressBar getOverallProgressBar() {
		if (this.ivjOverallProgressBar == null) {
			try {
				this.ivjOverallProgressBar = new JProgressBar();
				this.ivjOverallProgressBar.setName("OverallProgressBar");
				this.ivjOverallProgressBar.setStringPainted(true);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOverallProgressBar;
	}

	private void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
		JOptionPane.showMessageDialog(this, exception.toString(), "Exception", 2);
	}

	private void initialize() {
		try {
			setName("JDialogProgress");
			setDefaultCloseOperation(2);
			setSize(461, 215);
			setTitle("Analyzing Thread Dump");
			setContentPane(getJDialogContentPane());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			JDialogProgress aJDialogProgress = new JDialogProgress();
			aJDialogProgress.setModal(true);
			aJDialogProgress.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			aJDialogProgress.show();
			Insets insets = aJDialogProgress.getInsets();
			aJDialogProgress.setSize(aJDialogProgress.getWidth() + insets.left + insets.right,
					aJDialogProgress.getHeight() + insets.top + insets.bottom);
			aJDialogProgress.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of javax.swing.JDialog");
			exception.printStackTrace(System.out);
		}
	}
}
