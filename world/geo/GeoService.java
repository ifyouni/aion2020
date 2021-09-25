/*     */ package com.aionemu.gameserver.world.geo;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.GeoDataConfig;
/*     */ import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
/*     */ import com.aionemu.gameserver.geoEngine.math.Vector3f;
/*     */ import com.aionemu.gameserver.geoEngine.models.GeoMap;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.templates.BoundRadius;
/*     */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class GeoService
/*     */ {
/*  17 */   private static final Logger log = LoggerFactory.getLogger(GeoService.class);
/*     */   private GeoData geoData;
/*     */ 
/*     */   public void initializeGeo()
/*     */   {
/*  24 */     switch (1.$SwitchMap$com$aionemu$gameserver$world$geo$GeoType[getConfiguredGeoType().ordinal()])
/*     */     {
/*     */     case 1:
/*  26 */       this.geoData = new RealGeoData();
/*  27 */       break;
/*     */     case 2:
/*  29 */       this.geoData = new DummyGeoData();
/*     */     }
/*     */ 
/*  32 */     log.info("Configured Geo type: " + getConfiguredGeoType());
/*  33 */     this.geoData.loadGeoMaps();
/*     */   }
/*     */ 
/*     */   public void setDoorState(int worldId, int instanceId, String name, boolean isOpened) {
/*  37 */     if (GeoDataConfig.GEO_ENABLE)
/*  38 */       this.geoData.getMap(worldId).setDoorState(instanceId, name, isOpened);
/*     */   }
/*     */ 
/*     */   public float getZAfterMoveBehind(int worldId, float x, float y, float z, int instanceId)
/*     */   {
/*  47 */     if (GeoDataConfig.GEO_ENABLE) {
/*  48 */       return getZ(worldId, x, y, z, 0.0F, instanceId);
/*     */     }
/*  50 */     return getZ(worldId, x, y, z, 0.5F, instanceId);
/*     */   }
/*     */ 
/*     */   public float getZ(VisibleObject object)
/*     */   {
/*  58 */     return this.geoData.getMap(object.getWorldId()).getZ(object.getX(), object.getY(), object.getZ(), object.getInstanceId());
/*     */   }
/*     */ 
/*     */   public float getZ(int worldId, float x, float y, float z, float defaultUp, int instanceId)
/*     */   {
/*  70 */     float newZ = this.geoData.getMap(worldId).getZ(x, y, z, instanceId);
/*  71 */     if (!(GeoDataConfig.GEO_ENABLE)) {
/*  72 */       newZ += defaultUp;
/*     */     }
/*     */ 
/*  77 */     return newZ;
/*     */   }
/*     */ 
/*     */   public float getZ(int worldId, float x, float y)
/*     */   {
/*  87 */     return this.geoData.getMap(worldId).getZ(x, y);
/*     */   }
/*     */ 
/*     */   public String getDoorName(int worldId, String meshFile, float x, float y, float z) {
/*  91 */     return this.geoData.getMap(worldId).getDoorName(worldId, meshFile, x, y, z);
/*     */   }
/*     */ 
/*     */   public CollisionResults getCollisions(VisibleObject object, float x, float y, float z, boolean changeDirection, byte intentions) {
/*  95 */     return this.geoData.getMap(object.getWorldId()).getCollisions(object.getX(), object.getY(), object.getZ(), x, y, z, changeDirection, false, object.getInstanceId(), intentions);
/*     */   }
/*     */ 
/*     */   public boolean canSee(VisibleObject object, VisibleObject target)
/*     */   {
/* 104 */     if (!(GeoDataConfig.CANSEE_ENABLE)) {
/* 105 */       return true;
/*     */     }
/* 107 */     float limit = (float)(MathUtil.getDistance(object, target) - target.getObjectTemplate().getBoundRadius().getCollision());
/* 108 */     if (limit <= 0.0F)
/* 109 */       return true;
/* 110 */     return this.geoData.getMap(object.getWorldId()).canSee(object.getX(), object.getY(), object.getZ() + object.getObjectTemplate().getBoundRadius().getUpper() / 2.0F, target.getX(), target.getY(), target.getZ() + target.getObjectTemplate().getBoundRadius().getUpper() / 2.0F, limit, object.getInstanceId());
/*     */   }
/*     */ 
/*     */   public boolean canSee(int worldId, float x, float y, float z, float x1, float y1, float z1, float limit, int instanceId)
/*     */   {
/* 116 */     return this.geoData.getMap(worldId).canSee(x, y, z, x1, y1, z1, limit, instanceId);
/*     */   }
/*     */ 
/*     */   public boolean isGeoOn() {
/* 120 */     return GeoDataConfig.GEO_ENABLE;
/*     */   }
/*     */ 
/*     */   public Vector3f getClosestCollision(Creature object, float x, float y, float z, boolean changeDirection, byte intentions) {
/* 124 */     return this.geoData.getMap(object.getWorldId()).getClosestCollision(object.getX(), object.getY(), object.getZ(), x, y, z, changeDirection, object.isInFlyingState(), object.getInstanceId(), intentions);
/*     */   }
/*     */ 
/*     */   public GeoType getConfiguredGeoType()
/*     */   {
/* 129 */     if (GeoDataConfig.GEO_ENABLE) {
/* 130 */       return GeoType.GEO_MESHES;
/*     */     }
/* 132 */     return GeoType.NO_GEO;
/*     */   }
/*     */ 
/*     */   public static final GeoService getInstance() {
/* 136 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   private static final class SingletonHolder
/*     */   {
/* 142 */     protected static final GeoService instance = new GeoService();
/*     */   }
/*     */ }