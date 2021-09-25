/*    */ package com.aionemu.gameserver.utils.collections;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ListSplitter<T>
/*    */ {
/*    */   private T[] objects;
/*    */   private Class<?> componentType;
/*    */   private int splitCount;
/* 33 */   private int curentIndex = 0;
/* 34 */   private int length = 0;
/*    */ 
/*    */   public ListSplitter(Collection<T> collection, int splitCount)
/*    */   {
/* 38 */     if ((collection != null) && (collection.size() > 0)) {
/* 39 */       this.splitCount = splitCount;
/* 40 */       this.length = collection.size();
/* 41 */       this.objects = collection.toArray((Object[])new Object[this.length]);
/* 42 */       this.componentType = this.objects.getClass().getComponentType();
/*    */     }
/*    */   }
/*    */ 
/*    */   public List<T> getNext(int splitCount) {
/* 47 */     this.splitCount = splitCount;
/* 48 */     return getNext();
/*    */   }
/*    */ 
/*    */   public List<T> getNext()
/*    */   {
/* 53 */     Object[] subArray = (Object[])(Object[])Array.newInstance(this.componentType, Math.min(this.splitCount, this.length - this.curentIndex));
/* 54 */     if (subArray.length > 0) {
/* 55 */       System.arraycopy(this.objects, this.curentIndex, subArray, 0, subArray.length);
/* 56 */       this.curentIndex += subArray.length;
/*    */     }
/* 58 */     return Arrays.asList(subArray);
/*    */   }
/*    */ 
/*    */   public int size() {
/* 62 */     return this.length;
/*    */   }
/*    */ 
/*    */   public boolean isFirst() {
/* 66 */     return (this.curentIndex <= this.splitCount);
/*    */   }
/*    */ 
/*    */   public boolean isLast() {
/* 70 */     return (this.curentIndex == this.length);
/*    */   }
/*    */ }