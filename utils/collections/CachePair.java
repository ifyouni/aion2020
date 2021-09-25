/*    */ package com.aionemu.gameserver.utils.collections;
/*    */ 
/*    */ public class CachePair<K extends Comparable, V>
/*    */   implements Comparable<CachePair>
/*    */ {
/*    */   public K key;
/*    */   public V value;
/*    */ 
/*    */   public CachePair(K key, V value)
/*    */   {
/* 26 */     this.key = key;
/* 27 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 34 */     if (obj instanceof CachePair) {
/* 35 */       CachePair p = (CachePair)obj;
/* 36 */       return ((this.key.equals(p.key)) && (this.value.equals(p.value)));
/*    */     }
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */   public int compareTo(CachePair p) {
/* 42 */     int v = this.key.compareTo(p.key);
/* 43 */     if ((v == 0) && (p.value instanceof Comparable))
/* 44 */       return ((Comparable)this.value).compareTo(p.value);
/* 45 */     return v;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 50 */     int result = this.key.hashCode();
/* 51 */     result = 37 * result + this.value.hashCode();
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     return this.key + ": " + this.value;
/*    */   }
/*    */ }