/*    */ package com.aionemu.gameserver.ai2.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*    */ import com.aionemu.gameserver.controllers.NpcController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.model.templates.stats.NpcStatsTemplate;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ import com.aionemu.gameserver.world.geo.GeoService;
/*    */ 
/*    */ public class MoveEventHandler
/*    */ {
/*    */   public static final void onMoveValidate(NpcAI2 npcAI)
/*    */   {
/* 29 */     npcAI.getOwner().getController().onMove();
/* 30 */     TargetEventHandler.onTargetTooFar(npcAI);
/* 31 */     if ((npcAI.getOwner().getTarget() == null) || (npcAI.getOwner().getObjectTemplate().getStatsTemplate().getRunSpeed() == 0.0F) || 
/* 32 */       (GeoService.getInstance().canSee(npcAI.getOwner(), npcAI.getOwner().getTarget())) || (MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), 15.0F))) return;
/* 33 */     npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/*    */   }
/*    */ 
/*    */   public static final void onMoveArrived(NpcAI2 npcAI)
/*    */   {
/* 41 */     npcAI.getOwner().getController().onMove();
/* 42 */     TargetEventHandler.onTargetReached(npcAI);
/* 43 */     if ((npcAI.getOwner().getTarget() == null) || (npcAI.getOwner().getObjectTemplate().getStatsTemplate().getRunSpeed() == 0.0F) || 
/* 44 */       (GeoService.getInstance().canSee(npcAI.getOwner(), npcAI.getOwner().getTarget())) || (MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), 15.0F))) return;
/* 45 */     npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/*    */   }
/*    */ }