/*    */ package com.aionemu.gameserver.ai2;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.AIConfig;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class AI2Logger
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(AI2Logger.class);
/*    */ 
/*    */   public static final void info(AbstractAI ai, String message) {
/* 28 */     if (ai.isLogging())
/* 29 */       log.info("[AI2] " + ai.getOwner().getObjectId() + " - " + message);
/*    */   }
/*    */ 
/*    */   public static final void info(AI2 ai, String message)
/*    */   {
/* 34 */     info((AbstractAI)ai, message);
/*    */   }
/*    */ 
/*    */   public static void moveinfo(Creature owner, String message)
/*    */   {
/* 42 */     if ((AIConfig.MOVE_DEBUG) && (owner.getAi2().isLogging()))
/* 43 */       log.info("[AI2] " + owner.getObjectId() + " - " + message);
/*    */   }
/*    */ }