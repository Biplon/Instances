package instance.java.Struct;

public class CreatureWaveEntity
{
    public final String mobname;

    public final int amount;

    public final CreatureSpawnPoint sp;

    public CreatureWaveEntity(String mobname, int amount, CreatureSpawnPoint sp)
    {
        this.mobname = mobname;
        this.amount = amount;
        this.sp = sp;
    }

    @Override
    public String toString()
    {
        return "CreatureWaveEntity{" +
                "mobname='" + mobname + '\'' +
                ", amount=" + amount +
                ", sp=" + sp +
                '}';
    }
}
