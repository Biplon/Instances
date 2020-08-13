package instance.java.Effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class EffectGivePlayerPotionEffect extends EffectPlayer
{
    private final PotionType potionEffect;

    public PotionType getPotionEffect()
    {
        return potionEffect;
    }

    public EffectGivePlayerPotionEffect(int id, boolean hasPos, boolean allPlayer, double timer, PotionType potionEffect)
    {
        super(id, hasPos, allPlayer, timer);
        this.potionEffect = potionEffect;
    }


    @Override
    public void startEffect(Player[] p)
    {

    }
}
