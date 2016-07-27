package com.ibm.jinwoo.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

public class AnalyzerHeadless {
	String outputHTMLFileName;
	public Configuration cfg;
	public static final String HEADER = "<B>IBM Thread and Monitor Dump Analyzer for Java Technology</B> Version 4.0.1<BR>Architected & Developed by Jinwoo Hwang (jinwoo@us.ibm.com)<BR>(c) Copyright IBM Corp. 2005-2008 All Rights Reserved.<BR>";
	public static final String CSS = "<style type=\"text/css\">\n #stacktrace_id\n{\nfilter: progid:DXImageTransform.Microsoft.Shadow( color=gray , direction=130 ); \n visibility: hidden; \n position: absolute; \n width: 950px; \nborder: 3px solid blue; \n padding: 4px; \n background-color: lightyellow; \n z-index: 10; \n } \n  .parent \n { \n cursor: hand; \n cursor: pointer; \n } \n .children \n { \n cursor: hand; \n margin-left: 10px; \n display: none; \n } \t</style>";
	public static final String stackScript = "<div id=\"stacktrace_id\"></div><script type=\"text/javascript\"> \n var enabletip=false \n var stackTraceElement=document.getElementById(\"stacktrace_id\") \n  function showStackTrace(text){ \n if(enabled==\"0\") return false; \n stackTraceElement.innerHTML= text \n enabletip=true  \n return false \n} \n function tiplocation(e){ \n if (enabletip){  \n var newX=(document.all)? event.clientX+document.body.scrollLeft : e.pageX-50; \n var newY=(document.all)? event.clientY+document.body.scrollTop : e.pageY+30; \n var limitX =(document.all)? document.body.clientWidth-event.clientX+50 : window.innerWidth-e.clientX+30; \n var limitY =(document.all)? document.body.clientHeight-event.clientY-20 : window.innerHeight-e.clientY-40; \n if (limitX<stackTraceElement.offsetWidth) \n newX= (document.all)? document.body.scrollLeft+event.clientX-stackTraceElement.offsetWidth : window.pageXOffset+e.clientX-stackTraceElement.offsetWidth; \n if (limitY<stackTraceElement.offsetHeight) \n newY=(document.all)? document.body.scrollTop+event.clientY-stackTraceElement.offsetHeight-20 : window.pageYOffset+e.clientY-stackTraceElement.offsetHeight-20; \n  if(document.all){ \n   if(newX<document.body.scrollLeft) newX=document.body.scrollLeft + 10; \n   if(newY<document.body.scrollTop) newY=document.body.scrollTop + 10; \n  } else { \n   if(newX<window.pageXOffset) newX=window.pageXOffset + 10; \n   if(newY<window.pageYOffset) newY=window.pageYOffset + 10; \n  } \n stackTraceElement.style.left=newX+\"px\" \n stackTraceElement.style.top=newY+\"px\" \n stackTraceElement.style.visibility=\"visible\"  \n } \n }  \n var enabled=\"1\"; \n  function flip(){ \n   if(enabled==\"1\") enabled =\"0\"; \n  else enabled=\"1\"; \n  }\n function hideStackTrace(){  \n enabletip=false \n stackTraceElement.style.visibility=\"hidden\" \n }  \n function stayMenu(){ \n  if(document.all) { checkMenu.style.pixelTop=document.body.scrollTop+10; \n checkMenu.style.pixelLeft=document.body.scrollLeft+document.body.clientWidth-checkMenu.offsetWidth-80; } \n  else { \n document.getElementById(\"checkMenu\").style.top=window.pageYOffset+10; \n document.getElementById(\"checkMenu\").style.left=window.pageXOffset+window.innerWidth-checkMenu.offsetWidth-80; \n } \n  setTimeout(\"stayMenu()\",0); \n  } \n document.onmousemove=tiplocation \n window.onload=stayMenu \n</script>";

	public AnalyzerHeadless() {
	}

