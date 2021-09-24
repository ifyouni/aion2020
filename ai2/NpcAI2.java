/*     */ package com.aionemu.gameserver.ai2;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.handler.ActivateEventHandler;
/*     */ import com.aionemu.gameserver.ai2.handler.DiedEventHandler;
/*     */ import com.aionemu.gameserver.ai2.handler.ShoutEventHandler;
/*     */ import com.aionemu.gameserver.ai2.handler.SpawnEventHandler;
/*     */ import com.aionemu.gameserver.ai2.poll.AIAnswer;
/*     */ import com.aionemu.gameserver.ai2.poll.AIAnswers;
/*     */ import com.aionemu.gameserver.ai2.poll.AIQuestion;
/*     */ import com.aionemu.gameserver.ai2.poll.NpcAIPolls;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.effect.EffectController;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.model.Race;
/*     */ import com.aionemu.gameserver.model.TribeClass;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.skill.NpcSkillList;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcGameStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
/*     */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.world.knownlist.KnownList;
/*     */ 
/*     */ @AIName("npc")
/*     */ public class NpcAI2 extends AITemplate
/*     */ {
/*     */   public Npc getOwner()
/*     */   {
/*  47 */     return ((Npc)super.getOwner());
/*     */   }
/*     */ 
/*     */   protected NpcTemplate getObjectTemplate() {
/*  51 */     return getOwner().getObjectTemplate();
/*     */   }
/*     */ 
/*     */   protected SpawnTemplate getSpawnTemplate() {
/*  55 */     return getOwner().getSpawn();
/*     */   }
/*     */ 
/*     */   protected NpcLifeStats getLifeStats() {
/*  59 */     return getOwner().getLifeStats();
/*     */   }
/*     */ 
/*     */   protected Race getRace() {
/*  63 */     return getOwner().getRace();
/*     */   }
/*     */ 
/*     */   protected TribeClass getTribe() {
/*  67 */     return getOwner().getTribe();
/*     */   }
/*     */ 
/*     */   protected EffectController getEffectController() {
/*  71 */     return getOwner().getEffectController();
/*     */   }
/*     */ 
/*     */   protected KnownList getKnownList() {
/*  75 */     return getOwner().getKnownList();
/*     */   }
/*     */ 
/*     */   protected AggroList getAggroList() {
/*  79 */     return getOwner().getAggroList();
/*     */   }
/*     */ 
/*     */   protected NpcSkillList getSkillList() {
/*  83 */     return getOwner().getSkillList();
/*     */   }
/*     */ 
/*     */   protected VisibleObject getCreator() {
/*  87 */     return getOwner().getCreator();
/*     */   }
/*     */ 
/*     */   protected NpcMoveController getMoveController()
/*     */   {
/*  94 */     return getOwner().getMoveController();
/*     */   }
/*     */ 
/*     */   protected int getNpcId() {
/*  98 */     return getOwner().getNpcId();
/*     */   }
/*     */ 
/*     */   protected int getCreatorId() {
/* 102 */     return getOwner().getCreatorId();
/*     */   }
/*     */ 
/*     */   protected boolean isInRange(VisibleObject object, int range) {
/* 106 */     return MathUtil.isIn3dRange(getOwner(), object, range);
/*     */   }
/*     */ 
/*     */   protected void handleActivate()
/*     */   {
/* 111 */     ActivateEventHandler.onActivate(this);
/*     */   }
/*     */ 
/*     */   protected void handleDeactivate()
/*     */   {
/* 116 */     ActivateEventHandler.onDeactivate(this);
/*     */   }
/*     */ 
/*     */   protected void handleSpawned()
/*     */   {
/* 121 */     SpawnEventHandler.onSpawn(this);
/*     */   }
/*     */ 
/*     */   protected void handleRespawned()
/*     */   {
/* 126 */     SpawnEventHandler.onRespawn(this);
/*     */   }
/*     */ 
/*     */   protected void handleDespawned()
/*     */   {
/* 131 */     if (poll(AIQuestion.CAN_SHOUT))
/* 132 */       ShoutEventHandler.onBeforeDespawn(this);
/* 133 */     SpawnEventHandler.onDespawn(this);
/*     */   }
/*     */ 
/*     */   protected void handleDied()
/*     */   {
/* 138 */     DiedEventHandler.onSimpleDie(this);
/*     */   }
/*     */ 
/*     */   protected void handleMoveArrived()
/*     */   {
/* 143 */     if ((!(poll(AIQuestion.CAN_SHOUT))) || (getSpawnTemplate().getWalkerId() == null))
/* 144 */       return;
/* 145 */     ShoutEventHandler.onReachedWalkPoint(this);
/*     */   }
/*     */ 
/*     */   protected void handleTargetChanged(Creature creature)
/*     */   {
/* 150 */     super.handleMoveArrived();
/* 151 */     if (!(poll(AIQuestion.CAN_SHOUT)))
/* 152 */       return;
/* 153 */     ShoutEventHandler.onSwitchedTarget(this, creature);
/*     */   }
/*     */ 
/*     */   protected AIAnswer pollInstance(AIQuestion question)
/*     */   {
/* 158 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$poll$AIQuestion[question.ordinal()])
/*     */     {
/*     */     case 1:
/* 160 */       return NpcAIPolls.shouldDecay(this);
/*     */     case 2:
/* 162 */       return NpcAIPolls.shouldRespawn(this);
/*     */     case 3:
/* 164 */       return AIAnswers.POSITIVE;
/*     */     case 4:
/* 166 */       return ((isMayShout()) ? AIAnswers.POSITIVE : AIAnswers.NEGATIVE);
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isMayShout()
/*     */   {
/* 175 */     if (AIConfig.SHOUTS_ENABLE)
/* 176 */       return getOwner().mayShout(0);
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMoveSupported() {
/* 181 */     return ((getOwner().getGameStats().getMovementSpeedFloat() > 0.0F) && (!(isInSubState(AISubState.FREEZE))));
/*     */   }
/*     */ }