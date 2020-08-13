package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

public class TriggerOnBlockClick extends TriggerCoordinates
{
    private final Material myBlock;

    private final Action clickAction;

    public Material getMyBlock()
    {
        return myBlock;
    }

    public Action getClickAction()
    {
        return clickAction;
    }

    public TriggerOnBlockClick(int id, Location[] location, Material myMaterial, boolean rightClick, Effect myEffect, boolean singleUse)
    {
        super(id, location,myEffect,singleUse);
        myTrigger = ETriggerType.OnBlockClick;
        this.myBlock = myMaterial;
        if (rightClick)
        {
            this.clickAction = Action.RIGHT_CLICK_BLOCK;
        }
        else
        {
            this.clickAction = Action.LEFT_CLICK_BLOCK;
        }
    }
}