	public AnalyzerHeadless(String outputHTMLFileName) {
		this.outputHTMLFileName = outputHTMLFileName;
	}

	public void handleException(Throwable exception) {
		exception.printStackTrace(System.out);
	}

	String processThreadDump(String[] inputFileNames, Properties prop, int fileSequenceNumber) {
		File[] file = new File[inputFileNames.length];
		for (int i = 0; i < inputFileNames.length; i++) {
			file[i] = new File(inputFileNames[i]);
		}

		FileTask ft = new FileTask(null, null, fileSequenceNumber, this.outputHTMLFileName, this.cfg);
		return ft.processThreadDump(file, this.cfg, true);
	}

	public void readConfiguration(String cfile) {
		File file = new File(cfile);
		if (!file.exists()) {
			this.cfg = new Configuration();
		} else
			try {
				GZIPInputStream gs = new GZIPInputStream(new FileInputStream(file));
				ObjectInputStream ois = new ObjectInputStream(gs);
				this.cfg = ((Configuration) ois.readObject());
				ois.close();
				gs.close();
			} catch (Exception e) {
				handleException(e);
				this.cfg = new Configuration();
				System.out.println("Cannot understand configuration file :" + file.getAbsoluteFile());
			}
	}

	public void threadAnalysis(String[] inputFileNames, Properties prop, String outputHTMLFileName) {
		if (this.cfg == null)
			this.cfg = new Configuration();
		String outputString = "";
		outputString = processThreadDump(inputFileNames, prop, 0);

		outputString = "<HTML><HEAD><style type=\"text/css\">\n #stacktrace_id\n{\nfilter: progid:DXImageTransform.Microsoft.Shadow( color=gray , direction=130 ); \n visibility: hidden; \n position: absolute; \n width: 950px; \nborder: 3px solid blue; \n padding: 4px; \n background-color: lightyellow; \n z-index: 10; \n } \n  .parent \n { \n cursor: hand; \n cursor: pointer; \n } \n .children \n { \n cursor: hand; \n margin-left: 10px; \n display: none; \n } \t</style><TITLE>IBM Thread and Monitor Dump Analyzer for Java Technology</TITLE><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" ></HEAD><BODY><span ID=\"checkMenu\" style=\"filter: progid:DXImageTransform.Microsoft.Shadow( color=gray , direction=130 ); width: 200px;padding: 2px; border: 3px solid blue;background-color:Yellow;Position:absolute; Top:10; Right:80; Z-Index:2;cursor:hand;\"><input onclick=\"flip()\" type=\"checkbox\" CHECKED id=\"setting\" value=\"on\"/>Show Stack Trace</span><H1>Java Thread and Monitor Dump Analysis</H1><B>IBM Thread and Monitor Dump Analyzer for Java Technology</B> Version 4.0.1<BR>Architected & Developed by Jinwoo Hwang (jinwoo@us.ibm.com)<BR>(c) Copyright IBM Corp. 2005-2008 All Rights Reserved.<BR><div id=\"stacktrace_id\"></div><script type=\"text/javascript\"> \n var enabletip=false \n var stackTraceElement=document.getElementById(\"stacktrace_id\") \n  function showStackTrace(text){ \n if(enabled==\"0\") return false; \n stackTraceElement.innerHTML= text \n enabletip=true  \n return false \n} \n function tiplocation(e){ \n if (enabletip){  \n var newX=(document.all)? event.clientX+document.body.scrollLeft : e.pageX-50; \n var newY=(document.all)? event.clientY+document.body.scrollTop : e.pageY+30; \n var limitX =(document.all)? document.body.clientWidth-event.clientX+50 : window.innerWidth-e.clientX+30; \n var limitY =(document.all)? document.body.clientHeight-event.clientY-20 : window.innerHeight-e.clientY-40; \n if (limitX<stackTraceElement.offsetWidth) \n newX= (document.all)? document.body.scrollLeft+event.clientX-stackTraceElement.offsetWidth : window.pageXOffset+e.clientX-stackTraceElement.offsetWidth; \n if (limitY<stackTraceElement.offsetHeight) \n newY=(document.all)? document.body.scrollTop+event.clientY-stackTraceElement.offsetHeight-20 : window.pageYOffset+e.clientY-stackTraceElement.offsetHeight-20; \n  if(document.all){ \n   if(newX<document.body.scrollLeft) newX=document.body.scrollLeft + 10; \n   if(newY<document.body.scrollTop) newY=document.body.scrollTop + 10; \n  } else { \n   if(newX<window.pageXOffset) newX=window.pageXOffset + 10; \n   if(newY<window.pageYOffset) newY=window.pageYOffset + 10; \n  } \n stackTraceElement.style.left=newX+\"px\" \n stackTraceElement.style.top=newY+\"px\" \n stackTraceElement.style.visibility=\"visible\"  \n } \n }  \n var enabled=\"1\"; \n  function flip(){ \n   if(enabled==\"1\") enabled =\"0\"; \n  else enabled=\"1\"; \n  }\n function hideStackTrace(){  \n enabletip=false \n stackTraceElement.style.visibility=\"hidden\" \n }  \n function stayMenu(){ \n  if(document.all) { checkMenu.style.pixelTop=document.body.scrollTop+10; \n checkMenu.style.pixelLeft=document.body.scrollLeft+document.body.clientWidth-checkMenu.offsetWidth-80; } \n  else { \n document.getElementById(\"checkMenu\").style.top=window.pageYOffset+10; \n document.getElementById(\"checkMenu\").style.left=window.pageXOffset+window.innerWidth-checkMenu.offsetWidth-80; \n } \n  setTimeout(\"stayMenu()\",0); \n  } \n document.onmousemove=tiplocation \n window.onload=stayMenu \n</script><BR><TABLE WIDTH=\"100%\"><TR><TD WIDTH=\"0%\"></TD><TD WIDTH=\"100%\">Report date and time: "
				+ new Date() + "</TD>" + "</TR>" + "<TR><TD WIDTH=\"0%\"></TD>"
				+ "<TD WIDTH=\"100%\">Problem Type for Analysis: Hang, Deadlock and Performance Degradation Problems"
				+ "</TD>" + "</TR>" + "<TR><TD WIDTH=\"0%\"></TD>"
				+ "<TD WIDTH=\"100%\">Please run a <a href=\"http://www.alphaworks.ibm.com/tech/jca\" target=\"_blank\">standalone version of IBM Thread and Monitor Dump Analyzer for Java Technology</a> if you need interactive interfaces or more information"
				+ "</TD>" + "</TR>" + "</TABLE>" + "<BR>" + outputString + "</html>";
		File outputHTMLFile = new File(outputHTMLFileName);
		Writer outputWriter = null;
		try {
			outputWriter = new BufferedWriter(new FileWriter(outputHTMLFile));
			outputWriter.write(outputString);
			outputWriter.close();
		} catch (Exception e) {
			handleException(e);

			if (outputWriter != null)
				try {
					outputWriter.close();
				} catch (Exception ex) {
					handleException(ex);
				}
		} finally {
			if (outputWriter != null) {
				try {
					outputWriter.close();
				} catch (Exception e) {
					handleException(e);
				}

			}

		}

		String[] output = { "open.gif", "close.gif", "deadlock.gif", "leaf.gif" };

		File parent = new File(outputHTMLFileName);
		File path = parent.getParentFile();
		byte[] buffer = new byte[10240];

		for (int i = 0; i < output.length; i++)
			try {
				InputStream in = getClass().getResourceAsStream("/" + output[i]);
				File outputFile = new File(path, output[i]);
				OutputStream out = new FileOutputStream(outputFile);
				int bytesRead;
				while ((bytesRead = in.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
