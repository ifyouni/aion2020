/*    */ package com.aionemu.gameserver.ai2.event;
/*    */ 
/*    */ import java.util.concurrent.LinkedBlockingDeque;
/*    */ 
/*    */ public class AIEventLog extends LinkedBlockingDeque<AIEventType>
/*    */ {
/*    */   private static final long serialVersionUID = -7234174243343636729L;
/*    */ 
/*    */   public AIEventLog()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AIEventLog(int capacity)
/*    */   {
/* 32 */     super(capacity);
/*    */   }
/*    */ 
/*    */   public synchronized boolean offerFirst(AIEventType e)
/*    */   {
/* 37 */     if (remainingCapacity() == 0) {
/* 38 */       removeLast();
/*    */     }
/* 40 */     super.offerFirst(e);
/* 41 */     return true;
/*    */   }
/*    */ }