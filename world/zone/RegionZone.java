/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.WorldConfig;
/*    */ import com.aionemu.gameserver.model.geometry.AbstractArea;
/*    */ import com.aionemu.gameserver.model.geometry.RectangleArea;
/*    */ 
/*    */ public class RegionZone extends RectangleArea
/*    */ {
/*    */   public RegionZone(float startX, float startY, float minZ, float maxZ)
/*    */   {
/* 25 */     super(null, 0, startX, startY, startX + WorldConfig.WORLD_REGION_SIZE, startY + WorldConfig.WORLD_REGION_SIZE, minZ, maxZ);
/*    */   }
/*    */ 
/*    */   public boolean isInside(AbstractArea area) {
/* 29 */     return true;
/*    */   }
/*    */ }