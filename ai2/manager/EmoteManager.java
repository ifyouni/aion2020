/*    */ package com.aionemu.gameserver.ai2.manager;
/*    */ 
/*    */ import com.aionemu.gameserver.model.EmotionType;
/*    */ import com.aionemu.gameserver.model.gameobjects.Creature;
/*    */ import com.aionemu.gameserver.model.gameobjects.Npc;
/*    */ import com.aionemu.gameserver.model.gameobjects.player.Player;
/*    */ import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
/*    */ import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
/*    */ import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
/*    */ import com.aionemu.gameserver.utils.PacketSendUtility;
/*    */ 
/*    */ public class EmoteManager
/*    */ {
/*    */   public static final void emoteStartAttacking(Npc owner)
/*    */   {
/* 15 */     Creature target = (Creature)owner.getTarget();
/* 16 */     owner.unsetState(CreatureState.WALKING);
/* 17 */     if (!(owner.isInState(CreatureState.WEAPON_EQUIPPED))) {
/* 18 */       owner.setState(CreatureState.WEAPON_EQUIPPED);
/* 19 */       PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, target.getObjectId().intValue()));
/* 20 */       PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.ATTACKMODE, 0, target.getObjectId().intValue()));
/*    */     }
/*    */   }
/*    */ 
/*    */   public static final void emoteStopAttacking(Npc owner) {
/* 25 */     owner.unsetState(CreatureState.WEAPON_EQUIPPED);
/* 26 */     if ((owner.getTarget() != null) && (owner.getTarget() instanceof Player))
/* 27 */       PacketSendUtility.sendPacket((Player)owner.getTarget(), SM_SYSTEM_MESSAGE.STR_UI_COMBAT_NPC_RETURN(owner.getObjectTemplate().getNameId()));
/*    */   }
/*    */ 
/*    */   public static final void emoteStartFollowing(Npc owner)
/*    */   {
/* 32 */     owner.unsetState(CreatureState.WALKING);
/* 33 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
/* 34 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
/*    */   }
/*    */ 
/*    */   public static final void emoteStartWalking(Npc owner) {
/* 38 */     owner.setState(CreatureState.WALKING);
/* 39 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.WALK));
/*    */   }
/*    */ 
/*    */   public static final void emoteStopWalking(Npc owner) {
/* 43 */     owner.unsetState(CreatureState.WALKING);
/*    */   }
/*    */ 
/*    */   public static final void emoteStartReturning(Npc owner) {
/* 47 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
/* 48 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
/*    */   }
/*    */ 
/*    */   public static final void emoteStartIdling(Npc owner) {
/* 52 */     owner.setState(CreatureState.WALKING);
/* 53 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
/* 54 */     PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
/*    */   }
/*    */ }