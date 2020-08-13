package instance.java.Effect;

import org.bukkit.entity.Player;

public abstract class EffectPlayer extends Effect
{
    public EffectPlayer(int id, boolean hasPos, boolean allPlayer, double timer)
    {
        super(id, hasPos, allPlayer, timer);
    }

    public abstract void startEffect(Player[] p);
}
