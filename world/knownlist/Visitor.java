package com.aionemu.gameserver.world.knownlist;

public abstract interface Visitor<T>
{
  public abstract void visit(T paramT);
}