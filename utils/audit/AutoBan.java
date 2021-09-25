/*    */ package com.aionemu.gameserver.utils.audit;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.PunishmentConfig;
/*    */ import com.aionemu.gameserver.model.account.Account;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.network.BannedMacManager;
/*    */ import com.aionemu.gameserver.network.aion.AionConnection;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
/*    */ import com.aionemu.gameserver.network.loginserver.LoginServer;
/*    */ import com.aionemu.gameserver.services.PunishmentService;
/*    */ 
/*    */ public class AutoBan
/*    */ {
/*    */   protected static void punishment(Player player, String message)
/*    */   {
/* 29 */     String reason = "AUTO " + message;
/* 30 */     String address = player.getClientConnection().getMacAddress();
/* 31 */     String accountIp = player.getClientConnection().getIP();
/* 32 */     int accountId = player.getClientConnection().getAccount().getId();
/* 33 */     int playerId = player.getObjectId().intValue();
/* 34 */     int time = PunishmentConfig.PUNISHMENT_TIME;
/* 35 */     int minInDay = 1440;
/* 36 */     int dayCount = (int)Math.floor(time / minInDay);
/*    */ 
/* 38 */     switch (PunishmentConfig.PUNISHMENT_TYPE)
/*    */     {
/*    */     case 1:
/* 40 */       player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
/* 41 */       break;
/*    */     case 2:
/* 43 */       PunishmentService.banChar(playerId, dayCount, reason);
/* 44 */       break;
/*    */     case 3:
/* 46 */       LoginServer.getInstance().sendBanPacket(1, accountId, accountIp, time, 0);
/* 47 */       break;
/*    */     case 4:
/* 49 */       LoginServer.getInstance().sendBanPacket(2, accountId, accountIp, time, 0);
/* 50 */       break;
/*    */     case 5:
/* 52 */       player.getClientConnection().closeNow();
/* 53 */       BannedMacManager.getInstance().banAddress(address, System.currentTimeMillis() + time * 60000, reason);
/*    */     }
/*    */   }
/*    */ }