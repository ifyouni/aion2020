/*     */ package com.aionemu.gameserver.ai2.manager;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2;
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AIState;
/*     */ import com.aionemu.gameserver.ai2.AbstractAI;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.controllers.NpcController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.stats.calc.Stat2;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcGameStats;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.world.geo.GeoService;
/*     */ 
/*     */ public class SimpleAttackManager
/*     */ {
/*     */   public static void performAttack(NpcAI2 npcAI, int delay)
/*     */   {
/*  36 */     if (npcAI.isLogging()) {
/*  37 */       AI2Logger.info(npcAI, "performAttack");
/*     */     }
/*  39 */     if (npcAI.getOwner().getGameStats().isNextAttackScheduled()) {
/*  40 */       if (npcAI.isLogging()) {
/*  41 */         AI2Logger.info(npcAI, "Attack already sheduled");
/*     */       }
/*  43 */       scheduleCheckedAttackAction(npcAI, delay);
/*  44 */       return;
/*     */     }
/*     */ 
/*  47 */     if (!(isTargetInAttackRange(npcAI.getOwner()))) {
/*  48 */       if (npcAI.isLogging()) {
/*  49 */         AI2Logger.info(npcAI, "Attack will not be scheduled because of range");
/*     */       }
/*  51 */       npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
/*  52 */       return;
/*     */     }
/*  54 */     npcAI.getOwner().getGameStats().setNextAttackTime(System.currentTimeMillis() + delay);
/*  55 */     if (delay > 0) {
/*  56 */       ThreadPoolManager.getInstance().schedule(new SimpleAttackAction(npcAI), delay);
/*     */     }
/*     */     else
/*  59 */       attackAction(npcAI);
/*     */   }
/*     */ 
/*     */   private static void scheduleCheckedAttackAction(NpcAI2 npcAI, int delay)
/*     */   {
/*  68 */     if (npcAI.isLogging()) {
/*  69 */       AI2Logger.info(npcAI, "Scheduling checked attack " + delay);
/*     */     }
/*  71 */     ThreadPoolManager.getInstance().schedule(new SimpleCheckedAttackAction(npcAI), delay);
/*     */   }
/*     */ 
/*     */   public static boolean isTargetInAttackRange(Npc npc) {
/*  75 */     if (npc.getAi2().isLogging()) {
/*  76 */       float distance = npc.getDistanceToTarget();
/*  77 */       AI2Logger.info((AbstractAI)npc.getAi2(), "isTargetInAttackRange: " + distance);
/*     */     }
/*  79 */     if ((npc.getTarget() == null) || (!(npc.getTarget() instanceof Creature)))
/*  80 */       return false;
/*  81 */     return MathUtil.isInAttackRange(npc, (Creature)npc.getTarget(), npc.getGameStats().getAttackRange().getCurrent() / 1000.0F);
/*     */   }
/*     */ 
/*     */   protected static void attackAction(NpcAI2 npcAI)
/*     */   {
/*  89 */     if (!(npcAI.isInState(AIState.FIGHT))) {
/*  90 */       return;
/*     */     }
/*  92 */     if (npcAI.isLogging()) {
/*  93 */       AI2Logger.info(npcAI, "attackAction");
/*     */     }
/*  95 */     Npc npc = npcAI.getOwner();
/*  96 */     Creature target = (Creature)npc.getTarget();
/*  97 */     if ((target != null) && (!(target.getLifeStats().isAlreadyDead()))) {
/*  98 */       if ((!(npc.canSee(target))) || (!(GeoService.getInstance().canSee(npc, target)))) {
/*  99 */         npc.getController().cancelCurrentSkill();
/* 100 */         npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/* 101 */         return;
/*     */       }
/* 103 */       if (isTargetInAttackRange(npc)) {
/* 104 */         npc.getController().attackTarget(target, 0);
/* 105 */         npcAI.onGeneralEvent(AIEventType.ATTACK_COMPLETE);
/* 106 */         return;
/*     */       }
/* 108 */       npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
/*     */     }
/*     */     else {
/* 111 */       npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SimpleCheckedAttackAction
/*     */     implements Runnable
/*     */   {
/*     */     private NpcAI2 npcAI;
/*     */ 
/*     */     SimpleCheckedAttackAction(NpcAI2 npcAI)
/*     */     {
/* 136 */       this.npcAI = npcAI;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 141 */       if (!(this.npcAI.getOwner().getGameStats().isNextAttackScheduled())) {
/* 142 */         SimpleAttackManager.attackAction(this.npcAI);
/*     */       }
/* 145 */       else if (this.npcAI.isLogging()) {
/* 146 */         AI2Logger.info(this.npcAI, "Scheduled checked attacked confirmed");
/*     */       }
/*     */ 
/* 149 */       this.npcAI = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SimpleAttackAction
/*     */     implements Runnable
/*     */   {
/*     */     private NpcAI2 npcAI;
/*     */ 
/*     */     SimpleAttackAction(NpcAI2 npcAI)
/*     */     {
/* 120 */       this.npcAI = npcAI;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 125 */       SimpleAttackManager.attackAction(this.npcAI);
/* 126 */       this.npcAI = null;
/*     */     }
/*     */   }
/*     */ }