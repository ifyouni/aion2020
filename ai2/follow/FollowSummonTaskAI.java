/*    */ package com.aionemu.gameserver.ai2.follow;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AI2;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import com.aionemu.gameserver.controllers.PlayerController;
/*    */ import com.aionemu.gameserver.model.TaskId;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Summon;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.summons.SummonMode;
/*    */ import com.aionemu.gameserver.model.summons.UnsummonType;
/*    */ import com.aionemu.gameserver.services.summons.SummonsService;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ import java.util.concurrent.Future;
/*    */ 
/*    */ public class FollowSummonTaskAI
/*    */   implements Runnable
/*    */ {
/*    */   private Creature target;
/*    */   private Summon summon;
/*    */   private Player master;
/*    */   private float targetX;
/*    */   private float targetY;
/*    */   private float targetZ;
/*    */   private Future<?> task;
/*    */ 
/*    */   public FollowSummonTaskAI(Creature target, Summon summon)
/*    */   {
/* 26 */     this.target = target;
/* 27 */     this.summon = summon;
/* 28 */     this.master = summon.getMaster();
/* 29 */     this.task = summon.getMaster().getController().getTask(TaskId.SUMMON_FOLLOW);
/* 30 */     setLeadingCoordinates();
/*    */   }
/*    */ 
/*    */   private void setLeadingCoordinates() {
/* 34 */     this.targetX = this.target.getX();
/* 35 */     this.targetY = this.target.getY();
/* 36 */     this.targetZ = this.target.getZ();
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 41 */     if ((this.target == null) || (this.summon == null) || (this.master == null)) {
/* 42 */       if (this.task != null) {
/* 43 */         this.task.cancel(true);
/*    */       }
/* 45 */       return;
/*    */     }
/* 47 */     if (!(isInMasterRange())) {
/* 48 */       SummonsService.doMode(SummonMode.RELEASE, this.summon, UnsummonType.DISTANCE);
/* 49 */       return;
/*    */     }
/* 51 */     if (!(isInTargetRange())) {
/* 52 */       if ((this.targetX != this.target.getX()) || (this.targetY != this.target.getY()) || (this.targetZ != this.target.getZ())) {
/* 53 */         setLeadingCoordinates();
/* 54 */         onOutOfTargetRange();
/*    */       }
/*    */     }
/* 57 */     else if (!(this.master.equals(this.target)))
/* 58 */       onDestination();
/*    */   }
/*    */ 
/*    */   private boolean isInTargetRange()
/*    */   {
/* 63 */     return MathUtil.isIn3dRange(this.target, this.summon, 2.0F);
/*    */   }
/*    */ 
/*    */   private boolean isInMasterRange() {
/* 67 */     return MathUtil.isIn3dRange(this.master, this.summon, 50.0F);
/*    */   }
/*    */ 
/*    */   protected void onDestination() {
/* 71 */     this.summon.getAi2().onCreatureEvent(AIEventType.ATTACK, this.target);
/*    */   }
/*    */ 
/*    */   private void onOutOfTargetRange() {
/* 75 */     this.summon.getAi2().onGeneralEvent(AIEventType.MOVE_VALIDATE);
/*    */   }
/*    */ }