/*    */ package com.aionemu.gameserver.ai2.manager;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AI2Logger;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ 
/*    */ public class FollowManager
/*    */ {
/*    */   public static void targetTooFar(NpcAI2 npcAI)
/*    */   {
/* 25 */     Npc npc = npcAI.getOwner();
/* 26 */     if (npcAI.isLogging()) {
/* 27 */       AI2Logger.info(npcAI, "Follow manager - targetTooFar");
/*    */     }
/* 29 */     if (npcAI.isMoveSupported())
/* 30 */       npc.getMoveController().moveToTargetObject();
/*    */   }
/*    */ }