package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;

public class Configuration implements Serializable {
	private static final long serialVersionUID = -3202770688981538711L;
	int lookAndFeel = 0;
	String consoleText = "";
	File workingDir = new File(System.getProperty("user.dir"));
	boolean verbose = true;
	boolean terminals = true;
	boolean points;
	boolean save = true;
	Color runnable = Color.green;

	Color condition = new Color(138, 43, 226);
	Color monitor = Color.blue;
	Color suspended = Color.pink;
	Color object = Color.orange;
	Color hang = Color.red;
	Color blocked = Color.magenta;
	Color deadlock = Color.gray;
	Color park = Color.cyan;

	Color free = Color.blue;
	Color freed = Color.green;
	Color overhead = Color.yellow;
	Color used = Color.red;
	Color total = Color.orange;
	Color requested = Color.cyan;
	Color since = Color.magenta;
	Color completed = Color.pink;
	Color gccompleted = Color.gray;
	Color mark = Color.blue;
	Color sweep = Color.red;
	Color compact = Color.green;

	public String toString() {
		return super.toString();
	}
}
