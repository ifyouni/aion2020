/*    */ package com.aionemu.gameserver.utils.collections.cachemap;
/*    */ 
/*    */ import com.aionemu.gameserver.configs.main.CacheConfig;
/*    */ 
/*    */ public class CacheMapFactory
/*    */ {
/*    */   public static <K, V> CacheMap<K, V> createCacheMap(String cacheName, String valueName)
/*    */   {
/* 41 */     if (CacheConfig.SOFT_CACHE_MAP) {
/* 42 */       return createSoftCacheMap(cacheName, valueName);
/*    */     }
/* 44 */     return createWeakCacheMap(cacheName, valueName);
/*    */   }
/*    */ 
/*    */   public static <K, V> CacheMap<K, V> createSoftCacheMap(String cacheName, String valueName)
/*    */   {
/* 61 */     return new SoftCacheMap(cacheName, valueName);
/*    */   }
/*    */ 
/*    */   public static <K, V> CacheMap<K, V> createWeakCacheMap(String cacheName, String valueName)
/*    */   {
/* 78 */     return new WeakCacheMap(cacheName, valueName);
/*    */   }
/*    */ }