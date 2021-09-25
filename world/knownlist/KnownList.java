/*     */ package com.aionemu.gameserver.world.knownlist;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.SecurityConfig;
/*     */ import com.aionemu.gameserver.controllers.VisibleObjectController;
/*     */ import com.aionemu.gameserver.model.gameobjects.AionObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.world.MapRegion;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.FastMap.Entry;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class KnownList
/*     */ {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(KnownList.class);
/*     */   protected final VisibleObject owner;
/*  52 */   protected final FastMap<Integer, VisibleObject> knownObjects = new FastMap().shared();
/*     */   protected volatile FastMap<Integer, Player> knownPlayers;
/*  62 */   protected final FastMap<Integer, VisibleObject> visualObjects = new FastMap().shared();
/*     */   protected volatile FastMap<Integer, Player> visualPlayers;
/*  69 */   private ReentrantLock lock = new ReentrantLock();
/*     */ 
/*     */   public KnownList(VisibleObject owner)
/*     */   {
/*  75 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public void doUpdate()
/*     */   {
/*  82 */     this.lock.lock();
/*     */     try {
/*  84 */       forgetObjects();
/*  85 */       findVisibleObjects();
/*     */     }
/*     */     finally {
/*  88 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  96 */     for (VisibleObject object : this.knownObjects.values()) {
/*  97 */       object.getKnownList().del(this.owner, false);
/*     */     }
/*  99 */     this.knownObjects.clear();
/* 100 */     if (this.knownPlayers != null) {
/* 101 */       this.knownPlayers.clear();
/*     */     }
/* 103 */     this.visualObjects.clear();
/* 104 */     if (this.visualPlayers != null)
/* 105 */       this.visualPlayers.clear();
/*     */   }
/*     */ 
/*     */   public boolean knowns(AionObject object)
/*     */   {
/* 116 */     return this.knownObjects.containsKey(object.getObjectId());
/*     */   }
/*     */ 
/*     */   protected boolean add(VisibleObject object)
/*     */   {
/* 125 */     if (!(isAwareOf(object))) {
/* 126 */       return false;
/*     */     }
/* 128 */     if (this.knownObjects.put(object.getObjectId(), object) == null) {
/* 129 */       if (object instanceof Player) {
/* 130 */         checkKnownPlayersInitialized();
/* 131 */         this.knownPlayers.put(object.getObjectId(), (Player)object);
/*     */       }
/*     */ 
/* 134 */       addVisualObject(object);
/* 135 */       return true;
/*     */     }
/*     */ 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public void addVisualObject(VisibleObject object) {
/* 142 */     if (object instanceof Creature) {
/* 143 */       if ((SecurityConfig.INVIS) && (object instanceof Player) && 
/* 144 */         (!(this.owner.canSee((Player)object)))) {
/* 145 */         return;
/*     */       }
/*     */ 
/* 149 */       if (this.visualObjects.put(object.getObjectId(), object) == null) {
/* 150 */         if (object instanceof Player) {
/* 151 */           checkVisiblePlayersInitialized();
/* 152 */           this.visualPlayers.put(object.getObjectId(), (Player)object);
/*     */         }
/* 154 */         this.owner.getController().see(object);
/*     */       }
/*     */     }
/* 157 */     else if (this.visualObjects.put(object.getObjectId(), object) == null) {
/* 158 */       this.owner.getController().see(object);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void del(VisibleObject object, boolean isOutOfRange)
/*     */   {
/* 171 */     if (this.knownObjects.remove(object.getObjectId()) != null) {
/* 172 */       if (this.knownPlayers != null)
/* 173 */         this.knownPlayers.remove(object.getObjectId());
/* 174 */       delVisualObject(object, isOutOfRange);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delVisualObject(VisibleObject object, boolean isOutOfRange) {
/* 179 */     if (this.visualObjects.remove(object.getObjectId()) != null) {
/* 180 */       if (this.visualPlayers != null)
/* 181 */         this.visualPlayers.remove(object.getObjectId());
/* 182 */       this.owner.getController().notSee(object, isOutOfRange);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void forgetObjects()
/*     */   {
/* 190 */     for (VisibleObject object : this.knownObjects.values())
/* 191 */       if ((!(checkObjectInRange(object))) && (!(object.getKnownList().checkReversedObjectInRange(this.owner)))) {
/* 192 */         del(object, true);
/* 193 */         object.getKnownList().del(this.owner, true);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void findVisibleObjects()
/*     */   {
/*     */     FastMap.Entry e;
/* 202 */     if ((this.owner == null) || (!(this.owner.isSpawned()))) {
/* 203 */       return;
/*     */     }
/* 205 */     MapRegion[] regions = this.owner.getActiveRegion().getNeighbours();
/* 206 */     for (int i = 0; i < regions.length; ++i) {
/* 207 */       MapRegion r = regions[i];
/* 208 */       FastMap objects = r.getObjects();
/* 209 */       e = objects.head(); for (FastMap.Entry mapEnd = objects.tail(); (e = e.getNext()) != mapEnd; ) {
/* 210 */         VisibleObject newObject = (VisibleObject)e.getValue();
/* 211 */         if (newObject == this.owner) continue; if (newObject == null) {
/*     */           continue;
/*     */         }
/* 214 */         if (!(isAwareOf(newObject))) {
/*     */           continue;
/*     */         }
/* 217 */         if (this.knownObjects.containsKey(newObject.getObjectId())) {
/*     */           continue;
/*     */         }
/* 220 */         if ((!(checkObjectInRange(newObject))) && (!(newObject.getKnownList().checkReversedObjectInRange(this.owner))))
/*     */         {
/*     */           continue;
/*     */         }
/*     */ 
/* 226 */         if (add(newObject))
/* 227 */           newObject.getKnownList().add(this.owner);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isAwareOf(VisibleObject newObject)
/*     */   {
/* 241 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean checkObjectInRange(VisibleObject newObject)
/*     */   {
/* 246 */     if (Math.abs(this.owner.getZ() - newObject.getZ()) > this.owner.getMaxZVisibleDistance()) {
/* 247 */       return false;
/*     */     }
/* 249 */     return MathUtil.isInRange(this.owner, newObject, this.owner.getVisibilityDistance());
/*     */   }
/*     */ 
/*     */   protected boolean checkReversedObjectInRange(VisibleObject newObject)
/*     */   {
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   public void doOnAllNpcs(Visitor<Npc> visitor) {
/* 264 */     doOnAllNpcs(visitor, 2147483647);
/*     */   }
/*     */ 
/*     */   public int doOnAllNpcs(Visitor<Npc> visitor, int iterationLimit)
/*     */   {
/*     */     FastMap.Entry e;
/* 268 */     int counter = 0;
/*     */     try {
/* 270 */       e = this.knownObjects.head(); for (FastMap.Entry mapEnd = this.knownObjects.tail(); (e = e.getNext()) != mapEnd; ) {
/* 271 */         VisibleObject newObject = (VisibleObject)e.getValue();
/* 272 */         if (newObject instanceof Npc) {
/* 273 */           if (++counter == iterationLimit)
/*     */             break;
/* 275 */           visitor.visit((Npc)newObject);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 280 */       log.error("Exception when running visitor on all npcs" + ex);
/*     */     }
/* 282 */     return counter;
/*     */   }
/*     */ 
/*     */   public void doOnAllNpcsWithOwner(VisitorWithOwner<Npc, VisibleObject> visitor) {
/* 286 */     doOnAllNpcsWithOwner(visitor, 2147483647);
/*     */   }
/*     */ 
/*     */   public int doOnAllNpcsWithOwner(VisitorWithOwner<Npc, VisibleObject> visitor, int iterationLimit)
/*     */   {
/*     */     FastMap.Entry e;
/* 290 */     int counter = 0;
/*     */     try {
/* 292 */       e = this.knownObjects.head(); for (FastMap.Entry mapEnd = this.knownObjects.tail(); (e = e.getNext()) != mapEnd; ) {
/* 293 */         VisibleObject newObject = (VisibleObject)e.getValue();
/* 294 */         if (newObject instanceof Npc) {
/* 295 */           if (++counter == iterationLimit)
/*     */             break;
/* 297 */           visitor.visit((Npc)newObject, this.owner);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 302 */       log.error("Exception when running visitor on all npcs" + ex);
/*     */     }
/* 304 */     return counter;
/*     */   }
/*     */ 
/*     */   public void doOnAllPlayers(Visitor<Player> visitor)
/*     */   {
/*     */     FastMap.Entry e;
/* 308 */     if (this.knownPlayers == null)
/* 309 */       return;
/*     */     try
/*     */     {
/* 312 */       e = this.knownPlayers.head(); for (FastMap.Entry mapEnd = this.knownPlayers.tail(); (e = e.getNext()) != mapEnd; ) {
/* 313 */         Player player = (Player)e.getValue();
/* 314 */         if (player != null)
/* 315 */           visitor.visit(player);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 320 */       log.error("Exception when running visitor on all players" + ex); }
/*     */   }
/*     */ 
/*     */   public void doOnAllObjects(Visitor<VisibleObject> visitor) {
/*     */     FastMap.Entry e;
/*     */     try {
/* 326 */       e = this.knownObjects.head(); for (FastMap.Entry mapEnd = this.knownObjects.tail(); (e = e.getNext()) != mapEnd; ) {
/* 327 */         VisibleObject newObject = (VisibleObject)e.getValue();
/* 328 */         if (newObject != null)
/* 329 */           visitor.visit(newObject);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 334 */       log.error("Exception when running visitor on all objects" + ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Map<Integer, VisibleObject> getKnownObjects() {
/* 339 */     return this.knownObjects;
/*     */   }
/*     */ 
/*     */   public Map<Integer, VisibleObject> getVisibleObjects() {
/* 343 */     return this.visualObjects;
/*     */   }
/*     */ 
/*     */   public Map<Integer, Player> getKnownPlayers() {
/* 347 */     return ((this.knownPlayers != null) ? this.knownPlayers : Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public Map<Integer, Player> getVisiblePlayers() {
/* 351 */     return ((this.visualPlayers != null) ? this.visualPlayers : Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   final void checkKnownPlayersInitialized() {
/* 355 */     if (this.knownPlayers == null)
/* 356 */       synchronized (this) {
/* 357 */         if (this.knownPlayers == null)
/* 358 */           this.knownPlayers = new FastMap().shared();
/*     */       }
/*     */   }
/*     */ 
/*     */   final void checkVisiblePlayersInitialized()
/*     */   {
/* 365 */     if (this.visualPlayers == null)
/* 366 */       synchronized (this) {
/* 367 */         if (this.visualPlayers == null)
/* 368 */           this.visualPlayers = new FastMap().shared();
/*     */       }
/*     */   }
/*     */ 
/*     */   public VisibleObject getObject(int targetObjectId)
/*     */   {
/* 375 */     return ((VisibleObject)this.knownObjects.get(Integer.valueOf(targetObjectId)));
/*     */   }
/*     */ }