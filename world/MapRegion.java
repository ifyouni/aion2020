/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.configs.administration.DeveloperConfig;
/*     */ import com.aionemu.gameserver.configs.main.SiegeConfig;
/*     */ import com.aionemu.gameserver.configs.main.WorldConfig;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.StaticDoor;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.base.BaseNpc;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.world.zone.ZoneInstance;
/*     */ import com.aionemu.gameserver.world.zone.ZoneName;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.FastMap.Entry;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class MapRegion
/*     */ {
/*  46 */   private static final Logger log = LoggerFactory.getLogger(MapRegion.class);
/*     */   private final int regionId;
/*     */   private final WorldMapInstance parent;
/*  59 */   private volatile MapRegion[] neighbours = new MapRegion[0];
/*     */ 
/*  63 */   private final FastMap<Integer, VisibleObject> objects = new FastMap().shared();
/*     */ 
/*  65 */   private final AtomicInteger playerCount = new AtomicInteger(0);
/*     */ 
/*  67 */   private final AtomicBoolean regionActive = new AtomicBoolean(false);
/*     */   private final int zoneCount;
/*     */   private FastMap<Integer, TreeSet<ZoneInstance>> zoneMap;
/*     */ 
/*     */   MapRegion(int id, WorldMapInstance parent, ZoneInstance[] zones)
/*     */   {
/*  83 */     this.regionId = id;
/*  84 */     this.parent = parent;
/*  85 */     this.zoneCount = zones.length;
/*  86 */     createZoneMap(zones);
/*  87 */     addNeighbourRegion(this);
/*     */   }
/*     */ 
/*     */   public Integer getMapId()
/*     */   {
/*  96 */     return getParent().getMapId();
/*     */   }
/*     */ 
/*     */   public World getWorld()
/*     */   {
/* 103 */     return getParent().getWorld();
/*     */   }
/*     */ 
/*     */   public int getRegionId()
/*     */   {
/* 112 */     return this.regionId;
/*     */   }
/*     */ 
/*     */   public WorldMapInstance getParent()
/*     */   {
/* 121 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public FastMap<Integer, VisibleObject> getObjects()
/*     */   {
/* 130 */     return this.objects;
/*     */   }
/*     */ 
/*     */   public Map<Integer, StaticDoor> getDoors() {
/* 134 */     Map doors = new HashMap();
/* 135 */     for (VisibleObject obj : this.objects.values()) {
/* 136 */       if (obj instanceof StaticDoor) {
/* 137 */         StaticDoor door = (StaticDoor)obj;
/* 138 */         doors.put(Integer.valueOf(door.getSpawn().getEntityId()), door);
/*     */       }
/*     */     }
/* 141 */     return doors;
/*     */   }
/*     */ 
/*     */   public MapRegion[] getNeighbours()
/*     */   {
/* 148 */     return this.neighbours;
/*     */   }
/*     */ 
/*     */   void addNeighbourRegion(MapRegion neighbour)
/*     */   {
/* 157 */     this.neighbours = ((MapRegion[])(MapRegion[])ArrayUtils.add(this.neighbours, neighbour));
/*     */   }
/*     */ 
/*     */   void add(VisibleObject object)
/*     */   {
/* 166 */     if (this.objects.put(object.getObjectId(), object) == null)
/* 167 */       if (object instanceof Player) {
/* 168 */         checkActiveness(this.playerCount.incrementAndGet() > 0);
/*     */       }
/* 170 */       else if (DeveloperConfig.SPAWN_CHECK) {
/* 171 */         Iterator zoneIter = this.zoneMap.values().iterator();
/* 172 */         while (zoneIter.hasNext()) {
/* 173 */           TreeSet zones = (TreeSet)zoneIter.next();
/* 174 */           for (ZoneInstance zone : zones) {
/* 175 */             if (!(zone.isInsideCordinate(object.getX(), object.getY(), object.getZ())))
/*     */               continue;
/* 177 */             if (zone.getZoneTemplate().getZoneType() != ZoneClassName.DUMMY)
/* 178 */               return;
/*     */           }
/*     */         }
/* 181 */         log.warn("Outside any zones: id=" + object + " > X:" + object.getX() + ",Y:" + object.getY() + ",Z:" + object.getZ());
/*     */       }
/*     */   }
/*     */ 
/*     */   void remove(VisibleObject object)
/*     */   {
/* 192 */     if ((this.objects.remove(object.getObjectId()) == null) || 
/* 193 */       (!(object instanceof Player))) return;
/* 194 */     checkActiveness(this.playerCount.decrementAndGet() > 0);
/*     */   }
/*     */ 
/*     */   final void checkActiveness(boolean active)
/*     */   {
/* 199 */     if ((active) && (this.regionActive.compareAndSet(false, true))) {
/* 200 */       startActivation();
/*     */     }
/* 202 */     else if (!(active))
/* 203 */       startDeactivation();
/*     */   }
/*     */ 
/*     */   final void startActivation()
/*     */   {
/* 208 */     ThreadPoolManager.getInstance().schedule(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 212 */         MapRegion.log.debug("Activating in map {} region {}", MapRegion.this.getMapId(), Integer.valueOf(MapRegion.this.regionId));
/* 213 */         MapRegion.this.activateObjects();
/* 214 */         for (MapRegion neighbor : MapRegion.this.getNeighbours())
/* 215 */           neighbor.activate();
/*     */       }
/*     */     }
/*     */     , 1000L);
/*     */   }
/*     */ 
/*     */   final void startDeactivation()
/*     */   {
/* 222 */     ThreadPoolManager.getInstance().schedule(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 226 */         MapRegion.log.debug("Deactivating in map {} region {}", MapRegion.this.getMapId(), Integer.valueOf(MapRegion.this.regionId));
/* 227 */         for (MapRegion neighbor : MapRegion.this.getNeighbours())
/* 228 */           if (!(neighbor.isNeighboursActive()))
/* 229 */             neighbor.deactivate();
/*     */       }
/*     */     }
/*     */     , 60000L);
/*     */   }
/*     */ 
/*     */   public void activate()
/*     */   {
/* 237 */     if (this.regionActive.compareAndSet(false, true))
/* 238 */       activateObjects();
/*     */   }
/*     */ 
/*     */   private final void activateObjects()
/*     */   {
/* 246 */     for (VisibleObject visObject : this.objects.values())
/* 247 */       if (visObject instanceof Creature) {
/* 248 */         Creature creature = (Creature)visObject;
/* 249 */         creature.getAi2().onGeneralEvent(AIEventType.ACTIVATE);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void deactivate()
/*     */   {
/* 255 */     if (this.regionActive.compareAndSet(true, false))
/* 256 */       deactivateObjects();
/*     */   }
/*     */ 
/*     */   private void deactivateObjects()
/*     */   {
/* 264 */     for (VisibleObject visObject : this.objects.values())
/* 265 */       if ((visObject instanceof Creature) && (((!(SiegeConfig.BALAUR_AUTO_ASSAULT)) || (!(visObject instanceof SiegeNpc)))) && (visObject instanceof BaseNpc)) {
/* 266 */         Creature creature = (Creature)visObject;
/* 267 */         creature.getAi2().onGeneralEvent(AIEventType.DEACTIVATE);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isMapRegionActive()
/*     */   {
/* 273 */     return ((!(WorldConfig.WORLD_ACTIVE_TRACE)) || (this.regionActive.get()));
/*     */   }
/*     */ 
/*     */   boolean isNeighboursActive() {
/* 277 */     for (int i = 0; i < this.neighbours.length; ++i) {
/* 278 */       MapRegion r = this.neighbours[i];
/* 279 */       if ((r != null) && (r.regionActive.get()) && (r.playerCount.get() > 0))
/* 280 */         return true;
/*     */     }
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   public void revalidateZones(Creature creature)
/*     */   {
/*     */     boolean foundZone;
/*     */     int category;
/* 286 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; ) {
/* 287 */       foundZone = false;
/* 288 */       category = ((Integer)e.getKey()).intValue();
/* 289 */       TreeSet zones = (TreeSet)e.getValue();
/* 290 */       for (ZoneInstance zone : zones) {
/* 291 */         if ((!(creature.isSpawned())) || ((category != -1) && (foundZone))) {
/* 292 */           zone.onLeave(creature);
/*     */         }
/*     */ 
/* 295 */         boolean result = zone.revalidate(creature);
/* 296 */         if (!(result)) {
/* 297 */           zone.onLeave(creature);
/*     */         }
/*     */ 
/* 300 */         if (category != -1) {
/* 301 */           foundZone = true;
/*     */         }
/* 303 */         zone.onEnter(creature);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<ZoneInstance> getZones(Creature creature) {
/* 309 */     List z = new ArrayList();
/* 310 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; ) {
/* 311 */       TreeSet zones = (TreeSet)e.getValue();
/* 312 */       for (ZoneInstance zone : zones) {
/* 313 */         if (zone.isInsideCreature(creature)) {
/* 314 */           z.add(zone);
/*     */         }
/*     */       }
/*     */     }
/* 318 */     return z;
/*     */   }
/*     */ 
/*     */   public boolean onDie(Creature attacker, Creature target) {
/* 322 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; ) {
/* 323 */       TreeSet zones = (TreeSet)e.getValue();
/* 324 */       for (ZoneInstance zone : zones) {
/* 325 */         if ((zone.isInsideCreature(target)) && 
/* 326 */           (zone.onDie(attacker, target))) {
/* 327 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInsideZone(ZoneName zoneName, float x, float y, float z) {
/* 335 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; )
/*     */     {
/*     */       ZoneInstance zone;
/* 336 */       TreeSet zones = (TreeSet)e.getValue();
/* 337 */       Iterator i$ = zones.iterator();
/*     */       do { if (!(i$.hasNext())) break label95; zone = (ZoneInstance)i$.next(); }
/* 338 */       while (zone.getZoneTemplate().getName() != zoneName);
/*     */ 
/* 340 */       return zone.isInsideCordinate(x, y, z);
/*     */     }
/*     */ 
/* 343 */     label95: return false;
/*     */   }
/*     */ 
/*     */   public boolean isInsideZone(ZoneName zoneName, Creature creature) {
/* 347 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; )
/*     */     {
/*     */       ZoneInstance zone;
/* 348 */       TreeSet zones = (TreeSet)e.getValue();
/* 349 */       Iterator i$ = zones.iterator();
/*     */       do { if (!(i$.hasNext())) break label88; zone = (ZoneInstance)i$.next(); }
/* 350 */       while (zone.getZoneTemplate().getName() != zoneName);
/*     */ 
/* 352 */       return zone.isInsideCreature(creature);
/*     */     }
/*     */ 
/* 355 */     label88: return false;
/*     */   }
/*     */ 
/*     */   public boolean isInsideItemUseZone(ZoneName zoneName, Creature creature)
/*     */   {
/* 366 */     FastMap.Entry e = this.zoneMap.head(); for (FastMap.Entry mapEnd = this.zoneMap.tail(); (e = e.getNext()) != mapEnd; )
/*     */     {
/*     */       ZoneInstance zone;
/* 367 */       TreeSet zones = (TreeSet)e.getValue();
/* 368 */       Iterator i$ = zones.iterator();
/*     */       do do { if (!(i$.hasNext())) break label101; zone = (ZoneInstance)i$.next(); }
/* 369 */         while (!(zone.getZoneTemplate().getXmlName().startsWith(zoneName.toString())));
/*     */ 
/* 371 */       while (!(zone.isInsideCreature(creature)));
/*     */ 
/* 373 */       return true;
/*     */     }
/*     */ 
/* 376 */     label101: return false;
/*     */   }
/*     */ 
/*     */   private void createZoneMap(ZoneInstance[] zones) {
/* 380 */     this.zoneMap = new FastMap();
/* 381 */     for (int i = 0; i < zones.length; ++i) {
/* 382 */       ZoneInstance zone = zones[i];
/* 383 */       int category = -1;
/* 384 */       if (zone.getZoneTemplate().getPriority() != 0) {
/* 385 */         category = zone.getZoneTemplate().getZoneType().ordinal();
/*     */       }
/* 387 */       TreeSet zoneCategory = (TreeSet)this.zoneMap.get(Integer.valueOf(category));
/* 388 */       if (zoneCategory == null) {
/* 389 */         zoneCategory = new TreeSet();
/* 390 */         this.zoneMap.put(Integer.valueOf(category), zoneCategory);
/*     */       }
/* 392 */       zoneCategory.add(zone);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getZoneCount() {
/* 397 */     return this.zoneCount;
/*     */   }
/*     */ }