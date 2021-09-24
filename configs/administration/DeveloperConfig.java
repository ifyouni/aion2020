package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

public class DeveloperConfig
{

  @Property(key="gameserver.developer.spawn.enable", defaultValue="true")
  public static boolean SPAWN_ENABLE;

  @Property(key="gameserver.developer.spawn.check", defaultValue="false")
  public static boolean SPAWN_CHECK;

  @Property(key="gameserver.developer.itemstat.id", defaultValue="0")
  public static int ITEM_STAT_ID;

  @Property(key="gameserver.developer.showpackets.enable", defaultValue="false")
  public static boolean SHOW_PACKETS;

  @Property(key="gameserver.developer.show.packetnames.inchat.enable", defaultValue="false")
  public static boolean SHOW_PACKET_NAMES_INCHAT;

  @Property(key="gameserver.developer.show.packetbytes.inchat.enable", defaultValue="false")
  public static boolean SHOW_PACKET_BYTES_INCHAT;

  @Property(key="gameserver.developer.show.packetbytes.inchat.total", defaultValue="200")
  public static int TOTAL_PACKET_BYTES_INCHAT;

  @Property(key="gameserver.developer.filter.packets.inchat", defaultValue="*")
  public static String FILTERED_PACKETS_INCHAT;

  @Property(key="gameserver.developer.show.packets.inchat.accesslevel", defaultValue="6")
  public static int SHOW_PACKETS_INCHAT_ACCESSLEVEL;
}