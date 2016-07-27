 package com.ibm.jinwoo.thread;

public class Monitor
 {
   public boolean dup;
   public boolean visited;
   public boolean isPop;
   public int owner;
   public String objectName;
   public String threadName;
   public boolean isHeapLock;
   public boolean waitingHeapLock;
   boolean isDeadlock;
 
 
   public boolean isPop() {
     return this.isPop;
   }
   public String toString() {
     return this.threadName + "(" + this.objectName + ")" + this.owner;
   }
 }

