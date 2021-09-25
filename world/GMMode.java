/*    */ package com.aionemu.gameserver.world;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.motion.MotionList;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ import com.aionemu.gameserver.utils.audit.GMService;
/*    */ import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
/*    */ 
/*    */ public class GMMode extends AdminCommand
/*    */ {
/*    */   public GMMode()
/*    */   {
/* 29 */     super("gm");
/*    */   }
/*    */ 
/*    */   public void execute(Player admin, String[] params)
/*    */   {
/* 34 */     if (admin.getAccessLevel() < 1) {
/* 35 */       PacketSendUtility.sendMessage(admin, "You cannot use this command.");
/* 36 */       return;
/*    */     }
/*    */ 
/* 39 */     if (params.length != 2) {
/* 40 */       onFail(admin, null);
/* 41 */       return;
/*    */     }
/*    */ 
/* 44 */     if (params[0].toLowerCase().equals("on")) {
/* 45 */       if (params[1].equals("y")) {
/* 46 */         GMService.getInstance().onPlayerAvailable(admin);
/* 47 */         admin.setWispable();
/* 48 */       } else if (params[1].toLowerCase().equals("n")) {
/* 49 */         PacketSendUtility.sendMessage(admin, "You are Back Online");
/* 50 */         admin.setWispable();
/*    */       } else {
/* 52 */         admin.setWispable();
/* 53 */         PacketSendUtility.sendMessage(admin, "You are Back Online with GM Tag");
/*    */       }
/*    */ 
/* 56 */       if (!(admin.isGmMode())) {
/* 57 */         admin.setGmMode(true);
/*    */ 
/* 61 */         admin.clearKnownlist();
/* 62 */         PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
/* 63 */         PacketSendUtility.sendPacket(admin, new SM_MOTION(admin.getObjectId().intValue(), admin.getMotions().getActiveMotions()));
/* 64 */         admin.updateKnownlist();
/* 65 */         PacketSendUtility.sendMessage(admin, "you are now Available and Wispable by players");
/*    */       }
/*    */     }
/* 68 */     if (params[0].equals("off")) {
/* 69 */       if (params[1].toLowerCase().equals("y")) {
/* 70 */         GMService.getInstance().onPlayerUnavailable(admin);
/* 71 */         GMService.getInstance().onPlayerLogedOut(admin);
/* 72 */       } else if (params[1].toLowerCase().equals("n")) {
/* 73 */         PacketSendUtility.sendMessage(admin, "You are in Offline Status");
/* 74 */         PacketSendUtility.sendMessage(admin, "you are now Unavailable but can be Whisperable by players");
/*    */       } else {
/* 76 */         PacketSendUtility.sendMessage(admin, "You are Offline without GM Tag, But people can Whisper you.");
/*    */       }
/* 78 */       if (admin.isGmMode()) {
/* 79 */         admin.setGmMode(false);
/*    */ 
/* 81 */         admin.clearKnownlist();
/* 82 */         PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
/* 83 */         PacketSendUtility.sendPacket(admin, new SM_MOTION(admin.getObjectId().intValue(), admin.getMotions().getActiveMotions()));
/* 84 */         admin.updateKnownlist();
/* 85 */         PacketSendUtility.sendMessage(admin, "You are unavailable to players now.");
/*    */       }
/*    */     }
/* 88 */     if (!(params[0].equalsIgnoreCase("detector")))
/*    */       return;
/*    */   }
/*    */ 
/*    */   public void onFail(Player admin, String message)
/*    */   {
/* 97 */     String syntax = "syntax //gm <on|off> <y/n>\n y = You want to announce the players, that you are On\nAlso your Whisperable state changes to 'Whisperable'\n n = You don't want to announce the players, + You 'Whisperable' State goes Off";
/* 98 */     PacketSendUtility.sendMessage(admin, syntax);
/*    */   }
/*    */ }