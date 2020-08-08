package instance.java.Manager;

import instance.java.Instance.PlayerInstance;
import instance.java.Instances;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Utility.Utility;
import org.bukkit.entity.Player;

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
        if (!folder.exists())
        {
            folder.mkdir();
        }
        List<String> result = new ArrayList<>();
        Utility.search(".*\\.yml", folder, result);
        for (String path : result)
        {
            instances.add(new PlayerInstanceConfig(path));
        }
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

    public void CheckEntity()
    {
        for (PlayerInstanceConfig in : instances)
        {
            in.clearEnemyList();
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
}
