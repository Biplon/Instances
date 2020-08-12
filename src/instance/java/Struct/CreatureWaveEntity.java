package instance.java.Struct;

public class CreatureWaveEntity
{
    public final String mobName;

    public final int amount;

    public final int creatureSpawnPointId;

    public CreatureWaveEntity(String mobName, int amount, int sp)
    {
        this.mobName = mobName;
        this.amount = amount;
        this.creatureSpawnPointId = sp;
    }

    @Override
    public String toString()
    {
        return "CreatureWaveEntity{" +
                "mobName='" + mobName + '\'' +
                ", amount=" + amount +
                ", sp=" + creatureSpawnPointId +
                '}';
    }
}
