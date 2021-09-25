/*    */ package com.aionemu.gameserver.utils.i18n;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javolution.util.FastList;
/*    */ import javolution.util.FastMap;
/*    */ 
/*    */ public class Language
/*    */ {
/* 30 */   private final List<String> supportedLanguages = new FastList();
/* 31 */   private final Map<CustomMessageId, String> translatedMessages = new FastMap();
/*    */ 
/*    */   public Language() {
/*    */   }
/*    */ 
/*    */   protected Language(String language) {
/* 37 */     this.supportedLanguages.add(language);
/*    */   }
/*    */ 
/*    */   protected void addSupportedLanguage(String language) {
/* 41 */     this.supportedLanguages.add(language);
/*    */   }
/*    */ 
/*    */   public List<String> getSupportedLanguages() {
/* 45 */     return this.supportedLanguages;
/*    */   }
/*    */ 
/*    */   public String translate(CustomMessageId id, Object[] params) {
/* 49 */     if (this.translatedMessages.containsKey(id)) {
/* 50 */       return String.format((String)this.translatedMessages.get(id), params);
/*    */     }
/*    */ 
/* 53 */     return String.format(id.getFallbackMessage(), params);
/*    */   }
/*    */ 
/*    */   protected void addTranslatedMessage(CustomMessageId id, String message)
/*    */   {
/* 61 */     this.translatedMessages.put(id, message);
/*    */   }
/*    */ }