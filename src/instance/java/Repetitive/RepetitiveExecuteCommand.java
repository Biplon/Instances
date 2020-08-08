package instance.java.Repetitive;

import instance.java.Enum.ERepetitiveType;

public class RepetitiveExecuteCommand extends Repetitive
{
    private final boolean playerCommand;

    private final String command;

    public boolean isPlayerCommand()
    {
        return playerCommand;
    }

    public String getCommand()
    {
        return command;
    }

    public RepetitiveExecuteCommand(ERepetitiveType type, int timer, String command, boolean playerCommand)
    {
        super(type, timer);
        this.playerCommand = playerCommand;
        this.command = command;
    }
}
