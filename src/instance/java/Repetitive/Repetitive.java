package instance.java.Repetitive;

import instance.java.Enum.RepetitiveType;

public abstract class Repetitive
{
    private final RepetitiveType type;

    private final int timer;

    public int getTimer()
    {
        return timer;
    }

    public RepetitiveType getType()
    {
        return type;
    }

    public Repetitive(RepetitiveType type,int timer)
    {
        this.type = type;
        this.timer = timer;
    }
}
