package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class ArchDaevaConfig
{

  @Property(key="gameserver.max.cp.limit", defaultValue="1000")
  public static int CP_LIMIT_MAX;

  @Property(key="gameserver.item.not.for.highdaeva.enable", defaultValue="false")
  public static boolean ITEM_NOT_FOR_HIGHDAEVA_ENABLE;
}