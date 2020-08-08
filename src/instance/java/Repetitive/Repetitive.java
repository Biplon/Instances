package instance.java.Repetitive;

import instance.java.Enum.ERepetitiveType;

public abstract class Repetitive
{
    private final ERepetitiveType type;

    private final int timer;

    public int getTimer()
    {
        return timer;
    }

    public ERepetitiveType getType()
    {
        return type;
    }

    public Repetitive(ERepetitiveType type, int timer)
    {
        this.type = type;
        this.timer = timer;
    }
}
