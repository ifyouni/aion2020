/*     */ package com.aionemu.gameserver.world.zone;
/*     */ 
/*     */ import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
/*     */ import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
/*     */ import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
/*     */ import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
/*     */ import com.aionemu.gameserver.configs.main.GeoDataConfig;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.MaterialData;
/*     */ import com.aionemu.gameserver.dataholders.SiegeLocationData;
/*     */ import com.aionemu.gameserver.dataholders.VortexData;
/*     */ import com.aionemu.gameserver.dataholders.WorldMapsData;
/*     */ import com.aionemu.gameserver.dataholders.ZoneData;
/*     */ import com.aionemu.gameserver.geoEngine.scene.Spatial;
/*     */ import com.aionemu.gameserver.model.GameEngine;
/*     */ import com.aionemu.gameserver.model.geometry.Area;
/*     */ import com.aionemu.gameserver.model.geometry.CylinderArea;
/*     */ import com.aionemu.gameserver.model.geometry.PolyArea;
/*     */ import com.aionemu.gameserver.model.geometry.SemisphereArea;
/*     */ import com.aionemu.gameserver.model.geometry.SphereArea;
/*     */ import com.aionemu.gameserver.model.siege.SiegeLocation;
/*     */ import com.aionemu.gameserver.model.siege.SiegeShield;
/*     */ import com.aionemu.gameserver.model.templates.materials.MaterialTemplate;
/*     */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.Cylinder;
/*     */ import com.aionemu.gameserver.model.templates.zone.MaterialZoneTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.Points;
/*     */ import com.aionemu.gameserver.model.templates.zone.Semisphere;
/*     */ import com.aionemu.gameserver.model.templates.zone.Sphere;
/*     */ import com.aionemu.gameserver.model.templates.zone.WorldZoneTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;
/*     */ import com.aionemu.gameserver.model.vortex.VortexLocation;
/*     */ import com.aionemu.gameserver.services.ShieldService;
/*     */ import com.aionemu.gameserver.world.zone.handler.GeneralZoneHandler;
/*     */ import com.aionemu.gameserver.world.zone.handler.MaterialZoneHandler;
/*     */ import com.aionemu.gameserver.world.zone.handler.ZoneHandler;
/*     */ import com.aionemu.gameserver.world.zone.handler.ZoneHandlerClassListener;
/*     */ import com.aionemu.gameserver.world.zone.handler.ZoneNameAnnotation;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javolution.util.FastMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class ZoneService
/*     */   implements GameEngine
/*     */ {
/*  51 */   private static final Logger log = LoggerFactory.getLogger(ZoneService.class);
/*     */   private TIntObjectHashMap<List<ZoneInfo>> zoneByMapIdMap;
/*  53 */   private final Map<ZoneName, Class<? extends ZoneHandler>> handlers = new HashMap();
/*  54 */   private final FastMap<ZoneName, ZoneHandler> collidableHandlers = new FastMap();
/*  55 */   public static final ZoneHandler DUMMY_ZONE_HANDLER = new GeneralZoneHandler();
/*  56 */   private static ScriptManager scriptManager = new ScriptManager();
/*  57 */   public static final File ZONE_DESCRIPTOR_FILE = new File("./data/scripts/system/zonehandlers.xml");
/*     */ 
/*     */   private ZoneService() {
/*  60 */     this.zoneByMapIdMap = DataManager.ZONE_DATA.getZones();
/*     */   }
/*     */ 
/*     */   public static ZoneService getInstance() {
/*  64 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   public ZoneHandler getNewZoneHandler(ZoneName zoneName)
/*     */   {
/*  74 */     ZoneHandler zoneHandler = (ZoneHandler)this.collidableHandlers.get(zoneName);
/*  75 */     if (zoneHandler != null)
/*  76 */       return zoneHandler;
/*  77 */     Class zoneClass = (Class)this.handlers.get(zoneName);
/*  78 */     if (zoneClass != null) {
/*     */       try {
/*  80 */         zoneHandler = (ZoneHandler)zoneClass.newInstance();
/*     */       }
/*     */       catch (IllegalAccessException ex) {
/*  83 */         log.warn("Can't instantiate zone handler " + zoneName, ex);
/*     */       }
/*     */       catch (Exception ex) {
/*  86 */         log.warn("Can't instantiate zone handler " + zoneName, ex);
/*     */       }
/*     */     }
/*  89 */     if (zoneHandler == null) {
/*  90 */       zoneHandler = DUMMY_ZONE_HANDLER;
/*     */     }
/*  92 */     return zoneHandler;
/*     */   }
/*     */ 
/*     */   public final void addZoneHandlerClass(Class<? extends ZoneHandler> handler)
/*     */   {
/*  99 */     ZoneNameAnnotation idAnnotation = (ZoneNameAnnotation)handler.getAnnotation(ZoneNameAnnotation.class);
/* 100 */     if (idAnnotation != null) {
/* 101 */       String[] zoneNames = idAnnotation.value().split(" ");
/* 102 */       for (String zoneNameString : zoneNames)
/*     */         try {
/* 104 */           ZoneName zoneName = ZoneName.get(zoneNameString.trim());
/* 105 */           if (zoneName == ZoneName.get("NONE"))
/* 106 */             throw new RuntimeException();
/* 107 */           this.handlers.put(zoneName, handler);
/*     */         }
/*     */         catch (Exception e) {
/* 110 */           log.warn("缺少区域名: " + idAnnotation.value());
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void addZoneHandlerClass(ZoneName zoneName, Class<? extends ZoneHandler> handler)
/*     */   {
/* 117 */     this.handlers.put(zoneName, handler);
/*     */   }
/*     */ 
/*     */   public void load(CountDownLatch progressLatch)
/*     */   {
/* 122 */     log.info("启动区域引擎");
/* 123 */     scriptManager = new ScriptManager();
/*     */ 
/* 125 */     AggregatedClassListener acl = new AggregatedClassListener();
/* 126 */     acl.addClassListener(new OnClassLoadUnloadListener());
/* 127 */     acl.addClassListener(new ScheduledTaskClassListener());
/* 128 */     acl.addClassListener(new ZoneHandlerClassListener());
/* 129 */     scriptManager.setGlobalClassListener(acl);
/*     */     try
/*     */     {
/* 132 */       scriptManager.load(ZONE_DESCRIPTOR_FILE);
/* 133 */       log.info("加载 " + this.handlers.size() + " 个区域.");
/*     */     }
/*     */     catch (IllegalStateException e) {
/* 136 */       log.warn("无法初始化副本处理程序.", e.getMessage());
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */     finally {
/* 142 */       if (progressLatch != null)
/* 143 */         progressLatch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 150 */     log.info("Zone engine shutdown started");
/* 151 */     scriptManager.shutdown();
/* 152 */     scriptManager = null;
/* 153 */     this.handlers.clear();
/* 154 */     log.info("Zone engine shutdown complete");
/*     */   }
/*     */ 
/*     */   public Map<ZoneName, ZoneInstance> getZoneInstancesByWorldId(int mapId)
/*     */   {
/* 162 */     Map zones = new HashMap();
/* 163 */     int worldSize = DataManager.WORLD_MAPS_DATA.getTemplate(mapId).getWorldSize();
/* 164 */     WorldZoneTemplate zone = new WorldZoneTemplate(worldSize, Integer.valueOf(mapId));
/* 165 */     PolyArea fullArea = new PolyArea(zone.getName(), mapId, zone.getPoints().getPoint(), zone.getPoints().getBottom(), zone.getPoints().getTop());
/*     */ 
/* 167 */     ZoneInstance fullMap = new ZoneInstance(mapId, new ZoneInfo(fullArea, zone));
/* 168 */     fullMap.addHandler(getNewZoneHandler(zone.getName()));
/* 169 */     zones.put(zone.getName(), fullMap);
/*     */ 
/* 171 */     Collection areas = (Collection)this.zoneByMapIdMap.get(mapId);
/* 172 */     if (areas == null)
/* 173 */       return zones;
/* 174 */     ShieldService.getInstance().load(mapId);
/*     */ 
/* 176 */     for (Iterator i$ = areas.iterator(); i$.hasNext(); )
/*     */     {
/*     */       Iterator i$;
/* 176 */       ZoneInfo area = (ZoneInfo)i$.next();
/* 177 */       ZoneInstance instance = null;
/* 178 */       switch (2.$SwitchMap$com$aionemu$gameserver$model$templates$zone$ZoneClassName[area.getZoneTemplate().getZoneType().ordinal()])
/*     */       {
/*     */       case 1:
/* 180 */         instance = new FlyZoneInstance(mapId, area);
/* 181 */         break;
/*     */       case 2:
/* 183 */         instance = new SiegeZoneInstance(mapId, area);
/* 184 */         SiegeLocation siege = (SiegeLocation)DataManager.SIEGE_LOCATION_DATA.getSiegeLocations().get(area.getZoneTemplate().getSiegeId().get(0));
/* 185 */         if (siege != null) {
/* 186 */           siege.addZone((SiegeZoneInstance)instance);
/* 187 */           if (GeoDataConfig.GEO_SHIELDS_ENABLE)
/* 188 */             ShieldService.getInstance().attachShield(siege);  }
/* 188 */         break;
/*     */       case 3:
/* 192 */         instance = new SiegeZoneInstance(mapId, area);
/* 193 */         for (i$ = area.getZoneTemplate().getSiegeId().iterator(); i$.hasNext(); ) { int artifactId = ((Integer)i$.next()).intValue();
/* 194 */           SiegeLocation artifact = (SiegeLocation)DataManager.SIEGE_LOCATION_DATA.getArtifacts().get(Integer.valueOf(artifactId));
/* 195 */           if (artifact == null) {
/* 196 */             log.warn("Missing siege location data for zone " + area.getZoneTemplate().getName().name());
/*     */           }
/*     */           else {
/* 199 */             artifact.addZone((SiegeZoneInstance)instance);
/*     */           }
/*     */         }
/* 202 */         break;
/*     */       case 4:
/* 204 */         instance = new PvPZoneInstance(mapId, area);
/* 205 */         break;
/*     */       default:
/* 207 */         InvasionZoneInstance invasionZone = getIZI(area);
/* 208 */         if (invasionZone != null) {
/* 209 */           instance = invasionZone;
/*     */         }
/*     */         else {
/* 212 */           instance = new ZoneInstance(mapId, area);
/*     */         }
/*     */       }
/* 215 */       instance.addHandler(getNewZoneHandler(area.getZoneTemplate().getName()));
/* 216 */       zones.put(area.getZoneTemplate().getName(), instance);
/*     */     }
/* 218 */     return zones;
/*     */   }
/*     */ 
/*     */   private InvasionZoneInstance getIZI(ZoneInfo area) {
/* 222 */     if ((area.getZoneTemplate().getName().name().equals("WAILING_CLIFFS_220050000")) || (area.getZoneTemplate().getName().name().equals("BALTASAR_CEMETERY_220050000")) || (area.getZoneTemplate().getName().name().equals("THE_LEGEND_SHRINE_220050000")) || (area.getZoneTemplate().getName().name().equals("SUDORVILLE_220050000")) || (area.getZoneTemplate().getName().name().equals("BALTASAR_HILL_VILLAGE_220050000")) || (area.getZoneTemplate().getName().name().equals("BRUSTHONIN_MITHRIL_MINE_220050000")))
/*     */     {
/* 228 */       return validateZone(area);
/*     */     }
/* 230 */     if ((area.getZoneTemplate().getName().name().equals("JAMANOK_INN_210060000")) || (area.getZoneTemplate().getName().name().equals("THE_STALKING_GROUNDS_210060000")) || (area.getZoneTemplate().getName().name().equals("BLACK_ROCK_HOT_SPRING_210060000")) || (area.getZoneTemplate().getName().name().equals("FREGIONS_FLAME_210060000")))
/*     */     {
/* 234 */       return validateZone(area);
/*     */     }
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */   private InvasionZoneInstance validateZone(ZoneInfo area) {
/* 240 */     int mapId = area.getZoneTemplate().getMapid();
/* 241 */     VortexLocation vortex = DataManager.VORTEX_DATA.getVortexLocation(mapId);
/* 242 */     if (vortex != null) {
/* 243 */       InvasionZoneInstance instance = new InvasionZoneInstance(mapId, area);
/* 244 */       vortex.addZone(instance);
/* 245 */       return instance;
/*     */     }
/* 247 */     return null;
/*     */   }
/*     */ 
/*     */   public void createMaterialZoneTemplate(Spatial geometry, int worldId, int materialId, boolean failOnMissing)
/*     */   {
/* 258 */     ZoneName zoneName = null;
/* 259 */     if (failOnMissing)
/* 260 */       zoneName = ZoneName.get(geometry.getName() + "_" + worldId);
/*     */     else {
/* 262 */       zoneName = ZoneName.createOrGet(geometry.getName() + "_" + worldId);
/*     */     }
/* 264 */     if (zoneName.name().equals("NONE")) {
/* 265 */       return;
/*     */     }
/* 267 */     ZoneHandler handler = (ZoneHandler)this.collidableHandlers.get(zoneName);
/* 268 */     if (handler == null) {
/* 269 */       if (materialId == 11) {
/* 270 */         if (GeoDataConfig.GEO_SHIELDS_ENABLE) {
/* 271 */           handler = new SiegeShield(geometry);
/* 272 */           ShieldService.getInstance().registerShield(worldId, (SiegeShield)handler); break label171:
/*     */         }
/*     */ 
/* 275 */         return;
/*     */       }
/*     */ 
/* 278 */       MaterialTemplate template = DataManager.MATERIAL_DATA.getTemplate(materialId);
/* 279 */       if (template == null)
/* 280 */         return;
/* 281 */       handler = new MaterialZoneHandler(geometry, template);
/*     */ 
/* 283 */       label171: this.collidableHandlers.put(zoneName, handler);
/*     */     }
/*     */ 
/* 286 */     Collection areas = (Collection)this.zoneByMapIdMap.get(worldId);
/* 287 */     if (areas == null) {
/* 288 */       this.zoneByMapIdMap.put(worldId, new ArrayList());
/* 289 */       areas = (Collection)this.zoneByMapIdMap.get(worldId);
/*     */     }
/* 291 */     ZoneInfo zoneInfo = null;
/* 292 */     for (ZoneInfo area : areas) {
/* 293 */       if (area.getZoneTemplate().getName().equals(zoneName)) {
/* 294 */         zoneInfo = area;
/* 295 */         break;
/*     */       }
/*     */     }
/* 298 */     if (zoneInfo == null) {
/* 299 */       MaterialZoneTemplate zoneTemplate = new MaterialZoneTemplate(geometry, worldId);
/*     */ 
/* 301 */       Area zoneInfoArea = null;
/* 302 */       if (zoneTemplate.getSphere() != null) {
/* 303 */         zoneInfoArea = new SphereArea(zoneName, worldId, zoneTemplate.getSphere().getX().floatValue(), zoneTemplate.getSphere().getY().floatValue(), zoneTemplate.getSphere().getZ().floatValue(), zoneTemplate.getSphere().getR().floatValue());
/*     */       }
/* 306 */       else if (zoneTemplate.getCylinder() != null) {
/* 307 */         zoneInfoArea = new CylinderArea(zoneName, worldId, zoneTemplate.getCylinder().getX().floatValue(), zoneTemplate.getCylinder().getY().floatValue(), zoneTemplate.getCylinder().getR().floatValue(), zoneTemplate.getCylinder().getBottom().floatValue(), zoneTemplate.getCylinder().getTop().floatValue());
/*     */       }
/* 310 */       else if (zoneTemplate.getSemisphere() != null) {
/* 311 */         zoneInfoArea = new SemisphereArea(zoneName, worldId, zoneTemplate.getSemisphere().getX().floatValue(), zoneTemplate.getSemisphere().getY().floatValue(), zoneTemplate.getSemisphere().getZ().floatValue(), zoneTemplate.getSemisphere().getR().floatValue());
/*     */       }
/*     */ 
/* 314 */       if (zoneInfoArea != null) {
/* 315 */         zoneInfo = new ZoneInfo(zoneInfoArea, zoneTemplate);
/* 316 */         areas.add(zoneInfo);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createMaterialZoneTemplate(Spatial geometry, int regionId, int worldId, int materialId)
/*     */   {
/* 331 */     geometry.setName(geometry.getName() + "_" + regionId);
/* 332 */     createMaterialZoneTemplate(geometry, worldId, materialId, false);
/*     */   }
/*     */ 
/*     */   public void saveMaterialZones() {
/* 336 */     List templates = new ArrayList();
/* 337 */     for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA) {
/* 338 */       Collection areas = (Collection)this.zoneByMapIdMap.get(map.getMapId().intValue());
/* 339 */       if (areas == null)
/*     */         continue;
/* 341 */       for (ZoneInfo zone : areas) {
/* 342 */         if (this.collidableHandlers.containsKey(zone.getArea().getZoneName())) {
/* 343 */           templates.add(zone.getZoneTemplate());
/*     */         }
/*     */       }
/*     */     }
/* 347 */     Collections.sort(templates, new Comparator()
/*     */     {
/*     */       public int compare(ZoneTemplate o1, ZoneTemplate o2)
/*     */       {
/* 351 */         return (o1.getMapid() - o2.getMapid());
/*     */       }
/*     */     });
/* 355 */     ZoneData zoneData = new ZoneData();
/* 356 */     zoneData.zoneList = templates;
/* 357 */     zoneData.saveData();
/*     */   }
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/*  70 */     protected static final ZoneService instance = new ZoneService(null);
/*     */   }
/*     */ }