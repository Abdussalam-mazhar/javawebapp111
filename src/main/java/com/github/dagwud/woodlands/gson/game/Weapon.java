package com.github.dagwud.woodlands.gson.game;

import com.github.dagwud.woodlands.game.domain.GameCharacter;
import com.google.gson.annotations.SerializedName;

public class Weapon
{
  private static final String MELEE_ICON = "\u2694"; // crossed swords
  private static final String RANGED_ICON = "\ud83c\udff9"; // bow and arrow

  public String name;

  public Damage damage;

  public boolean ranged;

  @SerializedName(value = "custom_icon")
  public String customIcon;

  @SerializedName(value = "prevent_spawning")
  public boolean preventSpawning;

  public String getIcon()
  {
    if (customIcon != null)
    {
      return customIcon;
    }
    return ranged ? RANGED_ICON : MELEE_ICON;
  }

  public String summary(GameCharacter carrier)
  {
    return name + " " + getIcon() + determineDamageText(carrier);
  }

  private String determineDamageText(GameCharacter carrier)
  {
    int bonusDamage = carrier.getStats().getWeaponBonusDamage(this);

    String damageText = damage.determineAverageRoll();
    if (bonusDamage != 0)
    {
      damageText += " +" + bonusDamage;
    }
    return damageText;
  }

}
