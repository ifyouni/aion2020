package com.aionemu.gameserver.world.knownlist;

public abstract interface VisitorWithOwner<T, V>
{
  public abstract void visit(T paramT, V paramV);
}