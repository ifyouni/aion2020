package com.aionemu.gameserver.ai2;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public abstract class AI2Request
{
  public abstract void acceptRequest(Creature paramCreature, Player paramPlayer);

  public void denyRequest(Creature requester, Player responder)
  {
  }
}