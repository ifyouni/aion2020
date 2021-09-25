/*     */ package com.aionemu.gameserver.utils.idfactory;
/*     */ 
/*     */ import com.aionemu.commons.database.dao.DAOManager;
/*     */ import com.aionemu.commons.utils.GenericValidator;
/*     */ import com.aionemu.gameserver.dao.GuideDAO;
/*     */ import com.aionemu.gameserver.dao.HousesDAO;
/*     */ import com.aionemu.gameserver.dao.InventoryDAO;
/*     */ import com.aionemu.gameserver.dao.LegionDAO;
/*     */ import com.aionemu.gameserver.dao.MailDAO;
/*     */ import com.aionemu.gameserver.dao.PlayerDAO;
/*     */ import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class IDFactory
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(IDFactory.class);
/*     */   private final BitSet idList;
/*     */   private final ReentrantLock lock;
/*  53 */   private volatile int nextMinId = 1;
/*     */ 
/*     */   private IDFactory()
/*     */   {
/*  64 */     this.idList = new BitSet();
/*  65 */     this.lock = new ReentrantLock();
/*  66 */     lockIds(new int[] { 0 });
/*     */ 
/*  69 */     lockIds(((PlayerDAO)DAOManager.getDAO(PlayerDAO.class)).getUsedIDs());
/*  70 */     lockIds(((InventoryDAO)DAOManager.getDAO(InventoryDAO.class)).getUsedIDs());
/*  71 */     lockIds(((PlayerRegisteredItemsDAO)DAOManager.getDAO(PlayerRegisteredItemsDAO.class)).getUsedIDs());
/*  72 */     lockIds(((LegionDAO)DAOManager.getDAO(LegionDAO.class)).getUsedIDs());
/*  73 */     lockIds(((MailDAO)DAOManager.getDAO(MailDAO.class)).getUsedIDs());
/*  74 */     lockIds(((GuideDAO)DAOManager.getDAO(GuideDAO.class)).getUsedIDs());
/*  75 */     lockIds(((HousesDAO)DAOManager.getDAO(HousesDAO.class)).getUsedIDs());
/*  76 */     log.info("加载 " + getUsedCount() + " 个Id.");
/*     */   }
/*     */ 
/*     */   public static final IDFactory getInstance() {
/*  80 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   public int nextId()
/*     */   {
/*     */     try
/*     */     {
/*     */       int id;
/*  85 */       this.lock.lock();
/*     */ 
/*  88 */       if (this.nextMinId == -2147483648)
/*     */       {
/*  91 */         id = -2147483648;
/*     */       }
/*     */       else {
/*  94 */         id = this.idList.nextClearBit(this.nextMinId);
/*     */       }
/*     */ 
/*  99 */       if (id == -2147483648) {
/* 100 */         throw new IDFactoryError("All id's are used, please clear your database");
/*     */       }
/* 102 */       this.idList.set(id);
/*     */ 
/* 105 */       this.nextMinId = (id + 1);
/* 106 */       int i = id;
/*     */ 
/* 109 */       return i; } finally { this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void lockIds(int[] ids)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       this.lock.lock();
/* 124 */       for (int id : ids) {
/* 125 */         boolean status = this.idList.get(id);
/* 126 */         if (status) {
/* 127 */           throw new IDFactoryError("ID " + id + " is already taken, fatal error!!!");
/*     */         }
/* 129 */         this.idList.set(id);
/*     */       }
/*     */     }
/*     */     finally {
/* 133 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void lockIds(Iterable<Integer> ids)
/*     */   {
/*     */     Iterator i$;
/*     */     try
/*     */     {
/* 147 */       this.lock.lock();
/* 148 */       for (i$ = ids.iterator(); i$.hasNext(); ) { int id = ((Integer)i$.next()).intValue();
/* 149 */         boolean status = this.idList.get(id);
/* 150 */         if (status) {
/* 151 */           throw new IDFactoryError("ID " + id + " is already taken, fatal error!!!");
/*     */         }
/* 153 */         this.idList.set(id);
/*     */       }
/*     */     }
/*     */     finally {
/* 157 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void releaseId(int id)
/*     */   {
/*     */     try
/*     */     {
/* 171 */       this.lock.lock();
/* 172 */       boolean status = this.idList.get(id);
/* 173 */       if (!(status)) {
/* 174 */         throw new IDFactoryError("ID " + id + " is not taken, can't release it.");
/*     */       }
/* 176 */       this.idList.clear(id);
/* 177 */       if ((id < this.nextMinId) || (this.nextMinId == -2147483648))
/* 178 */         this.nextMinId = id;
/*     */     }
/*     */     finally
/*     */     {
/* 182 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void releaseIds(Collection<Integer> ids) {
/* 187 */     if (GenericValidator.isBlankOrNull(ids)) {
/* 188 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 192 */       this.lock.lock();
/* 193 */       for (Integer id : ids) {
/* 194 */         boolean status = this.idList.get(id.intValue());
/* 195 */         if (!(status)) {
/* 196 */           throw new IDFactoryError("ID " + id + " is not taken, can't release it.");
/*     */         }
/* 198 */         this.idList.clear(id.intValue());
/* 199 */         if ((id.intValue() < this.nextMinId) || (this.nextMinId == -2147483648))
/* 200 */           this.nextMinId = id.intValue();
/*     */       }
/*     */     }
/*     */     finally {
/* 204 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getUsedCount()
/*     */   {
/*     */     try
/*     */     {
/* 215 */       this.lock.lock();
/* 216 */       int i = this.idList.cardinality();
/*     */ 
/* 219 */       return i; } finally { this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/* 226 */     protected static final IDFactory instance = new IDFactory(null);
/*     */   }
/*     */ }