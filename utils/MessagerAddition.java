/*     */ package com.aionemu.gameserver.utils;
/*     */ 
/*     */ import com.aionemu.commons.network.util.ThreadPoolManager;
/*     */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*     */ import com.aionemu.gameserver.world.World;
/*     */ import com.aionemu.gameserver.world.knownlist.Visitor;
/*     */ 
/*     */ public class MessagerAddition
/*     */ {
/*     */   protected void DEEPINSIDE()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void announce(Player player, String msg)
/*     */   {
/*  30 */     PacketSendUtility.sendBrightYellowMessageOnCenter(player, msg);
/*     */   }
/*     */ 
/*     */   public static void message(Player player, String msg) {
/*  34 */     PacketSendUtility.sendMessage(player, msg);
/*     */   }
/*     */ 
/*     */   public static void whiteMsg(Player player, String msg) {
/*  38 */     PacketSendUtility.sendWhiteMessage(player, msg);
/*     */   }
/*     */ 
/*     */   public static void whiteMsgOnCtr(Player player, String msg) {
/*  42 */     PacketSendUtility.sendWhiteMessageOnCenter(player, msg);
/*     */   }
/*     */ 
/*     */   public static void yellowMsg(Player player, String msg) {
/*  46 */     PacketSendUtility.sendYellowMessage(player, msg);
/*     */   }
/*     */ 
/*     */   public static void yellowMsgOnCtr(Player player, String msg) {
/*  50 */     PacketSendUtility.sendYellowMessageOnCenter(player, msg);
/*     */   }
/*     */ 
/*     */   public static void announceAll(String msg, int delay) {
/*  54 */     if (delay > 0) {
/*  55 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/*  58 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/*  61 */               PacketSendUtility.sendBrightYellowMessageOnCenter(sender, MessagerAddition.1.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/*  68 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/*  71 */           PacketSendUtility.sendBrightYellowMessageOnCenter(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void messageToAll(String msg, int delay)
/*     */   {
/*  79 */     if (delay > 0) {
/*  80 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/*  83 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/*  86 */               PacketSendUtility.sendMessage(sender, MessagerAddition.3.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/*  93 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/*  96 */           PacketSendUtility.sendMessage(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void whiteMsgToAll(String msg, int delay)
/*     */   {
/* 104 */     if (delay > 0) {
/* 105 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/* 108 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/* 111 */               PacketSendUtility.sendWhiteMessage(sender, MessagerAddition.5.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/* 118 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/* 121 */           PacketSendUtility.sendWhiteMessage(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void whiteAnnounceToAll(String msg, int delay)
/*     */   {
/* 129 */     if (delay > 0) {
/* 130 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/* 133 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/* 136 */               PacketSendUtility.sendWhiteMessageOnCenter(sender, MessagerAddition.7.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/* 143 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/* 146 */           PacketSendUtility.sendWhiteMessageOnCenter(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void yellowMsgToAll(String msg, int delay)
/*     */   {
/* 154 */     if (delay > 0) {
/* 155 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/* 158 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/* 161 */               PacketSendUtility.sendYellowMessage(sender, MessagerAddition.9.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/* 168 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/* 171 */           PacketSendUtility.sendYellowMessage(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void yellowAnnounceToAll(String msg, int delay)
/*     */   {
/* 179 */     if (delay > 0) {
/* 180 */       ThreadPoolManager.getInstance().schedule(new Runnable(msg)
/*     */       {
/*     */         public void run() {
/* 183 */           World.getInstance().doOnAllPlayers(new Visitor()
/*     */           {
/*     */             public void visit(Player sender) {
/* 186 */               PacketSendUtility.sendYellowMessageOnCenter(sender, MessagerAddition.11.this.val$msg);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */       , delay);
/*     */     }
/*     */     else
/*     */     {
/* 193 */       World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */       {
/*     */         public void visit(Player sender) {
/* 196 */           PacketSendUtility.sendYellowMessageOnCenter(sender, this.val$msg);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void global(String msg)
/*     */   {
/* 204 */     World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */     {
/*     */       public void visit(Player sender) {
/* 207 */         PacketSendUtility.sendBrightYellowMessageOnCenter(sender, "[Global]:" + this.val$msg);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void attention(String msg)
/*     */   {
/* 214 */     World.getInstance().doOnAllPlayers(new Visitor(msg)
/*     */     {
/*     */       public void visit(Player sender) {
/* 217 */         PacketSendUtility.sendBrightYellowMessageOnCenter(sender, "[Attention]:" + this.val$msg);
/*     */       }
/*     */     });
/*     */   }
/*     */ }