package com.ibm.jinwoo.thread;

import java.io.File;
import java.io.Serializable;

public class GCInfo implements Serializable {
	private static final long serialVersionUID = -2494100003705167872L;
	File file;
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
