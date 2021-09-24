package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class AIConfig
{

  @Property(key="gameserver.ai.move.debug", defaultValue="true")
  public static boolean MOVE_DEBUG;

  @Property(key="gameserver.ai.event.debug", defaultValue="false")
  public static boolean EVENT_DEBUG;

  @Property(key="gameserver.ai.oncreate.debug", defaultValue="false")
  public static boolean ONCREATE_DEBUG;

  @Property(key="gameserver.npcmovement.enable", defaultValue="true")
  public static boolean ACTIVE_NPC_MOVEMENT;

  @Property(key="gameserver.npcmovement.delay.minimum", defaultValue="3")
  public static int MINIMIMUM_DELAY;

  @Property(key="gameserver.npcmovement.delay.maximum", defaultValue="15")
  public static int MAXIMUM_DELAY;

  @Property(key="gameserver.npcshouts.enable", defaultValue="false")
  public static boolean SHOUTS_ENABLE;
}