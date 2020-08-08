package instance.java.Task;

import instance.java.Enum.EEventType;

public class TaskEvent extends Task
{
    private final EEventType EEventType;

    public EEventType getEEventType()
    {
        return EEventType;
    }

    public TaskEvent(int number, double wavePreCountdown, boolean autostart, EEventType EEventType)
    {
        this.number = number;
        this.preCountdown = wavePreCountdown;
        this.autostart = autostart;
        this.EEventType = EEventType;
    }

    public void executeEvent()
    {

    }
}
