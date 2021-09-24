/*    */ package com.aionemu.gameserver.ai2;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import java.util.Arrays;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public enum StateEvents
/*    */ {
/* 23 */   CREATED_EVENTS, DESPAWN_EVENTS, DEAD_EVENTS;
/*    */ 
/*    */   private EnumSet<AIEventType> events;
/*    */ 
/*    */   public boolean hasEvent(AIEventType event)
/*    */   {
/* 35 */     return this.events.contains(event);
/*    */   }
/*    */ }