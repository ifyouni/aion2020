/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlEnum;
/*    */ import javax.xml.bind.annotation.XmlEnumValue;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ 
/*    */ @XmlType(name="ZoneAttributes")
/*    */ @XmlEnum(String.class)
/*    */ public enum ZoneAttributes
/*    */ {
/* 23 */   BIND, RECALL, GLIDE, FLY, RIDE, FLY_RIDE, PVP_ENABLED, DUEL_SAME_RACE_ENABLED, DUEL_OTHER_RACE_ENABLED;
/*    */ 
/*    */   private int id;
/*    */ 
/*    */   public int getId()
/*    */   {
/* 50 */     return this.id;
/*    */   }
/*    */ 
/*    */   public static Integer fromList(List<ZoneAttributes> flagValues) {
/* 54 */     Integer result = Integer.valueOf(0);
/* 55 */     for (ZoneAttributes attribute : values()) {
/* 56 */       if (flagValues.contains(attribute))
/* 57 */         result = Integer.valueOf(result.intValue() | attribute.getId());
/*    */     }
/* 59 */     return result;
/*    */   }
/*    */ }