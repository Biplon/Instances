package instance.java.Effect;

import org.bukkit.Location;

public class EffectCreateExplosion extends EffectLocation
{
    private final boolean safe;

    public boolean isSafe()
    {
        return safe;
    }

    public EffectCreateExplosion(int id, boolean hasPos, boolean allPlayer, double timer, boolean safe)
    {
        super(id, hasPos, allPlayer, timer);
        this.safe = safe;
    }

    @Override
    public void startEffect(Location pos)
    {

    }
}
