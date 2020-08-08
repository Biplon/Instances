package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;

public abstract class TriggerCoordinates extends Trigger
{
    private final Location[] myLocation;

    public Location[] getMyLocation()
    {
        return myLocation;
    }

    public TriggerCoordinates(int id, Location[] location, Effect myEffect, boolean singleUse)
    {
        super(id,myEffect,singleUse);
        myLocation = location;
    }
}
