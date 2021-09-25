package com.aionemu.gameserver.world.zone.handler;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.world.zone.ZoneInstance;

public abstract interface AdvencedZoneHandler extends ZoneHandler
{
  public abstract boolean onDie(Creature paramCreature1, Creature paramCreature2, ZoneInstance paramZoneInstance);
}