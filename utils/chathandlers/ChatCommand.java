/*    */ package com.aionemu.gameserver.utils.chathandlers;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public abstract class ChatCommand
/*    */ {
/*    */   private String alias;
/*    */   private Byte level;
/* 15 */   static final String[] EMPTY_PARAMS = new String[0];
/* 16 */   static final Logger log = LoggerFactory.getLogger(ChatCommand.class);
/*    */ 
/*    */   public ChatCommand(String alias) {
/* 19 */     this.alias = alias;
/*    */   }
/*    */ 
/*    */   public boolean run(Player player, String[] params) {
/*    */     try {
/* 24 */       execute(player, params);
/* 25 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 28 */       log.error("", e);
/* 29 */       onFail(player, e.getMessage()); }
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   public final String getAlias()
/*    */   {
/* 35 */     return this.alias;
/*    */   }
/*    */ 
/*    */   public void setAccessLevel(Byte level) {
/* 39 */     this.level = level;
/*    */   }
/*    */ 
/*    */   public final Byte getLevel() {
/* 43 */     return this.level;
/*    */   }
/*    */ 
/*    */   abstract boolean checkLevel(Player paramPlayer);
/*    */ 
/*    */   abstract boolean process(Player paramPlayer, String paramString);
/*    */ 
/*    */   public abstract void execute(Player paramPlayer, String[] paramArrayOfString);
/*    */ 
/*    */   public void onFail(Player player, String message) {
/* 53 */     PacketSendUtility.sendMessage(player, message);
/*    */   }
/*    */ }