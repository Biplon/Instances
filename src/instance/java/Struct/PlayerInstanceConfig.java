package instance.java.Struct;

import instance.java.Enum.InstancesTyp;
import instance.java.Enum.TaskTyp;
import instance.java.Instances;
import instance.java.ManageInstances.PlayerInstance;
import instance.java.ManageInstances.PlayerVisitInstanceManager;
import instance.java.Task.Task;
import instance.java.Task.TaskCreatureWave;
import instance.java.Task.TaskEvent;
import instance.java.Utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerInstanceConfig
{
    private final String instanceName;

    private final int visitsPerHour;

    private final int groupLives;

    private final boolean playerOwnInventory;

    private PlayerInstance[] instances = null;

    private int groupSize;

    private int groupMinSize;

    private InstancesTyp instancesTyp;

    private final ArrayList<String> instancesStartCommands = new ArrayList();

    private final ArrayList<String> instancesLoseCommands = new ArrayList();

    private final ArrayList<String> instancesWinCommands = new ArrayList();

    private final ArrayList<Task> tasks = new ArrayList();

    public String getInstanceName()
    {
        return instanceName;
    }

    public int getGroupLives()
    {
        return groupLives;
    }

    public boolean getPlayerOwnInventory()
    {
        return playerOwnInventory;
    }

    public InstancesTyp getInstancesTyp()
    {
        return  instancesTyp;
    }

    public PlayerInstance[] getInstances()
    {
        return instances;
    }

    public ArrayList<String> getInstancesStartCommands()
    {
        return instancesStartCommands;
    }

    public ArrayList<String> getInstancesWinCommands()
    {
        return instancesWinCommands;
    }

    public ArrayList<String> getInstancesLoseCommands()
    {
        return instancesLoseCommands;
    }

    public ArrayList<Task> getTasks()
    {
        return tasks;
    }

    public int getGroupMinSize()
    {
        return groupMinSize;
    }

    public int getGroupSize()
    {
        return groupSize;
    }

    public PlayerInstanceConfig(String path)
    {
        File f = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        this.instanceName = cfg.getString("general.instanceName");
        this.visitsPerHour = cfg.getInt("general.visitsPerHour");
        this.groupLives = cfg.getInt("general.groupLives");
        this.playerOwnInventory =  cfg.getBoolean("general.playerOwnInventory");
        this.instancesTyp =  InstancesTyp.valueOf(cfg.getString("general.instancesTyp"));
        this.groupSize =  cfg.getInt("general.groupsize");
        this.groupMinSize =  cfg.getInt("general.groupminsize");
        PlayerVisitInstanceManager.getInstance().addInstance(instanceName);
        boolean isnext = true;
        int count = 0;
        while (isnext)
        {
            if (cfg.getString("commands.start." + count) != null)
            {
                instancesStartCommands.add(cfg.getString("commands.start." + count));
                count++;
            }
            else
            {
                isnext = false;
            }
        }

        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("commands.win." + count) != null)
            {
                instancesWinCommands.add(cfg.getString("commands.win." + count));
                count++;
            }
            else
            {
                isnext = false;
            }
        }

        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("commands.lose." + count) != null)
            {
                instancesLoseCommands.add(cfg.getString("commands.lose." + count));
                count++;
            }
            else
            {
                isnext = false;
            }
        }
        if (createInstances())
        {
            Bukkit.getLogger().info(instanceName+": loaded!");
        }
        else
        {
            Bukkit.getLogger().info(instanceName+": can not load!");
            return;
        }
        if (instancesTyp == InstancesTyp.Waves)
        {
            boolean isnext2;
            int count2 = 0;
            while (isnext)
            {
                isnext2 = true;
                count2 = 0;
                if (cfg.getString("task." + count + ".cooldown") != null)
                {
                    if (TaskTyp.valueOf(cfg.getString("task.typ")) == TaskTyp.CreatureWave)
                    {
                        tasks.add(new TaskCreatureWave(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("waves." + count + ".autostart"))),count));
                        while (isnext2)
                        {
                            if (cfg.getString("task." + count + ".monster." + count2 + ".name") != null)
                            {
                                ((TaskCreatureWave) tasks.get(count)).addCreatureWaveEntity(new CreatureWaveEntity(cfg.getString("task." + count + ".monster." + count2 + ".name"), cfg.getInt("waves." + count + ".monster." + count2 + ".amount"), cfg.getInt("waves." + count + ".monster." + count2 + ".spawnpointid")));
                                count2++;
                            }
                            else
                            {
                                isnext2 = false;
                            }
                        }
                    }
                    else if (TaskTyp.valueOf(cfg.getString("task.typ")) == TaskTyp.Event)
                    {
                        tasks.add(new TaskEvent(count));
                    }
                    count++;
                }
                else
                {
                    isnext = false;
                }
            }

        }
    }

    private boolean createInstances()
    {
        File folder = new File(Instances.getInstance().getDataFolder() + "/instances/"+instanceName);
        if (!folder.exists())
        {
            folder.mkdir();
            return false;
        }
        List<String> result = new ArrayList<>();
        Utility.search(".*\\.yml", folder, result);
        if (result.size() >0)
        {
            instances = new PlayerInstance[result.size()];
            for (int i = 0; i < result.size(); i++)
            {
                instances[i] = new PlayerInstance(this,result.get(i));
            }
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean canJoin(Player p)
    {
        if (PlayerVisitInstanceManager.getInstance().getVisits(p, instanceName) < visitsPerHour || visitsPerHour == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isPlayerInInstance(Player p)
    {
        for (PlayerInstance pi: instances)
        {
            for (Player pl : pi.getMyGroup().getGroup())
            {
                if (pl != null)
                {
                    if (pl.getUniqueId() == p.getUniqueId())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public PlayerInstance getPlayerInstance(Player p)
    {
        for (PlayerInstance pi: instances)
        {
            for (Player pl : pi.getMyGroup().getGroup())
            {
                if (pl != null)
                {
                    if (pl.getUniqueId() == p.getUniqueId())
                    {
                        return pi;
                    }
                }
            }
        }
        return null;
    }

    public void removePlayer(Player p,boolean disconnect)
    {
        for (PlayerInstance pi: instances)
        {
            for (Player pl : pi.getMyGroup().getGroup())
            {
                if (pl != null)
                {
                    if (pl.getUniqueId() == p.getUniqueId())
                    {
                        pi.getMyGroup().removePlayer(pl,disconnect);
                        return;
                    }
                }
            }
        }
    }

    public void clearEnemyList()
    {
        for (PlayerInstance pi: instances)
        {
            pi.clearEnemyList();
        }
    }
}
