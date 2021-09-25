/*     */ package com.aionemu.gameserver.utils.audit.aion;
/*     */ 
/*     */ import com.aionemu.gameserver.utils.Util;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class NetServers
/*     */ {
/*  23 */   private static int number = 0;
/*     */   private static IpConfig ipconfig;
/*  53 */   private static boolean fail = false;
/*     */ 
/*     */   public static void setNumber(int num)
/*     */   {
/*  25 */     number = num; }
/*     */ 
/*     */   public static int getNumber() {
/*  28 */     return number;
/*     */   }
/*     */ 
/*     */   public static IpConfig getIpConfig()
/*     */   {
/*  42 */     return ipconfig;
/*     */   }
/*     */ 
/*     */   public static int getQQ() {
/*  46 */     return getIpConfig().getQQ();
/*     */   }
/*     */ 
/*     */   public static String getNetTime()
/*     */   {
/*  56 */     String nettime = null;
/*  57 */     String url = "https://www.baidu.com";
/*  58 */     if (fail)
/*  59 */       url = "https://www.baidu.com";
/*     */     try {
/*  61 */       URL U = new URL(url);
/*  62 */       URLConnection UC = U.openConnection();
/*  63 */       Date date = new Date(UC.getDate());
/*  64 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  65 */       nettime = sdf.format(date);
/*     */     }
/*     */     catch (Exception e) {
/*  68 */       if (!(fail))
/*     */       {
/*  70 */         fail = true;
/*  71 */         nettime = getNetTime();
/*     */       }
/*     */       else {
/*  74 */         nettime = "2012-01-01"; }
/*     */     }
/*  76 */     return nettime;
/*     */   }
/*     */ 
/*     */   public static synchronized void init()
/*     */   {
/*  86 */     Util.printSection("验证授权");
/*     */ 
/*  88 */     String ip = null;
/*  89 */     String qq = null;
/*  90 */     String buy_time = null;
/*  91 */     String end_time = null;
/*  92 */     String shop = null;
/*  93 */     String test = null;
/*     */ 
/*  98 */     String driver = "com.mysql.jdbc.Driver";
/*     */ 
/* 101 */     String url = "jdbc:mysql://key.cmchs.cn:3306/Aion_key";
/*     */ 
/* 103 */     String user = "Aion_key";
/*     */ 
/* 105 */     String password = "bcsFhkpaY2k6bGmW";
/*     */     try
/*     */     {
/* 110 */       Class.forName(driver);
/*     */ 
/* 112 */       Connection con = DriverManager.getConnection(url, user, password);
/*     */ 
/* 116 */       Statement statement = con.createStatement();
/*     */ 
/* 119 */       String sql = "select * from `server_1` where FIND_IN_SET('45.253.67.212',ip)";
/*     */ 
/* 122 */       ResultSet rs = statement.executeQuery(sql);
/*     */ 
/* 124 */       while (rs.next())
/*     */       {
/* 126 */         ip = rs.getString("ip");
/* 127 */         qq = rs.getString("qq");
/* 128 */         buy_time = rs.getString("buy_time");
/* 129 */         end_time = rs.getString("end_time");
/* 130 */         shop = rs.getString("shop");
/*     */       }
/*     */ 
/* 134 */       rs.close();
/* 135 */       con.close();
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 156 */       return;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 147 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/* 150 */       if (qq == null) {
/* 151 */         setNumber(2);
/* 152 */         System.out.println("00:00:00,000 INFO [main]: - 验证授权失败,无效的IP或QQ.");
/* 153 */         System.out.println("00:00:00,000 INFO [main]: - 自动进入网络影响模式,仅支持30人在线.");
/* 154 */         return;
/*     */       }
/* 156 */       setNumber(1);
/*     */     }
/*     */   }
/*     */ }