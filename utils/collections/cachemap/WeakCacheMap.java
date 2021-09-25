/*    */ package com.aionemu.gameserver.utils.collections.cachemap;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ class WeakCacheMap<K, V> extends AbstractCacheMap<K, V>
/*    */   implements CacheMap<K, V>
/*    */ {
/* 36 */   private static final Logger log = LoggerFactory.getLogger(WeakCacheMap.class);
/*    */ 
/*    */   WeakCacheMap(String cacheName, String valueName)
/*    */   {
/* 58 */     super(cacheName, valueName, log);
/*    */   }
/*    */ 
/*    */   protected synchronized void cleanQueue()
/*    */   {
/* 64 */     Entry en = null;
/* 65 */     while ((en = (Entry)this.refQueue.poll()) != null) {
/* 66 */       Object key = en.getKey();
/* 67 */       if (log.isDebugEnabled())
/* 68 */         log.debug(this.cacheName + " : cleaned up " + this.valueName + " for key: " + key);
/* 69 */       this.cacheMap.remove(key);
/*    */     }
/*    */   }
/*    */ 
/*    */   protected Reference<V> newReference(K key, V value, ReferenceQueue<V> vReferenceQueue)
/*    */   {
/* 75 */     return new Entry(key, value, vReferenceQueue);
/*    */   }
/*    */ 
/*    */   private class Entry extends WeakReference<V>
/*    */   {
/*    */     private K key;
/*    */ 
/*    */     Entry(V key, ReferenceQueue<? super V> referent)
/*    */     {
/* 48 */       super(referent, q);
/* 49 */       this.key = key;
/*    */     }
/*    */ 
/*    */     K getKey() {
/* 53 */       return this.key;
/*    */     }
/*    */   }
/*    */ }