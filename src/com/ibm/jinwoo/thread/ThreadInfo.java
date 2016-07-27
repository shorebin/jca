package com.ibm.jinwoo.thread;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ThreadInfo implements Serializable {
	private static final long serialVersionUID = -1946613594121775006L;
	File file;
	ArrayList<ThreadDump> threadDumps = new ArrayList<ThreadDump>();
	long[] timestamp;
	long[] freed;
	long[] free;
	long[] total;
	long[] mark;
	long[] sweep;
	long[] compact;
	long[] af;
	long[] since;
	long[] completed;
	long[] gccompleted;
	int[] ngc;
	int[] naf;
	int numberOfSet;
	int[] outOfHeapSpace;

	public String toString() {
		return super.toString();
	}
}
