package instance.java.Trigger;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;

public class TriggerOnChat extends TriggerRegion
{
    private final String text;

    public String getText()
    {
        return text;
    }

    public TriggerOnChat(int id, ProtectedRegion[] region, String text, Effect myEffect, boolean singleUse)
    {
        super(id, region,myEffect,singleUse);
        this.text = text;
        myTrigger = ETriggerType.OnChat;
    }
}