/*    */ package com.aionemu.gameserver;
/*    */ 
/*    */ public class GameServerError extends Error
/*    */ {
/*    */   private static final long serialVersionUID = -7445873741878754767L;
/*    */ 
/*    */   public GameServerError()
/*    */   {
/*    */   }
/*    */ 
/*    */   public GameServerError(Throwable cause)
/*    */   {
/* 44 */     super(cause);
/*    */   }
/*    */ 
/*    */   public GameServerError(String message)
/*    */   {
/* 55 */     super(message);
/*    */   }
/*    */ 
/*    */   public GameServerError(String message, Throwable cause)
/*    */   {
/* 72 */     super(message, cause);
/*    */   }
/*    */ }