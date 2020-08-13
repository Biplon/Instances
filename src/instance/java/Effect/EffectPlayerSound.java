package instance.java.Effect;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EffectPlayerSound extends EffectPlayer
{
    private final Sound sound;

    public Sound getSound()
    {
        return sound;
    }

    public EffectPlayerSound(int id, boolean hasPos, boolean allPlayer, double timer, Sound sound)
    {
        super(id, hasPos, allPlayer, timer);
        this.sound = sound;
    }

    @Override
    public void startEffect(Player[] p)
    {

    }
}
