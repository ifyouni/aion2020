/*     */ package com.aionemu.gameserver.world.container;
/*     */ 
/*     */ import com.aionemu.gameserver.model.team.legion.LegionMember;
/*     */ import com.aionemu.gameserver.model.team.legion.LegionMemberEx;
/*     */ import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
/*     */ import javolution.util.FastMap;
/*     */ 
/*     */ public class LegionMemberContainer
/*     */ {
/*     */   private final FastMap<Integer, LegionMember> legionMemberById;
/*     */   private final FastMap<Integer, LegionMemberEx> legionMemberExById;
/*     */   private final FastMap<String, LegionMemberEx> legionMemberExByName;
/*     */ 
/*     */   public LegionMemberContainer()
/*     */   {
/*  27 */     this.legionMemberById = new FastMap().shared();
/*     */ 
/*  29 */     this.legionMemberExById = new FastMap().shared();
/*  30 */     this.legionMemberExByName = new FastMap().shared();
/*     */   }
/*     */ 
/*     */   public void addMember(LegionMember legionMember)
/*     */   {
/*  38 */     if (!(this.legionMemberById.containsKey(Integer.valueOf(legionMember.getObjectId()))))
/*  39 */       this.legionMemberById.put(Integer.valueOf(legionMember.getObjectId()), legionMember);
/*     */   }
/*     */ 
/*     */   public LegionMember getMember(int memberObjId)
/*     */   {
/*  48 */     return ((LegionMember)this.legionMemberById.get(Integer.valueOf(memberObjId)));
/*     */   }
/*     */ 
/*     */   public void addMemberEx(LegionMemberEx legionMember)
/*     */   {
/*  57 */     if ((this.legionMemberExById.containsKey(Integer.valueOf(legionMember.getObjectId()))) || (this.legionMemberExByName.containsKey(legionMember.getName())))
/*     */     {
/*  59 */       throw new DuplicateAionObjectException(); }
/*  60 */     this.legionMemberExById.put(Integer.valueOf(legionMember.getObjectId()), legionMember);
/*  61 */     this.legionMemberExByName.put(legionMember.getName(), legionMember);
/*     */   }
/*     */ 
/*     */   public LegionMemberEx getMemberEx(int memberObjId)
/*     */   {
/*  70 */     return ((LegionMemberEx)this.legionMemberExById.get(Integer.valueOf(memberObjId)));
/*     */   }
/*     */ 
/*     */   public LegionMemberEx getMemberEx(String memberName)
/*     */   {
/*  79 */     return ((LegionMemberEx)this.legionMemberExByName.get(memberName));
/*     */   }
/*     */ 
/*     */   public void remove(LegionMemberEx legionMember)
/*     */   {
/*  88 */     this.legionMemberById.remove(Integer.valueOf(legionMember.getObjectId()));
/*  89 */     this.legionMemberExById.remove(Integer.valueOf(legionMember.getObjectId()));
/*  90 */     this.legionMemberExByName.remove(legionMember.getName());
/*     */   }
/*     */ 
/*     */   public boolean contains(int memberObjId)
/*     */   {
/* 100 */     return this.legionMemberById.containsKey(Integer.valueOf(memberObjId));
/*     */   }
/*     */ 
/*     */   public boolean containsEx(int memberObjId)
/*     */   {
/* 110 */     return this.legionMemberExById.containsKey(Integer.valueOf(memberObjId));
/*     */   }
/*     */ 
/*     */   public boolean containsEx(String memberName)
/*     */   {
/* 120 */     return this.legionMemberExByName.containsKey(memberName);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 124 */     this.legionMemberById.clear();
/* 125 */     this.legionMemberExById.clear();
/* 126 */     this.legionMemberExByName.clear();
/*     */   }
/*     */ }