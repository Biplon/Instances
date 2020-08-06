package instance.java.Task;

import instance.java.Enum.EventType;

public class TaskEvent extends Task
{
    private final EventType eventType;

    public EventType getEventType()
    {
        return eventType;
    }

    public TaskEvent(int number, double wavePreCountdown, boolean autostart,EventType eventType)
    {
        this.number = number;
        this.preCountdown = wavePreCountdown;
        this.autostart = autostart;
        this.eventType = eventType;
    }

    public void executeEvent()
    {

    }
}
