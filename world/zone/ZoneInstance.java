/*     */ package com.aionemu.gameserver.world.zone;
/*     */ 
/*     */ import com.aionemu.gameserver.controllers.CreatureController;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.geometry.Area;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneInfo;
/*     */ import com.aionemu.gameserver.model.templates.zone.ZoneTemplate;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import com.aionemu.gameserver.world.WorldMap;
/*     */ import com.aionemu.gameserver.world.zone.handler.AdvencedZoneHandler;
/*     */ import com.aionemu.gameserver.world.zone.handler.ZoneHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javolution.util.FastMap;
/*     */ 
/*     */ public class ZoneInstance
/*     */   implements Comparable<ZoneInstance>
/*     */ {
/*     */   private ZoneInfo template;
/*     */   private int mapId;
/*  37 */   private Map<Integer, Creature> creatures = new FastMap();
/*  38 */   protected List<ZoneHandler> handlers = new ArrayList();
/*     */ 
/*     */   public ZoneInstance(int mapId, ZoneInfo template) {
/*  41 */     this.template = template;
/*  42 */     this.mapId = mapId;
/*     */   }
/*     */ 
/*     */   public Area getAreaTemplate()
/*     */   {
/*  49 */     return this.template.getArea();
/*     */   }
/*     */ 
/*     */   public ZoneTemplate getZoneTemplate()
/*     */   {
/*  56 */     return this.template.getZoneTemplate();
/*     */   }
/*     */ 
/*     */   public boolean revalidate(Creature creature) {
/*  60 */     return ((this.mapId == creature.getWorldId()) && (this.template.getArea().isInside3D(creature.getX(), creature.getY(), creature.getZ())));
/*     */   }
/*     */ 
/*     */   public synchronized boolean onEnter(Creature creature) {
/*  64 */     if (this.creatures.containsKey(creature.getObjectId()))
/*  65 */       return false;
/*  66 */     this.creatures.put(creature.getObjectId(), creature);
/*  67 */     if (creature instanceof Player)
/*  68 */       creature.getController().onEnterZone(this);
/*  69 */     for (int i = 0; i < this.handlers.size(); ++i)
/*  70 */       ((ZoneHandler)this.handlers.get(i)).onEnterZone(creature, this);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean onLeave(Creature creature) {
/*  75 */     if (!(this.creatures.containsKey(creature.getObjectId())))
/*  76 */       return false;
/*  77 */     this.creatures.remove(creature.getObjectId());
/*  78 */     creature.getController().onLeaveZone(this);
/*  79 */     for (int i = 0; i < this.handlers.size(); ++i)
/*  80 */       ((ZoneHandler)this.handlers.get(i)).onLeaveZone(creature, this);
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean onDie(Creature attacker, Creature target) {
/*  85 */     if (!(this.creatures.containsKey(target.getObjectId())))
/*  86 */       return false;
/*  87 */     for (int i = 0; i < this.handlers.size(); ++i) {
/*  88 */       ZoneHandler handler = (ZoneHandler)this.handlers.get(i);
/*  89 */       if ((handler instanceof AdvencedZoneHandler) && 
/*  90 */         (((AdvencedZoneHandler)handler).onDie(attacker, target, this))) {
/*  91 */         return true;
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInsideCreature(Creature creature) {
/*  98 */     return this.creatures.containsKey(creature.getObjectId());
/*     */   }
/*     */ 
/*     */   public boolean isInsideCordinate(float x, float y, float z) {
/* 102 */     return this.template.getArea().isInside3D(x, y, z);
/*     */   }
/*     */ 
/*     */   public int compareTo(ZoneInstance o)
/*     */   {
/* 107 */     int result = getZoneTemplate().getPriority() - o.getZoneTemplate().getPriority();
/* 108 */     if (result == 0) {
/* 109 */       return (this.template.getZoneTemplate().getName().id() - o.template.getZoneTemplate().getName().id());
/*     */     }
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */   public void addHandler(ZoneHandler handler) {
/* 115 */     this.handlers.add(handler);
/*     */   }
/*     */ 
/*     */   public boolean canFly() {
/* 119 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.FLY)))
/* 120 */       return World.getInstance().getWorldMap(this.mapId).isPossibleFly();
/* 121 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.FLY.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canGlide() {
/* 125 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.GLIDE)))
/* 126 */       return World.getInstance().getWorldMap(this.mapId).canGlide();
/* 127 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.GLIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canPutKisk() {
/* 131 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.BIND)))
/* 132 */       return World.getInstance().getWorldMap(this.mapId).canPutKisk();
/* 133 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.BIND.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canRecall() {
/* 137 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.RECALL)))
/* 138 */       return World.getInstance().getWorldMap(this.mapId).canRecall();
/* 139 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.RECALL.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canRide() {
/* 143 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.RIDE)))
/* 144 */       return World.getInstance().getWorldMap(this.mapId).canRide();
/* 145 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.RIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean canFlyRide() {
/* 149 */     if ((this.template.getZoneTemplate().getFlags() == -1) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.FLY_RIDE)))
/* 150 */       return World.getInstance().getWorldMap(this.mapId).canFlyRide();
/* 151 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.FLY_RIDE.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isPvpAllowed() {
/* 155 */     if (this.template.getZoneTemplate().getZoneType() != ZoneClassName.PVP)
/* 156 */       return World.getInstance().getWorldMap(this.mapId).isPvpAllowed();
/* 157 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.PVP_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isSameRaceDuelsAllowed() {
/* 161 */     if ((this.template.getZoneTemplate().getZoneType() != ZoneClassName.DUEL) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.DUEL_SAME_RACE_ENABLED)))
/*     */     {
/* 163 */       return World.getInstance().getWorldMap(this.mapId).isSameRaceDuelsAllowed(); }
/* 164 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.DUEL_SAME_RACE_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public boolean isOtherRaceDuelsAllowed() {
/* 168 */     if ((this.template.getZoneTemplate().getZoneType() != ZoneClassName.DUEL) || (this.template.getZoneTemplate().getFlags() == 0) || (World.getInstance().getWorldMap(this.mapId).hasOverridenOption(ZoneAttributes.DUEL_OTHER_RACE_ENABLED)))
/*     */     {
/* 170 */       return World.getInstance().getWorldMap(this.mapId).isOtherRaceDuelsAllowed(); }
/* 171 */     return ((this.template.getZoneTemplate().getFlags() & ZoneAttributes.DUEL_OTHER_RACE_ENABLED.getId()) != 0);
/*     */   }
/*     */ 
/*     */   public int getTownId() {
/* 175 */     return this.template.getZoneTemplate().getTownId();
/*     */   }
/*     */ 
/*     */   public Map<Integer, Creature> getCreatures()
/*     */   {
/* 182 */     return this.creatures;
/*     */   }
/*     */ }