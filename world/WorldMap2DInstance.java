/*    */ package com.aionemu.gameserver.world;
/*    */ 
/*    */ import com.aionemu.gameserver.world.zone.ZoneInstance;
/*    */ import gnu.trove.map.hash.TIntObjectHashMap;
/*    */ 
/*    */ public class WorldMap2DInstance extends WorldMapInstance
/*    */ {
/*    */   private int ownerId;
/*    */ 
/*    */   public WorldMap2DInstance(WorldMap parent, int instanceId, int ownerId)
/*    */   {
/* 25 */     super(parent, instanceId);
/* 26 */     this.ownerId = ownerId;
/*    */   }
/*    */ 
/*    */   protected MapRegion createMapRegion(int regionId)
/*    */   {
/* 31 */     float startX = RegionUtil.getXFrom2dRegionId(regionId);
/* 32 */     float startY = RegionUtil.getYFrom2dRegionId(regionId);
/* 33 */     int size = getParent().getWorldSize();
/* 34 */     float maxZ = Math.round(size / regionSize) * regionSize;
/* 35 */     ZoneInstance[] zones = filterZones(getMapId().intValue(), regionId, startX, startY, 0.0F, maxZ);
/* 36 */     return new MapRegion(regionId, this, zones);
/*    */   }
/*    */ 
/*    */   protected void initMapRegions()
/*    */   {
/*    */     int y;
/*    */     int regionId;
/* 40 */     int size = getParent().getWorldSize();
/*    */ 
/* 42 */     for (int x = 0; x <= size; x += regionSize) {
/* 43 */       for (y = 0; y <= size; y += regionSize) {
/* 44 */         regionId = RegionUtil.get2dRegionId(x, y);
/* 45 */         this.regions.put(regionId, createMapRegion(regionId));
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 50 */     for (x = 0; x <= size; x += regionSize)
/* 51 */       for (y = 0; y <= size; y += regionSize) {
/* 52 */         regionId = RegionUtil.get2dRegionId(x, y);
/* 53 */         MapRegion mapRegion = (MapRegion)this.regions.get(regionId);
/* 54 */         for (int x2 = x - regionSize; x2 <= x + regionSize; x2 += regionSize)
/* 55 */           for (int y2 = y - regionSize; y2 <= y + regionSize; y2 += regionSize) {
/* 56 */             if ((x2 == x) && (y2 == y))
/*    */               continue;
/* 58 */             int neighbourId = RegionUtil.get2dRegionId(x2, y2);
/* 59 */             MapRegion neighbour = (MapRegion)this.regions.get(neighbourId);
/* 60 */             if (neighbour != null)
/* 61 */               mapRegion.addNeighbourRegion(neighbour);
/*    */           }
/*    */       }
/*    */   }
/*    */ 
/*    */   public MapRegion getRegion(float x, float y, float z)
/*    */   {
/* 70 */     int regionId = RegionUtil.get2dRegionId(x, y);
/* 71 */     return ((MapRegion)this.regions.get(regionId));
/*    */   }
/*    */ 
/*    */   public int getOwnerId()
/*    */   {
/* 78 */     return this.ownerId;
/*    */   }
/*    */ 
/*    */   public void setOwnerId(int ownerId)
/*    */   {
/* 86 */     this.ownerId = ownerId;
/*    */   }
/*    */ 
/*    */   public boolean isPersonal()
/*    */   {
/* 91 */     return (this.ownerId != 0);
/*    */   }
/*    */ }