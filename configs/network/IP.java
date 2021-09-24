/*    */ package com.aionemu.gameserver.configs.network;
/*    */ 
/*    */ import com.aionemu.gameserver.utils.Util;
/*    */ import com.aionemu.gameserver.utils.audit.aion.NetServers;
/*    */ import java.io.PrintStream;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class IP
/*    */ {
/* 15 */   public static final Logger log = LoggerFactory.getLogger(IP.class);
/*    */ 
/*    */   public static void print() {
/* 18 */     ShowCN();
/*    */   }
/*    */ 
/*    */   public static String getLocalIP()
/*    */   {
/*    */     byte[] ipAddr;
/* 23 */     String ipAddrStr = "";
/*    */     try
/*    */     {
/* 26 */       ipAddr = InetAddress.getLocalHost().getAddress();
/*    */     }
/*    */     catch (UnknownHostException e) {
/* 29 */       e.printStackTrace();
/* 30 */       return null;
/*    */     }
/* 32 */     for (int i = 0; i < ipAddr.length; ++i) {
/* 33 */       if (i > 0)
/* 34 */         ipAddrStr = ipAddrStr + ".";
/* 35 */       ipAddrStr = ipAddrStr + (ipAddr[i] & 0xFF);
/*    */     }
/* 37 */     return ipAddrStr;
/*    */   }
/*    */ 
/*    */   private static void ShowCN()
/*    */   {
/* 42 */     Util.printSection("授权信息");
/* 43 */     switch (NetServers.getNumber())
/*    */     {
/*    */     case 0:
/* 45 */       log.info("授权认证失败:进入单机模式.");
/* 46 */       break;
/*    */     case 1:
/* 48 */       log.info("授权认证成功:感谢您使用统一模拟器.");
/* 49 */       log.info("今天的时间是:" + NetServers.getNetTime());
/*    */ 
/* 52 */       break;
/*    */     case 2:
/* 54 */       log.info("网络认证模式，无法验证授权！");
/* 55 */       log.info("自动切换模式，进入单机模式！");
/* 56 */       break;
/*    */     case 3:
/* 58 */       log.info("您的包月更新时间已过，无法使用此版本！");
/* 59 */       log.info("今天的时间是:" + NetServers.getNetTime());
/*    */ 
/* 62 */       break;
/*    */     case 4:
/* 64 */       log.info("您的授权已经过期，请重新购买授权！");
/* 65 */       log.info("今天的时间是:" + NetServers.getNetTime());
/*    */     }
/*    */ 
/* 70 */     log.info("本机IP地址:" + getLocalIP());
/*    */ 
/* 72 */     System.out.println("                        ┌────────────┐                         ");
/* 73 */     System.out.println("┌───────────╡   统一永恒商业模拟器   ╞────────────┐");
/* 74 */     System.out.println("│                      └────────────┘                        │");
/* 75 */     System.out.println("│                         官方网站:WWW.CMCHS.CN                            │");
/* 76 */     System.out.println("│        认证只供个人研究和娱乐使用,请勿商业用途,当您使用就表示同意.       │");
/* 77 */     System.out.println("│                      ┌────────────┐                        │");
/* 78 */     System.out.println("└───────────╡   QQ交流群:609558543   ╞────────────┘");
/* 79 */     System.out.println("                        └────────────┘                         ");
/* 80 */     System.out.println("  ");
/*    */   }
/*    */ }