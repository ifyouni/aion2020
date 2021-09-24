/*     */ package com.aionemu.gameserver.ai2.handler;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AIState;
/*     */ import com.aionemu.gameserver.ai2.AISubState;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.ai2.manager.AttackManager;
/*     */ import com.aionemu.gameserver.ai2.manager.EmoteManager;
/*     */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*     */ import com.aionemu.gameserver.ai2.poll.AIQuestion;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcGameStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
/*     */ 
/*     */ public class AttackEventHandler
/*     */ {
/*     */   public static void onAttack(NpcAI2 npcAI, Creature creature)
/*     */   {
/*  37 */     if (npcAI.isLogging()) {
/*  38 */       AI2Logger.info(npcAI, "onAttack");
/*     */     }
/*  40 */     if ((creature == null) || (creature.getLifeStats().isAlreadyDead())) {
/*  41 */       return;
/*     */     }
/*     */ 
/*  44 */     if (npcAI.isInState(AIState.RETURNING)) {
/*  45 */       npcAI.getOwner().getMoveController().abortMove();
/*  46 */       npcAI.setStateIfNot(AIState.IDLE);
/*  47 */       npcAI.onGeneralEvent(AIEventType.NOT_AT_HOME);
/*  48 */       return;
/*     */     }
/*  50 */     if (!(npcAI.canThink())) {
/*  51 */       return;
/*     */     }
/*  53 */     if (npcAI.isInState(AIState.WALKING)) {
/*  54 */       WalkManager.stopWalking(npcAI);
/*     */     }
/*  56 */     npcAI.getOwner().getGameStats().renewLastAttackedTime();
/*  57 */     if (!(npcAI.isInState(AIState.FIGHT))) {
/*  58 */       npcAI.setStateIfNot(AIState.FIGHT);
/*  59 */       if (npcAI.isLogging()) {
/*  60 */         AI2Logger.info(npcAI, "onAttack() -> startAttacking");
/*     */       }
/*  62 */       npcAI.setSubStateIfNot(AISubState.NONE);
/*  63 */       npcAI.getOwner().setTarget(creature);
/*  64 */       AttackManager.startAttacking(npcAI);
/*  65 */       if (npcAI.poll(AIQuestion.CAN_SHOUT))
/*  66 */         ShoutEventHandler.onAttackBegin(npcAI, (Creature)npcAI.getOwner().getTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onForcedAttack(NpcAI2 npcAI)
/*     */   {
/*  74 */     onAttack(npcAI, (Creature)npcAI.getOwner().getTarget());
/*     */   }
/*     */ 
/*     */   public static void onAttackComplete(NpcAI2 npcAI)
/*     */   {
/*  81 */     if (npcAI.isLogging()) {
/*  82 */       AI2Logger.info(npcAI, "onAttackComplete: " + npcAI.getOwner().getGameStats().getLastAttackTimeDelta());
/*     */     }
/*  84 */     npcAI.getOwner().getGameStats().renewLastAttackTime();
/*  85 */     AttackManager.scheduleNextAttack(npcAI);
/*     */   }
/*     */ 
/*     */   public static void onFinishAttack(NpcAI2 npcAI)
/*     */   {
/*  92 */     if (!(npcAI.canThink())) {
/*  93 */       return;
/*     */     }
/*  95 */     if (npcAI.isLogging()) {
/*  96 */       AI2Logger.info(npcAI, "onFinishAttack");
/*     */     }
/*  98 */     Npc npc = npcAI.getOwner();
/*  99 */     EmoteManager.emoteStopAttacking(npc);
/* 100 */     npc.getLifeStats().startResting();
/* 101 */     npc.getAggroList().clear();
/* 102 */     if (npcAI.poll(AIQuestion.CAN_SHOUT))
/* 103 */       ShoutEventHandler.onAttackEnd(npcAI);
/* 104 */     npc.setTarget(null);
/* 105 */     npc.setSkillNumber(0);
/*     */   }
/*     */ }