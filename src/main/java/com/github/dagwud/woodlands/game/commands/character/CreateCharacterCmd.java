package com.github.dagwud.woodlands.game.commands.character;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.commands.core.AbstractCmd;
import com.github.dagwud.woodlands.game.domain.Brawler;
import com.github.dagwud.woodlands.game.domain.ECharacterClass;
import com.github.dagwud.woodlands.game.domain.GameCharacter;
import com.github.dagwud.woodlands.game.domain.Player;

public class CreateCharacterCmd extends AbstractCmd
{
  private final String characterName;
  private final ECharacterClass characterClass;
  private GameCharacter createdCharacter;
  private Player player;

  CreateCharacterCmd(Player player, String characterName, ECharacterClass characterClass)
  {
    this.player = player;
    this.characterName = characterName;
    this.characterClass = characterClass;
  }

  @Override
  public void execute()
  {
    GameCharacter character = GameCharacterFactory.create(characterClass, player);
    character.setName(characterName);

    InitCharacterStatsCmd cmd = new InitCharacterStatsCmd(character);
    CommandDelegate.execute(cmd);
    character.setSetupComplete(true);

    // Join a private party for just this character by default:
    JoinPartyCmd join = new JoinPartyCmd(character, "_" + characterName);
    CommandDelegate.execute(join);

    createdCharacter = character;
  }

  GameCharacter getCreatedCharacter()
  {
    return createdCharacter;
  }
}
