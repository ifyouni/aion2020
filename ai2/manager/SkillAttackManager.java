/*     */ package com.aionemu.gameserver.ai2.manager;
/*     */ 
/*     */ import com.aionemu.gameserver.ai2.AI2Logger;
/*     */ import com.aionemu.gameserver.ai2.AISubState;
/*     */ import com.aionemu.gameserver.ai2.NpcAI2;
/*     */ import com.aionemu.gameserver.ai2.event.AIEventType;
/*     */ import com.aionemu.gameserver.controllers.NpcController;
/*     */ import com.aionemu.gameserver.controllers.effect.EffectController;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.SkillData;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.skill.NpcSkillEntry;
/*     */ import com.aionemu.gameserver.model.skill.NpcSkillList;
/*     */ import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcGameStats;
/*     */ import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
/*     */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*     */ import com.aionemu.gameserver.skillengine.effect.AbnormalState;
/*     */ import com.aionemu.gameserver.skillengine.model.SkillTemplate;
/*     */ import com.aionemu.gameserver.skillengine.model.SkillType;
/*     */ import com.aionemu.gameserver.utils.MathUtil;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ 
/*     */ public class SkillAttackManager
/*     */ {
/*     */   public static void performAttack(NpcAI2 npcAI, int delay)
/*     */   {
/*  33 */     if ((npcAI.getOwner().getObjectTemplate().getAttackRange() == 0) && 
/*  34 */       (npcAI.getOwner().getTarget() != null) && (!(MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), npcAI.getOwner().getAggroRange())))) {
/*  35 */       npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
/*  36 */       npcAI.getOwner().getController().abortCast();
/*  37 */       return;
/*     */     }
/*  39 */     if (npcAI.setSubStateIfNot(AISubState.CAST))
/*  40 */       if (delay > 0)
/*  41 */         ThreadPoolManager.getInstance().schedule(new SkillAction(npcAI), delay + DataManager.SKILL_DATA.getSkillTemplate(npcAI.getSkillId()).getDuration());
/*     */       else
/*  43 */         skillAction(npcAI);
/*     */   }
/*     */ 
/*     */   protected static void skillAction(NpcAI2 npcAI)
/*     */   {
/*  49 */     Creature target = (Creature)npcAI.getOwner().getTarget();
/*  50 */     if ((npcAI.getOwner().getObjectTemplate().getAttackRange() == 0) && 
/*  51 */       (npcAI.getOwner().getTarget() != null) && (!(MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), npcAI.getOwner().getAggroRange())))) {
/*  52 */       npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
/*  53 */       npcAI.getOwner().getController().abortCast();
/*  54 */       return;
/*     */     }
/*     */ 
/*  57 */     if ((target != null) && (!(target.getLifeStats().isAlreadyDead()))) {
/*  58 */       int skillId = npcAI.getSkillId();
/*  59 */       int skillLevel = npcAI.getSkillLevel();
/*  60 */       SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
/*  61 */       int duration = template.getDuration();
/*  62 */       if (npcAI.isLogging()) {
/*  63 */         AI2Logger.info(npcAI, "Using skill " + skillId + " level: " + skillLevel + " duration: " + duration);
/*     */       }
/*  65 */       switch (1.$SwitchMap$com$aionemu$gameserver$skillengine$model$SkillSubType[template.getSubType().ordinal()])
/*     */       {
/*     */       case 1:
/*  67 */         switch (1.$SwitchMap$com$aionemu$gameserver$skillengine$properties$FirstTargetAttribute[template.getProperties().getFirstTarget().ordinal()])
/*     */         {
/*     */         case 1:
/*  69 */           if (!(npcAI.getOwner().getEffectController().isAbnormalPresentBySkillId(skillId))) break label263;
/*  70 */           afterUseSkill(npcAI);
/*  71 */           return;
/*     */         }
/*     */ 
/*  75 */         if (target.getEffectController().isAbnormalPresentBySkillId(skillId)) {
/*  76 */           afterUseSkill(npcAI);
/*  77 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  82 */       label263: boolean success = npcAI.getOwner().getController().useSkill(skillId, skillLevel);
/*  83 */       if (!(success))
/*  84 */         afterUseSkill(npcAI);
/*     */     }
/*     */     else {
/*  87 */       npcAI.setSubStateIfNot(AISubState.NONE);
/*  88 */       npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void afterUseSkill(NpcAI2 npcAI)
/*     */   {
/*  94 */     npcAI.setSubStateIfNot(AISubState.NONE);
/*  95 */     npcAI.onGeneralEvent(AIEventType.ATTACK_COMPLETE);
/*     */   }
/*     */ 
/*     */   public static NpcSkillEntry chooseNextSkill(NpcAI2 npcAI) {
/*  99 */     if (npcAI.isInSubState(AISubState.CAST)) {
/* 100 */       return null;
/*     */     }
/* 102 */     Npc owner = npcAI.getOwner();
/* 103 */     NpcSkillList skillList = owner.getSkillList();
/* 104 */     if ((skillList == null) || (skillList.size() == 0)) {
/* 105 */       return null;
/*     */     }
/* 107 */     if (owner.getGameStats().canUseNextSkill()) {
/* 108 */       NpcSkillEntry npcSkill = skillList.getRandomSkill();
/* 109 */       if (npcSkill != null) {
/* 110 */         int currentHpPercent = owner.getLifeStats().getHpPercentage();
/* 111 */         if (npcSkill.isReady(currentHpPercent, System.currentTimeMillis() - owner.getGameStats().getFightStartingTime())) {
/* 112 */           SkillTemplate template = npcSkill.getSkillTemplate();
/* 113 */           if (((template.getType() == SkillType.MAGICAL) && (owner.getEffectController().isAbnormalSet(AbnormalState.SILENCE))) || ((template.getType() == SkillType.PHYSICAL) && (owner.getEffectController().isAbnormalSet(AbnormalState.BIND))) || (owner.getEffectController().isUnderFear()))
/*     */           {
/* 116 */             return null; }
/* 117 */           npcSkill.setLastTimeUsed();
/* 118 */           return npcSkill;
/*     */         }
/*     */       }
/*     */     }
/* 122 */     return null; }
/*     */ 
/*     */   private static final class SkillAction implements Runnable {
/*     */     private NpcAI2 npcAI;
/*     */ 
/*     */     SkillAction(NpcAI2 npcAI) {
/* 128 */       this.npcAI = npcAI;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 133 */       SkillAttackManager.skillAction(this.npcAI);
/* 134 */       this.npcAI = null;
/*     */     }
/*     */   }
/*     */ }