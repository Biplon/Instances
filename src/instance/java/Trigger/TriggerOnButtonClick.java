package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;

public class TriggerOnButtonClick extends TriggerCoordinates
{

    public TriggerOnButtonClick(int id, Location location, Effect myEffect, boolean singleUse)
    {
        super(id, location,myEffect,singleUse);
        myTrigger = ETriggerType.OnButtonClick;
    }
}