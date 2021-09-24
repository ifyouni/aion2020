/*     */ package com.aionemu.gameserver;
/*     */ 
/*     */ import ch.lambdaj.Lambda;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import com.aionemu.commons.database.DatabaseFactory;
/*     */ import com.aionemu.commons.database.dao.DAOManager;
/*     */ import com.aionemu.commons.network.NioServer;
/*     */ import com.aionemu.commons.network.ServerCfg;
/*     */ import com.aionemu.commons.services.CronService;
/*     */ import com.aionemu.commons.utils.AEInfos;
/*     */ import com.aionemu.gameserver.ai2.AI2Engine;
/*     */ import com.aionemu.gameserver.cache.HTMLCache;
/*     */ import com.aionemu.gameserver.configs.Config;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.configs.main.AutoGroupConfig;
/*     */ import com.aionemu.gameserver.configs.main.CustomConfig;
/*     */ import com.aionemu.gameserver.configs.main.EventsConfig;
/*     */ import com.aionemu.gameserver.configs.main.FFAConfig;
/*     */ import com.aionemu.gameserver.configs.main.GSConfig;
/*     */ import com.aionemu.gameserver.configs.main.PvPModConfig;
/*     */ import com.aionemu.gameserver.configs.main.RankingConfig;
/*     */ import com.aionemu.gameserver.configs.main.SiegeConfig;
/*     */ import com.aionemu.gameserver.configs.main.ThreadConfig;
/*     */ import com.aionemu.gameserver.configs.main.WeddingsConfig;
/*     */ import com.aionemu.gameserver.configs.network.IP;
/*     */ import com.aionemu.gameserver.configs.network.IPConfig;
/*     */ import com.aionemu.gameserver.configs.network.NetworkConfig;
/*     */ import com.aionemu.gameserver.dao.PlayerDAO;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.instance.InstanceEngine;
/*     */ import com.aionemu.gameserver.model.GameEngine;
/*     */ import com.aionemu.gameserver.model.house.MaintenanceTask;
/*     */ import com.aionemu.gameserver.model.siege.Influence;
/*     */ import com.aionemu.gameserver.network.BannedMacManager;
/*     */ import com.aionemu.gameserver.network.aion.GameConnectionFactoryImpl;
/*     */ import com.aionemu.gameserver.network.chatserver.ChatServer;
/*     */ import com.aionemu.gameserver.network.loginserver.LoginServer;
/*     */ import com.aionemu.gameserver.questEngine.QuestEngine;
/*     */ import com.aionemu.gameserver.services.AbyssLandingService;
/*     */ import com.aionemu.gameserver.services.AbyssLandingSpecialService;
/*     */ import com.aionemu.gameserver.services.AdminService;
/*     */ import com.aionemu.gameserver.services.AgentService;
/*     */ import com.aionemu.gameserver.services.AnnouncementService;
/*     */ import com.aionemu.gameserver.services.AnohaService;
/*     */ import com.aionemu.gameserver.services.BaseService;
/*     */ import com.aionemu.gameserver.services.BeritraService;
/*     */ import com.aionemu.gameserver.services.BrokerService;
/*     */ import com.aionemu.gameserver.services.ChallengeTaskService;
/*     */ import com.aionemu.gameserver.services.ConquestService;
/*     */ import com.aionemu.gameserver.services.CuringZoneService;
/*     */ import com.aionemu.gameserver.services.DatabaseCleaningService;
/*     */ import com.aionemu.gameserver.services.DebugService;
/*     */ import com.aionemu.gameserver.services.DisputeLandService;
/*     */ import com.aionemu.gameserver.services.DynamicRiftService;
/*     */ import com.aionemu.gameserver.services.EventService;
/*     */ import com.aionemu.gameserver.services.ExchangeService;
/*     */ import com.aionemu.gameserver.services.FlyRingService;
/*     */ import com.aionemu.gameserver.services.GameTimeService;
/*     */ import com.aionemu.gameserver.services.HousingBidService;
/*     */ import com.aionemu.gameserver.services.IdianDepthsService;
/*     */ import com.aionemu.gameserver.services.InstanceRiftService;
/*     */ import com.aionemu.gameserver.services.IuService;
/*     */ import com.aionemu.gameserver.services.MoltenusService;
/*     */ import com.aionemu.gameserver.services.NightmareCircusService;
/*     */ import com.aionemu.gameserver.services.NpcShoutsService;
/*     */ import com.aionemu.gameserver.services.OutpostService;
/*     */ import com.aionemu.gameserver.services.PeriodicSaveService;
/*     */ import com.aionemu.gameserver.services.PetitionService;
/*     */ import com.aionemu.gameserver.services.ProtectorConquerorService;
/*     */ import com.aionemu.gameserver.services.RiftService;
/*     */ import com.aionemu.gameserver.services.RoadService;
/*     */ import com.aionemu.gameserver.services.RvrService;
/*     */ import com.aionemu.gameserver.services.ShieldService;
/*     */ import com.aionemu.gameserver.services.SiegeService;
/*     */ import com.aionemu.gameserver.services.SpringZoneService;
/*     */ import com.aionemu.gameserver.services.SvsService;
/*     */ import com.aionemu.gameserver.services.TowerOfEternityService;
/*     */ import com.aionemu.gameserver.services.TownService;
/*     */ import com.aionemu.gameserver.services.VortexService;
/*     */ import com.aionemu.gameserver.services.WeatherService;
/*     */ import com.aionemu.gameserver.services.WeddingService;
/*     */ import com.aionemu.gameserver.services.ZorshivDredgionService;
/*     */ import com.aionemu.gameserver.services.abyss.AbyssRankCleaningService;
/*     */ import com.aionemu.gameserver.services.abyss.AbyssRankUpdateService;
/*     */ import com.aionemu.gameserver.services.abysslandingservice.LandingUpdateService;
/*     */ import com.aionemu.gameserver.services.drop.DropRegistrationService;
/*     */ import com.aionemu.gameserver.services.events.AtreianPassportService;
/*     */ import com.aionemu.gameserver.services.events.BGService;
/*     */ import com.aionemu.gameserver.services.events.BanditService;
/*     */ import com.aionemu.gameserver.services.events.BoostEventService;
/*     */ import com.aionemu.gameserver.services.events.CrazyDaevaService;
/*     */ import com.aionemu.gameserver.services.events.EventWindowService;
/*     */ import com.aionemu.gameserver.services.events.FFAService;
/*     */ import com.aionemu.gameserver.services.events.LadderService;
/*     */ import com.aionemu.gameserver.services.events.PigPoppyEventService;
/*     */ import com.aionemu.gameserver.services.events.ShugoSweepService;
/*     */ import com.aionemu.gameserver.services.events.TreasureAbyssService;
/*     */ import com.aionemu.gameserver.services.gc.GarbageCollector;
/*     */ import com.aionemu.gameserver.services.instance.AsyunatarService;
/*     */ import com.aionemu.gameserver.services.instance.DredgionService2;
/*     */ import com.aionemu.gameserver.services.instance.EngulfedOphidanBridgeService;
/*     */ import com.aionemu.gameserver.services.instance.GrandArenaTrainingCampService;
/*     */ import com.aionemu.gameserver.services.instance.HallOfTenacityService;
/*     */ import com.aionemu.gameserver.services.instance.IDRunService;
/*     */ import com.aionemu.gameserver.services.instance.IdgelDomeLandmarkService;
/*     */ import com.aionemu.gameserver.services.instance.IdgelDomeService;
/*     */ import com.aionemu.gameserver.services.instance.InstanceService;
/*     */ import com.aionemu.gameserver.services.instance.IronWallWarfrontService;
/*     */ import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
/*     */ import com.aionemu.gameserver.services.instance.SuspiciousOphidanBridgeService;
/*     */ import com.aionemu.gameserver.services.player.LunaShopService;
/*     */ import com.aionemu.gameserver.services.player.PlayerEventService;
/*     */ import com.aionemu.gameserver.services.player.PlayerLimitService;
/*     */ import com.aionemu.gameserver.services.ranking.SeasonRankingUpdateService;
/*     */ import com.aionemu.gameserver.services.reward.RewardService;
/*     */ import com.aionemu.gameserver.services.teleport.HotspotTeleportService;
/*     */ import com.aionemu.gameserver.services.territory.TerritoryService;
/*     */ import com.aionemu.gameserver.services.transfers.PlayerTransferService;
/*     */ import com.aionemu.gameserver.spawnengine.ShugoImperialTombSpawnManager;
/*     */ import com.aionemu.gameserver.spawnengine.SpawnEngine;
/*     */ import com.aionemu.gameserver.spawnengine.TemporarySpawnEngine;
/*     */ import com.aionemu.gameserver.taskmanager.TaskManagerFromDB;
/*     */ import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
/*     */ import com.aionemu.gameserver.utils.AEVersions;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.utils.ThreadUncaughtExceptionHandler;
/*     */ import com.aionemu.gameserver.utils.Util;
/*     */ import com.aionemu.gameserver.utils.ZCXInfo;
/*     */ import com.aionemu.gameserver.utils.audit.aion.NetServers;
/*     */ import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;
/*     */ import com.aionemu.gameserver.utils.cron.ThreadPoolManagerRunnableRunner;
/*     */ import com.aionemu.gameserver.utils.gametime.DateTimeUtil;
/*     */ import com.aionemu.gameserver.utils.gametime.GameTimeManager;
/*     */ import com.aionemu.gameserver.utils.i18n.CustomMessageId;
/*     */ import com.aionemu.gameserver.utils.i18n.LanguageHandler;
/*     */ import com.aionemu.gameserver.utils.idfactory.IDFactory;
/*     */ import com.aionemu.gameserver.utils.javaagent.JavaAgentUtils;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import com.aionemu.gameserver.world.geo.GeoService;
/*     */ import com.aionemu.gameserver.world.zone.ZoneService;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class GameServer
/*     */ {
/*  99 */   public static final Logger log = LoggerFactory.getLogger(GameServer.class);
/* 100 */   public static HashSet<String> npcs_count = new HashSet();
/*     */   public static String LANGUAGE;
/*     */   public static String CONFIGURATION_FILE;
/* 585 */   private static Set<StartupHook> startUpHooks = new HashSet();
/*     */ 
/*     */   private static void initalizeLoggger()
/*     */   {
/* 106 */     new File("./log/backup/").mkdirs();
/* 107 */     File[] files = new File("log").listFiles(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String name) {
/* 110 */         return name.endsWith(".log");
/*     */       }
/*     */     });
/* 113 */     if ((files != null) && (files.length > 0)) {
/* 114 */       byte[] buf = new byte[1024];
/*     */       try {
/* 116 */         String outFilename = "./log/backup/" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date()) + ".zip";
/* 117 */         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
/* 118 */         out.setMethod(8);
/* 119 */         out.setLevel(9);
/*     */ 
/* 121 */         for (File logFile : files) {
/* 122 */           FileInputStream in = new FileInputStream(logFile);
/* 123 */           out.putNextEntry(new ZipEntry(logFile.getName()));
/*     */ 
/* 125 */           while ((len = in.read(buf)) > 0)
/*     */           {
/*     */             int len;
/* 126 */             out.write(buf, 0, len);
/*     */           }
/* 128 */           out.closeEntry();
/* 129 */           in.close();
/* 130 */           logFile.delete();
/*     */         }
/* 132 */         out.close();
/*     */       }
/*     */       catch (IOException e) {
/* 135 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 138 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/*     */     try {
/* 140 */       JoranConfigurator configurator = new JoranConfigurator();
/* 141 */       configurator.setContext(lc);
/* 142 */       lc.reset();
/* 143 */       configurator.doConfigure("config/slf4j-logback.xml");
/*     */     }
/*     */     catch (JoranException je)
/*     */     {
/* 147 */       throw new RuntimeException(LanguageHandler.translate(CustomMessageId.SDKERR, new Object[0]), je);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void initializeWelcomeMessage()
/*     */   {
/* 153 */     Util.printSection("统一模拟器");
/* 154 */     System.out.println("00:00:00,000 INFO [main] - Aion-Lightning Core Dev. NcTeam");
/* 155 */     System.out.println("00:00:00,000 INFO [main] - Data Team: 十年永恒 3XAION");
/* 156 */     System.out.println("00:00:00,000 INFO [main] - Code Team: 统一永恒 Ncaion 黑脚部落 ");
/* 157 */     System.out.println("00:00:00,000 INFO [main] - Web: Www.Cmchs.Cn ");
/* 158 */     Util.printSection("Ver:5.8.1");
/* 159 */     System.out.print("授权IP = " + IPConfig.getDefaultAddress());
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 167 */     long start = System.currentTimeMillis();
/*     */ 
/* 169 */     LANGUAGE = (args.length >= 1) ? args[0] : "EN";
/* 170 */     CONFIGURATION_FILE = LANGUAGE;
/*     */ 
/* 172 */     Lambda.enableJitting(true);
/* 173 */     GameEngine[] parallelEngines = { QuestEngine.getInstance(), InstanceEngine.getInstance(), AI2Engine.getInstance(), ChatProcessor.getInstance() };
/*     */ 
/* 175 */     CountDownLatch progressLatch = new CountDownLatch(parallelEngines.length);
/* 176 */     initializeWelcomeMessage();
/* 177 */     initalizeLoggger();
/* 178 */     initUtilityServicesAndConfig();
/*     */ 
/* 180 */     Util.printSection(LanguageHandler.translate(CustomMessageId.DATA_STATICDATA, new Object[0]));
/* 181 */     DataManager.getInstance();
/*     */ 
/* 183 */     Util.printSection(LanguageHandler.translate(CustomMessageId.IDFACTORY, new Object[0]));
/* 184 */     IDFactory.getInstance();
/*     */ 
/* 186 */     Util.printSection(LanguageHandler.translate(CustomMessageId.ZONE_INFO, new Object[0]));
/* 187 */     ZoneService.getInstance().load(null);
/* 188 */     HotspotTeleportService.getInstance();
/* 189 */     RoadService.getInstance();
/* 190 */     System.gc();
/* 191 */     World.getInstance();
/*     */ 
/* 196 */     Util.printSection(LanguageHandler.translate(CustomMessageId.LUNA_SYS, new Object[0]));
/* 197 */     LunaShopService.getInstance().init();
/*     */ 
/* 201 */     ShugoSweepService.getInstance().initShugoSweep();
/*     */ 
/* 203 */     AtreianPassportService.getInstance().onStart();
/*     */ 
/* 205 */     EventWindowService.getInstance().initialize();
/*     */ 
/* 211 */     Util.printSection(LanguageHandler.translate(CustomMessageId.GEO, new Object[0]));
/* 212 */     GeoService.getInstance().initializeGeo();
/* 213 */     DropRegistrationService.getInstance();
/* 214 */     GameServer gs = new GameServer();
/* 215 */     ((PlayerDAO)DAOManager.getDAO(PlayerDAO.class)).setPlayersOffline(false);
/*     */ 
/* 221 */     Util.printSection(LanguageHandler.translate(CustomMessageId.AI2, new Object[0]));
/* 222 */     for (int i = 0; i < parallelEngines.length; ++i) {
/* 223 */       int index = i;
/* 224 */       ThreadPoolManager.getInstance().execute(new Runnable(parallelEngines, index, progressLatch) {
/*     */         public void run() {
/* 226 */           this.val$parallelEngines[this.val$index].load(this.val$progressLatch);
/*     */         }
/*     */       });
/*     */     }
/*     */     try {
/* 231 */       progressLatch.await();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */ 
/* 240 */     SiegeService.getInstance().initSiegeLocations();
/*     */ 
/* 242 */     BaseService.getInstance().initBaseReset();
/* 243 */     BaseService.getInstance().initBaseLocations();
/*     */ 
/* 245 */     OutpostService.getInstance().initOupostReset();
/* 246 */     OutpostService.getInstance().initOutpostLocations();
/*     */ 
/* 248 */     VortexService.getInstance().initVortex();
/* 249 */     VortexService.getInstance().initVortexLocations();
/*     */ 
/* 251 */     BeritraService.getInstance().initBeritra();
/* 252 */     BeritraService.getInstance().initBeritraLocations();
/*     */ 
/* 254 */     AgentService.getInstance().initAgent();
/* 255 */     AgentService.getInstance().initAgentLocations();
/*     */ 
/* 257 */     AnohaService.getInstance().initAnoha();
/* 258 */     AnohaService.getInstance().initAnohaLocations();
/*     */ 
/* 260 */     SvsService.getInstance().initSvs();
/* 261 */     SvsService.getInstance().initSvsLocations();
/*     */ 
/* 263 */     RvrService.getInstance().initRvr();
/* 264 */     RvrService.getInstance().initRvrLocations();
/*     */ 
/* 266 */     IuService.getInstance().initConcert();
/* 267 */     IuService.getInstance().initConcertLocations();
/*     */ 
/* 269 */     NightmareCircusService.getInstance().initCircus();
/* 270 */     NightmareCircusService.getInstance().initCircusLocations();
/*     */ 
/* 272 */     DynamicRiftService.getInstance().initDynamicRift();
/* 273 */     DynamicRiftService.getInstance().initDynamicRiftLocations();
/*     */ 
/* 275 */     InstanceRiftService.getInstance().initInstance();
/* 276 */     InstanceRiftService.getInstance().initInstanceLocations();
/*     */ 
/* 278 */     ZorshivDredgionService.getInstance().initZorshivDredgion();
/* 279 */     ZorshivDredgionService.getInstance().initZorshivDredgionLocations();
/*     */ 
/* 281 */     MoltenusService.getInstance().initMoltenus();
/* 282 */     MoltenusService.getInstance().initMoltenusLocations();
/*     */ 
/* 284 */     RiftService.getInstance().initRifts();
/* 285 */     RiftService.getInstance().initRiftLocations();
/*     */ 
/* 287 */     ConquestService.getInstance().initOffering();
/* 288 */     ConquestService.getInstance().initConquestLocations();
/*     */ 
/* 290 */     IdianDepthsService.getInstance().initIdianDepths();
/* 291 */     IdianDepthsService.getInstance().initIdianDepthsLocations();
/*     */ 
/* 293 */     TowerOfEternityService.getInstance().initTowerOfEternity();
/* 294 */     TowerOfEternityService.getInstance().initTowerOfEternityLocation();
/*     */ 
/* 296 */     AbyssLandingService.getInstance().initLandingLocations();
/* 297 */     LandingUpdateService.getInstance().initResetQuestPoints();
/* 298 */     LandingUpdateService.getInstance().initResetAbyssLandingPoints();
/* 299 */     AbyssLandingSpecialService.getInstance().initLandingSpecialLocations();
/*     */ 
/* 307 */     Util.printSection(LanguageHandler.translate(CustomMessageId.SPAWN, new Object[0]));
/* 308 */     SpawnEngine.spawnAll();
/*     */ 
/* 310 */     if (EventsConfig.ENABLE_EVENT_SERVICE) {
/* 311 */       EventService.getInstance().start();
/*     */     }
/* 313 */     if (EventsConfig.EVENT_ENABLED) {
/* 314 */       PlayerEventService.getInstance();
/*     */     }
/* 316 */     if (EventsConfig.ENABLE_CRAZY) {
/* 317 */       CrazyDaevaService.getInstance().startTimer();
/*     */     }
/* 319 */     if (RankingConfig.TOP_RANKING_UPDATE_SETTING)
/* 320 */       AbyssRankUpdateService.getInstance().scheduleUpdateHour();
/*     */     else {
/* 322 */       AbyssRankUpdateService.getInstance().scheduleUpdateMinute();
/*     */     }
/*     */ 
/* 325 */     AbyssRankUpdateService.getInstance().initRewardWeeklyManager();
/*     */ 
/* 330 */     GarbageCollector.getInstance().start();
/*     */ 
/* 332 */     PacketBroadcaster.getInstance();
/*     */ 
/* 334 */     TemporarySpawnEngine.spawnAll();
/*     */ 
/* 339 */     Util.printSection("初始化缓存");
/* 340 */     DatabaseCleaningService.getInstance();
/* 341 */     AbyssRankCleaningService.getInstance();
/*     */ 
/* 346 */     Util.printSection("预定服务");
/* 347 */     if (EventsConfig.ENABLE_PIG_POPPY_EVENT) {
/* 348 */       PigPoppyEventService.ScheduleCron();
/*     */     }
/* 350 */     if (EventsConfig.ENABLE_ABYSS_EVENT) {
/* 351 */       TreasureAbyssService.ScheduleCron();
/*     */     }
/* 353 */     if (EventsConfig.IMPERIAL_TOMB_ENABLE) {
/* 354 */       ShugoImperialTombSpawnManager.getInstance().start();
/*     */     }
/*     */ 
/* 360 */     Util.printSection("自定义活动");
/* 361 */     if (FFAConfig.FFA_ENABLED) {
/* 362 */       FFAService.getInstance();
/*     */     }
/* 364 */     if (PvPModConfig.BG_ENABLED) {
/* 365 */       LadderService.getInstance();
/* 366 */       BGService.getInstance();
/*     */     }
/* 368 */     BanditService.getInstance().onInit();
/*     */ 
/* 373 */     Util.printSection("要塞系统");
/* 374 */     SiegeService.getInstance().initSieges();
/* 375 */     BaseService.getInstance().initBases();
/*     */ 
/* 380 */     Util.printSection("德雷得奇安");
/* 381 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 382 */       DredgionService2.getInstance().initDredgion();
/*     */     }
/* 384 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 385 */       AsyunatarService.getInstance().initAsyunatar();
/*     */     }
/*     */ 
/* 391 */     Util.printSection("初始化战场");
/* 392 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 393 */       KamarBattlefieldService.getInstance().initKamarBattlefield();
/*     */     }
/* 395 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 396 */       EngulfedOphidanBridgeService.getInstance().initEngulfedOphidan();
/*     */     }
/* 398 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 399 */       SuspiciousOphidanBridgeService.getInstance().initSuspiciousOphidan();
/*     */     }
/* 401 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 402 */       IronWallWarfrontService.getInstance().initIronWallWarfront();
/*     */     }
/* 404 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 405 */       IdgelDomeService.getInstance().initIdgelDome();
/*     */     }
/* 407 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 408 */       IdgelDomeLandmarkService.getInstance().initLandmark();
/*     */     }
/* 410 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 411 */       HallOfTenacityService.getInstance().initHallOfTenacity();
/*     */     }
/* 413 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 414 */       GrandArenaTrainingCampService.getInstance().initGrandArenaTrainingCamp();
/*     */     }
/* 416 */     if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
/* 417 */       IDRunService.getInstance().initIDRun();
/*     */     }
/*     */ 
/* 424 */     ProtectorConquerorService.getInstance().initSystem();
/*     */ 
/* 430 */     DisputeLandService.getInstance().initDisputeLand();
/* 431 */     OutpostService.getInstance().initOutposts();
/*     */ 
/* 437 */     HTMLCache.getInstance();
/*     */ 
/* 439 */     if (CustomConfig.ENABLE_REWARD_SERVICE) {
/* 440 */       RewardService.getInstance();
/*     */     }
/* 442 */     if (WeddingsConfig.WEDDINGS_ENABLE) {
/* 443 */       WeddingService.getInstance();
/*     */     }
/*     */ 
/* 450 */     PeriodicSaveService.getInstance();
/* 451 */     AdminService.getInstance();
/* 452 */     PlayerTransferService.getInstance();
/* 453 */     TerritoryService.getInstance().initTerritory();
/* 454 */     GameTimeService.getInstance();
/* 455 */     AnnouncementService.getInstance();
/* 456 */     DebugService.getInstance();
/* 457 */     WeatherService.getInstance();
/* 458 */     BrokerService.getInstance();
/* 459 */     Influence.getInstance();
/* 460 */     ExchangeService.getInstance();
/* 461 */     PetitionService.getInstance();
/* 462 */     InstanceService.load();
/* 463 */     FlyRingService.getInstance();
/* 464 */     CuringZoneService.getInstance();
/* 465 */     SpringZoneService.getInstance();
/* 466 */     BoostEventService.getInstance().onStart();
/* 467 */     TaskManagerFromDB.getInstance();
/*     */ 
/* 469 */     GameTimeManager.startClock();
/*     */ 
/* 471 */     if (CustomConfig.LIMITS_ENABLED) {
/* 472 */       PlayerLimitService.getInstance().scheduleUpdate();
/*     */     }
/* 474 */     if (AIConfig.SHOUTS_ENABLE) {
/* 475 */       NpcShoutsService.getInstance();
/*     */     }
/* 477 */     if (SiegeConfig.SIEGE_SHIELD_ENABLED) {
/* 478 */       ShieldService.getInstance().spawnAll();
/*     */     }
/*     */ 
/* 485 */     SeasonRankingUpdateService.getInstance().onStart();
/*     */ 
/* 491 */     Util.printSection(LanguageHandler.translate(CustomMessageId.HOUSING_LOADING, new Object[0]));
/* 492 */     HousingBidService.getInstance().start();
/* 493 */     MaintenanceTask.getInstance();
/* 494 */     TownService.getInstance();
/* 495 */     ChallengeTaskService.getInstance();
/*     */ 
/* 501 */     Util.printSection(LanguageHandler.translate(CustomMessageId.SYSINFO, new Object[0]));
/* 502 */     System.gc();
/* 503 */     AEVersions.printFullVersionInfo();
/* 504 */     AEInfos.printAllInfos();
/*     */ 
/* 506 */     IP.print();
/*     */ 
/* 515 */     gs.startServers();
/* 516 */     Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());
/* 517 */     ZCXInfo.checkForRatioLimitation();
/* 518 */     onStartup();
/*     */   }
/*     */ 
/*     */   private void startServers()
/*     */   {
/* 526 */     Util.printSection(LanguageHandler.translate(CustomMessageId.CONFIG_NETWORK, new Object[0]));
/* 527 */     NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_WRITE_THREADS, new ServerCfg[] { new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "Game Connections", new GameConnectionFactoryImpl()) });
/* 528 */     BannedMacManager.getInstance();
/*     */ 
/* 530 */     LoginServer ls = LoginServer.getInstance();
/* 531 */     ChatServer cs = ChatServer.getInstance();
/*     */ 
/* 533 */     ls.setNioServer(nioServer);
/* 534 */     cs.setNioServer(nioServer);
/*     */ 
/* 537 */     nioServer.connect();
/* 538 */     System.out.println("");
/* 539 */     ls.connect();
/*     */ 
/* 541 */     if (GSConfig.ENABLE_CHAT_SERVER) {
/* 542 */       cs.connect();
/*     */     }
/*     */ 
/* 545 */     Util.printSection(LanguageHandler.translate(CustomMessageId.SYSLOG, new Object[0]));
/*     */   }
/*     */ 
/*     */   private static void initUtilityServicesAndConfig()
/*     */   {
/* 553 */     Config.load();
/*     */ 
/* 555 */     LanguageHandler.getInstance();
/*     */ 
/* 558 */     Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
/*     */ 
/* 560 */     if (JavaAgentUtils.isConfigured())
/*     */     {
/* 562 */       log.info(LanguageHandler.translate(CustomMessageId.ZONE_INFO, new Object[0]));
/*     */     }
/*     */ 
/* 565 */     CronService.initSingleton(ThreadPoolManagerRunnableRunner.class);
/*     */ 
/* 567 */     Util.printSection(LanguageHandler.translate(CustomMessageId.CONFIG, new Object[0]));
/*     */ 
/* 569 */     DateTimeUtil.init();
/*     */ 
/* 571 */     Util.printSection(LanguageHandler.translate(CustomMessageId.DATABASE, new Object[0]));
/* 572 */     DatabaseFactory.init();
/*     */ 
/* 574 */     DAOManager.init();
/*     */ 
/* 576 */     NetServers.init();
/*     */ 
/* 580 */     Util.printSection(LanguageHandler.translate(CustomMessageId.POOLS, new Object[0]));
/* 581 */     ThreadConfig.load();
/* 582 */     ThreadPoolManager.getInstance();
/*     */   }
/*     */ 
/*     */   public static synchronized void addStartupHook(StartupHook hook)
/*     */   {
/* 588 */     if (startUpHooks != null) {
/* 589 */       startUpHooks.add(hook);
/*     */     }
/*     */     else
/* 592 */       hook.onStartup();
/*     */   }
/*     */ 
/*     */   private static synchronized void onStartup() {
/* 596 */     Set startupHooks = startUpHooks;
/*     */ 
/* 598 */     startUpHooks = null;
/*     */ 
/* 600 */     for (StartupHook hook : startupHooks)
/* 601 */       hook.onStartup();
/*     */   }
/*     */ 
/*     */   public static abstract interface StartupHook
/*     */   {
/*     */     public abstract void onStartup();
/*     */   }
/*     */ }