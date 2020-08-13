package instance.java.Effect;

import org.bukkit.entity.Player;

public class EffectConsoleCommand extends EffectPlayer
{
    private final String command;

    public String getCommand()
    {
        return command;
    }

    public EffectConsoleCommand(int id, boolean hasPos, boolean allPlayer, double timer, String command)
    {
        super(id, hasPos, allPlayer, timer);
        this.command = command;
    }

    @Override
    public void startEffect(Player[] p)
    {

    }
}