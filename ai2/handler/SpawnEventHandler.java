/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.world.WorldPosition;
/*    */ 
/*    */ public class SpawnEventHandler
/*    */ {
/*    */   public static void onSpawn(NpcAI2 npcAI)
/*    */   {
/* 27 */     if ((!(npcAI.setStateIfNot(AIState.IDLE))) || 
/* 28 */       (!(npcAI.getOwner().getPosition().isMapRegionActive()))) return;
/* 29 */     npcAI.think();
/*    */   }
/*    */ 
/*    */   public static void onDespawn(NpcAI2 npcAI)
/*    */   {
/* 38 */     npcAI.setStateIfNot(AIState.DESPAWNED);
/*    */   }
/*    */ 
/*    */   public static void onRespawn(NpcAI2 npcAI)
/*    */   {
/* 45 */     npcAI.getOwner().getMoveController().resetMove();
/*    */   }
/*    */ }