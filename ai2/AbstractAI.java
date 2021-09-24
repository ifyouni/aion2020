/*     */ package com.aionemu.gameserver.ai2;
/*     */ 
/*     */ import com.aionemu.commons.callbacks.metadata.ObjectCallback;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventLog;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.ai2.eventcallback.OnHandleAIGeneralEvent;
/*     */ import com.aionemu.gameserver.ai2.handler.FollowEventHandler;
/*     */ import com.aionemu.gameserver.ai2.handler.FreezeEventHandler;
/*     */ import com.aionemu.gameserver.ai2.manager.SimpleAttackManager;
/*     */ import com.aionemu.gameserver.ai2.manager.WalkManager;
/*     */ import com.aionemu.gameserver.ai2.poll.AIAnswer;
/*     */ import com.aionemu.gameserver.ai2.poll.AIAnswers;
/*     */ import com.aionemu.gameserver.ai2.poll.AIQuestion;
/*     */ import com.aionemu.gameserver.ai2.scenario.AI2Scenario;
/*     */ import com.aionemu.gameserver.ai2.scenario.AI2Scenarios;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.controllers.movement.MoveController;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.TribeRelationsData;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*     */ import com.aionemu.gameserver.model.templates.item.ItemAttackType;
/*     */ import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.spawnengine.SpawnEngine;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.world.WorldPosition;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ public abstract class AbstractAI
/*     */   implements AI2
/*     */ {
/*     */   private Creature owner;
/*     */   private AIState currentState;
/*     */   private AISubState currentSubState;
/*  54 */   private final Lock thinkLock = new ReentrantLock();
/*     */ 
/*  56 */   private boolean logging = false;
/*     */   protected int skillId;
/*     */   protected int skillLevel;
/*     */   private volatile AIEventLog eventLog;
/*     */   private AI2Scenario scenario;
/*     */ 
/*     */   AbstractAI()
/*     */   {
/*  66 */     this.currentState = AIState.CREATED;
/*  67 */     this.currentSubState = AISubState.NONE;
/*  68 */     clearScenario();
/*     */   }
/*     */ 
/*     */   public AI2Scenario getScenario() {
/*  72 */     return this.scenario;
/*     */   }
/*     */ 
/*     */   public void setScenario(AI2Scenario scenario) {
/*  76 */     this.scenario = scenario;
/*     */   }
/*     */ 
/*     */   public void clearScenario() {
/*  80 */     this.scenario = AI2Scenarios.NO_SCENARIO;
/*     */   }
/*     */ 
/*     */   public AIEventLog getEventLog() {
/*  84 */     return this.eventLog;
/*     */   }
/*     */ 
/*     */   public AIState getState()
/*     */   {
/*  89 */     return this.currentState;
/*     */   }
/*     */ 
/*     */   public final boolean isInState(AIState state) {
/*  93 */     return (this.currentState == state);
/*     */   }
/*     */ 
/*     */   public AISubState getSubState()
/*     */   {
/*  98 */     return this.currentSubState;
/*     */   }
/*     */ 
/*     */   public final boolean isInSubState(AISubState subState) {
/* 102 */     return (this.currentSubState == subState);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 107 */     if (super.getClass().isAnnotationPresent(AIName.class)) {
/* 108 */       AIName annotation = (AIName)super.getClass().getAnnotation(AIName.class);
/* 109 */       return annotation.value();
/*     */     }
/* 111 */     return "noname";
/*     */   }
/*     */ 
/*     */   public int getSkillId() {
/* 115 */     return this.skillId;
/*     */   }
/*     */ 
/*     */   public int getSkillLevel() {
/* 119 */     return this.skillLevel;
/*     */   }
/*     */ 
/*     */   protected boolean canHandleEvent(AIEventType eventType) {
/* 123 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[this.currentState.ordinal()])
/*     */     {
/*     */     case 1:
/* 125 */       return StateEvents.DESPAWN_EVENTS.hasEvent(eventType);
/*     */     case 2:
/* 127 */       return StateEvents.DEAD_EVENTS.hasEvent(eventType);
/*     */     case 3:
/* 129 */       return StateEvents.CREATED_EVENTS.hasEvent(eventType);
/*     */     }
/* 131 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$event$AIEventType[eventType.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/* 134 */       return isNonFightingState();
/*     */     case 3:
/* 136 */       return ((getName().equals("trap")) || ((this.currentState != AIState.FIGHT) && (isNonFightingState())));
/*     */     }
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isNonFightingState() {
/* 142 */     return ((this.currentState == AIState.WALKING) || (this.currentState == AIState.IDLE));
/*     */   }
/*     */ 
/*     */   public synchronized boolean setStateIfNot(AIState newState) {
/* 146 */     if (this.currentState == newState) {
/* 147 */       if (isLogging()) {
/* 148 */         AI2Logger.info(this, "Can't change state to " + newState + " from " + this.currentState);
/*     */       }
/* 150 */       return false;
/*     */     }
/* 152 */     if (isLogging()) {
/* 153 */       AI2Logger.info(this, "Setting AI state to " + newState);
/* 154 */       if ((this.currentState == AIState.DIED) && (newState == AIState.FIGHT)) {
/* 155 */         StackTraceElement[] stack = new Throwable().getStackTrace();
/* 156 */         for (StackTraceElement elem : stack)
/* 157 */           AI2Logger.info(this, elem.toString());
/*     */       }
/*     */     }
/* 160 */     this.currentState = newState;
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean setSubStateIfNot(AISubState newSubState) {
/* 165 */     if (this.currentSubState == newSubState) {
/* 166 */       if (isLogging()) {
/* 167 */         AI2Logger.info(this, "Can't change substate to " + newSubState + " from " + this.currentSubState);
/*     */       }
/* 169 */       return false;
/*     */     }
/* 171 */     if (isLogging()) {
/* 172 */       AI2Logger.info(this, "Setting AI substate to " + newSubState);
/*     */     }
/* 174 */     this.currentSubState = newSubState;
/* 175 */     return true;
/*     */   }
/*     */ 
/*     */   public void onGeneralEvent(AIEventType event)
/*     */   {
/* 180 */     if (canHandleEvent(event)) {
/* 181 */       if (isLogging()) {
/* 182 */         AI2Logger.info(this, "General event " + event);
/*     */       }
/* 184 */       handleGeneralEvent(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onCreatureEvent(AIEventType event, Creature creature)
/*     */   {
/* 190 */     Preconditions.checkNotNull(creature, "Creature must not be null");
/* 191 */     if (canHandleEvent(event)) {
/* 192 */       if (isLogging()) {
/* 193 */         AI2Logger.info(this, "Creature event " + event + ": " + creature.getObjectTemplate().getTemplateId());
/*     */       }
/* 195 */       handleCreatureEvent(event, creature);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onCustomEvent(int eventId, Object[] args)
/*     */   {
/* 201 */     if (isLogging()) {
/* 202 */       AI2Logger.info(this, "Custom event - id = " + eventId);
/*     */     }
/* 204 */     handleCustomEvent(eventId, args);
/*     */   }
/*     */ 
/*     */   public Creature getOwner()
/*     */   {
/* 213 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public int getObjectId() {
/* 217 */     return this.owner.getObjectId().intValue();
/*     */   }
/*     */ 
/*     */   public WorldPosition getPosition() {
/* 221 */     return this.owner.getPosition();
/*     */   }
/*     */ 
/*     */   public VisibleObject getTarget() {
/* 225 */     return this.owner.getTarget();
/*     */   }
/*     */ 
/*     */   public boolean isAlreadyDead() {
/* 229 */     return this.owner.getLifeStats().isAlreadyDead();
/*     */   }
/*     */ 
/*     */   void setOwner(Creature owner) {
/* 233 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public final boolean tryLockThink() {
/* 237 */     return this.thinkLock.tryLock();
/*     */   }
/*     */ 
/*     */   public final void unlockThink() {
/* 241 */     this.thinkLock.unlock();
/*     */   }
/*     */ 
/*     */   public final boolean isLogging()
/*     */   {
/* 246 */     return this.logging;
/*     */   }
/*     */ 
/*     */   public void setLogging(boolean logging) {
/* 250 */     this.logging = logging;
/*     */   }
/*     */ 
/*     */   protected abstract void handleActivate();
/*     */ 
/*     */   protected abstract void handleDeactivate();
/*     */ 
/*     */   protected abstract void handleSpawned();
/*     */ 
/*     */   protected abstract void handleRespawned();
/*     */ 
/*     */   protected abstract void handleDespawned();
/*     */ 
/*     */   protected abstract void handleDied();
/*     */ 
/*     */   protected abstract void handleMoveValidate();
/*     */ 
/*     */   protected abstract void handleMoveArrived();
/*     */ 
/*     */   protected abstract void handleAttackComplete();
/*     */ 
/*     */   protected abstract void handleFinishAttack();
/*     */ 
/*     */   protected abstract void handleTargetReached();
/*     */ 
/*     */   protected abstract void handleTargetTooFar();
/*     */ 
/*     */   protected abstract void handleTargetGiveup();
/*     */ 
/*     */   protected abstract void handleNotAtHome();
/*     */ 
/*     */   protected abstract void handleBackHome();
/*     */ 
/*     */   protected abstract void handleDropRegistered();
/*     */ 
/*     */   protected abstract void handleAttack(Creature paramCreature);
/*     */ 
/*     */   protected abstract boolean handleCreatureNeedsSupport(Creature paramCreature);
/*     */ 
/*     */   protected abstract boolean handleGuardAgainstAttacker(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleCreatureSee(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleCreatureNotSee(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleCreatureMoved(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleCreatureAggro(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleTargetChanged(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleFollowMe(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleStopFollowMe(Creature paramCreature);
/*     */ 
/*     */   protected abstract void handleDialogStart(Player paramPlayer);
/*     */ 
/*     */   protected abstract void handleDialogFinish(Player paramPlayer);
/*     */ 
/*     */   protected abstract void handleCustomEvent(int paramInt, Object[] paramArrayOfObject);
/*     */ 
/*     */   public abstract boolean onPatternShout(ShoutEventType paramShoutEventType, String paramString, int paramInt);
/*     */ 
/*     */   @ObjectCallback(OnHandleAIGeneralEvent.class)
/*     */   protected void handleGeneralEvent(AIEventType event) {
/* 315 */     if (isLogging()) {
/* 316 */       AI2Logger.info(this, "Handle general event " + event);
/*     */     }
/* 318 */     logEvent(event);
/* 319 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$event$AIEventType[event.ordinal()])
/*     */     {
/*     */     case 4:
/* 321 */       handleMoveValidate();
/* 322 */       break;
/*     */     case 5:
/* 324 */       handleMoveArrived();
/* 325 */       break;
/*     */     case 6:
/* 327 */       handleSpawned();
/* 328 */       break;
/*     */     case 7:
/* 330 */       handleRespawned();
/* 331 */       break;
/*     */     case 8:
/* 333 */       handleDespawned();
/* 334 */       break;
/*     */     case 9:
/* 336 */       handleDied();
/* 337 */       break;
/*     */     case 10:
/* 339 */       handleAttackComplete();
/* 340 */       break;
/*     */     case 11:
/* 342 */       handleFinishAttack();
/* 343 */       break;
/*     */     case 12:
/* 345 */       handleTargetReached();
/* 346 */       break;
/*     */     case 13:
/* 348 */       handleTargetTooFar();
/* 349 */       break;
/*     */     case 14:
/* 351 */       handleTargetGiveup();
/* 352 */       break;
/*     */     case 15:
/* 354 */       handleNotAtHome();
/* 355 */       break;
/*     */     case 16:
/* 357 */       handleBackHome();
/* 358 */       break;
/*     */     case 17:
/* 360 */       handleActivate();
/* 361 */       break;
/*     */     case 18:
/* 363 */       handleDeactivate();
/* 364 */       break;
/*     */     case 19:
/* 366 */       FreezeEventHandler.onFreeze(this);
/* 367 */       break;
/*     */     case 20:
/* 369 */       FreezeEventHandler.onUnfreeze(this);
/* 370 */       break;
/*     */     case 21:
/* 372 */       handleDropRegistered();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void logEvent(AIEventType event)
/*     */   {
/* 381 */     if (AIConfig.EVENT_DEBUG) {
/* 382 */       if (this.eventLog == null) {
/* 383 */         synchronized (this) {
/* 384 */           if (this.eventLog == null) {
/* 385 */             this.eventLog = new AIEventLog(10);
/*     */           }
/*     */         }
/*     */       }
/* 389 */       this.eventLog.addFirst(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   void handleCreatureEvent(AIEventType event, Creature creature) {
/* 394 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$event$AIEventType[event.ordinal()])
/*     */     {
/*     */     case 22:
/* 396 */       if (DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(getOwner().getTribe(), creature.getTribe()))
/* 397 */         return;
/* 398 */       handleAttack(creature);
/* 399 */       logEvent(event);
/* 400 */       break;
/*     */     case 23:
/* 402 */       if ((!(handleCreatureNeedsSupport(creature))) && 
/* 403 */         (creature.getTarget() instanceof Creature) && 
/* 404 */         (!(handleCreatureNeedsSupport((Creature)creature.getTarget()))) && (!(handleGuardAgainstAttacker(creature)))) {
/* 405 */         handleGuardAgainstAttacker((Creature)creature.getTarget()); } 
/*     */ logEvent(event);
/* 409 */       break;
/*     */     case 24:
/* 411 */       handleCreatureSee(creature);
/* 412 */       break;
/*     */     case 25:
/* 414 */       handleCreatureNotSee(creature);
/* 415 */       break;
/*     */     case 3:
/* 417 */       handleCreatureMoved(creature);
/* 418 */       break;
/*     */     case 26:
/* 420 */       handleCreatureAggro(creature);
/* 421 */       logEvent(event);
/* 422 */       break;
/*     */     case 27:
/* 424 */       handleTargetChanged(creature);
/* 425 */       break;
/*     */     case 28:
/* 427 */       handleFollowMe(creature);
/* 428 */       logEvent(event);
/* 429 */       break;
/*     */     case 29:
/* 431 */       handleStopFollowMe(creature);
/* 432 */       logEvent(event);
/* 433 */       break;
/*     */     case 1:
/* 435 */       handleDialogStart((Player)creature);
/* 436 */       logEvent(event);
/* 437 */       break;
/*     */     case 2:
/* 439 */       handleDialogFinish((Player)creature);
/* 440 */       logEvent(event);
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21: }  } 
/*     */   public boolean poll(AIQuestion question) { AIAnswer instanceAnswer = pollInstance(question);
/* 448 */     if (instanceAnswer != null) {
/* 449 */       return instanceAnswer.isPositive();
/*     */     }
/* 451 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$poll$AIQuestion[question.ordinal()])
/*     */     {
/*     */     case 1:
/* 453 */       return isDestinationReached();
/*     */     case 2:
/* 455 */       return isCanSpawnOnDaytimeChange();
/*     */     case 3:
/* 457 */       return isMayShout();
/*     */     }
/* 459 */     return false;
/*     */   }
/*     */ 
/*     */   protected AIAnswer pollInstance(AIQuestion question)
/*     */   {
/* 469 */     return null;
/*     */   }
/*     */ 
/*     */   public AIAnswer ask(AIQuestion question)
/*     */   {
/* 474 */     return AIAnswers.NEGATIVE;
/*     */   }
/*     */ 
/*     */   protected boolean isDestinationReached()
/*     */   {
/* 479 */     AIState state = this.currentState;
/* 480 */     switch (1.$SwitchMap$com$aionemu$gameserver$ai2$AIState[state.ordinal()])
/*     */     {
/*     */     case 4:
/* 482 */       return MathUtil.isNearCoordinates(getOwner(), this.owner.getMoveController().getTargetX2(), this.owner.getMoveController().getTargetY2(), this.owner.getMoveController().getTargetZ2(), 1);
/*     */     case 5:
/* 485 */       return SimpleAttackManager.isTargetInAttackRange((Npc)this.owner);
/*     */     case 6:
/* 487 */       SpawnTemplate spawn = getOwner().getSpawn();
/* 488 */       return MathUtil.isNearCoordinates(getOwner(), spawn.getX(), spawn.getY(), spawn.getZ(), 1);
/*     */     case 7:
/* 490 */       return FollowEventHandler.isInRange(this, getOwner().getTarget());
/*     */     case 8:
/* 492 */       return ((this.currentSubState == AISubState.TALK) || (WalkManager.isArrivedAtPoint((NpcAI2)this)));
/*     */     }
/* 494 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isCanSpawnOnDaytimeChange() {
/* 498 */     return ((this.currentState == AIState.DESPAWNED) || (this.currentState == AIState.CREATED));
/*     */   }
/*     */ 
/*     */   public abstract boolean isMayShout();
/*     */ 
/*     */   public abstract AttackIntention chooseAttackIntention();
/*     */ 
/*     */   public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
/*     */   {
/* 507 */     return false;
/*     */   }
/*     */ 
/*     */   public long getRemainigTime()
/*     */   {
/* 512 */     return 0L;
/*     */   }
/*     */ 
/*     */   protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading)
/*     */   {
/* 519 */     return spawn(this.owner.getWorldId(), npcId, x, y, z, heading, 0, getPosition().getInstanceId());
/*     */   }
/*     */ 
/*     */   protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, int entityId)
/*     */   {
/* 526 */     return spawn(this.owner.getWorldId(), npcId, x, y, z, heading, entityId, getPosition().getInstanceId());
/*     */   }
/*     */ 
/*     */   protected VisibleObject spawn(int worldId, int npcId, float x, float y, float z, byte heading, int entityId, int instanceId)
/*     */   {
/* 531 */     SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
/* 532 */     template.setEntityId(entityId);
/* 533 */     return SpawnEngine.spawnObject(template, instanceId);
/*     */   }
/*     */ 
/*     */   public int modifyDamage(int damage)
/*     */   {
/* 538 */     return damage;
/*     */   }
/*     */ 
/*     */   public int modifyOwnerDamage(int damage)
/*     */   {
/* 543 */     return damage;
/*     */   }
/*     */ 
/*     */   public void onIndividualNpcEvent(Creature npc)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int modifyHealValue(int value)
/*     */   {
/* 552 */     return value;
/*     */   }
/*     */ 
/*     */   public int modifyMaccuracy(int value)
/*     */   {
/* 557 */     return value;
/*     */   }
/*     */ 
/*     */   public int modifySensoryRange(int value)
/*     */   {
/* 562 */     return value;
/*     */   }
/*     */ 
/*     */   public ItemAttackType modifyAttackType(ItemAttackType type)
/*     */   {
/* 567 */     return type;
/*     */   }
/*     */ }