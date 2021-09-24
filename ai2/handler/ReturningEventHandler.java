/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AI2Logger;
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.manager.EmoteManager;
/*    */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*    */ import com.aionemu.gameserver.controllers.NpcController;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.geometry.Point3D;
/*    */ 
/*    */ public class ReturningEventHandler
/*    */ {
/*    */   public static void onNotAtHome(NpcAI2 npcAI)
/*    */   {
/* 32 */     if (npcAI.isLogging()) {
/* 33 */       AI2Logger.info(npcAI, "onNotAtHome");
/*    */     }
/* 35 */     if (npcAI.setStateIfNot(AIState.RETURNING)) {
/* 36 */       if (npcAI.isLogging()) {
/* 37 */         AI2Logger.info(npcAI, "returning and restoring");
/*    */       }
/* 39 */       EmoteManager.emoteStartReturning(npcAI.getOwner());
/*    */     }
/* 41 */     if (npcAI.isInState(AIState.RETURNING)) {
/* 42 */       Npc npc = npcAI.getOwner();
/* 43 */       if (npc.hasWalkRoutes()) {
/* 44 */         WalkManager.startWalking(npcAI);
/*    */       }
/*    */       else {
/* 47 */         Point3D prevStep = npcAI.getOwner().getMoveController().recallPreviousStep();
/* 48 */         npcAI.getOwner().getMoveController().moveToPoint(prevStep.getX(), prevStep.getY(), prevStep.getZ());
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onBackHome(NpcAI2 npcAI)
/*    */   {
/* 57 */     if (npcAI.isLogging()) {
/* 58 */       AI2Logger.info(npcAI, "onBackHome");
/*    */     }
/* 60 */     npcAI.getOwner().getMoveController().clearBackSteps();
/* 61 */     if (npcAI.setStateIfNot(AIState.IDLE)) {
/* 62 */       EmoteManager.emoteStartIdling(npcAI.getOwner());
/* 63 */       ThinkEventHandler.thinkIdle(npcAI);
/*    */     }
/* 65 */     Npc npc = npcAI.getOwner();
/* 66 */     npc.getController().onReturnHome();
/*    */   }
/*    */ }