package instance.java.Trigger;

import instance.java.Effect.Effect;
import instance.java.Enum.ETriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TriggerOnBlockClick extends TriggerCoordinates
{
    private final Material myBlock;

    private final boolean rightClick;

    public Material getMyBlock()
    {
        return myBlock;
    }

    public boolean isRightClick()
    {
        return rightClick;
    }

    public TriggerOnBlockClick(int id, Location[] location, Material myMaterial, boolean rightClick, Effect myEffect, boolean singleUse)
    {
        super(id, location,myEffect,singleUse);
        myTrigger = ETriggerType.OnBlockClick;
        this.myBlock = myMaterial;
        this.rightClick = rightClick;
    }

    public boolean isTriggered()
    {
        return super.isTriggered();
    }
}
