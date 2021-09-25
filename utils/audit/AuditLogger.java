/*    */ package com.aionemu.gameserver.utils.audit;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.LoggingConfig;
/*    */ import com.aionemu.gameserver.configs.main.PunishmentConfig;
/*    */ import com.aionemu.gameserver.configs.main.SecurityConfig;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.google.common.base.Preconditions;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class AuditLogger
/*    */ {
/* 28 */   private static final Logger log = LoggerFactory.getLogger("AUDIT_LOG");
/*    */ 
/*    */   public static final void info(Player player, String message) {
/* 31 */     Preconditions.checkNotNull(player, "Player should not be null or use different info method");
/* 32 */     if (LoggingConfig.LOG_AUDIT) {
/* 33 */       info(player.getName(), player.getObjectId().intValue(), message);
/*    */     }
/* 35 */     if (PunishmentConfig.PUNISHMENT_ENABLE)
/* 36 */       AutoBan.punishment(player, message);
/*    */   }
/*    */ 
/*    */   public static final void info(String playerName, int objectId, String message)
/*    */   {
/* 41 */     message = message + " Player name: " + playerName + " objectId: " + objectId;
/* 42 */     log.info(message);
/*    */ 
/* 44 */     if (SecurityConfig.GM_AUDIT_MESSAGE_BROADCAST)
/* 45 */       GMService.getInstance().broadcastMesage(message);
/*    */   }
/*    */ }