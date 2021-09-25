/*    */ package com.aionemu.gameserver.utils.chathandlers;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ 
/*    */ public abstract class PlayerCommand extends ChatCommand
/*    */ {
/*    */   public PlayerCommand(String alias)
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
/* 22 */     if (!(checkLevel(player))) {
/* 23 */       PacketSendUtility.sendMessage(player, "You not have permission for use this command.");
/* 24 */       return true;
/*    */     }
/*    */ 
/* 27 */     boolean success = false;
/* 28 */     if (text.length() == getAlias().length())
/* 29 */       success = run(player, EMPTY_PARAMS);
/*    */     else {
/* 31 */       success = run(player, text.substring(getAlias().length() + 1).split(" "));
/*    */     }
/* 33 */     return success;
/*    */   }
/*    */ }