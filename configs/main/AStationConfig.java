package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class AStationConfig
{

  @Property(key="a.station.server.id", defaultValue="2")
  public static int A_STATION_SERVER_ID;

  @Property(key="a.station.max.level", defaultValue="65")
  public static int A_STATION_MAX_LEVEL;

  @Property(key="a.station.enable", defaultValue="true")
  public static boolean A_STATION_ENABLE;
}