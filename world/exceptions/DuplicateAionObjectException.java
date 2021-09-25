/*    */ package com.aionemu.gameserver.world.exceptions;
/*    */ 
/*    */ public class DuplicateAionObjectException extends RuntimeException
/*    */ {
/*    */   public DuplicateAionObjectException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public DuplicateAionObjectException(String s)
/*    */   {
/* 42 */     super(s);
/*    */   }
/*    */ 
/*    */   public DuplicateAionObjectException(String message, Throwable cause)
/*    */   {
/* 54 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public DuplicateAionObjectException(Throwable cause)
/*    */   {
/* 64 */     super(cause);
/*    */   }
/*    */ }