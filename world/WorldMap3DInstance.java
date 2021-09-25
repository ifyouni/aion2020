/*    */ package com.aionemu.gameserver.world;
/*    */ 
/*    */ import com.aionemu.gameserver.world.zone.ZoneInstance;
/*    */ import gnu.trove.map.hash.TIntObjectHashMap;
/*    */ 
/*    */ public class WorldMap3DInstance extends WorldMapInstance
/*    */ {
/*    */   public WorldMap3DInstance(WorldMap parent, int instanceId)
/*    */   {
/* 27 */     super(parent, instanceId);
/*    */   }
/*    */ 
/*    */   public MapRegion getRegion(float x, float y, float z)
/*    */   {
/* 32 */     int regionId = RegionUtil.get3dRegionId(x, y, z);
/* 33 */     return ((MapRegion)this.regions.get(regionId));
/*    */   }
/*    */ 
/*    */   protected void initMapRegions()
/*    */   {
/*    */     int y;
/*    */     int z;
/*    */     int regionId;
/* 37 */     int size = getParent().getWorldSize();
/* 38 */     float maxZ = Math.round(size / regionSize) * regionSize;
/*    */ 
/* 41 */     for (int x = 0; x <= size; x += regionSize) {
/* 42 */       for (y = 0; y <= size; y += regionSize) {
/* 43 */         for (z = 0; z < maxZ; z += regionSize) {
/* 44 */           regionId = RegionUtil.get3dRegionId(x, y, z);
/* 45 */           this.regions.put(regionId, createMapRegion(regionId));
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 51 */     for (x = 0; x <= size; x += regionSize)
/* 52 */       for (y = 0; y <= size; y += regionSize)
/* 53 */         for (z = 0; z < maxZ; z += regionSize) {
/* 54 */           regionId = RegionUtil.get3dRegionId(x, y, z);
/* 55 */           MapRegion mapRegion = (MapRegion)this.regions.get(regionId);
/* 56 */           for (int x2 = x - regionSize; x2 <= x + regionSize; x2 += regionSize)
/* 57 */             for (int y2 = y - regionSize; y2 <= y + regionSize; y2 += regionSize)
/* 58 */               for (int z2 = z - regionSize; z2 < z + regionSize; z2 += regionSize) {
/* 59 */                 if ((x2 == x) && (y2 == y) && (z2 == z))
/*    */                   continue;
/* 61 */                 int neighbourId = RegionUtil.get3dRegionId(x2, y2, z2);
/* 62 */                 MapRegion neighbour = (MapRegion)this.regions.get(neighbourId);
/* 63 */                 if (neighbour != null)
/* 64 */                   mapRegion.addNeighbourRegion(neighbour);
/*    */               }
/*    */         }
/*    */   }
/*    */ 
/*    */   protected MapRegion createMapRegion(int regionId)
/*    */   {
/* 75 */     float startX = RegionUtil.getXFrom3dRegionId(regionId);
/* 76 */     float startY = RegionUtil.getYFrom3dRegionId(regionId);
/* 77 */     float startZ = RegionUtil.getZFrom3dRegionId(regionId);
/* 78 */     ZoneInstance[] zones = filterZones(getMapId().intValue(), regionId, startX, startY, startZ, startZ + regionSize);
/* 79 */     return new MapRegion(regionId, this, zones);
/*    */   }
/*    */ 
/*    */   public boolean isPersonal()
/*    */   {
/* 84 */     return false;
/*    */   }
/*    */ 
/*    */   public int getOwnerId()
/*    */   {
/* 89 */     return 0;
/*    */   }
/*    */ }