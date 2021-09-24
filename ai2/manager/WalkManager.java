/*     */ package com.aionemu.gameserver.ai2.manager;
/*     */ 
/*     */ import com.aionemu.commons.utils.Rnd;
/*     */ import com.aionemu.gameserver.ai2.AIState;
/*     */ import com.aionemu.gameserver.ai2.AISubState;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.configs.main.GeoDataConfig;
/*     */ import com.aionemu.gameserver.controllers.movement.NpcMoveController;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.WalkerData;
/*     */ import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
/*     */ import com.aionemu.gameserver.geoEngine.math.Vector3f;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*     */ import com.aionemu.gameserver.model.templates.walker.RouteStep;
/*     */ import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
/*     */ import com.aionemu.gameserver.spawnengine.WalkerGroup;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.world.geo.GeoService;
/*     */ import java.util.List;
/*     */ 
/*     */ public class WalkManager
/*     */ {
/*     */   private static final int WALK_RANDOM_RANGE = 5;
/*     */ 
/*     */   public static boolean startWalking(NpcAI2 npcAI)
/*     */   {
/*  44 */     npcAI.setStateIfNot(AIState.WALKING);
/*  45 */     Npc owner = npcAI.getOwner();
/*  46 */     WalkerTemplate template = DataManager.WALKER_DATA.getWalkerTemplate(owner.getSpawn().getWalkerId());
/*  47 */     if (template != null) {
/*  48 */       npcAI.setSubStateIfNot(AISubState.WALK_PATH);
/*  49 */       startRouteWalking(npcAI, owner, template);
/*     */     }
/*     */     else {
/*  52 */       return startRandomWalking(npcAI, owner);
/*     */     }
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean startRandomWalking(NpcAI2 npcAI, Npc owner)
/*     */   {
/*  62 */     if (!(AIConfig.ACTIVE_NPC_MOVEMENT)) {
/*  63 */       return false;
/*     */     }
/*  65 */     int randomWalkNr = owner.getSpawn().getRandomWalk();
/*  66 */     if (randomWalkNr == 0) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (npcAI.setSubStateIfNot(AISubState.WALK_RANDOM)) {
/*  70 */       EmoteManager.emoteStartWalking(npcAI.getOwner());
/*  71 */       chooseNextRandomPoint(npcAI);
/*  72 */       return true;
/*     */     }
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   protected static void startRouteWalking(NpcAI2 npcAI, Npc owner, WalkerTemplate template)
/*     */   {
/*  83 */     if (!(AIConfig.ACTIVE_NPC_MOVEMENT))
/*  84 */       return;
/*  85 */     List route = template.getRouteSteps();
/*  86 */     int currentPoint = owner.getMoveController().getCurrentPoint();
/*  87 */     RouteStep nextStep = findNextRoutStep(owner, route);
/*  88 */     owner.getMoveController().setCurrentRoute(route);
/*  89 */     owner.getMoveController().setRouteStep(nextStep, (RouteStep)route.get(currentPoint));
/*  90 */     EmoteManager.emoteStartWalking(npcAI.getOwner());
/*  91 */     npcAI.getOwner().getMoveController().moveToNextPoint();
/*     */   }
/*     */ 
/*     */   protected static RouteStep findNextRoutStep(Npc owner, List<RouteStep> route)
/*     */   {
/* 100 */     int currentPoint = owner.getMoveController().getCurrentPoint();
/* 101 */     RouteStep nextStep = null;
/* 102 */     if (currentPoint != 0) {
/* 103 */       nextStep = findNextRouteStepAfterPause(owner, route, currentPoint);
/*     */     }
/*     */     else {
/* 106 */       nextStep = findClosestRouteStep(owner, route, nextStep);
/*     */     }
/* 108 */     return nextStep;
/*     */   }
/*     */ 
/*     */   protected static RouteStep findClosestRouteStep(Npc owner, List<RouteStep> route, RouteStep nextStep)
/*     */   {
/* 118 */     double closestDist = 0.0D;
/* 119 */     float x = owner.getX();
/* 120 */     float y = owner.getY();
/* 121 */     float z = owner.getZ();
/*     */ 
/* 123 */     if (owner.getWalkerGroup() != null)
/*     */     {
/* 125 */       if (owner.getWalkerGroup().getGroupStep() < 2)
/* 126 */         nextStep = (RouteStep)route.get(0);
/*     */       else
/* 128 */         nextStep = (RouteStep)route.get(owner.getWalkerGroup().getGroupStep() - 1);
/*     */     }
/*     */     else {
/* 131 */       for (RouteStep step : route) {
/* 132 */         double stepDist = MathUtil.getDistance(x, y, z, step.getX(), step.getY(), step.getZ());
/* 133 */         if ((closestDist == 0.0D) || (stepDist < closestDist)) {
/* 134 */           closestDist = stepDist;
/* 135 */           nextStep = step;
/*     */         }
/*     */       }
/*     */     }
/* 139 */     return nextStep;
/*     */   }
/*     */ 
/*     */   protected static RouteStep findNextRouteStepAfterPause(Npc owner, List<RouteStep> route, int currentPoint)
/*     */   {
/* 149 */     RouteStep nextStep = (RouteStep)route.get(currentPoint);
/* 150 */     double stepDist = MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), nextStep.getX(), nextStep.getY(), nextStep.getZ());
/*     */ 
/* 152 */     if (stepDist < 1.0D) {
/* 153 */       nextStep = nextStep.getNextStep();
/*     */     }
/* 155 */     return nextStep;
/*     */   }
/*     */ 
/*     */   public static boolean isWalking(NpcAI2 npcAI)
/*     */   {
/* 165 */     return ((npcAI.isMoveSupported()) && (((hasWalkRoutes(npcAI)) || (npcAI.getOwner().isAttackableNpc()))));
/*     */   }
/*     */ 
/*     */   public static boolean hasWalkRoutes(NpcAI2 npcAI)
/*     */   {
/* 173 */     return npcAI.getOwner().hasWalkRoutes();
/*     */   }
/*     */ 
/*     */   public static void targetReached(NpcAI2 npcAI)
/*     */   {
/* 180 */     if (npcAI.isInState(AIState.WALKING))
/* 181 */       switch (3.$SwitchMap$com$aionemu$gameserver$ai2$AISubState[npcAI.getSubState().ordinal()])
/*     */       {
/*     */       case 1:
/* 183 */         npcAI.getOwner().updateKnownlist();
/* 184 */         if (npcAI.getOwner().getWalkerGroup() != null) {
/* 185 */           npcAI.getOwner().getWalkerGroup().targetReached(npcAI); return;
/*     */         }
/*     */ 
/* 188 */         chooseNextRouteStep(npcAI);
/*     */ 
/* 190 */         break;
/*     */       case 2:
/* 192 */         npcAI.setSubStateIfNot(AISubState.WALK_PATH);
/* 193 */         chooseNextRouteStep(npcAI);
/* 194 */         break;
/*     */       case 3:
/* 196 */         chooseNextRandomPoint(npcAI);
/* 197 */         break;
/*     */       case 4:
/* 199 */         npcAI.getOwner().getMoveController().abortMove();
/*     */       }
/*     */   }
/*     */ 
/*     */   protected static void chooseNextRouteStep(NpcAI2 npcAI)
/*     */   {
/* 211 */     int walkPause = npcAI.getOwner().getMoveController().getWalkPause();
/* 212 */     if (walkPause == 0) {
/* 213 */       npcAI.getOwner().getMoveController().resetMove();
/* 214 */       npcAI.getOwner().getMoveController().chooseNextStep();
/* 215 */       npcAI.getOwner().getMoveController().moveToNextPoint();
/*     */     }
/*     */     else {
/* 218 */       npcAI.getOwner().getMoveController().abortMove();
/* 219 */       npcAI.getOwner().getMoveController().chooseNextStep();
/* 220 */       ThreadPoolManager.getInstance().schedule(new Runnable(npcAI)
/*     */       {
/*     */         public void run()
/*     */         {
/* 224 */           if (this.val$npcAI.isInState(AIState.WALKING))
/* 225 */             this.val$npcAI.getOwner().getMoveController().moveToNextPoint();
/*     */         }
/*     */       }
/*     */       , walkPause);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void chooseNextRandomPoint(NpcAI2 npcAI)
/*     */   {
/* 236 */     Npc owner = npcAI.getOwner();
/* 237 */     owner.getMoveController().abortMove();
/* 238 */     int randomWalkNr = owner.getSpawn().getRandomWalk();
/* 239 */     int walkRange = Math.max(randomWalkNr, 5);
/*     */ 
/* 241 */     float distToSpawn = (float)owner.getDistanceToSpawnLocation();
/*     */ 
/* 243 */     ThreadPoolManager.getInstance().schedule(new Runnable(npcAI, distToSpawn, walkRange, owner)
/*     */     {
/*     */       public void run()
/*     */       {
/* 247 */         if (this.val$npcAI.isInState(AIState.WALKING))
/* 248 */           if (this.val$distToSpawn > this.val$walkRange) {
/* 249 */             this.val$owner.getMoveController().moveToPoint(this.val$owner.getSpawn().getX(), this.val$owner.getSpawn().getY(), this.val$owner.getSpawn().getZ());
/*     */           }
/*     */           else
/*     */           {
/* 253 */             int nextX = Rnd.nextInt(this.val$walkRange * 2) - this.val$walkRange;
/* 254 */             int nextY = Rnd.nextInt(this.val$walkRange * 2) - this.val$walkRange;
/* 255 */             if ((GeoDataConfig.GEO_ENABLE) && (GeoDataConfig.GEO_NPC_MOVE)) {
/* 256 */               byte flags = (byte)(CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId() | CollisionIntention.WALK.getId());
/* 257 */               Vector3f loc = GeoService.getInstance().getClosestCollision(this.val$owner, this.val$owner.getX() + nextX, this.val$owner.getY() + nextY, this.val$owner.getZ(), true, flags);
/*     */ 
/* 259 */               this.val$owner.getMoveController().moveToPoint(loc.x, loc.y, loc.z);
/*     */             }
/*     */             else {
/* 262 */               this.val$owner.getMoveController().moveToPoint(this.val$owner.getX() + nextX, this.val$owner.getY() + nextY, this.val$owner.getZ());
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */     , Rnd.get(AIConfig.MINIMIMUM_DELAY, AIConfig.MAXIMUM_DELAY) * 1000);
/*     */   }
/*     */ 
/*     */   public static void stopWalking(NpcAI2 npcAI)
/*     */   {
/* 275 */     npcAI.getOwner().getMoveController().abortMove();
/* 276 */     npcAI.setStateIfNot(AIState.IDLE);
/* 277 */     npcAI.setSubStateIfNot(AISubState.NONE);
/* 278 */     EmoteManager.emoteStopWalking(npcAI.getOwner());
/*     */   }
/*     */ 
/*     */   public static boolean isArrivedAtPoint(NpcAI2 npcAI)
/*     */   {
/* 286 */     return npcAI.getOwner().getMoveController().isReachedPoint();
/*     */   }
/*     */ }