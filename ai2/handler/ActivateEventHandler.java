/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*    */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*    */ import com.aionemu.gameserver.controllers.effect.EffectController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ 
/*    */ public class ActivateEventHandler
/*    */ {
/*    */   public static void onActivate(NpcAI2 npcAI)
/*    */   {
/* 26 */     if (npcAI.isInState(AIState.IDLE)) {
/* 27 */       npcAI.getOwner().updateKnownlist();
/* 28 */       npcAI.think();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onDeactivate(NpcAI2 npcAI) {
/* 33 */     if (npcAI.isInState(AIState.WALKING)) {
/* 34 */       WalkManager.stopWalking(npcAI);
/*    */     }
/* 36 */     npcAI.think();
/* 37 */     Npc npc = npcAI.getOwner();
/* 38 */     npc.updateKnownlist();
/* 39 */     npc.getAggroList().clear();
/* 40 */     npc.getEffectController().removeAllEffects();
/*    */   }
/*    */ }