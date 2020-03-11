package com.github.dagwud.woodlands.game.domain.events;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.commands.character.UnlockAchievementCmd;
import com.github.dagwud.woodlands.game.domain.EAchievement;

public class SparringEventRecipient implements EventRecipient<SparringEvent>
{
  @Override
  public void trigger(SparringEvent event)
  {
    CommandDelegate.execute(new UnlockAchievementCmd(event.getPlayerCharacter(), EAchievement.FISTICUFFS));
    CommandDelegate.execute(new UnlockAchievementCmd(event.getPlayerCharacter(), EAchievement.FISTICUFFS));
  }
}
