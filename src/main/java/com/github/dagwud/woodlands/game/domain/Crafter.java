package com.github.dagwud.woodlands.game.domain;

import java.util.HashMap;
import java.util.Map;

public abstract class Crafter<T extends Item> extends NonPlayerCharacter
{
  private static final long serialVersionUID = 1L;
  private Map<PlayerCharacter, T> busyCrafting;
  private Map<PlayerCharacter, T> readyForCollection;
  
  Crafter(Player ownedBy)
  {
    super(ownedBy);
  }

  public void completeCrafting(PlayerCharacter character)
  {
    T remove = getBusyCrafting().remove(character);
    if (remove != null)
    {
      readyForCollection.put(character, remove);
    }
  }

  public void setBusyCrafting(PlayerCharacter character, T item)
  {
    getBusyCrafting().put(character, item);
  }

  public boolean isBusyCrafting()
  {
    return !getBusyCrafting().isEmpty();
  }

  public Map<PlayerCharacter, T> getBusyCrafting()
  {
    if (busyCrafting == null)
    {
      busyCrafting = new HashMap<>();
    }
    return busyCrafting;
  }

  public T collectFor(PlayerCharacter craftedFor)
  {
    T collect = getReadyForCollection().remove(craftedFor);
    if (collect != null)
    {
      incrementCollectionStat(craftedFor);
    }
    return collect;
  }

  protected abstract void incrementCollectionStat(PlayerCharacter collectedBy);

  private Map<PlayerCharacter, T> getReadyForCollection()
  {
    if (readyForCollection == null)
    {
      readyForCollection = new HashMap<>();
    }
    return readyForCollection;
  }

}
