/*    */ package com.aionemu.gameserver.utils.audit.aion;
/*    */ 
/*    */ public class IpConfig
/*    */ {
/*    */   private String ip;
/*    */   private int qq;
/*    */   private String end_time;
/*    */   private String update_time;
/*    */   private String ad;
/*    */   private int ad_time;
/*    */   private boolean ad_send;
/*    */   private boolean npc_move;
/*    */   private boolean can_marry;
/*    */   private boolean use_shop;
/*    */ 
/*    */   public IpConfig(String ip, int qq, String end_time, String update_time, String ad, int ad_time, boolean ad_send, boolean npc_move, boolean can_marry, boolean use_shop)
/*    */   {
/* 24 */     this.ip = ip;
/* 25 */     this.qq = qq;
/* 26 */     this.end_time = end_time;
/* 27 */     this.update_time = update_time;
/* 28 */     this.ad = ad;
/* 29 */     this.ad_time = ad_time;
/* 30 */     this.ad_send = ad_send;
/* 31 */     this.npc_move = npc_move;
/* 32 */     this.can_marry = can_marry;
/* 33 */     this.use_shop = use_shop;
/*    */   }
/*    */ 
/*    */   public String getIp()
/*    */   {
/* 38 */     return this.ip;
/*    */   }
/*    */ 
/*    */   public int getQQ()
/*    */   {
/* 43 */     return this.qq;
/*    */   }
/*    */ 
/*    */   public String getEndtime()
/*    */   {
/* 48 */     return this.end_time;
/*    */   }
/*    */ 
/*    */   public String getUpdatetime()
/*    */   {
/* 53 */     return this.update_time;
/*    */   }
/*    */ 
/*    */   public String getAD()
/*    */   {
/* 58 */     return this.ad;
/*    */   }
/*    */ 
/*    */   public int getADtime()
/*    */   {
/* 63 */     return this.ad_time;
/*    */   }
/*    */ 
/*    */   public boolean canADsend()
/*    */   {
/* 68 */     return this.ad_send;
/*    */   }
/*    */ 
/*    */   public boolean canNpcMove()
/*    */   {
/* 73 */     return this.npc_move;
/*    */   }
/*    */ 
/*    */   public boolean canMarry()
/*    */   {
/* 78 */     return this.can_marry;
/*    */   }
/*    */ 
/*    */   public boolean canUseShop()
/*    */   {
/* 83 */     return this.use_shop;
/*    */   }
/*    */ }