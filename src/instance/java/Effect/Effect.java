package instance.java.Effect;

import instance.java.Enum.EEffectType;
import instance.java.Group.Group;
import org.bukkit.entity.Player;

import javax.xml.stream.Location;

public abstract class Effect
{
    final int id;

    final boolean hasPos;

    final boolean allPlayer;

    EEffectType myType;

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

    public Effect(int id, boolean hasPos, boolean allPlayer)
    {
        this.id = id;
        this.hasPos = hasPos;
        this.allPlayer = allPlayer;
    }

    public void startEffect(Player[] p)
    {
        //TODO implement startEffect player
    }

    public void startEffect(Location pos)
    {
        //TODO implement startEffect loc
    }
}
