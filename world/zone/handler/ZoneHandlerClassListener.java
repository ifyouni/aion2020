/*    */ package com.aionemu.gameserver.world.zone.handler;
/*    */ 
/*    */ import com.aionemu.commons.scripting.classlistener.ClassListener;
/*    */ import com.aionemu.commons.utils.ClassUtils;
/*    */ import com.aionemu.gameserver.instance.InstanceHandlerClassListener;
/*    */ import com.aionemu.gameserver.world.zone.ZoneService;
/*    */ import java.lang.reflect.Modifier;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ZoneHandlerClassListener
/*    */   implements ClassListener
/*    */ {
/* 31 */   private static final Logger log = LoggerFactory.getLogger(InstanceHandlerClassListener.class);
/*    */ 
/*    */   public void postLoad(Class<?>[] classes)
/*    */   {
/* 36 */     for (Class c : classes) {
/* 37 */       if (log.isDebugEnabled()) {
/* 38 */         log.debug("Load class " + c.getName());
/*    */       }
/* 40 */       if (!(isValidClass(c))) {
/*    */         continue;
/*    */       }
/* 43 */       if (ClassUtils.isSubclass(c, ZoneHandler.class)) {
/* 44 */         Class tmp = c;
/* 45 */         if (tmp != null)
/* 46 */           ZoneService.getInstance().addZoneHandlerClass(tmp);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void preUnload(Class<?>[] classes)
/*    */   {
/* 54 */     if (log.isDebugEnabled())
/* 55 */       for (Class c : classes)
/* 56 */         log.debug("Unload class " + c.getName());
/*    */   }
/*    */ 
/*    */   public boolean isValidClass(Class<?> clazz)
/*    */   {
/* 61 */     int modifiers = clazz.getModifiers();
/*    */ 
/* 63 */     if ((Modifier.isAbstract(modifiers)) || (Modifier.isInterface(modifiers))) {
/* 64 */       return false;
/*    */     }
/*    */ 
/* 67 */     return (!(Modifier.isPublic(modifiers)));
/*    */   }
/*    */ }