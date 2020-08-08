package instance.java.Repetitive;

import instance.java.Enum.ERepetitiveType;

public class RepetitiveSpawnCreature extends Repetitive
{
    private final String creature;

    private final int amount;

    private final int sp;

    public String getCreature()
    {
        return creature;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getSp()
    {
        return sp;
    }

    public RepetitiveSpawnCreature(ERepetitiveType type, int timer, String creature, int amount, int sp)
    {
        super(type, timer);
        this.creature = creature;
        this.amount = amount;
        this.sp = sp;
    }
}
