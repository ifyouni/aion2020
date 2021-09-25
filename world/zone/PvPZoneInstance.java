/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
/*    */ import com.aionemu.gameserver.model.templates.zone.ZoneType;
/*    */ 
/*    */ public class PvPZoneInstance extends SiegeZoneInstance
/*    */ {
/*    */   public PvPZoneInstance(int mapId, ZoneInfo template)
/*    */   {
/* 10 */     super(mapId, template);
/*    */   }
/*    */ 
/*    */   public synchronized boolean onEnter(Creature creature)
/*    */   {
/* 15 */     if (super.onEnter(creature)) {
/* 16 */       creature.setInsideZoneType(ZoneType.PVP);
/* 17 */       return true;
/*    */     }
/* 19 */     return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean onLeave(Creature creature)
/*    */   {
/* 25 */     if (super.onLeave(creature)) {
/* 26 */       creature.unsetInsideZoneType(ZoneType.PVP);
/* 27 */       return true;
/*    */     }
/* 29 */     return false;
/*    */   }
/*    */ }