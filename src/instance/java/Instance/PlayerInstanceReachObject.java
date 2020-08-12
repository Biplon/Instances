package instance.java.Instance;

import instance.java.Struct.PlayerInstanceConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerInstanceReachObject extends PlayerInstance
{
    private Location reachObjectLocation;

    public Location getReachObjectLocation()
    {
        return reachObjectLocation;
    }

    public PlayerInstanceReachObject(PlayerInstanceConfig config, int id, String path)
    {
        super(config, id, path);
        try
        {
            File f = new File(path);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            String[] coords = cfg.getString("general.objecttoreachcoords").split(",");
            reachObjectLocation = new Location(myWorld,Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void resetInstance()
    {
        super.resetInstance();
    }

    public void setInstanceEnd(boolean win)
    {
        endInstance(win);
        resetInstance();
    }
}
