/*    */ package com.aionemu.gameserver.world.knownlist;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ import com.aionemu.gameserver.utils.MathUtil;
/*    */ 
/*    */ public class SphereKnownList extends PlayerAwareKnownList
/*    */ {
/*    */   private final float radius;
/*    */ 
/*    */   public SphereKnownList(VisibleObject owner, float radius)
/*    */   {
/* 26 */     super(owner);
/* 27 */     this.radius = radius;
/*    */   }
/*    */ 
/*    */   protected boolean checkReversedObjectInRange(VisibleObject newObject)
/*    */   {
/* 32 */     return MathUtil.isIn3dRange(this.owner, newObject, this.radius);
/*    */   }
/*    */ }