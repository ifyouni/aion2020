/*     */ package com.aionemu.gameserver.ai2.handler;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.controllers.attack.AggroList;
/*     */ import com.aionemu.gameserver.controllers.attack.AttackResult;
/*     */ import com.aionemu.gameserver.controllers.attack.AttackStatus;
/*     */ import com.aionemu.gameserver.model.TribeClass;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.VisibleObject;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*     */ import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
/*     */ import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.world.geo.GeoService;
/*     */ import com.aionemu.gameserver.world.knownlist.KnownList;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class AggroEventHandler
/*     */ {
/*     */   public static void onAggro(NpcAI2 npcAI, Creature myTarget)
/*     */   {
/*  44 */     Npc owner = npcAI.getOwner();
/*     */ 
/*  46 */     if ((myTarget.getAdminNeutral() == 1) || (myTarget.getAdminNeutral() == 3) || (myTarget.getAdminEnmity() == 1) || (myTarget.getAdminEnmity() == 3))
/*     */     {
/*  48 */       return;
/*     */     }
/*  50 */     PacketSendUtility.broadcastPacket(owner, new SM_ATTACK(owner, myTarget, 0, 633, 0, Collections.singletonList(new AttackResult(0, AttackStatus.NORMALHIT))));
/*     */ 
/*  52 */     ThreadPoolManager.getInstance().schedule(new AggroNotifier(owner, myTarget, true), 500L);
/*     */   }
/*     */ 
/*     */   public static boolean onCreatureNeedsSupport(NpcAI2 npcAI, Creature notMyTarget) {
/*  56 */     Npc owner = npcAI.getOwner();
/*  57 */     if ((notMyTarget.isSupportFrom(owner)) && (MathUtil.isInRange(owner, notMyTarget, owner.getAggroRange())) && (GeoService.getInstance().canSee(owner, notMyTarget)))
/*     */     {
/*  59 */       VisibleObject myTarget = notMyTarget.getTarget();
/*  60 */       if ((myTarget != null) && (myTarget instanceof Creature)) {
/*  61 */         Creature targetCreature = (Creature)myTarget;
/*     */ 
/*  63 */         PacketSendUtility.broadcastPacket(owner, new SM_ATTACK(owner, targetCreature, 0, 633, 0, Collections.singletonList(new AttackResult(0, AttackStatus.NORMALHIT))));
/*  64 */         ThreadPoolManager.getInstance().schedule(new AggroNotifier(owner, targetCreature, false), 500L);
/*  65 */         return true;
/*     */       }
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean onGuardAgainstAttacker(NpcAI2 npcAI, Creature attacker) {
/*  72 */     Npc owner = npcAI.getOwner();
/*  73 */     TribeClass tribe = owner.getTribe();
/*  74 */     if ((!(tribe.isGuard())) && (owner.getObjectTemplate().getNpcTemplateType() != NpcTemplateType.GUARD)) {
/*  75 */       return false;
/*     */     }
/*  77 */     VisibleObject target = attacker.getTarget();
/*  78 */     if ((target != null) && (target instanceof Player)) {
/*  79 */       Player playerTarget = (Player)target;
/*  80 */       if ((!(owner.isEnemy(playerTarget))) && (owner.isEnemy(attacker)) && (MathUtil.isInRange(owner, playerTarget, owner.getAggroRange())) && (GeoService.getInstance().canSee(owner, attacker))) {
/*  81 */         owner.getAggroList().startHate(attacker);
/*  82 */         return true;
/*     */       }
/*     */     }
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   private static final class AggroNotifier implements Runnable
/*     */   {
/*     */     private Npc aggressive;
/*     */     private Creature target;
/*     */     private boolean broadcast;
/*     */ 
/*     */     AggroNotifier(Npc aggressive, Creature target, boolean broadcast) {
/*  95 */       this.aggressive = aggressive;
/*  96 */       this.target = target;
/*  97 */       this.broadcast = broadcast;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 102 */       this.aggressive.getAggroList().addHate(this.target, 1);
/* 103 */       if (this.broadcast) {
/* 104 */         this.aggressive.getKnownList().doOnAllNpcs(new Visitor()
/*     */         {
/*     */           public void visit(Npc object)
/*     */           {
/* 108 */             object.getAi2().onCreatureEvent(AIEventType.CREATURE_NEEDS_SUPPORT, AggroEventHandler.AggroNotifier.this.aggressive);
/*     */           }
/*     */         });
/*     */       }
/* 112 */       this.aggressive = null;
/* 113 */       this.target = null;
/*     */     }
/*     */   }
/*     */ }