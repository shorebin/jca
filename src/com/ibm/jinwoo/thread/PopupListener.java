package com.ibm.jinwoo.thread;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

class PopupListener extends MouseAdapter {
	JPopupMenu popup;
	JTable table;
	Analyzer analyzer;

	PopupListener(JPopupMenu popupMenu) {
		this.popup = popupMenu;
	}

	PopupListener(JPopupMenu popupMenu, JTable g) {
		this.popup = popupMenu;
		this.table = g;
	}

	PopupListener(JPopupMenu popupMenu, JTable g, Analyzer a) {
		this.popup = popupMenu;
		this.table = g;
		this.analyzer = a;
	}

	private void maybeShowPopup(MouseEvent e) {
		if (this.table.getSelectedRowCount() == 0)
			return;

		if (e.isPopupTrigger()) {
			this.popup.show(e.getComponent(), e.getX(), e.getY());
		} else {
			this.analyzer.displaySummary();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

}
