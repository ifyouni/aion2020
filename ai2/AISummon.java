/*    */ package com.aionemu.gameserver.ai2;
/*    */ 
/*    */ import com.aionemu.gameserver.controllers.SummonController;
/*    */ import com.aionemu.gameserver.controllers.movement.SummonMoveController;
/*    */ import com.aionemu.gameserver.model.Race;
/*    */ import com.aionemu.gameserver.model.gameobjects.Summon;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
/*    */ 
/*    */ @AIName("summon")
/*    */ public class AISummon extends AITemplate
/*    */ {
/*    */   public Summon getOwner()
/*    */   {
/* 16 */     return ((Summon)super.getOwner());
/*    */   }
/*    */ 
/*    */   protected NpcTemplate getObjectTemplate() {
/* 20 */     return getOwner().getObjectTemplate();
/*    */   }
/*    */ 
/*    */   protected SpawnTemplate getSpawnTemplate() {
/* 24 */     return getOwner().getSpawn();
/*    */   }
/*    */ 
/*    */   protected Race getRace() {
/* 28 */     return getOwner().getRace();
/*    */   }
/*    */ 
/*    */   protected Player getMaster() {
/* 32 */     return getOwner().getMaster();
/*    */   }
/*    */ 
/*    */   protected SummonMoveController getMoveController() {
/* 36 */     return getOwner().getMoveController();
/*    */   }
/*    */ 
/*    */   protected SummonController getController() {
/* 40 */     return getOwner().getController();
/*    */   }
/*    */ }