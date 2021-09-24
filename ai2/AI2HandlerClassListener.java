/*    */ package com.aionemu.gameserver.ai2;
/*    */ 
/*    */ import com.aionemu.commons.scripting.classlistener.ClassListener;
/*    */ import com.aionemu.commons.utils.ClassUtils;
/*    */ import java.lang.reflect.Modifier;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class AI2HandlerClassListener
/*    */   implements ClassListener
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(AI2HandlerClassListener.class);
/*    */ 
/*    */   public void postLoad(Class<?>[] classes)
/*    */   {
/* 32 */     for (Class c : classes) {
/* 33 */       if (log.isDebugEnabled()) {
/* 34 */         log.debug("Load class " + c.getName());
/*    */       }
/* 36 */       if (!(isValidClass(c))) {
/*    */         continue;
/*    */       }
/* 39 */       if (ClassUtils.isSubclass(c, AbstractAI.class)) {
/* 40 */         Class tmp = c;
/* 41 */         if (tmp != null)
/* 42 */           AI2Engine.getInstance().registerAI(tmp);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void preUnload(Class<?>[] classes)
/*    */   {
/* 50 */     if (log.isDebugEnabled())
/* 51 */       for (Class c : classes)
/* 52 */         log.debug("Unload class " + c.getName());
/*    */   }
/*    */ 
/*    */   public boolean isValidClass(Class<?> clazz)
/*    */   {
/* 57 */     int modifiers = clazz.getModifiers();
/*    */ 
/* 59 */     if ((Modifier.isAbstract(modifiers)) || (Modifier.isInterface(modifiers))) {
/* 60 */       return false;
/*    */     }
/*    */ 
/* 63 */     return (!(Modifier.isPublic(modifiers)));
/*    */   }
/*    */ }