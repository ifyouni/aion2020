/*     */ package com.aionemu.gameserver.ai2;
/*     */ 
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
/*     */ 
/*     */ public abstract class AITemplate extends AbstractAI
/*     */ {
/*     */   public void think()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean canThink()
/*     */   {
/*  30 */     return true;
/*     */   }
/*     */ 
/*     */   protected void handleActivate()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDeactivate()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleMoveValidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleMoveArrived()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleAttack(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected boolean handleCreatureNeedsSupport(Creature creature)
/*     */   {
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean handleGuardAgainstAttacker(Creature creature)
/*     */   {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   protected void handleCreatureSee(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleCreatureNotSee(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleCreatureMoved(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleCreatureAggro(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleFollowMe(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleStopFollowMe(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDialogStart(Player player)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDialogFinish(Player player)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleCustomEvent(int eventId, Object[] args)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleSpawned()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleRespawned()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDespawned()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDied()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleTargetReached()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleAttackComplete()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleFinishAttack()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleTargetTooFar()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleTargetGiveup()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleTargetChanged(Creature creature)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleNotAtHome()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleBackHome()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void handleDropRegistered()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isMayShout()
/*     */   {
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean onPatternShout(ShoutEventType event, String pattern, int skillNumber)
/*     */   {
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   public AttackIntention chooseAttackIntention()
/*     */   {
/* 163 */     return AttackIntention.SIMPLE_ATTACK;
/*     */   }
/*     */ }