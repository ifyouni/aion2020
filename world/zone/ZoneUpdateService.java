/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.controllers.CreatureController;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.taskmanager.AbstractFIFOPeriodicTaskManager;
/*    */ 
/*    */ public class ZoneUpdateService extends AbstractFIFOPeriodicTaskManager<Creature>
/*    */ {
/*    */   private ZoneUpdateService()
/*    */   {
/* 25 */     super(500);
/*    */   }
/*    */ 
/*    */   protected void callTask(Creature creature)
/*    */   {
/* 30 */     creature.getController().refreshZoneImpl();
/* 31 */     if (creature instanceof Player)
/* 32 */       ZoneLevelService.checkZoneLevels((Player)creature);
/*    */   }
/*    */ 
/*    */   protected String getCalledMethodName()
/*    */   {
/* 38 */     return "ZoneUpdateService()";
/*    */   }
/*    */ 
/*    */   public static ZoneUpdateService getInstance() {
/* 42 */     return SingletonHolder.instance;
/*    */   }
/*    */ 
/*    */   private static class SingletonHolder
/*    */   {
/* 48 */     protected static final ZoneUpdateService instance = new ZoneUpdateService(null);
/*    */   }
/*    */ }