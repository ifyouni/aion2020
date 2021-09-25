/*     */ package com.aionemu.gameserver.world;
/*     */ 
/*     */ import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
/*     */ import com.aionemu.gameserver.world.zone.ZoneAttributes;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javolution.util.FastMap;
/*     */ 
/*     */ public class WorldMap
/*     */ {
/*     */   private WorldMapTemplate worldMapTemplate;
/*  33 */   private AtomicInteger nextInstanceId = new AtomicInteger(0);
/*     */ 
/*  37 */   private Map<Integer, WorldMapInstance> instances = new FastMap().shared();
/*     */   private World world;
/*     */   private int worldOptions;
/*     */ 
/*     */   public WorldMap(WorldMapTemplate worldMapTemplate, World world)
/*     */   {
/*  44 */     this.world = world;
/*  45 */     this.worldMapTemplate = worldMapTemplate;
/*  46 */     this.worldOptions = worldMapTemplate.getFlags();
/*     */ 
/*  48 */     if (worldMapTemplate.getTwinCount() != 0) {
/*  49 */       for (int i = 1; i <= worldMapTemplate.getTwinCount(); ++i) {
/*  50 */         int nextId = getNextInstanceId();
/*  51 */         addInstance(nextId, WorldMapInstanceFactory.createWorldMapInstance(this, nextId));
/*     */       }
/*     */     }
/*     */     else {
/*  55 */       int nextId = getNextInstanceId();
/*  56 */       addInstance(nextId, WorldMapInstanceFactory.createWorldMapInstance(this, nextId));
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  61 */     return this.worldMapTemplate.getName();
/*     */   }
/*     */ 
/*     */   public int getWaterLevel() {
/*  65 */     return this.worldMapTemplate.getWaterLevel();
/*     */   }
/*     */ 
/*     */   public int getDeathLevel() {
/*  69 */     return this.worldMapTemplate.getDeathLevel();
/*     */   }
/*     */ 
/*     */   public WorldType getWorldType() {
/*  73 */     return this.worldMapTemplate.getWorldType();
/*     */   }
/*     */ 
/*     */   public int getWorldSize() {
/*  77 */     return this.worldMapTemplate.getWorldSize();
/*     */   }
/*     */ 
/*     */   public Integer getMapId() {
/*  81 */     return this.worldMapTemplate.getMapId();
/*     */   }
/*     */ 
/*     */   public boolean isPossibleFly() {
/*  85 */     return ((this.worldOptions & ZoneAttributes.FLY.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isExceptBuff() {
/*  89 */     return this.worldMapTemplate.isExceptBuff();
/*     */   }
/*     */ 
/*     */   public boolean canGlide() {
/*  93 */     return ((this.worldOptions & ZoneAttributes.GLIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canPutKisk() {
/*  97 */     return ((this.worldOptions & ZoneAttributes.BIND.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canRecall() {
/* 101 */     return ((this.worldOptions & ZoneAttributes.RECALL.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canRide() {
/* 105 */     return ((this.worldOptions & ZoneAttributes.RIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canFlyRide() {
/* 109 */     return ((this.worldOptions & ZoneAttributes.FLY_RIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isPvpAllowed() {
/* 113 */     return ((this.worldOptions & ZoneAttributes.PVP_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isSameRaceDuelsAllowed() {
/* 117 */     return ((this.worldOptions & ZoneAttributes.DUEL_SAME_RACE_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isOtherRaceDuelsAllowed() {
/* 121 */     return ((this.worldOptions & ZoneAttributes.DUEL_OTHER_RACE_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public void setWorldOption(ZoneAttributes option) {
/* 125 */     this.worldOptions |= option.getId();
/*     */   }
/*     */ 
/*     */   public void removeWorldOption(ZoneAttributes option) {
/* 129 */     this.worldOptions &= (option.getId() ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public boolean hasOverridenOption(ZoneAttributes option) {
/* 133 */     if ((this.worldMapTemplate.getFlags() & option.getId()) == 0)
/* 134 */       return ((this.worldOptions & option.getId()) != 0);
/* 135 */     return ((this.worldOptions & option.getId()) == 0);
/*     */   }
/*     */ 
/*     */   public int getInstanceCount() {
/* 139 */     int twinCount = this.worldMapTemplate.getTwinCount();
/* 140 */     return ((twinCount > 0) ? twinCount : 1);
/*     */   }
/*     */ 
/*     */   public WorldMapInstance getMainWorldMapInstance()
/*     */   {
/* 151 */     return getWorldMapInstance(1);
/*     */   }
/*     */ 
/*     */   public WorldMapInstance getWorldMapInstanceById(int instanceId)
/*     */   {
/* 161 */     if ((this.worldMapTemplate.getTwinCount() != 0) && 
/* 162 */       (instanceId > this.worldMapTemplate.getTwinCount())) {
/* 163 */       throw new IllegalArgumentException("WorldMapInstance " + getMapId() + " has lower instances count than " + instanceId);
/*     */     }
/*     */ 
/* 167 */     return getWorldMapInstance(instanceId);
/*     */   }
/*     */ 
/*     */   private WorldMapInstance getWorldMapInstance(int instanceId)
/*     */   {
/* 178 */     if (instanceId == 0)
/* 179 */       instanceId = 1;
/* 180 */     return ((WorldMapInstance)this.instances.get(Integer.valueOf(instanceId)));
/*     */   }
/*     */ 
/*     */   public void removeWorldMapInstance(int instanceId)
/*     */   {
/* 190 */     if (instanceId == 0)
/* 191 */       instanceId = 1;
/* 192 */     this.instances.remove(Integer.valueOf(instanceId));
/*     */   }
/*     */ 
/*     */   public void addInstance(int instanceId, WorldMapInstance instance)
/*     */   {
/* 203 */     if (instanceId == 0)
/* 204 */       instanceId = 1;
/* 205 */     this.instances.put(Integer.valueOf(instanceId), instance);
/*     */   }
/*     */ 
/*     */   public World getWorld()
/*     */   {
/* 212 */     return this.world;
/*     */   }
/*     */ 
/*     */   public final WorldMapTemplate getTemplate() {
/* 216 */     return this.worldMapTemplate;
/*     */   }
/*     */ 
/*     */   public int getNextInstanceId()
/*     */   {
/* 223 */     return this.nextInstanceId.incrementAndGet();
/*     */   }
/*     */ 
/*     */   public boolean isInstanceType()
/*     */   {
/* 232 */     return this.worldMapTemplate.isInstance();
/*     */   }
/*     */ 
/*     */   public Iterator<WorldMapInstance> iterator()
/*     */   {
/* 239 */     return this.instances.values().iterator();
/*     */   }
/*     */ 
/*     */   public Collection<Integer> getAvailableInstanceIds()
/*     */   {
/* 246 */     return this.instances.keySet();
/*     */   }
/*     */ 
/*     */   public Collection<WorldMapInstance> getInstances() {
/* 250 */     return this.instances.values();
/*     */   }
/*     */ 
/*     */   public WorldDropType getWorldDropType() {
/* 254 */     return this.worldMapTemplate.getWorldDropType();
/*     */   }
/*     */ }