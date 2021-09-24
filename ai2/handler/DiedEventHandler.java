/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AI2Logger;
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.AISubState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.poll.AIQuestion;
/*    */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ 
/*    */ public class DiedEventHandler
/*    */ {
/*    */   public static void onDie(NpcAI2 npcAI)
/*    */   {
/* 28 */     if (npcAI.isLogging()) {
/* 29 */       AI2Logger.info(npcAI, "onDie");
/*    */     }
/*    */ 
/* 32 */     onSimpleDie(npcAI);
/*    */ 
/* 34 */     Npc owner = npcAI.getOwner();
/* 35 */     owner.setTarget(null);
/*    */   }
/*    */ 
/*    */   public static void onSimpleDie(NpcAI2 npcAI) {
/* 39 */     if (npcAI.isLogging()) {
/* 40 */       AI2Logger.info(npcAI, "onSimpleDie");
/*    */     }
/*    */ 
/* 43 */     if (npcAI.poll(AIQuestion.CAN_SHOUT)) {
/* 44 */       ShoutEventHandler.onDied(npcAI);
/*    */     }
/* 46 */     npcAI.setStateIfNot(AIState.DIED);
/* 47 */     npcAI.setSubStateIfNot(AISubState.NONE);
/* 48 */     npcAI.getOwner().getAggroList().clear();
/*    */   }
/*    */ }