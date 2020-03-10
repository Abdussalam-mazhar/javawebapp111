package com.github.dagwud.woodlands.game.domain;

import com.github.dagwud.woodlands.game.Icons;
import com.github.dagwud.woodlands.game.domain.spells.SpellAbilities;
import com.github.dagwud.woodlands.game.domain.stats.Stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

public abstract class Fighter extends GameObject
{
  private static final long serialVersionUID = 1L;
  private static final BigDecimal EIGHTY_PER_CENT = new BigDecimal("0.8");
  private static final BigDecimal SIXTY_FIVE_PER_CENT = new BigDecimal("0.65");
  private static final BigDecimal FORTY_PER_CENT = new BigDecimal("0.45");
  private static final BigDecimal TWENTY_PER_CENT = new BigDecimal("0.2");

  private SpellAbilities spellAbilities;

  public abstract Stats getStats();

  public abstract CarriedItems getCarrying();

  public String summary()
  {
    return summary(true);
  }

  public String summary(boolean showName)
  {
    String name = (showName ? getName() + ": " : "");
    Stats stats = getStats();
    if (stats.getState() == EState.DEAD)
    {
      return name + Icons.DEAD + "️dead";
    }
    String message = name + healthIcon(stats) + stats.getHitPointsDescription();
    if (stats.getMaxMana().total() != 0)
    {
      message += ", " + Icons.MANA + stats.getMana() + "/" + stats.getMaxMana();
    }
    return message;
  }

  private String healthIcon(Stats stats)
  {
    BigDecimal perc = new BigDecimal(stats.getHitPoints())
        .divide(new BigDecimal(stats.getMaxHitPoints().total()), 3, RoundingMode.HALF_DOWN);
    if (perc.compareTo(EIGHTY_PER_CENT) >= 0)
    {
      return Icons.HP_HEALTH;
    }
    if (perc.compareTo(SIXTY_FIVE_PER_CENT) >= 0)
    {
      return Icons.HP_FAIRLY_HEALTHY;
    }
    if (perc.compareTo(FORTY_PER_CENT) >= 0)
    {
      return Icons.HP_HURT;
    }
    if (perc.compareTo(TWENTY_PER_CENT) >= 0)
    {
      return Icons.HP_BADLY_HURT + "️";
    }
    return Icons.HP_CRITICAL;
  }

  public SpellAbilities getSpellAbilities()
  {
    if (spellAbilities == null)
    {
      spellAbilities = new SpellAbilities();
    }
    return spellAbilities;
  }

  public boolean isConscious()
  {
    return getStats().getState() == EState.ALIVE;
  }

  public boolean isDrinking()
  {
    return getStats().getState() == EState.DRINKING;
  }

  public boolean isDead()
  {
    return getStats().getState() == EState.DEAD;
  }

  public boolean isResting()
  {
    return getStats().getState() == EState.SHORT_RESTING ||
        getStats().getState() == EState.LONG_RESTING;
  }

  public abstract Fighter chooseFighterToAttack(Collection<? extends Fighter> fighters);

  public final boolean canCarryMore()
  {
    return getCarrying().countTotalCarried() < determineMaxAllowedItems();
  }

  protected int determineMaxAllowedItems()
  {
    return Integer.MAX_VALUE;
  }
}
