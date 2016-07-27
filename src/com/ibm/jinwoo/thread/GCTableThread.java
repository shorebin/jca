package com.ibm.jinwoo.thread;

import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JTable;

public class GCTableThread extends Thread {
	GCInfo hi;
	JTable jt;
	Analyzer gca;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public GCTableThread() {
	}

	public GCTableThread(GCInfo h) {
		this.hi = h;
	}

	public GCTableThread(Runnable target) {
		super(target);
	}

	public GCTableThread(Runnable target, String name) {
		super(target, name);
	}

	public GCTableThread(String name) {
		super(name);
	}

	public GCTableThread(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	public GCTableThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}

	public GCTableThread(ThreadGroup group, String name) {
		super(group, name);
	}

	public GCTableThread(JTable j, GCInfo h) {
		this.hi = h;
		this.jt = j;
	}

	public GCTableThread(JTable j, GCInfo h, Analyzer gca) {
		this.hi = h;
		this.jt = j;
		this.gca = gca;
	}

	public void run() {
		GCTableModel om = new GCTableModel(this.hi, 12);
		om.setTableHeader(this.jt.getTableHeader());
		if (om.sortedArrary == null) {
			if (this.gca.cfg.verbose)
				System.out.println(new Date() + " Requesting " + numberFormatter.format(this.hi.total.length * 16)
						+ " bytes of Java heap.");
			om.sortedArrary = new long[2][this.hi.total.length];
		}

		this.jt.setModel(om);
	}
}
