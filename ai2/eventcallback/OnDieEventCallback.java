/*    */ package com.aionemu.gameserver.ai2.eventcallback;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AbstractAI;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ 
/*    */ public abstract class OnDieEventCallback extends OnHandleAIGeneralEvent
/*    */ {
/*    */   protected void onBeforeHandleGeneralEvent(AbstractAI obj, AIEventType eventType)
/*    */   {
/* 15 */     if (AIEventType.DIED == eventType)
/* 16 */       onBeforeDie(obj);
/*    */   }
/*    */ 
/*    */   protected void onAfterHandleGeneralEvent(AbstractAI obj, AIEventType eventType)
/*    */   {
/* 22 */     if (AIEventType.DIED == eventType)
/* 23 */       onAfterDie(obj);
/*    */   }
/*    */ 
/*    */   public abstract void onBeforeDie(AbstractAI paramAbstractAI);
/*    */ 
/*    */   public abstract void onAfterDie(AbstractAI paramAbstractAI);
/*    */ }