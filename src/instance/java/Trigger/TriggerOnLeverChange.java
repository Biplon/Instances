package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;

public class TriggerOnLeverChange extends TriggerCoordinates
{
    private final boolean powered;

    public boolean isPowered()
    {
        return powered;
    }

    public TriggerOnLeverChange(int id, Location location, boolean powered, Effect myEffect, boolean singleUse)
    {
        super(id, location, myEffect, singleUse);
        this.powered = powered;
        myTrigger = ETriggerType.OnLeverChange;
    }
}