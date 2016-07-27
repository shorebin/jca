package com.ibm.jinwoo.thread;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;

public class ChartPanel extends JPanel {
	private static final long serialVersionUID = 8552062451721063744L;
	Analyzer gca;
	public boolean zoomIn;
	public boolean zoomOut;
	public boolean zoomCenter;
	public boolean select;
	static final BasicStroke stroke = new BasicStroke(1.0F);

	static final float[] dash1 = { 2.0F };
	static final BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 2.0F, dash1, 0.0F);

	int x = 50;
	int y = 50;
	static final int XFACTOR = 60;
	static final int minZoom = 5;
	static final int leftMargin = 100;
	static final int bottomMargin = 50;
	static final int topMargin = 20;
	GCInfo gi;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d");

	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	static final Color bg = Color.white;
	static final Color fg = Color.black;
	static final Color red = Color.red;
	static final Color white = Color.white;
	static final Color blue = Color.blue;
	static final Color pink = Color.pink;
	static final Color green = Color.green;
	static final Color cyan = Color.cyan;
	static final Color magenta = Color.magenta;
	static final Color orange = Color.orange;
	static final Color yellow = Color.yellow;
	static final Color gray = Color.gray;
	long zoom;
	long oldZoom;
	int center = -1;
	long lx;
	long rx;
	int il;
	int ir;
	boolean noMoreZoomIn = false;
	boolean noMoreZoomOut = false;
	Dimension dimension;
	boolean free = true;
	boolean total = false;
	boolean freed = false;
	boolean requested = false;
	boolean since = false;
	boolean compact = false;
	boolean mark = false;
	boolean sweep = false;
	boolean completed = false;
	boolean gccompleted = false;
	boolean used;
	boolean overhead;
	boolean[] isShortage;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	public ChartPanel() {
		initialize();
	}

	public ChartPanel(GCInfo gi) {
		initialize();
		this.gi = gi;
		this.il = 0;
		this.ir = (gi.free.length - 1);
		this.lx = gi.timestamp[this.il];
		this.rx = gi.timestamp[this.ir];
		this.zoom = (this.oldZoom = this.rx - this.lx);

		if (gi.outOfHeapSpace != null) {
			this.isShortage = new boolean[gi.free.length];
			for (int i = 0; i < gi.outOfHeapSpace.length; i++)
				this.isShortage[gi.outOfHeapSpace[i]] = true;
		}
	}

	public ChartPanel(GCInfo gi, Analyzer gca) {
		initialize();
		this.gi = gi;
		this.gca = gca;
		this.il = 0;
		this.ir = (gi.free.length - 1);
		this.lx = gi.timestamp[this.il];
		this.rx = gi.timestamp[this.ir];
		this.zoom = (this.oldZoom = this.rx - this.lx);

		if (gi.outOfHeapSpace != null) {
			this.isShortage = new boolean[gi.free.length];
			for (int i = 0; i < gi.outOfHeapSpace.length; i++)
				this.isShortage[gi.outOfHeapSpace[i]] = true;
		}
	}

	public ChartPanel(LayoutManager layout) {
		super(layout);
	}

	public ChartPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ChartPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public void chartPanel_MouseClicked(MouseEvent mouseEvent) {
		this.center = mouseEvent.getX();
		if ((this.center > this.dimension.width - 100) || (this.center < 100)) {
			return;
		}
		int Y = mouseEvent.getY();
		if ((Y > this.dimension.height - 50) || (Y < 20)) {
			return;
		}
		if (this.select) {
			float ratio2 = (this.center - 100.0F) / (this.dimension.width - 200.0F);

			long selectedTS = this.lx + (long) ((float) (this.rx - this.lx) * ratio2);
			int selectedIdx = this.il;

			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.timestamp[i] == selectedTS) {
					selectedIdx = i;
					break;
				}
				if (this.gi.timestamp[i] > selectedTS) {
					selectedIdx = i;
					if (selectedIdx == this.il)
						break;
					if (selectedTS - this.gi.timestamp[(selectedIdx - 1)] >= this.gi.timestamp[selectedIdx]
							- selectedTS)
						break;
					selectedIdx--;

					break;
				}
			}
			showTable(selectedIdx);
		}
		if (this.zoomIn) {
			this.oldZoom = this.zoom;
			this.zoom /= 2L;

			repaint();
		} else if (this.zoomOut) {
			this.oldZoom = this.zoom;
			this.zoom *= 2L;

			repaint();
		} else if (this.zoomCenter) {
			this.oldZoom = -1L;

			repaint();
		}
	}

	private void connEtoC1(MouseEvent arg1) {
		try {
			chartPanel_MouseClicked(arg1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public int get10(long x) {
		int i = 0;
		while ((x /= 10L) >= 1L) {
			i++;
		}
		return i;
	}

	long getCompleted(int idx) {
		if (this.gi.completed[idx] != 0L)
			return this.gi.completed[idx];
		if (this.gi.gccompleted[idx] == 0L)
			return 0L;
		for (int i = idx; i < this.gi.free.length; i++) {
			if (this.gi.completed[i] != 0L)
				return this.gi.completed[i];
		}
		return 0L;
	}

	public int getFirstDigit(long x) {
		long y = x;
		while ((x /= 10L) >= 1L) {
			y = x;
		}
		return (int) y;
	}

	long getRequested(int idx) {
		if (this.gi.af[idx] != 0L)
			return this.gi.af[idx];
		for (int i = idx; i >= 0; i--) {
			if (this.gi.af[i] != 0L)
				return this.gi.af[i];
		}
		return 0L;
	}

	public int getSecondDigit(long x) {
		long y = x;
		while ((x /= 10L) >= 10L) {
			y = x;
		}
		return (int) y;
	}

	long getSince(int idx) {
		if (this.gi.since[idx] != 0L)
			return this.gi.since[idx];
		for (int i = idx; i >= 0; i--) {
			if ((this.gi.since[i] != 0L) && (this.gi.af[i] != 0L))
				return this.gi.since[i];
		}
		return 0L;
	}

	private void handleException(Throwable exception) {
	}

	private void initConnections() throws Exception {
		addMouseListener(this.ivjEventHandler);
	}

	private void initialize() {
		try {
			setName("ChartPanel");
			setLayout(null);
			setBackground(new Color(204, 204, 204));
			setSize(696, 416);
			setMinimumSize(new Dimension(300, 200));
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	boolean isEndPoint(int idx) {
		if ((idx >= this.gi.free.length) || (idx < 0))
			return false;
		if (idx == this.gi.free.length - 1)
			return true;
		if (this.gi.ngc[(idx + 1)] <= this.gi.ngc[idx])
			return true;
		return false;
	}

	boolean isStartPoint(int idx) {
		if ((idx >= this.gi.free.length) || (idx < 0))
			return false;
		if (idx == 0)
			return true;
		if (this.gi.ngc[(idx - 1)] >= this.gi.ngc[idx])
			return true;
		return false;
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();

			ChartPanel aChartPanel = new ChartPanel();
			frame.setContentPane(aChartPanel);
			frame.setSize(aChartPanel.getSize());
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		this.dimension = getSize();

		Color fg3D = Color.lightGray;

		g.setColor(fg);

		g.clearRect(100, 20, this.dimension.width - 200, this.dimension.height - 70);
		g.setColor(Color.lightGray);
		g.draw3DRect(96, 16, this.dimension.width - 200 + 7, this.dimension.height - 70 + 7, true);
		g.draw3DRect(99, 19, this.dimension.width - 200 + 1, this.dimension.height - 70 + 1, false);

		if (this.zoom != this.oldZoom) {
			float ratio = (this.center - 100.0F) / (this.dimension.width - 200.0F);

			long mx = this.lx + (long) ((float) (this.rx - this.lx) * ratio);

			this.lx = (mx - this.zoom / 2L);
			this.rx = (mx + this.zoom / 2L);

			if (this.lx < this.gi.timestamp[0]) {
				this.il = 0;
			}
			if (this.rx > this.gi.timestamp[(this.gi.free.length - 1)]) {
				this.ir = (this.gi.free.length - 1);
			}

			for (int i = 0; i < this.gi.free.length; i++) {
				if (this.gi.timestamp[i] >= this.rx) {
					this.ir = i;
					break;
				}
			}

			for (int i = this.gi.free.length - 1; i >= 0; i--) {
				if (this.gi.timestamp[i] <= this.lx) {
					this.il = i;
					break;
				}

			}

			this.oldZoom = this.zoom;
		}

		long xw = this.rx - this.lx;

		long yw = 0L;
		long yw2 = 0L;

		if (this.free) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.free[i] > yw)
					yw = this.gi.free[i];
			}
		}
		if (this.used) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.total[i] - this.gi.free[i] > yw)
					yw = this.gi.total[i] - this.gi.free[i];
			}
		}
		if (this.total) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.total[i] > yw)
					yw = this.gi.total[i];
			}
		}
		if (this.freed) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.freed[i] > yw)
					yw = this.gi.freed[i];
			}
		}
		if (this.requested) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.af[i] > yw)
					yw = this.gi.af[i];
			}
		}

		if (this.since) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.since[i] > yw2)
					yw2 = this.gi.since[i];
			}
		}
		if (this.completed) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.completed[i] > yw2)
					yw2 = this.gi.completed[i];
			}
		}
		if (this.mark) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.mark[i] > yw2)
					yw2 = this.gi.mark[i];
			}
		}
		if (this.sweep) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.sweep[i] > yw2)
					yw2 = this.gi.sweep[i];
			}
		}
		if (this.compact) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.compact[i] > yw2)
					yw2 = this.gi.compact[i];
			}
		}
		if (this.gccompleted) {
			for (int i = this.il; i <= this.ir; i++) {
				if (this.gi.gccompleted[i] > yw2)
					yw2 = this.gi.gccompleted[i];

			}

		}

		if (this.overhead) {
			for (int i = this.il; i <= this.ir; i++) {
				if (!isStartPoint(i)) {
					long afOverhead = Math
							.round(100.0F * (float) getCompleted(i) / (float) (getSince(i) + getCompleted(i)));
					if (afOverhead > yw2)
						yw2 = afOverhead;

				}

			}

		}

		float ratioX = (this.dimension.width - 200.0F) / (float) xw;
		float ratioY = (this.dimension.height - 70.0F) / (float) yw;
		float ratioY2 = (this.dimension.height - 70.0F) / (float) yw2;

		g.setColor(fg);
		g.drawString(this.dateFormatter.format(new Date(this.lx)), 85, this.dimension.height - this.y + 40);
		g.drawString(this.timeFormatter.format(new Date(this.lx)), 77, this.dimension.height - this.y + 20);
		g.drawString(this.dateFormatter.format(new Date(this.rx)), this.dimension.width - 100 - 15,
				this.dimension.height - this.y + 40);
		g.drawString(this.timeFormatter.format(new Date(this.rx)), this.dimension.width - 100 - 23,
				this.dimension.height - this.y + 20);
		int numberOfxt = (this.dimension.width - 200) / 60;

		if (numberOfxt > 1) {
			long delta = (this.rx - this.lx) / numberOfxt;
			for (int i = 1; i < numberOfxt; i++) {
				g.setColor(fg);
				g.drawString(this.dateFormatter.format(new Date(delta * i + this.lx)),
						(int) ((float) (delta * i) * ratioX) + 100 - 15, this.dimension.height - this.y + 40);
				g.drawString(this.timeFormatter.format(new Date(delta * i + this.lx)),
						(int) ((float) (delta * i) * ratioX) + 100 - 23, this.dimension.height - this.y + 20);
				g2.setColor(fg3D);
				g2.setStroke(dashed);
				g2.drawLine((int) ((float) (delta * i) * ratioX) + 100, 20, (int) ((float) (delta * i) * ratioX) + 100,
						this.dimension.height - 20);
			}

		}

		int log = get10(yw);

		int first = getFirstDigit(yw);

		long maxLevel = 0L;
		if (yw > 9L) {
			maxLevel = (long) Math.pow(10.0D, log - 1) * getSecondDigit(yw);
		} else {
			maxLevel = first * (long) Math.pow(10.0D, log);
		}

		for (int i = 0; maxLevel > 0L; i++) {
			int t = this.dimension.height - (int) ((float) maxLevel * ratioY) - 50;
			g2.setColor(fg3D);
			g2.setStroke(dashed);
			g2.drawLine(100, t, this.dimension.width - 100, t);
			g.setColor(fg);
			if (i == 0)
				g.drawString("bytes", 20, 15);
			g.drawString(numberFormatter.format(maxLevel), 10, t + 5);
			if (first == 1)
				maxLevel -= (long) Math.pow(10.0D, log - 1);
			else {
				maxLevel -= (long) Math.pow(10.0D, log);
			}

		}

		int log2 = get10(yw2);

		int first2 = getFirstDigit(yw2);

		long maxLevel2 = 0L;
		if (yw2 > 9L) {
			maxLevel2 = (long) Math.pow(10.0D, log2 - 1) * getSecondDigit(yw2);
		} else {
			maxLevel2 = first2 * (long) Math.pow(10.0D, log2);
		}

		for (int i = 0; maxLevel2 > 0L; i++) {
			int t = this.dimension.height - (int) ((float) maxLevel2 * ratioY2) - 50;
			g2.setColor(fg3D);
			g2.setStroke(dashed);
			g2.drawLine(100, t, this.dimension.width - 100, t);
			g.setColor(fg);
			if (i == 0) {
				if (this.overhead)
					g.drawString("ms or %", 10 + this.dimension.width - 100, 15);
				else
					g.drawString("ms", 10 + this.dimension.width - 100, 15);
			}
			g.drawString(numberFormatter.format(maxLevel2), 10 + this.dimension.width - 100, t + 5);
			if (first2 == 1)
				maxLevel2 -= (long) Math.pow(10.0D, log2 - 1);
			else {
				maxLevel2 -= (long) Math.pow(10.0D, log2);
			}
		}

		g.setClip(100, 20, this.dimension.width - 200 + 1, this.dimension.height - 20 - 50 + 1);

		for (int i = this.il; i < this.ir; i++) {
			if (i + 1 == this.gi.free.length - 1) {
				g2.setStroke(dashed);
				g2.setColor(fg);
				g2.drawLine((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100, 20,
						(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
						this.dimension.height - 50);
			}

			if (this.gi.ngc[(i + 1)] < this.gi.ngc[i]) {
				g2.setStroke(dashed);
				g2.setColor(fg);
				g2.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100, 20,
						(int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100, this.dimension.height - 50);
			} else {
				g2.setStroke(stroke);
				if (this.free) {
					g.setColor(this.gca.cfg.free);

					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.free[i] * ratioY) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.free[(i + 1)] * ratioY) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.free[i] * ratioY) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.free[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.free[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
				}
				if (this.used) {
					g.setColor(this.gca.cfg.used);

					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) (this.gi.total[i] - this.gi.free[i]) * ratioY) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100, this.dimension.height
									- (int) ((float) (this.gi.total[(i + 1)] - this.gi.free[(i + 1)]) * ratioY) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height
											- (int) ((float) (this.gi.total[i] - this.gi.free[i]) * ratioY) - 50 - 2,
									4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height
											- (int) ((float) (this.gi.total[(i + 1)] - this.gi.free[(i + 1)]) * ratioY)
											- 50 - 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height
											- (int) ((float) (this.gi.total[(i + 1)] - this.gi.free[(i + 1)]) * ratioY)
											- 50 - 2,
									4, 4);
					}
				}
				if (this.total) {
					g.setColor(this.gca.cfg.total);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.total[i] * ratioY) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.total[(i + 1)] * ratioY) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.total[i] * ratioY) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.total[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.total[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
				}
				if (this.freed) {
					g.setColor(this.gca.cfg.freed);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.freed[i] * ratioY) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.freed[(i + 1)] * ratioY) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.freed[i] * ratioY) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.freed[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.freed[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
				}
				if (this.requested) {
					g.setColor(this.gca.cfg.requested);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.af[i] * ratioY) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.af[(i + 1)] * ratioY) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.af[i] * ratioY) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.af[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1)) {
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.af[(i + 1)] * ratioY) - 50 - 2, 4,
									4);
						}
					}
				}

				if (this.compact) {
					g.setColor(this.gca.cfg.compact);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.compact[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.compact[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.compact[i] * ratioY2) - 50 - 2, 4,
									4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.compact[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.compact[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
				}
				if (this.sweep) {
					g.setColor(this.gca.cfg.sweep);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.sweep[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.sweep[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.sweep[i] * ratioY2) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.sweep[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.sweep[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
				}
				if (this.mark) {
					g.setColor(this.gca.cfg.mark);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.mark[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.mark[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.mark[i] * ratioY2) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.mark[(i + 1)] * ratioY2) - 50 - 2, 4,
									4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.mark[(i + 1)] * ratioY2) - 50 - 2, 4,
									4);
					}
				}
				if (this.completed) {
					g.setColor(this.gca.cfg.completed);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.completed[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.completed[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.completed[i] * ratioY2) - 50 - 2, 4,
									4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.completed[(i + 1)] * ratioY2) - 50
											- 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.completed[(i + 1)] * ratioY2) - 50
											- 2,
									4, 4);
					}
				}
				if (this.since) {
					g.setColor(this.gca.cfg.since);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.since[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.since[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.since[i] * ratioY2) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.since[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.since[(i + 1)] * ratioY2) - 50 - 2,
									4, 4);
					}
				}
				if (this.gccompleted) {
					g.setColor(this.gca.cfg.gccompleted);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.gccompleted[i] * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) this.gi.gccompleted[(i + 1)] * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.gccompleted[i] * ratioY2) - 50 - 2,
									4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.gccompleted[(i + 1)] * ratioY2) - 50
											- 2,
									4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1)) {
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) this.gi.gccompleted[(i + 1)] * ratioY2) - 50
											- 2,
									4, 4);
						}

					}

				}

				if (this.overhead) {
					long afOverhead;
					if (isStartPoint(i))
						afOverhead = 0L;
					else
						afOverhead = Math
								.round(100.0F * (float) getCompleted(i) / (float) (getSince(i) + getCompleted(i)));
					long afOverhead2 = Math.round(
							100.0F * (float) getCompleted(i + 1) / (float) (getSince(i + 1) + getCompleted(i + 1)));
					g.setColor(this.gca.cfg.overhead);
					g.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) afOverhead * ratioY2) - 50,
							(int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100,
							this.dimension.height - (int) ((float) afOverhead2 * ratioY2) - 50);
					if (this.gca.cfg.terminals) {
						if (isStartPoint(i))
							g.fillRect((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) afOverhead * ratioY2) - 50 - 2, 4, 4);
						if (isEndPoint(i + 1))
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) afOverhead2 * ratioY2) - 50 - 2, 4, 4);
					}
					if (this.gca.cfg.points) {
						if (!isEndPoint(i + 1)) {
							g.fillRect((int) ((float) (this.gi.timestamp[(i + 1)] - this.lx) * ratioX) + 100 - 2,
									this.dimension.height - (int) ((float) afOverhead2 * ratioY2) - 50 - 2, 4, 4);
						}

					}

				}

			}

			if ((this.gi.outOfHeapSpace != null) && (this.isShortage[i])) {
				g2.setStroke(dashed);
				g2.setColor(red);

				g2.drawLine((int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100, 20,
						(int) ((float) (this.gi.timestamp[i] - this.lx) * ratioX) + 100, this.dimension.height - 50);
			}
		}

		g.setClip(null);

		g.setColor(fg);
	}

	public void showTable(int idx) {
		HeapFrame hf = new HeapFrame(this.gi.file.getName() + " GC View", this.gi);
		hf.fileName = this.gi.file.getName();
		JTable jt = new JTable();

		jt.setDragEnabled(true);
		jt.getTableHeader().setToolTipText("Click to sort ; Click again to sort in reverse order");

		hf.JScrollPaneSetViewportView(jt);

		this.gca.getJDesktopPane1().add(hf);
		this.gca.getJDesktopPane1().getDesktopManager().activateFrame(hf);
		try {
			hf.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		GCTableModel om = new GCTableModel(this.gi, 12);
		om.setTableHeader(jt.getTableHeader());
		if (om.sortedArrary == null) {
			if (this.gca.cfg.verbose)
				System.out.println(new Date() + " Requesting " + numberFormatter.format(this.gi.total.length * 16)
						+ " bytes of Java heap.");
			om.sortedArrary = new long[2][this.gi.total.length];
		}
		jt.setModel(om);

		JViewport vp = hf.ivjJScrollPane1.getViewport();
		vp.scrollRectToVisible(new Rectangle(1, (int) (jt.getHeight() * idx / this.gi.total.length), 1, 1));
		jt.changeSelection(idx, 0, false, false);
	}

	class IvjEventHandler implements MouseListener {
		IvjEventHandler() {
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == ChartPanel.this)
				ChartPanel.this.connEtoC1(e);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}
