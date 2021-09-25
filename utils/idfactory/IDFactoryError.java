/*    */ package com.aionemu.gameserver.utils.idfactory;
/*    */ 
/*    */ public class IDFactoryError extends Error
/*    */ {
/*    */   public IDFactoryError()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IDFactoryError(String message)
/*    */   {
/* 33 */     super(message);
/*    */   }
/*    */ 
/*    */   public IDFactoryError(String message, Throwable cause) {
/* 37 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public IDFactoryError(Throwable cause) {
/* 41 */     super(cause);
/*    */   }
/*    */ }