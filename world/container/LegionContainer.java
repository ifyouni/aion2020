/*    */ package com.aionemu.gameserver.world.container;
/*    */ 
/*    */ import com.aionemu.gameserver.model.team.legion.Legion;
/*    */ import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javolution.util.FastList;
/*    */ import javolution.util.FastMap;
/*    */ 
/*    */ public class LegionContainer
/*    */   implements Iterable<Legion>
/*    */ {
/*    */   private final Map<Integer, Legion> legionsById;
/*    */   private final Map<String, Legion> legionsByName;
/*    */ 
/*    */   public LegionContainer()
/*    */   {
/* 13 */     this.legionsById = new FastMap().shared();
/* 14 */     this.legionsByName = new FastMap().shared(); }
/*    */ 
/*    */   public void add(Legion legion) {
/* 17 */     if ((legion == null) || (legion.getLegionName() == null))
/* 18 */       return;
/* 19 */     if (this.legionsById.put(Integer.valueOf(legion.getLegionId()), legion) != null)
/* 20 */       throw new DuplicateAionObjectException();
/* 21 */     if (this.legionsByName.put(legion.getLegionName().toLowerCase(), legion) != null)
/* 22 */       throw new DuplicateAionObjectException();
/*    */   }
/*    */ 
/*    */   public void remove(Legion legion)
/*    */   {
/* 27 */     this.legionsById.remove(Integer.valueOf(legion.getLegionId()));
/* 28 */     this.legionsByName.remove(legion.getLegionName().toLowerCase());
/*    */   }
/*    */ 
/*    */   public Legion get(int legionId) {
/* 32 */     return ((Legion)this.legionsById.get(Integer.valueOf(legionId)));
/*    */   }
/*    */ 
/*    */   public Legion get(String name) {
/* 36 */     return ((Legion)this.legionsByName.get(name.toLowerCase()));
/*    */   }
/*    */ 
/*    */   public FastList<Legion> getAllLegions() {
/* 40 */     FastList list = new FastList();
/* 41 */     list.addAll(this.legionsByName.values());
/* 42 */     return list;
/*    */   }
/*    */ 
/*    */   public boolean contains(int legionId) {
/* 46 */     return this.legionsById.containsKey(Integer.valueOf(legionId));
/*    */   }
/*    */ 
/*    */   public boolean contains(String name) {
/* 50 */     return this.legionsByName.containsKey(name.toLowerCase());
/*    */   }
/*    */ 
/*    */   public Iterator<Legion> iterator()
/*    */   {
/* 55 */     return this.legionsById.values().iterator();
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 59 */     this.legionsById.clear();
/* 60 */     this.legionsByName.clear();
/*    */   }
/*    */ }