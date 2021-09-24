/*    */ package com.aionemu.gameserver.ai2.eventcallback;
/*    */ 
/*    */ import com.aionemu.commons.callbacks.Callback;
/*    */ import com.aionemu.commons.callbacks.CallbackResult;
/*    */ import com.aionemu.gameserver.ai2.AbstractAI;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ 
/*    */ public abstract class OnHandleAIGeneralEvent
/*    */   implements Callback<AbstractAI>
/*    */ {
/*    */   public CallbackResult beforeCall(AbstractAI obj, Object[] args)
/*    */   {
/* 18 */     AIEventType eventType = (AIEventType)args[0];
/* 19 */     onBeforeHandleGeneralEvent(obj, eventType);
/* 20 */     return CallbackResult.newContinue();
/*    */   }
/*    */ 
/*    */   public CallbackResult afterCall(AbstractAI obj, Object[] args, Object methodResult)
/*    */   {
/* 25 */     AIEventType eventType = (AIEventType)args[0];
/* 26 */     onAfterHandleGeneralEvent(obj, eventType);
/* 27 */     return CallbackResult.newContinue();
/*    */   }
/*    */ 
/*    */   public Class<? extends Callback> getBaseClass()
/*    */   {
/* 32 */     return OnHandleAIGeneralEvent.class;
/*    */   }
/*    */ 
/*    */   protected abstract void onBeforeHandleGeneralEvent(AbstractAI paramAbstractAI, AIEventType paramAIEventType);
/*    */ 
/*    */   protected abstract void onAfterHandleGeneralEvent(AbstractAI paramAbstractAI, AIEventType paramAIEventType);
/*    */ }