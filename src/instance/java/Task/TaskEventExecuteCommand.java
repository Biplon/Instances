package instance.java.Task;

import instance.java.Enum.EEventType;

public class TaskEventExecuteCommand extends TaskEvent
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

    public TaskEventExecuteCommand(int number, double wavePreCountdown, boolean autostart, EEventType EEventType, boolean playerCommand, String command)
    {
        super(number, wavePreCountdown, autostart, EEventType);
        this.playerCommand = playerCommand;
        this.command = command;
    }
}