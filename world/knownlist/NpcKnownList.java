/*    */ package com.aionemu.gameserver.world.knownlist;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ import com.aionemu.gameserver.world.MapRegion;
/*    */ 
/*    */ public class NpcKnownList extends CreatureAwareKnownList
/*    */ {
/*    */   public NpcKnownList(VisibleObject owner)
/*    */   {
/* 24 */     super(owner);
/*    */   }
/*    */ 
/*    */   public void doUpdate()
/*    */   {
/* 29 */     MapRegion activeRegion = this.owner.getActiveRegion();
/* 30 */     if ((activeRegion != null) && (activeRegion.isMapRegionActive()))
/* 31 */       super.doUpdate();
/*    */     else
/* 33 */       clear();
/*    */   }
/*    */ }