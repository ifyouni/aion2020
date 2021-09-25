/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import javolution.util.FastMap;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public final class ZoneName
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(ZoneName.class);
/*    */ 
/* 26 */   private static final FastMap<String, ZoneName> zoneNames = new FastMap();
/*    */   public static final String NONE = "NONE";
/*    */   public static final String ABYSS_CASTLE = "_ABYSS_CASTLE_AREA_";
/*    */   private String _name;
/*    */ 
/*    */   private ZoneName(String name)
/*    */   {
/* 38 */     this._name = name;
/*    */   }
/*    */ 
/*    */   public String name() {
/* 42 */     return this._name;
/*    */   }
/*    */ 
/*    */   public int id() {
/* 46 */     return this._name.hashCode();
/*    */   }
/*    */ 
/*    */   public static final ZoneName createOrGet(String name) {
/* 50 */     name = name.toUpperCase();
/* 51 */     if (zoneNames.containsKey(name))
/* 52 */       return ((ZoneName)zoneNames.get(name));
/* 53 */     ZoneName newZone = new ZoneName(name);
/* 54 */     zoneNames.put(name, newZone);
/* 55 */     return newZone;
/*    */   }
/*    */ 
/*    */   public static final int getId(String name) {
/* 59 */     name = name.toUpperCase();
/* 60 */     if (zoneNames.containsKey(name))
/* 61 */       return ((ZoneName)zoneNames.get(name)).id();
/* 62 */     return ((ZoneName)zoneNames.get("NONE")).id();
/*    */   }
/*    */ 
/*    */   public static final ZoneName get(String name) {
/* 66 */     name = name.toUpperCase();
/* 67 */     if (zoneNames.containsKey(name))
/* 68 */       return ((ZoneName)zoneNames.get(name));
/* 69 */     log.warn("缺少区域:" + name);
/* 70 */     return ((ZoneName)zoneNames.get("NONE"));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 75 */     return this._name;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 31 */     zoneNames.put("NONE", new ZoneName("NONE"));
/* 32 */     zoneNames.put("_ABYSS_CASTLE_AREA_", new ZoneName("_ABYSS_CASTLE_AREA_"));
/*    */   }
/*    */ }