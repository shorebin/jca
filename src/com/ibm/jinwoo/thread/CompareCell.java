 package com.ibm.jinwoo.thread;
 
 public class CompareCell
 {
   ThreadDump[] threadDump;
   int index;
   int threadIndex;
   Long tid;
 
   public CompareCell()
   {
   }
 
   public CompareCell(ThreadDump[] td, int idx, int thread, Long ti)
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

