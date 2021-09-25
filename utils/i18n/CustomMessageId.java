/*     */ package com.aionemu.gameserver.utils.i18n;
/*     */ 
/*     */ import com.aionemu.gameserver.configs.network.NetworkConfig;
/*     */ 
/*     */ public enum CustomMessageId
/*     */ {
/*  25 */   SDKERR, DATABASE, DATABASE_FULL, POOLS, SYSLOG, SYSINFO, THREADPOOL, DAO, CONFIG, CONFIG_ADMIN, CONFIG_MAIN, CONFIG_NETWORK, STATIC_WORD, STATIC_LEVEL, STATIC_STARTS, STATIC_SUMMON_STARTS, STATIC_ITEM_CLEANUP, STATIC_ITEM_DATA, STATIC_ITEM_RANDOM, STATIC_ITEM_GROUPS, STATIC_NPCDATA, STATIC_MAIL, STATIC_NPCSHOUT, STATIC_PETDATA, STATIC_PETDOPING, STATIC_PLAYERDATA, STATIC_TRADELIST, STATIC_PORTER, STATIC_TELELOC, STATIC_SKILL, STATIC_MOTION_TIMES, STATIC_SKILLTREE, STATIC_CUBEEXP, STATIC_WAREHOUSE, STATIC_BIND, STATIC_QUESTDATA, STATIC_GATHERABLE, STATIC_TITLEDATA, STATIC_WALKERDATA, STATIC_ZONEDATA, STATIC_GOODLIST, STATIC_TRIBE, STATIC_RECIPE, STATIC_CHEST, STATIC_DOOR, STATIC_ITEMSET, STATIC_NPCFACTIONS, STATIC_NPCSKILL, STATIC_PETSKILL, STATIC_SIEGELOC, STATIC_FLYRING, STATIC_SHIELD, STATIC_PET, STATIC_GUIDE, STATIC_ROAD, STATIC_COOLTIME, STATIC_DECO, STATIC_AI, STATIC_FLYPATH, STATIC_WIND, STATIC_ASSEMBLEDNPC, STATIC_COSMETIC, STATIC_NPCDROP, STATIC_AUTOGROUP, STATIC_SPAWN, STATIC_EVENT, STATIC_PANELSKILL, STATIC_BUFFS, STATIC_HOUSING, STATIC_RIDE, STATIC_EXIT, STATIC_PORTALLOC, STATIC_PORTALTEMP, STATIC_HOUSE, STATIC_HOUSEPARTS, STATIC_HOUSENPC, STATIC_HOUSE_SCRIPT, STATIC_CURING, STATIC_ASSEMBLY, STATIC_GOTOMAPS, DATA_STATICDATA, DATA_LOAD_TIME, IDFACTORY, IDFACTORYS, ZONE_INFO, ZONE_STARTED, LUNA_SYS, GEO, AI2, SPAWN, ZONE_LOAD, WORLD_LOAD, DATA_CLEANING, DATA_CLEANING1, QUEST, INSTANCE, INSTANCE_LOAD, ANNOUNCE_LOAD, AI2_LOAD, QUEST_DUP, QUEST_LOAD, SIEGE, SPAWN_WORLD, HOUSING_LOADING, HOUSING_LOADED, HOUSING_SPAWN, STATICDOOR_SPAWN, TASK, HOUSING_BIGS, HOUSING_MISS_BIGS, HOUSING_BIGS_LOAD, SPAWN_LOAD_NPC, SPAWN_LOAD_GATHERABLE, RIFT, RIFT_SPAWN, RIFT_SPAWN_INSTANCE, RIFT_SPAWN1, LIMITED_LOAD, GAMETIME, GAMETIME_UPDATA, DEBUG, DEBUG_UPDATA, BROKER, BROKER_LOAD, PETITION, PETITION_LOAD, CURING, HTML_CACHE, HTML_CACHE_LOAD, HTML_CACHE_SKIPPED, HTML_CACHE_SKIPP, HTML_CACHE_DELETE, HTML_CACHE_USING, HTML_CACHE_CREATING, HTML_CACHE_LOAD_PATH, ABYSSRANK_UPDATA, ABYSSRANK, ABYSSRANK_EXE_TIME, ABYSSRANK_CACHE, LEGION_TASK_STARTED, LEGION_UPDATA, TASK_FROM_DB, DREDGION_START, EVENT_SPAWN, EVENT_DESPAWN, TRANSFER, TRANSFER_INFO, TRANSFER_CHANGE_NAME, HOUSE_PAY_TASK, HOUSE_PAY_ADD, AION_CONNECTIONS, AEVERSIONS, AEVERSIONS1, AEVERSIONS2, AEVERSIONS3, AEVERSIONS4, AEVERSIONS5, NIOSERVERALL, SIEGEAUTORACE_AUTO_SOURCE_RACE, SIEGEAUTORACE_AUTO_SIEGE_RACE, GAME_STARTED, LOGIN_CONNECT, LOGIN_NOT_CONNECT, LOGIN_CONNECT_LOST, LOGIN_ACC_CONNECT_DISC, LOGIN_ACC_DETECTED, LOGIN_ACC_AUTHED, LOGIN_ACC_NOAUTHED, LOGIN_RE_CONNECT, LOGIN_CLOSING_CONNECT, LOGIN_GS_CONNECT, LOGIN_CONNECTED, CHAT_CONNECT, CHAT_NOT_CONNECT, CHAT_CONNECT_LOST, BAN_MAC, MOTION_LOGGING, CHAT_CONNECTED, CHAT_GS_AUTHED, GAMETIME_SAVE, GAMETIME_CHANGED, MAC_LOGIN_AUDIT, PLAYER_LOGIN_IN, SURVEY_START, DUEL_START, QUEST_COMPLETE, LEGION_WH_UPDATA_START, LEGION_WH_UPDATA_STARTED, LEGION_WH_SAVE, LEGION_WH_SAVED, PLAYER_LOGIN_OUT, PLAYER_LOGIN_OUT1, AIONCLIENTPACKET, CRAFT_SKILL_UPDATA, CRAFT_MAX_EXPERT, CRAFT_MAX_MASTER, IN_GAMESHOP_DISABLED, IN_GAMESHOP_LOADITEMS, IN_GAMESHOP_COSTLOG, IN_GAMESHOP_GIFTS_DISABLED, IN_GAMESHOP_NOT_ADDTOLL, IN_GAMESHOP_LIMIT_LEVEL, IN_GAMESHOP_LIMIT_TIME, WELCOME, WELCOME_MASSEGE1, WELCOME_MASSEGE2, WELCOME_MASSEGE3, DELINITER, VERSION, VERSION_MASSEGE, LOGGED_INVUL, LOGGED_INVIS, LOGGED_ENEMITY_NEUTRAL, LOGGED_ENEMITY_ENEMY, LOGGED_VISION, LOGGED_WHISPER, PLAYER_LASTIP, PLAYER_LASTONLINE, PLAYER_KILLCOUNT, VIPTLTLE1, VIPTLTLE2, VIPTLTLE3, VIP_MASSEGE, TYAIONBTTS, TYAION_T_ZZBG, TYAION_M_ZZBG, TYAION_TLY_ZZBG, TYAIONACTION_1, TYAIONACTION_2, TYAIONACTION_3, TYAIONACTION_4, TYAIONACTION_5, TYAIONACTION_6, TYAIONACTION_7, PLAYERRACE_ASMOS, PLAYERRACE_ELYOS, PLAYERGENDER_MALE, PLAYERGENDER_FEMALE, PLAYERCLASS_WARRIOR, PLAYERCLASS_GLADIATOR, PLAYERCLASS_TEMPLAR, PLAYERCLASS_SCOUT, PLAYERCLASS_ASSASSIN, PLAYERCLASS_RANGER, PLAYERCLASS_MAGE, PLAYERCLASS_SORCERER, PLAYERCLASS_SPIRIT_MASTER, PLAYERCLASS_PRIEST, PLAYERCLASS_CLERIC, PLAYERCLASS_CHANTER, PLAYERCLASS_ENGINEER, PLAYERCLASS_GUNNER, PLAYERCLASS_RIDER, PLAYERCLASS_ARTIST, PLAYERCLASS_BARD, PLAYERLOGGED_MESSAGE, PLAYERLOGGED_MESSAGE1, PLAYERLOGGED_MESSAGE2, PLAYERLOGGED_MESSAGE3, PLAYERLOGGED_MESSAGE4, NPCCONTROLLER_0, NPCCONTROLLER_1, NPCCONTROLLER_2, NPCCONTROLLER_3, NPCCONTROLLER_4, NPCCONTROLLER_5, NPCCONTROLLER_6, NPCCONTROLLER_7, TYAIONACTION_299, TYAIONACTION_300;
/*     */ 
/*     */   private String fallbackMessage;
/*     */ 
/*     */   public String getFallbackMessage()
/*     */   {
/* 333 */     return this.fallbackMessage;
/*     */   }
/*     */ 
/*     */   public static String getMaster() {
/* 337 */     if (NetworkConfig.GAME_BIND_ADDRESS.equals(NetworkConfig.GAME_BIND_ADDRESS)) {
/* 338 */       return "";
/*     */     }
/* 340 */     return "Emulator";
/*     */   }
/*     */ }