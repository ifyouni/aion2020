/*     */ package com.aionemu.gameserver.configs.main;
/*     */ 
/*     */ import com.aionemu.commons.configuration.Property;
/*     */ 
/*     */ public class CustomConfig
/*     */ {
/*     */ 
/*     */   @Property(key="gameserver.loginserver.info", defaultValue="false")
/*     */   public static boolean LOGIN_SERVER_INFO;
/*     */ 
/*     */   @Property(key="gameserver.pve.toll.rewarding.enable", defaultValue="false")
/*     */   public static boolean ENABLE_PVE_TOLL_REWARD;
/*     */ 
/*     */   @Property(key="gameserver.pve.toll.reward.chance", defaultValue="50")
/*     */   public static int TOLL_PVE_CHANCE;
/*     */ 
/*     */   @Property(key="gameserver.pve.toll.reward.quantity", defaultValue="5")
/*     */   public static int TOLL_PVE_QUANTITY;
/*     */ 
/*     */   @Property(key="gameserver.pve.toll.reward.worldid", defaultValue="5")
/*     */   public static String TOLL_PVE_WORLDID;
/*     */ 
/*     */   @Property(key="gameserver.worldchannel.costs", defaultValue="50000")
/*     */   public static int WORLD_CHANNEL_AP_COSTS;
/*     */ 
/*     */   @Property(key="gameserver.faction.free", defaultValue="true")
/*     */   public static boolean FACTION_FREE_USE;
/*     */ 
/*     */   @Property(key="gameserver.faction.prices", defaultValue="10000")
/*     */   public static int FACTION_USE_PRICE;
/*     */ 
/*     */   @Property(key="gameserver.faction.cmdchannel", defaultValue="true")
/*     */   public static boolean FACTION_CMD_CHANNEL;
/*     */ 
/*     */   @Property(key="gameserver.faction.chatchannels", defaultValue="false")
/*     */   public static boolean FACTION_CHAT_CHANNEL;
/*     */ 
/*     */   @Property(key="gameserver.premium.notify", defaultValue="false")
/*     */   public static boolean PREMIUM_NOTIFY;
/*     */ 
/*     */   @Property(key="gameserver.enchant.announce.enable", defaultValue="true")
/*     */   public static boolean ENABLE_ENCHANT_ANNOUNCE;
/*     */ 
/*     */   @Property(key="gameserver.chat.factions.enable", defaultValue="false")
/*     */   public static boolean SPEAKING_BETWEEN_FACTIONS;
/*     */ 
/*     */   @Property(key="gameserver.chat.whisper.level", defaultValue="10")
/*     */   public static int LEVEL_TO_WHISPER;
/*     */ 
/*     */   @Property(key="gameserver.search.factions.mode", defaultValue="false")
/*     */   public static boolean FACTIONS_SEARCH_MODE;
/*     */ 
/*     */   @Property(key="gameserver.search.gm.list", defaultValue="false")
/*     */   public static boolean SEARCH_GM_LIST;
/*     */ 
/*     */   @Property(key="gameserver.cross.faction.binding", defaultValue="false")
/*     */   public static boolean ENABLE_CROSS_FACTION_BINDING;
/*     */ 
/*     */   @Property(key="gameserver.simple.secondclass.enable", defaultValue="false")
/*     */   public static boolean ENABLE_SIMPLE_2NDCLASS;
/*     */ 
/*     */   @Property(key="gameserver.skill.chain.triggerrate", defaultValue="true")
/*     */   public static boolean SKILL_CHAIN_TRIGGERRATE;
/*     */ 
/*     */   @Property(key="gameserver.unstuck.delay", defaultValue="3600")
/*     */   public static int UNSTUCK_DELAY;
/*     */ 
/*     */   @Property(key="gameserver.admin.dye.price", defaultValue="1000000")
/*     */   public static int DYE_PRICE;
/*     */ 
/*     */   @Property(key="gameserver.base.flytime", defaultValue="60")
/*     */   public static int BASE_FLYTIME;
/*     */ 
/*     */   @Property(key="gameserver.oldnames.coupon.disable", defaultValue="false")
/*     */   public static boolean OLD_NAMES_COUPON_DISABLED;
/*     */ 
/*     */   @Property(key="gameserver.oldnames.command.disable", defaultValue="true")
/*     */   public static boolean OLD_NAMES_COMMAND_DISABLED;
/*     */ 
/*     */   @Property(key="gameserver.friendlist.size", defaultValue="90")
/*     */   public static int FRIENDLIST_SIZE;
/*     */ 
/*     */   @Property(key="gameserver.basic.questsize.limit", defaultValue="40")
/*     */   public static int BASIC_QUEST_SIZE_LIMIT;
/*     */ 
/*     */   @Property(key="gameserver.instances.enable", defaultValue="true")
/*     */   public static boolean ENABLE_INSTANCES;
/*     */ 
/*     */   @Property(key="gameserver.instances.mob.aggro", defaultValue="300030000,300040000,300050000,300060000,300070000,300080000,300090000,300100000,300110000,300120000,300130000,300140000,300150000,300160000,300170000,300190000,300200000,300210000,300220000,300230000,300240000,300250000,300260000,300270000,300280000,300300000,300310000,300320000,300380000,300440000,300460000,300470000,300510000,300520000,300530000,300540000,300560000,300580000,300590000,300600000,300700000,300800000,300900000,301010000,301020000,301030000,301040000,301050000,301110000,301120000,301130000,301140000,301170000,301180000,301190000,301210000,310050000,310080000,310090000,310100000,310110000,320050000,320080000,320090000,320100000,320110000,320120000,320130000,320150000")
/*     */   public static String INSTANCES_MOB_AGGRO;
/*     */ 
/*     */   @Property(key="gameserver.instances.cooldown.filter", defaultValue="0")
/*     */   public static String INSTANCES_COOL_DOWN_FILTER;
/*     */ 
/*     */   @Property(key="gameserver.instances.cooldown.rate", defaultValue="1")
/*     */   public static int INSTANCES_RATE;
/*     */ 
/*     */   @Property(key="gameserver.enable.kinah.cap", defaultValue="false")
/*     */   public static boolean ENABLE_KINAH_CAP;
/*     */ 
/*     */   @Property(key="gameserver.kinah.cap.value", defaultValue="1000000000")
/*     */   public static long KINAH_CAP_VALUE;
/*     */ 
/*     */   @Property(key="gameserver.noap.mentor.group", defaultValue="false")
/*     */   public static boolean MENTOR_GROUP_AP;
/*     */ 
/*     */   @Property(key="gameserver.dialog.show.id", defaultValue="true")
/*     */   public static boolean ENABLE_SHOW_DIALOG_ID;
/*     */ 
/*     */   @Property(key="gameserver.reward.service.enable", defaultValue="false")
/*     */   public static boolean ENABLE_REWARD_SERVICE;
/*     */ 
/*     */   @Property(key="gameserver.limits.enable", defaultValue="true")
/*     */   public static boolean LIMITS_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.limits.update", defaultValue="0 0 0 ? * *")
/*     */   public static String LIMITS_UPDATE;
/*     */ 
/*     */   @Property(key="gameserver.limits.rate", defaultValue="1")
/*     */   public static int LIMITS_RATE;
/*     */ 
/*     */   @Property(key="gameserver.chat.text.length", defaultValue="150")
/*     */   public static int MAX_CHAT_TEXT_LENGHT;
/*     */ 
/*     */   @Property(key="gameserver.abyssxform.afterlogout", defaultValue="false")
/*     */   public static boolean ABYSSXFORM_LOGOUT;
/*     */ 
/*     */   @Property(key="gameserver.instance.duel.enable", defaultValue="true")
/*     */   public static boolean INSTANCE_DUEL_ENABLE;
/*     */ 
/*     */   @Property(key=" gameserver.ride.restriction.enable", defaultValue="true")
/*     */   public static boolean ENABLE_RIDE_RESTRICTION;
/*     */ 
/*     */   @Property(key="gameserver.challenge.tasks.enabled", defaultValue="false")
/*     */   public static boolean CHALLENGE_TASKS_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.commands.admin.dot.enable", defaultValue="false")
/*     */   public static boolean ENABLE_ADMIN_DOT_COMMANDS;
/*     */ 
/*     */   @Property(key="gameserver.rift.enable", defaultValue="true")
/*     */   public static boolean RIFT_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.rift.duration", defaultValue="1")
/*     */   public static int RIFT_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.rift.appear.chance", defaultValue="50")
/*     */   public static int RIFT_APPEAR_CHANCE;
/*     */ 
/*     */   @Property(key="gameserver.auto.kinah.enabled", defaultValue="false")
/*     */   public static boolean AUTO_KINAH_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.ap.player", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_AP_PLAYER;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.ap.npc", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_AP_NPC;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.hunting", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_HUNTING;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.group.hunting", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_GROUP_HUNTING;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.quest", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_QUEST;
/*     */ 
/*     */   @Property(key="gameserver.enable.exp.progressive.book", defaultValue="false")
/*     */   public static boolean ENABLE_EXP_PROGRESSIVE_BOOK;
/*     */ 
/*     */   @Property(key="gameserver.vortex.enable", defaultValue="true")
/*     */   public static boolean VORTEX_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.vortex.duration", defaultValue="2")
/*     */   public static int VORTEX_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.dispute.land.enable", defaultValue="true")
/*     */   public static boolean DISPUTE_LAND_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.dispute.land.schedule", defaultValue="0 0 2 ? * *")
/*     */   public static String DISPUTE_LAND_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.dispute.land.duration", defaultValue="2")
/*     */   public static int DISPUTE_LAND_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.beritra.enable", defaultValue="true")
/*     */   public static boolean BERITRA_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.beritra.duration", defaultValue="2")
/*     */   public static int BERITRA_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.agent.enable", defaultValue="true")
/*     */   public static boolean AGENT_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.agent.duration", defaultValue="2")
/*     */   public static int AGENT_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.anoha.enable", defaultValue="true")
/*     */   public static boolean ANOHA_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.anoha.duration", defaultValue="1")
/*     */   public static int ANOHA_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.svs.enable", defaultValue="true")
/*     */   public static boolean SVS_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.svs.duration", defaultValue="1")
/*     */   public static int SVS_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.rvr.enable", defaultValue="true")
/*     */   public static boolean RVR_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.rvr.duration", defaultValue="1")
/*     */   public static int RVR_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.moltenus.enable", defaultValue="true")
/*     */   public static boolean MOLTENUS_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.moltenus.duration", defaultValue="1")
/*     */   public static int MOLTENUS_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.dynamic.rift.enable", defaultValue="true")
/*     */   public static boolean DYNAMIC_RIFT_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.dynamic.rift.dragon.schedule", defaultValue="0 0 2 ? * *")
/*     */   public static String DYNAMIC_RIFT_DRAGON_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.dynamic.rift.indratoo.schedule", defaultValue="0 0 2 ? * *")
/*     */   public static String DYNAMIC_RIFT_INDRATOO_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.shugo.merchant.league.schedule", defaultValue="0 0 2 ? * *")
/*     */   public static String SHUGO_MERCHANT_LEAGUE_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.dynamic.rift.duration", defaultValue="1")
/*     */   public static int DYNAMIC_RIFT_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.tower.of.eternity.enable", defaultValue="true")
/*     */   public static boolean TOWER_OF_ETERNITY_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.tower.of.eternity.schedule", defaultValue="0 0 1 ? * *")
/*     */   public static String TOWER_OF_ETERNITY_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.tower.of.eternity.duration", defaultValue="1")
/*     */   public static int TOWER_OF_ETERNITY_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.zorshiv.dredgion.enable", defaultValue="true")
/*     */   public static boolean ZORSHIV_DREDGION_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.zorshiv.dredgion.duration", defaultValue="1")
/*     */   public static int ZORSHIV_DREDGION_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.iu.enable", defaultValue="true")
/*     */   public static boolean IU_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.iu.schedule", defaultValue="0 0 2 ? * *")
/*     */   public static String IU_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.iu.duration", defaultValue="1")
/*     */   public static int IU_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.nightmare.circus.enable", defaultValue="true")
/*     */   public static boolean NIGHTMARE_CIRCUS_ENABLE;
/*     */ 
/*     */   @Property(key="gameserver.nightmare.circus.duration", defaultValue="1")
/*     */   public static int NIGHTMARE_CIRCUS_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.conquest.enable", defaultValue="true")
/*     */   public static boolean CONQUEST_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.conquest.duration", defaultValue="1")
/*     */   public static int CONQUEST_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.idian.depths.enable", defaultValue="true")
/*     */   public static boolean IDIAN_DEPTHS_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.idian.depths.schedule", defaultValue="0 0 6 ? * *")
/*     */   public static String IDIAN_DEPTHS_SCHEDULE;
/*     */ 
/*     */   @Property(key="gameserver.idian.depths.duration", defaultValue="1")
/*     */   public static int IDIAN_DEPTHS_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.instance.rift.enable", defaultValue="true")
/*     */   public static boolean INSTANCE_RIFT_ENABLED;
/*     */ 
/*     */   @Property(key="gameserver.instance.rift.duration", defaultValue="24")
/*     */   public static int INSTANCE_RIFT_DURATION;
/*     */ 
/*     */   @Property(key="gameserver.reconnect.to.bind.point", defaultValue="true")
/*     */   public static boolean ENABLE_RECONNECT_TO_BIND_POINT;
/*     */ 
/*     */   @Property(key="gameserver.base.rewards.enable", defaultValue="true")
/*     */   public static boolean ENABLE_BASE_REWARDS;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.enable", defaultValue="true")
/*     */   public static boolean PROTECTOR_CONQUEROR_ENABLE;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.handled.worlds", defaultValue="")
/* 275 */   public static String PROTECTOR_CONQUEROR_WORLDS = "";
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.kills.refresh", defaultValue="5")
/*     */   public static int PROTECTOR_CONQUEROR_REFRESH;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.kills.decrease", defaultValue="1")
/*     */   public static int PROTECTOR_CONQUEROR_DECREASE;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.level.diff", defaultValue="10")
/*     */   public static int PROTECTOR_CONQUEROR_LEVEL_DIFF;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.1st.rank.kills", defaultValue="5")
/*     */   public static int PROTECTOR_CONQUEROR_1ST_RANK_KILLS;
/*     */ 
/*     */   @Property(key="gameserver.protector.conqueror.2nd.rank.kills", defaultValue="10")
/*     */   public static int PROTECTOR_CONQUEROR_2ND_RANK_KILLS;
/*     */ 
/*     */   @Property(key="gameserver.break.illusion.godstones", defaultValue="1.0")
/*     */   public static float ILLUSION_GODSTONE_BREAK_RATE;
/*     */ 
/*     */   @Property(key="gameserver.energy.of.repose.enable", defaultValue="false")
/*     */   public static boolean ENERGY_OF_REPOSE_ENABLE;
/*     */ 
/*     */   @Property(key="gameserver.enable.luna.cap", defaultValue="false")
/*     */   public static boolean ENABLE_LUNA_CAP;
/*     */ 
/*     */   @Property(key="gameserver.luna.cap.value", defaultValue="9999999")
/*     */   public static long LUNA_CAP_VALUE;
/*     */ 
/*     */   @Property(key="gameserver.pk.tag", defaultValue="☠ %s")
/*     */   public static String TAG_PK;
/*     */ 
/*     */   @Property(key="gameserver.pve.tag", defaultValue="⛨ %s")
/*     */   public static String TAG_PVE;
/*     */ 
/*     */   @Property(key="gameserver.thieves.guild.enable", defaultValue="false")
/*     */   public static boolean THIEVES_ENABLE;
/*     */ 
/*     */   @Property(key="gameserver.minion.max.size", defaultValue="10")
/*     */   public static int MAX_MINION_LIST;
/*     */ 
/*     */   @Property(key="gameserver.threes.upgrade.enable", defaultValue="false")
/*     */   public static boolean THREES_UPGRADE_ENABLE;
/*     */ 
/*     */   @Property(key="gameserver.threes.upgrade.rate", defaultValue="1")
/*     */   public static int THREES_UPGRADE_RATE;
/*     */ }