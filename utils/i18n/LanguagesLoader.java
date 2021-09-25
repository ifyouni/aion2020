/*    */ package com.aionemu.gameserver.utils.i18n;
/*    */ 
/*    */ import com.aionemu.commons.scripting.classlistener.ClassListener;
/*    */ import com.aionemu.commons.utils.ClassUtils;
/*    */ import com.aionemu.gameserver.GameServer;
/*    */ import java.lang.reflect.Modifier;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class LanguagesLoader
/*    */   implements ClassListener
/*    */ {
/* 33 */   private static final Logger log = LoggerFactory.getLogger(Language.class);
/*    */   private final LanguageHandler handler;
/*    */ 
/*    */   public LanguagesLoader(LanguageHandler handler)
/*    */   {
/* 37 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   public void postLoad(Class<?>[] classes)
/*    */   {
/* 43 */     for (Class clazz : classes) {
/* 44 */       if (log.isDebugEnabled()) {
/* 45 */         log.debug("Loading class " + clazz.getName());
/*    */       }
/*    */ 
/* 48 */       if (!(isValidClass(clazz))) {
/*    */         continue;
/*    */       }
/*    */ 
/* 52 */       if (ClassUtils.isSubclass(clazz, Language.class)) {
/* 53 */         Class language = clazz;
/* 54 */         if (language == null) continue;
/*    */         try {
/* 56 */           this.handler.registerLanguage((Language)language.newInstance());
/*    */         }
/*    */         catch (Exception e) {
/* 59 */           log.error("Registering " + language.getName(), e);
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 65 */     GameServer.log.info("[LanguagesLoader] Loaded " + this.handler.size() + " custom message handlers.");
/*    */   }
/*    */ 
/*    */   public void preUnload(Class<?>[] classes)
/*    */   {
/* 70 */     if (log.isDebugEnabled()) {
/* 71 */       for (Class clazz : classes) {
/* 72 */         log.debug("Unload language " + clazz.getName());
/*    */       }
/*    */     }
/* 75 */     this.handler.clear();
/*    */   }
/*    */ 
/*    */   public boolean isValidClass(Class<?> clazz) {
/* 79 */     int modifiers = clazz.getModifiers();
/*    */ 
/* 81 */     if ((Modifier.isAbstract(modifiers)) || (Modifier.isInterface(modifiers))) {
/* 82 */       return false;
/*    */     }
/*    */ 
/* 86 */     return (!(Modifier.isPublic(modifiers)));
/*    */   }
/*    */ }