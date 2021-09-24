/*     */ package com.aionemu.gameserver.ai2.handler;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AIState;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.ai2.manager.AttackManager;
/*     */ import com.aionemu.gameserver.ai2.manager.FollowManager;
/*     */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.world.knownlist.KnownList;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TargetEventHandler
/*     */ {
/*     */   public static void onTargetReached(NpcAI2 npcAI)
/*     */   {
/*  34 */     if (npcAI.isLogging()) {
/*  35 */       AI2Logger.info(npcAI, "onTargetReached");
/*     */     }
/*     */ 
/*  38 */     AIState currentState = npcAI.getState();
/*  39 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[currentState.ordinal()])
/*     */     {
/*     */     case 1:
/*  41 */       npcAI.getOwner().getMoveController().abortMove();
/*  42 */       AttackManager.scheduleNextAttack(npcAI);
/*  43 */       if (!(npcAI.getOwner().getMoveController().isFollowingTarget())) return;
/*  44 */       npcAI.getOwner().getMoveController().storeStep(); break;
/*     */     case 2:
/*  47 */       npcAI.getOwner().getMoveController().abortMove();
/*  48 */       npcAI.getOwner().getMoveController().recallPreviousStep();
/*  49 */       if (npcAI.getOwner().isAtSpawnLocation()) {
/*  50 */         npcAI.onGeneralEvent(AIEventType.BACK_HOME); return;
/*     */       }
/*  52 */       npcAI.onGeneralEvent(AIEventType.NOT_AT_HOME);
/*  53 */       break;
/*     */     case 3:
/*  55 */       WalkManager.targetReached(npcAI);
/*  56 */       checkAggro(npcAI);
/*  57 */       break;
/*     */     case 4:
/*  59 */       npcAI.getOwner().getMoveController().abortMove();
/*  60 */       npcAI.getOwner().getMoveController().storeStep();
/*  61 */       break;
/*     */     case 5:
/*  63 */       npcAI.getOwner().getMoveController().abortMove();
/*  64 */       npcAI.getOwner().getMoveController().storeStep();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onTargetTooFar(NpcAI2 npcAI)
/*     */   {
/*  73 */     if (npcAI.isLogging()) {
/*  74 */       AI2Logger.info(npcAI, "onTargetTooFar");
/*     */     }
/*  76 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[npcAI.getState().ordinal()])
/*     */     {
/*     */     case 1:
/*  78 */       AttackManager.targetTooFar(npcAI);
/*  79 */       break;
/*     */     case 4:
/*  81 */       FollowManager.targetTooFar(npcAI);
/*  82 */       break;
/*     */     case 5:
/*  84 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     default:
/*  86 */       if (!(npcAI.isLogging())) return;
/*  87 */       AI2Logger.info(npcAI, "default onTargetTooFar");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onTargetGiveup(NpcAI2 npcAI)
/*     */   {
/*  96 */     if (npcAI.isLogging()) {
/*  97 */       AI2Logger.info(npcAI, "onTargetGiveup");
/*     */     }
/*  99 */     VisibleObject target = npcAI.getOwner().getTarget();
/* 100 */     if (target != null) {
/* 101 */       npcAI.getOwner().getAggroList().stopHating(target);
/*     */     }
/* 103 */     if (npcAI.isMoveSupported()) {
/* 104 */       npcAI.getOwner().getMoveController().abortMove();
/*     */     }
/* 106 */     if (!(npcAI.isAlreadyDead()))
/* 107 */       npcAI.think();
/*     */   }
/*     */ 
/*     */   public static void onTargetChange(NpcAI2 npcAI, Creature creature)
/*     */   {
/* 114 */     if (npcAI.isLogging()) {
/* 115 */       AI2Logger.info(npcAI, "onTargetChange");
/*     */     }
/* 117 */     if (npcAI.isInState(AIState.FIGHT)) {
/* 118 */       npcAI.getOwner().setTarget(creature);
/* 119 */       AttackManager.scheduleNextAttack(npcAI);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkAggro(NpcAI2 npcAI) {
/* 124 */     for (VisibleObject obj : npcAI.getOwner().getKnownList().getKnownObjects().values())
/* 125 */       if (obj instanceof Creature)
/* 126 */         CreatureEventHandler.checkAggro(npcAI, (Creature)obj);
/*     */   }
/*     */ }