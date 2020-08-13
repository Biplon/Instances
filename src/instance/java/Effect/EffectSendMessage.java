package instance.java.Effect;

import org.bukkit.entity.Player;

public class EffectSendMessage extends EffectPlayer
{
    private final String message;

    public String getMessage()
    {
        return message;
    }

    public EffectSendMessage(int id, boolean hasPos, boolean allPlayer, double timer, String message)
    {
        super(id, hasPos, allPlayer, timer);
        this.message = message;
    }

    @Override
    public void startEffect(Player[] p)
    {

    }
}
