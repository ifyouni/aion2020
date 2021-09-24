package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class GroupConfig
{

  @Property(key="gameserver.playergroup.removetime", defaultValue="600")
  public static int GROUP_REMOVE_TIME;

  @Property(key="gameserver.playergroup.maxdistance", defaultValue="100")
  public static int GROUP_MAX_DISTANCE;

  @Property(key="gameserver.group.inviteotherfaction", defaultValue="false")
  public static boolean GROUP_INVITEOTHERFACTION;

  @Property(key="gameserver.playeralliance.removetime", defaultValue="600")
  public static int ALLIANCE_REMOVE_TIME;

  @Property(key="gameserver.playeralliance.inviteotherfaction", defaultValue="false")
  public static boolean ALLIANCE_INVITEOTHERFACTION;

  @Property(key="gameserver.team2.enable", defaultValue="false")
  public static boolean TEAM2_ENABLE;
}