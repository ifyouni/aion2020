/*     */ package com.aionemu.gameserver.utils;
/*     */ 
/*     */ import com.aionemu.commons.objects.filter.ObjectFilter;
/*     */ import com.aionemu.gameserver.model.ChatType;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.team.legion.Legion;
/*     */ import com.aionemu.gameserver.network.aion.AionConnection;
/*     */ import com.aionemu.gameserver.network.aion.AionServerPacket;
/*     */ import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import com.aionemu.gameserver.world.knownlist.KnownList;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ import com.aionemu.gameserver.world.zone.SiegeZoneInstance;
/*     */ 
/*     */ public class PacketSendUtility
/*     */ {
/*     */   public static void sendMessage(Player player, String msg)
/*     */   {
/*  30 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.GOLDEN_YELLOW)); }
/*     */ 
/*     */   public static void sendWhiteMessage(Player player, String msg) {
/*  33 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE)); }
/*     */ 
/*     */   public static void sendWhiteMessageOnCenter(Player player, String msg) {
/*  36 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE_CENTER)); }
/*     */ 
/*     */   public static void sendYellowMessage(Player player, String msg) {
/*  39 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW)); }
/*     */ 
/*     */   public static void sendYellowMessageOnCenter(Player player, String msg) {
/*  42 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW_CENTER)); }
/*     */ 
/*     */   public static void sendBrightYellowMessage(Player player, String msg) {
/*  45 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW)); }
/*     */ 
/*     */   public static void sendBrightYellowMessageOnCenter(Player player, String msg) {
/*  48 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW_CENTER)); }
/*     */ 
/*     */   public static void sendSys1Message(Player player, String sender, String msg) {
/*  51 */     sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.GROUP_LEADER)); }
/*     */ 
/*     */   public static void sendSys2Message(Player player, String sender, String msg) {
/*  54 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE)); }
/*     */ 
/*     */   public static void sendSys3Message(Player player, String sender, String msg) {
/*  57 */     sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COMMAND)); }
/*     */ 
/*     */   public static void sendSys4Message(Player player, String sender, String msg) {
/*  60 */     sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.LEGION)); }
/*     */ 
/*     */   public static void sendSys5Message(Player player, String sender, String msg) {
/*  63 */     sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COALITION)); }
/*     */ 
/*     */   public static void sendSys6Message(Player player, String sender, String msg) {
/*  66 */     sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.LEAGUE)); }
/*     */ 
/*     */   public static void sendWarnMessageOnCenter(Player player, String msg) {
/*  69 */     sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.LEAGUE_ALERT));
/*     */   }
/*     */ 
/*     */   public static void sendPacket(Player player, AionServerPacket packet) {
/*  73 */     if (player.getClientConnection() != null)
/*  74 */       player.getClientConnection().sendPacket(packet);
/*     */   }
/*     */ 
/*     */   public static void playerSendPacketTime(Player player, AionServerPacket packet, int time)
/*     */   {
/*  82 */     ThreadPoolManager.getInstance().schedule(new Runnable(player, packet)
/*     */     {
/*     */       public void run() {
/*  85 */         if (this.val$player.getClientConnection() != null)
/*  86 */           this.val$player.getClientConnection().sendPacket(this.val$packet);
/*     */       }
/*     */     }
/*     */     , time);
/*     */   }
/*     */ 
/*     */   public static void npcSendPacketTime(Npc npc, AionServerPacket packet, int time)
/*     */   {
/*  96 */     ThreadPoolManager.getInstance().schedule(new Runnable(npc, packet)
/*     */     {
/*     */       public void run() {
/*  99 */         this.val$npc.getKnownList().doOnAllPlayers(new Visitor()
/*     */         {
/*     */           public void visit(Player player) {
/* 102 */             if (player.isOnline())
/* 103 */               PacketSendUtility.sendPacket(player, PacketSendUtility.2.this.val$packet);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     , time);
/*     */   }
/*     */ 
/*     */   public static void sendMessageTime(Player player, String message, int time)
/*     */   {
/* 112 */     ThreadPoolManager.getInstance().schedule(new Runnable(message)
/*     */     {
/*     */       public void run() {
/* 115 */         World.getInstance().doOnAllPlayers(new Visitor()
/*     */         {
/*     */           public void visit(Player player) {
/* 118 */             if (player.isOnline())
/* 119 */               PacketSendUtility.sendMessage(player, PacketSendUtility.3.this.val$message);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     , time);
/*     */   }
/*     */ 
/*     */   public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf)
/*     */   {
/* 128 */     if (toSelf)
/* 129 */       sendPacket(player, packet);
/* 130 */     broadcastPacket(player, packet);
/*     */   }
/*     */ 
/*     */   public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet) {
/* 134 */     if (visibleObject instanceof Player)
/* 135 */       sendPacket((Player)visibleObject, packet);
/* 136 */     broadcastPacket(visibleObject, packet);
/*     */   }
/*     */ 
/*     */   public static void broadcastPacket(VisibleObject visibleObject, AionServerPacket packet) {
/* 140 */     visibleObject.getKnownList().doOnAllPlayers(new Visitor(packet)
/*     */     {
/*     */       public void visit(Player player) {
/* 143 */         if (player.isOnline())
/* 144 */           PacketSendUtility.sendPacket(player, this.val$packet);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf, ObjectFilter<Player> filter)
/*     */   {
/* 152 */     if (toSelf) {
/* 153 */       sendPacket(player, packet);
/*     */     }
/* 155 */     player.getKnownList().doOnAllPlayers(new Visitor(filter, packet)
/*     */     {
/*     */       public void visit(Player object) {
/* 158 */         if (this.val$filter.acceptObject(object))
/* 159 */           PacketSendUtility.sendPacket(object, this.val$packet);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void broadcastPacket(VisibleObject visibleObject, AionServerPacket packet, int distance) {
/* 165 */     visibleObject.getKnownList().doOnAllPlayers(new Visitor(visibleObject, distance, packet)
/*     */     {
/*     */       public void visit(Player p) {
/* 168 */         if (MathUtil.isIn3dRange(this.val$visibleObject, p, this.val$distance))
/* 169 */           PacketSendUtility.sendPacket(p, this.val$packet);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void broadcastFilteredPacket(AionServerPacket packet, ObjectFilter<Player> filter)
/*     */   {
/* 176 */     World.getInstance().doOnAllPlayers(new Visitor(filter, packet)
/*     */     {
/*     */       public void visit(Player object)
/*     */       {
/* 180 */         if (this.val$filter.acceptObject(object))
/* 181 */           PacketSendUtility.sendPacket(object, this.val$packet);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet) {
/* 187 */     for (Player onlineLegionMember : legion.getOnlineLegionMembers())
/* 188 */       sendPacket(onlineLegionMember, packet);
/*     */   }
/*     */ 
/*     */   public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId)
/*     */   {
/* 193 */     for (Player onlineLegionMember : legion.getOnlineLegionMembers())
/* 194 */       if (onlineLegionMember.getObjectId().intValue() != playerObjId)
/* 195 */         sendPacket(onlineLegionMember, packet);
/*     */   }
/*     */ 
/*     */   public static void broadcastPacketToZone(SiegeZoneInstance zone, AionServerPacket packet)
/*     */   {
/* 200 */     zone.doOnAllPlayers(new Visitor(packet)
/*     */     {
/*     */       public void visit(Player player) {
/* 203 */         PacketSendUtility.sendPacket(player, this.val$packet);
/*     */       }
/*     */     });
/*     */   }
/*     */ }