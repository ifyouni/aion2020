/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.AISubState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
/*    */ import com.aionemu.gameserver.questEngine.QuestEngine;
/*    */ import com.aionemu.gameserver.questEngine.model.QuestEnv;
/*    */ import com.aionemu.gameserver.services.TownService;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ 
/*    */ public class TalkEventHandler
/*    */ {
/*    */   public static void onTalk(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 30 */     onSimpleTalk(npcAI, creature);
/* 31 */     if (creature instanceof Player) {
/* 32 */       Player player = (Player)creature;
/* 33 */       if (QuestEngine.getInstance().onDialog(new QuestEnv(npcAI.getOwner(), player, Integer.valueOf(0), Integer.valueOf(-1))))
/* 34 */         return;
/* 35 */       switch (npcAI.getOwner().getObjectTemplate().getTitleId())
/*    */       {
/*    */       case 462877:
/*    */       case 462878:
/* 39 */         int playerTownId = TownService.getInstance().getTownResidence(player);
/* 40 */         int currentTownId = TownService.getInstance().getTownIdByPosition(player);
/* 41 */         if (playerTownId != currentTownId) {
/* 42 */           PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId().intValue(), 44));
/* 43 */           return;
/*    */         }
/* 45 */         PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId().intValue(), 10));
/* 46 */         return;
/*    */       }
/*    */ 
/* 49 */       PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId().intValue(), 10));
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onSimpleTalk(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 56 */     npcAI.setSubStateIfNot(AISubState.TALK);
/* 57 */     npcAI.getOwner().setTarget(creature);
/*    */   }
/*    */ 
/*    */   public static void onFinishTalk(NpcAI2 npcAI, Creature creature) {
/* 61 */     Npc owner = npcAI.getOwner();
/* 62 */     if (owner.isTargeting(creature.getObjectId().intValue())) {
/* 63 */       if (npcAI.getState() != AIState.FOLLOWING) {
/* 64 */         owner.setTarget(null);
/*    */       }
/* 66 */       npcAI.think();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onSimpleFinishTalk(NpcAI2 npcAI, Creature creature) {
/* 71 */     Npc owner = npcAI.getOwner();
/* 72 */     if ((owner.isTargeting(creature.getObjectId().intValue())) && (npcAI.setSubStateIfNot(AISubState.NONE)))
/* 73 */       owner.setTarget(null);
/*    */   }
/*    */ }