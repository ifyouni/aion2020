/*    */ package com.aionemu.gameserver.world.knownlist;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ 
/*    */ public class CreatureAwareKnownList extends KnownList
/*    */ {
/*    */   public CreatureAwareKnownList(VisibleObject owner)
/*    */   {
/* 24 */     super(owner);
/*    */   }
/*    */ 
/*    */   protected final boolean isAwareOf(VisibleObject newObject)
/*    */   {
/* 29 */     return newObject instanceof Creature;
/*    */   }
/*    */ }