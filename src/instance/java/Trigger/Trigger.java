package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Trigger
{
    private final int id;

    ETriggerType myTrigger;

    final Effect myEffect;

    final boolean singleUse;

    public int getId()
    {
        return id;
    }

    public Effect getMyEffect()
    {
        return myEffect;
    }

    public boolean isSingleUse()
    {
        return singleUse;
    }

    public ETriggerType getMyTrigger()
    {
        return myTrigger;
    }

    public Trigger(int id, Effect myEffect, boolean singleUse)
    {
        this.id = id;
        this.myEffect = myEffect;
        this.singleUse = singleUse;
    }

    public void runEffect(Location loc)
    {
        Bukkit.getLogger().info("effect triggered " + loc);
        //TODO implement
    }

    public void runEffect(Player[] p)
    {
        //TODO implement
    }
}
