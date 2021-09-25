/*    */ package com.aionemu.gameserver.utils.javaagent;
/*    */ 
/*    */ import com.aionemu.commons.callbacks.Callback;
/*    */ import com.aionemu.commons.callbacks.CallbackResult;
/*    */ import com.aionemu.commons.callbacks.EnhancedObject;
/*    */ import com.aionemu.commons.callbacks.metadata.GlobalCallback;
/*    */ import com.aionemu.commons.callbacks.metadata.ObjectCallback;
/*    */ import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
/*    */ 
/*    */ public class JavaAgentUtils
/*    */ {
/*    */   public static boolean isConfigured()
/*    */   {
/* 17 */     JavaAgentUtils jau = new JavaAgentUtils();
/* 18 */     if (!(jau instanceof EnhancedObject)) {
/* 19 */       throw new Error("Please configure -javaagent jvm option.");
/*    */     }
/* 21 */     if (!(checkGlobalCallback())) {
/* 22 */       throw new Error("Global callbacks are not working correctly!");
/*    */     }
/* 24 */     ((EnhancedObject)jau).addCallback(new CheckCallback());
/* 25 */     if (!(jau.checkObjectCallback())) {
/* 26 */       throw new Error("Object callbacks are not working correctly!");
/*    */     }
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   @GlobalCallback(CheckCallback.class)
/*    */   private static boolean checkGlobalCallback()
/*    */   {
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   @ObjectCallback(CheckCallback.class)
/*    */   private boolean checkObjectCallback()
/*    */   {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 13 */     GlobalCallbackHelper.addCallback(new CheckCallback());
/*    */   }
/*    */ 
/*    */   public static class CheckCallback
/*    */     implements Callback
/*    */   {
/*    */     public CallbackResult<Boolean> beforeCall(Object obj, Object[] args)
/*    */     {
/* 48 */       return CallbackResult.newFullBlocker(Boolean.valueOf(true));
/*    */     }
/*    */ 
/*    */     public CallbackResult<Boolean> afterCall(Object obj, Object[] args, Object methodResult)
/*    */     {
/* 53 */       return CallbackResult.newContinue();
/*    */     }
/*    */ 
/*    */     public Class<? extends Callback> getBaseClass()
/*    */     {
/* 58 */       return CheckCallback.class;
/*    */     }
/*    */   }
/*    */ }