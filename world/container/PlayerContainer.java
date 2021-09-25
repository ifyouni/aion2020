/*     */ package com.aionemu.gameserver.world.container;
/*     */ 
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.FastMap.Entry;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class PlayerContainer
/*     */   implements Iterable<Player>
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(PlayerContainer.class);
/*     */   private final FastMap<Integer, Player> playersById;
/*     */   private final FastMap<String, Player> playersByName;
/*     */ 
/*     */   public PlayerContainer()
/*     */   {
/*  41 */     this.playersById = new FastMap().shared();
/*     */ 
/*  45 */     this.playersByName = new FastMap().shared();
/*     */   }
/*     */ 
/*     */   public void add(Player player)
/*     */   {
/*  53 */     if (this.playersById.put(player.getObjectId(), player) != null)
/*  54 */       throw new DuplicateAionObjectException();
/*  55 */     if (this.playersByName.put(player.getName(), player) != null)
/*  56 */       throw new DuplicateAionObjectException();
/*     */   }
/*     */ 
/*     */   public void remove(Player player)
/*     */   {
/*  65 */     this.playersById.remove(player.getObjectId());
/*  66 */     this.playersByName.remove(player.getName());
/*     */   }
/*     */ 
/*     */   public Player get(int objectId)
/*     */   {
/*  77 */     return ((Player)this.playersById.get(Integer.valueOf(objectId)));
/*     */   }
/*     */ 
/*     */   public Player get(String name)
/*     */   {
/*  88 */     return ((Player)this.playersByName.get(name));
/*     */   }
/*     */ 
/*     */   public Iterator<Player> iterator()
/*     */   {
/*  93 */     return this.playersById.values().iterator();
/*     */   }
/*     */ 
/*     */   public void doOnAllPlayers(Visitor<Player> visitor)
/*     */   {
/*     */     FastMap.Entry e;
/*     */     try
/*     */     {
/* 102 */       e = this.playersById.head(); for (FastMap.Entry mapEnd = this.playersById.tail(); (e = e.getNext()) != mapEnd; ) {
/* 103 */         Player player = (Player)e.getValue();
/* 104 */         if (player != null)
/* 105 */           visitor.visit(player);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 110 */       log.error("Exception when running visitor on all players" + ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<Player> getAllPlayers() {
/* 115 */     return this.playersById.values();
/*     */   }
/*     */ }