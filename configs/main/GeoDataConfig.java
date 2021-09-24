package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class GeoDataConfig
{

  @Property(key="gameserver.geodata.enable", defaultValue="false")
  public static boolean GEO_ENABLE;

  @Property(key="gameserver.geodata.cansee.enable", defaultValue="true")
  public static boolean CANSEE_ENABLE;

  @Property(key="gameserver.geodata.fear.enable", defaultValue="true")
  public static boolean FEAR_ENABLE;

  @Property(key="gameserver.geo.npc.move", defaultValue="false")
  public static boolean GEO_NPC_MOVE;

  @Property(key="gameserver.geo.npc.aggro", defaultValue="false")
  public static boolean GEO_NPC_AGGRO;

  @Property(key="gameserver.geo.materials.enable", defaultValue="false")
  public static boolean GEO_MATERIALS_ENABLE;

  @Property(key="gameserver.geo.materials.showdetails", defaultValue="false")
  public static boolean GEO_MATERIALS_SHOWDETAILS;

  @Property(key="gameserver.geo.shields.enable", defaultValue="false")
  public static boolean GEO_SHIELDS_ENABLE;

  @Property(key="gameserver.geo.doors.enable", defaultValue="false")
  public static boolean GEO_DOORS_ENABLE;

  @Property(key="gameserver.geodata.objectfactory.enabled", defaultValue="true")
  public static boolean GEO_OBJECT_FACTORY_ENABLE;
}