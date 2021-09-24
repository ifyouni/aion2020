package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class CacheConfig
{

  @Property(key="gameserver.cache.softcache", defaultValue="false")
  public static boolean SOFT_CACHE_MAP;

  @Property(key="gameserver.cache.players", defaultValue="false")
  public static boolean CACHE_PLAYERS;

  @Property(key="gameserver.cache.pcd", defaultValue="false")
  public static boolean CACHE_COMMONDATA;

  @Property(key="gameserver.cache.accounts", defaultValue="false")
  public static boolean CACHE_ACCOUNTS;
}