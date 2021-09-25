/*    */ package com.aionemu.gameserver.utils.chathandlers;
/*    */ 
/*    */ import com.aionemu.commons.scripting.classlistener.ClassListener;
/*    */ import com.aionemu.commons.utils.ClassUtils;
/*    */ import java.lang.reflect.Modifier;
/*    */ 
/*    */ public class ChatCommandsLoader
/*    */   implements ClassListener
/*    */ {
/*    */   private ChatProcessor processor;
/*    */ 
/*    */   public ChatCommandsLoader(ChatProcessor processor)
/*    */   {
/* 18 */     this.processor = processor;
/*    */   }
/*    */ 
/*    */   public void postLoad(Class<?>[] classes)
/*    */   {
/* 23 */     for (Class c : classes) {
/* 24 */       if (!(isValidClass(c)))
/*    */         continue;
/* 26 */       Class tmp = c;
/* 27 */       if (tmp == null) continue;
/*    */       try {
/* 29 */         this.processor.registerCommand((ChatCommand)tmp.newInstance());
/*    */       }
/*    */       catch (InstantiationException e) {
/* 32 */         e.printStackTrace();
/*    */       }
/*    */       catch (IllegalAccessException e) {
/* 35 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 38 */     this.processor.onCompileDone();
/*    */   }
/*    */ 
/*    */   public void preUnload(Class<?>[] classes)
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean isValidClass(Class<?> clazz)
/*    */   {
/* 47 */     int modifiers = clazz.getModifiers();
/*    */ 
/* 49 */     if ((Modifier.isAbstract(modifiers)) || (Modifier.isInterface(modifiers))) {
/* 50 */       return false;
/*    */     }
/* 52 */     if (!(Modifier.isPublic(modifiers))) {
/* 53 */       return false;
/*    */     }
/*    */ 
/* 57 */     return ((!(ClassUtils.isSubclass(clazz, AdminCommand.class))) && (!(ClassUtils.isSubclass(clazz, PlayerCommand.class))) && (!(ClassUtils.isSubclass(clazz, WeddingCommand.class))));
/*    */   }
/*    */ }