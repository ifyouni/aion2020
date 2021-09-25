/*    */ package com.aionemu.gameserver.utils.gametime;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.GSConfig;
/*    */ import java.util.GregorianCalendar;
/*    */ import org.joda.time.DateTime;
/*    */ import org.joda.time.DateTimeZone;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public final class DateTimeUtil
/*    */ {
/* 30 */   static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);
/*    */ 
/* 32 */   private static boolean canApplyZoneChange = false;
/*    */ 
/*    */   public static void init()
/*    */   {
/*    */     try
/*    */     {
/* 38 */       if (!(GSConfig.TIME_ZONE_ID.isEmpty()))
/*    */       {
/* 40 */         DateTimeZone.forID(System.getProperty("Duser.timezone"));
/* 41 */         DateTimeZone.forID(GSConfig.TIME_ZONE_ID);
/* 42 */         canApplyZoneChange = true;
/*    */       }
/*    */     }
/*    */     catch (Throwable e)
/*    */     {
/* 47 */       log.error("Invalid or not supported timezones specified!!!\nUse both -Duser.timezone=\"timezone_id\" switch from command line\nand add a valid value for GSConfig.TIME_ZONE_ID");
/*    */     }
/*    */   }
/*    */ 
/*    */   public static DateTime getDateTime()
/*    */   {
/* 56 */     DateTime dt = new DateTime();
/* 57 */     if (canApplyZoneChange)
/*    */     {
/* 59 */       return dt.withZoneRetainFields(DateTimeZone.forID(GSConfig.TIME_ZONE_ID));
/*    */     }
/* 61 */     return dt;
/*    */   }
/*    */ 
/*    */   public static DateTime getDateTime(String isoDateTime)
/*    */   {
/* 66 */     DateTime dt = new DateTime(isoDateTime);
/* 67 */     if (canApplyZoneChange)
/*    */     {
/* 69 */       return dt.withZoneRetainFields(DateTimeZone.forID(GSConfig.TIME_ZONE_ID));
/*    */     }
/* 71 */     return dt;
/*    */   }
/*    */ 
/*    */   public static DateTime getDateTime(GregorianCalendar calendar)
/*    */   {
/* 76 */     DateTime dt = new DateTime(calendar);
/* 77 */     if (canApplyZoneChange)
/*    */     {
/* 79 */       return dt.withZoneRetainFields(DateTimeZone.forID(GSConfig.TIME_ZONE_ID));
/*    */     }
/* 81 */     return dt;
/*    */   }
/*    */ 
/*    */   public static DateTime getDateTime(long millisSinceSeventies)
/*    */   {
/* 86 */     DateTime dt = new DateTime(millisSinceSeventies);
/* 87 */     if (canApplyZoneChange)
/*    */     {
/* 89 */       return dt.withZoneRetainFields(DateTimeZone.forID(GSConfig.TIME_ZONE_ID));
/*    */     }
/* 91 */     return dt;
/*    */   }
/*    */ 
/*    */   public static boolean canApplyZoneChange()
/*    */   {
/* 96 */     return canApplyZoneChange;
/*    */   }
/*    */ }