package instance.java.Effect;

import org.bukkit.Location;
import org.bukkit.Material;

public class EffectSetBlock extends EffectLocation
{
    private final Material material;

    public Material getMaterial()
    {
        return material;
    }

    public EffectSetBlock(int id, boolean hasPos, boolean allPlayer, double timer, Material material)
    {
        super(id, hasPos, allPlayer, timer);
        this.material = material;
    }

    @Override
    public void startEffect(Location pos)
    {

    }
}
