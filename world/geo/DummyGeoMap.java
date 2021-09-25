/*    */ package com.aionemu.gameserver.world.geo;
/*    */ 
/*    */ import com.aionemu.gameserver.geoEngine.math.Vector3f;
/*    */ import com.aionemu.gameserver.geoEngine.models.GeoMap;
/*    */ import com.aionemu.gameserver.geoEngine.scene.Spatial;
/*    */ 
/*    */ public class DummyGeoMap extends GeoMap
/*    */ {
/*    */   public DummyGeoMap(String name, int worldSize)
/*    */   {
/* 25 */     super(name, worldSize);
/*    */   }
/*    */ 
/*    */   public final float getZ(float x, float y, float z, int instanceId)
/*    */   {
/* 30 */     return z;
/*    */   }
/*    */ 
/*    */   public final boolean canSee(float x, float y, float z, float targetX, float targetY, float targetZ, float limit, int instanceId)
/*    */   {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   public Vector3f getClosestCollision(float x, float y, float z, float targetX, float targetY, float targetZ, boolean changeDirction, boolean fly, int instanceId, byte intentions)
/*    */   {
/* 41 */     return new Vector3f(targetX, targetY, targetZ);
/*    */   }
/*    */ 
/*    */   public void setDoorState(int instanceId, String name, boolean state)
/*    */   {
/*    */   }
/*    */ 
/*    */   public int attachChild(Spatial child)
/*    */   {
/* 52 */     return 0;
/*    */   }
/*    */ }