package com.github.dagwud.woodlands.game.commands.locations;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.PlayerState;
import com.github.dagwud.woodlands.game.commands.core.SendPartyMessageCmd;
import com.github.dagwud.woodlands.game.commands.core.ShowMenuCmd;
import com.github.dagwud.woodlands.game.commands.core.AbstractCmd;
import com.github.dagwud.woodlands.game.commands.core.SendMessageCmd;
import com.github.dagwud.woodlands.game.commands.locations.mountain.EndEncounterCmd;
import com.github.dagwud.woodlands.game.commands.locations.mountain.EnterTheMountainCmd;
import com.github.dagwud.woodlands.game.domain.ELocation;
import com.github.dagwud.woodlands.game.domain.GameCharacter;
import com.github.dagwud.woodlands.game.domain.Party;

import java.util.HashSet;
import java.util.Set;

public class MoveToLocationCmd extends AbstractCmd
{
  private final GameCharacter characterToMove;
  private final ELocation location;

  public MoveToLocationCmd(GameCharacter characterToMove, ELocation location)
  {
    this.characterToMove = characterToMove;
    this.location = location;
  }

  @Override
  public void execute()
  {
    int chatId = characterToMove.getPlayedBy().getChatId();

    if (!characterToMove.isSetupComplete())
    {
      SendMessageCmd cmd = new SendMessageCmd(chatId,"You need to create a character first. Please use /new");
      CommandDelegate.execute(cmd);
      return;
    }

    if (allMoveTogether(this.location))
    {
      // location requires whole party to move as one:
      if (location != ELocation.VILLAGE_SQUARE && !allAtSameLocation(characterToMove.getParty()))
      {
        SendPartyMessageCmd cmd = new SendPartyMessageCmd(characterToMove.getParty(), "Can't go to " + location + " until all party members are in the same place");
        CommandDelegate.execute(cmd);
        return;
      }
    }

    endActiveEncounter();

    if (allMoveTogether(this.location))
    {
      for (GameCharacter character : characterToMove.getParty().getMembers())
      {
        doMove(character, location);
      }
    }
    else
    {
      // players can move individually:
      doMove(characterToMove, location);
    }
  }

  private boolean allMoveTogether(ELocation moveTo)
  {
    return moveTo == ELocation.MOUNTAIN || moveTo == ELocation.VILLAGE_SQUARE || moveTo == ELocation.WOODLANDS;
  }

  private void doMove(GameCharacter characterToMove, ELocation moveTo)
  {
    characterToMove.setLocation(moveTo);

    SendMessageCmd c = new SendMessageCmd(characterToMove.getPlayedBy().getChatId(), "You are now at " + location);
    CommandDelegate.execute(c);

    showMenuForLocation(moveTo, characterToMove.getPlayedBy().getPlayerState());
    handleLocationEntry(moveTo, characterToMove.getPlayedBy().getPlayerState());
  }

  private void endActiveEncounter()
  {
    if (characterToMove.getParty().getActiveEncounter() != null)
    {
      EndEncounterCmd cmd = new EndEncounterCmd(characterToMove.getParty().getActiveEncounter());
      CommandDelegate.execute(cmd);
    }
  }

  private boolean allAtSameLocation(Party party)
  {
    Set<ELocation> locations = new HashSet<>();
    for (GameCharacter member : party.getMembers())
    {
      locations.add(member.getLocation());
    }
    return locations.size() == 1;
  }

  private void showMenuForLocation(ELocation location, PlayerState playerState)
  {
    ShowMenuCmd cmd = new ShowMenuCmd(location.getMenu(), playerState);
    CommandDelegate.execute(cmd);
  }

  private void handleLocationEntry(ELocation location, PlayerState playerState)
  {
    if (playerState.getActiveCharacter().getParty().isLedBy(playerState.getActiveCharacter()))
    {
      if (location == ELocation.MOUNTAIN)
      {
        EnterTheMountainCmd cmd = new EnterTheMountainCmd(playerState);
        CommandDelegate.execute(cmd);
      }
    }
  }
}
