/*     */ package com.aionemu.gameserver.utils.collections;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ @ThreadSafe
/*     */ public class OptimisticLinkedQueue<E> extends AbstractQueue<E>
/*     */   implements Queue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3445502502831420722L;
/*  82 */   private static final AtomicReferenceFieldUpdater<OptimisticLinkedQueue, Node> tailUpdater = AtomicReferenceFieldUpdater.newUpdater(OptimisticLinkedQueue.class, Node.class, "tail");
/*     */ 
/*  85 */   private static final AtomicReferenceFieldUpdater<OptimisticLinkedQueue, Node> headUpdater = AtomicReferenceFieldUpdater.newUpdater(OptimisticLinkedQueue.class, Node.class, "head");
/*     */ 
/*  99 */   private volatile transient Node<E> head = new Node(null, null);
/*     */ 
/* 103 */   private volatile transient Node<E> tail = ???.head;
/*     */ 
/* 111 */   AtomicInteger count = new AtomicInteger();
/*     */ 
/*     */   private boolean casTail(Node<E> cmp, Node<E> val)
/*     */   {
/*  89 */     return tailUpdater.compareAndSet(this, cmp, val);
/*     */   }
/*     */ 
/*     */   private boolean casHead(Node<E> cmp, Node<E> val) {
/*  93 */     return headUpdater.compareAndSet(this, cmp, val);
/*     */   }
/*     */ 
/*     */   public boolean offer(E e)
/*     */   {
/* 117 */     if (e == null)
/* 118 */       throw new NullPointerException();
/* 119 */     Node n = new Node(e, null);
/*     */     while (true) {
/* 121 */       Node t = this.tail;
/* 122 */       n.setNext(t);
/* 123 */       this.count.incrementAndGet();
/* 124 */       if (casTail(t, n)) {
/* 125 */         t.setPrev(n);
/* 126 */         return true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/*     */     while (true)
/*     */     {
/* 137 */       Node h = this.head;
/* 138 */       Node t = this.tail;
/* 139 */       Node first = h.getPrev();
/* 140 */       if (h == this.head)
/* 141 */         if (h != t) {
/* 142 */           if (first == null) {
/* 143 */             fixList(t, h);
/*     */           }
/*     */ 
/* 146 */           Object item = first.getItem();
/* 147 */           if (casHead(h, first)) {
/* 148 */             h.setNext(null);
/* 149 */             h.setPrev(null);
/* 150 */             this.count.decrementAndGet();
/* 151 */             return item;
/*     */           }
/*     */         }
/*     */         else {
/* 155 */           return null;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fixList(Node<E> t, Node<E> h)
/*     */   {
/* 165 */     Node curNode = t;
/* 166 */     while ((h == this.head) && (curNode != h)) {
/* 167 */       Node curNodeNext = curNode.getNext();
/* 168 */       curNodeNext.setPrev(curNode);
/* 169 */       curNode = curNode.getNext();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 174 */     if (poll() == null);
/*     */   }
/*     */ 
/*     */   public int leaveTail() {
/* 178 */     Object elem = null;
/* 179 */     Object elem1 = null;
/* 180 */     int removed = 0;
/* 181 */     while ((elem = poll()) != null) {
/* 182 */       elem1 = elem;
/* 183 */       ++removed;
/*     */     }
/* 185 */     if (elem1 != null) {
/* 186 */       --removed;
/* 187 */       offer(elem1);
/*     */     }
/* 189 */     return removed;
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 194 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 204 */     return this.count.get();
/*     */   }
/*     */ 
/*     */   private static class Node<E>
/*     */   {
/*     */     private volatile E item;
/*     */     private volatile Node<E> next;
/*     */     private volatile Node<E> prev;
/*     */ 
/*     */     Node(E x)
/*     */     {
/*  44 */       this.item = x;
/*  45 */       this.next = null;
/*  46 */       this.prev = null;
/*     */     }
/*     */ 
/*     */     Node(E x, Node<E> n) {
/*  50 */       this.item = x;
/*  51 */       this.next = n;
/*  52 */       this.prev = null;
/*     */     }
/*     */ 
/*     */     E getItem() {
/*  56 */       return this.item;
/*     */     }
/*     */ 
/*     */     void setItem(E val)
/*     */     {
/*  61 */       this.item = val;
/*     */     }
/*     */ 
/*     */     Node<E> getNext() {
/*  65 */       return this.next;
/*     */     }
/*     */ 
/*     */     void setNext(Node<E> val) {
/*  69 */       this.next = val;
/*     */     }
/*     */ 
/*     */     Node<E> getPrev() {
/*  73 */       return this.prev;
/*     */     }
/*     */ 
/*     */     void setPrev(Node<E> val) {
/*  77 */       this.prev = val;
/*     */     }
/*     */   }
/*     */ }