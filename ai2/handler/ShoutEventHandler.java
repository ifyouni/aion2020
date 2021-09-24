/*     */ package com.aionemu.gameserver.ai2.handler;
/*     */ 
/*     */ import com.aionemu.commons.utils.Rnd;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.NpcShoutData;
/*     */ import com.aionemu.gameserver.dataholders.WalkerData;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
/*     */ import com.aionemu.gameserver.model.templates.npcshout.NpcShout;
/*     */ import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
/*     */ import com.aionemu.gameserver.model.templates.npcshout.ShoutType;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
/*     */ import com.aionemu.gameserver.services.NpcShoutsService;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.world.WorldPosition;
/*     */ import com.aionemu.gameserver.world.knownlist.KnownList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class ShoutEventHandler
/*     */ {
/*     */   public static void onSee(NpcAI2 npcAI, Creature target)
/*     */   {
/*  38 */     Npc npc = npcAI.getOwner();
/*  39 */     if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SEE)) {
/*  40 */       List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SEE, null, 0);
/*     */ 
/*  42 */       NpcShoutsService.getInstance().shout(npc, target, shouts, 0, false);
/*  43 */       shouts.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onBeforeDespawn(NpcAI2 npcAI) {
/*  48 */     Npc npc = npcAI.getOwner();
/*  49 */     if (!(DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.BEFORE_DESPAWN)))
/*     */       return;
/*  51 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.BEFORE_DESPAWN, null, 0);
/*     */ 
/*  53 */     NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
/*  54 */     shouts.clear();
/*     */   }
/*     */ 
/*     */   public static void onReachedWalkPoint(NpcAI2 npcAI)
/*     */   {
/*  59 */     Npc npc = npcAI.getOwner();
/*  60 */     WalkerTemplate tp = DataManager.WALKER_DATA.getWalkerTemplate(npc.getSpawn().getWalkerId());
/*  61 */     int stepCount = tp.getRouteSteps().size();
/*  62 */     ShoutEventType shoutType = (npc.getMoveController().isChangingDirection()) ? ShoutEventType.WALK_DIRECTION : ShoutEventType.WALK_WAYPOINT;
/*     */ 
/*  64 */     if ((!(DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), shoutType))) || 
/*  65 */       (Rnd.get(stepCount) >= 2)) return;
/*  66 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), shoutType, null, 0);
/*     */ 
/*  68 */     if (npc.getTarget() instanceof Creature)
/*  69 */       NpcShoutsService.getInstance().shout(npc, (Creature)npc.getTarget(), shouts, 0, false);
/*     */     else
/*  71 */       NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
/*  72 */     shouts.clear();
/*     */   }
/*     */ 
/*     */   public static void onSwitchedTarget(NpcAI2 npcAI, Creature creature)
/*     */   {
/*  78 */     Npc npc = npcAI.getOwner();
/*  79 */     if (!(DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SWITCH_TARGET)))
/*     */       return;
/*  81 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.SWITCH_TARGET, null, 0);
/*     */ 
/*  83 */     NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
/*  84 */     shouts.clear();
/*     */   }
/*     */ 
/*     */   public static void onDied(NpcAI2 npcAI)
/*     */   {
/*  89 */     Npc owner = npcAI.getOwner();
/*  90 */     if (DataManager.NPC_SHOUT_DATA.hasAnyShout(owner.getPosition().getMapId(), owner.getNpcId(), ShoutEventType.DIED)) {
/*  91 */       List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(owner.getPosition().getMapId(), owner.getNpcId(), ShoutEventType.DIED, null, 0);
/*     */ 
/*  93 */       if (shouts.size() > 0)
/*  94 */         NpcShoutsService.getInstance().shout(owner, (Creature)owner.getTarget(), shouts, 0, false);
/*  95 */       shouts.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onAttackBegin(NpcAI2 npcAI, Creature creature)
/*     */   {
/* 105 */     Npc npc = npcAI.getOwner();
/* 106 */     if (!(DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_BEGIN)))
/*     */       return;
/* 108 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_BEGIN, null, 0);
/*     */ 
/* 110 */     NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
/* 111 */     shouts.clear();
/* 112 */     return;
/*     */   }
/*     */ 
/*     */   public static void onHelp(NpcAI2 npcAI, Creature creature)
/*     */   {
/* 121 */     Npc npc = npcAI.getOwner();
/* 122 */     if (npc.getAttackedCount() == 0)
/*     */     {
/*     */       List shouts;
/* 123 */       if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED)) {
/* 124 */         shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED, null, 0);
/*     */ 
/* 126 */         NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
/* 127 */         shouts.clear();
/* 128 */         return;
/*     */       }
/* 130 */       if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.HELPCALL)) {
/* 131 */         shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.HELPCALL, null, 0);
/*     */ 
/* 133 */         NpcShoutsService.getInstance().shout(npc, creature, shouts, 0, false);
/* 134 */         shouts.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void onEnemyAttack(NpcAI2 npcAI, Creature target)
/*     */   {
/* 145 */     Npc npc = npcAI.getOwner();
/* 146 */     if (!(DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED))) {
/* 147 */       return;
/*     */     }
/* 149 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACKED, null, 0);
/*     */ 
/* 152 */     List finalShouts = new ArrayList();
/* 153 */     for (NpcShout s : shouts) {
/* 154 */       if (s.getShoutType() == ShoutType.SAY) {
/* 155 */         finalShouts.add(s);
/*     */       }
/*     */     }
/* 158 */     if (finalShouts.size() == 0) {
/* 159 */       return;
/*     */     }
/* 161 */     int randomShout = Rnd.get(finalShouts.size());
/* 162 */     NpcShout shout = (NpcShout)finalShouts.get(randomShout);
/* 163 */     finalShouts.clear();
/* 164 */     shouts.clear();
/*     */ 
/* 166 */     if (!(npc.mayShout(shout.getPollDelay() / 1000))) {
/* 167 */       return;
/*     */     }
/* 169 */     ThreadPoolManager.getInstance().schedule(new Runnable(npc, shout)
/*     */     {
/*     */       public void run()
/*     */       {
/* 173 */         Iterator iter = this.val$npc.getKnownList().getKnownPlayers().values().iterator();
/* 174 */         while (iter.hasNext()) {
/* 175 */           Player kObj = (Player)iter.next();
/* 176 */           if (kObj.getLifeStats().isAlreadyDead())
/* 177 */             return;
/* 178 */           NpcShoutsService.getInstance().shout(this.val$npc, kObj, this.val$shout, this.val$shout.getPollDelay() / 1000);
/*     */         }
/*     */       }
/*     */     }
/*     */     , 0L);
/*     */   }
/*     */ 
/*     */   public static void onCast(NpcAI2 npcAI, Creature creature)
/*     */   {
/* 185 */     handleNumericEvent(npcAI, creature, ShoutEventType.CAST_K);
/*     */   }
/*     */ 
/*     */   public static void onAttack(NpcAI2 npcAI, Creature creature)
/*     */   {
/* 192 */     handleNumericEvent(npcAI, creature, ShoutEventType.ATTACK_K);
/*     */   }
/*     */ 
/*     */   private static void handleNumericEvent(NpcAI2 npcAI, Creature creature, ShoutEventType eventType) {
/* 196 */     Npc owner = npcAI.getOwner();
/* 197 */     List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(owner.getPosition().getMapId(), owner.getNpcId(), eventType, null, 0);
/*     */ 
/* 199 */     if (shouts == null) {
/* 200 */       return;
/*     */     }
/* 202 */     List validShouts = new ArrayList();
/* 203 */     List nonNumberedShouts = new ArrayList();
/* 204 */     for (NpcShout shout : shouts) {
/* 205 */       if (shout.getSkillNo() == 0)
/* 206 */         nonNumberedShouts.add(shout);
/* 207 */       else if (shout.getSkillNo() == owner.getSkillNumber()) {
/* 208 */         validShouts.add(shout);
/*     */       }
/*     */     }
/* 211 */     if (validShouts.size() == 0) {
/* 212 */       validShouts.clear();
/* 213 */       validShouts = nonNumberedShouts;
/*     */     }
/*     */     else {
/* 216 */       nonNumberedShouts.clear();
/*     */     }
/*     */ 
/* 219 */     if (validShouts.size() > 0) {
/* 220 */       NpcShoutsService.getInstance().shout(owner, creature, validShouts, 0, false);
/*     */     }
/*     */ 
/* 223 */     validShouts.clear();
/* 224 */     shouts.clear();
/*     */   }
/*     */ 
/*     */   public static void onAttackEnd(NpcAI2 npcAI) {
/* 228 */     Npc npc = npcAI.getOwner();
/* 229 */     if (DataManager.NPC_SHOUT_DATA.hasAnyShout(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_END)) {
/* 230 */       List shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(npc.getPosition().getMapId(), npc.getNpcId(), ShoutEventType.ATTACK_END, null, 0);
/*     */ 
/* 232 */       NpcShoutsService.getInstance().shout(npc, null, shouts, 0, false);
/* 233 */       shouts.clear();
/*     */     }
/*     */   }
/*     */ }