package instance.java.Instance;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Utility.Utility;
import org.bukkit.entity.Entity;

public class PlayerInstanceKillSpecificCreature extends PlayerInstance
{
    private int creatureKilled = 0;

    public PlayerInstanceKillSpecificCreature(PlayerInstanceConfig config, int id, String path)
    {
        super(config, id, path);
    }


    @Override
    public void resetInstance()
    {
        super.resetInstance();
        creatureKilled = 0;
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
                if (myConfig.getCreatureName().equals(e.getCustomName()))
                {
                    creatureKilled++;
                    Utility.sendActionbarMessage(""+creatureKilled,getMyGroup());
                    if (creatureKilled >=myConfig.getCreatureToKill())
                    {
                        endInstance(true);
                        resetInstance();
                    }
                }
            }
        }
    }
}
