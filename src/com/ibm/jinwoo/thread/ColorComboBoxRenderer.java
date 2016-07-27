package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColorComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = -4617618995273728345L;
	Configuration cfg = null;
	JComboBox<String> cb = null;
	static String[] stateString = { "Runnable", "Wait on Condition", "Wait on Monitor", "Suspended", "Object.wait",
			"Blocked", "Hang Suspect", "Deadlock", "Parked" };

	public ColorComboBoxRenderer() {
	}

	public ColorComboBoxRenderer(Configuration cfg) {
		this.cfg = cfg;
		setOpaque(true);
	}

	public ColorComboBoxRenderer(Configuration cfg, JComboBox<String> cb) {
		this.cfg = cfg;
		this.cb = cb;
		setOpaque(true);
	}

	public ColorComboBoxRenderer(String text) {
		super(text);
	}

	public ColorComboBoxRenderer(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public ColorComboBoxRenderer(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public ColorComboBoxRenderer(Icon image) {
		super(image);
	}

	public ColorComboBoxRenderer(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (this.cfg == null) {
			return this;
		}
		Color selectedColor = this.cfg.runnable;
		String selectedString = (String) value;
		int selected = 0;

		for (int i = 0; i < stateString.length; i++) {
			if (selectedString.startsWith(stateString[i])) {
				selected = i;
				break;
			}
		}
		switch (selected) {
		case 0:
			selectedColor = this.cfg.runnable;
			break;
		case 1:
			selectedColor = this.cfg.condition;
			break;
		case 2:
			selectedColor = this.cfg.monitor;
			break;
		case 3:
			selectedColor = this.cfg.suspended;
			break;
		case 4:
			selectedColor = this.cfg.object;
			break;
		case 5:
			selectedColor = this.cfg.blocked;
			break;
		case 6:
			selectedColor = this.cfg.hang;
			break;
		case 7:
			selectedColor = this.cfg.deadlock;
			break;
		case 8:
			selectedColor = this.cfg.park;
		}

		if (this.cb != null) {
			this.cb.setBackground(selectedColor);
		}
		setBackground(selectedColor);
		if (isSelected) {
			setForeground(Color.white);
		} else {
			setForeground(list.getForeground());
		}

		setText(stateString[selected]);

		return this;
	}
}
