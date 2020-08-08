package instance.java.Trigger;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;

public class TriggerOnRegionLeave extends TriggerRegion
{

    public TriggerOnRegionLeave(int id, ProtectedRegion[] region, Effect myEffect, boolean singleUse)
    {
        super(id, region,myEffect,singleUse);
        myTrigger = ETriggerType.OnRegionLeave;
    }
}