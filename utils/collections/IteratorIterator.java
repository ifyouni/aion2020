/*     */ package com.aionemu.gameserver.utils.collections;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class IteratorIterator<V>
/*     */   implements Iterator<V>
/*     */ {
/*     */   private Iterator<? extends Iterable<V>> firstLevelIterator;
/*     */   private Iterator<V> secondLevelIterator;
/*     */ 
/*     */   public IteratorIterator(Iterable<? extends Iterable<V>> itit)
/*     */   {
/*  73 */     this.firstLevelIterator = itit.iterator();
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  81 */     if ((this.secondLevelIterator != null) && (this.secondLevelIterator.hasNext())) {
/*  82 */       return true;
/*     */     }
/*  84 */     while (this.firstLevelIterator.hasNext()) {
/*  85 */       Iterable iterable = (Iterable)this.firstLevelIterator.next();
/*     */ 
/*  87 */       if (iterable != null) {
/*  88 */         this.secondLevelIterator = iterable.iterator();
/*     */ 
/*  90 */         if (this.secondLevelIterator.hasNext())
/*  91 */           return true;
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public V next()
/*     */   {
/* 103 */     if ((this.secondLevelIterator == null) || (!(this.secondLevelIterator.hasNext())))
/* 104 */       throw new NoSuchElementException();
/* 105 */     return this.secondLevelIterator.next();
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/* 113 */     throw new UnsupportedOperationException("This operation is not supported.");
/*     */   }
/*     */ }