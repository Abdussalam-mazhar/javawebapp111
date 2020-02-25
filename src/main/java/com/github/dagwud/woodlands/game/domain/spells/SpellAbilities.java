package com.github.dagwud.woodlands.game.domain.spells;

import com.github.dagwud.woodlands.game.domain.characters.spells.*;

import java.io.Serializable;
import java.util.*;

public class SpellAbilities implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Collection<PassiveBattleRoundSpell> passives;
  private Collection<PassivePartySpell> passivePartySpells;
  private List<SingleCastSpell> knownActiveSpell;
  private Deque<SingleCastSpell> preparedSpells;

  public SpellAbilities()
  {
    passives = new ArrayList<>(1);
    passivePartySpells = new ArrayList<>(1);
    preparedSpells = new LinkedList<>();
    knownActiveSpell = new ArrayList<>(2);
  }

  public Collection<PassiveBattleRoundSpell> getPassives()
  {
    return passives;
  }

  public Collection<PassivePartySpell> getPassivePartySpells()
  {
    return passivePartySpells;
  }

  public void register(PassiveSpell spell)
  {
    if (spell instanceof PassivePartySpell)
    {
      passivePartySpells.add((PassivePartySpell) spell);
    }
    else
    {
      passives.add((PassiveBattleRoundSpell) spell);
    }
  }

  public void register(PassivePartySpell spell)
  {
    passivePartySpells.add(spell);
  }

  public void register(SingleCastSpell spell)
  {
    knownActiveSpell.add(spell);
  }

  public void prepare(SingleCastSpell spell, int allowedActions)
  {
    makeSpaceForPreparedSpell(allowedActions);
    preparedSpells.addLast(spell);
  }

  public void makeSpaceForPreparedSpell(int allowedActions)
  {
    while (countPrepared() + 1> allowedActions) // +1 for the one we're going to add
    {
      preparedSpells.removeFirst();
    }
  }

  public boolean hasPreparedSpell()
  {
    return !preparedSpells.isEmpty();
  }

  public List<SingleCastSpell> getKnownActiveSpell()
  {
    return knownActiveSpell;
  }

  public SingleCastSpell popPrepared()
  {
    return preparedSpells.pop();
  }

  public String[] listPrepared()
  {
    List<String> prepared = new ArrayList<>(preparedSpells.size());
    for (SingleCastSpell preparedSpell : preparedSpells)
    {
      prepared.add(preparedSpell.getSpellName());
    }
    return prepared.toArray(new String[0]);
  }

  public int countPrepared()
  {
    return preparedSpells.size();
  }

  public boolean hasKnownSpell(Class<? extends SingleCastSpell> spellClass)
  {
    for (SingleCastSpell knownSpell : getKnownActiveSpell())
    {
      if (knownSpell.getClass() == spellClass)
      {
        return true;
      }
    }
    return false;
  }
}
