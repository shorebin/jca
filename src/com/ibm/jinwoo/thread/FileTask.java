package com.ibm.jinwoo.thread;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileTask {
	private static final String MEMUSER = "MEMUSER";
	static final String BTT = "<p><a href=\"#top\">Back to top</a></p>";
	static NumberFormat nf = NumberFormat.getNumberInstance();
	int fileSequenceNumber;
	String outputHTMLFileName = null;
	boolean debug = false;
	Configuration cfg;
	int monIndex = 0;
	List<MonitorDump> monList = new ArrayList<MonitorDump>();
	Hashtable<Long, Long> threadIdent = new Hashtable<Long, Long>();
	public static final int SIZE_OF_LARGE_OBJECT = 900000;
	private int lengthOfTask;
	private int current = 0;
	private int overall = 0;
	private boolean done = false;
	private String statMessage;
	static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy'/'MM'/'dd 'at' HH:mm:ss");

	static SimpleDateFormat formatterSolaris = new SimpleDateFormat("yyyy'-'MM'-'dd HH:mm:ss");
	static final String homeDir = "1CIJAVAHOMEDIR Java Home Dir:";
	static final String dllDir = "1CIJAVADLLDIR  Java DLL Dir:";
	static final String sysCP = "1CISYSCP       Sys Classpath:";
	static final String userArgs0 = "1CIUSERARGS";
	static final String userArgs = "2CIUSERARG";
	static final String osLevel = "2XHOSLEVEL     OS Level         : ";
	static final String cpuArchitecture = "3XHCPUARCH       Architecture   : ";
	static final String numberOfCPU = "3XHNUMCPUS       How Many       : ";
	static final String xmxcl = "NOTE: Only for Java 5.0 Service Refresh 4 (build date:February 1st, 2007) and older. When you use delegated class loaders, the JVM can create a large number of ClassLoader objects. On IBM Java 5.0 Service Refresh 4 and older, the number of class loaders that are permitted is limited to 8192 by default and an OutOfMemoryError exception is thrown when this limit is exceeded. Use the -Xmxcl parameter to increase the number of class loaders allowed to avoid this problem, for example to 25000, by setting -Xmxcl25000, until the problem is resolved.<BR><BR>Please examine the current thread stack trace to check whether a class loader is being loaded if there is an OutOfMemoryError. For example, the following stack trace indicates that a class loader is being loaded:<BR><BR>at com/ibm/oti/vm/VM.initializeClassLoader(Native Method)<BR>at java/lang/ClassLoader.<init>(ClassLoader.java:120)";
	static final String[] symptom1 = { "java/lang/OutOfMemoryError", "-Xmxcl",
			"com/ibm/oti/vm/VM.initializeClassLoader" };
	static final String signatureGCHistoryHeader = "1STGCHTYPE";
	static final String signatureGCHistory = "3STHSTTYPE";
	static final String memorySignature = "1STSEGTYPE     Internal Memory";
	static final String memorySignature626 = "1STHEAPTYPE    Object Memory";
	static final String memoryOMSignature = "1STSEGTYPE     Object Memory";
	static final String memoryCMSignature = "1STSEGTYPE     Class Memory";
	static final String memoryJCSignature = "1STSEGTYPE     JIT Code Cache";
	static final String memoryJDSignature = "1STSEGTYPE     JIT Data Cache";
	static final String memorySegmentSignature = "1STSEGMENT";
	static final String memorySegmentSignature626 = "1STHEAP";
	static final String IBMJ9 = "IBM J9";
	static final int LOW_FREE_HEAP = 15;
	static final String kalSSL5 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String kalSSL05 = "at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String kal5 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String kal4 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String kal05 = "at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String kal04 = "at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String idle5 = "at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.BoundedBuffer.take<BR>at com.ibm.ws.util.ThreadPool.getTask<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String idle4 = "at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.CachedThread.waitForRunner<BR>at com.ibm.ws.util.CachedThread.run";
	static final String rr4 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String rr5 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String rrSSL4 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String rrSSL5 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String rr04 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String rr05 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String rrSSL04 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run";
	static final String rrSSL05 = "at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run";
	static final String listen = "at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run";
	static final String listenSSL = "at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.br.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run";
	static final String listen2SSL = "at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bc.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run";
	static final String listen3SSL = "at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bu.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run";
	static final String signatureAllThread = "1XMTHDINFO";
	static final String signatureFileName = "1TIFILENAME";
	static final String signatureMore = "4XEMORENOTSHOWN";
	static final String signatureClass = "3CLTEXTCLASS";
	static final String signatureClassLoader = "2CLTEXTCLLOAD  \t\tLoader ";
	static final String signatureCurrentHeapBase = "1STCURHBASE";
	static final String signatureCurrentHeapLimit = "1STCURHLIM";
	static final String signatureHeapLockWaiting = "3LKWAITERQ            Waiting to enter:";
	static final String signatureHeapLock = "2LKREGMON          Heap lock";
	static final String signatureSolarisWait = "- waiting to lock <";
	static final String signatureSolarisLock = "- locked <";
	static final String signatureSolarisWaitAfter = "- waiting to lock [";
	static final String signatureSolarisLockAfter = "- locked [";
	static final String signatureWaiting = "  waiting to lock monitor 0x";
	static final String signatureHeld = "  which is held by \"";
	static final String signatureLocked = "  which is locked by \"";
	static final String signalSignature = "1TISIGINFO";
	static final String waiterqSignature = "3LKWAITERQ";
	static final String waiterSignature = "3LKWAITER";
	static final String freeSignature = "1STHEAPFREE";
	static final String currentThreadSignature = "1XMCURTHDINFO";
	static final String allocatedSignature = "1STHEAPALLOC";
	static final String gcCounterSignature = "1STGCCTR";
	static final String afCounterSignature = "1STAFCTR";
	static final String timeStampSignature = "1TIDATETIME";
	static final String javaVersionSignature = "1CIJAVAVERSION";
	static final String vmVersionSignature = "1CIVMVERSION";
	static final String jitVersionSignature = "1CIJITVERSION";
	static final String gcVersionSignature = "1CIGCVERSION";
	static final String commandLineSignature = "1CICMDLINE";
	static final String javacoreSignature = "NULL           ------------------------------------------------------------------------";
	static final String javacoreSignature2 = "NULL            ---------------------------------------------------------------";
	static final String javacoreSignature3 = "0SECTION       TITLE subcomponent dump routine";
	static final String nullSignature = "NULL";
	static final String sys_threadSignature = ", sys_thread_t:";
	static final String sys_threadSignature60 = ", j9thread_t:";
	static final String threadHeaderAll = "3XMTHREADINFO";
	static final String threadHeaderAlone = "3XMTHREADINFO ";
	static final String threadHeader1 = "3XMTHREADINFO1";
	static final String threadHeader2 = "3XMTHREADINFO2";
	static final String threadHeaderAnonymousNativeThread = "Anonymous native thread";
	static final String stackTraceHeader = "4XESTACKTRACE";
	static final String stackTraceHeaderSolaris = "\tat ";
	static final String stackTraceHeaderHPwaiting1 = "\t- waiting on <";
	static final String stackTraceHeaderHPwaiting2 = "\t- waiting to lock <";
	static final String stackTraceHeaderHPlocked = "\t- locked <";
	static final String stackLineHeader = "3XHSTACKLINE";
	static final String stackLineHeader2 = "3HPSTACKLINE";
	static final String stackLineHeaderIBM6 = "4XENATIVESTACK";
	static final String threadDumpSignature = "Full thread dump";
	static final String threadSignature = " prio=";
	static final String threadIDSignature = "\" (TID:";
	static final String threadIDSignature60Old = "\" TID:";
	static final String threadIDSignature60New = "\" J9VMThread:";
	static final String threadIDSignatureSolaris = " tid=";
	static final String lwp_id = "lwp_id=";
	static final String threadStateSignature = ", state:";
	static final String threadNativeIDSignature = ", native ID:";
	static final String threadNativeIDSignatureIBM6 = "(native thread ID:";
	static final String threadNativeIDSignatureSolaris = " nid=";
	static final String threadIDMonSignature = "2LKFLATMON         ident 0x";
	static final String threadIDMonIDSignature = "\" (0x";
	static final String threadIDMonIDSignature2 = "\" (";
	static final String monSignature = "3LKMONOBJECT";
	static final String monSignatureSystem = "2LKREGMON";
	static final String monSignature2 = "thread ident 0x";
	static final String monSignatureJ9 = "Flat locked by \"";
	static final String notifySignature0 = "3LKNOTIFYQ";
	static final String notifySignature = "3LKWAITNOTIFY";
	static final String deadlockSignatureSolaris131 = "FOUND A JAVA LEVEL DEADLOCK:";
	static final String deadlockSignatureSolaris142 = "Found one Java-level deadlock:";
	static final String deadlockSignature = "1LKDEADLOCK";
	static final String deadlockThreadSignature = "2LKDEADLOCKTHR  Thread \"";
	static final String errorSignature = "2LKERROR";
	static final String signatureClassloader = "2CLTEXTCLLOADER";
	static final String signatureNumberOfLoaded = "3CLNMBRLOADEDCL";
	static final String signatureLoader = "Loader ";
	static final String signatureParent = ", Parent ";
	static final String signatureNumberOfClasses = "Number of loaded classes";
	public Analyzer ha = null;
	public File[] file = null;
	private JDialogProgress jp = null;
	GCInfo gi = null;
	boolean isJavacore = false;

	public FileTask(Analyzer h, File[] f) {
		this.file = f;
		this.ha = h;
		this.lengthOfTask = 1000;
	}

	public FileTask(Analyzer h, File[] f, int fileSequenceNumber, String outputHTMLFileName, Configuration cfg) {
		this.file = f;
		this.ha = h;
		this.cfg = cfg;
		this.outputHTMLFileName = outputHTMLFileName;
		this.lengthOfTask = 1000;
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public FileTask(Analyzer h, File[] f, JDialogProgress j) {
		this.file = f;
		this.ha = h;
		this.jp = j;
		this.lengthOfTask = 100;
	}

	public FileTask(Analyzer h, File[] f, JDialogProgress j, Configuration c) {
		this.file = f;
		this.ha = h;
		this.jp = j;
		this.cfg = c;
		this.lengthOfTask = 100;
	}

	public void checkIdle(String st) {
		if ((st == null) || (st.length() == 0))
			return;
		String temp = "";
		String str = st;
		int s = 0;
		while (true) {
			int p = str.indexOf("(");
			if (p == -1) {
				break;
			}

			temp = temp + str.substring(s, p);
			str = str.substring(p + 1);

			int n = str.indexOf("<BR>");
			if (n == -1) {
				break;
			}

			s = n;
			str = str.substring(s);
			s = 0;
		}
		if ((temp.compareTo(
				"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.BoundedBuffer.take<BR>at com.ibm.ws.util.ThreadPool.getTask<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.CachedThread.waitForRunner<BR>at com.ibm.ws.util.CachedThread.run") == 0))
			System.out.println("IDLE");
	}

	public void checkIdle(String st, int index) {
		if ((st == null) || (st.length() == 0))
			return;
		String temp = "";
		String str = st;
		int s = 0;
		while (true) {
			int p = str.indexOf("(");
			if (p == -1) {
				break;
			}

			temp = temp + str.substring(s, p);
			str = str.substring(p + 1);

			int n = str.indexOf("<BR>");
			if (n == -1) {
				break;
			}

			s = n;
			str = str.substring(s);
			s = 0;
		}
		if ((temp.compareTo(
				"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.BoundedBuffer.take<BR>at com.ibm.ws.util.ThreadPool.getTask<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.CachedThread.waitForRunner<BR>at com.ibm.ws.util.CachedThread.run") == 0))
			System.out.println("IDLE");
	}

	public void checkIdle(String st, int index, ThreadDump td) {
		if ((st == null) || (st.length() == 0))
			return;
		String temp = "";
		String str = st;
		int s = 0;
		while (true) {
			int p = str.indexOf("(");
			if (p == -1) {
				break;
			}

			int lock = str.substring(s, p).indexOf("- lock");
			if (lock == -1)
				lock = str.substring(s, p).indexOf("- wait");

			if (lock != -1) {
				str = str.substring(p + 1);
			} else {
				temp = temp + str.substring(s, p);
				str = str.substring(p + 1);
			}

			int n = str.indexOf("<BR>");
			if (n == -1) {
				break;
			}

			s = n;
			str = str.substring(s);
			s = 0;
		}
		if ((temp.compareTo(
				"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.BoundedBuffer.take<BR>at com.ibm.ws.util.ThreadPool.getTask<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.CachedThread.waitForRunner<BR>at com.ibm.ws.util.CachedThread.run") == 0))
			td.macro[index] = 1;
		else if ((temp.compareTo(
				"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0))
			td.macro[index] = 2;
		else if ((temp.compareTo(
				"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.br.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bc.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bu.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0))
			td.macro[index] = 3;
		else if ((temp.compareTo(
				"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0))
			td.macro[index] = 4;

		if (this.ha == null)
			return;

		if (this.ha.keywords == null)
			return;

		String all = this.ha.keywords.getProperty("jca.keyword.list");

		if ((all == null) || (all.length() == 0))
			return;

		StringTokenizer token = new StringTokenizer(all, " ");

		while (token.hasMoreTokens()) {
			String key = token.nextToken();
			if (key != null) {
				String stack = this.ha.keywords.getProperty(key + ".stack");
				if (stack != null) {
					if (stack.length() == 0)
						continue;

					while ((stack.charAt(stack.length() - 1) == '\n') && (stack.length() > 1)) {
						stack = stack.substring(0, stack.length() - 1);
					}

					stack = stack.replaceAll("\n", "<BR>");

					if (temp.compareTo(stack) == 0) {
						td.pattern[index] = this.ha.keywords.getProperty(key + ".name");
					}
				}
			}
		}
	}

	public void checkIdle2(String st, int index, ThreadDump td) {
		if ((st == null) || (st.length() == 0))
			return;
		String temp = "";
		String str = st;
		int s = 0;
		while (true) {
			int p = str.indexOf("(");
			if (p == -1) {
				break;
			}

			int lock = str.substring(s, p).indexOf("- lock");
			if (lock == -1)
				lock = str.substring(s, p).indexOf("- wait");

			if (lock != -1) {
				str = str.substring(p + 1);
			} else {
				temp = temp + str.substring(s, p);
				str = str.substring(p + 1);
			}

			int n = str.indexOf("<BR>");
			if (n == -1) {
				break;
			}

			s = n;
			str = str.substring(s);
			s = 0;
		}
		if ((temp.compareTo(
				"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.BoundedBuffer.take<BR>at com.ibm.ws.util.ThreadPool.getTask<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.lang.Object.wait<BR>at java.lang.Object.wait<BR>at com.ibm.ws.util.CachedThread.waitForRunner<BR>at com.ibm.ws.util.CachedThread.run") == 0))
			td.macro[index] = 1;
		else if ((temp.compareTo(
				"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead0<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0))
			td.macro[index] = 2;
		else if ((temp.compareTo(
				"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.br.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bc.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0)
				|| (temp.compareTo(
						"at java.net.PlainSocketImpl.socketAccept<BR>at java.net.PlainSocketImpl.accept<BR>at java.net.ServerSocket.implAccept<BR>at java.net.ServerSocket.accept<BR>at com.ibm.jsse.bu.accept<BR>at com.ibm.ws.http.HttpTransport.run<BR>at java.lang.Thread.run") == 0))
			td.macro[index] = 3;
		else if ((temp.compareTo(
				"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.CachedThread.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0)
				|| (temp.compareTo(
						"at java.net.SocketInputStream.socketRead<BR>at java.net.SocketInputStream.read<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.s.b<BR>at com.ibm.sslite.s.a<BR>at com.ibm.sslite.a.read<BR>at com.ibm.jsse.a.read<BR>at com.ibm.ws.io.Stream.read<BR>at com.ibm.ws.io.ReadStream.readBuffer<BR>at com.ibm.ws.io.ReadStream.read(ReadStream.java<BR>at com.ibm.ws.http.HttpRequest.readRequestLine<BR>at com.ibm.ws.http.HttpRequest.readRequest<BR>at com.ibm.ws.http.HttpConnection.readAndHandleRequest<BR>at com.ibm.ws.http.HttpConnection.run<BR>at com.ibm.ws.util.ThreadPool$Worker.run") == 0))
			td.macro[index] = 4;
	}

	public String generateMonitorDetail(int sn, ThreadDump td) {
		if ((td == null) || (td.mdump == null))
			return null;
		MonitorModel m = new MonitorModel(td);

		String id = "_" + sn;
		String stackArray = "var stacks" + id + " = new Array()\n";
		for (int i = 0; i < td.getTotalThread(); i++) {
			stackArray = stackArray + "stacks" + id + "[" + i + "]=\"" + td.getStackTrace(i) + "\"\n";
		}

		String highLight = "function highLight" + id
				+ "(objref,state,row)\n{\nobjref.style.color = (0 == state) ? '#000000' : '#0000FF'; \n showStackTrace(stacks"
				+ id + "[row]) \n if(state==0) hideStackTrace() \n }\n";
		String tree = "var openImg = new Image(); \n openImg.src = \"open.gif\"; \n var closeImg = new Image(); \n closeImg.src = \"close.gif\"; \n function changeTree(disp,img) \n { \n var obj = document.getElementById(disp).style; \n if(obj.display == \"block\") \n  obj.display = \"none\"; \n else \n  obj.display = \"block\"; \t\n var Image = document.getElementById(img); \n if(Image.src.indexOf('deadlock.gif')>-1) ; \n else if(Image.src.indexOf('close.gif')>-1) \n  Image.src = openImg.src; \n else \n  Image.src = closeImg.src; \n } ";
		String javaScript = "\n<script type=\"text/javascript\">\n" + stackArray + "\nfunction showMonStack" + id
				+ "(rowNumber)   { \ndocument.getElementById(\"monStackColumn" + id + "\").innerHTML=stacks" + id
				+ "[rowNumber];\n }\n" + highLight + " " + tree + " </script>\n";

		String str = "<UL><LI>Monitor Detail : " + td.fileName
				+ "<BR><BR><table border=\"1\"><col width=50%></col><col width=50%></col><caption align=\"bottom\">"
				+ "Monitor Detail : " + td.fileName
				+ "</caption><tr><th>Monitor Owner</th><th>Stack Trace</th></tr><tr><td valign=\"top\"><div class=\"parent\" onClick=\"changeTree('branch"
				+ id + "_" + "1','folder" + id + "_" + "1')\"> \n \t<img src=\"close.gif\" id=\"folder" + id + "_"
				+ "1\">[TotalSize/Size] ThreadName (ObjectName) " + m.getChildCount(m.getRoot()) + "</div>";
		str = str + "<span class=\"children\" id=\"branch" + id + "_" + "1\">";

		int seq = 2;

		Monitor aNode = null;
		Stack<Object> stack = new Stack<Object>();
		for (int i = 0; i < m.getChildCount(m.getRoot()); i++) {
			stack.push(m.getChild(m.getRoot(), i));
		}
		int n = -1;
		while (!stack.empty()) {
			aNode = (Monitor) stack.pop();
			n = m.getThreadDumpIndex(aNode);
			if (aNode.isPop()) {
				str = str + "</span>\n";
			} else if (m.isRecursive(aNode)) {
				if (aNode.isDeadlock) {
					if (n >= 0)
						str = str + "<img src=\"deadlock.gif\"><a  onmouseover=\"highLight" + id + "(this,1," + n
								+ ")\" onmouseout=\"highLight" + id + "(this,0)\"  onclick=\"showMonStack" + id + "("
								+ n + ")\"> " + getTreeEntry(m, aNode) + "</a><br>\n";
					else {
						str = str + "<img src=\"deadlock.gif\"> " + getTreeEntry(m, aNode) + "<br>\n";
					}

				} else if (n >= 0)
					str = str + "<img src=\"leaf.gif\"><a  onmouseover=\"highLight" + id + "(this,1," + n
							+ ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showMonStack" + id + "(" + n
							+ ") ; showMonStack" + id + "(" + n + ")\" > " + getTreeEntry(m, aNode) + "</a><br>\n";
				else
					str = str + "<img src=\"leaf.gif\"><a onclick=\"showMonStack" + id + "(" + n + ")\" >"
							+ getTreeEntry(m, aNode) + "</a><br>\n";
			} else {
				if (m.getChildCount(aNode) == 0) {
					if (n >= 0)
						str = str + "<img src=\"leaf.gif\"><a  onmouseover=\"highLight" + id + "(this,1," + n
								+ ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showMonStack" + id + "(" + n
								+ ")\" > " + getTreeEntry(m, aNode) + "</a><br>\n";
					else
						str = str + "<img src=\"leaf.gif\"><a onclick=\"showMonStack" + id + "(" + n + ")\" >"
								+ getTreeEntry(m, aNode) + "</a><br>\n";
				} else {
					if (aNode.isDeadlock) {
						if (n >= 0)
							str = str + "<span class=\"parent\" onClick=\"changeTree('branch" + id + "_" + seq
									+ "','folder" + id + "_" + seq + "')\">\n\t<img src=\"deadlock.gif\" id=\"folder"
									+ id + "_" + seq + "\"><a  onmouseover=\"highLight" + id + "(this,1," + n
									+ ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showMonStack" + id + "("
									+ n + ")\" > " + getTreeEntry(m, aNode)
									+ "</a><br>\n </span> \n <span class=\"children\" id=\"branch" + id + "_" + seq
									+ "\"> \n";
						else {
							str = str + "<span class=\"parent\" onClick=\"changeTree('branch" + id + "_" + seq
									+ "','folder" + id + "_" + seq + "') ; showMonStack" + id + "(" + n
									+ ")\" >\n\t<img src=\"deadlock.gif\" id=\"folder" + id + "_" + seq + "\"> "
									+ getTreeEntry(m, aNode) + "<br>\n </span> \n <span class=\"children\" id=\"branch"
									+ id + "_" + seq + "\"> \n";
						}
					} else if (n >= 0)
						str = str + "<span class=\"parent\" onClick=\"changeTree('branch" + id + "_" + seq + "','folder"
								+ id + "_" + seq + "') ; showMonStack" + id + "(" + n
								+ ")\" >\n\t<img src=\"close.gif\" id=\"folder" + id + "_" + seq
								+ "\"><a  onmouseover=\"highLight" + id + "(this,1," + n + ")\" onmouseout=\"highLight"
								+ id + "(this,0)\" >  " + getTreeEntry(m, aNode)
								+ "</a><br>\n </span> \n <span class=\"children\" id=\"branch" + id + "_" + seq
								+ "\"> \n";
					else {
						str = str + "<span class=\"parent\" onClick=\"changeTree('branch" + id + "_" + seq + "','folder"
								+ id + "_" + seq + "') ; showMonStack" + id + "(" + n
								+ ")\" >\n\t<img src=\"close.gif\" id=\"folder" + id + "_" + seq + "\"> "
								+ getTreeEntry(m, aNode) + "<br>\n </span> \n <span class=\"children\" id=\"branch" + id
								+ "_" + seq + "\"> \n";
					}
					seq++;
					aNode.isPop = true;
					stack.push(aNode);
				}

				if (m.getChildCount(aNode) > 0) {
					for (int i = 0; i < m.getChildCount(aNode); i++) {
						Monitor cNode = (Monitor) m.getChild(aNode, i);
						if (!cNode.visited) {
							cNode.visited = true;
							stack.push(cNode);
						}
					}
				}
			}

		}

		str = str + "</span></td><td id=\"monStackColumn" + id
				+ "\" valign=\"top\" >Click on entries to display stack traces</td></tr></table>";

		return javaScript + str + "</UL>";
	}

	public String generateReport(ThreadInfo ti) {
		StringBuffer output = new StringBuffer();

		output.append("<UL>");
		for (int i = 0; i < ti.threadDumps.size(); i++) {
			output.append("<li><a href=\"#T" + i + "\">Java Thread Dump Individual Analysis : "
					+ ((ThreadDump) ti.threadDumps.get(i)).fileName + "</a><br>");
			output.append("<ul><li><a href=\"#TS" + i + "\">Thread Status Analysis</a><br>");
			output.append("<li><a href=\"#TM" + i + "\">Thread Method Analysis</a><br>");
			output.append("<li><a href=\"#TD" + i + "\">Thread Detail  Analysis</a><br><br></ul>");
			output.append("<li><a href=\"#M" + i + "\">Java Monitor Dump Individual Analysis : "
					+ ((ThreadDump) ti.threadDumps.get(i)).fileName + "</a><br><br>");
		}
		if (ti.threadDumps.size() > 1) {
			output.append("<li><a href=\"#C\">Java Thread Dump Comparison Analysis</a>");
			output.append("<UL><li><a href=\"#CS\">Thread Comparison Summary</a>");
			output.append("<li><a href=\"#CD\">Thread Comparison Detail</a></UL>");
			output.append("</UL><br><br>");
		} else {
			output.append("<li>Java Thread Dump Comparison Analysis : Not available for a single thread dump");
			output.append("</UL><br><br>");
		}
		String temp = null;
		for (int i = 0; i < ti.threadDumps.size(); i++) {
			output.append("<H2><a name=\"T" + i + "\">Java Thread Dump Individual Analysis</a></H2>");
			output.append("<UL>");
			output.append(((ThreadDump) ti.threadDumps.get(i)).warning);
			output.append(((ThreadDump) ti.threadDumps.get(i)).summary);
			output.append("</UL>");

			output.append("<BR>");
			output.append(generateThreadSummary(i, (ThreadDump) ti.threadDumps.get(i)));
			output.append("<p><a href=\"#top\">Back to top</a></p>");
			output.append("<BR>");
			output.append(generateThreadDetail(i, (ThreadDump) ti.threadDumps.get(i)));
			output.append("<p><a href=\"#top\">Back to top</a></p>");
			output.append("<BR>");
			output.append("<H2><a name=\"M" + i + "\">Java Monitor Dump Individual Analysis</a></H2>");
			temp = generateMonitorDetail(i, (ThreadDump) ti.threadDumps.get(i));
			if (temp == null)
				output.append("<BR>Monitor dump is not available in " + ((ThreadDump) ti.threadDumps.get(i)).fileName);
			else
				output.append(temp);
			output.append("<BR>");
			output.append("<p><a href=\"#top\">Back to top</a></p>");
		}

		if (ti.threadDumps.size() > 1) {
			output.append("<H2><a name=\"C\">Java Thread Dump Comparison Analysis</a></H2>");
			output.append(generateThreadComparison(ti));
			output.append("<p><a href=\"#top\">Back to top</a></p>");
		}
		return output.toString();
	}

	public String generateThreadComparison(ThreadInfo ti) {
		if (ti == null)
			return "";
		if (ti.threadDumps == null)
			return "";
		if (ti.threadDumps.size() < 2)
			return "";
		String id = "comp";

		ThreadDump[] td = new ThreadDump[ti.threadDumps.size()];
		for (int i = 0; i < ti.threadDumps.size(); i++) {
			td[i] = ((ThreadDump) ti.threadDumps.get(i));
		}
		CompareTableModel model = new CompareTableModel(td);

		String stackArray = "var stacks" + id + " = [";
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 1; j < model.getColumnCount(); j++) {
				if (j == 1) {
					stackArray = stackArray + "[\"" + model.getStack(i, j - 1) + "\"";
				} else {
					stackArray = stackArray + ",\"" + model.getStack(i, j - 1) + "\"";
				}
			}
			stackArray = stackArray + "],\n";
		}
		stackArray = stackArray + "];\n";
		String highLight = "function highLight" + id
				+ "(objref,state,row,col)\n{\nobjref.style.color = (0 == state) ? '#000000' : '#0000FF';\n showStackTrace(stackscomp[row][col]); \n  if(state==0) hideStackTrace();  }\n";
		String javaScript = "\n<script type=\"text/javascript\">\n" + stackArray + "\nfunction showStack" + id
				+ "(rowNumber,columnNumber)   { \ndocument.getElementById(\"stackColumn" + id + "\").innerHTML=stacks"
				+ id + "[rowNumber][columnNumber];\n }\n" + highLight + " </script>\n";
		String str = "<UL><LI><a name=\"CS\">Thread Comparison Summary</a><BR><BR>"
				+ generateThreadComparisonSummary(td) + hangSummary(model) + "</UL><BR><BR>"
				+ "<LI><a name=\"CD\">Thread Comparison Detail</a><BR><BR><table border=\"1\"><col width=20%></col><col width=20%></col><col width=20%></col><col width=40%></col><caption align=\"bottom\">Thread Comparison Detail</caption><tr><th>Thread Name</th>";
		for (int i = 0; i < td.length; i++) {
			str = str + "<th>" + td[i].fileName + "</th>";
		}
		str = str + "<th>Stack Trace</th></tr>";

		for (int i = 0; i < model.getRowCount(); i++) {
			str = str + "<tr><td>" + model.getValueAt(i, 0) + "</font></a></td>";
			for (int j = 0; j < td.length; j++) {
				str = str + "<td " + getStateBGColor(model, i, j + 1)
						+ "><a  style=\"cursor:hand\"  onmouseover=\"highLight" + id + "(this,1," + i + "," + j
						+ ")\" onmouseout=\"highLight" + id + "(this,0," + i + "," + j + ")\" onclick=\"showStack" + id
						+ "(" + i + "," + j + ")\"><font " + getStateColor(model, i, j + 1) + ">"
						+ (model.getValueAt(i, j + 1) == null ? "NO THREAD" : model.getValueAt(i, j + 1))
						+ "</font></a></td>";
			}
			if (i == 0) {
				str = str + "<td\t id=\"stackColumn" + id + "\" rowspan=\"" + model.getRowCount()
						+ "\" valign=\"top\" >Click on threads to display stack traces</td></tr>";
			} else
				str = str + "</tr>";
		}

		return javaScript + str + "</table></UL>";
	}

	public String generateThreadComparisonSummary(ThreadDump[] td) {
		String compareSummary = "<UL>";
		if (td[0].pid != -1L) {
			if (!isSamePID(td))
				compareSummary = compareSummary
						+ "<LI>WARNING!! Thread dumps are taken from different processes.Further analysis is not meaningful<BR><BR>";
			else {
				compareSummary = compareSummary + "<LI>Process ID : " + td[0].pid + "<BR><BR>";
			}

		}

		compareSummary = compareSummary + "<LI>List of files compared<UL><BR>";
		for (int i = 0; i < td.length; i++) {
			compareSummary = compareSummary + "<LI>" + td[i].fileName + "<BR>";
		}
		compareSummary = compareSummary + "</UL><BR>";
		long endTime;
		long startTime = endTime = td[0].timeStamp;

		if (startTime != -1L) {
			long endGC;
			long startGC = endGC = td[0].gc;
			long endAF;
			long startAF = endAF = td[0].af;
			for (int i = 0; i < td.length; i++) {
				if (startTime > td[i].timeStamp) {
					startTime = td[i].timeStamp;
					startGC = td[i].gc;
					startAF = td[i].af;
				}
				if (endTime < td[i].timeStamp) {
					endTime = td[i].timeStamp;
					endGC = td[i].gc;
					endAF = td[i].af;
				}
			}
			float min = (float) (endTime - startTime) / 60000.0F;

			compareSummary = compareSummary + "<LI>First Dump : " + new Date(startTime) + "<BR><BR><LI>Last Dump : "
					+ new Date(endTime) + "<BR><BR>";

			if (startGC != -1L) {
				if (min != 0.0F) {
					if (td[0].isJ9)
						compareSummary = compareSummary + "<LI>Global Collections per Minute : "
								+ (float) (endGC - startGC) / min + "<BR><BR><LI>Scavenge Collections per Minute : "
								+ (float) (endAF - startAF) / min + "<BR><BR>";
					else {
						compareSummary = compareSummary + "<LI>Garbage Collections per Minute : "
								+ (float) (endGC - startGC) / min + "<BR><BR><LI>Allocation Failures per Minute : "
								+ (float) (endAF - startAF) / min + "<BR><BR>";
					}
				}
				long t = (endTime - startTime) / 1000L;

				long s = t % 60L;
				long m = t / 60L % 60L;
				long h = t / 60L / 60L % 24L;
				long d = t / 60L / 60L / 24L;

				if ((t != 0L) && (s + m + h + d != 0L))
					compareSummary = compareSummary + "<LI>Elapsed Time : "
							+ (d == 0L ? "" : new StringBuilder(String.valueOf(d)).append(" Day(s) ").toString())
							+ (h == 0L ? "" : new StringBuilder(String.valueOf(h)).append(" Hour(s) ").toString())
							+ (m == 0L ? "" : new StringBuilder(String.valueOf(m)).append(" Minute(s) ").toString())
							+ (s == 0L ? ""
									: new StringBuilder(String.valueOf(s)).append(" Second(s)").append("<BR><BR>")
											.toString());
			}
		}
		return compareSummary;
	}

	public String generateThreadDetail(int sn, ThreadDump td) {
		String id = "_" + sn;
		String stackArray = "var stacks" + id + " = new Array()\n";
		for (int i = 0; i < td.getTotalThread(); i++) {
			stackArray = stackArray + "stacks" + id + "[" + i + "]=\"" + td.getStackTrace(i) + "\"\n";
		}

		String highLight = "function highLight" + id
				+ "(objref,state,row)\n{\nobjref.style.color = (0 == state) ? '#000000' : '#0000FF'; \n showStackTrace(stacks"
				+ id + "[row]) \n if(state==0) hideStackTrace() \n }\n";
		String javaScript = "\n<script type=\"text/javascript\">\n" + stackArray + "\nfunction showStack" + id
				+ "(rowNumber)   { \ndocument.getElementById(\"stackColumn" + id + "\").innerHTML=stacks" + id
				+ "[rowNumber];\n }\n" + highLight + " </script>\n";

		String str = "<UL><LI><a name=\"TD" + sn + "\">Thread Detail : " + td.fileName
				+ "</a><BR><BR><table border=\"1\"><col width=20%></col><col width=20%></col><col width=30%></col><col width=30%></col><caption align=\"bottom\">"
				+ "Thread Detail : " + td.fileName
				+ "</caption><tr><th>Name</th><th>State</th><th>Method</th><th>Stack Trace</th></tr>";

		str = str + "<tr><td><a style=\"cursor:hand\" onmouseover=\"highLight" + id
				+ "(this,1,0)\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showStack" + id + "(0)\">"
				+ td.getName(0) + "</a></td><td " + getStateColor(td, 0)
				+ "<a style=\"cursor:hand\" onmouseover=\"highLight" + id + "(this,1,0)\" onmouseout=\"highLight" + id
				+ "(this,0)\" onclick=\"showStack" + id + "(0)\">" + td.getState(0)
				+ "</a></td><td><a style=\"cursor:hand\" onmouseover=\"highLight" + id
				+ "(this,1,0)\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showStack" + id + "(0)\">"
				+ td.getCurrentMethod(0) + "</a></td><td\t id=\"stackColumn" + id + "\" rowspan=\""
				+ td.getTotalThread() + "\" valign=\"top\" >Click on threads to display stack traces</td></tr>";
		for (int i = 1; i < td.getTotalThread(); i++) {
			str = str + "<tr><td><a style=\"cursor:hand\"  onmouseover=\"highLight" + id + "(this,1," + i
					+ ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showStack" + id + "(" + i + ")\">"
					+ td.getName(i) + "</a></td><td " + getStateColor(td, i)
					+ " ><a  style=\"cursor:hand\"  onmouseover=\"highLight" + id + "(this,1," + i
					+ ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showStack" + id + "(" + i + ")\">"
					+ td.getState(i) + "</a></td><td><a style=\"cursor:hand\"  onmouseover=\"highLight" + id
					+ "(this,1," + i + ")\" onmouseout=\"highLight" + id + "(this,0)\" onclick=\"showStack" + id + "("
					+ i + ")\">" + td.getCurrentMethod(i) + "</a></td></tr>";
		}

		return javaScript + str + "</table></UL>";
	}

	public String generateThreadSummary(int sn, ThreadDump td) {
		if (td != null) {
			long total = td.getTotalThread();
			long runnable = td.getRunnable();
			long condition = td.getWCondition();
			long monitor = td.getWMonitor();
			long suspended = td.getSuspended();
			long wait = td.getOWait();
			long blocked = td.getBlocked();
			long parked = td.getParked();
			String summary = "<UL><LI><a name=\"TS" + sn
					+ "\">Thread Status Analysis</a><BR><BR><table border=\"1\"><tr><th>Status</th><th>Number of Threads : "
					+ total + "</th><th>Percentage</th></tr><tr><td bgcolor=\"#" + getHTMLColor(this.cfg.runnable)
					+ "\">Runnable</td><td>" + runnable + "</td><td>"
					+ Math.round((float) runnable * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.condition) + "\">Waiting on condition</td><td>" + condition + "</td><td>"
					+ Math.round((float) condition * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.monitor) + "\">Waiting on monitor</td><td>" + monitor + "</td><td>"
					+ Math.round((float) monitor * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.suspended) + "\">Suspended</td><td>" + suspended + "</td><td>"
					+ Math.round((float) suspended * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.object) + "\">Object.wait()</td><td>" + wait + "</td><td>"
					+ Math.round((float) wait * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.blocked) + "\">Blocked</td><td>" + blocked + "</td><td>"
					+ Math.round((float) blocked * 100.0F / (float) total) + " (%)</td></tr><tr><td bgcolor=\"#"
					+ getHTMLColor(this.cfg.park) + "\">Parked</td><td>" + parked + "</td><td>"
					+ Math.round((float) parked * 100.0F / (float) total) + " (%)</td></tr></table></UL>";
			return summary + "<br>" + td.getMethodSummary(sn);
		}
		return "";
	}

	public String getAddress(String str) {
		if ((str == null) || (str.length() == 0))
			return null;
		int open = str.indexOf("(");
		int close = str.indexOf(")");
		if ((open == -1) || (close == -1))
			return null;

		long value = Long.decode(str.substring(open + 1, close)).longValue();
		System.out.println("value:" + value);

		return str.substring(open + 1, close);
	}

	long getCompleted(int idx) {
		if (this.gi.completed[idx] != 0L)
			return this.gi.completed[idx];
		if (this.gi.gccompleted[idx] == 0L)
			return 0L;
		for (int i = idx; i < this.gi.free.length; i++) {
			if (this.gi.completed[i] != 0L)
				return this.gi.completed[i];
		}
		return 0L;
	}

	public int getCurrent() {
		return this.current;
	}

	public String getHTMLColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		String R;
		if (r <= 15)
			R = "0" + Integer.toHexString(r);
		else
			R = Integer.toHexString(r);
		String G;
		if (g <= 15)
			G = "0" + Integer.toHexString(g);
		else
			G = Integer.toHexString(g);
		String B;
		if (b <= 15)
			B = "0" + Integer.toHexString(b);
		else
			B = Integer.toHexString(b);
		return R + G + B;
	}

	public int getLengthOfTask() {
		return this.lengthOfTask;
	}

	public String getMessage() {
		return this.statMessage;
	}

	public String getName(String str) {
		if ((str == null) || (str.length() == 0))
			return null;
		int open = str.indexOf("(");
		if (open == -1)
			return null;
		return str.substring(0, open);
	}

	public int getNumberOfChar(String str, char c) {
		int count = 0;
		int newIndex = 0;
		while ((newIndex = str.indexOf(c)) >= 0) {
			count++;
			if (newIndex >= str.length())
				break;
			str = str.substring(newIndex + 1);
		}

		return count;
	}

	public int getOverall() {
		return this.overall;
	}

	private long getPid(String str) {
		int i = str.lastIndexOf("JAVADUMP");
		if (i != -1) {
			int j = str.lastIndexOf(".txt");
			if (j != -1) {
				int k = str.substring(0, j - 1).lastIndexOf(".");
				if (k != -1) {
					return Long.parseLong(str.substring(k + 1, j).trim(), 10);
				}
			}
		} else {
			i = str.lastIndexOf("javacore.");
			if (i != -1) {
				int j = str.lastIndexOf(".txt");
				if (j != -1) {
					if (getNumberOfChar(str.substring(i), '.') == 5) {
						int k = str.substring(0, j - 1).lastIndexOf(".");
						if (k != -1) {
							int l = str.substring(0, k - 1).lastIndexOf(".");
							if (l != -1) {
								return Long.parseLong(str.substring(l + 1, k), 10);
							}
							return -1L;
						}
					} else {
						int k = str.substring(0, j - 1).lastIndexOf(".");
						if (k != -1) {
							return Long.parseLong(str.substring(k + 1, j), 10);
						}
					}
				}
			} else {
				i = str.lastIndexOf("javacore");
				if (i != -1) {
					int j = str.indexOf(".", i);
					if (j != -1) {
						return Long.parseLong(str.substring(i + 8, j).trim(), 10);
					}
				}
			}
		}
		return -1L;
	}

	long getRequested(int idx) {
		if (this.gi.af[idx] == 0L) {
			for (int i = idx; i >= 0; i--)
				if (this.gi.af[i] != 0L)
					return this.gi.af[i];
		} else
			return this.gi.af[idx];
		return 0L;
	}

	long getSince(int idx) {
		if (this.gi.since[idx] != 0L)
			return this.gi.since[idx];
		for (int i = idx; i >= 0; i--) {
			if ((this.gi.since[i] != 0L) && (this.gi.af[i] != 0L))
				return this.gi.since[i];
		}
		return 0L;
	}

	public String getStateBGColor(CompareTableModel model, int row, int col) {
		if ((row < 0) || (col <= 0))
			return "";
		Long tid = (Long) model.tidHash.get(new Integer(model.sortedNames[row]));
		if (!model.hi[(col - 1)].threadHash.containsKey(tid))
			return "";
		int idx = ((Integer) model.hi[(col - 1)].threadHash.get(tid)).intValue();
		if (idx < 0)
			return "";

		if (model.hi[(col - 1)].javaStack[idx] != null) {
			if (col - 1 != 0) {
				if (model.hi[(col - 1 - 1)].threadHash.containsKey(tid)) {
					int idx2 = ((Integer) model.hi[(col - 1 - 1)].threadHash.get(tid)).intValue();
					if (model.hi[(col - 1 - 1)].javaStack[idx2] != null) {
						if (model.hi[(col - 1 - 1)].javaStack[idx2]
								.compareTo(model.hi[(col - 1)].javaStack[idx]) == 0) {
							return " bgcolor=\"#" + getHTMLColor(this.cfg.hang) + "\" ";
						}
					}
				}
			}

			if (col - 1 < model.hi.length - 1) {
				if (model.hi[(col - 1 + 1)].threadHash.containsKey(tid)) {
					int idx2 = ((Integer) model.hi[(col - 1 + 1)].threadHash.get(tid)).intValue();
					if (model.hi[(col - 1 + 1)].javaStack[idx2] != null) {
						if (model.hi[(col - 1 + 1)].javaStack[idx2]
								.compareTo(model.hi[(col - 1)].javaStack[idx]) == 0) {
							return " bgcolor=\"#" + getHTMLColor(this.cfg.hang) + "\" ";
						}
					}

				}

			}

		}

		return "";
	}

	public String getStateColor(CompareTableModel model, int row, int col) {
		if ((row < 0) || (col <= 0))
			return "";
		Long tid = (Long) model.tidHash.get(new Integer(model.sortedNames[row]));
		if (!model.hi[(col - 1)].threadHash.containsKey(tid))
			return "";
		int idx = ((Integer) model.hi[(col - 1)].threadHash.get(tid)).intValue();
		if (idx < 0)
			return "";

		if (model.hi[(col - 1)].isDeadlock[idx])
			return " color=\"#" + getHTMLColor(this.cfg.deadlock) + "\" ";
		switch (model.hi[(col - 1)].state[idx]) {
		case 0:
			return " color=\"#" + getHTMLColor(this.cfg.runnable) + "\" ";
		case 1:
			return " color=\"#" + getHTMLColor(this.cfg.condition) + "\" ";
		case 2:
			return " color=\"#" + getHTMLColor(this.cfg.monitor) + "\" ";
		case 3:
			return " color=\"#" + getHTMLColor(this.cfg.suspended) + "\" ";
		case 4:
			return " color=\"#" + getHTMLColor(this.cfg.object) + "\" ";
		case 5:
			return " color=\"#" + getHTMLColor(this.cfg.blocked) + "\" ";
		case 6:
			return " color=\"#" + getHTMLColor(this.cfg.park) + "\" ";
		}

		return "";
	}

	public String getStateColor(ThreadDump td, int idx) {
		if (td.isDeadlock[idx])
			return " bgcolor=\"#" + getHTMLColor(this.cfg.deadlock) + "\" ";
		switch (td.state[idx]) {
		case 0:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.runnable) + "\" ";
		case 1:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.condition) + "\" ";
		case 2:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.monitor) + "\" ";
		case 3:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.suspended) + "\" ";
		case 4:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.object) + "\" ";
		case 5:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.blocked) + "\" ";
		case 6:
			return " bgcolor=\"#" + getHTMLColor(this.cfg.park) + "\" ";
		}

		return "";
	}

	public String getTreeEntry(MonitorModel model, Object value) {
		int id = ((Monitor) value).owner;
		if (id == -1)
			return "[TotalSize/Size] ThreadName (ObjectName) " + nf.format(model.rootChildren.length);
		if (model.child[id] != null) {
			if (model.objectName[id] == -1) {
				return "[" + nf.format(model.total[id]) + "/" + nf.format(model.size[id]) + "] "
						+ model.getThreadName(id);
			}
			return "[" + nf.format(model.total[id]) + "/" + nf.format(model.size[id]) + "] " + model.getThreadName(id)
					+ " (" + model.objectArray[model.objectName[id]] + ")";
		}

		if (model.objectName[id] >= 0)
			return model.getThreadName(id) + " (" + model.objectArray[model.objectName[id]] + ")";

		return model.getThreadName(id);
	}

	String getTrend(int x, int y) {
		String ts = "between " + formatter.format(new Date(this.gi.timestamp[x])) + " and "
				+ formatter.format(new Date(this.gi.timestamp[y]));
		float alpha = 0.9F;
		float st_1 = (float) (this.gi.total[x] - this.gi.free[x]);
		float st1 = 0.0F;
		float st2 = 0.0F;
		float st2_1 = (float) (this.gi.total[x] - this.gi.free[x]);
		float mape = 0.0F;
		long maxUsed = 0L;
		for (int i = x + 1; i <= y; i++) {
			if (maxUsed < this.gi.total[i] - this.gi.free[i])
				maxUsed = this.gi.total[i] - this.gi.free[i];

			st1 = alpha * (float) (this.gi.total[i] - this.gi.free[i]) + (1.0F - alpha) * st_1;
			st2 = alpha * st1 + (1.0F - alpha) * st2_1;
			st_1 = st1;
			st2_1 = st2;
			mape += Math.abs((float) (this.gi.total[i] - this.gi.free[i]) - st2)
					/ (float) (this.gi.total[i] - this.gi.free[i]);
		}

		float doub = 2.0F * st_1 - st2_1 + alpha / (1.0F - alpha) * (st_1 - st2_1);

		if (mape / (y - x) > 0.5D) {
			return "Trend model not reliable (percentage error is larger than 50%) " + ts;
		}

		float number = doub;
		long max = 0L;
		for (int i = 0; i < y - x + 1; i++) {
			st1 = alpha * number + (1.0F - alpha) * st_1;
			st2 = alpha * st1 + (1.0F - alpha) * st2_1;
			st_1 = st1;
			st2_1 = st2;

			number = 2.0F * st_1 - st2_1 + alpha / (1.0F - alpha) * (st_1 - st2_1);
			if (number < 1.0F) {
				return "Trend ratio(%): 0 (Recommended max heap size of "
						+ numberFormatter.format(1.3F * (float) maxUsed) + " or greater) with percentage error(%): "
						+ 100.0F * mape / (y - x) + " " + ts;
			}
			if (number > 4.0E+009F) {
				return "Trend ratio(%): over 100 with percentage error(%): " + 100.0F * mape / (y - x) + " " + ts;
			}
			if (number > (float) max)
				max = (long) number;

		}

		if (max < this.gi.total[y] - this.gi.free[y])
			return "Trend ratio(%): "
					+ 100.0F * (float) (max - (this.gi.total[y] - this.gi.free[y]))
							/ (float) (this.gi.total[y] - this.gi.free[y])
					+ " (Recommended max heap size of " + numberFormatter.format(1.3F * (float) maxUsed)
					+ " or greater) with percentage error(%): " + 100.0F * mape / (y - x) + " " + ts;
		return "Trend ratio(%): "
				+ 100.0F * (float) (max - (this.gi.total[y] - this.gi.free[y]))
						/ (float) (this.gi.total[y] - this.gi.free[y])
				+ " with percentage error(%): " + 100.0F * mape / (y - x) + " " + ts;
	}

	public long getXmx(String str) {
		if (str == null)
			return -1L;
		int g = str.indexOf('g');
		if (g < 0)
			g = str.indexOf('G');

		long output = 0L;
		String num = null;

		if (g < 0) {
			int m = str.indexOf('m');
			if (m < 0)
				m = str.indexOf('M');
			if (m < 0) {
				int k = str.indexOf('k');
				if (k < 0)
					k = str.indexOf('K');
				if (k < 0) {
					output = Long.parseLong(str);
					return output;
				}
				num = str.substring(0, k);
				if (num == null)
					return -1L;
				output = Long.parseLong(num);
				return output * 1024L;
			}

			num = str.substring(0, m);
			if (num == null)
				return -1L;
			output = Long.parseLong(num);
			return output * 1024L * 1024L;
		}

		num = str.substring(0, g);
		if (num == null)
			return -1L;
		output = Long.parseLong(num);
		return output * 1024L * 1024L * 1024L;
	}

	public void go() {
		ThreadHandler worker = new ThreadHandler() {
			public Object construct() {
				FileTask.this.current = 0;
				FileTask.this.done = false;
				FileTask.this.statMessage = null;
				return new FileTask.ActualTask();
			}
		};
		worker.start();
	}

	private String hangSummary(CompareTableModel cTableModel) {
		String id = "comp";
		String summary = "";
		List<String> threadNameList = Collections.synchronizedList(new ArrayList<String>());

		if ((cTableModel != null) && (cTableModel.hi != null) && (cTableModel.hi.length != 1)) {

			ThreadDump[] hi = cTableModel.hi;
			String[] threads = new String[cTableModel.tidHash.size()];
			boolean[] isMoving = new boolean[cTableModel.tidHash.size()];
			for (int i = 0; i < cTableModel.tidHash.size(); i++) {
				Long tid = (Long) cTableModel.tidHash.get(new Integer(i));

				Integer idx = (Integer) hi[0].threadHash.get(tid);
				if (idx != null) {
					threads[i] = hi[0].javaStack[idx.intValue()];
					if (threads[i] == null) {
						isMoving[i] = true;

						continue;
					}
				} else {
					isMoving[i] = true;
					continue;
				}

				for (int j = 1; j < hi.length; j++) {
					idx = (Integer) hi[j].threadHash.get(tid);
					if (idx != null) {
						if ((hi[j].javaStack[idx.intValue()] == null)
								|| (threads[i].compareTo(hi[j].javaStack[idx.intValue()]) != 0)) {
							isMoving[i] = true;
							break;
						}

					} else {
						isMoving[i] = true;

						break;
					}
				}

				idx = (Integer) hi[0].threadHash.get(tid);
				if ((!isMoving[i]) && (idx != null)) {
					if (hi[0].macro[idx.intValue()] != 1)
						threadNameList.add(hi[0].name[idx.intValue()]);

				}

			}

			if (threadNameList.size() != 0) {
				String[] nameString = (String[]) threadNameList.toArray(new String[threadNameList.size()]);
				Arrays.sort(nameString);
				String highLight = "function highLightHang" + id
						+ "(objref,state,row,col)\n{ \n objref.style.color = (0 == state) ? '#000000' : '#0000FF';\n showStackTrace(stackscomp[row][col]); \n  if(state==0) hideStackTrace();  }\n";
				String javaScript = "\n<script type=\"text/javascript\">\n function showStackHang" + id
						+ "(rowNumber,columnNumber)   { \ndocument.getElementById(\"stackColumnHang" + id
						+ "\").innerHTML=stacks" + id + "[rowNumber][columnNumber];\n }\n" + highLight + " </script>\n";
				summary = "<LI>Number of hang suspects : " + nameString.length
						+ "<BR><BR><LI>List of hang suspects:<BR><BR><UL><table border=\"1\"><col width=20%></col><col width=20%></col><col width=20%></col><col width=40%></col><caption align=\"bottom\">Hang suspects</caption><tr><th>Thread Name</th>";
				summary = summary + "<th>Method</th><th>Stack Trace</th></tr>";

				for (int i = 0; i < nameString.length; i++) {
					for (int j = 0; j < cTableModel.getRowCount(); j++) {
						if (nameString[i].compareTo((String) cTableModel.getValueAt(j, 0)) == 0) {
							summary = summary + "<tr><td>" + cTableModel.getValueAt(j, 0) + "</font></a></td>";
							summary = summary + "<td " + getStateBGColor(cTableModel, j, 1)
									+ "><a  style=\"cursor:hand\"  onmouseover=\"highLightHang" + id + "(this,1," + j
									+ ",0)\" onmouseout=\"highLightHang" + id + "(this,0," + j
									+ ",0)\" onclick=\"showStackHang" + id + "(" + j + ",0)\"><font "
									+ getStateColor(cTableModel, j, 1) + ">" + (cTableModel.getValueAt(j, 1) == null
											? "NO THREAD" : cTableModel.getValueAt(j, 1))
									+ "</font></a></td>";
							break;
						}
					}
					if (i == 0)
						summary = summary + "<td id=\"stackColumnHang" + id + "\" rowspan=\""
								+ cTableModel.getRowCount()
								+ "\" valign=\"top\" >Click on threads to display stack traces</td></tr>";
					else {
						summary = summary + "</tr>";
					}
				}
				summary = javaScript + summary + "</table></UL>";
			} else {
				summary = "<li>No hang suspects found.</br>";
			}

		}

		return summary;
	}

	boolean hasOOM(int x, int y) {
		if (this.gi.outOfHeapSpace == null)
			return false;
		for (int i = 0; i < this.gi.outOfHeapSpace.length; i++) {
			if ((x <= this.gi.outOfHeapSpace[i]) && (y >= this.gi.outOfHeapSpace[i]))
				return true;
		}
		return false;
	}

	public int indexOfContent(String str) {
		int idx = -1;
		int start = str.indexOf(" ");
		if (start >= 0) {
			while ((str.length() > start) && (str.charAt(start) == ' ')) {
				start++;
				idx = start;
			}
		}
		return idx;
	}

	public boolean isDone() {
		return this.done;
	}

	boolean isFragmented(int idx) {
		if (this.gi.free[idx] < 1000000L)
			return false;

		if ((float) this.gi.free[idx] / (float) this.gi.total[idx] > 0.1D)
			return true;
		if ((float) getRequested(idx) / (float) this.gi.free[idx] < 0.05D)
			return true;
		return false;
	}

	private boolean isSamePID(ThreadDump[] td) {
		long pid = td[0].pid;
		for (int i = 1; i < td.length; i++) {
			if (pid != td[i].pid)
				return false;
		}
		return true;
	}

	boolean isStartPoint(int idx) {
		if ((idx >= this.gi.free.length) || (idx < 0))
			return false;
		if (idx == 0)
			return true;
		if (this.gi.ngc[(idx - 1)] >= this.gi.ngc[idx])
			return true;
		return false;
	}

	boolean isTooLarge(int idx) {
		if (getRequested(idx) < 900000L)
			return false;

		if ((float) getRequested(idx) / (float) this.gi.free[idx] > 0.1D)
			return true;
		return false;
	}

	public static long jinwooDecode(String ad) throws NumberFormatException {
		if (ad == null)
			throw new NumberFormatException();
		int max = ad.length();

		if (max == 18) {
			int firstDigit = Character.digit(ad.charAt(2), 16);
			if (firstDigit == -1)
				return -1L;
			if (firstDigit >= 8) {
				int digit = 0;
				long result = 0L;
				int i = 2;
				int b = 0;
				while (i < max) {
					digit = Character.digit(ad.charAt(i++), 16);
					b = (byte) digit;
					b = (byte) (b | 0xFFFFFFF0);
					b = (byte) (b ^ 0xFFFFFFFF);

					result <<= 4;
					result += b;
				}
				result += 1L;
				return -1L * result;
			}
		}

		int digit = 0;
		long result = 0L;
		int i = 2;
		while (i < max) {
			digit = Character.digit(ad.charAt(i++), 16);
			if (digit == -1)
				return -1L;
			result <<= 4;
			result += digit;
		}

		return result;
	}

	public String processThreadDump(File[] file, Configuration cfg, boolean headless) {
		ThreadInfo threadInfo = null;
		if (headless) {
			threadInfo = new ThreadInfo();
		}
		Date dt = new Date();

		String commandLine = new String();
		long tmpL = 0L;
		long numberOfClasses = 0L;
		long numberOfClassLoader = 0L;
		long heapLimit = -1L;
		long heapBase = -1L;

		long lineNumber = 0L;

		long fileLocation = 0L;

		this.gi = new GCInfo();

		String line = new String();

		int i = 0;
		int j = 0;
		int k = 0;

		int totalThread = 0;
		int totalThreadDump = -1;

		for (int z = 0; z < file.length; z++) {
			boolean canCount = false;
			fileLocation = 0L;
			lineNumber = 0L;
			this.monIndex = 0;
			numberOfClasses = numberOfClassLoader = 0L;
			this.monList = new ArrayList<MonitorDump>();
			this.threadIdent = new Hashtable<Long, Long>();

			this.overall = 0;
			this.statMessage = "Loading thread dump";
			totalThread = 0;
			totalThreadDump = -1;
			ArrayList<Integer> list = new ArrayList<Integer>();
			try {
				if (!file[z].exists()) {
					JOptionPane.showMessageDialog(this.ha, "File not found:" + file[z].getAbsoluteFile(),
							"File Open Error", 2);
					continue;
				}

				long fileSize = file[z].length();
				BufferedReader in = new BufferedReader(new InputStreamReader(Analyzer.getInputStream(file[z])));
				line = in.readLine();
				lineNumber += 1L;
				if (line != null) {
					fileLocation += line.length();
					if ((line.startsWith(
							"NULL           ------------------------------------------------------------------------"))
							|| (line.startsWith(
									"NULL            ---------------------------------------------------------------"))
							|| (line.startsWith("0SECTION       TITLE subcomponent dump routine")))
						this.isJavacore = true;
				}
				if (this.isJavacore) {
					while (line != null) {
						if (line.startsWith("1XMTHDINFO")) {
							canCount = true;
						} else if ((line.startsWith("3XMTHREADINFO ")) && (!line.contains("Anonymous native thread"))) {
							if (canCount)
								totalThread++;
						} else if (line.startsWith("2CLTEXTCLLOADER")) {
							int loader = line.indexOf("Loader ");
							int parentloader = line.indexOf(", Parent ");
							int comma = line.indexOf(",");
							if ((loader != -1) && (comma != -1)) {

								if (parentloader != -1) {
								}
							}

							line = in.readLine();
							if ((line != null) && (line.startsWith("3CLNMBRLOADEDCL"))) {
								int number = line.indexOf("Number of loaded classes");
								if (number != -1) {
								}
							}

						} else if (line.startsWith("1STCURHBASE")) {
							i = line.indexOf(":");
							if (i != -1) {
								j = line.indexOf("0x");
								if (j == -1)
									heapBase = jinwooDecode("0x" + line.substring(i + 1).trim());
								else {
									heapBase = jinwooDecode(line.substring(j).trim());
								}
							}
						} else if (line.startsWith("1STCURHLIM")) {
							i = line.indexOf(":");
							if (i != -1) {
								j = line.indexOf("0x");
								if (j == -1)
									heapLimit = jinwooDecode("0x" + line.substring(i + 1).trim());
								else {
									heapLimit = jinwooDecode(line.substring(j).trim());
								}
							}
						} else if (line.startsWith("3CLTEXTCLASS")) {
							i = line.lastIndexOf("(0x");
							j = line.lastIndexOf(")");
							if ((i != -1) && (j != -1) && (heapBase == -1L)) {
								numberOfClasses += 1L;
							} else if ((i != -1) && (j != -1) && (heapBase != -1L) && (heapLimit != -1L)) {
								tmpL = jinwooDecode(line.substring(i + 1, j));

								if ((heapBase != heapLimit) && (tmpL >= heapBase) && (tmpL <= heapLimit))
									numberOfClasses += 1L;
							}
						} else if (line.startsWith("2CLTEXTCLLOAD  \t\tLoader ")) {
							numberOfClassLoader += 1L;
						}
						line = in.readLine();
						lineNumber += 1L;
						if (line != null) {
							fileLocation += line.length();
							if (line.startsWith("2LKFLATMON         ident 0x")) {
								i = line.indexOf(" \"");
								if (i != -1) {
									Long key = new Long(jinwooDecode(
											"0x" + line.substring("2LKFLATMON         ident 0x".length(), i)));

									i = line.lastIndexOf("\" (0x");
									if (i != -1) {
										j = line.lastIndexOf(")");
										if (j != -1) {
											Long value = new Long(
													jinwooDecode("0x" + line.substring(i + "\" (0x".length(), j)));

											this.threadIdent.put(key, value);
										}
									} else {
										i = line.lastIndexOf("\" (");
										if (i != -1) {
											j = line.lastIndexOf(")");
											if (j != -1) {
												Long value = new Long(
														jinwooDecode("0x" + line.substring(i + "\" (".length(), j)));

												this.threadIdent.put(key, value);
											}
										}
									}
								}
							}
						}

						this.current = ((int) (100.0F * (float) fileLocation / (float) fileSize));

						this.overall = ((int) (30.0F * (float) fileLocation / (float) fileSize));
					}
				} else {
					int tempI = 0;
					while (line != null) {
						if (line.indexOf("Full thread dump") >= 0) {
							totalThreadDump++;
							list.add(totalThreadDump, new Integer(0));
						} else if ((line.startsWith("\"")) && (line.indexOf(" prio=") >= 0)) {
							tempI = ((Integer) list.get(totalThreadDump)).intValue();
							list.set(totalThreadDump, new Integer(tempI + 1));
							totalThread++;
						}

						line = in.readLine();
						lineNumber += 1L;
						if (line != null)
							fileLocation += line.length();
						this.current = ((int) (100.0F * (float) fileLocation / (float) fileSize));

						this.overall = ((int) (30.0F * (float) fileLocation / (float) fileSize));
					}
				}

				in.close();
			} catch (Exception e) {
				System.out.println("Exception while parsing line " + numberFormatter.format(lineNumber) + " : " + line
						+ " in " + file[z].getName());
				if (!headless) {
					this.ha.handleException(e);
				}
				continue;
			}
			BufferedReader in;
			long fileSize = 0;
			if (totalThread == 0) {
				if (!headless) {
					JOptionPane.showMessageDialog(this.ha, "Cannot find any thread dumps in " + file[z].getName(),
							"Information", 2);
				}

			} else {
				long totalLine = lineNumber;
				fileLocation = 0L;
				String oldLine = null;
				try {
					lineNumber = 0L;

					in = new BufferedReader(new InputStreamReader(Analyzer.getInputStream(file[z])));
					line = in.readLine();
					lineNumber += 1L;
					if (line != null) {
						fileLocation += line.length();
					}

					ThreadDump td = null;
					int dumpIndex = 0;

					if (this.isJavacore) {
						td = new ThreadDump();
						td.xmx = -1L;
						td.isIBM = true;
						td.javaHeap = -1;
						td.fileName = file[z].getName();
						td.name = new String[totalThread];
						td.pattern = new String[totalThread];
						td.isDeadlock = new boolean[totalThread];
						td.nid = new long[totalThread];
						td.state = new int[totalThread];
						td.priority = new int[totalThread];
						td.javaStack = new String[totalThread];
						td.macro = new int[totalThread];
						td.nativeStack = new String[totalThread];
						td.sys_thread = new long[totalThread];
						td.tid = new long[totalThread];
						td.summary = ("<LI>File name : " + file[z].getAbsolutePath() + "<BR><BR>");
						td.warning = "";

						td.javaHeap = -1;
						totalThread = 0;

						td.free = -1L;
						td.allocated = -1L;
						td.af = -1L;
						td.gc = -1L;
						td.pid = -1L;
						td.currentTid = -1L;
						String args = null;
						boolean inArgs = false;

						boolean inAnonymous = false;
						while (line != null) {
							if (line.startsWith("3STHSTTYPE")) {
								if (td.gcHistory == null)
									td.gcHistory = new String();
								ThreadDump tmp1956_1954 = td;
								tmp1956_1954.gcHistory = (tmp1956_1954.gcHistory
										+ line.substring("3STHSTTYPE".length() + 1).trim() + " <BR>");
							} else if (line.startsWith("1XMCURTHDINFO")) {
								line = in.readLine();
								lineNumber += 1L;
								if (line == null)
									break;
								if (line.startsWith("NULL")) {
									line = in.readLine();
									lineNumber += 1L;
									if (line == null)
										break;
									if (line.startsWith("3XMTHREADINFO")) {
										i = line.indexOf(", state:");
										j = line.indexOf(", sys_thread_t:");
										if ((i != -1) && (j != -1)) {
											if (line.substring(j + ", sys_thread_t:".length(), i).startsWith("0x"))
												td.currentTid = jinwooDecode(
														line.substring(j + ", sys_thread_t:".length(), i));
											else {
												td.currentTid = jinwooDecode(
														"0x" + line.substring(j + ", sys_thread_t:".length(), i));
											}
										}

										i = line.indexOf('"');
										j = line.lastIndexOf('"');

										if ((i != -1) && (j != -1)) {
											ThreadDump tmp2233_2231 = td;
											tmp2233_2231.summary = (tmp2233_2231.summary + "<LI>Current Thread : "
													+ line.substring(i, j + 1) + "<BR><BR>");
										}
									}
								}
								do {
									line = in.readLine();
									lineNumber += 1L;
									if (line == null)
										break;
								} while (!line.startsWith("1XMTHDINFO"));
							} else if (line.startsWith("1TIDATETIME")) {
								String converted = line.replace('<', ' ');
								line = converted.replace('>', ' ');
								ThreadDump tmp2356_2354 = td;
								tmp2356_2354.summary = (tmp2356_2354.summary + "<LI>"
										+ line.substring("1TIDATETIME".length()).trim() + "<BR><BR>");
								i = line.indexOf(":");
								if (i != -1) {
									dt = null;
									try {
										dt = formatter.parse(line.substring(1 + i).trim());
									} catch (ParseException localParseException) {
									}
									if (dt != null)
										td.timeStamp = dt.getTime();
								}
							} else if (line.startsWith("2XHOSLEVEL     OS Level         : ")) {
								td.osLevel = line.substring("2XHOSLEVEL     OS Level         : ".length());
								ThreadDump tmp2494_2492 = td;
								tmp2494_2492.summary = (tmp2494_2492.summary + "<LI>Operating System : " + td.osLevel
										+ "<BR><BR>");
							} else if (line.startsWith("3XHCPUARCH       Architecture   : ")) {
								td.architecture = line.substring("3XHCPUARCH       Architecture   : ".length());
								ThreadDump tmp2565_2563 = td;
								tmp2565_2563.summary = (tmp2565_2563.summary + "<LI>Processor Architecture : "
										+ td.architecture + "<BR><BR>");
							} else if (line.startsWith("3XHNUMCPUS       How Many       : ")) {
								td.numberOfCPU = line.substring("3XHNUMCPUS       How Many       : ".length());
								ThreadDump tmp2636_2634 = td;
								tmp2636_2634.summary = (tmp2636_2634.summary + "<LI>Number of Processors : "
										+ td.numberOfCPU + "<BR><BR>");
							} else if (line.startsWith("1CIUSERARGS")) {
								td.summary += "<LI>User Arguments :<BR>";
							} else if (line.startsWith("2CIUSERARG")) {
								inArgs = true;
								args = line.substring("2CIUSERARG".length());
								td.userArgs += args;
								ThreadDump tmp2777_2775 = td;
								tmp2777_2775.summary = (tmp2777_2775.summary + args + "<BR>");
							} else if (line.startsWith("NULL")) {
								if (inArgs) {
									td.summary += "<BR>";
									inArgs = false;
								}
							} else if (line.startsWith("1CIJAVAHOMEDIR Java Home Dir:")) {
								td.homeDir = line.substring("1CIJAVAHOMEDIR Java Home Dir:".length());
								ThreadDump tmp2889_2887 = td;
								tmp2889_2887.summary = (tmp2889_2887.summary + "<LI>Java Home Directory : " + td.homeDir
										+ "<BR><BR>");
							} else if (line.startsWith("1CIJAVADLLDIR  Java DLL Dir:")) {
								td.dllDir = line.substring("1CIJAVADLLDIR  Java DLL Dir:".length());
								ThreadDump tmp2960_2958 = td;
								tmp2960_2958.summary = (tmp2960_2958.summary + "<LI>Java DLL Directory : " + td.dllDir
										+ "<BR><BR>");
							} else if (line.startsWith("1CISYSCP       Sys Classpath:")) {
								td.sysCP = line.substring("1CISYSCP       Sys Classpath:".length());
								ThreadDump tmp3031_3029 = td;
								tmp3031_3029.summary = (tmp3031_3029.summary + "<LI>System Classpath : " + td.sysCP
										+ "<BR><BR>");
							} else if (line.startsWith("1STHEAPFREE")) {
								i = line.indexOf(": ");
								if (i == -1) {
									ThreadDump tmp3103_3101 = td;
									tmp3103_3101.summary = (tmp3103_3101.summary
											+ line.substring("1STHEAPFREE".length()).trim() + "<BR>");
								} else {
									try {
										td.free = jinwooDecode("0x" + line.substring(i + 2).trim());
									} catch (NumberFormatException e) {
										td.free = -1L;
									}
									if (td.free == -1L) {
										td.summary += "<LI>Free Java heap size: unknown<BR>";
									} else {
										ThreadDump tmp3244_3242 = td;
										tmp3244_3242.summary = (tmp3244_3242.summary + "<LI>Free Java heap size: "
												+ numberFormatter.format(td.free) + " bytes<BR><BR>");
									}
								}
							} else if (line.startsWith("1STHEAPALLOC")) {
								i = line.indexOf(": ");
								if (i == -1) {
									ThreadDump tmp3322_3320 = td;
									tmp3322_3320.summary = (tmp3322_3320.summary
											+ line.substring("1STHEAPALLOC".length()).trim() + "<BR><BR>");
								} else {
									try {
										td.allocated = jinwooDecode("0x" + line.substring(i + 2).trim());
									} catch (NumberFormatException e) {
										td.allocated = -1L;
									}
									if (td.allocated == -1L) {
										td.summary += "<LI>Allocated Java heap size: unknown<BR><BR>";
									} else {
										ThreadDump tmp3463_3461 = td;
										tmp3463_3461.summary = (tmp3463_3461.summary + "<LI>Allocated Java heap size: "
												+ numberFormatter.format(td.allocated) + " bytes<BR><BR>");
									}
								}
							} else if (line.startsWith("1STAFCTR")) {
								i = line.indexOf(": ");
								if (i != -1)
									td.af = jinwooDecode(line.substring(i + 2).trim());
								ThreadDump tmp3561_3559 = td;
								tmp3561_3559.summary = (tmp3561_3559.summary + "<LI>"
										+ line.substring("1STAFCTR".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1STGCCTR")) {
								i = line.indexOf(": ");
								if (i != -1)
									td.gc = jinwooDecode(line.substring(i + 2).trim());
								ThreadDump tmp3661_3659 = td;
								tmp3661_3659.summary = (tmp3661_3659.summary + "<LI>"
										+ line.substring("1STGCCTR".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("0MEMUSER")) {
								td.nativeMemoryTree = new JTree();

								DefaultMutableTreeNode[] levels = new DefaultMutableTreeNode[20];
								levels[0] = new DefaultMutableTreeNode("Native Memory : Size / Allocation");

								while ((line != null) && (line.contains(MEMUSER))) {
									int level = Integer.parseInt(line.substring(0, line.indexOf(MEMUSER)));

									i = line.indexOf(":");
									if (i >= 0) {
										j = line.lastIndexOf("-");
										if (j < 0)
											j = i - 4;
										DefaultMutableTreeNode jre = new DefaultMutableTreeNode(line.substring(j + 1));
										levels[(level - 1)].add(jre);
										levels[level] = jre;
									}
									line = in.readLine();
								}
								td.nativeMemoryTree = new JTree(levels[0]);
							} else if (line.startsWith("1STHEAPTYPE    Object Memory")) {
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STHEAP") < 0)
									continue;
								long countIM = 0L;
								StringTokenizer st = null;

								long totalUsed = 0L;
								long totalReserved = 0L;
								long sumCount = 0L;
								long sumUsed = 0L;
								long sumReserved = 0L;
								while (line.indexOf("1STHEAP") >= 0) {
									st = new StringTokenizer(line);
									st.nextToken();
									st.nextToken();
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else {
										base = jinwooDecode("0x" + baseStr);
									}

									countIM += 1L;
									totalUsed += base;
									totalReserved += base;
									line = in.readLine();
								}
								td.summary += "<LI> Memory Segment Analysis <BR><BR><table border=\"1\"><col width=10%></col><col width=20%></col><col width=20%></col><col width=20%></col><col width=20%></col><caption align=\"bottom\">Memory Segment Analysis</caption><tr><th>Memory Type</th><th># of Segments</th><th>Used Memory(bytes)</th><th>Used Memory(%)</th><th>Free Memory(bytes)</th><th>Free Memory(%)</th><th>Total Memory(bytes)</th></tr>";
								ThreadDump tmp4126_4124 = td;
								tmp4126_4124.summary = (tmp4126_4124.summary
										+ "<tr><td>Object(reserved)</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								while (line.indexOf("1STSEGTYPE     Internal Memory") < 0) {
									line = in.readLine();
									if (line.startsWith("1STHEAPFREE")) {
										i = line.indexOf(": ");
										j = line.indexOf("(");
										if ((i >= 0) && (j >= 0)) {
											try {
												td.free = Long.parseLong(line.substring(i + 2, j).trim());
											} catch (NumberFormatException e) {
												td.free = -1L;
											}
										}
									} else if (line.startsWith("1STHEAPTOTAL")) {
										i = line.indexOf(": ");
										j = line.indexOf("(");
										if ((i >= 0) && (j >= 0)) {
											try {
												td.allocated = Long.parseLong(line.substring(i + 2, j).trim());
											} catch (NumberFormatException e) {
												td.allocated = -1L;
											}
										}
									} else if (line.startsWith("1STHEAPINUSE")) {
										i = line.indexOf(": ");
										j = line.indexOf("(");
										if ((i >= 0) && (j >= 0)) {
											try {
												td.inuse = Long.parseLong(line.substring(i + 2, j).trim());
											} catch (NumberFormatException e) {
												td.inuse = -1L;
											}
										}

									}

								}

								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp4850_4848 = td;
								tmp4850_4848.summary = (tmp4850_4848.summary + "<tr><td>Internal</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								while (line.indexOf("1STSEGTYPE     Class Memory") < 0) {
									line = in.readLine();
								}

								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp5341_5339 = td;
								tmp5341_5339.summary = (tmp5341_5339.summary + "<tr><td>Class</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								while (line.indexOf("1STSEGTYPE     JIT Code Cache") < 0) {
									line = in.readLine();
								}

								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp5832_5830 = td;
								tmp5832_5830.summary = (tmp5832_5830.summary
										+ "<tr><td>JIT Code Cache</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								while (line.indexOf("1STSEGTYPE     JIT Data Cache") < 0) {
									line = in.readLine();
								}

								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp6323_6321 = td;
								tmp6323_6321.summary = (tmp6323_6321.summary
										+ "<tr><td>JIT Data Cache</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;
								ThreadDump tmp6501_6499 = td;
								tmp6501_6499.summary = (tmp6501_6499.summary + "<tr><td>Overall</td><td align=right>"
										+ numberFormatter.format(sumCount) + "</td><td align=right>"
										+ numberFormatter.format(sumUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) sumUsed / (float) sumReserved)
										+ "</td><td align=right>" + numberFormatter.format(sumReserved - sumUsed)
										+ "</td><td align=right>"
										+ numberFormatter
												.format(100.0F * (float) (sumReserved - sumUsed) / (float) sumReserved)
										+ "</td><td align=right>" + numberFormatter.format(sumReserved)
										+ "</td></tr></table><BR><BR>");

								if (td.allocated == -1L) {
									td.summary += "<LI>Total Java heap size: unknown<BR><BR>";
								} else {
									ThreadDump tmp6702_6700 = td;
									tmp6702_6700.summary = (tmp6702_6700.summary + "<LI>Total Java heap size: "
											+ numberFormatter.format(td.allocated) + " bytes<BR><BR>");
								}
								if (td.inuse == -1L) {
									td.summary += "<LI>Used Java heap size: unknown<BR>";
								} else {
									ThreadDump tmp6795_6793 = td;
									tmp6795_6793.summary = (tmp6795_6793.summary + "<LI>Used Java heap size: "
											+ numberFormatter.format(td.inuse) + " bytes<BR><BR>");
								}
								if (td.free == -1L) {
									td.summary += "<LI>Free Java heap size: unknown<BR>";
								} else {
									ThreadDump tmp6888_6886 = td;
									tmp6888_6886.summary = (tmp6888_6886.summary + "<LI>Free Java heap size: "
											+ numberFormatter.format(td.free) + " bytes<BR><BR>");
								}

							} else if (line.startsWith("1STSEGTYPE     Internal Memory")) {
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								long countIM = 0L;
								StringTokenizer st = null;

								long totalUsed = 0L;
								long totalReserved = 0L;
								long sumCount = 0L;
								long sumUsed = 0L;
								long sumReserved = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								td.summary += "<LI> Memory Segment Analysis <BR><BR><table border=\"1\"><col width=10%></col><col width=20%></col><col width=20%></col><col width=20%></col><col width=20%></col><caption align=\"bottom\">Memory Segment Analysis</caption><tr><th>Memory Type</th><th># of Segments</th><th>Used Memory(bytes)</th><th>Used Memory(%)</th><th>Free Memory(bytes)</th><th>Free Memory(%)</th><th>Total Memory(bytes)</th></tr>";
								ThreadDump tmp7290_7288 = td;
								tmp7290_7288.summary = (tmp7290_7288.summary + "<tr><td>Internal</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGTYPE     Object Memory") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp7794_7792 = td;
								tmp7794_7792.summary = (tmp7794_7792.summary
										+ "<tr><td>Object(reserved)</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGTYPE     Class Memory") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp8298_8296 = td;
								tmp8298_8296.summary = (tmp8298_8296.summary + "<tr><td>Class</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGTYPE     JIT Code Cache") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp8802_8800 = td;
								tmp8802_8800.summary = (tmp8802_8800.summary
										+ "<tr><td>JIT Code Cache</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;

								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGTYPE     JIT Data Cache") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("NULL") < 0)
									continue;
								line = in.readLine();
								if (line.indexOf("1STSEGMENT") < 0)
									continue;
								totalUsed = 0L;
								totalReserved = 0L;
								countIM = 0L;
								while (line.indexOf("1STSEGMENT") >= 0) {
									st = new StringTokenizer(line);
									if (st.countTokens() != 7)
										break;
									st.nextToken();
									st.nextToken();
									String baseStr = st.nextToken();
									long base;
									if (baseStr.startsWith("0x"))
										base = jinwooDecode(baseStr);
									else
										base = jinwooDecode("0x" + baseStr);
									String allocStr = st.nextToken();
									long alloc;
									if (allocStr.startsWith("0x"))
										alloc = jinwooDecode(allocStr);
									else
										alloc = jinwooDecode("0x" + allocStr);
									st.nextToken();
									st.nextToken();
									String reservedStr = st.nextToken();
									long reserved;
									if (reservedStr.startsWith("0x"))
										reserved = jinwooDecode(reservedStr);
									else {
										reserved = jinwooDecode("0x" + reservedStr);
									}
									countIM += 1L;
									totalUsed += alloc - base;
									totalReserved += reserved;
									line = in.readLine();
								}
								ThreadDump tmp9306_9304 = td;
								tmp9306_9304.summary = (tmp9306_9304.summary
										+ "<tr><td>JIT Data Cache</td><td align=right>"
										+ numberFormatter.format(countIM) + "</td><td align=right>"
										+ numberFormatter.format(totalUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) totalUsed / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved - totalUsed)
										+ "</td><td align=right>"
										+ numberFormatter.format(
												100.0F * (float) (totalReserved - totalUsed) / (float) totalReserved)
										+ "</td><td align=right>" + numberFormatter.format(totalReserved)
										+ "</td></tr>");
								sumCount += countIM;
								sumUsed += totalUsed;
								sumReserved += totalReserved;
								ThreadDump tmp9484_9482 = td;
								tmp9484_9482.summary = (tmp9484_9482.summary + "<tr><td>Overall</td><td align=right>"
										+ numberFormatter.format(sumCount) + "</td><td align=right>"
										+ numberFormatter.format(sumUsed) + "</td><td align=right>"
										+ numberFormatter.format(100.0F * (float) sumUsed / (float) sumReserved)
										+ "</td><td align=right>" + numberFormatter.format(sumReserved - sumUsed)
										+ "</td><td align=right>"
										+ numberFormatter
												.format(100.0F * (float) (sumReserved - sumUsed) / (float) sumReserved)
										+ "</td><td align=right>" + numberFormatter.format(sumReserved)
										+ "</td></tr></table><BR><BR>");
							} else if (line.startsWith("1CICMDLINE")) {
								String converted = line.replace('<', ' ');
								line = converted.replace('>', ' ');
								commandLine = "<LI>Command line :<BR>" + line.substring("1CICMDLINE".length()).trim()
										+ "<BR><BR>";
								i = line.lastIndexOf(" -Xmx");
								j = line.lastIndexOf(" -Xmxcl");
								while ((i == j) && (j >= 0) && (i >= 0)) {
									i = line.substring(0, j).lastIndexOf(" -Xmx");
									j = line.substring(0, j).lastIndexOf(" -Xmxcl");
								}
								boolean sub = false;
								String max = null;
								if (i != -1) {
									max = line.substring(i + 5, line.indexOf(" ", i + 5));
									ThreadDump tmp9820_9818 = td;
									tmp9820_9818.summary = (tmp9820_9818.summary
											+ "<LI>Java Heap Information<BR><UL><LI>Maximum Java heap size : " + max
											+ "<BR>");
									sub = true;
									td.xmx = getXmx(max);
								}

								i = line.lastIndexOf(" -Xms");
								if (i != -1) {
									ThreadDump tmp9890_9888 = td;
									tmp9890_9888.summary = (tmp9890_9888.summary + "<LI>Initial Java heap size : "
											+ line.substring(i + 5, line.indexOf(" ", i + 5)) + "<BR>");
								}
								i = line.lastIndexOf(" -Xmxcl");
								if (i != -1) {
									ThreadDump tmp9965_9963 = td;
									tmp9965_9963.summary = (tmp9965_9963.summary + "<LI>Maximum class loader size : "
											+ line.substring(i + 7, line.indexOf(" ", i + 7)) + "<BR>");
								}
								if (sub)
									td.summary += "</UL><BR>";
							} else if (line.startsWith("1CIJAVAVERSION")) {
								if (line.indexOf("IBM J9") >= 0)
									td.isJ9 = true;
								ThreadDump tmp10089_10087 = td;
								tmp10089_10087.summary = (tmp10089_10087.summary + "<LI>Java version : "
										+ line.substring("1CIJAVAVERSION".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1CIVMVERSION")) {
								ThreadDump tmp10153_10151 = td;
								tmp10153_10151.summary = (tmp10153_10151.summary + "<LI>Virtual machine version : "
										+ line.substring("1CIVMVERSION".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1CIJITVERSION")) {
								ThreadDump tmp10217_10215 = td;
								tmp10217_10215.summary = (tmp10217_10215.summary
										+ "<LI>Just-In-Time(JIT) compiler switch, Ahead-Of-Time (AOT) compiler switch, Compiler version : "
										+ line.substring("1CIJITVERSION".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1CIGCVERSION")) {
								ThreadDump tmp10281_10279 = td;
								tmp10281_10279.summary = (tmp10281_10279.summary + "<LI>Garbage collector version : "
										+ line.substring("1CIGCVERSION".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1TISIGINFO")) {
								ThreadDump tmp10345_10343 = td;
								tmp10345_10343.summary = (tmp10345_10343.summary + "<LI>Cause of thread dump : "
										+ line.substring("1TISIGINFO".length()).trim() + "<BR><BR>");
							} else if (line.startsWith("1TIFILENAME")) {
								td.pid = getPid(line);
								if (td.pid != -1L) {
									ThreadDump tmp10432_10430 = td;
									tmp10432_10430.summary = (tmp10432_10430.summary + "<LI>Process ID : " + td.pid
											+ "<BR><BR>");
								} else {
									td.summary += "<LI>Process ID : Not available<BR><BR>";
								}
							} else if (line.startsWith("2LKDEADLOCKTHR  Thread \"")) {
								do {
									if (line.startsWith("2LKDEADLOCKTHR  Thread \"")) {
										i = line.lastIndexOf("(");
										j = line.lastIndexOf(")");
										if ((i != -1) && (j != -1)) {
											long systhread;
											try {
												systhread = jinwooDecode(line.substring(i + 1, j));
											} catch (NumberFormatException e) {
												systhread = jinwooDecode("0x" + line.substring(i + 1, j));
											}

											Long newSysThread = new Long(systhread);
											if (td.deadlock == null)
												td.deadlock = new ArrayList<Object>();
											if (!td.deadlock.contains(newSysThread)) {
												td.deadlock.add(newSysThread);
											}
										}
									} else if (line.startsWith("NULL")) {
										if (td.deadlock == null)
											td.deadlock = new ArrayList<Object>();
										td.deadlock.add(new Long(-1L));
									} else if (line.startsWith("2LKERROR")) {
										ThreadDump tmp10747_10745 = td;
										tmp10747_10745.warning = (tmp10747_10745.warning
												+ "<LI style=\"color:red\"><b>***WARNING*** Thread dump error : </b>"
												+ line.substring("2LKERROR".length() + 1).trim() + "<BR><BR>");
									}
									line = in.readLine();
									lineNumber += 1L;

									if ((line == null) || (line.startsWith(
											"NULL           ------------------------------------------------------------------------")))
										break;
								} while (!line.startsWith(
										"NULL            ---------------------------------------------------------------"));
							} else if (line.startsWith("2LKERROR")) {
								ThreadDump tmp10853_10851 = td;
								tmp10853_10851.warning = (tmp10853_10851.warning
										+ "<LI style=\"color:red\"><b>***WARNING*** Thread dump error : </b>"
										+ line.substring("2LKERROR".length() + 1).trim() + "<BR><BR>");
							} else if (line.startsWith("2LKREGMON          Heap lock")) {
								i = line.lastIndexOf(": owner \"");
								if (i != -1) {
									j = line.substring(0, i).lastIndexOf(" ");

									String name = "Heap lock";
									i = line.lastIndexOf("\" (0x");
									k = 5;
									if (i == -1) {
										i = line.lastIndexOf("(");
										k = 1;
									}
									j = line.lastIndexOf(")");
									if ((i != -1) && (j != -1)) {
										long value = jinwooDecode("0x" + line.substring(i + k, j));
										MonitorDump mon = new MonitorDump(name, value, true);
										this.monList.add(this.monIndex++, mon);
										line = in.readLine();
										lineNumber += 1L;
										if (line.startsWith("3LKWAITERQ            Waiting to enter:")) {
											mon.waiting = new ArrayList<Long>();
											int waitIndex = 0;
											line = in.readLine();
											lineNumber += 1L;
											while (line != null) {
												if (!line.startsWith("3LKWAITER"))
													break;
												i = line.lastIndexOf("(0x");
												k = 3;
												j = line.lastIndexOf(")");
												if (i == -1) {
													i = line.lastIndexOf("(");
													k = 1;
												}
												if ((i != -1) && (j != -1)) {
													mon.waiting.add(waitIndex++,
															new Long(jinwooDecode("0x" + line.substring(i + k, j))));
												}
												line = in.readLine();
												lineNumber += 1L;
											}
										} else {
											this.monIndex -= 1;
											if (this.monIndex >= 0)
												this.monList.remove(this.monIndex);
											else
												System.out.println("Error: invalid monIndex," + this.monIndex);
										}
									}
								}
							} else if ((line.startsWith("3LKMONOBJECT")) || (line.startsWith("2LKREGMON"))) {
								i = line.indexOf("thread ident 0x");

								if (i != -1) {
									j = line.lastIndexOf(",");
									if (j != -1) {
										Long valueLong = (Long) this.threadIdent.get(new Long(jinwooDecode(
												"0x" + line.substring(i + "thread ident 0x".length(), j))));

										i = line.indexOf(": Flat");
										if ((i != -1) && (valueLong != null)) {
											j = line.substring(0, i).lastIndexOf(" ");
											String name = line.substring(j + 1, i);

											MonitorDump mon = new MonitorDump(name, valueLong.longValue());
											this.monList.add(this.monIndex++, mon);
											line = in.readLine();
											lineNumber += 1L;
											if (line.startsWith("3LKNOTIFYQ")) {
												mon.waiting = new ArrayList<Long>();
												int waitIndex = 0;
												line = in.readLine();
												lineNumber += 1L;
												while (line != null) {
													if (!line.startsWith("3LKWAITNOTIFY"))
														break;
													i = line.lastIndexOf("(0x");
													k = 3;
													j = line.lastIndexOf(")");
													if (i == -1) {
														i = line.lastIndexOf("(");
														k = 1;
													}

													if ((i != -1) && (j != -1)) {
														mon.waiting.add(waitIndex++, new Long(
																jinwooDecode("0x" + line.substring(i + k, j))));
													}

													line = in.readLine();
													lineNumber += 1L;
												}
											}
										}
									}

								} else {
									i = line.lastIndexOf(": owner \"");
									if (i != -1) {
										if (this.debug)
											System.out.println("owner " + line);

										if (td.isJ9) {
											if (this.debug)
												System.out.println("J9");
											td.isNewFormat = true;
										}
										j = line.substring(0, i).lastIndexOf(" ");
										String name = null;
										if (line.startsWith("2LKREGMON")) {
											String sysMon = line.substring("2LKREGMON".length());
											sysMon = sysMon.trim();
											name = sysMon.substring(0, sysMon.lastIndexOf(": owner \""));
										} else {
											name = line.substring(j + 1, i);
										}

										int x = i;
										i = line.substring(x).lastIndexOf("(0x");
										k = 3;
										if (i == -1) {
											i = line.lastIndexOf("(");
											k = 1;
										} else {
											i += x;
										}
										j = line.lastIndexOf(")");
										if ((i != -1) && (j != -1)) {
											if (this.debug)
												System.out.println("Owner: " + jinwooDecode(new StringBuilder("0x")
														.append(line.substring(i + k, j)).toString()));
											long value = jinwooDecode("0x" + line.substring(i + k, j));
											MonitorDump mon = new MonitorDump(name, value);
											this.monList.add(this.monIndex++, mon);
											line = in.readLine();
											lineNumber += 1L;
											if (line.startsWith("3LKNOTIFYQ")) {
												mon.waiting = new ArrayList<Long>();
												int waitIndex = 0;
												line = in.readLine();
												lineNumber += 1L;
												while (line != null) {
													if (!line.startsWith("3LKWAITNOTIFY"))
														break;
													i = line.lastIndexOf("(0x");
													k = 3;
													j = line.lastIndexOf(")");
													if (i == -1) {
														i = line.lastIndexOf("(");
														k = 1;
													}

													if ((i != -1) && (j != -1)) {
														if (this.debug)
															System.out.println(
																	jinwooDecode("0x" + line.substring(i + k, j)));
														mon.waiting.add(waitIndex++, new Long(
																jinwooDecode("0x" + line.substring(i + k, j))));
													}

													line = in.readLine();
													lineNumber += 1L;
												}

											} else if (line.startsWith("3LKWAITERQ")) {
												mon.waiting = new ArrayList<Long>();
												int waitIndex = 0;
												line = in.readLine();
												lineNumber += 1L;
												while (line != null) {
													if (!line.startsWith("3LKWAITER"))
														break;
													i = line.lastIndexOf("(0x");
													k = 3;
													j = line.lastIndexOf(")");
													if (i == -1) {
														i = line.lastIndexOf("(");
														k = 1;
													}

													if (this.debug)
														System.out.println(line.substring(i + "(0x".length(), j));
													if ((i != -1) && (j != -1)) {
														mon.waiting.add(waitIndex++, new Long(
																jinwooDecode("0x" + line.substring(i + k, j))));
														if (this.debug)
															System.out.println(
																	"adding " + jinwooDecode(new StringBuilder("0x")
																			.append(line.substring(i + k, j))
																			.toString()));
													}

													line = in.readLine();
													lineNumber += 1L;
												}
											} else {
												this.monIndex -= 1;
												if (this.monIndex >= 0)
													this.monList.remove(this.monIndex);
												else {
													System.out.println("Error: invalid monIndex," + this.monIndex);
												}
											}
										}
									} else {
										i = line.indexOf("Flat locked by \"");

										if (i >= 0) {
											if (this.debug)
												System.out.println("Setting new format j9");
											td.isNewFormat = true;

											i = line.lastIndexOf("(");
											j = line.lastIndexOf(")");
											if ((i > -1) && (j > -1) && (j > i)) {
												long value = jinwooDecode(line.substring(i + 1, j));

												i = line.indexOf(": Flat");

												if (i != -1) {
													String name = null;
													if (line.startsWith("2LKREGMON")) {
														String sysMon = line.substring("2LKREGMON".length());
														sysMon = sysMon.trim();
														name = sysMon.substring(0, sysMon.indexOf(": Flat"));
													} else {
														j = line.substring(0, i).lastIndexOf(" ");
														name = line.substring(j + 1, i);
													}

													MonitorDump mon = new MonitorDump(name, value);
													this.monList.add(this.monIndex++, mon);
													line = in.readLine();
													lineNumber += 1L;
													if (line.startsWith("3LKWAITERQ")) {
														mon.waiting = new ArrayList<Long>();
														int waitIndex = 0;
														line = in.readLine();
														lineNumber += 1L;
														while (line != null) {
															if (!line.startsWith("3LKWAITER"))
																break;
															i = line.lastIndexOf("(0x");
															k = 3;
															j = line.lastIndexOf(")");
															if (i == -1) {
																i = line.lastIndexOf("(");
																k = 1;
															}

															if ((i != -1) && (j != -1)) {
																mon.waiting.add(waitIndex++, new Long(
																		jinwooDecode("0x" + line.substring(i + k, j))));
															}

															line = in.readLine();
															lineNumber += 1L;
														}

													} else {
														this.monIndex -= 1;
														if (this.monIndex >= 0)
															this.monList.remove(this.monIndex);
														else {
															System.out.println(
																	"Error: invalid monIndex," + this.monIndex);
														}
													}
												}
											}
										}

									}

								}

							} else if (line.startsWith("3XMTHREADINFO1")) {
								i = line.indexOf("(native thread ID:");
								if (i >= 0) {
									int end = line.indexOf(",", i);
									if ((end >= 0) && (oldLine != null)
											&& (!oldLine.contains("Anonymous native thread"))) {
										try {
											td.nid[(totalThread - 1)] = jinwooDecode(
													line.substring(i + "(native thread ID:".length(), end));
										} catch (NumberFormatException e) {
											td.nid[(totalThread - 1)] = jinwooDecode(
													"0x" + line.substring(i + "(native thread ID:".length(), end));
										}
									}

								}

							} else if ((line.startsWith("3XMTHREADINFO "))
									&& (!line.contains("Anonymous native thread"))) {
								inAnonymous = false;
								i = line.indexOf(", state:");
								if (i != -1) {
									int index_nid = line.indexOf(", native ID:", i);
									String stateString = null;
									if (index_nid >= 0) {
										stateString = line.substring(i + ", state:".length(), index_nid);
										int end = line.indexOf(")", index_nid);
										if (end >= 0) {
											try {
												td.nid[totalThread] = jinwooDecode(
														line.substring(index_nid + ", native ID:".length(), end));
											} catch (NumberFormatException e) {
												td.nid[totalThread] = jinwooDecode("0x"
														+ line.substring(index_nid + ", native ID:".length(), end));
											}
										}
									} else {
										stateString = line.substring(i + ", state:".length(),
												line.indexOf(", prio=", i));
									}
									int stateInt = 4;
									if ((stateString.compareTo("R") == 0) || (stateString.compareTo("<21>") == 0)) {
										td.numberOfRunnable += 1;
										stateInt = 0;
									} else if ((stateString.compareTo("CW") == 0)
											|| (stateString.compareTo("<24>") == 0)
											|| (stateString.compareTo("<23>") == 0)) {
										stateInt = 1;
									} else if (stateString.compareTo("MW") == 0) {
										stateInt = 2;
									} else if (stateString.compareTo("S") == 0) {
										stateInt = 3;
									} else if (stateString.compareTo("P") == 0) {
										stateInt = 6;
									} else {
										stateInt = 5;
									}
									td.state[totalThread] = stateInt;

									j = line.indexOf(", sys_thread_t:");
									if (j != -1) {
										if (line.substring(j + ", sys_thread_t:".length(), i).startsWith("0x"))
											td.sys_thread[totalThread] = jinwooDecode(
													line.substring(j + ", sys_thread_t:".length(), i));
										else {
											td.sys_thread[totalThread] = jinwooDecode(
													"0x" + line.substring(j + ", sys_thread_t:".length(), i));
										}
									} else {
										j = line.indexOf(", j9thread_t:");
										if (j >= 0) {
											String checkNewFormat = line.substring(j + ", j9thread_t:".length(), i);
											int ci = checkNewFormat.indexOf(",");
											if (ci >= 0) {
												checkNewFormat = checkNewFormat.substring(0, ci);
											}
											if (checkNewFormat.startsWith("0x")) {
												td.sys_thread[totalThread] = jinwooDecode(checkNewFormat);
											} else {
												td.sys_thread[totalThread] = jinwooDecode("0x" + checkNewFormat);
											}
										}
									}

								}

								i = line.indexOf("\" (TID:");
								if (i != -1) {
									td.name[totalThread] = line.substring(line.indexOf("\"") + 1, i);
									try {
										td.tid[totalThread] = jinwooDecode(
												line.substring(i + "\" (TID:".length(), line.indexOf(",", i)));
									} catch (NumberFormatException e) {
										td.tid[totalThread] = jinwooDecode(
												"0x" + line.substring(i + "\" (TID:".length(), line.indexOf(",", i)));
									}

									totalThread++;
								} else {
									i = line.indexOf("\" J9VMThread:");
									if (i >= 0) {
										td.name[totalThread] = line.substring(line.indexOf("\"") + 1, i);
										try {
											td.tid[totalThread] = jinwooDecode(line
													.substring(i + "\" J9VMThread:".length(), line.indexOf(",", i)));
										} catch (NumberFormatException e) {
											td.tid[totalThread] = jinwooDecode("0x" + line
													.substring(i + "\" J9VMThread:".length(), line.indexOf(",", i)));
										}

										totalThread++;
									} else {
										i = line.indexOf("\" TID:");
										if (i >= 0) {
											td.name[totalThread] = line.substring(line.indexOf("\"") + 1, i);
											try {
												td.tid[totalThread] = jinwooDecode(
														line.substring(i + "\" TID:".length(), line.indexOf(",", i)));
											} catch (NumberFormatException e) {
												td.tid[totalThread] = jinwooDecode("0x"
														+ line.substring(i + "\" TID:".length(), line.indexOf(",", i)));
											}

											totalThread++;
										}
									}
								}
							} else if ((line.startsWith("3XMTHREADINFO "))
									&& (line.contains("Anonymous native thread"))) {
								inAnonymous = true;
							}

							if (line.startsWith("4XESTACKTRACE")) {
								i = line.indexOf("at ");
								if ((i != -1) && (totalThread != 0)) {
									if (td.javaStack[(totalThread - 1)] == null)
										td.javaStack[(totalThread - 1)] = new String();
									int tmp14358_14357 = (totalThread - 1);
									String[] tmp14358_14351 = td.javaStack;
									tmp14358_14351[tmp14358_14357] = (tmp14358_14351[tmp14358_14357] + line.substring(i)
											+ "<BR>");
								}
							} else if (line.startsWith("4XEMORENOTSHOWN")) {
								if (totalThread != 0) {
									if (td.javaStack[(totalThread - 1)] == null)
										td.javaStack[(totalThread - 1)] = new String();
									int tmp14448_14447 = (totalThread - 1);
									String[] tmp14448_14441 = td.javaStack;
									tmp14448_14441[tmp14448_14447] = (tmp14448_14441[tmp14448_14447]
											+ "... (more frames not shown in thread dump)<BR>");
								}
							} else if (line.startsWith("3XHSTACKLINE")) {
								i = line.indexOf("at ");
								if ((i != -1) && (totalThread != 0)) {
									if (td.nativeStack[(totalThread - 1)] == null)
										td.nativeStack[(totalThread - 1)] = new String();
									int tmp14545_14544 = (totalThread - 1);
									String[] tmp14545_14538 = td.nativeStack;
									tmp14545_14538[tmp14545_14544] = (tmp14545_14538[tmp14545_14544] + line.substring(i)
											+ "<BR>");
								}
							} else if (line.startsWith("3HPSTACKLINE")) {
								i = indexOfContent(line);

								if ((i != -1) && (totalThread != 0)) {
									if (td.nativeStack[(totalThread - 1)] == null)
										td.nativeStack[(totalThread - 1)] = new String();
									int tmp14650_14649 = (totalThread - 1);
									String[] tmp14650_14643 = td.nativeStack;
									tmp14650_14643[tmp14650_14649] = (tmp14650_14643[tmp14650_14649] + line.substring(i)
											+ "<BR>");
								}
							} else if ((line.startsWith("4XENATIVESTACK")) && (!inAnonymous)) {
								i = indexOfContent(line);

								if ((i != -1) && (totalThread != 0)) {
									if (td.nativeStack[(totalThread - 1)] == null)
										td.nativeStack[(totalThread - 1)] = new String();
									int tmp14760_14759 = (totalThread - 1);
									String[] tmp14760_14753 = td.nativeStack;
									tmp14760_14753[tmp14760_14759] = (tmp14760_14753[tmp14760_14759] + line.substring(i)
											+ "<BR>");
								}
							}

							oldLine = line;
							line = in.readLine();
							lineNumber += 1L;
							if (line != null)
								fileLocation += line.length();
							this.current = ((int) (100.0F * (float) lineNumber / (float) totalLine));

							this.overall = (30 + (int) (70.0F * (float) fileLocation / (float) fileSize));
						}
						if ((td.deadlock != null) && (td.deadlock.size() != 0)) {
							boolean inTID = true;
							ThreadDump tmp14892_14890 = td;
							tmp14892_14890.warning = (tmp14892_14890.warning
									+ "<LI style=\"color:red\"><b>***WARNING*** Deadlock detected in </b><br>");
							for (int q = 0; q < td.deadlock.size(); q++) {
								if (((Long) td.deadlock.get(q)).longValue() != -1L) {
									for (int r = 0; r < td.sys_thread.length; r++) {
										if (td.sys_thread[r] == ((Long) td.deadlock.get(q)).longValue()) {
											td.isDeadlock[r] = true;
											ThreadDump tmp14997_14995 = td;
											tmp14997_14995.warning = (tmp14997_14995.warning + "  [" + td.name[r]
													+ "]");
											inTID = false;
										}
									}
									if (inTID) {
										for (int r = 0; r < td.tid.length; r++) {
											if (td.tid[r] == ((Long) td.deadlock.get(q)).longValue()) {
												td.isDeadlock[r] = true;
												ThreadDump tmp15110_15108 = td;
												tmp15110_15108.warning = (tmp15110_15108.warning + "  [" + td.name[r]
														+ "]");
											}
										}
									}
								}
							}
							td.warning += "<BR><BR>";
						}
						if ((td.free != -1L) && (td.allocated != -1L) && (td.xmx != -1L)) {
							if ((float) td.allocated / (float) td.xmx > 0.9F) {
								td.javaHeap = ((int) (100L * td.free / td.xmx));

								if (td.javaHeap < 15) {
									ThreadDump tmp15304_15302 = td;
									tmp15304_15302.warning = (tmp15304_15302.warning
											+ "<LI style=\"color:red\"><b>***WARNING*** Java heap is almost exhausted : </b>"
											+ td.javaHeap
											+ "% free Java heap<BR>Please enable verbosegc trace and use IBM Pattern Modeling and Analysis Tool(http://www.alphaworks.ibm.com/tech/pmat) to analyze garbage collection activities.<BR>If heapdumps are generated at the same time, please use IBM HeapAnalyzer(http://www.alphaworks.ibm.com/tech/heapanalyzer) to analyze Java heap.<BR><BR>");
								}
							}
						}

						if ((heapBase != heapLimit) && (numberOfClasses != 0L)) {
							ThreadDump tmp15362_15360 = td;
							tmp15362_15360.summary = (tmp15362_15360.summary
									+ "<LI>Number of loaded classes in Java heap : "
									+ numberFormatter.format(numberOfClasses)
									+ "<BR><BR><LI>Recommended size of kCluster : greater than "
									+ numberFormatter.format((1.1F * (float) numberOfClasses)) + "<BR><BR>");
						} else if ((heapBase == -1L) && (numberOfClasses != 0L)) {
							ThreadDump tmp15450_15448 = td;
							tmp15450_15448.summary = (tmp15450_15448.summary
									+ "<LI>Number of loaded classes in Java heap : "
									+ numberFormatter.format(numberOfClasses) + "<BR><BR>");
						}
						if (numberOfClassLoader != 0L) {
							ThreadDump tmp15503_15501 = td;
							tmp15503_15501.summary = (tmp15503_15501.summary
									+ "<LI>Number of classloaders in Java heap : "
									+ numberFormatter.format(numberOfClassLoader) + "<BR><BR>");
							if (heapBase == heapLimit) {
								if (numberOfClassLoader >= 8192L) {
									ThreadDump tmp15566_15564 = td;
									tmp15566_15564.summary = (tmp15566_15564.summary
											+ "<LI>Recommended -Xmxcl setting (only for IBM Java 5.0, up to and including Service Refresh 4 (build date:February 1st ,2007)) : "
											+ numberFormatter.format((int) (numberOfClassLoader * 1.3D))
											+ " or greater<BR><BR>");
								}
								td.summary += "<P><LI>NOTE: Only for Java 5.0 Service Refresh 4 (build date:February 1st, 2007) and older. When you use delegated class loaders, the JVM can create a large number of ClassLoader objects. On IBM Java 5.0 Service Refresh 4 and older, the number of class loaders that are permitted is limited to 8192 by default and an OutOfMemoryError exception is thrown when this limit is exceeded. Use the -Xmxcl parameter to increase the number of class loaders allowed to avoid this problem, for example to 25000, by setting -Xmxcl25000, until the problem is resolved.<BR><BR>Please examine the current thread stack trace to check whether a class loader is being loaded if there is an OutOfMemoryError. For example, the following stack trace indicates that a class loader is being loaded:<BR><BR>at com/ibm/oti/vm/VM.initializeClassLoader(Native Method)<BR>at java/lang/ClassLoader.<init>(ClassLoader.java:120)</P><BR><BR>";
							}
						}

						if ((td.isJ9) && (td.javaHeap == -1) && (td.gcHistory != null)) {
							String newspaceSignature = "newspace=";
							String oldspaceSignature = "oldspace=";
							String loaspaceSignature = "loa=";
							long free = 0L;
							long total = 0L;
							i = td.gcHistory.indexOf(newspaceSignature);
							if (i >= 0) {
								String newspace = td.gcHistory.substring(i + newspaceSignature.length());
								j = newspace.indexOf("/");
								if (j >= 0) {
									free = Long.parseLong(newspace.substring(0, j));
									k = newspace.indexOf(" ");
									if (k >= 0) {
										total += Long.parseLong(newspace.substring(j + 1, k));
									}
								}
							}

							i = td.gcHistory.indexOf(oldspaceSignature);
							if (i >= 0) {
								String oldspace = td.gcHistory.substring(i + oldspaceSignature.length());
								j = oldspace.indexOf("/");
								if (j >= 0) {
									free += Long.parseLong(oldspace.substring(0, j));
									k = oldspace.indexOf(" ");
									if (k >= 0) {
										try {
											total += Long.parseLong(oldspace.substring(j + 1, k));
										} catch (NumberFormatException e) {
											System.out.println("Incorrect number " + oldspace.substring(j + 1, k)
													+ " in GC History.");
										}
									}
								}
							}

							i = td.gcHistory.indexOf(loaspaceSignature);
							if (i >= 0) {
								String loaspace = td.gcHistory.substring(i + loaspaceSignature.length());
								j = loaspace.indexOf("/");
								if (j >= 0) {
									free += Long.parseLong(loaspace.substring(0, j));
									k = loaspace.indexOf(" ");
									int m = loaspace.indexOf("\n");

									if (k < 0) {
										k = m;
									} else {
										int o = Math.min(k, m);
										k = o;
									}
									if (k >= 0) {
										total += Long.parseLong(loaspace.substring(j + 1, k));
									}
								}
							}

							if (td.free == -1L)
								td.free = free;
							if (td.allocated == -1L)
								td.allocated = total;
							if ((td.free != -1L) && (td.allocated != -1L)) {
								td.javaHeap = ((int) (100L * td.free / td.allocated));
							}

						}

						if ((td.isJ9) && (td.gcHistory != null)) {
							if (td.gc == -1L) {
								String globalSignature = "globalcount=";
								i = td.gcHistory.indexOf(globalSignature);

								if (i >= 0) {
									String global = td.gcHistory.substring(i + globalSignature.length());
									j = global.indexOf(" ");
									if (j >= 0) {
										td.gc = Long.parseLong(global.substring(0, j));
									}
								}
							}

							if (td.af == -1L) {
								String scaSignature = "scavengecount=";
								i = td.gcHistory.indexOf(scaSignature);

								if (i >= 0) {
									String sca = td.gcHistory.substring(i + scaSignature.length());

									j = sca.indexOf(" ");
									int m = sca.indexOf("\n");

									if (j < 0) {
										j = m;
									} else {
										int o = Math.min(j, m);
										j = o;
									}
									if (j >= 0) {
										td.af = Long.parseLong(sca.substring(0, j));
									}
								}
							}
						}

						if (td.currentTid != -1L) {
							int indexOfCause = td.summary.indexOf("Cause of thread dump");
							if (indexOfCause != -1) {
								int indexOfEnd = td.summary.substring(indexOfCause).indexOf("<BR><BR>");
								if (indexOfEnd != -1) {
									int resultIndex = td.summary.substring(indexOfCause, indexOfCause + indexOfEnd)
											.indexOf("java/lang/OutOfMemoryError");
									if (resultIndex != -1) {
										for (int m = 0; m < td.sys_thread.length; m++) {
											if (td.sys_thread[m] == td.currentTid) {
												int firstIndex = td.javaStack[m].indexOf("<BR>");
												if (firstIndex == -1)
													break;
												int foundIndex = td.javaStack[m].substring(0, firstIndex)
														.indexOf("com/ibm/oti/vm/VM.initializeClassLoader");
												if (foundIndex == -1)
													break;
												if ((numberOfClassLoader != 0L) && (heapBase == heapLimit)) {
													td.summary = ("***ERROR*** The failure was caused because the class loader limit(-Xmxcl) was exceeded. Please set -Xmxcl to "
															+ numberFormatter.format((int) (numberOfClassLoader * 1.3D))
															+ " or greater<BR><BR>" + td.summary);

													break;
												}

												td.summary = ("***ERROR*** The failure was caused because the class loader limit(-Xmxcl) was exceeded. Please set  -Xmxcl larger than the number of class loaders.<BR><BR>"
														+ td.summary);

												break;
											}
										}
									}
								}
							}

						}

						td.summary += commandLine;
						if (headless)
							threadInfo.threadDumps.add(td);
						else {
							this.ha.ti.threadDumps.add(td);
						}
					} else {
						while (line != null) {
							if (line.indexOf("Full thread dump") >= 0) {
								if (td != null) {
									if (headless)
										threadInfo.threadDumps.add(td);
									else
										this.ha.ti.threadDumps.add(td);
									td = null;
								}
								int threadSize = ((Integer) list.get(dumpIndex++)).intValue();

								td = new ThreadDump();
								td.xmx = -1L;
								td.javaHeap = -1;
								td.warning = "";
								td.name = new String[threadSize];
								td.nid = new long[threadSize];
								td.state = new int[threadSize];
								td.isDeadlock = new boolean[threadSize];
								td.priority = new int[threadSize];
								td.javaStack = new String[threadSize];
								td.macro = new int[threadSize];
								td.pattern = new String[threadSize];
								td.nativeStack = new String[threadSize];
								td.sys_thread = new long[threadSize];
								td.tid = new long[threadSize];
								td.summary = ("<LI>File name : " + file[z].getAbsolutePath() + "<BR><BR>");
								totalThread = 0;
								td.fileName = (file[z].getName() + "_" + dumpIndex);
								td.pid = -1L;
								td.free = -1L;
								td.allocated = -1L;
								td.af = -1L;
								td.gc = -1L;
								td.timeStamp = -1L;
								Date d = null;
								if (oldLine != null) {
									oldLine = oldLine.trim();
									if ((oldLine.length() > 0) && (oldLine.charAt(0) >= '0')
											&& (oldLine.charAt(0) <= '9')) {
										try {
											d = formatterSolaris.parse(oldLine);
										} catch (Exception e) {
											e.printStackTrace();
										}
										if (d != null) {
											td.timeStamp = d.getTime();
											ThreadDump tmp17125_17123 = td;
											tmp17125_17123.summary = (tmp17125_17123.summary + "<LI>Timestamp : "
													+ formatterSolaris.format(d) + "<BR><BR>");
										}

									}

								}

							} else if ((line.startsWith("\"")) && (line.indexOf(" prio=") >= 0)) {
								i = line.indexOf(" tid=");
								if (i != -1) {
									td.name[totalThread] = line.substring(1, line.indexOf("\"", 1));
									try {
										td.tid[totalThread] = jinwooDecode(
												line.substring(i + " tid=".length(), line.indexOf(" n", i)));

										td.sys_thread[totalThread] = td.tid[totalThread];
									} catch (NumberFormatException e) {
										td.tid[totalThread] = jinwooDecode(
												"0x" + line.substring(i + " tid=".length(), line.indexOf(" n", i)));

										td.sys_thread[totalThread] = td.tid[totalThread];
									}

								}

								i = line.indexOf(" nid=");
								if (i != -1) {
									td.nid[totalThread] = Integer
											.decode(line.substring(i + " nid=".length(), line.indexOf(" ", i + 1)))
											.intValue();

									String stateString = line.substring(line.indexOf(" ", i + 1) + 1);

									if (stateString.startsWith("lwp_id=")) {
										stateString = stateString.substring(stateString.indexOf(" ") + 1);
									}
									int stateInt = 4;
									if (stateString.startsWith("runnable")) {
										td.numberOfRunnable += 1;
										stateInt = 0;
									} else if (stateString.startsWith("waiting on condition")) {
										stateInt = 1;
									} else if ((stateString.startsWith("waiting on monitor"))
											|| (stateString.startsWith("waiting for monitor"))) {
										stateInt = 2;
									} else if (stateString.startsWith("suspended")) {
										stateInt = 3;
									} else if (stateString.startsWith("in Object.wait")) {
										stateInt = 4;
									} else {
										stateInt = 5;
									}
									td.state[totalThread] = stateInt;
								}

								totalThread++;
							}

							if (line.startsWith("\tat ")) {
								i = line.indexOf("at ");
								if ((i != -1) && (totalThread != 0)) {
									if (td.javaStack[(totalThread - 1)] == null)
										td.javaStack[(totalThread - 1)] = new String();
									int tmp17659_17658 = (totalThread - 1);
									String[] tmp17659_17652 = td.javaStack;
									tmp17659_17652[tmp17659_17658] = (tmp17659_17652[tmp17659_17658] + line.substring(i)
											+ "<BR>");
								}

							} else if ((line.startsWith("\t- locked <")) || (line.startsWith("\t- waiting on <"))
									|| (line.startsWith("\t- waiting to lock <"))) {
								i = line.indexOf("- ");
								if ((i != -1) && (totalThread != 0)) {
									if (td.javaStack[(totalThread - 1)] == null)
										td.javaStack[(totalThread - 1)] = new String();
									String newStr = line.substring(i);
									newStr = newStr.replace('<', '[');
									newStr = newStr.replace('>', ']');
									int tmp17819_17818 = (totalThread - 1);
									String[] tmp17819_17812 = td.javaStack;
									tmp17819_17812[tmp17819_17818] = (tmp17819_17812[tmp17819_17818] + newStr + "<BR>");
								}

							} else if (!line.startsWith("FOUND A JAVA LEVEL DEADLOCK:")) {
								if (!line.startsWith("Found one Java-level deadlock:"))
									;
							} else {
								while ((line != null)
										&& ((!line.startsWith("Found ")) || (!line.endsWith(" deadlock.")))
										&& ((!line.startsWith("Found ")) || (!line.endsWith(" deadlocks.")))) {
									if (line.startsWith("\"")) {
										i = line.indexOf("\":");
										if (i != -1) {
											if (td.deadlock == null)
												td.deadlock = new ArrayList<Object>();
											String tempS = line.substring(1, i);
											if (!td.deadlock.contains(tempS)) {
												td.deadlock.add(tempS);
											}
										}
									} else if (!line.startsWith("  waiting to lock monitor 0x")) {
										if (!line.startsWith("  which is locked by \""))
											line.startsWith("  which is held by \"");
									}
									oldLine = line;
									line = in.readLine();
									lineNumber += 1L;
								}
								if ((td.deadlock != null) && (td.deadlock.size() != 0)) {
									ThreadDump tmp18080_18078 = td;
									tmp18080_18078.warning = (tmp18080_18078.warning
											+ "<LI style=\"color:red\"><b>***WARNING*** Deadlock detected in </b><br>");
									for (int q = 0; q < td.deadlock.size(); q++) {
										ThreadDump tmp18115_18113 = td;
										tmp18115_18113.warning = (tmp18115_18113.warning + "  [" + td.deadlock.get(q)
												+ "]");
									}
									td.warning += "<BR><BR>";
								}

							}

							oldLine = line;
							line = in.readLine();
							lineNumber += 1L;
							if (line != null)
								fileLocation += line.length();
							this.current = ((int) (100.0F * (float) lineNumber / (float) totalLine));

							this.overall = (30 + (int) (70.0F * (float) fileLocation / (float) fileSize));
						}

						if (td != null) {
							if (headless)
								threadInfo.threadDumps.add(td);
							else {
								this.ha.ti.threadDumps.add(td);
							}
						}
					}
					in.close();

					if (this.monList.size() == 0) {
						td.mdump = null;
					} else {
						td.mdump = ((MonitorDump[]) this.monList.toArray(new MonitorDump[this.monList.size()]));

						if (td.isNewFormat) {
							for (i = 0; i < td.mdump.length; i++) {
								if (td.mdump[i].waiting != null) {
									long temp = td.getSys_ThreadFromTID(td.mdump[i].owner);
									if (this.debug)
										System.out.println("temp=" + temp + " owner=" + td.mdump[i].owner + " waiting="
												+ td.mdump[i].waiting);

									if (temp != -1L) {
										td.mdump[i].owner = temp;
										List<Long> newList = new ArrayList<Long>();
										for (j = 0; j < td.mdump[i].waiting.size(); j++) {
											newList.add(new Long(td.getSys_ThreadFromTID(
													((Long) td.mdump[i].waiting.get(j)).longValue())));
										}
										td.mdump[i].waiting = newList;
									}
								}
							}

						}

					}

				} catch (Exception e) {
					System.out.println("Exception while parsing line " + numberFormatter.format(lineNumber) + " : "
							+ line + " in " + file[z].getName());
					if (!headless) {
						this.ha.handleException(e);
					}
					continue;
				}

				this.monList = null;
				this.threadIdent.clear();
				this.threadIdent = null;
			}
		}

		if (!headless)
			this.jp.dispose();
		this.done = true;

		if (!headless)
			this.ha.threadDumpTable.updateUI();
		int size;
		if (headless)
			size = threadInfo.threadDumps.size();
		else {
			size = this.ha.ti.threadDumps.size();
		}
		for (i = 0; i < size; i++) {
			ThreadDump td;
			if (headless)
				td = (ThreadDump) threadInfo.threadDumps.get(i);
			else
				td = (ThreadDump) this.ha.ti.threadDumps.get(i);
			if (!td.isIBM) {
				this.monIndex = 0;
				int nameIndex = 0;
				List<String> nameList = new ArrayList<String>();
				this.monList = new ArrayList<MonitorDump>();

				for (j = 0; j < td.javaStack.length; j++)
					if (td.javaStack[j] != null) {
						k = td.javaStack[j].indexOf("- waiting to lock [");
						String tempString = td.javaStack[j];
						while (k != -1) {
							tempString = tempString.substring(k + "- waiting to lock <".length());

							int k2 = tempString.indexOf("] ");
							if (k2 == -1)
								break;
							String name = tempString.substring(0, k2);

							if (!nameList.contains(name)) {
								nameList.add(nameIndex++, name);
								MonitorDump mon = new MonitorDump(name, -1L);
								mon.waiting = new ArrayList<Long>();
								this.monList.add(this.monIndex++, mon);
								mon.waiting.add(new Long(td.tid[j]));
							} else {
								int k3 = nameList.indexOf(name);
								MonitorDump mon = (MonitorDump) this.monList.get(k3);
								if (mon.waiting == null)
									mon.waiting = new ArrayList<Long>();
								mon.waiting.add(new Long(td.tid[j]));
							}
							k = tempString.indexOf("- waiting to lock [");
						}
					}
				for (j = 0; j < td.javaStack.length; j++) {
					if (td.javaStack[j] != null) {
						k = td.javaStack[j].indexOf("- locked [");

						String tempString = td.javaStack[j];
						while (k != -1) {
							tempString = tempString.substring(k + "- locked <".length());

							int k2 = tempString.indexOf("] ");
							if (k2 == -1)
								break;
							String name = tempString.substring(0, k2);
							if (nameList.contains(name)) {
								int k3 = nameList.indexOf(name);

								MonitorDump mon = (MonitorDump) this.monList.get(k3);
								mon.owner = td.tid[j];
								if ((td.deadlock != null) && (td.deadlock.contains(td.name[j]))) {
									td.isDeadlock[j] = true;
								}
							}

							k = tempString.indexOf("- locked [");
						}
					}
				}
				if (this.monList.size() == 0)
					td.mdump = null;
				else {
					td.mdump = ((MonitorDump[]) this.monList.toArray(new MonitorDump[this.monList.size()]));
				}
			}
			for (int x = 0; x < td.javaStack.length; x++) {
				checkIdle(td.javaStack[x], x, td);
			}

			if (td.sortedArray == null) {
				if (cfg.verbose)
					System.out.println(
							"Requesting " + numberFormatter.format(td.sys_thread.length * 16) + " bytes of Java heap.");
				td.sortedArray = new long[2][td.sys_thread.length];
				for (j = 0; j < td.sys_thread.length; j++) {
					td.sortedArray[0][j] = td.sys_thread[j];
					td.sortedArray[1][j] = j;
				}
				if (cfg.verbose)
					System.out.println("Sorting table by System Thread ID.");
				Arrays2.sort(td.sortedArray);
			}

			if (td.stateArray == null) {
				if (cfg.verbose)
					System.out.println(
							"Requesting " + numberFormatter.format(td.sys_thread.length * 8) + " bytes of Java heap.");
				td.stateArray = new int[td.state.length];
				int[][] tempArray = new int[2][td.state.length];
				for (j = 0; j < td.state.length; j++) {
					tempArray[0][j] = td.state[j];
					tempArray[1][j] = j;
				}
				if (cfg.verbose)
					System.out.println("Sorting table by State.");
				Arrays2.sort(tempArray);
				for (j = 0; j < td.state.length; j++) {
					td.stateArray[j] = tempArray[1][j];
				}
				tempArray = (int[][]) null;
			}

			if (td.idArray == null) {
				if (cfg.verbose)
					System.out.println(
							"Requesting " + numberFormatter.format(td.nid.length * 8) + " bytes of Java heap.");
				td.idArray = new int[td.nid.length];
				long[][] tempArray = new long[2][td.nid.length];
				for (j = 0; j < td.nid.length; j++) {
					tempArray[0][j] = td.nid[j];
					tempArray[1][j] = j;
				}
				if (cfg.verbose)
					System.out.println("Sorting table by Native ID.");
				Arrays2.sort(tempArray);
				for (j = 0; j < td.nid.length; j++) {
					td.idArray[j] = ((int) tempArray[1][j]);
				}

				tempArray = (long[][]) null;
			}

			if (td.nameArray == null) {
				td.nameArray = new int[td.tid.length];
				String EMPTY = "";
				String[] tempString = new String[td.name.length];
				for (j = 0; j < td.nameArray.length; j++) {
					tempString[j] = td.name[j];
					if (tempString[j] == null)
						tempString[j] = EMPTY;
					td.nameArray[j] = j;
				}

				Arrays2.sort(tempString, td.nameArray);

				tempString = (String[]) null;
			}
			if (td.currentMethodArray == null) {
				td.currentMethodArray = new int[td.tid.length];
				String[] tempString = new String[td.name.length];
				for (j = 0; j < td.currentMethodArray.length; j++) {
					tempString[j] = td.getCurrentMethod(j);
					td.currentMethodArray[j] = j;
				}
				Arrays2.sort(tempString, td.currentMethodArray);
				tempString = (String[]) null;
			}
		}

		if (headless) {
			return generateReport(threadInfo);
		}
		return null;
	}

	public void stop() {
		this.statMessage = null;
	}

	class ActualTask {
		ActualTask() {
			FileTask.numberFormatter.setMaximumFractionDigits(2);
			FileTask.this.processThreadDump(FileTask.this.file, FileTask.this.cfg, false);
		}
	}
}
