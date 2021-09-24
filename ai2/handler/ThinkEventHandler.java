/*     */ package com.aionemu.gameserver.ai2.handler;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AIState;
/*     */ import com.aionemu.gameserver.ai2.AISubState;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.world.WorldPosition;
/*     */ 
/*     */ public class ThinkEventHandler
/*     */ {
/*     */   public static void onThink(NpcAI2 npcAI)
/*     */   {
/*  33 */     if (npcAI.isLogging()) {
/*  34 */       AI2Logger.info(npcAI, "think");
/*     */     }
/*  36 */     if (npcAI.isAlreadyDead()) {
/*  37 */       AI2Logger.info(npcAI, "can't think in dead state");
/*  38 */       return;
/*     */     }
/*  40 */     if (!(npcAI.tryLockThink())) {
/*  41 */       AI2Logger.info(npcAI, "can't acquire lock");
/*  42 */       return;
/*     */     }
/*     */     try {
/*  45 */       if ((!(npcAI.getOwner().getPosition().isMapRegionActive())) || (npcAI.getSubState() == AISubState.FREEZE)) {
/*  46 */         thinkInInactiveRegion(npcAI);
/*     */         return;
/*     */       }
/*  49 */       if (npcAI.isLogging()) {
/*  50 */         AI2Logger.info(npcAI, "think state " + npcAI.getState());
/*     */       }
/*  52 */       switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[npcAI.getState().ordinal()])
/*     */       {
/*     */       case 1:
/*  54 */         thinkAttack(npcAI);
/*  55 */         break;
/*     */       case 2:
/*  57 */         thinkWalking(npcAI);
/*  58 */         break;
/*     */       case 3:
/*  60 */         thinkIdle(npcAI);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*  65 */       npcAI.unlockThink();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void thinkInInactiveRegion(NpcAI2 npcAI)
/*     */   {
/*  73 */     if (!(npcAI.canThink())) {
/*  74 */       return;
/*     */     }
/*  76 */     if (npcAI.isLogging()) {
/*  77 */       AI2Logger.info(npcAI, "think in inactive region: " + npcAI.getState());
/*     */     }
/*  79 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[npcAI.getState().ordinal()])
/*     */     {
/*     */     case 1:
/*  81 */       thinkAttack(npcAI);
/*  82 */       break;
/*     */     default:
/*  84 */       if (npcAI.getOwner().isAtSpawnLocation()) return;
/*  85 */       npcAI.onGeneralEvent(AIEventType.NOT_AT_HOME);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void thinkAttack(NpcAI2 npcAI)
/*     */   {
/*  95 */     Npc npc = npcAI.getOwner();
/*  96 */     Creature mostHated = npc.getAggroList().getMostHated();
/*  97 */     if ((mostHated != null) && (!(mostHated.getLifeStats().isAlreadyDead()))) {
/*  98 */       npcAI.onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
/*     */     }
/*     */     else {
/* 101 */       npc.getMoveController().recallPreviousStep();
/* 102 */       npcAI.onGeneralEvent(AIEventType.ATTACK_FINISH);
/* 103 */       npcAI.onGeneralEvent((npc.isAtSpawnLocation()) ? AIEventType.BACK_HOME : AIEventType.NOT_AT_HOME);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void thinkWalking(NpcAI2 npcAI)
/*     */   {
/* 111 */     WalkManager.startWalking(npcAI);
/*     */   }
/*     */ 
/*     */   public static void thinkIdle(NpcAI2 npcAI)
/*     */   {
/* 118 */     if (WalkManager.isWalking(npcAI)) {
/* 119 */       boolean startedWalking = WalkManager.startWalking(npcAI);
/* 120 */       if (!(startedWalking))
/* 121 */         npcAI.setStateIfNot(AIState.IDLE);
/*     */     }
/*     */   }
/*     */ }