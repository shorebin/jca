package com.ibm.jinwoo.thread;

import java.io.File;
import java.io.PrintStream;

public class TestJCA {

	public static void main(String[] args) {
		File f = new File("D:/k49/dev/javatool/jca423/javacore.20160708.091648.7221.0002.txt");
		File[] file = new File[] { f };
		Configuration cfg = new Configuration();
		Analyzer a;
		PrintStream out = System.out;
		PrintStream err = System.err;
		try {
			a = new Analyzer();
			JDialogProgress jp = new JDialogProgress(a);
			FileTask ft = new FileTask(a, file, jp);
			ft.processThreadDump(file, cfg, false);
		} finally {
			System.setOut(out);
			System.setErr(err);
		}
		ThreadDump td = a.ti.threadDumps.get(0);
		for (String n : td.name) {
			System.out.println(n);
		}

	}

}
