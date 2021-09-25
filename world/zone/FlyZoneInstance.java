/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
/*    */ import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
/*    */ import com.aionemu.gameserver.model.templates.zone.ZoneType;
/*    */ import com.aionemu.gameserver.utils.audit.AuditLogger;
/*    */ 
/*    */ public class FlyZoneInstance extends ZoneInstance
/*    */ {
/*    */   public FlyZoneInstance(int mapId, ZoneInfo template)
/*    */   {
/* 28 */     super(mapId, template);
/*    */   }
/*    */ 
/*    */   public synchronized boolean onEnter(Creature creature)
/*    */   {
/* 33 */     if (super.onEnter(creature)) {
/* 34 */       creature.setInsideZoneType(ZoneType.FLY);
/* 35 */       return true;
/*    */     }
/*    */ 
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean onLeave(Creature creature)
/*    */   {
/* 44 */     if (super.onLeave(creature)) {
/* 45 */       creature.unsetInsideZoneType(ZoneType.FLY);
/* 46 */       if ((creature.isInState(CreatureState.FLYING)) && (!(creature.isInState(CreatureState.FLIGHT_TELEPORT))) && 
/* 47 */         (creature instanceof Player)) {
/* 48 */         AuditLogger.info((Player)creature, "On leave Fly zone in fly state!!");
/*    */       }
/* 50 */       return true;
/*    */     }
/*    */ 
/* 53 */     return false;
/*    */   }
/*    */ }