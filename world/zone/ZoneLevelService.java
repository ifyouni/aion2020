/*    */ package com.aionemu.gameserver.world.zone;
/*    */ 
/*    */ import com.aionemu.gameserver.controllers.PlayerController;
/*    */ import com.aionemu.gameserver.model.TaskId;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
/*    */ import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
/*    */ import com.aionemu.gameserver.utils.ThreadPoolManager;
/*    */ import com.aionemu.gameserver.world.World;
/*    */ import com.aionemu.gameserver.world.WorldMap;
/*    */ 
/*    */ public class ZoneLevelService
/*    */ {
/*    */   private static final long DROWN_PERIOD = 2000L;
/*    */ 
/*    */   public static void checkZoneLevels(Player player)
/*    */   {
/* 31 */     World world = World.getInstance();
/* 32 */     float z = player.getZ();
/*    */ 
/* 34 */     if (player.getLifeStats().isAlreadyDead()) {
/* 35 */       return;
/*    */     }
/* 37 */     if (z < world.getWorldMap(player.getWorldId()).getDeathLevel()) {
/* 38 */       player.getController().die();
/* 39 */       return;
/*    */     }
/*    */ 
/* 43 */     float playerheight = player.getPlayerAppearance().getHeight() * 1.6F;
/* 44 */     if (z < world.getWorldMap(player.getWorldId()).getWaterLevel() - playerheight)
/* 45 */       startDrowning(player);
/*    */     else
/* 47 */       stopDrowning(player);
/*    */   }
/*    */ 
/*    */   private static void startDrowning(Player player)
/*    */   {
/* 54 */     if (!(isDrowning(player)))
/* 55 */       scheduleDrowningTask(player);
/*    */   }
/*    */ 
/*    */   private static void stopDrowning(Player player)
/*    */   {
/* 62 */     if (isDrowning(player))
/* 63 */       player.getController().cancelTask(TaskId.DROWN);
/*    */   }
/*    */ 
/*    */   private static boolean isDrowning(Player player)
/*    */   {
/* 72 */     return (player.getController().getTask(TaskId.DROWN) != null);
/*    */   }
/*    */ 
/*    */   private static void scheduleDrowningTask(Player player)
/*    */   {
/* 79 */     player.getController().addTask(TaskId.DROWN, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(player)
/*    */     {
/*    */       public void run()
/*    */       {
/* 83 */         int value = Math.round(this.val$player.getLifeStats().getMaxHp() / 10);
/*    */ 
/* 85 */         if (!(this.val$player.getLifeStats().isAlreadyDead())) {
/* 86 */           if (!(this.val$player.isInvul())) {
/* 87 */             this.val$player.getLifeStats().reduceHp(value, this.val$player);
/* 88 */             this.val$player.getLifeStats().sendHpPacketUpdate();
/*    */           }
/*    */         }
/*    */         else
/* 92 */           ZoneLevelService.access$000(this.val$player);
/*    */       }
/*    */     }
/*    */     , 0L, 2000L));
/*    */   }
/*    */ }