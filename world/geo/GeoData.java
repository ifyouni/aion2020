package com.aionemu.gameserver.world.geo;

import com.aionemu.gameserver.geoEngine.models.GeoMap;

public abstract interface GeoData
{
  public abstract void loadGeoMaps();

  public abstract GeoMap getMap(int paramInt);
}