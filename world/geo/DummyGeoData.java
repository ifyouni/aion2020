/*    */ package com.aionemu.gameserver.world.geo;
/*    */ 
/*    */ import com.aionemu.gameserver.geoEngine.models.GeoMap;
/*    */ 
/*    */ public class DummyGeoData
/*    */   implements GeoData
/*    */ {
/* 23 */   public static final DummyGeoMap DUMMY_MAP = new DummyGeoMap("", 0);
/*    */ 
/*    */   public void loadGeoMaps()
/*    */   {
/*    */   }
/*    */ 
/*    */   public GeoMap getMap(int worldId)
/*    */   {
/* 31 */     return DUMMY_MAP;
/*    */   }
/*    */ }