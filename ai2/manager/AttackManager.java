/*     */ package com.aionemu.gameserver.ai2.manager;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AISubState;
/*     */ import com.aionemu.gameserver.ai2.AttackIntention;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcGameStats;
/*     */ import com.aionemu.gameserver.model.templates.world.AiInfo;
/*     */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*     */ import com.aionemu.gameserver.world.WorldMapInstance;
/*     */ import com.aionemu.gameserver.world.WorldPosition;
/*     */ 
/*     */ public class AttackManager
/*     */ {
/*     */   public static void startAttacking(NpcAI2 npcAI)
/*     */   {
/*  32 */     if (npcAI.isLogging()) {
/*  33 */       AI2Logger.info(npcAI, "AttackManager: startAttacking");
/*     */     }
/*  35 */     npcAI.getOwner().getGameStats().setFightStartingTime();
/*  36 */     EmoteManager.emoteStartAttacking(npcAI.getOwner());
/*  37 */     scheduleNextAttack(npcAI);
/*     */   }
/*     */ 
/*     */   public static void scheduleNextAttack(NpcAI2 npcAI)
/*     */   {
/*  44 */     if (npcAI.isLogging()) {
/*  45 */       AI2Logger.info(npcAI, "AttackManager: scheduleNextAttack");
/*     */     }
/*     */ 
/*  48 */     AISubState subState = npcAI.getSubState();
/*  49 */     if (subState == AISubState.NONE) {
/*  50 */       chooseAttack(npcAI, npcAI.getOwner().getGameStats().getNextAttackInterval());
/*     */     }
/*  53 */     else if (npcAI.isLogging())
/*  54 */       AI2Logger.info(npcAI, "Will not choose attack in substate" + subState);
/*     */   }
/*     */ 
/*     */   protected static void chooseAttack(NpcAI2 npcAI, int delay)
/*     */   {
/*  63 */     AttackIntention attackIntention = npcAI.chooseAttackIntention();
/*  64 */     if (npcAI.isLogging()) {
/*  65 */       AI2Logger.info(npcAI, "AttackManager: chooseAttack " + attackIntention + " delay " + delay);
/*     */     }
/*  67 */     if (!(npcAI.canThink())) {
/*  68 */       return;
/*     */     }
/*  70 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AttackIntention[attackIntention.ordinal()])
/*     */     {
/*     */     case 1:
/*  72 */       SimpleAttackManager.performAttack(npcAI, delay);
/*  73 */       break;
/*     */     case 2:
/*  75 */       SkillAttackManager.performAttack(npcAI, delay);
/*  76 */       break;
/*     */     case 3:
/*  78 */       npcAI.think();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void targetTooFar(NpcAI2 npcAI)
/*     */   {
/*  87 */     Npc npc = npcAI.getOwner();
/*  88 */     if (npcAI.isLogging()) {
/*  89 */       AI2Logger.info(npcAI, "AttackManager: attackTimeDelta " + npc.getGameStats().getLastAttackTimeDelta());
/*     */     }
/*     */ 
/*  93 */     if (npc.getGameStats().getLastChangeTargetTimeDelta() > 5) {
/*  94 */       Creature mostHated = npc.getAggroList().getMostHated();
/*  95 */       if ((mostHated != null) && (!(mostHated.getLifeStats().isAlreadyDead())) && (!(npc.isTargeting(mostHated.getObjectId().intValue())))) {
/*  96 */         if (npcAI.isLogging()) {
/*  97 */           AI2Logger.info(npcAI, "AttackManager: switching target during chase");
/*     */         }
/*  99 */         npcAI.onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
/* 100 */         return;
/*     */       }
/*     */     }
/* 103 */     if (!(npc.canSee((Creature)npc.getTarget()))) {
/* 104 */       npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/* 105 */       return;
/*     */     }
/* 107 */     if (checkGiveupDistance(npcAI)) {
/* 108 */       npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/* 109 */       return;
/*     */     }
/* 111 */     if (npcAI.isMoveSupported()) {
/* 112 */       npc.getMoveController().moveToTargetObject();
/* 113 */       return;
/*     */     }
/* 115 */     npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/*     */   }
/*     */ 
/*     */   private static boolean checkGiveupDistance(NpcAI2 npcAI) {
/* 119 */     Npc npc = npcAI.getOwner();
/*     */ 
/* 121 */     float distanceToTarget = npc.getDistanceToTarget();
/* 122 */     if (npcAI.isLogging()) {
/* 123 */       AI2Logger.info(npcAI, "AttackManager: distanceToTarget " + distanceToTarget);
/*     */     }
/*     */ 
/* 126 */     int chaseTarget = (npc.isBoss()) ? 50 : npc.getPosition().getWorldMapInstance().getTemplate().getAiInfo().getChaseTarget();
/*     */ 
/* 128 */     if (distanceToTarget > chaseTarget) {
/* 129 */       return true;
/*     */     }
/* 131 */     double distanceToHome = npc.getDistanceToSpawnLocation();
/*     */ 
/* 133 */     int chaseHome = (npc.isBoss()) ? 150 : npc.getPosition().getWorldMapInstance().getTemplate().getAiInfo().getChaseHome();
/*     */ 
/* 135 */     if (distanceToHome > chaseHome) {
/* 136 */       return true;
/*     */     }
/*     */ 
/* 142 */     return ((chaseHome > 6) || (
/* 140 */       (((npc.getGameStats().getLastAttackTimeDelta() <= 20) || (npc.getGameStats().getLastAttackedTimeDelta() <= 20))) && (((distanceToHome <= chaseHome / 2) || (npc.getGameStats().getLastAttackedTimeDelta() <= 2)))));
/*     */   }
/*     */ }