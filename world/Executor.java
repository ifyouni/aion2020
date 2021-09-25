/*    */ package com.aionemu.gameserver.world;
/*    */ 
/*    */ import com.aionemu.gameserver.model.gameobjects.AionObject;
/*    */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*    */ import java.util.Collection;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public abstract class Executor<T extends AionObject>
/*    */ {
/* 31 */   private static final Logger log = LoggerFactory.getLogger(Executor.class);
/*    */ 
/*    */   public abstract boolean run(T paramT);
/*    */ 
/*    */   private final void runImpl(Collection<T> objects) {
/*    */     try {
/* 37 */       for (AionObject o : objects) {
/* 38 */         if ((o != null) && 
/* 39 */           (!(run(o))))
/*    */           break;
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 45 */       log.warn(e.getMessage(), e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void execute(Collection<T> objects, boolean now) {
/* 50 */     if (now) {
/* 51 */       runImpl(objects);
/*    */     }
/*    */     else
/* 54 */       ThreadPoolManager.getInstance().execute(new Runnable(objects)
/*    */       {
/*    */         public void run()
/*    */         {
/* 58 */           Executor.this.runImpl(this.val$objects);
/*    */         }
/*    */       });
/*    */   }
/*    */ 
/*    */   public final void execute(Collection<T> objects)
/*    */   {
/* 65 */     execute(objects, false);
/*    */   }
/*    */ }