 package com.ibm.jinwoo.thread;
 
 public abstract class ThreadHandler
 {
   public Object object;
   private ThreadObject threadObject;
 
   public ThreadHandler()
   {
     Runnable aRun = new Runnable() {
       public void run() {
         try {
           ThreadHandler.this.setObject(ThreadHandler.this.construct());
         } finally {
           ThreadHandler.this.threadObject.reset();
         }
       }
     };
     this.threadObject = new ThreadObject(new Thread(aRun));
   }
   public abstract Object construct();
 
   private synchronized void setObject(Object o) { this.object = o; }
 
   public void start() {
     Thread tr = this.threadObject.getThread();
     if (tr != null)
       tr.start();
   }
 
   public static class ThreadObject
   {
     public Thread thread;
 
     ThreadObject(Thread tr)
     {
       this.thread = tr;
     }
     synchronized void reset() {
       this.thread = null;
     }
     synchronized Thread getThread() {
       return this.thread;
     }
   }
 }

