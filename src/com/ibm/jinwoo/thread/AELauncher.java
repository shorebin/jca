package com.ibm.jinwoo.thread;

import java.util.Properties;
import java.util.StringTokenizer;

public class AELauncher {
	static final String cfgFile = "tdv.cfg";
	static final String usage = "Usage : InputFiles -f OutputHTMLFile \n-InputFiles: input file selection set -- a list of filesystem paths identifying the input files selected by the user, with paths delimited by the value of the Java System property  path.separator.\n-OutputHTMLFile: guaranteed not to be an existing file\n";

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println(
					"Usage : InputFiles -f OutputHTMLFile \n-InputFiles: input file selection set -- a list of filesystem paths identifying the input files selected by the user, with paths delimited by the value of the Java System property  path.separator.\n-OutputHTMLFile: guaranteed not to be an existing file\n");
			return;
		}
		if (!args[1].equalsIgnoreCase("-f")) {
			System.out.println(
					"Usage : InputFiles -f OutputHTMLFile \n-InputFiles: input file selection set -- a list of filesystem paths identifying the input files selected by the user, with paths delimited by the value of the Java System property  path.separator.\n-OutputHTMLFile: guaranteed not to be an existing file\n");
			return;
		}

		String separator = System.getProperty("path.separator");
		StringTokenizer st = new StringTokenizer(args[0], separator);
		int numberOfFiles = st.countTokens();
		String[] inputFiles = new String[numberOfFiles];
		for (int i = 0; (i < numberOfFiles) && (st.hasMoreTokens()); i++) {
			inputFiles[i] = st.nextToken();
		}
		Properties p = new Properties();

		AnalyzerHeadless hl = new AnalyzerHeadless(args[2]);
		hl.readConfiguration("tdv.cfg");
		hl.threadAnalysis(inputFiles, p, args[2]);
	}
}
