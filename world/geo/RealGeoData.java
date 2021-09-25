/*    */ package com.aionemu.gameserver.world.geo;
/*    */ 
/*    */ import com.aionemu.gameserver.dataholders.DataManager;
/*    */ import com.aionemu.gameserver.dataholders.WorldMapsData;
/*    */ import com.aionemu.gameserver.geoEngine.GeoWorldLoader;
/*    */ import com.aionemu.gameserver.geoEngine.models.GeoMap;
/*    */ import com.aionemu.gameserver.geoEngine.scene.Spatial;
/*    */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*    */ import com.aionemu.gameserver.utils.Util;
/*    */ import gnu.trove.map.hash.TIntObjectHashMap;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class RealGeoData
/*    */   implements GeoData
/*    */ {
/* 35 */   private static final Logger log = LoggerFactory.getLogger(RealGeoData.class);
/*    */   private TIntObjectHashMap<GeoMap> geoMaps;
/*    */ 
/*    */   public RealGeoData()
/*    */   {
/* 37 */     this.geoMaps = new TIntObjectHashMap();
/*    */   }
/*    */ 
/*    */   public void loadGeoMaps() {
/* 41 */     Map models = loadMeshes();
/* 42 */     loadWorldMaps(models);
/* 43 */     models.clear();
/* 44 */     models = null;
/* 45 */     log.info("加载: " + this.geoMaps.size() + " 个地理数据!");
/*    */   }
/*    */ 
/*    */   protected void loadWorldMaps(Map<String, Spatial> models)
/*    */   {
/* 52 */     log.info("加载地理数据..");
/* 53 */     Util.printProgressBarHeader(DataManager.WORLD_MAPS_DATA.size());
/* 54 */     List mapsWithErrors = new ArrayList();
/*    */ 
/* 56 */     for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA) {
/* 57 */       GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId().intValue()), map.getWorldSize());
/*    */       try {
/* 59 */         if (GeoWorldLoader.loadWorld(map.getMapId().intValue(), models, geoMap))
/* 60 */           this.geoMaps.put(map.getMapId().intValue(), geoMap);
/*    */       }
/*    */       catch (Throwable t)
/*    */       {
/* 64 */         mapsWithErrors.add(map.getMapId());
/* 65 */         this.geoMaps.put(map.getMapId().intValue(), DummyGeoData.DUMMY_MAP);
/*    */       }
/* 67 */       Util.printCurrentProgress();
/*    */     }
/* 69 */     Util.printEndProgress();
/* 70 */     log.warn("以下地图未正确加载并恢复为默认状态: ");
/* 71 */     if (mapsWithErrors.size() <= 0)
/*    */       return;
/*    */   }
/*    */ 
/*    */   protected Map<String, Spatial> loadMeshes()
/*    */   {
/* 79 */     log.info("加载网格..");
/* 80 */     Map models = null;
/*    */     try {
/* 82 */       models = GeoWorldLoader.loadMeshs("data/geo/meshs.geo");
/*    */     }
/*    */     catch (IOException e) {
/* 85 */       throw new IllegalStateException("加载问题网格", e);
/*    */     }
/* 87 */     return models;
/*    */   }
/*    */ 
/*    */   public GeoMap getMap(int worldId)
/*    */   {
/* 92 */     GeoMap geoMap = (GeoMap)this.geoMaps.get(worldId);
/* 93 */     return ((geoMap != null) ? geoMap : DummyGeoData.DUMMY_MAP);
/*    */   }
/*    */ }