/*    */ package com.aionemu.gameserver.utils.cron;
/*    */ 
/*    */ import com.aionemu.commons.services.cron.RunnableRunner;
/*    */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*    */ 
/*    */ public class ThreadPoolManagerRunnableRunner extends RunnableRunner
/*    */ {
/*    */   public void executeRunnable(Runnable r)
/*    */   {
/* 10 */     ThreadPoolManager.getInstance().execute(r);
/*    */   }
/*    */ 
/*    */   public void executeLongRunningRunnable(Runnable r)
/*    */   {
/* 15 */     ThreadPoolManager.getInstance().executeLongRunning(r);
/*    */   }
/*    */ }