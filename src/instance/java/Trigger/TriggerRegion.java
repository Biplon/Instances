package instance.java.Trigger;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;

public class TriggerRegion extends Trigger
{
    private final ProtectedRegion[] myRegion;

    public ProtectedRegion[] getMyRegion()
    {
        return myRegion;
    }

    public TriggerRegion(int id, ProtectedRegion[] region, Effect myEffect, boolean singleUse)
    {
        super(id,myEffect,singleUse);
        myRegion = region;
    }
}
