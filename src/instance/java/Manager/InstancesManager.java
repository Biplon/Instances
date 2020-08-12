package instance.java.Manager;

import instance.java.Enum.EInstancesType;
import instance.java.Enum.EReachObjectType;
import instance.java.Instance.PlayerInstance;
import instance.java.Instances;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstancesManager
{
    private static InstancesManager instance;

    public static InstancesManager getInstance()
    {
        return instance;
    }

    private final List<PlayerInstanceConfig> instances = new ArrayList<>();

    public InstancesManager()
    {
        instance = this;
    }

    public void loadInstances()
    {
        File folder = new File(Instances.getInstance().getDataFolder() + "/instances");
        List<String> result = new ArrayList<>();
        Utility.search(".*\\.yml", folder, result);
        for (String path : result)
        {
            instances.add(new PlayerInstanceConfig(path));
        }
    }

    public boolean isPlayerInRunningInstance(Player p)
    {
        for (PlayerInstanceConfig pic : instances)
        {
            if (pic.isPlayerInRunningInstance(p))
            {
                return true;
            }
        }
        return false;
    }

    public List<PlayerInstanceConfig> getInstances()
    {
        return instances;
    }

    public PlayerInstanceConfig getPlayerInstanceConfig(String name)
    {
        for (PlayerInstanceConfig pic : instances)
        {
            if (pic.getInstanceName().equals(name))
            {
                return pic;
            }
        }
        return null;
    }

    public void leavePlayer(Player p, boolean disconnect)
    {
        for (PlayerInstanceConfig in : instances)
        {
            if (in.isPlayerInInstance(p))
            {
                in.removePlayer(p, disconnect);
                return;
            }
        }
    }

    public PlayerInstance getInstanceOfPlayer(Player p)
    {
        for (PlayerInstanceConfig in : instances)
        {
            if (in.isPlayerInInstance(p))
            {
                return in.getPlayerInstance(p);
            }
        }
        return null;
    }

    public void setPlayerReady(Player p)
    {
        PlayerInstance tmp = getInstanceOfPlayer(p);
        if (tmp != null)
        {
            if (tmp.getMyGroup().setPlayerReady(p))
            {
                tmp.prepareStart();
            }
        }
    }

    public void CheckEntity(Entity e)
    {
        for (PlayerInstanceConfig in : instances)
        {
            in.clearEnemyList(e);
        }
    }

    public boolean canJoin(Player p, String instanceName)
    {
        for (PlayerInstanceConfig in : instances)
        {
            if (in.getPlayerInstance(p) != null)
            {
                return false;
            }
        }
        for (PlayerInstanceConfig in : instances)
        {
            if (in.getInstanceName().equals(instanceName))
            {
                return in.canJoin(p);
            }

        }
        return false;
    }

    public void checkTriggerPIE(PlayerInteractEvent event)
    {
        for (PlayerInstanceConfig pic : instances)
        {
            if (pic.checkTriggerPIE(event))
            {
                return;
            }
        }
    }

    public void checkReachObjectInstances(PlayerInteractEvent event)
    {
        EReachObjectType type = null;

        if (event.getAction() == Action.PHYSICAL)
        {
            type = EReachObjectType.OnPressurePlateActivate;
        }
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getMaterial() == Material.LEVER)
            {
                type = EReachObjectType.OnLeverChange;
            }
            else
            {
                switch (event.getMaterial())
                {
                    case BIRCH_BUTTON:
                    case ACACIA_BUTTON:
                    case DARK_OAK_BUTTON:
                    case JUNGLE_BUTTON:
                    case OAK_BUTTON:
                    case SPRUCE_BUTTON:
                    case STONE_BUTTON:
                        type = EReachObjectType.OnButtonClick;
                        break;
                }
            }
        }

        if (type != null)
        {
            for (PlayerInstanceConfig pic : instances)
            {
                if (pic.getInstancesType() == EInstancesType.ReachObject)
                {
                    if (pic.getObjecttoreach() == type)
                    {
                        if (pic.checkReachObjectInstances(event))
                        {
                            return;
                        }
                    }
                }
            }
        }
    }
}
