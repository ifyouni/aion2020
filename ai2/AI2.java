package com.aionemu.gameserver.ai2;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;

public abstract interface AI2
{
  public abstract void onCreatureEvent(AIEventType paramAIEventType, Creature paramCreature);

  public abstract void onCustomEvent(int paramInt, Object[] paramArrayOfObject);

  public abstract void onGeneralEvent(AIEventType paramAIEventType);

  public abstract boolean onDialogSelect(Player paramPlayer, int paramInt1, int paramInt2, int paramInt3);

  public abstract void think();

  public abstract boolean canThink();

  public abstract AIState getState();

  public abstract AISubState getSubState();

  public abstract String getName();

  public abstract boolean poll(AIQuestion paramAIQuestion);

  public abstract AIAnswer ask(AIQuestion paramAIQuestion);

  public abstract boolean isLogging();

  public abstract long getRemainigTime();

  public abstract int modifyDamage(int paramInt);

  public abstract int modifyOwnerDamage(int paramInt);

  public abstract void onIndividualNpcEvent(Creature paramCreature);

  public abstract int modifyHealValue(int paramInt);

  public abstract int modifyMaccuracy(int paramInt);

  public abstract int modifySensoryRange(int paramInt);

  public abstract ItemAttackType modifyAttackType(ItemAttackType paramItemAttackType);
}