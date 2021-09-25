/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class WorldPosition
/*     */ {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(WorldPosition.class);
/*     */   private int mapId;
/*     */   private MapRegion mapRegion;
/*     */   private float x;
/*     */   private float y;
/*     */   private float z;
/*     */   private byte heading;
/*  65 */   private boolean isSpawned = false;
/*     */ 
/*     */   public WorldPosition(int mapId)
/*     */   {
/*  29 */     this.mapId = mapId;
/*     */   }
/*     */ 
/*     */   public int getMapId()
/*     */   {
/*  73 */     if (this.mapId == 0)
/*  74 */       log.warn("WorldPosition has (mapId == 0) " + toString());
/*  75 */     return this.mapId;
/*     */   }
/*     */ 
/*     */   public void setMapId(int mapId)
/*     */   {
/*  83 */     this.mapId = mapId;
/*     */   }
/*     */ 
/*     */   public float getX()
/*     */   {
/*  92 */     return this.x;
/*     */   }
/*     */ 
/*     */   public float getY()
/*     */   {
/* 101 */     return this.y;
/*     */   }
/*     */ 
/*     */   public float getZ()
/*     */   {
/* 110 */     return this.z;
/*     */   }
/*     */ 
/*     */   public MapRegion getMapRegion()
/*     */   {
/* 119 */     return ((this.isSpawned) ? this.mapRegion : null);
/*     */   }
/*     */ 
/*     */   public int getInstanceId()
/*     */   {
/* 126 */     return this.mapRegion.getParent().getInstanceId();
/*     */   }
/*     */ 
/*     */   public int getInstanceCount()
/*     */   {
/* 133 */     return this.mapRegion.getParent().getParent().getInstanceCount();
/*     */   }
/*     */ 
/*     */   public boolean isInstanceMap()
/*     */   {
/* 140 */     return this.mapRegion.getParent().getParent().isInstanceType();
/*     */   }
/*     */ 
/*     */   public boolean isMapRegionActive()
/*     */   {
/* 147 */     return this.mapRegion.isMapRegionActive();
/*     */   }
/*     */ 
/*     */   public byte getHeading()
/*     */   {
/* 156 */     return this.heading;
/*     */   }
/*     */ 
/*     */   public World getWorld()
/*     */   {
/* 165 */     return this.mapRegion.getWorld();
/*     */   }
/*     */ 
/*     */   public WorldMapInstance getWorldMapInstance()
/*     */   {
/* 172 */     return this.mapRegion.getParent();
/*     */   }
/*     */ 
/*     */   public boolean isSpawned()
/*     */   {
/* 181 */     return this.isSpawned;
/*     */   }
/*     */ 
/*     */   void setIsSpawned(boolean val)
/*     */   {
/* 190 */     this.isSpawned = val;
/*     */   }
/*     */ 
/*     */   void setMapRegion(MapRegion r)
/*     */   {
/* 200 */     this.mapRegion = r;
/*     */   }
/*     */ 
/*     */   public void setXYZH(Float newX, Float newY, Float newZ, Byte newHeading)
/*     */   {
/* 213 */     if (newX != null)
/* 214 */       this.x = newX.floatValue();
/* 215 */     if (newY != null)
/* 216 */       this.y = newY.floatValue();
/* 217 */     if (newZ != null)
/* 218 */       this.z = newZ.floatValue();
/* 219 */     if (newHeading != null)
/* 220 */       this.heading = newHeading.byteValue();
/*     */   }
/*     */ 
/*     */   public void setZ(float z) {
/* 224 */     this.z = z;
/*     */   }
/*     */ 
/*     */   public void setH(byte h) {
/* 228 */     this.heading = h;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 233 */     return "WorldPosition [heading=" + this.heading + ", isSpawned=" + this.isSpawned + ", mapRegion=" + this.mapRegion + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
/*     */   }
/*     */ 
/*     */   public WorldPosition clone()
/*     */   {
/* 238 */     WorldPosition pos = new WorldPosition(this.mapId);
/* 239 */     pos.heading = this.heading;
/* 240 */     pos.isSpawned = this.isSpawned;
/* 241 */     pos.mapRegion = this.mapRegion;
/* 242 */     pos.x = this.x;
/* 243 */     pos.y = this.y;
/* 244 */     pos.z = this.z;
/* 245 */     return pos;
/*     */   }
/*     */ }