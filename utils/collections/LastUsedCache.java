/*     */ package com.aionemu.gameserver.utils.collections;
/*     */ 
/*     */ import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class LastUsedCache<K extends Comparable, V>
/*     */   implements ICache<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3674312987828041877L;
/*  31 */   Map<K, Item> map = PlatformDependent.newConcurrentHashMap();
/*  32 */   Item startItem = new Item();
/*  33 */   Item endItem = new Item();
/*     */   int maxSize;
/*  35 */   private final Object syncRoot = new Object();
/*     */ 
/*     */   void removeItem(Item item)
/*     */   {
/*  53 */     synchronized (this.syncRoot) {
/*  54 */       item.previous.next = item.next;
/*  55 */       item.next.previous = item.previous;
/*     */     }
/*     */   }
/*     */ 
/*     */   void insertHead(Item item) {
/*  60 */     synchronized (this.syncRoot) {
/*  61 */       item.previous = this.startItem;
/*  62 */       item.next = this.startItem.next;
/*  63 */       this.startItem.next.previous = item;
/*  64 */       this.startItem.next = item;
/*     */     }
/*     */   }
/*     */ 
/*     */   void moveToHead(Item item) {
/*  69 */     synchronized (this.syncRoot) {
/*  70 */       item.previous.next = item.next;
/*  71 */       item.next.previous = item.previous;
/*  72 */       item.previous = this.startItem;
/*  73 */       item.next = this.startItem.next;
/*  74 */       this.startItem.next.previous = item;
/*  75 */       this.startItem.next = item;
/*     */     }
/*     */   }
/*     */ 
/*     */   public LastUsedCache(int maxObjects) {
/*  80 */     this.maxSize = maxObjects;
/*  81 */     this.startItem.next = this.endItem;
/*  82 */     this.endItem.previous = this.startItem;
/*     */   }
/*     */ 
/*     */   public CachePair[] getAll()
/*     */   {
/*  87 */     CachePair[] p = new CachePair[this.maxSize];
/*  88 */     int count = 0;
/*     */ 
/*  90 */     synchronized (this.syncRoot) {
/*  91 */       Item cur = this.startItem.next;
/*  92 */       while (cur != this.endItem) {
/*  93 */         p[count] = new CachePair(cur.key, cur.value);
/*  94 */         ++count;
/*  95 */         cur = cur.next;
/*     */       }
/*     */     }
/*     */ 
/*  99 */     CachePair[] np = new CachePair[count];
/* 100 */     System.arraycopy(p, 0, np, 0, count);
/* 101 */     return np;
/*     */   }
/*     */ 
/*     */   public V get(K key)
/*     */   {
/* 109 */     Item cur = (Item)this.map.get(key);
/* 110 */     if (cur == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     if (cur != this.startItem.next)
/* 114 */       moveToHead(cur);
/* 115 */     return cur.value;
/*     */   }
/*     */ 
/*     */   public void put(K key, V value)
/*     */   {
/* 123 */     Item cur = (Item)this.map.get(key);
/* 124 */     if (cur != null) {
/* 125 */       cur.value = value;
/* 126 */       moveToHead(cur);
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     if ((this.map.size() >= this.maxSize) && (this.maxSize != 0)) {
/* 131 */       cur = this.endItem.previous;
/* 132 */       this.map.remove(cur.key);
/* 133 */       removeItem(cur);
/*     */     }
/*     */ 
/* 136 */     Item item = new Item(key, value);
/* 137 */     insertHead(item);
/* 138 */     this.map.put(key, item);
/*     */   }
/*     */ 
/*     */   public void remove(K key)
/*     */   {
/* 143 */     Item cur = (Item)this.map.get(key);
/* 144 */     if (cur == null)
/* 145 */       return;
/* 146 */     this.map.remove(key);
/* 147 */     removeItem(cur);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 152 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   static class Item
/*     */   {
/*     */     public Comparable key;
/*     */     public Object value;
/*     */     public Item previous;
/*     */     public Item next;
/*     */ 
/*     */     public Item(Comparable k, Object v)
/*     */     {
/*  40 */       this.key = k;
/*  41 */       this.value = v;
/*     */     }
/*     */ 
/*     */     public Item()
/*     */     {
/*     */     }
/*     */   }
/*     */ }