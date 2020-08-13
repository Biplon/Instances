package instance.java.Trigger;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.entity.EntityType;

public class TriggerOnCreatureDeath extends TriggerRegion
{
    private final EntityType type;

    private final String name;

    public EntityType getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public TriggerOnCreatureDeath(int id, ProtectedRegion[] region, Effect myEffect, boolean singleUse, EntityType type, String name)
    {
        super(id, region, myEffect, singleUse);
        this.type = type;
        this.name = name;
        myTrigger = ETriggerType.OnCreatureDeath;
    }
}