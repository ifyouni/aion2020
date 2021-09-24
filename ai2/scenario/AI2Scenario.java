package com.aionemu.gameserver.ai2.scenario;

import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Creature;

public abstract interface AI2Scenario
{
  public abstract void onCreatureEvent(AbstractAI paramAbstractAI, AIEventType paramAIEventType, Creature paramCreature);

  public abstract void onGeneralEvent(AbstractAI paramAbstractAI, AIEventType paramAIEventType);
}