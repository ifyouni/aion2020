package com.aionemu.gameserver.utils.collections.cachemap;

public abstract interface CacheMap<K, V>
{
  public abstract void put(K paramK, V paramV);

  public abstract V get(K paramK);

  public abstract boolean contains(K paramK);

  public abstract void remove(K paramK);
}