/*    */ package com.aionemu.gameserver.world.knownlist;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ 
/*    */ public class PlayerAwareKnownList extends KnownList
/*    */ {
/*    */   public PlayerAwareKnownList(VisibleObject owner)
/*    */   {
/* 24 */     super(owner);
/*    */   }
/*    */ 
/*    */   protected final boolean isAwareOf(VisibleObject newObject)
/*    */   {
/* 29 */     return newObject instanceof Player;
/*    */   }
/*    */ }