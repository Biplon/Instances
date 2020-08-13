package instance.java.Effect;


import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EffectTeleportPlayer extends EffectPlayer
{
    private final Location location;

    public Location getLocation()
    {
        return location;
    }

    public EffectTeleportPlayer(int id, boolean hasPos, boolean allPlayer, double timer, Location location)
    {
        super(id, hasPos, allPlayer, timer);
        this.location = location;
    }


    @Override
    public void startEffect(Player[] p)
    {

    }
}
