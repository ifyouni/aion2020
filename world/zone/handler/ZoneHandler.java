package com.aionemu.gameserver.world.zone.handler;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.world.zone.ZoneInstance;

public abstract interface ZoneHandler
{
  public abstract void onEnterZone(Creature paramCreature, ZoneInstance paramZoneInstance);

  public abstract void onLeaveZone(Creature paramCreature, ZoneInstance paramZoneInstance);
}