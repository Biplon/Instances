package instance.java.Task;

import instance.java.Enum.EventType;

public class TaskEventSendMessage extends TaskEvent
{
    private final boolean actionbar;

    private final String text;

    public boolean isActionbar()
    {
        return actionbar;
    }

    public String getText()
    {
        return text;
    }

    public TaskEventSendMessage(int number, double wavePreCountdown, boolean autostart, EventType eventType, boolean actionbar, String text)
    {
        super(number, wavePreCountdown, autostart, eventType);
        this.actionbar = actionbar;
        this.text = text;
    }
}