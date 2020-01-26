package com.github.dagwud.woodlands.game.domain;

import com.github.dagwud.woodlands.game.domain.stats.Stats;
import java.math.BigDecimal;

public abstract class Fighter
{
  public abstract String getName();

  public abstract Stats getStats();

  public abstract CarriedItems getCarrying();

  public String summary()
  {
    Stats stats = getStats();
    if (stats.getState() == EState.DEAD)
    {
      return getName() + ": ☠️dead";
    }
    String message = getName() + ": " + healthIcon(stats) + stats.getHitPoints() + " / " + stats.getMaxHitPoints();
    if (stats.getMaxMana() != 0)
    {
      message += ", ✨" + stats.getMana() + "/" + stats.getMaxMana();
    }
    return message;
  }

  private String healthIcon(Stats stats)
  {
    BigDecimal perc = new BigDecimal(stats.getHitPoints()).divide(new BigDecimal(stats.getMaxHitPoints()));
    if (perc.compareTo(new BigDecimal("0.8")) >= 0)
    {
      return "💚";
    }
    if (perc.compareTo(new BigDecimal("0.65")) >= 0)
    {
      return "💛";
    }
    if (perc.compareTo(new BigDecimal("0.45")) >= 0)
    {
      return "🧡";
    }
    if (perc.compareTo(new BigDecimal("0.2")) >= 0)
    {
      return "❤️";
    }
    return "💔";
  }
}
