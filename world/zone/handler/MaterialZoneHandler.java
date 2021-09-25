/*    */ package com.aionemu.gameserver.world.zone.handler;
/*    */ 
/*    */ import com.aionemu.gameserver.controllers.ObserveController;
/*    */ import com.aionemu.gameserver.controllers.observer.ActionObserver;
/*    */ import com.aionemu.gameserver.controllers.observer.CollisionMaterialActor;
/*    */ import com.aionemu.gameserver.controllers.observer.IActor;
/*    */ import com.aionemu.gameserver.geoEngine.scene.Spatial;
/*    */ import com.aionemu.gameserver.model.Race;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.templates.materials.MaterialSkill;
/*    */ import com.aionemu.gameserver.model.templates.materials.MaterialTarget;
/*    */ import com.aionemu.gameserver.model.templates.materials.MaterialTemplate;
/*    */ import com.aionemu.gameserver.world.zone.ZoneInstance;
/*    */ import javolution.util.FastMap;
/*    */ 
/*    */ public class MaterialZoneHandler
/*    */   implements ZoneHandler
/*    */ {
/* 32 */   FastMap<Integer, IActor> observed = new FastMap();
/*    */   private Spatial geometry;
/*    */   private MaterialTemplate template;
/* 36 */   private boolean actOnEnter = false;
/* 37 */   private Race ownerRace = Race.NONE;
/*    */ 
/*    */   public MaterialZoneHandler(Spatial geometry, MaterialTemplate template) {
/* 40 */     this.geometry = geometry;
/* 41 */     this.template = template;
/* 42 */     String name = geometry.getName();
/* 43 */     if ((name.indexOf("FIRE_BOX") != -1) || (name.indexOf("FIRE_SEMISPHERE") != -1) || (name.indexOf("FIREPOT") != -1) || (name.indexOf("FIRE_CYLINDER") != -1) || (name.indexOf("FIRE_CONE") != -1) || (name.startsWith("BU_H_CENTERHALL")))
/*    */     {
/* 45 */       this.actOnEnter = true; }
/* 46 */     if (name.startsWith("BU_AB_DARKSP"))
/* 47 */       this.ownerRace = Race.ASMODIANS;
/* 48 */     else if (name.startsWith("BU_AB_LIGHTSP"))
/* 49 */       this.ownerRace = Race.ELYOS;
/*    */   }
/*    */ 
/*    */   public void onEnterZone(Creature creature, ZoneInstance zone)
/*    */   {
/* 54 */     if (this.ownerRace == creature.getRace())
/* 55 */       return;
/* 56 */     MaterialSkill foundSkill = null;
/* 57 */     for (MaterialSkill skill : this.template.getSkills()) {
/* 58 */       if (skill.getTarget().isTarget(creature)) {
/* 59 */         foundSkill = skill;
/* 60 */         break;
/*    */       }
/*    */     }
/* 63 */     if (foundSkill == null)
/* 64 */       return;
/* 65 */     CollisionMaterialActor actor = new CollisionMaterialActor(creature, this.geometry, this.template);
/* 66 */     creature.getObserveController().addObserver(actor);
/* 67 */     this.observed.put(creature.getObjectId(), actor);
/* 68 */     if (this.actOnEnter)
/* 69 */       actor.act();
/*    */   }
/*    */ 
/*    */   public void onLeaveZone(Creature creature, ZoneInstance zone)
/*    */   {
/* 74 */     IActor actor = (IActor)this.observed.get(creature.getObjectId());
/* 75 */     if (actor != null) {
/* 76 */       creature.getObserveController().removeObserver((ActionObserver)actor);
/* 77 */       this.observed.remove(creature.getObjectId());
/* 78 */       actor.abort();
/*    */     }
/*    */   }
/*    */ }