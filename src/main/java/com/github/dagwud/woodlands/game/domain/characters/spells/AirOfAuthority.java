package com.github.dagwud.woodlands.game.domain.characters.spells;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.commands.core.DiceRollCmd;
import com.github.dagwud.woodlands.game.commands.core.SendMessageCmd;
import com.github.dagwud.woodlands.game.domain.ECharacterClass;
import com.github.dagwud.woodlands.game.domain.GameCharacter;
import com.github.dagwud.woodlands.game.domain.characters.General;

import java.util.HashMap;
import java.util.Map;

public class AirOfAuthority extends BattleRoundSpell
{
  private Map<GameCharacter, Integer> buffs;

  public AirOfAuthority(General caster)
  {
    super("Air of Authority", caster);
    buffs = new HashMap<>();
  }

  @Override
  public boolean shouldCast()
  {
    return true;
  }

  @Override
  public void cast()
  {
    for (GameCharacter target : getCaster().getParty().getActiveMembers())
    {
      int buffAmount = rollBuff(target);
      if (buffAmount != 0)
      {
        target.getStats().getStrength().addBonus(buffAmount);
        buffs.put(target, buffAmount);

        SendMessageCmd cmd = new SendMessageCmd(target.getPlayedBy().getChatId(), getCaster().getName() + " buffed your strength by +" + buffAmount);
        CommandDelegate.execute(cmd);
      }
    }
  }

  @Override
  public void expire()
  {
    for (GameCharacter target : buffs.keySet())
    {
      Integer buffedAmount = buffs.get(target);
      target.getStats().getStrength().removeBonus(buffedAmount);
    }
  }

  private int rollBuff(GameCharacter target)
  {
    if (getCaster() == target)
    {
      // Can't buff yourself:
      return 0;
    }

    int diceFaces = determineBuffDiceFaces(target);

    DiceRollCmd roll1 = new DiceRollCmd(1, diceFaces);
    CommandDelegate.execute(roll1);

    DiceRollCmd roll2 = new DiceRollCmd(1, diceFaces);
    CommandDelegate.execute(roll2);

    return Math.min(roll1.getTotal(), roll2.getTotal());
  }

  private int determineBuffDiceFaces(GameCharacter target)
  {
    if (target.getCharacterClass() == ECharacterClass.TRICKSTER)
    {
      return 6;
    }
    if (target.getCharacterClass() == ECharacterClass.DRUID
            || target.getCharacterClass() == ECharacterClass.WIZARD
            || target.getCharacterClass() == ECharacterClass.EXPLORER)
    {
      return 4;
    }
    if (target.getCharacterClass() == ECharacterClass.BRAWLER)
    {
      return 8;
    }
    if (target.getCharacterClass() == ECharacterClass.GENERAL)
    {
      // Generals can't buff each other
      return 0;
    }
    return 0;
  }
}
