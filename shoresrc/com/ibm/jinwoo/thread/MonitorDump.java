 package com.ibm.jinwoo.thread;
 
 import java.util.List;
 
 public class MonitorDump
 {
   public String objectName;
   public String threadName;
   public long owner;
   public List<Long> waiting;
   public boolean isHeapLock;
   public boolean waitingHeapLock;
 
   public MonitorDump()
   {
   }
 
   public MonitorDump(String name, long owner)
   {
     this.objectName = name;
     this.owner = owner;
   }
 
   public MonitorDump(String name, long owner, boolean heapLock)
   {
     this.objectName = name;
     this.owner = owner;
     this.isHeapLock = heapLock;
   }
   public boolean isHeapLock() {
     return this.isHeapLock;
   }
   public boolean waitingHeapLock() {
     return this.waitingHeapLock;
   }
 }

