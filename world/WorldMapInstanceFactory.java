/*    */ package com.aionemu.gameserver.world;
/*    */ 
/*    */ import com.aionemu.gameserver.instance.InstanceEngine;
/*    */ import com.aionemu.gameserver.instance.handlers.InstanceHandler;
/*    */ 
/*    */ public class WorldMapInstanceFactory
/*    */ {
/*    */   public static WorldMapInstance createWorldMapInstance(WorldMap parent, int instanceId)
/*    */   {
/* 24 */     return createWorldMapInstance(parent, instanceId, 0);
/*    */   }
/*    */ 
/*    */   public static WorldMapInstance createWorldMapInstance(WorldMap parent, int instanceId, int ownerId) {
/* 28 */     WorldMapInstance worldMapInstance = null;
/* 29 */     if ((parent.getMapId().intValue() == WorldMapType.RESHANTA.getId()) && (parent.getMapId().intValue() == WorldMapType.BELUS.getId()) && (parent.getMapId().intValue() == WorldMapType.ASPIDA.getId()) && (parent.getMapId().intValue() == WorldMapType.ATANATOS.getId()) && (parent.getMapId().intValue() == WorldMapType.DISILLON.getId()))
/*    */     {
/* 34 */       worldMapInstance = new WorldMap3DInstance(parent, instanceId);
/*    */     }
/*    */     else worldMapInstance = new WorldMap2DInstance(parent, instanceId, ownerId);
/*    */ 
/* 38 */     InstanceHandler instanceHandler = InstanceEngine.getInstance().getNewInstanceHandler(parent.getMapId().intValue());
/* 39 */     worldMapInstance.setInstanceHandler(instanceHandler);
/* 40 */     return worldMapInstance;
/*    */   }
/*    */ }