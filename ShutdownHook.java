/*     */ package com.aionemu.gameserver;
/*     */ 
/*     */ import com.aionemu.commons.services.CronService;
/*     */ import com.aionemu.commons.utils.concurrent.RunnableStatsManager;
/*     */ import com.aionemu.commons.utils.concurrent.RunnableStatsManager.SortBy;
/*     */ import com.aionemu.gameserver.configs.main.ShutdownConfig;
/*     */ import com.aionemu.gameserver.controllers.PlayerController;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.network.aion.AionConnection;
/*     */ import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
/*     */ import com.aionemu.gameserver.network.loginserver.LoginServer;
/*     */ import com.aionemu.gameserver.services.PeriodicSaveService;
/*     */ import com.aionemu.gameserver.services.player.PlayerLeaveWorldService;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.utils.gametime.GameTimeManager;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import java.util.Iterator;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ShutdownHook extends Thread
/*     */ {
/*  39 */   private static final Logger log = LoggerFactory.getLogger(ShutdownHook.class);
/*     */ 
/*     */   public static ShutdownHook getInstance() {
/*  42 */     return SingletonHolder.INSTANCE;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*  47 */     if (ShutdownConfig.HOOK_MODE == 1) {
/*  48 */       shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.SHUTDOWN);
/*     */     }
/*  50 */     else if (ShutdownConfig.HOOK_MODE == 2)
/*  51 */       shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.RESTART);
/*     */   }
/*     */ 
/*     */   private void sendShutdownMessage(int seconds)
/*     */   {
/*     */     try
/*     */     {
/*  73 */       Iterator onlinePlayers = World.getInstance().getPlayersIterator();
/*  74 */       if (!(onlinePlayers.hasNext()))
/*  75 */         return;
/*  76 */       while (onlinePlayers.hasNext()) {
/*  77 */         Player player = (Player)onlinePlayers.next();
/*  78 */         if ((player != null) && (player.getClientConnection() != null))
/*  79 */           player.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.STR_SERVER_SHUTDOWN(String.valueOf(seconds)));
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  83 */       log.error(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void sendShutdownStatus(boolean status) {
/*     */     try {
/*  89 */       Iterator onlinePlayers = World.getInstance().getPlayersIterator();
/*  90 */       if (!(onlinePlayers.hasNext()))
/*  91 */         return;
/*  92 */       while (onlinePlayers.hasNext()) {
/*  93 */         Player player = (Player)onlinePlayers.next();
/*  94 */         if ((player != null) && (player.getClientConnection() != null))
/*  95 */           player.getController().setInShutdownProgress(status);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  99 */       log.error(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void shutdownHook(int duration, int interval, ShutdownMode mode) {
/* 104 */     for (int i = duration; i >= interval; i -= interval) {
/*     */       try {
/* 106 */         if (World.getInstance().getPlayersIterator().hasNext()) {
/* 107 */           log.info("Runtime is " + mode.getText() + " in " + i + " seconds.");
/* 108 */           sendShutdownMessage(i);
/* 109 */           sendShutdownStatus(ShutdownConfig.SAFE_REBOOT);
/*     */         }
/*     */         else {
/* 112 */           log.info("Runtime is " + mode.getText() + " now ...");
/* 113 */           break label165:
/*     */         }
/*     */ 
/* 116 */         if (i > interval) {
/* 117 */           sleep(interval * 1000);
/*     */         }
/*     */         else
/* 120 */           sleep(i * 1000);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 124 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 129 */     label165: LoginServer.getInstance().gameServerDisconnected();
/*     */ 
/* 133 */     Iterator onlinePlayers = World.getInstance().getPlayersIterator();
/* 134 */     while (onlinePlayers.hasNext()) {
/* 135 */       Player activePlayer = (Player)onlinePlayers.next();
/*     */       try {
/* 137 */         PlayerLeaveWorldService.startLeaveWorld(activePlayer);
/*     */       }
/*     */       catch (Exception e) {
/* 140 */         log.error("Error while saving player " + e.getMessage());
/*     */       }
/*     */     }
/* 143 */     log.info("All players are disconnected...");
/*     */ 
/* 145 */     RunnableStatsManager.dumpClassStats(RunnableStatsManager.SortBy.AVG);
/* 146 */     PeriodicSaveService.getInstance().onShutdown();
/*     */ 
/* 149 */     GameTimeManager.saveTime();
/*     */ 
/* 151 */     CronService.getInstance().shutdown();
/*     */ 
/* 153 */     ThreadPoolManager.getInstance().shutdown();
/*     */ 
/* 156 */     if (mode == ShutdownMode.RESTART)
/* 157 */       Runtime.getRuntime().halt(2);
/*     */     else {
/* 159 */       Runtime.getRuntime().halt(0);
/*     */     }
/* 161 */     log.info("Runtime is " + mode.getText() + " now...");
/*     */   }
/*     */ 
/*     */   public void doShutdown(int delay, int announceInterval, ShutdownMode mode)
/*     */   {
/* 170 */     shutdownHook(delay, announceInterval, mode);
/*     */   }
/*     */ 
/*     */   private static final class SingletonHolder
/*     */   {
/* 175 */     private static final ShutdownHook INSTANCE = new ShutdownHook();
/*     */   }
/*     */ 
/*     */   public static enum ShutdownMode
/*     */   {
/*  55 */     NONE, SHUTDOWN, RESTART;
/*     */ 
/*     */     private final String text;
/*     */ 
/*     */     public String getText()
/*     */     {
/*  67 */       return this.text;
/*     */     }
/*     */   }
/*     */ }