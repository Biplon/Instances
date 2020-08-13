package instance.java.Effect;

import instance.java.Enum.EEffectType;
import org.bukkit.entity.Player;

import javax.xml.stream.Location;

public abstract class Effect
{
    final int id;

    final boolean hasPos;

    final boolean allPlayer;

    EEffectType myType;

    final double timer;

    public double getTimer()
    {
        return timer;
    }

    public int getId()
    {
        return id;
    }

    public boolean isAllPlayer()
    {
        return allPlayer;
    }

    public boolean isHasPos()
    {
        return hasPos;
    }

    public Effect(int id, boolean hasPos, boolean allPlayer,double timer)
    {
        this.id = id;
        this.hasPos = hasPos;
        this.allPlayer = allPlayer;
        this.timer = timer;
    }
}
