/*    */ package com.aionemu.gameserver.utils;
/*    */ 
/*    */ import com.aionemu.commons.utils.AEInfos;
/*    */ import com.aionemu.commons.versionning.Version;
/*    */ import com.aionemu.gameserver.GameServer;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class AEVersions
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(AEVersions.class);
/* 27 */   private static final Version commons = new Version(AEInfos.class);
/* 28 */   private static final Version gameserver = new Version(GameServer.class);
/*    */ 
/*    */   private static String getRevisionInfo(Version version) {
/* 31 */     return String.format("%-6s", new Object[] { version.getRevision() });
/*    */   }
/*    */ 
/*    */   private static String getBranchInfo(Version version) {
/* 35 */     return String.format("%-6s", new Object[] { version.getBranch() });
/*    */   }
/*    */ 
/*    */   private static String getBranchCommitTimeInfo(Version version) {
/* 39 */     return String.format("%-6s", new Object[] { version.getCommitTime() });
/*    */   }
/*    */ 
/*    */   private static String getDateInfo(Version version) {
/* 43 */     return String.format("[ %4s ]", new Object[] { version.getDate() });
/*    */   }
/*    */ 
/*    */   public static String[] getFullVersionInfo() {
/* 47 */     return new String[] { "公共库版本: " + getRevisionInfo(commons), "公共库分支: " + getDateInfo(commons), "GS 版本: " + getRevisionInfo(gameserver), "GS 分支: " + getBranchInfo(gameserver), "GS 分支提交: " + getBranchCommitTimeInfo(gameserver), "GS 编译日期: " + getDateInfo(gameserver), "..................................................", ".................................................." };
/*    */   }
/*    */ 
/*    */   public static void printFullVersionInfo()
/*    */   {
/* 55 */     for (String line : getFullVersionInfo())
/* 56 */       log.info(line);
/*    */   }
/*    */ 
/*    */   public static String getDate()
/*    */   {
/* 61 */     String time = gameserver.getDate();
/* 62 */     String[] block = time.trim().split(" ");
/* 63 */     return block[0];
/*    */   }
/*    */ }