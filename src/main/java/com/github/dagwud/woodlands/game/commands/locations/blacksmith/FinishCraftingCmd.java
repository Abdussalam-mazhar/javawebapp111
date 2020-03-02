package com.github.dagwud.woodlands.game.commands.locations.blacksmith;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.commands.core.AbstractCmd;
import com.github.dagwud.woodlands.game.commands.core.SendAdminMessageCmd;
import com.github.dagwud.woodlands.game.commands.inventory.DoGiveItemCmd;
import com.github.dagwud.woodlands.game.domain.Blacksmith;
import com.github.dagwud.woodlands.game.domain.PlayerCharacter;
import com.github.dagwud.woodlands.gson.game.Weapon;

public class FinishCraftingCmd extends AbstractCmd
{
  private static final long serialVersionUID = 1L;

  private final Weapon crafted;
  private final PlayerCharacter craftedFor;

  public FinishCraftingCmd(Weapon crafted, PlayerCharacter craftedFor)
  {
    this.crafted = crafted;
    this.craftedFor = craftedFor;
  }

  @Override
  public void execute()
  {
    Blacksmith blacksmith = craftedFor.getParty().getBlacksmith();
    CommandDelegate.execute(new DoGiveItemCmd(null, blacksmith, crafted));
    blacksmith.setBusyCrafting(false);
    blacksmith.addReadyForCollection(crafted, craftedFor);
    CommandDelegate.execute(new SendAdminMessageCmd("Blacksmith has crafted " + crafted.getName() + " for " + craftedFor.getName()));
  }
}
