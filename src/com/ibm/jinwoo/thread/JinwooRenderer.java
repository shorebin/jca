package com.ibm.jinwoo.thread;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class JinwooRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -382059114287441939L;
	MonitorModel model;
	static NumberFormat nf = NumberFormat.getNumberInstance();

	public JinwooRenderer() {
	}

	public JinwooRenderer(MonitorModel m) {
		this.model = m;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		int id = ((Monitor) value).owner;
		if (id == -1) {
			setText("[TotalSize/Size] ThreadName (ObjectName) " + nf.format(this.model.rootChildren.length));
		} else {
			TreePath tp = tree.getPathForRow(row);
			if (tp != null) {
				Monitor m1 = (Monitor) tp.getLastPathComponent();
				if ((m1 != null) && (tp.getPathCount() > 2)) {
					TreePath tpp = tp.getParentPath();
					while (tpp != null) {
						if (m1.owner == ((Monitor) tpp.getLastPathComponent()).owner) {
							setIcon(new ImageIcon(getClass().getResource("/Dup16.gif")));
							break;
						}
						tpp = tpp.getParentPath();
					}
				}
			}

			if (((Monitor) value).isDeadlock) {
				setIcon(new ImageIcon(getClass().getResource("/deadlock_view.gif")));
			} else {
				switch (this.model.getThreadState(id)) {
				case 0:
					setIcon(new ImageIcon(getClass().getResource("/run.gif")));
					break;
				case 1:
					setIcon(new ImageIcon(getClass().getResource("/condition.gif")));
					break;
				case 2:
					setIcon(new ImageIcon(getClass().getResource("/monitor_wait.gif")));
					break;
				case 3:
					setIcon(new ImageIcon(getClass().getResource("/suspend.gif")));
					break;
				case 4:
					setIcon(new ImageIcon(getClass().getResource("/waiting.gif")));
					break;
				case 5:
					setIcon(new ImageIcon(getClass().getResource("/block.gif")));
					break;
				case 6:
					setIcon(new ImageIcon(getClass().getResource("/park.gif")));
				}

			}

			if (this.model.child[id] != null) {
				if (this.model.objectName[id] == -1)
					setText("[" + nf.format(this.model.total[id]) + "/" + nf.format(this.model.size[id]) + "] "
							+ this.model.getThreadName(id));
				else {
					setText("[" + nf.format(this.model.total[id]) + "/" + nf.format(this.model.size[id]) + "] "
							+ this.model.getThreadName(id) + " (" + this.model.objectArray[this.model.objectName[id]]
							+ ")");
				}

			} else if (this.model.objectName[id] >= 0)
				setText(this.model.getThreadName(id) + " (" + this.model.objectArray[this.model.objectName[id]] + ")");
			else {
				setText(this.model.getThreadName(id));
			}

		}

		return this;
	}
}
