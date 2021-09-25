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
/*    */ public class InvasionZoneInstance extends ZoneInstance
/*    */ {
/* 32 */   private static final Logger log = LoggerFactory.getLogger(InvasionZoneInstance.class);
/* 33 */   private FastMap<Integer, Player> players = new FastMap();
/*    */ 
/*    */   public InvasionZoneInstance(int mapId, ZoneInfo template)
/*    */   {
/* 41 */     super(mapId, template);
/*    */   }
/*    */ 
/*    */   public boolean onEnter(Creature creature)
/*    */   {
/* 46 */     if (super.onEnter(creature)) {
/* 47 */       if (creature instanceof Player) {
/* 48 */         this.players.put(creature.getObjectId(), (Player)creature);
/*    */       }
/* 50 */       return true;
/*    */     }
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean onLeave(Creature creature)
/*    */   {
/* 57 */     if (super.onLeave(creature)) {
/* 58 */       if (creature instanceof Player) {
/* 59 */         this.players.remove(creature.getObjectId());
/*    */       }
/* 61 */       return true;
/*    */     }
/* 63 */     return false; }
/*    */ 
/*    */   public void doOnAllPlayers(Visitor<Player> visitor) {
/*    */     FastMap.Entry e;
/*    */     try {
/* 68 */       e = this.players.head(); for (FastMap.Entry mapEnd = this.players.tail(); (e = e.getNext()) != mapEnd; ) {
/* 69 */         Player player = (Player)e.getValue();
/* 70 */         if (player != null)
/* 71 */           visitor.visit(player);
/*    */       }
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 76 */       log.error("Exception when running visitor on all players" + ex);
/*    */     }
/*    */   }
/*    */ }