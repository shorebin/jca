package com.ibm.jinwoo.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileTaskExt {

	public ThreadInfo ti = new ThreadInfo();

	private static final String MEMUSER = "MEMUSER";
	static final String BTT = "<p><a href=\"#top\">Back to top</a></p>";
	static NumberFormat nf = NumberFormat.getNumberInstance();
	int fileSequenceNumber;
	String outputHTMLFileName = null;
	boolean debug = false;
	int monIndex = 0;
	List<MonitorDump> monList = new ArrayList<MonitorDump>();
	Hashtable<Long, Long> threadIdent = new Hashtable<Long, Long>();
	public static final int SIZE_OF_LARGE_OBJECT = 900000;
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
	public File[] file = null;
	GCInfo gi = null;
	boolean isJavacore = false;

	public FileTaskExt(File[] f) {
		this.file = f;
		processThreadDump(f);
	}

	private int getNumberOfChar(String str, char c) {
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

	boolean hasOOM(int x, int y) {
		if (this.gi.outOfHeapSpace == null)
			return false;
		for (int i = 0; i < this.gi.outOfHeapSpace.length; i++) {
			if ((x <= this.gi.outOfHeapSpace[i]) && (y >= this.gi.outOfHeapSpace[i]))
				return true;
		}
		return false;
	}

	private int indexOfContent(String str) {
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

	private static long jinwooDecode(String ad) throws NumberFormatException {
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

	private String processThreadDump(File[] file) {
		Date dt = new Date();

		String commandLine = new String();
		long tmpL = 0L;
		long numberOfClasses = 0L;
		long numberOfClassLoader = 0L;
		long heapLimit = -1L;
		long heapBase = -1L;

		long lineNumber = 0L;

		this.gi = new GCInfo();

		String line = new String();

		int i = 0;
		int j = 0;
		int k = 0;

		int totalThread = 0;
		int totalThreadDump = -1;

		for (int z = 0; z < file.length; z++) {
			boolean canCount = false;
			lineNumber = 0L;
			this.monIndex = 0;
			numberOfClasses = numberOfClassLoader = 0L;
			this.monList = new ArrayList<MonitorDump>();
			this.threadIdent = new Hashtable<Long, Long>();

			totalThread = 0;
			totalThreadDump = -1;
			ArrayList<Integer> list = new ArrayList<Integer>();
			try {
				if (!file[z].exists()) {
					continue;
				}

				BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream(file[z])));
				line = in.readLine();
				lineNumber += 1L;
				if (line != null) {
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
					}
				}

				in.close();
			} catch (Exception e) {
				System.out.println("Exception while parsing line " + numberFormatter.format(lineNumber) + " : " + line
						+ " in " + file[z].getName());
				this.handleException(e);
				continue;
			}
			BufferedReader in;
			if (totalThread == 0) {

			} else {
				String oldLine = null;
				try {
					lineNumber = 0L;

					in = new BufferedReader(new InputStreamReader(getInputStream(file[z])));
					line = in.readLine();
					lineNumber += 1L;
					if (line != null) {
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
						this.ti.threadDumps.add(td);
					} else {
						while (line != null) {
							if (line.indexOf("Full thread dump") >= 0) {
								if (td != null) {
									this.ti.threadDumps.add(td);
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
						}

						if (td != null) {
							this.ti.threadDumps.add(td);
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
					this.handleException(e);
					continue;
				}

				this.monList = null;
				this.threadIdent.clear();
				this.threadIdent = null;
			}
		}

		int size;
		size = this.ti.threadDumps.size();
		for (i = 0; i < size; i++) {
			ThreadDump td;
			td = (ThreadDump) this.ti.threadDumps.get(i);
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
				td.sortedArray = new long[2][td.sys_thread.length];
				for (j = 0; j < td.sys_thread.length; j++) {
					td.sortedArray[0][j] = td.sys_thread[j];
					td.sortedArray[1][j] = j;
				}
				Arrays2.sort(td.sortedArray);
			}

			if (td.stateArray == null) {
				td.stateArray = new int[td.state.length];
				int[][] tempArray = new int[2][td.state.length];
				for (j = 0; j < td.state.length; j++) {
					tempArray[0][j] = td.state[j];
					tempArray[1][j] = j;
				}
				Arrays2.sort(tempArray);
				for (j = 0; j < td.state.length; j++) {
					td.stateArray[j] = tempArray[1][j];
				}
				tempArray = (int[][]) null;
			}

			if (td.idArray == null) {
				td.idArray = new int[td.nid.length];
				long[][] tempArray = new long[2][td.nid.length];
				for (j = 0; j < td.nid.length; j++) {
					tempArray[0][j] = td.nid[j];
					tempArray[1][j] = j;
				}
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

		return null;
	}

	private long getXmx(String str) {
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

	private void checkIdle(String st, int index, ThreadDump td) {
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

	private void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
	}

	private static InputStream getInputStream(File file) throws IOException {
		if (file.getName().endsWith(".gz")) {
			try {
				return new GZIPInputStream(new FileInputStream(file));
			} catch (FileNotFoundException ex) {
				throw ex;
			} catch (IOException e) {
				return new FileInputStream(file);
			}
		}
		return new FileInputStream(file);
	}

	class ActualTask {
		ActualTask() {
			FileTaskExt.numberFormatter.setMaximumFractionDigits(2);
			FileTaskExt.this.processThreadDump(FileTaskExt.this.file);
		}
	}
}
