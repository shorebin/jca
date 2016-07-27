 package com.ibm.jinwoo.thread;
 
 import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;
 
 public class ConsolePrintStream extends PrintStream
 {
   JTextArea text;
 
   public ConsolePrintStream(OutputStream out)
   {
     super(out);
   }
 
   public ConsolePrintStream(OutputStream out, JTextArea jt)
   {
     super(out);
     this.text = jt;
   }
 
   public ConsolePrintStream(OutputStream out, boolean autoFlush)
   {
     super(out, autoFlush);
   }
 
   public ConsolePrintStream(JTextArea jt)
   {
     super(new PipedOutputStream());
     this.text = jt;
   }
 
   public void print(char[] c) {
     if (c != null) this.text.append(new String(c, 0, c.length));
     scroll();
   }
 
   public void print(char c) {
     this.text.append(c + "");
     scroll();
   }
 
   public void print(double d) {
     this.text.append(d + "");
     scroll();
   }
 
   public void print(float f) {
     this.text.append(f + "");
     scroll();
   }
 
   public void print(int i) {
     this.text.append(i + "");
     scroll();
   }
 
   public void print(Object obj) {
     if (obj != null) this.text.append(obj.toString()); else
       this.text.append("null");
     scroll();
   }
 
   public void print(String str) {
     if (str != null) this.text.append(str); else
       this.text.append("null");
     scroll();
   }
 
   public void print(boolean b) {
     this.text.append(b + "");
     scroll();
   }
 
   public void println(char[] c) {
     if (c != null) this.text.append(new String(c, 0, c.length) + "\n");
     scroll();
   }
 
   public void println(char c) {
     this.text.append(c + "\n");
     scroll();
   }
 
   public void println(double d) {
     this.text.append(d + "\n");
     scroll();
   }
 
   public void println(float f) {
     this.text.append(f + "\n");
     scroll();
   }
 
   public void println(int i) {
     this.text.append(i + "\n");
     scroll();
   }
 
   public void println(Object obj) {
     if (obj != null) this.text.append(obj.toString() + "\n"); else
       this.text.append("null\n");
     scroll();
   }
 
   public void println(String str) {
     if (str != null) this.text.append(str + "\n"); else
       this.text.append("null\n");
     scroll();
   }
 
   public void println(boolean b) {
     this.text.append(b + "\n");
     scroll();
   }
   private void scroll() {
     this.text.setCaretPosition(this.text.getDocument().getLength());
   }
 }

