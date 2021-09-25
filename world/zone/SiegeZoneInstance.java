/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
/*    */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*    */ import javolution.util.FastMap;
/*    */ import javolution.util.FastMap.Entry;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class SiegeZoneInstance extends ZoneInstance
/*    */ {
/* 30 */   private static final Logger log = LoggerFactory.getLogger(SiegeZoneInstance.class);
/*    */ 
/* 32 */   private FastMap<Integer, Player> players = new FastMap();
/*    */ 
/*    */   public SiegeZoneInstance(int mapId, ZoneInfo template)
/*    */   {
/* 40 */     super(mapId, template);
/*    */   }
/*    */ 
/*    */   public boolean onEnter(Creature creature)
/*    */   {
/* 45 */     if (super.onEnter(creature)) {
/* 46 */       if (creature instanceof Player)
/* 47 */         this.players.put(creature.getObjectId(), (Player)creature);
/* 48 */       return true;
/*    */     }
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean onLeave(Creature creature)
/*    */   {
/* 55 */     if (super.onLeave(creature)) {
/* 56 */       if (creature instanceof Player)
/* 57 */         this.players.remove(creature.getObjectId());
/* 58 */       return true;
/*    */     }
/* 60 */     return false; }
/*    */ 
/*    */   public void doOnAllPlayers(Visitor<Player> visitor) {
/*    */     FastMap.Entry e;
/*    */     try {
/* 65 */       e = this.players.head(); for (FastMap.Entry mapEnd = this.players.tail(); (e = e.getNext()) != mapEnd; ) {
/* 66 */         Player player = (Player)e.getValue();
/* 67 */         if (player != null)
/* 68 */           visitor.visit(player);
/*    */       }
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 73 */       log.error("Exception when running visitor on all players" + ex);
/*    */     }
/*    */   }
/*    */ }