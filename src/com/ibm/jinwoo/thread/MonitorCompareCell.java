 package com.ibm.jinwoo.thread;
 
 public class MonitorCompareCell
 {
   ThreadDump[] threadDump;
   int index;
   int threadIndex;
   Long tid;
 
   public MonitorCompareCell()
   {
   }
 
   public MonitorCompareCell(ThreadDump[] td, int idx, int thread, Long ti)
   {
     this.threadDump = td;
     this.index = idx;
     this.threadIndex = thread;
     this.tid = ti;
   }
 
   public String toString() {
     return this.threadDump[this.index].getCurrentMethod(this.threadIndex);
   }
 }

