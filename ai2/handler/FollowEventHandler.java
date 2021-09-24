/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.AbstractAI;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import com.aionemu.gameserver.ai2.manager.EmoteManager;
/*    */ import com.aionemu.gameserver.controllers.NpcController;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ 
/*    */ public class FollowEventHandler
/*    */ {
/*    */   public static void follow(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 34 */     if (npcAI.setStateIfNot(AIState.FOLLOWING)) {
/* 35 */       npcAI.getOwner().setTarget(creature);
/* 36 */       EmoteManager.emoteStartFollowing(npcAI.getOwner());
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void creatureMoved(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 45 */     if ((!(npcAI.isInState(AIState.FOLLOWING))) || 
/* 46 */       (!(npcAI.getOwner().isTargeting(creature.getObjectId().intValue()))) || (creature.getLifeStats().isAlreadyDead())) return;
/* 47 */     checkFollowTarget(npcAI, creature);
/*    */   }
/*    */ 
/*    */   public static void checkFollowTarget(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 56 */     if (!(isInRange(npcAI, creature)))
/* 57 */       npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
/*    */   }
/*    */ 
/*    */   public static boolean isInRange(AbstractAI ai, VisibleObject object)
/*    */   {
/* 62 */     if (object == null)
/* 63 */       return false;
/* 64 */     if (object.isInInstance())
/* 65 */       return MathUtil.isIn3dRange(ai.getOwner(), object, 9999.0F);
/* 66 */     if (ai.getOwner().getLifeStats().getHpPercentage() < 100) {
/* 67 */       return MathUtil.isIn3dRange(ai.getOwner(), object, 30.0F);
/*    */     }
/* 69 */     return MathUtil.isIn3dRange(ai.getOwner(), object, 15.0F);
/*    */   }
/*    */ 
/*    */   public static void stopFollow(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 78 */     if (npcAI.setStateIfNot(AIState.IDLE)) {
/* 79 */       npcAI.getOwner().setTarget(null);
/* 80 */       npcAI.getOwner().getMoveController().abortMove();
/* 81 */       npcAI.getOwner().getController().scheduleRespawn();
/* 82 */       npcAI.getOwner().getController().onDelete();
/*    */     }
/*    */   }
/*    */ }