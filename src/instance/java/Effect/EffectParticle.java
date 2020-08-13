package instance.java.Effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EffectParticle extends EffectLocation
{
    private final Particle particle;

    public Particle getParticle()
    {
        return particle;
    }

    public EffectParticle(int id, boolean hasPos, boolean allPlayer, double timer, Particle particle)
    {
        super(id, hasPos, allPlayer, timer);
        this.particle = particle;
    }

    @Override
    public void startEffect(Location pos)
    {

    }
}
