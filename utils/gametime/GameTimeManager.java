/*    */ package com.aionemu.gameserver.utils.gametime;
/*    */ 
/*    */ import com.aionemu.commons.database.dao.DAOManager;
/*    */ import com.aionemu.gameserver.dao.ServerVariablesDAO;
/*    */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class GameTimeManager
/*    */ {
/* 11 */   private static final Logger log = LoggerFactory.getLogger(GameTimeManager.class);
/*    */   private static GameTime instance;
/*    */   private static GameTimeUpdater updater;
/* 14 */   private static boolean clockStarted = false;
/*    */ 
/*    */   public static GameTime getGameTime()
/*    */   {
/* 22 */     return instance;
/*    */   }
/*    */ 
/*    */   public static void startClock() {
/* 26 */     if (clockStarted) {
/* 27 */       throw new IllegalStateException("Clock is already started");
/*    */     }
/* 29 */     updater = new GameTimeUpdater(getGameTime());
/* 30 */     ThreadPoolManager.getInstance().scheduleAtFixedRate(updater, 0L, 5000L);
/* 31 */     clockStarted = true;
/*    */   }
/*    */ 
/*    */   public static boolean saveTime() {
/* 35 */     return ((ServerVariablesDAO)DAOManager.getDAO(ServerVariablesDAO.class)).store("time", getGameTime().getTime());
/*    */   }
/*    */ 
/*    */   public static void reloadTime(int time) {
/* 39 */     ThreadPoolManager.getInstance().purge();
/* 40 */     instance = new GameTime(time);
/* 41 */     clockStarted = false;
/* 42 */     startClock();
/* 43 */     log.info("Game time changed by admin and clock restarted...");
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 17 */     ServerVariablesDAO dao = (ServerVariablesDAO)DAOManager.getDAO(ServerVariablesDAO.class);
/* 18 */     instance = new GameTime(dao.load("time"));
/*    */   }
/*    */ }