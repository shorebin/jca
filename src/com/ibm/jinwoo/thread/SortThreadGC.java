 package com.ibm.jinwoo.thread;
 
 import java.text.NumberFormat;
 
 public class SortThreadGC extends Thread
 {
   GCTableModel om;
   int column;
   static NumberFormat numberFormatter = NumberFormat.getNumberInstance();
 
   public SortThreadGC()
   {
   }
 
   public SortThreadGC(Runnable target)
   {
     super(target);
   }
 
   public SortThreadGC(Runnable target, String name)
   {
     super(target, name);
   }
 
   public SortThreadGC(String name)
   {
     super(name);
   }
 
   public SortThreadGC(ThreadGroup group, Runnable target)
   {
     super(group, target);
   }
 
   public SortThreadGC(ThreadGroup group, Runnable target, String name)
   {
     super(group, target, name);
   }
 
   public SortThreadGC(ThreadGroup group, String name)
   {
     super(group, name);
   }
 
   public SortThreadGC(GCTableModel objectTableModel)
   {
     this.om = objectTableModel;
     this.column = 0;
   }
 
   public SortThreadGC(GCTableModel objectTableModel, int column)
   {
     this.om = objectTableModel;
     this.column = column;
   }
   public void run() {
     GCInfo hi = this.om.hi;
 
     switch (this.column)
     {
     case 0:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.free[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 1:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.total[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 2:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.af[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 3:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.freed[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 4:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.completed[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 5:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.since[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 6:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.mark[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 7:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.sweep[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 8:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.compact[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 9:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = hi.gccompleted[i];
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 10:
       for (int i = 0; i < hi.total.length; i++) {
         if (this.om.isStartPoint(i)) this.om.sortedArrary[0][i] = 0L; else
           this.om.sortedArrary[0][i] = ((long)((float)this.om.getCompleted(i) * 100.0F / (float)(this.om.getCompleted(i) + this.om.getSince(i))));
         this.om.sortedArrary[1][i] = i;
       }
       Arrays2.sort(this.om.sortedArrary);
       break;
     case 11:
       for (int i = 0; i < hi.total.length; i++) {
         this.om.sortedArrary[0][i] = 0L;
         this.om.sortedArrary[1][i] = i;
       }
       if (hi.outOfHeapSpace != null)
       {
         for (int i = 0; i < hi.outOfHeapSpace.length; i++) {
           this.om.sortedArrary[0][hi.outOfHeapSpace[i]] = 1L;
         }
       }
       Arrays2.sort(this.om.sortedArrary);
     }
 
     this.om.sortedColumn = this.column;
     this.om.direction = true;
     this.om.fireTableDataChanged();
     if (this.om.tableHeader != null)
       this.om.tableHeader.repaint();
   }
 }

