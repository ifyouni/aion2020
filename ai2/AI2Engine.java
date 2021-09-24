/*     */ package com.aionemu.gameserver.ai2;
/*     */ 
/*     */ import ch.lambdaj.Lambda;
/*     */ import ch.lambdaj.collection.LambdaCollection;
/*     */ import ch.lambdaj.collection.LambdaCollections;
/*     */ import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
/*     */ import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
/*     */ import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
/*     */ import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
/*     */ import com.aionemu.gameserver.configs.main.AIConfig;
/*     */ import com.aionemu.gameserver.dataholders.DataManager;
/*     */ import com.aionemu.gameserver.dataholders.NpcData;
/*     */ import com.aionemu.gameserver.model.GameEngine;
/*     */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*     */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*     */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import java.io.File;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class AI2Engine
/*     */   implements GameEngine
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(AI2Engine.class);
/*  44 */   private static ScriptManager scriptManager = new ScriptManager();
/*  45 */   public static final File INSTANCE_DESCRIPTOR_FILE = new File("./data/scripts/system/aihandlers.xml");
/*     */   private final Map<String, Class<? extends AbstractAI>> aiMap;
/*     */ 
/*     */   public AI2Engine()
/*     */   {
/*  47 */     this.aiMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public void load(CountDownLatch progressLatch) {
/*  51 */     log.info("启动AI2引擎");
/*  52 */     scriptManager = new ScriptManager();
/*     */ 
/*  54 */     AggregatedClassListener acl = new AggregatedClassListener();
/*  55 */     acl.addClassListener(new OnClassLoadUnloadListener());
/*  56 */     acl.addClassListener(new ScheduledTaskClassListener());
/*  57 */     acl.addClassListener(new AI2HandlerClassListener());
/*  58 */     scriptManager.setGlobalClassListener(acl);
/*     */     try
/*     */     {
/*  61 */       scriptManager.load(INSTANCE_DESCRIPTOR_FILE);
/*  62 */       log.info("加载 " + this.aiMap.size() + " 个ai.");
/*  63 */       validateScripts();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */     finally {
/*  69 */       if (progressLatch != null)
/*  70 */         progressLatch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/*  76 */     log.info("AI2引擎关机启动");
/*  77 */     scriptManager.shutdown();
/*  78 */     scriptManager = null;
/*  79 */     this.aiMap.clear();
/*  80 */     log.info("AI2发动机关机完成");
/*     */   }
/*     */ 
/*     */   public void registerAI(Class<? extends AbstractAI> class1) {
/*  84 */     AIName nameAnnotation = (AIName)class1.getAnnotation(AIName.class);
/*  85 */     if (nameAnnotation != null)
/*  86 */       this.aiMap.put(nameAnnotation.value(), class1);
/*     */   }
/*     */ 
/*     */   public final AI2 setupAI(String name, Creature owner)
/*     */   {
/*  91 */     AbstractAI aiInstance = null;
/*     */     try {
/*  93 */       aiInstance = (AbstractAI)((Class)this.aiMap.get(name)).newInstance();
/*  94 */       aiInstance.setOwner(owner);
/*  95 */       owner.setAi2(aiInstance);
/*  96 */       if (AIConfig.ONCREATE_DEBUG)
/*  97 */         aiInstance.setLogging(true);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 101 */       log.error("[AI2] AI factory error: " + name, e);
/*     */     }
/* 103 */     return aiInstance;
/*     */   }
/*     */ 
/*     */   public void setupAI(AiNames aiName, Npc owner)
/*     */   {
/* 111 */     setupAI(aiName.getName(), owner);
/*     */   }
/*     */ 
/*     */   private void validateScripts() {
/* 115 */     Collection npcAINames = Lambda.selectDistinct(LambdaCollections.with(DataManager.NPC_DATA.getNpcData().valueCollection()).extract(((NpcTemplate)Lambda.on(NpcTemplate.class)).getAi()));
/* 116 */     npcAINames.removeAll(this.aiMap.keySet());
/* 117 */     if (npcAINames.size() > 0)
/* 118 */       log.warn("Bad AI names: " + Lambda.join(npcAINames));
/*     */   }
/*     */ 
/*     */   public static final AI2Engine getInstance()
/*     */   {
/* 123 */     return SingletonHolder.instance;
/*     */   }
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/* 129 */     protected static final AI2Engine instance = new AI2Engine();
/*     */   }
/*     */ }