package com.aionemu.gameserver.ai2.poll;

public abstract interface AIAnswer
{
  public abstract boolean isPositive();

  public abstract Object getResult();
}