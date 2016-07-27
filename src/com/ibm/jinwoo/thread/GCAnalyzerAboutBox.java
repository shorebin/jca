package com.ibm.jinwoo.thread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class GCAnalyzerAboutBox extends JDialog {
	private static final long serialVersionUID = 3341367917221107090L;
	private ImageIcon in;
	private Image image;
	private static String versionInfo = "4.2.3";

	private JPanel ivjButtonPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JPanel ivjJDialogContentPane = null;
	private JButton ivjOkButton = null;
	private JPanel ivjTextPane = null;

	public static final String getVersionInfo() {
		return versionInfo;
	}

	public GCAnalyzerAboutBox() {
		initialize();
	}

	public GCAnalyzerAboutBox(Dialog owner) {
		super(owner);
	}

	public GCAnalyzerAboutBox(Dialog owner, String title) {
		super(owner, title);
	}

	public GCAnalyzerAboutBox(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public GCAnalyzerAboutBox(Dialog owner, boolean modal) {
		super(owner, modal);
	}

	public GCAnalyzerAboutBox(Frame owner) {
		super(owner);
	}

	public GCAnalyzerAboutBox(Frame owner, String title) {
		super(owner, title);
	}

	public GCAnalyzerAboutBox(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public GCAnalyzerAboutBox(Frame owner, boolean modal) {
		super(owner, modal);
	}

	public GCAnalyzerAboutBox(String ver, Frame owner) {
		super(owner);
		initialize();
	}

	private void connEtoM1(ActionEvent arg1) {
		try {
			dispose();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private JPanel getButtonPane() {
		if (this.ivjButtonPane == null) {
			try {
				this.ivjButtonPane = new JPanel();
				this.ivjButtonPane.setName("ButtonPane");
				this.ivjButtonPane.setLayout(new FlowLayout());
				getButtonPane().add(getOkButton(), getOkButton().getName());

				JButton authorButton = new JButton("Author");
				JButton siteButton = new JButton("Home");
				this.ivjButtonPane.add(authorButton);
				this.ivjButtonPane.add(siteButton);
				authorButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (Desktop.isDesktopSupported())
							try {
								Desktop.getDesktop().browse(new URI("http://jinwoohwang.sys-con.com/"));
							} catch (IOException e) {
								e.printStackTrace();
							} catch (URISyntaxException e) {
								e.printStackTrace();
							}
					}
				});
				siteButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (Desktop.isDesktopSupported()) {
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

				});
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjButtonPane;
	}

	private JPanel getJDialogContentPane() {
		if (this.ivjJDialogContentPane == null) {
			try {
				this.ivjJDialogContentPane = new JPanel();
				this.ivjJDialogContentPane.setName("JDialogContentPane");
				this.ivjJDialogContentPane.setLayout(new BorderLayout());
				getJDialogContentPane().add(getButtonPane(), "South");
				getJDialogContentPane().add(getTextPane(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjJDialogContentPane;
	}

	private JButton getOkButton() {
		if (this.ivjOkButton == null) {
			try {
				this.ivjOkButton = new JButton();
				this.ivjOkButton.setName("OkButton");
				this.ivjOkButton.setText("OK");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjOkButton;
	}

	private JPanel getTextPane() {
		if (this.ivjTextPane == null) {
			try {
				this.ivjTextPane = new JPanel() {
					private static final long serialVersionUID = 3052354204283688924L;

					protected void paintComponent(Graphics g) {
						g.drawImage(GCAnalyzerAboutBox.this.image, 0, 0, this);
						((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

						Font font = new Font(g.getFont().getName(), 1, 12);
						g.setFont(font);
						g.setColor(new Color(42, 42, 42));
						g.drawString(GCAnalyzerAboutBox.getVersionInfo(), 62, 287);
					}
				};
				this.ivjTextPane.setPreferredSize(new Dimension(this.in.getIconWidth(), this.in.getIconHeight()));
				this.ivjTextPane.setMinimumSize(new Dimension(this.in.getIconWidth(), this.in.getIconHeight()));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjTextPane;
	}

	private void handleException(Throwable exception) {
	}

	private void initConnections() throws Exception {
		getOkButton().addActionListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("GCAnalyzerAboutBox");
			setDefaultCloseOperation(2);

			this.in = new ImageIcon(getClass().getResource("/jca.jpg"));
			this.image = this.in.getImage();
			setSize(this.in.getIconWidth() + 6, this.in.getIconHeight() + 80);
			setTitle("About");
			setContentPane(getJDialogContentPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public static void main(String[] args) {
		try {
			GCAnalyzerAboutBox aGCAnalyzerAboutBox = new GCAnalyzerAboutBox();
			aGCAnalyzerAboutBox.setModal(true);
			aGCAnalyzerAboutBox.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			aGCAnalyzerAboutBox.show();
			Insets insets = aGCAnalyzerAboutBox.getInsets();
			aGCAnalyzerAboutBox.setSize(aGCAnalyzerAboutBox.getWidth() + insets.left + insets.right,
					aGCAnalyzerAboutBox.getHeight() + insets.top + insets.bottom);
			aGCAnalyzerAboutBox.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of javax.swing.JDialog");
			exception.printStackTrace(System.out);
		}
	}

	class IvjEventHandler implements ActionListener {
		IvjEventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == GCAnalyzerAboutBox.this.getOkButton())
				GCAnalyzerAboutBox.this.connEtoM1(e);
		}
	}
}
