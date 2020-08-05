package instance.java.ManageInstances;

import instance.java.Instances;
import instance.java.Struct.PlayerInstanceConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InstancesManager
{
    private static InstancesManager instance;

    public static InstancesManager getInstance()
    {
        return instance;
    }

    private List<PlayerInstanceConfig> instances = new ArrayList<>();

    public InstancesManager()
    {
        instance = this;
    }

    public void loadInstances()
    {

    }

    public String getInstanceOfPlayer(String playerName)
    {
        return "";
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

    public void CheckEntity()
    {
            for (PlayerInstanceConfig in : instances)
            {
                    in.clearEnemyList();
            }

    }

    public boolean canJoin(Player p,String instancename)
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
            if (in.getInstanceName().equals(instancename))
            {
                if (in.canJoin(p))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

        }
        return false;
    }
}
