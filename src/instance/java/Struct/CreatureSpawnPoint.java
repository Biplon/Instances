package instance.java.Struct;

import org.bukkit.Location;
import org.bukkit.World;

public class CreatureSpawnPoint
{
    public final int id;

    public final Location loc;

    public CreatureSpawnPoint(int id, World world, int posx, int posy, int posz)
    {
        this.id = id;
        loc = new Location(world, posx, posy, posz);
    }

    @Override
    public String toString()
    {
        return "CreatureSpawnPoint{" +
                "id=" + id +
                ", loc=" + loc +
                '}';
    }
}
