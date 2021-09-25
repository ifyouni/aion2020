package com.aionemu.gameserver.utils.collections;

public abstract interface ICache<K extends Comparable, V>
{
  public abstract V get(K paramK);

  public abstract void put(K paramK, V paramV);

  public abstract void remove(K paramK);

  public abstract CachePair[] getAll();

  public abstract int size();
}