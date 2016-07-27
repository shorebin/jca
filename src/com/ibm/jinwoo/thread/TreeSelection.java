package com.ibm.jinwoo.thread;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class TreeSelection extends TransferHandler {
	private static final long serialVersionUID = -8233463189643672511L;
	static NumberFormat nf = NumberFormat.getNumberInstance();

	public Transferable createTransferable(JComponent comp) {
		TreePath[] tp = ((JTree) comp).getSelectionPaths();
		int[][] row = new int[2][tp.length];
		for (int i = 0; i < tp.length; i++) {
			row[0][i] = ((JTree) comp).getRowForPath(tp[i]);
			row[1][i] = i;
		}
		Arrays2.sort(row);
		int[] space = new int[tp.length];
		int min = tp[0].getPathCount();
		for (int i = 0; i < space.length; i++) {
			space[i] = tp[i].getPathCount();
			if (space[i] < min)
				min = space[i];
		}
		for (int i = 0; i < space.length; i++) {
			space[i] -= min;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < tp.length; i++) {
			Monitor currentNode = (Monitor) tp[row[1][i]].getLastPathComponent();
			if (currentNode != null) {
				int id = currentNode.owner;
				MonitorModel model = (MonitorModel) ((JTree) comp).getModel();

				if (id == -1) {
					sb.append(
							"[TotalSize/Size] ThreadName (ObjectName) " + nf.format(model.rootChildren.length) + "\n");
				} else {
					for (int j = 0; j < space[row[1][i]]; j++) {
						sb.append(' ');
					}
					if (model.child[id] != null) {
						if (model.objectName[id] == -1)
							sb.append("[" + nf.format(model.total[id]) + "/" + nf.format(model.size[id]) + "] "
									+ model.getThreadName(id) + "\n");
						else {
							sb.append("[" + nf.format(model.total[id]) + "/" + nf.format(model.size[id]) + "] "
									+ model.getThreadName(id) + " (" + model.objectArray[model.objectName[id]] + ")\n");
						}

					} else {
						sb.append(model.getThreadName(id) + " (" + model.objectArray[model.objectName[id]] + ")\n");
					}

				}

			}

		}

		return new StringSelection(sb.toString());
	}

	public int getSourceActions(JComponent c) {
		return 1;
	}
}
