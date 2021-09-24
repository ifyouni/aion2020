/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.AISubState;
/*    */ import com.aionemu.gameserver.ai2.AbstractAI;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*    */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*    */ import com.aionemu.gameserver.controllers.effect.EffectController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*    */ 
/*    */ public class FreezeEventHandler
/*    */ {
/*    */   public static void onUnfreeze(AbstractAI ai)
/*    */   {
/* 13 */     if (ai.isInSubState(AISubState.FREEZE)) {
/* 14 */       ai.setSubStateIfNot(AISubState.NONE);
/* 15 */       if (ai instanceof NpcAI2) {
/* 16 */         Npc npc = ((NpcAI2)ai).getOwner();
/* 17 */         if (npc.getWalkerGroup() != null) {
/* 18 */           ai.setStateIfNot(AIState.WALKING);
/* 19 */           ai.setSubStateIfNot(AISubState.WALK_WAIT_GROUP);
/* 20 */         } else if (npc.getSpawn().getRandomWalk() > 0) {
/* 21 */           ai.setStateIfNot(AIState.WALKING);
/* 22 */           ai.setSubStateIfNot(AISubState.WALK_RANDOM);
/*    */         }
/* 24 */         npc.updateKnownlist();
/*    */       }
/* 26 */       ai.think();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onFreeze(AbstractAI ai) {
/* 31 */     if (ai.isInState(AIState.WALKING)) {
/* 32 */       WalkManager.stopWalking((NpcAI2)ai);
/*    */     }
/* 34 */     ai.setStateIfNot(AIState.IDLE);
/* 35 */     ai.setSubStateIfNot(AISubState.FREEZE);
/* 36 */     ai.think();
/* 37 */     if (ai instanceof NpcAI2) {
/* 38 */       Npc npc = ((NpcAI2)ai).getOwner();
/* 39 */       npc.updateKnownlist();
/* 40 */       npc.getAggroList().clear();
/* 41 */       npc.getEffectController().removeAllEffects();
/*    */     }
/*    */   }
/*    */ }