/*    */ package com.aionemu.gameserver.world.exceptions;
/*    */ 
/*    */ public class NotSetPositionException extends RuntimeException
/*    */ {
/*    */   public NotSetPositionException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NotSetPositionException(String s)
/*    */   {
/* 38 */     super(s);
/*    */   }
/*    */ 
/*    */   public NotSetPositionException(String message, Throwable cause)
/*    */   {
/* 50 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public NotSetPositionException(Throwable cause)
/*    */   {
/* 60 */     super(cause);
/*    */   }
/*    */ }