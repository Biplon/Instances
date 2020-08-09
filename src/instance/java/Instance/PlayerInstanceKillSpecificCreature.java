package instance.java.Instance;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import instance.java.Struct.PlayerInstanceConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;

public class PlayerInstanceKillSpecificCreature extends PlayerInstance
{
    private String creatureName;

    private int creatureToKill;

    private int creatureKilled = 0;

    public PlayerInstanceKillSpecificCreature(PlayerInstanceConfig config, int id, String path)
    {
        super(config, id, path);
        loadConfig(path);
    }

    private void loadConfig(String path)
    {
        File f = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        creatureName = cfg.getString("general.creaturename");
        creatureToKill = cfg.getInt("general.creaturetokill");
    }

    @Override
    public void resetInstance()
    {
        super.resetInstance();
        creatureKilled = 0;
        myGroup.clearGroup();
    }

    public void checkKillCreature(Entity e)
    {
        Location loc = BukkitAdapter.adapt(e.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for (ProtectedRegion region : set)
        {
            if (region == getMyRegion())
            {
                if (creatureName.equals(e.getName()))
                {
                    creatureKilled++;
                    if (creatureKilled >= creatureToKill)
                    {
                        endInstance(true);
                        resetInstance();
                    }
                }
            }
        }

    }
}
