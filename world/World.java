/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import com.aionemu.commons.utils.GenericValidator;
/*     */ import com.aionemu.gameserver.controllers.VisibleObjectController;
/*     */ import com.aionemu.gameserver.controllers.movement.MoveController;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.PlayerInitialData;
/*     */ import com.aionemu.gameserver.dataholders.PlayerInitialData.LocationData;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.base.BaseNpc;
/*     */ import com.aionemu.gameserver.model.gameobjects.outpost.OutpostNpc;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
/*     */ import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
/*     */ import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
/*     */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*     */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*     */ import com.aionemu.gameserver.world.container.PlayerContainer;
/*     */ import com.aionemu.gameserver.world.exceptions.AlreadySpawnedException;
/*     */ import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
/*     */ import com.aionemu.gameserver.world.exceptions.WorldMapNotExistException;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import javolution.util.FastList;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.FastMap.Entry;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class World
/*     */ {
/*  46 */   private static final Logger log = LoggerFactory.getLogger(World.class);
/*     */   private final PlayerContainer allPlayers;
/*     */   private final FastMap<Integer, VisibleObject> allObjects;
/*  49 */   private final TIntObjectHashMap<Collection<SiegeNpc>> localSiegeNpcs = new TIntObjectHashMap();
/*  50 */   private final TIntObjectHashMap<Collection<BaseNpc>> localBaseNpcs = new TIntObjectHashMap();
/*  51 */   private final TIntObjectHashMap<Collection<OutpostNpc>> localOutpostNpcs = new TIntObjectHashMap();
/*     */   private final FastMap<Integer, Npc> allNpcs;
/*     */   private final TIntObjectHashMap<WorldMap> worldMaps;
/*     */ 
/*     */   private World()
/*     */   {
/*  60 */     this.allPlayers = new PlayerContainer();
/*  61 */     this.allObjects = new FastMap().shared();
/*  62 */     this.allNpcs = new FastMap().shared();
/*  63 */     this.worldMaps = new TIntObjectHashMap();
/*  64 */     for (WorldMapTemplate template : DataManager.WORLD_MAPS_DATA) {
/*  65 */       this.worldMaps.put(template.getMapId().intValue(), new WorldMap(template, this));
/*     */     }
/*  67 */     log.info("加载: " + this.worldMaps.size() + " 个地图场景.");
/*     */   }
/*     */ 
/*     */   public static World getInstance() {
/*  71 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   public void storeObject(VisibleObject object)
/*     */   {
/*     */     Collection npcs;
/*  78 */     if (object.getPosition() == null) {
/*  79 */       log.warn("Not putting object with null position!!! " + object.getObjectTemplate().getTemplateId());
/*  80 */       return; }
/*  81 */     if (this.allObjects.put(object.getObjectId(), object) != null)
/*  82 */       throw new DuplicateAionObjectException();
/*  83 */     if (object instanceof Player)
/*  84 */       this.allPlayers.add((Player)object);
/*  85 */     if (object instanceof SiegeNpc) {
/*  86 */       SiegeNpc siegeNpc = (SiegeNpc)object;
/*  87 */       npcs = (Collection)this.localSiegeNpcs.get(siegeNpc.getSiegeId());
/*  88 */       if (npcs == null) {
/*  89 */         synchronized (this.localSiegeNpcs) {
/*  90 */           if (this.localSiegeNpcs.containsKey(siegeNpc.getSiegeId())) {
/*  91 */             npcs = (Collection)this.localSiegeNpcs.get(siegeNpc.getSiegeId());
/*     */           } else {
/*  93 */             npcs = new FastList().shared();
/*  94 */             this.localSiegeNpcs.put(siegeNpc.getSiegeId(), npcs);
/*     */           }
/*     */         }
/*     */       }
/*  98 */       npcs.add(siegeNpc);
/*  99 */     } else if (object instanceof BaseNpc) {
/* 100 */       BaseNpc baseNpc = (BaseNpc)object;
/* 101 */       npcs = (Collection)this.localBaseNpcs.get(baseNpc.getBaseId());
/* 102 */       if (npcs == null) {
/* 103 */         synchronized (this.localBaseNpcs) {
/* 104 */           if (this.localBaseNpcs.containsKey(baseNpc.getBaseId())) {
/* 105 */             npcs = (Collection)this.localBaseNpcs.get(baseNpc.getBaseId());
/*     */           } else {
/* 107 */             npcs = new FastList().shared();
/* 108 */             this.localBaseNpcs.put(baseNpc.getBaseId(), npcs);
/*     */           }
/*     */         }
/*     */       }
/* 112 */       npcs.add(baseNpc);
/* 113 */     } else if (object instanceof OutpostNpc) {
/* 114 */       OutpostNpc outpostNpc = (OutpostNpc)object;
/* 115 */       npcs = (Collection)this.localOutpostNpcs.get(outpostNpc.getOutpostId());
/* 116 */       if (npcs == null) {
/* 117 */         synchronized (this.localOutpostNpcs) {
/* 118 */           if (this.localOutpostNpcs.containsKey(outpostNpc.getOutpostId())) {
/* 119 */             npcs = (Collection)this.localOutpostNpcs.get(outpostNpc.getOutpostId());
/*     */           } else {
/* 121 */             npcs = new FastList().shared();
/* 122 */             this.localOutpostNpcs.put(outpostNpc.getOutpostId(), npcs);
/*     */           }
/*     */         }
/*     */       }
/* 126 */       npcs.add(outpostNpc); }
/* 127 */     if (object instanceof Npc)
/* 128 */       this.allNpcs.put(object.getObjectId(), (Npc)object);
/*     */   }
/*     */ 
/*     */   public void removeObject(VisibleObject object)
/*     */   {
/*     */     Collection locSpawn;
/* 137 */     this.allObjects.remove(object.getObjectId());
/* 138 */     if (object instanceof SiegeNpc) {
/* 139 */       SiegeNpc siegeNpc = (SiegeNpc)object;
/* 140 */       locSpawn = (Collection)this.localSiegeNpcs.get(siegeNpc.getSiegeId());
/* 141 */       if (!(GenericValidator.isBlankOrNull(locSpawn)))
/* 142 */         locSpawn.remove(siegeNpc);
/*     */     }
/* 144 */     else if (object instanceof BaseNpc) {
/* 145 */       BaseNpc baseNpc = (BaseNpc)object;
/* 146 */       locSpawn = (Collection)this.localBaseNpcs.get(baseNpc.getBaseId());
/* 147 */       if (!(GenericValidator.isBlankOrNull(locSpawn)))
/* 148 */         locSpawn.remove(baseNpc);
/*     */     }
/* 150 */     else if (object instanceof OutpostNpc) {
/* 151 */       OutpostNpc outpostNpc = (OutpostNpc)object;
/* 152 */       locSpawn = (Collection)this.localOutpostNpcs.get(outpostNpc.getOutpostId());
/* 153 */       if (!(GenericValidator.isBlankOrNull(locSpawn)))
/* 154 */         locSpawn.remove(outpostNpc);
/*     */     }
/* 156 */     if (object instanceof Npc)
/* 157 */       this.allNpcs.remove(object.getObjectId());
/* 158 */     if (object instanceof Player)
/* 159 */       this.allPlayers.remove((Player)object);
/*     */   }
/*     */ 
/*     */   public Iterator<Player> getPlayersIterator()
/*     */   {
/* 167 */     return this.allPlayers.iterator();
/*     */   }
/*     */ 
/*     */   public Collection<SiegeNpc> getLocalSiegeNpcs(int locationId) {
/* 171 */     Collection result = (Collection)this.localSiegeNpcs.get(locationId);
/* 172 */     return ((result != null) ? result : Collections.emptySet());
/*     */   }
/*     */ 
/*     */   public Collection<BaseNpc> getLocalBaseNpcs(int locationId) {
/* 176 */     Collection result = (Collection)this.localBaseNpcs.get(locationId);
/* 177 */     return ((result != null) ? result : Collections.emptySet());
/*     */   }
/*     */ 
/*     */   public Collection<OutpostNpc> getLocalOutpostNpcs(int locationId) {
/* 181 */     Collection result = (Collection)this.localOutpostNpcs.get(locationId);
/* 182 */     return ((result != null) ? result : Collections.emptySet());
/*     */   }
/*     */ 
/*     */   public Collection<Npc> getNpcs() {
/* 186 */     return this.allNpcs.values();
/*     */   }
/*     */ 
/*     */   public Player findPlayer(String name)
/*     */   {
/* 193 */     return this.allPlayers.get(name);
/*     */   }
/*     */ 
/*     */   public Player findPlayer(int objectId)
/*     */   {
/* 200 */     return this.allPlayers.get(objectId);
/*     */   }
/*     */ 
/*     */   public VisibleObject findVisibleObject(int objectId)
/*     */   {
/* 207 */     return ((VisibleObject)this.allObjects.get(Integer.valueOf(objectId)));
/*     */   }
/*     */ 
/*     */   public boolean isInWorld(VisibleObject object)
/*     */   {
/* 214 */     return this.allObjects.containsKey(object.getObjectId());
/*     */   }
/*     */ 
/*     */   public WorldMap getWorldMap(int id)
/*     */   {
/* 221 */     WorldMap map = (WorldMap)this.worldMaps.get(id);
/* 222 */     if (map == null) {
/* 223 */       throw new WorldMapNotExistException("Map: " + id + " not exist!");
/*     */     }
/* 225 */     return map;
/*     */   }
/*     */ 
/*     */   public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading)
/*     */   {
/* 237 */     updatePosition(object, newX, newY, newZ, newHeading, true);
/*     */   }
/*     */ 
/*     */   public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading, boolean updateKnownList)
/*     */   {
/* 248 */     if (!(object.isSpawned()))
/* 249 */       return;
/* 250 */     MapRegion oldRegion = object.getActiveRegion();
/* 251 */     if (oldRegion == null) {
/* 252 */       log.warn(String.format("CHECKPOINT: oldRegion is null, map - %d, object coordinates - %f %f %f", new Object[] { Integer.valueOf(object.getWorldId()), Float.valueOf(object.getX()), Float.valueOf(object.getY()), Float.valueOf(object.getZ()) }));
/* 253 */       return;
/*     */     }
/* 255 */     MapRegion newRegion = oldRegion.getParent().getRegion(newX, newY, newZ);
/* 256 */     if (newRegion == null) {
/* 257 */       log.warn(String.format("CHECKPOINT: newRegion is null, map - %d, object coordinates - %f %f %f", new Object[] { Integer.valueOf(object.getWorldId()), Float.valueOf(newX), Float.valueOf(newY), Float.valueOf(newZ) }), new Throwable());
/* 258 */       if (object instanceof Creature)
/* 259 */         ((Creature)object).getMoveController().abortMove();
/* 260 */       if (object instanceof Player)
/*     */       {
/*     */         float x;
/*     */         float y;
/*     */         float z;
/*     */         int worldId;
/* 261 */         Player player = (Player)object;
/*     */ 
/* 264 */         byte h = 0;
/* 265 */         if (player.getBindPoint() != null) {
/* 266 */           BindPointPosition bplist = player.getBindPoint();
/* 267 */           worldId = bplist.getMapId();
/* 268 */           x = bplist.getX();
/* 269 */           y = bplist.getY();
/* 270 */           z = bplist.getZ();
/* 271 */           h = bplist.getHeading();
/*     */         } else {
/* 273 */           PlayerInitialData.LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getCommonData().getRace());
/* 274 */           worldId = locationData.getMapId();
/* 275 */           x = locationData.getX();
/* 276 */           y = locationData.getY();
/* 277 */           z = locationData.getZ();
/*     */         }
/* 279 */         setPosition(object, worldId, x, y, z, h);
/*     */       }
/* 281 */       return;
/*     */     }
/* 283 */     object.getPosition().setXYZH(Float.valueOf(newX), Float.valueOf(newY), Float.valueOf(newZ), Byte.valueOf(newHeading));
/* 284 */     if (newRegion != oldRegion) {
/* 285 */       if (object instanceof Creature) {
/* 286 */         oldRegion.revalidateZones((Creature)object);
/* 287 */         newRegion.revalidateZones((Creature)object);
/*     */       }
/* 289 */       oldRegion.remove(object);
/* 290 */       newRegion.add(object);
/* 291 */       object.getPosition().setMapRegion(newRegion); }
/* 292 */     if (updateKnownList)
/* 293 */       object.updateKnownlist();
/*     */   }
/*     */ 
/*     */   public void setPosition(VisibleObject object, int mapId, float x, float y, float z, byte heading)
/*     */   {
/* 307 */     int instanceId = 1;
/* 308 */     if (object.getWorldId() == mapId) {
/* 309 */       instanceId = object.getInstanceId();
/*     */     }
/* 311 */     setPosition(object, mapId, instanceId, x, y, z, heading);
/*     */   }
/*     */ 
/*     */   public void setPosition(VisibleObject object, int mapId, int instance, float x, float y, float z, byte heading)
/*     */   {
/* 324 */     if (object.isSpawned()) {
/* 325 */       despawn(object);
/*     */     }
/* 327 */     WorldMapInstance instanceMap = getWorldMap(mapId).getWorldMapInstanceById(instance);
/* 328 */     if (instanceMap == null) {
/* 329 */       return;
/*     */     }
/* 331 */     object.getPosition().setXYZH(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z), Byte.valueOf(heading));
/* 332 */     object.getPosition().setMapId(mapId);
/* 333 */     MapRegion region = instanceMap.getRegion(object);
/* 334 */     object.getPosition().setMapRegion(region);
/*     */   }
/*     */ 
/*     */   public WorldPosition createPosition(int mapId, float x, float y, float z, byte heading, int instanceId)
/*     */   {
/* 348 */     WorldPosition position = new WorldPosition(mapId);
/* 349 */     position.setXYZH(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z), Byte.valueOf(heading));
/* 350 */     position.setMapId(mapId);
/* 351 */     position.setMapRegion(getWorldMap(mapId).getWorldMapInstanceById(instanceId).getRegion(x, y, z));
/* 352 */     return position;
/*     */   }
/*     */ 
/*     */   public void preSpawn(VisibleObject object) {
/* 356 */     ((Player)object).setState(CreatureState.ACTIVE);
/* 357 */     object.getPosition().setIsSpawned(true);
/* 358 */     object.getActiveRegion().getParent().addObject(object);
/* 359 */     object.getActiveRegion().add(object);
/* 360 */     object.getController().onAfterSpawn();
/*     */   }
/*     */ 
/*     */   public void spawn(VisibleObject object)
/*     */   {
/* 367 */     if (object.getPosition().isSpawned()) {
/* 368 */       throw new AlreadySpawnedException();
/*     */     }
/* 370 */     object.getController().onBeforeSpawn();
/* 371 */     object.getPosition().setIsSpawned(true);
/* 372 */     object.getActiveRegion().getParent().addObject(object);
/* 373 */     object.getActiveRegion().add(object);
/* 374 */     object.getController().onAfterSpawn();
/* 375 */     object.updateKnownlist();
/*     */   }
/*     */ 
/*     */   public void despawn(VisibleObject object)
/*     */   {
/* 382 */     despawn(object, true);
/*     */   }
/*     */ 
/*     */   public void despawn(VisibleObject object, boolean clearKnownlist) {
/* 386 */     MapRegion oldMapRegion = object.getActiveRegion();
/* 387 */     if (object.getActiveRegion() != null) {
/* 388 */       if (object.getActiveRegion().getParent() != null) {
/* 389 */         object.getActiveRegion().getParent().removeObject(object);
/*     */       }
/* 391 */       object.getActiveRegion().remove(object);
/*     */     }
/* 393 */     object.getPosition().setIsSpawned(false);
/* 394 */     if ((oldMapRegion != null) && (object instanceof Creature))
/* 395 */       oldMapRegion.revalidateZones((Creature)object);
/* 396 */     if (clearKnownlist)
/* 397 */       object.clearKnownlist();
/*     */   }
/*     */ 
/*     */   public Collection<Player> getAllPlayers()
/*     */   {
/* 405 */     return this.allPlayers.getAllPlayers();
/*     */   }
/*     */ 
/*     */   public void doOnAllPlayers(Visitor<Player> visitor)
/*     */   {
/* 412 */     this.allPlayers.doOnAllPlayers(visitor);
/*     */   }
/*     */ 
/*     */   public void doOnAllObjects(Visitor<VisibleObject> visitor)
/*     */   {
/*     */     FastMap.Entry e;
/*     */     try
/*     */     {
/* 420 */       e = this.allObjects.head(); for (FastMap.Entry mapEnd = this.allObjects.tail(); (e = e.getNext()) != mapEnd; ) {
/* 421 */         VisibleObject object = (VisibleObject)e.getValue();
/* 422 */         if (object != null)
/* 423 */           visitor.visit(object);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 427 */       log.error("Exception when running visitor on all objects", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/* 433 */     protected static final World instance = new World(null);
/*     */   }
/*     */ }