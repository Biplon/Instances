package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;
import org.bukkit.Material;

public class TriggerOnClickWithItem extends TriggerCoordinates
{
    private final boolean airClick;

    private final boolean rightClick;

    private final boolean customItem;

    private String lore1;

    private final Material myMaterial;

    public boolean isAirClick()
    {
        return airClick;
    }

    public boolean isRightClick()
    {
        return rightClick;
    }

    public boolean isCustomItem()
    {
        return customItem;
    }

    public String getLore1()
    {
        return lore1;
    }

    public Material getMyMaterial()
    {
        return myMaterial;
    }

    public TriggerOnClickWithItem(int id, Location[] location, boolean airClick, boolean rightClick, boolean customItem, Material myMaterial, Effect myEffect, boolean singleUse)
    {
        super(id, location, myEffect, singleUse);
        this.airClick = airClick;
        this.rightClick = rightClick;
        this.customItem = customItem;
        this.myMaterial = myMaterial;
        myTrigger = ETriggerType.OnClickWithItem;
    }

    public TriggerOnClickWithItem(int id, Location[] location, boolean airClick, boolean rightClick, boolean customItem, String lore1, Material myMaterial, Effect myEffect, boolean singleUse)
    {
        super(id, location, myEffect, singleUse);
        this.airClick = airClick;
        this.rightClick = rightClick;
        this.customItem = customItem;
        this.lore1 = lore1;
        this.myMaterial = myMaterial;
        myTrigger = ETriggerType.OnClickWithItem;
    }
}