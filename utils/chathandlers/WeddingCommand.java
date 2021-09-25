/*    */ package com.aionemu.gameserver.utils.chathandlers;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ 
/*    */ public abstract class WeddingCommand extends ChatCommand
/*    */ {
/*    */   public WeddingCommand(String alias)
/*    */   {
/* 12 */     super(alias);
/*    */   }
/*    */ 
/*    */   public boolean checkLevel(Player player)
/*    */   {
/* 17 */     return player.havePermission(getLevel().byteValue());
/*    */   }
/*    */ 
/*    */   boolean process(Player player, String text)
/*    */   {
/* 22 */     if (!(player.isMarried()))
/* 23 */       return false;
/* 24 */     String alias = getAlias();
/*    */ 
/* 26 */     if (!(checkLevel(player))) {
/* 27 */       PacketSendUtility.sendMessage(player, "You not have permission for use this command.");
/* 28 */       return true;
/*    */     }
/*    */ 
/* 31 */     boolean success = false;
/* 32 */     if (text.length() == alias.length())
/* 33 */       success = run(player, EMPTY_PARAMS);
/*    */     else {
/* 35 */       success = run(player, text.substring(alias.length() + 1).split(" "));
/*    */     }
/* 37 */     return success;
/*    */   }
/*    */ }