/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.WorldConfig;
/*     */ 
/*     */ public class RegionUtil
/*     */ {
/*     */   public static final int X_3D_OFFSET = 1000000;
/*     */   public static final int Y_3D_OFFSET = 1000;
/*     */   public static final int X_2D_OFFSET = 1000;
/*     */ 
/*     */   public static final int get2DRegionId(int regionSize, float x, float y)
/*     */   {
/*  33 */     return ((int)x / regionSize * 1000 + (int)y / regionSize);
/*     */   }
/*     */ 
/*     */   public static final int get3DRegionId(int regionSize, float x, float y, float z)
/*     */   {
/*  44 */     return ((int)x / regionSize * 1000000 + (int)y / regionSize * 1000 + (int)z / regionSize);
/*     */   }
/*     */ 
/*     */   public static final int get2dRegionId(float x, float y)
/*     */   {
/*  53 */     return get2DRegionId(WorldConfig.WORLD_REGION_SIZE, x, y);
/*     */   }
/*     */ 
/*     */   public static final int get3dRegionId(float x, float y, float z)
/*     */   {
/*  63 */     return get3DRegionId(WorldConfig.WORLD_REGION_SIZE, x, y, z);
/*     */   }
/*     */ 
/*     */   public static final int getXFrom2dRegionId(int regionId)
/*     */   {
/*  71 */     return (regionId / 1000 * WorldConfig.WORLD_REGION_SIZE);
/*     */   }
/*     */ 
/*     */   public static final int getYFrom2dRegionId(int regionId)
/*     */   {
/*  79 */     return (regionId % 1000 * WorldConfig.WORLD_REGION_SIZE);
/*     */   }
/*     */ 
/*     */   public static final int getXFrom3dRegionId(int regionId)
/*     */   {
/*  87 */     return (regionId / 1000000 * WorldConfig.WORLD_REGION_SIZE);
/*     */   }
/*     */ 
/*     */   public static final int getYFrom3dRegionId(int regionId)
/*     */   {
/*  95 */     return (regionId % 1000000 / 1000 * WorldConfig.WORLD_REGION_SIZE);
/*     */   }
/*     */ 
/*     */   public static final int getZFrom3dRegionId(int regionId)
/*     */   {
/* 103 */     return (regionId % 1000000 % 1000 * WorldConfig.WORLD_REGION_SIZE);
/*     */   }
/*     */ }