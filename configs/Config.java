/*     */ package com.aionemu.gameserver.configs;
/*     */ 
/*     */ import com.aionemu.commons.configs.CommonsConfig;
/*     */ import com.aionemu.commons.configs.DatabaseConfig;
/*     */ import com.aionemu.commons.configuration.ConfigurableProcessor;
/*     */ import com.aionemu.commons.utils.PropertiesUtils;
/*     */ import com.aionemu.gameserver.configs.administration.AdminConfig;
/*     */ import com.aionemu.gameserver.configs.administration.DeveloperConfig;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.configs.main.AStationConfig;
/*     */ import com.aionemu.gameserver.configs.main.AbyssLandingConfig;
/*     */ import com.aionemu.gameserver.configs.main.AdvCustomConfig;
/*     */ import com.aionemu.gameserver.configs.main.ArchDaevaConfig;
/*     */ import com.aionemu.gameserver.configs.main.AutoGroupConfig;
/*     */ import com.aionemu.gameserver.configs.main.BrokerConfig;
/*     */ import com.aionemu.gameserver.configs.main.CacheConfig;
/*     */ import com.aionemu.gameserver.configs.main.CleaningConfig;
/*     */ import com.aionemu.gameserver.configs.main.CraftConfig;
/*     */ import com.aionemu.gameserver.configs.main.CustomConfig;
/*     */ import com.aionemu.gameserver.configs.main.DropConfig;
/*     */ import com.aionemu.gameserver.configs.main.EnchantsConfig;
/*     */ import com.aionemu.gameserver.configs.main.EventsConfig;
/*     */ import com.aionemu.gameserver.configs.main.FFAConfig;
/*     */ import com.aionemu.gameserver.configs.main.FallDamageConfig;
/*     */ import com.aionemu.gameserver.configs.main.GSConfig;
/*     */ import com.aionemu.gameserver.configs.main.GeoDataConfig;
/*     */ import com.aionemu.gameserver.configs.main.GroupConfig;
/*     */ import com.aionemu.gameserver.configs.main.HTMLConfig;
/*     */ import com.aionemu.gameserver.configs.main.HousingConfig;
/*     */ import com.aionemu.gameserver.configs.main.InGameShopConfig;
/*     */ import com.aionemu.gameserver.configs.main.LegionConfig;
/*     */ import com.aionemu.gameserver.configs.main.LoggingConfig;
/*     */ import com.aionemu.gameserver.configs.main.MembershipConfig;
/*     */ import com.aionemu.gameserver.configs.main.NameConfig;
/*     */ import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
/*     */ import com.aionemu.gameserver.configs.main.PlayerTransferConfig;
/*     */ import com.aionemu.gameserver.configs.main.PricesConfig;
/*     */ import com.aionemu.gameserver.configs.main.PunishmentConfig;
/*     */ import com.aionemu.gameserver.configs.main.PvPConfig;
/*     */ import com.aionemu.gameserver.configs.main.PvPModConfig;
/*     */ import com.aionemu.gameserver.configs.main.RankingConfig;
/*     */ import com.aionemu.gameserver.configs.main.RateConfig;
/*     */ import com.aionemu.gameserver.configs.main.SecurityConfig;
/*     */ import com.aionemu.gameserver.configs.main.ShutdownConfig;
/*     */ import com.aionemu.gameserver.configs.main.SiegeConfig;
/*     */ import com.aionemu.gameserver.configs.main.ThreadConfig;
/*     */ import com.aionemu.gameserver.configs.main.WeddingsConfig;
/*     */ import com.aionemu.gameserver.configs.main.WorldConfig;
/*     */ import com.aionemu.gameserver.configs.network.IPConfig;
/*     */ import com.aionemu.gameserver.configs.network.NetworkConfig;
/*     */ import java.util.Properties;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class Config
/*     */ {
/*  31 */   protected static final Logger log = LoggerFactory.getLogger(Config.class);
/*     */ 
/*     */   public static void load() {
/*     */     try {
/*  35 */       Properties myProps = null;
/*     */       try {
/*  37 */         log.info("加载: mygs.properties");
/*  38 */         myProps = PropertiesUtils.load("./config/mygs.properties");
/*     */       } catch (Exception e) {
/*  40 */         log.info("没有替代属性");
/*     */       }
/*  42 */       String administration = "./config/administration";
/*  43 */       Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
/*  44 */       PropertiesUtils.overrideProperties(adminProps, myProps);
/*  45 */       ConfigurableProcessor.process(AdminConfig.class, adminProps);
/*  46 */       ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
/*  47 */       String main = "./config/main";
/*  48 */       Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
/*  49 */       PropertiesUtils.overrideProperties(mainProps, myProps);
/*  50 */       ConfigurableProcessor.process(AIConfig.class, mainProps);
/*  51 */       ConfigurableProcessor.process(BrokerConfig.class, mainProps);
/*  52 */       ConfigurableProcessor.process(CommonsConfig.class, mainProps);
/*  53 */       ConfigurableProcessor.process(CacheConfig.class, mainProps);
/*  54 */       ConfigurableProcessor.process(CleaningConfig.class, mainProps);
/*  55 */       ConfigurableProcessor.process(CraftConfig.class, mainProps);
/*  56 */       ConfigurableProcessor.process(CustomConfig.class, mainProps);
/*  57 */       ConfigurableProcessor.process(DropConfig.class, mainProps);
/*  58 */       ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
/*  59 */       ConfigurableProcessor.process(EventsConfig.class, mainProps);
/*  60 */       ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
/*  61 */       ConfigurableProcessor.process(AStationConfig.class, mainProps);
/*  62 */       ConfigurableProcessor.process(GSConfig.class, mainProps);
/*  63 */       ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
/*  64 */       ConfigurableProcessor.process(GroupConfig.class, mainProps);
/*  65 */       ConfigurableProcessor.process(HousingConfig.class, mainProps);
/*  66 */       ConfigurableProcessor.process(HTMLConfig.class, mainProps);
/*  67 */       ConfigurableProcessor.process(InGameShopConfig.class, mainProps);
/*  68 */       ConfigurableProcessor.process(AbyssLandingConfig.class, mainProps);
/*  69 */       ConfigurableProcessor.process(LegionConfig.class, mainProps);
/*  70 */       ConfigurableProcessor.process(LoggingConfig.class, mainProps);
/*  71 */       ConfigurableProcessor.process(MembershipConfig.class, mainProps);
/*  72 */       ConfigurableProcessor.process(NameConfig.class, mainProps);
/*  73 */       ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
/*  74 */       ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
/*  75 */       ConfigurableProcessor.process(PricesConfig.class, mainProps);
/*  76 */       ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
/*  77 */       ConfigurableProcessor.process(PvPConfig.class, mainProps);
/*  78 */       ConfigurableProcessor.process(RankingConfig.class, mainProps);
/*  79 */       ConfigurableProcessor.process(RateConfig.class, mainProps);
/*  80 */       ConfigurableProcessor.process(SecurityConfig.class, mainProps);
/*  81 */       ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
/*  82 */       ConfigurableProcessor.process(SiegeConfig.class, mainProps);
/*  83 */       ConfigurableProcessor.process(ThreadConfig.class, mainProps);
/*  84 */       ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
/*  85 */       ConfigurableProcessor.process(WorldConfig.class, mainProps);
/*  86 */       ConfigurableProcessor.process(AdvCustomConfig.class, mainProps);
/*  87 */       ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
/*  88 */       ConfigurableProcessor.process(PvPModConfig.class, mainProps);
/*  89 */       ConfigurableProcessor.process(FFAConfig.class, mainProps);
/*  90 */       ConfigurableProcessor.process(ArchDaevaConfig.class, mainProps);
/*  91 */       String network = "./config/network";
/*  92 */       Properties[] networkProps = PropertiesUtils.loadAllFromDirectory(network);
/*  93 */       PropertiesUtils.overrideProperties(networkProps, myProps);
/*  94 */       ConfigurableProcessor.process(DatabaseConfig.class, networkProps);
/*  95 */       ConfigurableProcessor.process(NetworkConfig.class, networkProps);
/*     */     } catch (Exception e) {
/*  97 */       log.error("Can't load gameserver configuration: ", e);
/*  98 */       throw new Error("Can't load gameserver configuration: ", e);
/*     */     }
/* 100 */     IPConfig.load();
/*     */   }
/*     */ 
/*     */   public static void reload() {
/*     */     try {
/* 105 */       Properties myProps = null;
/*     */       try {
/* 107 */         log.info("加载: mygs.properties");
/* 108 */         myProps = PropertiesUtils.load("./config/mygs.properties");
/*     */       } catch (Exception e) {
/* 110 */         log.info("没有替代属性");
/*     */       }
/* 112 */       String administration = "./config/administration";
/* 113 */       Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
/* 114 */       PropertiesUtils.overrideProperties(adminProps, myProps);
/* 115 */       ConfigurableProcessor.process(AdminConfig.class, adminProps);
/* 116 */       ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
/* 117 */       String main = "./config/main";
/* 118 */       Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
/* 119 */       PropertiesUtils.overrideProperties(mainProps, myProps);
/* 120 */       ConfigurableProcessor.process(AIConfig.class, mainProps);
/* 121 */       ConfigurableProcessor.process(BrokerConfig.class, mainProps);
/* 122 */       ConfigurableProcessor.process(CommonsConfig.class, mainProps);
/* 123 */       ConfigurableProcessor.process(CacheConfig.class, mainProps);
/* 124 */       ConfigurableProcessor.process(CleaningConfig.class, mainProps);
/* 125 */       ConfigurableProcessor.process(CraftConfig.class, mainProps);
/* 126 */       ConfigurableProcessor.process(CustomConfig.class, mainProps);
/* 127 */       ConfigurableProcessor.process(DropConfig.class, mainProps);
/* 128 */       ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
/* 129 */       ConfigurableProcessor.process(EventsConfig.class, mainProps);
/* 130 */       ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
/* 131 */       ConfigurableProcessor.process(AStationConfig.class, mainProps);
/* 132 */       ConfigurableProcessor.process(GSConfig.class, mainProps);
/* 133 */       ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
/* 134 */       ConfigurableProcessor.process(GroupConfig.class, mainProps);
/* 135 */       ConfigurableProcessor.process(HousingConfig.class, mainProps);
/* 136 */       ConfigurableProcessor.process(HTMLConfig.class, mainProps);
/* 137 */       ConfigurableProcessor.process(InGameShopConfig.class, mainProps);
/* 138 */       ConfigurableProcessor.process(AbyssLandingConfig.class, mainProps);
/* 139 */       ConfigurableProcessor.process(LegionConfig.class, mainProps);
/* 140 */       ConfigurableProcessor.process(LoggingConfig.class, mainProps);
/* 141 */       ConfigurableProcessor.process(MembershipConfig.class, mainProps);
/* 142 */       ConfigurableProcessor.process(NameConfig.class, mainProps);
/* 143 */       ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
/* 144 */       ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
/* 145 */       ConfigurableProcessor.process(PricesConfig.class, mainProps);
/* 146 */       ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
/* 147 */       ConfigurableProcessor.process(PvPConfig.class, mainProps);
/* 148 */       ConfigurableProcessor.process(RankingConfig.class, mainProps);
/* 149 */       ConfigurableProcessor.process(RateConfig.class, mainProps);
/* 150 */       ConfigurableProcessor.process(SecurityConfig.class, mainProps);
/* 151 */       ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
/* 152 */       ConfigurableProcessor.process(SiegeConfig.class, mainProps);
/* 153 */       ConfigurableProcessor.process(ThreadConfig.class, mainProps);
/* 154 */       ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
/* 155 */       ConfigurableProcessor.process(WorldConfig.class, mainProps);
/* 156 */       ConfigurableProcessor.process(AdvCustomConfig.class, mainProps);
/* 157 */       ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
/* 158 */       ConfigurableProcessor.process(PvPModConfig.class, mainProps);
/* 159 */       ConfigurableProcessor.process(FFAConfig.class, mainProps);
/* 160 */       ConfigurableProcessor.process(ArchDaevaConfig.class, mainProps);
/*     */     } catch (Exception e) {
/* 162 */       log.error("无法重新加载配置: ", e);
/* 163 */       throw new Error("无法重新加载配置: ", e);
/*     */     }
/*     */   }
/*     */ }