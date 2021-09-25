/*    */ package com.aionemu.gameserver.utils.collections.cachemap;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ abstract class AbstractCacheMap<K, V>
/*    */   implements CacheMap<K, V>
/*    */ {
/*    */   private final Logger log;
/*    */   protected final String cacheName;
/*    */   protected final String valueName;
/* 41 */   protected final Map<K, Reference<V>> cacheMap = new HashMap();
/*    */ 
/* 43 */   protected final ReferenceQueue<V> refQueue = new ReferenceQueue();
/*    */ 
/*    */   AbstractCacheMap(String cacheName, String valueName, Logger log)
/*    */   {
/* 50 */     this.cacheName = "#CACHE  [" + cacheName + "]#  ";
/* 51 */     this.valueName = valueName;
/* 52 */     this.log = log;
/*    */   }
/*    */ 
/*    */   public void put(K key, V value)
/*    */   {
/* 58 */     cleanQueue();
/*    */ 
/* 60 */     if (this.cacheMap.containsKey(key)) {
/* 61 */       throw new IllegalArgumentException("Key: " + key + " already exists in map");
/*    */     }
/* 63 */     Reference entry = newReference(key, value, this.refQueue);
/*    */ 
/* 65 */     this.cacheMap.put(key, entry);
/*    */ 
/* 67 */     if (this.log.isDebugEnabled())
/* 68 */       this.log.debug(this.cacheName + " : added " + this.valueName + " for key: " + key);
/*    */   }
/*    */ 
/*    */   public V get(K key)
/*    */   {
/* 74 */     cleanQueue();
/*    */ 
/* 76 */     Reference reference = (Reference)this.cacheMap.get(key);
/*    */ 
/* 78 */     if (reference == null) {
/* 79 */       return null;
/*    */     }
/* 81 */     Object res = reference.get();
/*    */ 
/* 83 */     if ((res != null) && (this.log.isDebugEnabled())) {
/* 84 */       this.log.debug(this.cacheName + " : obtained " + this.valueName + " for key: " + key);
/*    */     }
/* 86 */     return res;
/*    */   }
/*    */ 
/*    */   public boolean contains(K key)
/*    */   {
/* 91 */     cleanQueue();
/* 92 */     return this.cacheMap.containsKey(key);
/*    */   }
/*    */ 
/*    */   protected abstract void cleanQueue();
/*    */ 
/*    */   public void remove(K key)
/*    */   {
/* 99 */     this.cacheMap.remove(key);
/*    */   }
/*    */ 
/*    */   protected abstract Reference<V> newReference(K paramK, V paramV, ReferenceQueue<V> paramReferenceQueue);
/*    */ }