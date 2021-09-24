/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.NpcType;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ import com.aionemu.gameserver.world.MapRegion;
/*    */ import com.aionemu.gameserver.world.geo.GeoService;
/*    */ 
/*    */ public class SimpleAbyssGuardHandler
/*    */ {
/*    */   public static void onCreatureMoved(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 15 */     checkAggro(npcAI, creature);
/*    */   }
/*    */ 
/*    */   public static void onCreatureSee(NpcAI2 npcAI, Creature creature) {
/* 19 */     checkAggro(npcAI, creature);
/*    */   }
/*    */ 
/*    */   protected static void checkAggro(NpcAI2 ai, Creature creature) {
/* 23 */     if (!(creature instanceof Npc)) {
/* 24 */       CreatureEventHandler.checkAggro(ai, creature);
/* 25 */       return;
/*    */     }
/* 27 */     Npc owner = ai.getOwner();
/* 28 */     if ((creature.getLifeStats().isAlreadyDead()) || (!(owner.canSee(creature))))
/* 29 */       return;
/* 30 */     Npc npc = (Npc)creature;
/* 31 */     if (((npc.getNpcType() != NpcType.ATTACKABLE) && (npc.getNpcType() != NpcType.AGGRESSIVE)) || (npc.getLevel() < 2))
/* 32 */       return;
/* 33 */     if (creature.getTarget() != null)
/* 34 */       return;
/* 35 */     if (!(owner.getActiveRegion().isMapRegionActive()))
/* 36 */       return;
/* 37 */     if ((ai.isInState(AIState.FIGHT)) || (!(MathUtil.isIn3dRange(owner, creature, owner.getObjectTemplate().getAggroRange()))) || 
/* 38 */       (!(GeoService.getInstance().canSee(owner, creature)))) return;
/* 39 */     if (!(ai.isInState(AIState.RETURNING)))
/* 40 */       ai.getOwner().getMoveController().storeStep();
/* 41 */     ai.onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
/*    */   }
/*    */ }