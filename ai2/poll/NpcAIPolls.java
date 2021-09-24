/*    */ package com.aionemu.gameserver.ai2.poll;
/*    */ 
/*    */ import com.aionemu.gameserver.ai2.NpcAI2;
/*    */ 
/*    */ public class NpcAIPolls
/*    */ {
/*    */   public static AIAnswer shouldDecay(NpcAI2 npcAI)
/*    */   {
/* 26 */     return AIAnswers.POSITIVE;
/*    */   }
/*    */ 
/*    */   public static AIAnswer shouldRespawn(NpcAI2 npcAI)
/*    */   {
/* 34 */     return AIAnswers.POSITIVE;
/*    */   }
/*    */ }