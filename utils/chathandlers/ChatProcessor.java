/*     */ package com.aionemu.gameserver.utils.chathandlers;
/*     */ 
/*     */ import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
/*     */ import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
/*     */ import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
/*     */ import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
/*     */ import com.aionemu.commons.utils.PropertiesUtils;
/*     */ import com.aionemu.gameserver.GameServerError;
/*     */ import com.aionemu.gameserver.configs.main.CustomConfig;
/*     */ import com.aionemu.gameserver.model.GameEngine;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javolution.util.FastMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ChatProcessor
/*     */   implements GameEngine
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger("ADMINAUDIT_LOG");
/*  29 */   private static ChatProcessor instance = new ChatProcessor();
/*  30 */   private Map<String, ChatCommand> commands = new FastMap();
/*  31 */   private Map<String, Byte> accessLevel = new FastMap();
/*  32 */   private ScriptManager sm = new ScriptManager();
/*  33 */   private Exception loadException = null;
/*     */ 
/*     */   public static ChatProcessor getInstance() {
/*  36 */     return instance;
/*     */   }
/*     */ 
/*     */   public void load(CountDownLatch progressLatch)
/*     */   {
/*     */     try {
/*  42 */       log.info("Chat processor load started");
/*  43 */       init(this.sm, this);
/*     */     }
/*     */     finally {
/*  46 */       if (progressLatch != null)
/*  47 */         progressLatch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/*     */   }
/*     */ 
/*     */   private ChatProcessor() {
/*     */   }
/*     */ 
/*     */   private ChatProcessor(ScriptManager scriptManager) {
/*  59 */     init(scriptManager, this);
/*     */   }
/*     */ 
/*     */   private void init(ScriptManager scriptManager, ChatProcessor processor) {
/*  63 */     loadLevels();
/*     */ 
/*  65 */     AggregatedClassListener acl = new AggregatedClassListener();
/*  66 */     acl.addClassListener(new OnClassLoadUnloadListener());
/*  67 */     acl.addClassListener(new ScheduledTaskClassListener());
/*  68 */     acl.addClassListener(new ChatCommandsLoader(processor));
/*  69 */     scriptManager.setGlobalClassListener(acl);
/*     */ 
/*  71 */     File[] files = { new File("./data/scripts/system/adminhandlers.xml"), new File("./data/scripts/system/playerhandlers.xml"), new File("./data/scripts/system/weddinghandlers.xml") };
/*     */ 
/*  73 */     CountDownLatch loadLatch = new CountDownLatch(files.length);
/*     */ 
/*  75 */     for (int i = 0; i < files.length; ++i) {
/*  76 */       int index = i;
/*  77 */       ThreadPoolManager.getInstance().execute(new Runnable(scriptManager, files, index, loadLatch)
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/*  82 */             this.val$scriptManager.load(this.val$files[this.val$index]);
/*     */           }
/*     */           catch (Exception e) {
/*  85 */             ChatProcessor.access$002(ChatProcessor.this, e);
/*     */           }
/*     */           finally {
/*  88 */             this.val$loadLatch.countDown();
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     try
/*     */     {
/*  95 */       loadLatch.await();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/*     */     }
/*  99 */     if (this.loadException != null)
/* 100 */       throw new GameServerError("Can't initialize chat handlers.", this.loadException);
/*     */   }
/*     */ 
/*     */   public void registerCommand(ChatCommand cmd)
/*     */   {
/* 105 */     if (this.commands.containsKey(cmd.getAlias())) {
/* 106 */       log.warn("Command " + cmd.getAlias() + " is already registered. Fail");
/* 107 */       return;
/*     */     }
/*     */ 
/* 110 */     if (!(this.accessLevel.containsKey(cmd.getAlias()))) {
/* 111 */       log.warn("Command " + cmd.getAlias() + " do not have access level. Fail");
/* 112 */       return;
/*     */     }
/*     */ 
/* 115 */     cmd.setAccessLevel((Byte)this.accessLevel.get(cmd.getAlias()));
/* 116 */     this.commands.put(cmd.getAlias(), cmd);
/*     */   }
/*     */ 
/*     */   public void reload()
/*     */   {
/*     */     ScriptManager tmpSM;
/*     */     ChatProcessor adminCP;
/* 122 */     Map backupCommands = new FastMap(this.commands);
/* 123 */     this.commands.clear();
/* 124 */     this.loadException = null;
/*     */     try
/*     */     {
/* 127 */       tmpSM = new ScriptManager();
/* 128 */       adminCP = new ChatProcessor(tmpSM);
/*     */     }
/*     */     catch (Throwable e) {
/* 131 */       this.commands = backupCommands;
/* 132 */       throw new GameServerError("Can't reload chat handlers.", e);
/*     */     }
/*     */ 
/* 135 */     if ((tmpSM != null) && (adminCP != null)) {
/* 136 */       backupCommands.clear();
/* 137 */       this.sm.shutdown();
/* 138 */       this.sm = null;
/* 139 */       this.sm = tmpSM;
/* 140 */       instance = adminCP;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadLevels()
/*     */   {
/*     */     Properties props;
/*     */     Iterator i$;
/* 145 */     this.accessLevel.clear();
/*     */     try {
/* 147 */       props = PropertiesUtils.load("config/administration/commands.properties");
/*     */ 
/* 149 */       for (i$ = props.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
/* 150 */         String str = (String)key;
/* 151 */         this.accessLevel.put(str, Byte.valueOf(props.getProperty(str).trim()));
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 155 */       log.error("Can't read commands.properties", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean handleChatCommand(Player player, String text) {
/* 160 */     if (text.split(" ").length == 0)
/* 161 */       return false;
/* 162 */     if (((text.startsWith("//")) && (getCommand(text.substring(2)) instanceof AdminCommand)) || ((text.startsWith("..")) && (getCommand(text.substring(2)) instanceof WeddingCommand)))
/*     */     {
/* 164 */       return getCommand(text.substring(2)).process(player, text.substring(2));
/*     */     }
/* 166 */     if ((text.startsWith(".")) && (((getCommand(text.substring(1)) instanceof PlayerCommand) || ((CustomConfig.ENABLE_ADMIN_DOT_COMMANDS) && (getCommand(text.substring(1)) instanceof AdminCommand)))))
/*     */     {
/* 169 */       return getCommand(text.substring(1)).process(player, text.substring(1));
/*     */     }
/*     */ 
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   private ChatCommand getCommand(String text) {
/* 176 */     String alias = text.split(" ")[0];
/* 177 */     ChatCommand cmd = (ChatCommand)this.commands.get(alias);
/* 178 */     return cmd;
/*     */   }
/*     */ 
/*     */   public void onCompileDone() {
/* 182 */     log.info("Loaded " + this.commands.size() + " commands.");
/*     */   }
/*     */ }