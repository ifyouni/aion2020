/*     */ package com.aionemu.gameserver.utils.i18n;
/*     */ 
/*     */ import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
/*     */ import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
/*     */ import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
/*     */ import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
/*     */ import com.aionemu.gameserver.GameServerError;
/*     */ import com.aionemu.gameserver.configs.main.GSConfig;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javolution.util.FastMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class LanguageHandler
/*     */ {
/*  40 */   private static final File LANGUAGE_DESCRIPTOR_FILE = new File("./config/languages.xml");
/*  41 */   private static Logger log = LoggerFactory.getLogger(Language.class);
/*  42 */   private Map<String, Language> languages = new FastMap();
/*     */   private Language language;
/*  44 */   private static final LanguageHandler instance = new LanguageHandler();
/*  45 */   private ScriptManager sm = new ScriptManager();
/*     */ 
/*     */   public static final LanguageHandler getInstance() {
/*  48 */     AggregatedClassListener acl = new AggregatedClassListener();
/*  49 */     acl.addClassListener(new OnClassLoadUnloadListener());
/*  50 */     acl.addClassListener(new ScheduledTaskClassListener());
/*  51 */     acl.addClassListener(new LanguagesLoader(instance));
/*  52 */     instance.sm.setGlobalClassListener(acl);
/*     */     try
/*     */     {
/*  55 */       instance.sm.load(LANGUAGE_DESCRIPTOR_FILE);
/*     */     }
/*     */     catch (Exception e) {
/*  58 */       throw new GameServerError("无法加载语言", e);
/*     */     }
/*     */ 
/*  61 */     instance.language = instance.getLanguage(GSConfig.LANG);
/*  62 */     return instance;
/*     */   }
/*     */ 
/*     */   public static String translate(CustomMessageId id, Object[] params)
/*     */   {
/*  69 */     return instance.language.translate(id, params);
/*     */   }
/*     */ 
/*     */   public void registerLanguage(Language language) {
/*  73 */     if (language == null) {
/*  74 */       throw new NullPointerException("Cannot register null Language");
/*     */     }
/*     */ 
/*  77 */     List langs = language.getSupportedLanguages();
/*     */ 
/*  79 */     for (String lang : langs) {
/*  80 */       if (this.languages.containsKey(lang)) {
/*  81 */         log.warn("Overriding language " + lang + " with class " + language.getClass().getName());
/*     */       }
/*     */ 
/*  84 */       this.languages.put(lang, language);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Language getLanguage(String language) {
/*  89 */     if (!(this.languages.containsKey(language))) {
/*  90 */       return new Language();
/*     */     }
/*     */ 
/*  93 */     return ((Language)this.languages.get(language));
/*     */   }
/*     */ 
/*     */   public void clear() {
/*  97 */     this.languages.clear();
/*     */   }
/*     */ 
/*     */   public int size() {
/* 101 */     return this.languages.size();
/*     */   }
/*     */ }