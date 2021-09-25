/*    */ package com.aionemu.gameserver.world.exceptions;
/*    */ 
/*    */ public class AlreadySpawnedException extends RuntimeException
/*    */ {
/*    */   public AlreadySpawnedException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public AlreadySpawnedException(String s)
/*    */   {
/* 37 */     super(s);
/*    */   }
/*    */ 
/*    */   public AlreadySpawnedException(String message, Throwable cause)
/*    */   {
/* 49 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public AlreadySpawnedException(Throwable cause)
/*    */   {
/* 59 */     super(cause);
/*    */   }
/*    */ }