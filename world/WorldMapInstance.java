/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.WorldConfig;
/*     */ import com.aionemu.gameserver.instance.handlers.InstanceHandler;
/*     */ import com.aionemu.gameserver.model.gameobjects.AionObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.StaticDoor;
/*     */ import com.aionemu.gameserver.model.gameobjects.Trap;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.geometry.Area;
/*     */ import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
/*     */ import com.aionemu.gameserver.model.team2.group.PlayerGroup;
/*     */ import com.aionemu.gameserver.model.team2.league.League;
/*     */ import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
/*     */ import com.aionemu.gameserver.model.templates.quest.QuestNpc;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneType;
/*     */ import com.aionemu.gameserver.questEngine.QuestEngine;
/*     */ import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ import com.aionemu.gameserver.world.zone.RegionZone;
/*     */ import com.aionemu.gameserver.world.zone.ZoneInstance;
/*     */ import com.aionemu.gameserver.world.zone.ZoneName;
/*     */ import com.aionemu.gameserver.world.zone.ZoneService;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import javolution.util.FastList;
/*     */ import javolution.util.FastMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public abstract class WorldMapInstance
/*     */ {
/*  52 */   private static final Logger log = LoggerFactory.getLogger(WorldMapInstance.class);
/*     */ 
/*  56 */   public static final int regionSize = WorldConfig.WORLD_REGION_SIZE;
/*     */   private final WorldMap parent;
/*  64 */   protected final TIntObjectHashMap<MapRegion> regions = new TIntObjectHashMap();
/*     */ 
/*  69 */   private final Map<Integer, VisibleObject> worldMapObjects = new FastMap().shared();
/*     */ 
/*  74 */   private final FastMap<Integer, Player> worldMapPlayers = new FastMap().shared();
/*     */ 
/*  76 */   private final Set<Integer> registeredObjects = Collections.newSetFromMap(new FastMap().shared());
/*     */ 
/*  78 */   private PlayerGroup registeredGroup = null;
/*     */ 
/*  80 */   private Future<?> emptyInstanceTask = null;
/*     */   private int instanceId;
/*  87 */   private final FastList<Integer> questIds = new FastList();
/*     */   private InstanceHandler instanceHandler;
/*  91 */   private Map<ZoneName, ZoneInstance> zones = new HashMap();
/*     */   private Integer soloPlayer;
/*     */   private PlayerAlliance registredAlliance;
/*     */   private League registredLeague;
/*     */ 
/*     */   public WorldMapInstance(WorldMap parent, int instanceId)
/*     */   {
/* 105 */     this.parent = parent;
/* 106 */     this.instanceId = instanceId;
/* 107 */     this.zones = ZoneService.getInstance().getZoneInstancesByWorldId(parent.getMapId().intValue());
/* 108 */     initMapRegions();
/*     */   }
/*     */ 
/*     */   public Integer getMapId()
/*     */   {
/* 117 */     return getParent().getMapId();
/*     */   }
/*     */ 
/*     */   public WorldMap getParent()
/*     */   {
/* 126 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public WorldMapTemplate getTemplate() {
/* 130 */     return this.parent.getTemplate();
/*     */   }
/*     */ 
/*     */   MapRegion getRegion(VisibleObject object)
/*     */   {
/* 140 */     return getRegion(object.getX(), object.getY(), object.getZ());
/*     */   }
/*     */ 
/*     */   public abstract MapRegion getRegion(float paramFloat1, float paramFloat2, float paramFloat3);
/*     */ 
/*     */   protected abstract MapRegion createMapRegion(int paramInt);
/*     */ 
/*     */   protected abstract void initMapRegions();
/*     */ 
/*     */   public abstract boolean isPersonal();
/*     */ 
/*     */   public abstract int getOwnerId();
/*     */ 
/*     */   public World getWorld()
/*     */   {
/* 172 */     return getParent().getWorld();
/*     */   }
/*     */ 
/*     */   public void addObject(VisibleObject object)
/*     */   {
/*     */     Iterator i$;
/* 179 */     if (this.worldMapObjects.put(object.getObjectId(), object) != null) {
/* 180 */       throw new DuplicateAionObjectException("Object with templateId " + String.valueOf(object.getObjectTemplate().getTemplateId()) + " already spawned in the instance " + String.valueOf(getMapId()) + " " + String.valueOf(getInstanceId()));
/*     */     }
/*     */ 
/* 184 */     if (object instanceof Npc) {
/* 185 */       QuestNpc data = QuestEngine.getInstance().getQuestNpc(((Npc)object).getNpcId());
/* 186 */       if (data != null)
/* 187 */         for (i$ = data.getOnQuestStart().iterator(); i$.hasNext(); ) { int id = ((Integer)i$.next()).intValue();
/* 188 */           if (!(this.questIds.contains(Integer.valueOf(id))))
/* 189 */             this.questIds.add(Integer.valueOf(id));
/*     */         }
/*     */     }
/* 192 */     if (object instanceof Player) {
/* 193 */       if (getParent().isPossibleFly())
/* 194 */         ((Player)object).setInsideZoneType(ZoneType.FLY);
/* 195 */       this.worldMapPlayers.put(object.getObjectId(), (Player)object);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeObject(AionObject object)
/*     */   {
/* 203 */     this.worldMapObjects.remove(object.getObjectId());
/* 204 */     if (object instanceof Player) {
/* 205 */       if (getParent().isPossibleFly())
/* 206 */         ((Player)object).unsetInsideZoneType(ZoneType.FLY);
/* 207 */       this.worldMapPlayers.remove(object.getObjectId());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Npc getNpc(int npcId)
/*     */   {
/* 217 */     for (Iterator iter = objectIterator(); iter.hasNext(); ) {
/* 218 */       VisibleObject obj = (VisibleObject)iter.next();
/* 219 */       if (obj instanceof Npc) {
/* 220 */         Npc npc = (Npc)obj;
/* 221 */         if (npc.getNpcId() == npcId) {
/* 222 */           return npc;
/*     */         }
/*     */       }
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public List<Player> getPlayersInside() {
/* 230 */     List playersInside = new ArrayList();
/* 231 */     Iterator players = playerIterator();
/* 232 */     while (players.hasNext()) {
/* 233 */       playersInside.add(players.next());
/*     */     }
/* 235 */     return playersInside;
/*     */   }
/*     */ 
/*     */   public List<Npc> getNpcs(int npcId)
/*     */   {
/* 244 */     List npcs = new ArrayList();
/* 245 */     for (Iterator iter = objectIterator(); iter.hasNext(); ) {
/* 246 */       VisibleObject obj = (VisibleObject)iter.next();
/* 247 */       if (obj instanceof Npc) {
/* 248 */         Npc npc = (Npc)obj;
/* 249 */         if (npc.getNpcId() == npcId) {
/* 250 */           npcs.add(npc);
/*     */         }
/*     */       }
/*     */     }
/* 254 */     return npcs;
/*     */   }
/*     */ 
/*     */   public List<Npc> getNpcs()
/*     */   {
/* 261 */     List npcs = new ArrayList();
/* 262 */     for (Iterator iter = objectIterator(); iter.hasNext(); ) {
/* 263 */       VisibleObject obj = (VisibleObject)iter.next();
/* 264 */       if (obj instanceof Npc) {
/* 265 */         npcs.add((Npc)obj);
/*     */       }
/*     */     }
/* 268 */     return npcs;
/*     */   }
/*     */ 
/*     */   public Map<Integer, StaticDoor> getDoors()
/*     */   {
/* 275 */     Map doors = new HashMap();
/* 276 */     for (Iterator iter = objectIterator(); iter.hasNext(); ) {
/* 277 */       VisibleObject obj = (VisibleObject)iter.next();
/* 278 */       if (obj instanceof StaticDoor) {
/* 279 */         StaticDoor door = (StaticDoor)obj;
/* 280 */         doors.put(Integer.valueOf(door.getSpawn().getEntityId()), door);
/*     */       }
/*     */     }
/* 283 */     return doors;
/*     */   }
/*     */ 
/*     */   public List<Trap> getTraps(Creature p)
/*     */   {
/* 290 */     List traps = new ArrayList();
/* 291 */     for (Iterator iter = objectIterator(); iter.hasNext(); ) {
/* 292 */       VisibleObject obj = (VisibleObject)iter.next();
/* 293 */       if (obj instanceof Trap) {
/* 294 */         Trap t = (Trap)obj;
/* 295 */         if (t.getCreatorId() == p.getObjectId().intValue()) {
/* 296 */           traps.add(t);
/*     */         }
/*     */       }
/*     */     }
/* 300 */     return traps;
/*     */   }
/*     */ 
/*     */   public int getInstanceId()
/*     */   {
/* 307 */     return this.instanceId;
/*     */   }
/*     */ 
/*     */   public final boolean isBeginnerInstance() {
/* 311 */     if (this.parent == null)
/* 312 */       return false;
/* 313 */     if (this.parent.getTemplate().isInstance()) {
/* 314 */       return false;
/*     */     }
/* 316 */     int twinCount = this.parent.getTemplate().getTwinCount();
/* 317 */     if (twinCount == 0) {
/* 318 */       twinCount = 1;
/*     */     }
/* 320 */     return (getInstanceId() > twinCount);
/*     */   }
/*     */ 
/*     */   public boolean isInInstance(int objId)
/*     */   {
/* 330 */     return this.worldMapPlayers.containsKey(Integer.valueOf(objId));
/*     */   }
/*     */ 
/*     */   public Iterator<VisibleObject> objectIterator()
/*     */   {
/* 337 */     return this.worldMapObjects.values().iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<Player> playerIterator()
/*     */   {
/* 344 */     return this.worldMapPlayers.values().iterator();
/*     */   }
/*     */ 
/*     */   public void registerGroup(PlayerGroup group) {
/* 348 */     this.registeredGroup = group;
/* 349 */     register(group.getTeamId().intValue());
/*     */   }
/*     */ 
/*     */   public void registerGroup(PlayerAlliance group) {
/* 353 */     this.registredAlliance = group;
/* 354 */     register(group.getObjectId().intValue());
/*     */   }
/*     */ 
/*     */   public void registerGroup(League group) {
/* 358 */     this.registredLeague = group;
/* 359 */     register(group.getObjectId().intValue());
/*     */   }
/*     */ 
/*     */   public PlayerAlliance getRegistredAlliance() {
/* 363 */     return this.registredAlliance;
/*     */   }
/*     */ 
/*     */   public League getRegistredLeague() {
/* 367 */     return this.registredLeague;
/*     */   }
/*     */ 
/*     */   public void register(int objectId)
/*     */   {
/* 374 */     this.registeredObjects.add(Integer.valueOf(objectId));
/*     */   }
/*     */ 
/*     */   public boolean isRegistered(int objectId)
/*     */   {
/* 382 */     return this.registeredObjects.contains(Integer.valueOf(objectId));
/*     */   }
/*     */ 
/*     */   public Future<?> getEmptyInstanceTask()
/*     */   {
/* 389 */     return this.emptyInstanceTask;
/*     */   }
/*     */ 
/*     */   public void setEmptyInstanceTask(Future<?> emptyInstanceTask)
/*     */   {
/* 397 */     this.emptyInstanceTask = emptyInstanceTask;
/*     */   }
/*     */ 
/*     */   public PlayerGroup getRegisteredGroup()
/*     */   {
/* 404 */     return this.registeredGroup;
/*     */   }
/*     */ 
/*     */   public int playersCount()
/*     */   {
/* 411 */     return this.worldMapPlayers.size();
/*     */   }
/*     */ 
/*     */   public FastList<Integer> getQuestIds() {
/* 415 */     return this.questIds;
/*     */   }
/*     */ 
/*     */   public final InstanceHandler getInstanceHandler() {
/* 419 */     return this.instanceHandler;
/*     */   }
/*     */ 
/*     */   public final void setInstanceHandler(InstanceHandler instanceHandler) {
/* 423 */     this.instanceHandler = instanceHandler;
/*     */   }
/*     */ 
/*     */   public Player getPlayer(Integer object) {
/* 427 */     for (Player player : this.worldMapPlayers.values()) {
/* 428 */       if (object == player.getObjectId()) {
/* 429 */         return player;
/*     */       }
/*     */     }
/* 432 */     return null;
/*     */   }
/*     */ 
/*     */   public void doOnAllPlayers(Visitor<Player> visitor)
/*     */   {
/*     */     try
/*     */     {
/* 440 */       for (Player player : this.worldMapPlayers.values())
/* 441 */         if (player != null)
/* 442 */           visitor.visit(player);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 446 */       log.error("Exception when running visitor on all players" + ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ZoneInstance[] filterZones(int mapId, int regionId, float startX, float startY, float minZ, float maxZ) {
/* 451 */     List regionZones = new ArrayList();
/* 452 */     RegionZone regionZone = new RegionZone(startX, startY, minZ, maxZ);
/*     */ 
/* 454 */     for (ZoneInstance zoneInstance : this.zones.values()) {
/* 455 */       if (zoneInstance.getAreaTemplate().intersectsRectangle(regionZone))
/* 456 */         regionZones.add(zoneInstance);
/* 457 */       else if (zoneInstance.getZoneTemplate().getZoneType() == ZoneClassName.DUMMY) {
/* 458 */         log.error("Region " + regionId + " should intersect with whole map zone!!! (map=" + mapId + ")");
/*     */       }
/*     */     }
/* 461 */     return ((ZoneInstance[])regionZones.toArray(new ZoneInstance[regionZones.size()]));
/*     */   }
/*     */ 
/*     */   public boolean isInsideZone(VisibleObject object, ZoneName zoneName)
/*     */   {
/* 470 */     ZoneInstance zoneTemplate = (ZoneInstance)this.zones.get(zoneName);
/* 471 */     if (zoneTemplate == null)
/* 472 */       return false;
/* 473 */     return isInsideZone(object.getPosition(), zoneName);
/*     */   }
/*     */ 
/*     */   public boolean isInsideZone(WorldPosition pos, ZoneName zoneName)
/*     */   {
/* 482 */     MapRegion mapRegion = getRegion(pos.getX(), pos.getY(), pos.getZ());
/* 483 */     return mapRegion.isInsideZone(zoneName, pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */ 
/*     */   public void setSoloPlayerObj(Integer obj) {
/* 487 */     this.soloPlayer = obj;
/*     */   }
/*     */ 
/*     */   public Integer getSoloPlayerObj() {
/* 491 */     return this.soloPlayer;
/*     */   }
/*     */ }