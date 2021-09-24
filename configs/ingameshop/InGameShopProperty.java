/*    */ package com.aionemu.gameserver.configs.ingameshop;
/*    */ 
/*    */ import com.aionemu.commons.utils.xml.JAXBUtil;
/*    */ import com.aionemu.gameserver.model.templates.ingameshop.IGCategory;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlRootElement(name="in_game_shop")
/*    */ public class InGameShopProperty
/*    */ {
/*    */ 
/*    */   @XmlElement(name="category", required=true)
/*    */   private List<IGCategory> categories;
/*    */ 
/*    */   public List<IGCategory> getCategories()
/*    */   {
/* 23 */     if (this.categories == null) {
/* 24 */       this.categories = new ArrayList();
/*    */     }
/* 26 */     return this.categories;
/*    */   }
/*    */ 
/*    */   public int size() {
/* 30 */     return getCategories().size();
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 34 */     if (this.categories != null)
/* 35 */       this.categories.clear();
/*    */   }
/*    */ 
/*    */   public static InGameShopProperty load() {
/* 39 */     InGameShopProperty ing = null;
/*    */     try {
/* 41 */       String xml = FileUtils.readFileToString(new File("./config/ingameshop/in_game_shop.xml"), "UTF-8");
/* 42 */       ing = (InGameShopProperty)JAXBUtil.deserialize(xml, InGameShopProperty.class);
/*    */     }
/*    */     catch (Exception e) {
/* 45 */       throw new RuntimeException("Failed to initialize ingameshop", e);
/*    */     }
/* 47 */     return ing;
/*    */   }
/*    */ }