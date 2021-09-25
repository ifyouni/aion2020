/*    */ package com.aionemu.gameserver.utils.chathandlers;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.LoggingConfig;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public abstract class AdminCommand extends ChatCommand
/*    */ {
/* 15 */   static final Logger log = LoggerFactory.getLogger("ADMINAUDIT_LOG");
/*    */ 
/*    */   public AdminCommand(String alias) {
/* 18 */     super(alias);
/*    */   }
/*    */ 
/*    */   public boolean checkLevel(Player player)
/*    */   {
/* 23 */     return (player.getAccessLevel() >= getLevel().byteValue());
/*    */   }
/*    */ 
/*    */   boolean process(Player player, String text)
/*    */   {
/* 29 */     if (!(checkLevel(player))) {
/* 30 */       if (LoggingConfig.LOG_GMAUDIT) {
/* 31 */         log.info("[ADMIN COMMAND] > [Player: " + player.getName() + "] has tried to use the command " + getAlias() + " without having the rights");
/*    */       }
/* 33 */       if (player.isGM()) {
/* 34 */         PacketSendUtility.sendMessage(player, "[WARN] You need to have access level " + getLevel() + " or more to use " + getAlias());
/* 35 */         return true;
/*    */       }
/* 37 */       return false;
/*    */     }
/*    */ 
/* 40 */     boolean success = false;
/* 41 */     if (text.length() == getAlias().length())
/* 42 */       success = run(player, EMPTY_PARAMS);
/*    */     else {
/* 44 */       success = run(player, text.substring(getAlias().length() + 1).split(" "));
/*    */     }
/* 46 */     if (LoggingConfig.LOG_GMAUDIT) {
/* 47 */       if ((player.getTarget() != null) && (player.getTarget() instanceof Creature)) {
/* 48 */         Creature target = (Creature)player.getTarget();
/* 49 */         log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "][Target : " + target.getName() + "]: " + text);
/*    */       }
/*    */       else {
/* 52 */         log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "]: " + text);
/*    */       }
/*    */     }
/* 55 */     if (!(success)) {
/* 56 */       PacketSendUtility.sendMessage(player, "<You have failed to execute " + text + ">");
/* 57 */       return true;
/*    */     }
/*    */ 
/* 60 */     return success;
/*    */   }
/*    */ }