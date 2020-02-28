package com.github.dagwud.woodlands.game.domain;

import com.github.dagwud.woodlands.gson.game.Weapon;

import java.util.Map;
import java.util.HashMap;

public class Blacksmith extends NonPlayerCharacter
{
  private static final long serialVersionUID = 1L;
  private boolean busyCrafting;
  private Map<PlayerCharacter, Weapon> readyForCollection;

  Blacksmith()
  {
    super(null);
    setName("the Blacksmith");
  }

  public void setBusyCrafting(boolean busyCrafting)
  {
    this.busyCrafting = busyCrafting;
  }

  public boolean isBusyCrafting()
  {
    return busyCrafting;
  }

  public Weapon collectWeaponFor(PlayerCharacter craftedFor)
  {
    if (readyForCollection == null)
    {
      readyForCollection = new HashMap<>();
    }
    return readyForCollection.remove(craftedFor);
  }
}
