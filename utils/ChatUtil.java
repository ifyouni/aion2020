/*    */ package com.aionemu.gameserver.utils;
/*    */ 
/*    */ import com.aionemu.gameserver.world.WorldPosition;
/*    */ 
/*    */ public class ChatUtil
/*    */ {
/*    */   public static String position(String label, WorldPosition pos)
/*    */   {
/* 27 */     return position(label, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ());
/*    */   }
/*    */ 
/*    */   public static String position(String label, long worldId, float x, float y, float z)
/*    */   {
/* 32 */     return String.format("[pos:%s;%d %f %f %f -1]", new Object[] { label, Long.valueOf(worldId), Float.valueOf(x), Float.valueOf(y), Float.valueOf(z) });
/*    */   }
/*    */ 
/*    */   public static String item(long itemId) {
/* 36 */     return String.format("[item: %d]", new Object[] { Long.valueOf(itemId) });
/*    */   }
/*    */ 
/*    */   public static String recipe(long recipeId) {
/* 40 */     return String.format("[recipe: %d]", new Object[] { Long.valueOf(recipeId) });
/*    */   }
/*    */ 
/*    */   public static String quest(int questId) {
/* 44 */     return String.format("[quest: %d]", new Object[] { Integer.valueOf(questId) });
/*    */   }
/*    */ 
/*    */   public static String getRealAdminName(String name) {
/* 48 */     int index = name.lastIndexOf(" ");
/* 49 */     if (index == -1)
/* 50 */       return name;
/* 51 */     return name.substring(index + 1);
/*    */   }
/*    */ }