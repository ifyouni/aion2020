/*    */ package com.aionemu.gameserver.ai2.poll;
/*    */ 
/*    */ public class SimpleAIAnswer
/*    */   implements AIAnswer
/*    */ {
/*    */   private final boolean answer;
/*    */ 
/*    */   SimpleAIAnswer(boolean answer)
/*    */   {
/* 26 */     this.answer = answer;
/*    */   }
/*    */ 
/*    */   public boolean isPositive()
/*    */   {
/* 31 */     return this.answer;
/*    */   }
/*    */ 
/*    */   public Object getResult()
/*    */   {
/* 36 */     return Boolean.valueOf(this.answer);
/*    */   }
/*    */ }