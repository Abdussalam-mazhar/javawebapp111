package com.github.dagwud.woodlands.game.domain;

import com.github.dagwud.woodlands.game.CommandDelegate;
import com.github.dagwud.woodlands.game.commands.core.SendPartyAlertCmd;
import com.github.dagwud.woodlands.game.commands.core.SendPartyMessageCmd;
import com.github.dagwud.woodlands.game.domain.events.CreatureDroppedEventRecipient;
import com.github.dagwud.woodlands.game.domain.events.CreatureWasMuggedEventRecipient;
import com.github.dagwud.woodlands.game.domain.events.Event;
import com.github.dagwud.woodlands.game.domain.events.EventRecipient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EEvent
{
  PLAYER_DEATH, JOINED_PARTY, LEFT_PARTY, MOVED, CREATURE_DROPPED_ITEM;

  private static transient Map<EEvent, List<EventRecipient<? extends Event>>> subscribers;

  public static void subscribeToStandardEvents()
  {
    EEvent.PLAYER_DEATH.subscribe(event -> CommandDelegate.execute(new SendPartyAlertCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getName() + " has died! Nice job, " + event.getPlayerCharacter().getParty().getLeader().getName())));

    EEvent.JOINED_PARTY.subscribe(event ->
    {
      CommandDelegate.execute(new SendPartyAlertCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getName() + " has joined " + event.getPlayerCharacter().getParty().getName()));
      CommandDelegate.execute(new SendPartyMessageCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getName() + " has joined " + event.getPlayerCharacter().getParty().getName() + "!"));
    });

    EEvent.LEFT_PARTY.subscribe(event ->
    {
      CommandDelegate.execute(new SendPartyAlertCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getName() + " has left " + event.getPlayerCharacter().getParty().getName()));
      CommandDelegate.execute(new SendPartyMessageCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getName() + " has left " + event.getPlayerCharacter().getParty().getName()));
    });

    EEvent.MOVED.subscribe(event -> CommandDelegate.execute(new SendPartyAlertCmd(event.getPlayerCharacter().getParty(), event.getPlayerCharacter().getParty().getName() + " is entering " + event.getPlayerCharacter().getLocation().getDisplayName() + ".\nJoin the battle: @TheWoodlandsBot")));

    EEvent.CREATURE_DROPPED_ITEM.subscribe(new CreatureDroppedEventRecipient());
    EEvent.CREATURE_DROPPED_ITEM.subscribe(new CreatureWasMuggedEventRecipient());
  }

  public void subscribe(EventRecipient<? extends Event> recipient)
  {
    getSubscribers(this).add(recipient);
  }

  // Ease-of-use standard case where it just involves a player character.
  public void trigger(PlayerCharacter playerCharacter)
  {
    for (EventRecipient<? extends Event> subscriber : getSubscribers(this))
    {
      subscriber.preTrigger(new Event(playerCharacter));
    }
  }

  // More general use-case
  public void trigger(Event event)
  {
    for (EventRecipient<? extends Event> subscriber : getSubscribers(this))
    {
      subscriber.preTrigger(event);
    }
  }

  public static List<EventRecipient<? extends Event>> getSubscribers(EEvent eEvent)
  {
    if (subscribers == null)
    {
      createSubscribers();
    }

    if (subscribers.get(eEvent) == null)
    {
      buildEvent(eEvent);
    }

    return subscribers.get(eEvent);
  }

  private synchronized static void buildEvent(EEvent eEvent)
  {
    subscribers.computeIfAbsent(eEvent, k -> new ArrayList<>());
  }

  private static synchronized void createSubscribers()
  {
    if (subscribers == null)
    {
      subscribers = new HashMap<>();
    }
  }
}
