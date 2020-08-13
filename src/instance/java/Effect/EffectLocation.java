package instance.java.Effect;


import org.bukkit.Location;

public abstract class EffectLocation extends Effect
{
    public EffectLocation(int id, boolean hasPos, boolean allPlayer, double timer)
    {
        super(id, hasPos, allPlayer, timer);
    }

    public abstract void startEffect(Location pos);
}
