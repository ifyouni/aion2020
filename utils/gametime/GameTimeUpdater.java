/*    */ package com.aionemu.gameserver.utils.gametime;
/*    */ 
/*    */ public class GameTimeUpdater
/*    */   implements Runnable
/*    */ {
/*    */   private GameTime time;
/*    */ 
/*    */   public GameTimeUpdater(GameTime time)
/*    */   {
/* 31 */     this.time = time;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 39 */     this.time.increase();
/*    */   }
/*    */ }