package instance.java.Effect;


import org.bukkit.Location;

public class EffectSpawnCreature extends EffectLocation
{
    private final String creatureName;

    private final int amount;

    public String getCreatureName()
    {
        return creatureName;
    }

    public int getAmount()
    {
        return amount;
    }

    public EffectSpawnCreature(int id, boolean hasPos, boolean allPlayer, double timer, String creatureName, int amount)
    {
        super(id, hasPos, allPlayer, timer);
        this.creatureName = creatureName;
        this.amount = amount;
    }

    @Override
    public void startEffect(Location pos)
    {

    }


}
