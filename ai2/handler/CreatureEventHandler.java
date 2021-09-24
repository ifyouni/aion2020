/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.AIState;
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import com.aionemu.gameserver.ai2.poll.AIQuestion;
/*    */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.questEngine.QuestEngine;
/*    */ import com.aionemu.gameserver.questEngine.model.QuestEnv;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ import com.aionemu.gameserver.world.MapRegion;
/*    */ import com.aionemu.gameserver.world.geo.GeoService;
/*    */ 
/*    */ public class CreatureEventHandler
/*    */ {
/*    */   public static void onCreatureMoved(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 37 */     checkAggro(npcAI, creature);
/* 38 */     if (creature instanceof Player) {
/* 39 */       Player player = (Player)creature;
/* 40 */       QuestEngine.getInstance().onAtDistance(new QuestEnv(npcAI.getOwner(), player, Integer.valueOf(0), Integer.valueOf(0)));
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void onCreatureSee(NpcAI2 npcAI, Creature creature)
/*    */   {
/* 49 */     checkAggro(npcAI, creature);
/* 50 */     if (creature instanceof Player) {
/* 51 */       Player player = (Player)creature;
/* 52 */       QuestEngine.getInstance().onAtDistance(new QuestEnv(npcAI.getOwner(), player, Integer.valueOf(0), Integer.valueOf(0)));
/*    */     }
/*    */   }
/*    */ 
/*    */   protected static void checkAggro(NpcAI2 ai, Creature creature)
/*    */   {
/* 62 */     Npc owner = ai.getOwner();
/*    */ 
/* 64 */     if (ai.isInState(AIState.FIGHT)) {
/* 65 */       return;
/*    */     }
/* 67 */     if (creature.getLifeStats().isAlreadyDead()) {
/* 68 */       return;
/*    */     }
/* 70 */     if (!(owner.canSee(creature))) {
/* 71 */       return;
/*    */     }
/* 73 */     if (!(owner.getActiveRegion().isMapRegionActive())) {
/* 74 */       return;
/*    */     }
/* 76 */     boolean isInAggroRange = false;
/* 77 */     if (ai.poll(AIQuestion.CAN_SHOUT)) {
/* 78 */       int shoutRange = owner.getObjectTemplate().getMinimumShoutRange();
/* 79 */       double distance = MathUtil.getDistance(owner, creature);
/* 80 */       if (distance <= shoutRange) {
/* 81 */         ShoutEventHandler.onSee(ai, creature);
/* 82 */         isInAggroRange = shoutRange <= owner.getObjectTemplate().getAggroRange();
/*    */       }
/*    */     }
/* 85 */     if ((ai.isInState(AIState.FIGHT)) || ((!(isInAggroRange)) && (!(MathUtil.isIn3dRange(owner, creature, owner.getObjectTemplate().getAggroRange())))) || 
/* 86 */       (!(owner.isAggressiveTo(creature))) || (!(GeoService.getInstance().canSee(owner, creature)))) return;
/* 87 */     if (!(ai.isInState(AIState.RETURNING)))
/* 88 */       ai.getOwner().getMoveController().storeStep();
/* 89 */     if (ai.canThink())
/* 90 */       ai.onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
/*    */   }
/*    */ }