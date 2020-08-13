package instance.java.Effect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EffectGivePlayerItem extends EffectPlayer
{
    private final Material item;

    private  final int amount;

    public int getAmount()
    {
        return amount;
    }

    public Material getItem()
    {
        return item;
    }

    public EffectGivePlayerItem(int id, boolean hasPos, boolean allPlayer, double timer, Material item,int amount)
    {
        super(id, hasPos, allPlayer, timer);
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void startEffect(Player[] p)
    {

    }
}
