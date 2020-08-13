package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;

public class TriggerOnPressurePlateActivate extends TriggerCoordinates
{

    public TriggerOnPressurePlateActivate(int id, Location[] location, Effect myEffect, boolean singleUse)
    {
        super(id, location, myEffect, singleUse);
        myTrigger = ETriggerType.OnPressurePlateActivate;
    }
}