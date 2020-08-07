package instance.java.Struct;

import instance.java.Enum.EventType;
import instance.java.Enum.InstancesType;
import instance.java.Enum.RepetitiveType;
import instance.java.Enum.TaskType;
import instance.java.Instance.PlayerInstanceWave;
import instance.java.Instances;
import instance.java.Instance.PlayerInstance;
import instance.java.ManageInstances.PlayerVisitInstanceManager;
import instance.java.Repetitive.Repetitive;
import instance.java.Repetitive.RepetitiveExecuteCommand;
import instance.java.Repetitive.RepetitiveSendMassage;
import instance.java.Repetitive.RepetitiveSpawnCreature;
import instance.java.Task.*;
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

    private final int groupSize;

    private final int groupMinSize;

    private final InstancesType instancesType;

    private final ArrayList<String> instancesStartCommands = new ArrayList();

    private final ArrayList<String> instancesLoseCommands = new ArrayList();

    private final ArrayList<String> instancesWinCommands = new ArrayList();

    private final ArrayList<Task> tasks = new ArrayList();

    private final ArrayList<Repetitive> repetitives = new ArrayList();

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

    public InstancesType getInstancesType()
    {
        return instancesType;
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
        this.instancesType =  InstancesType.valueOf(cfg.getString("general.instancesType"));
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
        boolean isnext2;
        int count2 = 0;
        if (instancesType == InstancesType.Waves)
        {
            isnext = true;
            count = 0;
            while (isnext)
            {
                isnext2 = true;
                count2 = 0;
                if (cfg.getString("task." + count + ".cooldown") != null)
                {
                    if (TaskType.valueOf(cfg.getString("task." + count + ".type")) == TaskType.CreatureWave)
                    {
                        tasks.add(new TaskCreatureWave(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))),count));
                        while (isnext2)
                        {
                            if (cfg.getString("task." + count + ".monster." + count2 + ".name") != null)
                            {
                                ((TaskCreatureWave) tasks.get(count)).addCreatureWaveEntity(new CreatureWaveEntity(cfg.getString("task." + count + ".monster." + count2 + ".name"), cfg.getInt("task." + count + ".monster." + count2 + ".amount"), cfg.getInt("task." + count + ".monster." + count2 + ".spawnpointid")));
                                count2++;
                            }
                            else
                            {
                                isnext2 = false;
                            }
                        }
                    }
                    else if (TaskType.valueOf(cfg.getString("task." + count + ".type")) == TaskType.Event)
                    {
                        if (EventType.valueOf(cfg.getString("task." + count + ".type.eventtype"))  == EventType.ChangePlayerSpawn)
                        {
                            tasks.add(new TaskEventChangePlayerSpawn(count,Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))),EventType.ChangePlayerSpawn ,cfg.getInt("task." + count + ".spawnid")));
                        }
                        else if(EventType.valueOf(cfg.getString("task." + count + ".type.eventtype"))  == EventType.ExecuteCommand)
                        {
                            tasks.add(new TaskEventExecuteCommand(count,Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))),EventType.ExecuteCommand,cfg.getBoolean("task." + count + ".playercommand"),cfg.getString("task." + count + ".command")));
                        }
                        else if(EventType.valueOf(cfg.getString("task." + count + ".type.eventtype"))  == EventType.SendMessage)
                        {
                            tasks.add(new TaskEventSendMessage(count,Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))),EventType.SendMessage,cfg.getBoolean("task." + count + ".actionbar"),cfg.getString("task." + count + ".text")));
                        }

                    }
                    count++;
                }
                else
                {
                    isnext = false;
                }
            }
        }
        isnext = true;
        count = 0;
        while (isnext)
        {
            if (cfg.getString("repetitive." + count + ".type") != null)
            {
                if (RepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == RepetitiveType.SpawnCreature)
                {
                    repetitives.add(new RepetitiveSpawnCreature(RepetitiveType.SpawnCreature,cfg.getInt("repetitive." + count + ".timer"),cfg.getString("repetitive." + count + ".monster"),cfg.getInt("repetitive." + count + ".amount"),cfg.getInt("repetitive." + count + ".spawnpointid")));
                }
                else if (RepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == RepetitiveType.ExecuteCommand)
                {
                    repetitives.add(new RepetitiveExecuteCommand(RepetitiveType.ExecuteCommand,cfg.getInt("repetitive." + count + ".timer"),cfg.getString("repetitive." + count + ".command"),cfg.getBoolean("repetitive." + count + ".playercommand")));
                }
                else if (RepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == RepetitiveType.SendMassage)
                {
                    repetitives.add(new RepetitiveSendMassage(RepetitiveType.SendMassage,cfg.getInt("repetitive." + count + ".timer"),cfg.getString("repetitive." + count + ".text"),cfg.getBoolean("repetitive." + count + ".actionbar")));
                }
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

            if (cfg.getString("trigger." + count + ".type") != null)
            {
                count++;
            }
            else
            {
                isnext = false;
            }
        }

        for (PlayerInstance pi:instances)
        {
            pi.repetitives = repetitives;
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
                instances[i] = new PlayerInstance(this,i,result.get(i));
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
        return PlayerVisitInstanceManager.getInstance().getVisits(p, instanceName) < visitsPerHour || visitsPerHour == 0;
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
        if (instancesType == InstancesType.Waves)
        {
            for (PlayerInstance pi: instances)
            {
                ((PlayerInstanceWave)pi).clearEnemyList();
            }
        }
    }

    public boolean idExist(int id)
    {
        for (PlayerInstance pi: instances)
        {
            if (pi.getId() == id)
            {
                return true;
            }
        }
        return false;
    }

    public PlayerInstance getInstanceById(int id)
    {
        for (PlayerInstance pi: instances)
        {
            if (pi.getId() == id)
            {
                return pi;
            }
        }
        return null;
    }
}
