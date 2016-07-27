package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class SortableHeaderIcon implements Icon {
	boolean direction;
	int size;

	public SortableHeaderIcon() {
	}

	public SortableHeaderIcon(boolean direction, int size) {
		this.direction = direction;
		this.size = size;
	}

	public int getIconHeight() {
		return this.size;
	}

	public int getIconWidth() {
		return this.size;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color color;
		if (c == null)
			color = Color.gray;
		else {
			color = c.getBackground();
		}

		int dx = this.size * 2 / 3;
		int dy = this.direction ? dx : -dx;

		y = y + 5 * this.size / 6 + (this.direction ? -dy : 0);

		g.translate(x, y);

		if (this.direction) {
			g.setColor(Color.blue);
		} else {
			dx++;
			dy--;
			g.setColor(Color.red);
		}

		int[] xP = { dx / 2, 0, dx };
		int[] yP = { dy, 0, 0 };
		g.fillPolygon(xP, yP, 3);

		g.setColor(color);
		g.translate(-x, -y);
	}
}
