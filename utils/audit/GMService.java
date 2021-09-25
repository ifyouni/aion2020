/*     */ package com.aionemu.gameserver.utils.audit;
/*     */ 
/*     */ import com.aionemu.commons.database.dao.DAOManager;
/*     */ import com.aionemu.gameserver.configs.administration.AdminConfig;
/*     */ import com.aionemu.gameserver.configs.main.MembershipConfig;
/*     */ import com.aionemu.gameserver.dao.PlayerDAO;
/*     */ import com.aionemu.gameserver.model.ChatType;
/*     */ import com.aionemu.gameserver.model.account.Account;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.network.aion.AionConnection;
/*     */ import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
/*     */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javolution.util.FastMap;
/*     */ 
/*     */ public class GMService
/*     */ {
/*  33 */   private Map<Integer, Player> gms = new FastMap();
/*  34 */   private boolean announceAny = false;
/*     */   private List<Byte> announceList;
/*     */ 
/*     */   public static final GMService getInstance()
/*     */   {
/*  30 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   private GMService()
/*     */   {
/*  39 */     this.announceList = new ArrayList();
/*  40 */     this.announceAny = AdminConfig.ANNOUNCE_LEVEL_LIST.equals("*");
/*  41 */     if (this.announceAny) return;
/*     */     try {
/*  43 */       for (String level : AdminConfig.ANNOUNCE_LEVEL_LIST.split(","))
/*  44 */         this.announceList.add(Byte.valueOf(Byte.parseByte(level)));
/*     */     }
/*     */     catch (Exception e) {
/*  47 */       this.announceAny = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onPlayerLogin(Player player) {
/*  52 */     if (player.isGM())
/*  53 */       this.gms.put(player.getObjectId(), player);
/*     */   }
/*     */ 
/*     */   public void onPlayerLogedOut(Player player)
/*     */   {
/*  70 */     if (player.isGM())
/*  71 */       this.gms.remove(player.getObjectId());
/*     */   }
/*     */ 
/*     */   public Collection<Player> getGMs()
/*     */   {
/*  76 */     return this.gms.values();
/*     */   }
/*     */ 
/*     */   public void onPlayerAvailable(Player player) {
/*  80 */     if (player.isGM()) {
/*  81 */       this.gms.put(player.getObjectId(), player);
/*  82 */       String adminTag = "%s";
/*  83 */       StringBuilder sb = new StringBuilder(adminTag);
/*     */ 
/*  85 */       if (player.getClientConnection() == null) {
/*     */         return;
/*     */       }
/*  88 */       if (MembershipConfig.PREMIUM_TAG_DISPLAY) {
/*  89 */         switch (player.getClientConnection().getAccount().getMembership())
/*     */         {
/*     */         case 1:
/*  91 */           adminTag = sb.insert(0, MembershipConfig.TAG_PREMIUM.substring(0, 2)).toString();
/*  92 */           break;
/*     */         case 2:
/*  94 */           adminTag = sb.insert(0, MembershipConfig.TAG_VIP.substring(0, 2)).toString();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  99 */       if (player.isMarried()) {
/* 100 */         String partnerName = ((PlayerDAO)DAOManager.getDAO(PlayerDAO.class)).getPlayerNameByObjId(player.getPartnerId());
/* 101 */         adminTag = new StringBuilder().append(adminTag).append("").append(partnerName).toString();
/*     */       }
/* 103 */       if (AdminConfig.CUSTOMTAG_ENABLE) {
/* 104 */         if (player.getAccessLevel() == 1)
/* 105 */           adminTag = AdminConfig.ADMIN_TAG_1.replace("%s", sb.toString());
/* 106 */         else if (player.getAccessLevel() == 2)
/* 107 */           adminTag = AdminConfig.ADMIN_TAG_2.replace("%s", sb.toString());
/* 108 */         else if (player.getAccessLevel() == 3)
/* 109 */           adminTag = AdminConfig.ADMIN_TAG_3.replace("%s", sb.toString());
/* 110 */         else if (player.getAccessLevel() == 4)
/* 111 */           adminTag = AdminConfig.ADMIN_TAG_4.replace("%s", sb.toString());
/* 112 */         else if (player.getAccessLevel() == 5)
/* 113 */           adminTag = AdminConfig.ADMIN_TAG_5.replace("%s", sb.toString());
/* 114 */         else if (player.getAccessLevel() == 6)
/* 115 */           adminTag = AdminConfig.ADMIN_TAG_6.replace("%s", sb.toString());
/* 116 */         else if (player.getAccessLevel() == 7)
/* 117 */           adminTag = AdminConfig.ADMIN_TAG_7.replace("%s", sb.toString());
/* 118 */         else if (player.getAccessLevel() == 8)
/* 119 */           adminTag = AdminConfig.ADMIN_TAG_8.replace("%s", sb.toString());
/* 120 */         else if (player.getAccessLevel() == 9)
/* 121 */           adminTag = AdminConfig.ADMIN_TAG_9.replace("%s", sb.toString());
/* 122 */         else if (player.getAccessLevel() == 10) {
/* 123 */           adminTag = AdminConfig.ADMIN_TAG_10.replace("%s", sb.toString());
/*     */         }
/*     */       }
/*     */ 
/* 127 */       Iterator iter = World.getInstance().getPlayersIterator();
/* 128 */       while (iter.hasNext())
/* 129 */         PacketSendUtility.sendBrightYellowMessageOnCenter((Player)iter.next(), new StringBuilder().append("Information : ").append(String.format(adminTag, new Object[] { player.getName() })).append("Is Now Available For Support!!").toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onPlayerUnavailable(Player player)
/*     */   {
/* 135 */     this.gms.remove(player.getObjectId());
/* 136 */     String adminTag = "%s";
/* 137 */     StringBuilder sb = new StringBuilder(adminTag);
/*     */ 
/* 141 */     if (MembershipConfig.PREMIUM_TAG_DISPLAY) {
/* 142 */       switch (player.getClientConnection().getAccount().getMembership())
/*     */       {
/*     */       case 1:
/* 144 */         adminTag = sb.insert(0, MembershipConfig.TAG_PREMIUM.substring(0, 2)).toString();
/* 145 */         break;
/*     */       case 2:
/* 147 */         adminTag = sb.insert(0, MembershipConfig.TAG_VIP.substring(0, 2)).toString();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 152 */     if (player.isMarried()) {
/* 153 */       String partnerName = ((PlayerDAO)DAOManager.getDAO(PlayerDAO.class)).getPlayerNameByObjId(player.getPartnerId());
/* 154 */       adminTag = new StringBuilder().append(adminTag).append("").append(partnerName).toString();
/*     */     }
/*     */ 
/* 157 */     if (AdminConfig.CUSTOMTAG_ENABLE) {
/* 158 */       if (player.getAccessLevel() == 1)
/* 159 */         adminTag = AdminConfig.ADMIN_TAG_1.replace("%s", sb.toString());
/* 160 */       else if (player.getAccessLevel() == 2)
/* 161 */         adminTag = AdminConfig.ADMIN_TAG_2.replace("%s", sb.toString());
/* 162 */       else if (player.getAccessLevel() == 3)
/* 163 */         adminTag = AdminConfig.ADMIN_TAG_3.replace("%s", sb.toString());
/* 164 */       else if (player.getAccessLevel() == 4)
/* 165 */         adminTag = AdminConfig.ADMIN_TAG_4.replace("%s", sb.toString());
/* 166 */       else if (player.getAccessLevel() == 5)
/* 167 */         adminTag = AdminConfig.ADMIN_TAG_5.replace("%s", sb.toString());
/* 168 */       else if (player.getAccessLevel() == 6)
/* 169 */         adminTag = AdminConfig.ADMIN_TAG_6.replace("%s", sb.toString());
/* 170 */       else if (player.getAccessLevel() == 7)
/* 171 */         adminTag = AdminConfig.ADMIN_TAG_7.replace("%s", sb.toString());
/* 172 */       else if (player.getAccessLevel() == 8)
/* 173 */         adminTag = AdminConfig.ADMIN_TAG_8.replace("%s", sb.toString());
/* 174 */       else if (player.getAccessLevel() == 9)
/* 175 */         adminTag = AdminConfig.ADMIN_TAG_9.replace("%s", sb.toString());
/* 176 */       else if (player.getAccessLevel() == 10) {
/* 177 */         adminTag = AdminConfig.ADMIN_TAG_10.replace("%s", sb.toString());
/*     */       }
/*     */     }
/*     */ 
/* 181 */     Iterator iter = World.getInstance().getPlayersIterator();
/* 182 */     while (iter.hasNext())
/* 183 */       PacketSendUtility.sendBrightYellowMessageOnCenter((Player)iter.next(), new StringBuilder().append("Information : ").append(String.format(adminTag, new Object[] { player.getName() })).append("Is Now Unavailable For Support!!").toString());
/*     */   }
/*     */ 
/*     */   public void broadcastMesage(String message)
/*     */   {
/* 188 */     SM_MESSAGE packet = new SM_MESSAGE(0, null, message, ChatType.YELLOW);
/* 189 */     for (Player player : this.gms.values())
/* 190 */       PacketSendUtility.sendPacket(player, packet);
/*     */   }
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/* 197 */     protected static final GMService instance = new GMService(null);
/*     */   }
/*     */ }