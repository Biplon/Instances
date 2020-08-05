package instance.java.Struct;

public class CreatureWaveEntity
{
    public final String mobname;

    public final int amount;

    public final int creaturespawnpointid;

    public CreatureWaveEntity(String mobname, int amount, int sp)
    {
        this.mobname = mobname;
        this.amount = amount;
        this.creaturespawnpointid = sp;
    }

    @Override
    public String toString()
    {
        return "CreatureWaveEntity{" +
                "mobname='" + mobname + '\'' +
                ", amount=" + amount +
                ", sp=" + creaturespawnpointid +
                '}';
    }
}
