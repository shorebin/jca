package com.ibm.jinwoo.thread;

import java.io.File;
import java.io.PrintStream;

public class TestJCA {

	public static void main(String[] args) {
		File f = new File("D:/k49/dev/javatool/jca423/javacore.20160708.091648.7221.0002.txt");
		File[] file = new File[] { f };
		FileTaskExt ft;
		PrintStream out = System.out;
		PrintStream err = System.err;
		try {
			ft = new FileTaskExt(file);
		} finally {
			System.setOut(out);
			System.setErr(err);
		}
		ThreadDump td = ft.ti.threadDumps.get(0);
		for (String n : td.name) {
			System.out.println(n);
		}

	}

}
