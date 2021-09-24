/*     */ package com.aionemu.gameserver.ai2;
/*     */ 
/*     */ import com.aionemu.gameserver.controllers.CreatureController;
/*     */ import com.aionemu.gameserver.controllers.NpcController;
/*     */ import com.aionemu.gameserver.controllers.ObserveController;
/*     */ import com.aionemu.gameserver.controllers.observer.DialogObserver;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.SkillData;
/*     */ import com.aionemu.gameserver.instance.handlers.InstanceHandler;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.ResponseRequester;
/*     */ import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
/*     */ import com.aionemu.gameserver.questEngine.QuestEngine;
/*     */ import com.aionemu.gameserver.questEngine.model.QuestEnv;
/*     */ import com.aionemu.gameserver.services.drop.DropRegistrationService;
/*     */ import com.aionemu.gameserver.skillengine.model.Effect;
/*     */ import com.aionemu.gameserver.skillengine.model.SkillTemplate;
/*     */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*     */ import com.aionemu.gameserver.world.WorldMapInstance;
/*     */ import com.aionemu.gameserver.world.WorldPosition;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class AI2Actions
/*     */ {
/*     */   public static void deleteOwner(AbstractAI ai2)
/*     */   {
/*  42 */     ai2.getOwner().getController().onDelete();
/*     */   }
/*     */ 
/*     */   public static void killSilently(AbstractAI ai2, Creature target)
/*     */   {
/*  49 */     target.getController().onDie(ai2.getOwner());
/*     */   }
/*     */ 
/*     */   public static void dieSilently(AbstractAI ai2, Creature attacker)
/*     */   {
/*  56 */     ai2.getOwner().getController().onDie(attacker);
/*     */   }
/*     */ 
/*     */   public static void useSkill(AbstractAI ai2, int skillId)
/*     */   {
/*  63 */     ai2.getOwner().getController().useSkill(skillId);
/*     */   }
/*     */ 
/*     */   public static void applyEffect(AbstractAI ai2, SkillTemplate template, Creature target)
/*     */   {
/*  70 */     Effect effect = new Effect(ai2.getOwner(), target, template, template.getLvl(), 0);
/*  71 */     effect.setIsForcedEffect(true);
/*  72 */     effect.initialize();
/*  73 */     effect.applyEffect();
/*     */   }
/*     */ 
/*     */   public static void applyEffectSelf(AbstractAI ai2, int skillId) {
/*  77 */     SkillTemplate st = DataManager.SKILL_DATA.getSkillTemplate(skillId);
/*  78 */     Effect effect = new Effect(ai2.getOwner(), ai2.getOwner(), st, 1, st.getEffectsDuration(skillId));
/*  79 */     effect.initialize();
/*  80 */     effect.applyEffect();
/*     */   }
/*     */ 
/*     */   public static void targetSelf(AbstractAI ai2) {
/*  84 */     ai2.getOwner().setTarget(ai2.getOwner());
/*     */   }
/*     */ 
/*     */   public static void targetCreature(AbstractAI ai2, Creature target) {
/*  88 */     ai2.getOwner().setTarget(target);
/*     */   }
/*     */ 
/*     */   public static void handleUseItemFinish(AbstractAI ai2, Player player) {
/*  92 */     ai2.getPosition().getWorldMapInstance().getInstanceHandler().handleUseItemFinish(player, (Npc)ai2.getOwner());
/*     */   }
/*     */ 
/*     */   public static void fireIndividualEvent(AbstractAI ai2, Npc target) {
/*  96 */     target.getAi2().onIndividualNpcEvent(ai2.getOwner());
/*     */   }
/*     */ 
/*     */   public static void fireNpcKillInstanceEvent(AbstractAI ai2, Player player) {
/* 100 */     ai2.getPosition().getWorldMapInstance().getInstanceHandler().onDie((Npc)ai2.getOwner());
/*     */   }
/*     */ 
/*     */   public static void registerDrop(AbstractAI ai2, Player player, Collection<Player> registeredPlayers) {
/* 104 */     DropRegistrationService.getInstance().registerDrop((Npc)ai2.getOwner(), player, registeredPlayers);
/*     */   }
/*     */ 
/*     */   public static void scheduleRespawn(NpcAI2 ai2) {
/* 108 */     ai2.getOwner().getController().scheduleRespawn();
/*     */   }
/*     */ 
/*     */   public static SelectDialogResult selectDialog(AbstractAI ai2, Player player, int questId, int dialogId) {
/* 112 */     QuestEnv env = new QuestEnv(ai2.getOwner(), player, Integer.valueOf(questId), Integer.valueOf(dialogId));
/* 113 */     boolean result = QuestEngine.getInstance().onDialog(env);
/* 114 */     return new SelectDialogResult(result, env, null);
/*     */   }
/*     */ 
/*     */   public static void addRequest(AbstractAI ai2, Player player, int requestId, AI2Request request, Object[] requestParams)
/*     */   {
/* 139 */     addRequest(ai2, player, requestId, ai2.getObjectId(), request, requestParams);
/*     */   }
/*     */ 
/*     */   public static void addRequest(AbstractAI ai2, Player player, int requestId, int senderId, int range, AI2Request request, Object[] requestParams)
/*     */   {
/* 148 */     boolean requested = player.getResponseRequester().putRequest(requestId, new RequestResponseHandler(ai2.getOwner(), request)
/*     */     {
/*     */       public void denyRequest(Creature requester, Player responder)
/*     */       {
/* 152 */         this.val$request.denyRequest(requester, responder);
/*     */       }
/*     */ 
/*     */       public void acceptRequest(Creature requester, Player responder)
/*     */       {
/* 157 */         this.val$request.acceptRequest(requester, responder);
/*     */       }
/*     */     });
/* 161 */     if (requested) {
/* 162 */       if (range > 0) {
/* 163 */         player.getObserveController().addObserver(new DialogObserver(ai2.getOwner(), player, range, request)
/*     */         {
/*     */           public void tooFar(Creature requester, Player responder) {
/* 166 */             this.val$request.denyRequest(requester, responder);
/*     */           }
/*     */         });
/*     */       }
/* 170 */       PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(requestId, senderId, range, requestParams));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void addRequest(AbstractAI ai2, Player player, int requestId, int senderId, AI2Request request, Object[] requestParams)
/*     */   {
/* 178 */     addRequest(ai2, player, requestId, senderId, 0, request, requestParams);
/*     */   }
/*     */ 
/*     */   public static final class SelectDialogResult
/*     */   {
/*     */     private final boolean success;
/*     */     private final QuestEnv env;
/*     */ 
/*     */     private SelectDialogResult(boolean success, QuestEnv env)
/*     */     {
/* 122 */       this.success = success;
/* 123 */       this.env = env; }
/*     */ 
/*     */     public boolean isSuccess() {
/* 126 */       return this.success; }
/*     */ 
/*     */     public QuestEnv getEnv() {
/* 129 */       return this.env;
/*     */     }
/*     */   }
/*     */ }