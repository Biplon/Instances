package instance.java.Repetitive;

import instance.java.Enum.RepetitiveType;

public class RepetitiveExecuteCommand extends Repetitive
{
    private final String command;

    public String getCommand()
    {
        return command;
    }

    public RepetitiveExecuteCommand(RepetitiveType type, int timer,String command)
    {
        super(type, timer);
        this.command = command;
    }
}
