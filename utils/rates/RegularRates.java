/*     */ package com.aionemu.gameserver.utils.rates;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.main.CraftConfig;
/*     */ import com.aionemu.gameserver.configs.main.RateConfig;
/*     */ 
/*     */ public class RegularRates extends Rates
/*     */ {
/*     */   public float getGroupXpRate()
/*     */   {
/*  10 */     return RateConfig.GROUPXP_RATE;
/*     */   }
/*     */ 
/*     */   public float getDropRate()
/*     */   {
/*  15 */     return RateConfig.DROP_RATE;
/*     */   }
/*     */ 
/*     */   public float getApNpcRate()
/*     */   {
/*  20 */     return RateConfig.AP_NPC_RATE;
/*     */   }
/*     */ 
/*     */   public float getApPlayerGainRate()
/*     */   {
/*  25 */     return RateConfig.AP_PLAYER_GAIN_RATE;
/*     */   }
/*     */ 
/*     */   public float getXpPlayerGainRate()
/*     */   {
/*  30 */     return RateConfig.XP_PLAYER_GAIN_RATE;
/*     */   }
/*     */ 
/*     */   public float getApPlayerLossRate()
/*     */   {
/*  35 */     return RateConfig.AP_PLAYER_LOSS_RATE;
/*     */   }
/*     */ 
/*     */   public float getGpPlayerLossRate() {
/*  39 */     return RateConfig.GP_PLAYER_LOSS_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestKinahRate() {
/*  43 */     return RateConfig.QUEST_KINAH_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestXpRate()
/*     */   {
/*  48 */     return RateConfig.QUEST_XP_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestApRate()
/*     */   {
/*  53 */     return RateConfig.QUEST_AP_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestGpRate()
/*     */   {
/*  58 */     return RateConfig.QUEST_GP_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestAbyssOpRate()
/*     */   {
/*  63 */     return RateConfig.QUEST_ABYSS_OP_RATE;
/*     */   }
/*     */ 
/*     */   public float getQuestExpBoostRate()
/*     */   {
/*  68 */     return RateConfig.QUEST_EXP_BOOST_RATE;
/*     */   }
/*     */ 
/*     */   public float getXpRate()
/*     */   {
/*  73 */     return RateConfig.XP_RATE;
/*     */   }
/*     */ 
/*     */   public float getBookXpRate()
/*     */   {
/*  78 */     return RateConfig.BOOK_RATE;
/*     */   }
/*     */ 
/*     */   public float getCraftingXPRate()
/*     */   {
/*  83 */     return RateConfig.CRAFTING_XP_RATE;
/*     */   }
/*     */ 
/*     */   public float getGatheringXPRate()
/*     */   {
/*  88 */     return RateConfig.GATHERING_XP_RATE;
/*     */   }
/*     */ 
/*     */   public int getGatheringCountRate()
/*     */   {
/*  93 */     return RateConfig.GATHERING_COUNT_RATE;
/*     */   }
/*     */ 
/*     */   public float getDpNpcRate()
/*     */   {
/*  98 */     return RateConfig.DP_NPC_RATE;
/*     */   }
/*     */ 
/*     */   public float getDpPlayerRate()
/*     */   {
/* 103 */     return RateConfig.DP_PLAYER_RATE;
/*     */   }
/*     */ 
/*     */   public int getCraftCritRate()
/*     */   {
/* 108 */     return CraftConfig.CRAFT_CRIT_RATE;
/*     */   }
/*     */ 
/*     */   public int getComboCritRate()
/*     */   {
/* 113 */     return CraftConfig.CRAFT_COMBO_RATE;
/*     */   }
/*     */ 
/*     */   public float getDisciplineRewardRate()
/*     */   {
/* 118 */     return RateConfig.PVP_ARENA_DISCIPLINE_REWARD_RATE;
/*     */   }
/*     */ 
/*     */   public float getChaosRewardRate()
/*     */   {
/* 123 */     return RateConfig.PVP_ARENA_CHAOS_REWARD_RATE;
/*     */   }
/*     */ 
/*     */   public float getHarmonyRewardRate()
/*     */   {
/* 128 */     return RateConfig.PVP_ARENA_HARMONY_REWARD_RATE;
/*     */   }
/*     */ 
/*     */   public float getGloryRewardRate()
/*     */   {
/* 133 */     return RateConfig.PVP_ARENA_GLORY_REWARD_RATE;
/*     */   }
/*     */ 
/*     */   public float getTollRewardRate()
/*     */   {
/* 138 */     return RateConfig.TOLL_REWARD_RATE;
/*     */   }
/*     */ 
/*     */   public float getGlobalDropRate()
/*     */   {
/* 143 */     return RateConfig.GLOBAL_DROP_RATE;
/*     */   }
/*     */ 
/*     */   public float getGpPlayerGainRate()
/*     */   {
/* 148 */     return RateConfig.GP_PLAYER_GAIN_RATE;
/*     */   }
/*     */ }