package com.ibm.jinwoo.thread;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MonitorModel implements TreeModel {
	ThreadDump tdump;
	MonitorDump[] mdump;
	Monitor rootNode;
	int[] rootChildren;
	boolean[] hasParent;
	boolean[] visited;
	boolean[] isPseudoRoot;
	long[] threadArray;
	int[] owner;
	int[] total;
	int[] size;
	int[] parent;
	int[] recursiveParent;
	int[][] child;
	boolean[] counted;
	int[] objectName;
	int[] id;
	String[] objectArray;
	boolean[] isHeapLock;
	boolean[] waitingHeapLock;
	int[][] owningObject;
	Hashtable<Integer, Integer> nodeCounter = new Hashtable<Integer, Integer>();

	public MonitorModel() {
	}

	public MonitorModel(ThreadDump td) {
		this.tdump = td;
		this.mdump = td.mdump;
		this.rootNode = new Monitor();
		this.rootNode.owner = -1;

		this.rootNode.threadName = "Thread Name";
		this.rootNode.objectName = "Object Name";

		Hashtable<Integer, Long> threadHash = new Hashtable<Integer, Long>();

		int index = 0;

		for (int i = 0; i < this.mdump.length; i++) {
			Long newOwner = new Long(this.mdump[i].owner);

			if (!threadHash.containsValue(newOwner)) {
				threadHash.put(new Integer(index++), newOwner);
			}

			if ((this.mdump[i].waiting != null) && (this.mdump[i].waiting.size() != 0)) {
				for (int j = 0; j < this.mdump[i].waiting.size(); j++) {
					newOwner = (Long) this.mdump[i].waiting.get(j);
					if (!threadHash.containsValue(newOwner)) {
						threadHash.put(new Integer(index++), newOwner);
					}

				}

			}

		}

		this.hasParent = new boolean[threadHash.size()];
		this.isPseudoRoot = new boolean[threadHash.size()];
		this.counted = new boolean[threadHash.size()];
		this.objectName = new int[threadHash.size()];
		this.threadArray = new long[threadHash.size()];
		this.visited = new boolean[threadHash.size()];
		this.total = new int[threadHash.size()];
		this.size = new int[threadHash.size()];
		this.parent = new int[threadHash.size()];
		this.recursiveParent = new int[threadHash.size()];
		for (int i = 0; i < this.recursiveParent.length; i++) {
			this.recursiveParent[i] = -1;
		}
		this.child = new int[threadHash.size()][];
		this.objectArray = new String[this.mdump.length];
		this.owner = new int[threadHash.size()];
		this.id = new int[threadHash.size()];
		this.isHeapLock = new boolean[threadHash.size()];
		this.waitingHeapLock = new boolean[threadHash.size()];
		this.owningObject = new int[threadHash.size()][];

		for (int i = 0; i < this.owner.length; i++) {
			this.owner[i] = -1;
			this.objectName[i] = -1;
			this.parent[i] = -1;
		}

		Enumeration<Long> enum2 = threadHash.elements();
		int idx = 0;
		int idx2 = 0;
		while (enum2.hasMoreElements()) {
			this.threadArray[(idx++)] = ((Long) enum2.nextElement()).longValue();
		}
		Arrays.sort(this.threadArray);
		for (int i = 0; i < this.id.length; i++) {
			this.id[i] = this.tdump.getIndexFromSysThread(this.threadArray[i]);
		}

		threadHash.clear();
		threadHash = null;

		for (int i = 0; i < this.mdump.length; i++) {
			this.objectArray[i] = this.mdump[i].objectName;
			if ((this.mdump[i].waiting != null) && (this.mdump[i].waiting.size() != 0)) {
				idx = Arrays.binarySearch(this.threadArray, this.mdump[i].owner);
				if (idx != -1) {
					if (this.owningObject[idx] == null) {
						this.owningObject[idx] = new int[1];
						this.owningObject[idx][0] = i;
					} else {
						int[] temp = new int[this.owningObject[idx].length];
						System.arraycopy(this.owningObject[idx], 0, temp, 0, temp.length);
						this.owningObject[idx] = new int[temp.length + 1];
						this.owningObject[idx][0] = i;
						System.arraycopy(temp, 0, this.owningObject[idx], this.owningObject[idx].length - temp.length,
								temp.length);
					}

					if (this.mdump[i].isHeapLock())
						this.isHeapLock[idx] = true;
					this.size[idx] += this.mdump[i].waiting.size();

					if (this.child[idx] == null) {
						this.child[idx] = new int[this.mdump[i].waiting.size()];
					} else {
						int[] temp = new int[this.child[idx].length];
						System.arraycopy(this.child[idx], 0, temp, 0, temp.length);

						this.child[idx] = new int[this.size[idx]];
						System.arraycopy(temp, 0, this.child[idx], this.child[idx].length - temp.length, temp.length);
					}

					for (int j = 0; j < this.mdump[i].waiting.size(); j++) {
						Long newOwner = (Long) this.mdump[i].waiting.get(j);
						idx2 = Arrays.binarySearch(this.threadArray, newOwner.longValue());
						if (idx2 != -1) {
							if (this.mdump[i].isHeapLock())
								this.waitingHeapLock[idx2] = true;
							this.hasParent[idx2] = true;
							this.objectName[idx2] = i;
							this.owner[idx2] = idx;
							this.child[idx][j] = idx2;
							this.recursiveParent[idx2] = idx;
						}
					}
				}
			}
		}

		Vector<Integer> rv = new Vector<Integer>();
		for (int i = 0; i < this.hasParent.length; i++) {
			if (!this.hasParent[i]) {
				calculateTotal(i);
				rv.addElement(new Integer(i));
			}
		}

		markPseudoRoot();
		for (int i = 0; i < this.hasParent.length; i++) {
			if ((!this.visited[i]) && (this.child[i] != null) && (this.isPseudoRoot[i])) {
				calculateTotal(i);
				rv.addElement(new Integer(i));
			}
		}

		Enumeration<Integer> enum3 = rv.elements();
		idx = 0;
		this.rootChildren = new int[rv.size()];
		while (enum3.hasMoreElements()) {
			this.rootChildren[(idx++)] = (enum3.nextElement()).intValue();
		}
		rv.clear();

		int[][] tempRoot = new int[2][this.rootChildren.length];
		for (int i = 0; i < this.rootChildren.length; i++) {
			tempRoot[0][i] = this.total[this.rootChildren[i]];
			tempRoot[1][i] = this.rootChildren[i];
		}
		Arrays2.sort(tempRoot);

		for (int i = 0; i < this.rootChildren.length; i++) {
			this.rootChildren[(this.rootChildren.length - 1 - i)] = tempRoot[1][i];
		}
		tempRoot = (int[][]) null;

		for (int i = 0; i < this.child.length; i++)
			if (this.child[i] != null) {
				int[][] tempChild = new int[2][this.child[i].length];
				for (int j = 0; j < this.child[i].length; j++) {
					tempChild[0][j] = this.total[this.child[i][j]];
					tempChild[1][j] = this.child[i][j];
				}
				Arrays2.sort(tempChild);

				for (int j = 0; j < this.child[i].length; j++)
					this.child[i][(this.child[i].length - 1 - j)] = tempChild[1][j];
			}
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public void calculateTotal(int r) {
		boolean anyChild = false;

		Stack<Integer> stack = new Stack<Integer>();

		this.total[r] = this.size[r];
		this.visited[r] = true;

		stack.push(new Integer(r));

		while (!stack.empty()) {
			int aNode = ((Integer) stack.peek()).intValue();

			anyChild = false;
			if (this.child[aNode] != null) {
				for (int i = 0; i < this.child[aNode].length; i++) {
					int index = this.child[aNode][i];

					if (index >= 0) {
						int cNode = index;

						if (!this.visited[cNode]) {
							this.parent[cNode] = aNode;

							this.visited[cNode] = true;
							this.total[cNode] = this.size[cNode];
							stack.push(new Integer(cNode));

							anyChild = true;
						}
					}
				}

			}

			if (!anyChild) {
				aNode = ((Integer) stack.pop()).intValue();

				if (this.child[aNode] != null)
					for (int i = 0; i < this.child[aNode].length; i++) {
						int index = this.child[aNode][i];
						if (index >= 0) {
							int cNode = index;
							if ((this.parent[cNode] == aNode) && (!this.counted[cNode])) {
								this.total[aNode] += this.total[cNode];
								this.counted[cNode] = true;
							}
						}
					}
			}
		}
	}

	public Object getChild(Object parent, int index) {
		if (((Monitor) parent).owner == -1) {
			Monitor m = new Monitor();
			m.owner = this.rootChildren[index];
			m.isHeapLock = this.isHeapLock[m.owner];
			m.waitingHeapLock = this.waitingHeapLock[m.owner];

			if ((this.id[m.owner] != -1) && (this.tdump.isDeadlock[this.id[m.owner]]))
				m.isDeadlock = true;

			return m;
		}

		if (this.child[((Monitor) parent).owner] == null)
			return null;

		Monitor m = new Monitor();

		m.owner = this.child[((Monitor) parent).owner][index];
		m.isHeapLock = this.isHeapLock[m.owner];
		m.waitingHeapLock = this.waitingHeapLock[m.owner];

		m.objectName = this.objectArray[this.objectName[m.owner]];
		if ((this.id[m.owner] != -1) && (this.tdump.isDeadlock[this.id[m.owner]]))
			m.isDeadlock = true;

		return m;
	}

	public int getChildCount(Object parent) {
		if (((Monitor) parent).owner == -1) {
			return this.rootChildren.length;
		}
		if (this.child[((Monitor) parent).owner] != null)
			return this.child[((Monitor) parent).owner].length;
		return 0;
	}

	public int getIndexOfChild(Object parent, Object c) {
		if (((Monitor) parent).owner == -1) {
			for (int i = 0; i < this.rootChildren.length; i++) {
				if (((Monitor) c).owner == this.rootChildren[i])
					return i;
			}
			return -1;
		}

		if (this.child[((Monitor) parent).owner] != null) {
			for (int i = 0; i < this.child[((Monitor) parent).owner].length; i++) {
				if (((Monitor) c).owner == this.child[((Monitor) parent).owner][i]) {
					return i;
				}
			}
		}
		return -1;
	}

	public String getOwningObjects(int index) {
		if ((index != -1) && (this.owningObject[index] != null) && (this.owningObject[index].length != 0)) {
			String objects = new String();
			boolean isFirst = true;
			for (int i = 0; i < this.owningObject[index].length; i++) {
				if (isFirst) {
					objects = this.objectArray[this.owningObject[index][i]];
					isFirst = false;
				} else {
					objects = objects + " , " + this.objectArray[this.owningObject[index][i]];
				}
			}
			return objects;
		}
		return null;
	}

	public Object getParent(Object parent) {
		if (((Monitor) parent).owner == -1)
			return this.rootNode;

		Monitor m = new Monitor();

		m.owner = this.recursiveParent[((Monitor) parent).owner];
		if (m.owner == -1)
			return this.rootNode;
		m.isHeapLock = this.isHeapLock[m.owner];
		m.waitingHeapLock = this.waitingHeapLock[m.owner];

		int obj = this.objectName[m.owner];
		if (obj != -1) {
			m.objectName = this.objectArray[obj];
		}
		int d = this.id[m.owner];
		if (d != -1) {
			if ((this.id[m.owner] != -1) && (this.tdump.isDeadlock[d]))
				m.isDeadlock = true;

		}

		return m;
	}

	public Object getRoot() {
		return this.rootNode;
	}

	public String getSummary(Monitor node) {
		if (node == null)
			return "";
		if (node.owner == -1)
			return "";

		if (this.id[node.owner] == -1)
			return "";

		String stackTrace = "Thread Name : " + this.tdump.name[this.id[node.owner]] + "<BR>State : "
				+ this.tdump.getState(this.id[node.owner]) + "<BR>";
		if (node.isHeapLock)
			stackTrace = stackTrace + "Owns Heap Lock<BR>";
		if (node.waitingHeapLock)
			stackTrace = stackTrace + "Waiting for Heap Lock<BR>";
		if (node.objectName != null)
			stackTrace = stackTrace + "Waiting for Monitor Lock on " + node.objectName + "<BR>";
		String oo = getOwningObjects(node.owner);
		if (oo != null)
			stackTrace = stackTrace + "Owns Monitor Lock on " + oo + "<BR>";

		if (this.tdump.javaStack[this.id[node.owner]] == null) {
			stackTrace = stackTrace + "No Java Stack available<BR>";
		} else {
			stackTrace = stackTrace + "Java Stack<BR>" + this.tdump.javaStack[this.id[node.owner]] + "<BR>";
		}
		if (this.tdump.nativeStack[this.id[node.owner]] != null)
			stackTrace = stackTrace + "Native Stack<BR>" + this.tdump.nativeStack[this.id[node.owner]];
		return stackTrace;
	}

	public int getThreadDumpIndex(Monitor node) {
		if (node == null)
			return -1;
		if (node.owner == -1)
			return -1;

		return this.id[node.owner];
	}

	public String getThreadName(int index) {
		long threadID = this.threadArray[index];

		for (int i = 0; i < this.tdump.sys_thread.length; i++) {
			if (threadID == this.tdump.sys_thread[i]) {
				return this.tdump.name[i];
			}
		}
		return "Unknown";
	}

	public int getThreadState(int index) {
		long threadID = this.threadArray[index];

		for (int i = 0; i < this.tdump.sys_thread.length; i++) {
			if (threadID == this.tdump.sys_thread[i]) {
				return this.tdump.state[i];
			}
		}
		return -1;
	}

	public boolean isLeaf(Object node) {
		if (((Monitor) node).owner == -1) {
			if (this.rootChildren.length == 0)
				return true;
			return false;
		}
		if (this.child[((Monitor) node).owner] == null)
			return true;
		return false;
	}

	public boolean isRecursive(Object node) {
		if (((Monitor) node).owner == -1) {
			return false;
		}

		HashSet<Integer> nodeSet = new HashSet<Integer>();
		Integer newNode = null;
		int count = 0;

		int current = ((Monitor) node).owner;
		int up;
		while ((up = this.recursiveParent[current]) != -1) {
			current = up;
			newNode = new Integer(up);
			if (this.nodeCounter.containsKey(newNode))
				count = ((Integer) this.nodeCounter.get(newNode)).intValue();
			else
				count = 0;
			if (up == ((Monitor) node).owner) {
				count++;
				this.nodeCounter.put(newNode, new Integer(count));
				if (count > 3) {
					return true;
				}
				return false;
			}

			if ((!nodeSet.isEmpty()) && (nodeSet.contains(newNode)))
				return false;
			nodeSet.add(newNode);
		}

		return false;
	}

	public boolean isRootChild(Object node) {
		return false;
	}

	public void markPseudoRoot() {
		Hashtable<Integer, String> hash = new Hashtable<Integer, String>();
		boolean[] checked = new boolean[this.hasParent.length];

		for (int i = 0; i < this.hasParent.length; i++) {
			if ((!this.visited[i]) && (this.child[i] != null) && (!checked[i])) {
				hash.clear();

				int c = i;
				checked[c] = true;
				Integer newInt = new Integer(c);
				hash.put(newInt, "");
				while (true) {
					int p = this.owner[c];

					if (p == -1)
						break;
					if (checked[p]) {
						newInt = new Integer(p);
						if (!hash.containsKey(newInt)) {
							break;
						}
						this.isPseudoRoot[c] = true;

						break;
					}
					checked[p] = true;
					newInt = new Integer(p);
					if (hash.containsKey(newInt)) {
						this.isPseudoRoot[c] = true;

						break;
					}

					hash.put(newInt, "");

					c = p;
				}
			}
		}
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}
}
