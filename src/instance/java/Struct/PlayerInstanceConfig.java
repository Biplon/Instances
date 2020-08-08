package instance.java.Struct;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import instance.java.Enum.*;
import instance.java.Instance.PlayerInstanceWave;
import instance.java.Instances;
import instance.java.Instance.PlayerInstance;
import instance.java.Manager.EffectPresetManager;
import instance.java.Manager.PlayerVisitInstanceManager;
import instance.java.Repetitive.Repetitive;
import instance.java.Repetitive.RepetitiveExecuteCommand;
import instance.java.Repetitive.RepetitiveSendMassage;
import instance.java.Repetitive.RepetitiveSpawnCreature;
import instance.java.Task.*;
import instance.java.Trigger.*;
import instance.java.Utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

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

    private final EInstancesType instancesType;

    private final ArrayList<String> instancesStartCommands = new ArrayList();

    private final ArrayList<String> instancesLoseCommands = new ArrayList();

    private final ArrayList<String> instancesWinCommands = new ArrayList();

    private final ArrayList<Task> tasks = new ArrayList();

    private final ArrayList<Repetitive> repetitives = new ArrayList();

    private final ArrayList<Trigger> trigger = new ArrayList<>();

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

    public EInstancesType getInstancesType()
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
        this.playerOwnInventory = cfg.getBoolean("general.playerOwnInventory");
        this.instancesType = EInstancesType.valueOf(cfg.getString("general.instancesType"));
        this.groupSize = cfg.getInt("general.groupsize");
        this.groupMinSize = cfg.getInt("general.groupminsize");
        PlayerVisitInstanceManager.getInstance().addInstance(instanceName);
        loadTrigger(cfg);
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
            Bukkit.getLogger().info("instance: " +instanceName + ": loaded!");
        }
        else
        {
            Bukkit.getLogger().info("instance: " +instanceName + ": can not load!");
            return;
        }
        boolean isnext2;
        int count2 = 0;
        if (instancesType == EInstancesType.Waves)
        {
            isnext = true;
            count = 0;
            while (isnext)
            {
                isnext2 = true;
                count2 = 0;
                if (cfg.getString("task." + count + ".cooldown") != null)
                {
                    if (ETaskType.valueOf(cfg.getString("task." + count + ".type")) == ETaskType.CreatureWave)
                    {
                        tasks.add(new TaskCreatureWave(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))), count));
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
                    else if (ETaskType.valueOf(cfg.getString("task." + count + ".type")) == ETaskType.Event)
                    {
                        if (EEventType.valueOf(cfg.getString("task." + count + ".eventtype")) == EEventType.ChangePlayerSpawn)
                        {
                            tasks.add(new TaskEventChangePlayerSpawn(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))), EEventType.ChangePlayerSpawn, cfg.getInt("task." + count + ".spawnid")));
                        }
                        else if (EEventType.valueOf(cfg.getString("task." + count + ".eventtype")) == EEventType.ExecuteCommand)
                        {
                            tasks.add(new TaskEventExecuteCommand(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))), EEventType.ExecuteCommand, cfg.getBoolean("task." + count + ".playercommand"), cfg.getString("task." + count + ".command")));
                        }
                        else if (EEventType.valueOf(cfg.getString("task." + count + ".eventtype")) == EEventType.SendMessage)
                        {
                            tasks.add(new TaskEventSendMessage(count, Double.parseDouble(Objects.requireNonNull(cfg.getString("task." + count + ".cooldown"))), Boolean.parseBoolean(Objects.requireNonNull(cfg.getString("task." + count + ".autostart"))), EEventType.SendMessage, cfg.getBoolean("task." + count + ".actionbar"), cfg.getString("task." + count + ".text")));
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
                if (ERepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == ERepetitiveType.SpawnCreature)
                {
                    repetitives.add(new RepetitiveSpawnCreature(ERepetitiveType.SpawnCreature, cfg.getInt("repetitive." + count + ".timer"), cfg.getString("repetitive." + count + ".monster"), cfg.getInt("repetitive." + count + ".amount"), cfg.getInt("repetitive." + count + ".spawnpointid")));
                }
                else if (ERepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == ERepetitiveType.ExecuteCommand)
                {
                    repetitives.add(new RepetitiveExecuteCommand(ERepetitiveType.ExecuteCommand, cfg.getInt("repetitive." + count + ".timer"), cfg.getString("repetitive." + count + ".command"), cfg.getBoolean("repetitive." + count + ".playercommand")));
                }
                else if (ERepetitiveType.valueOf(cfg.getString("repetitive." + count + ".type")) == ERepetitiveType.SendMassage)
                {
                    repetitives.add(new RepetitiveSendMassage(ERepetitiveType.SendMassage, cfg.getInt("repetitive." + count + ".timer"), cfg.getString("repetitive." + count + ".text"), cfg.getBoolean("repetitive." + count + ".actionbar")));
                }
                count++;
            }
            else
            {
                isnext = false;
            }
        }
        for (PlayerInstance pi : instances)
        {
            pi.repetitives = repetitives;
        }
    }

    private void loadTrigger(FileConfiguration cfg)
    {
        boolean isnext = true;
        int count = 0;
        ETriggerType type;
        while (isnext)
        {
            if (cfg.getString("trigger." + count + ".type") != null)
            {
                type = ETriggerType.valueOf(cfg.getString("trigger." + count + ".type"));
                switch (type)
                {
                    case OnCreatureDeath:
                        trigger.add(new TriggerOnCreatureDeath(count, getSubRegions(Objects.requireNonNull(cfg.getString("trigger." + count + ".regionid"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse"), EntityType.valueOf(cfg.getString("trigger." + count + ".creaturetype")), cfg.getString("trigger." + count + ".creaturename")));
                        break;
                    case OnPlayerDeath:
                        trigger.add(new TriggerOnPlayerDeath(count, getSubRegions(Objects.requireNonNull(cfg.getString("trigger." + count + ".regionid"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnRegionEnter:
                        trigger.add(new TriggerOnRegionEnter(count, getSubRegions(Objects.requireNonNull(cfg.getString("trigger." + count + ".regionid"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnRegionLeave:
                        trigger.add(new TriggerOnRegionLeave(count, getSubRegions(Objects.requireNonNull(cfg.getString("trigger." + count + ".regionid"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnChat:
                        trigger.add(new TriggerOnChat(count, getSubRegions(Objects.requireNonNull(cfg.getString("trigger." + count + ".regionid"))), cfg.getString("trigger." + count + ".text"), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnBlockClick:
                        trigger.add(new TriggerOnBlockClick(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), Material.valueOf(Objects.requireNonNull(cfg.getString("trigger." + count + ".material"))), cfg.getBoolean("trigger." + count + ".rightclick"), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnClickWithItem:
                        if (cfg.getBoolean("trigger." + count + ".customitem"))
                        {
                            trigger.add(new TriggerOnClickWithItem(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), cfg.getBoolean("trigger." + count + ".airclick"), cfg.getBoolean("trigger." + count + ".rightclick"), true, cfg.getString("trigger." + count + ".lore1"), Material.valueOf(Objects.requireNonNull(cfg.getString("trigger." + count + ".material"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        }
                        else
                        {
                            trigger.add(new TriggerOnClickWithItem(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), cfg.getBoolean("trigger." + count + ".airclick"), cfg.getBoolean("trigger." + count + ".rightclick"), false, Material.valueOf(Objects.requireNonNull(cfg.getString("trigger." + count + ".material"))), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        }

                        break;
                    case OnLeverChange:
                        trigger.add(new TriggerOnLeverChange(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), cfg.getBoolean("trigger." + count + ".powered"), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnPressurePlateActivate:
                        trigger.add(new TriggerOnPressurePlateActivate(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                    case OnButtonClick:
                        trigger.add(new TriggerOnButtonClick(count, getTriggerLocations(cfg.getInt("trigger." + count + ".posid")), EffectPresetManager.getInstance().getEffect(cfg.getInt("trigger." + count + ".effect.presetid")), cfg.getBoolean("trigger." + count + ".singleuse")));
                        break;
                }
                count++;
            }
            else
            {
                isnext = false;
            }
        }
    }

    private ProtectedRegion[] getSubRegions(String id)
    {
        ProtectedRegion[] reg = new ProtectedRegion[instances.length];
        if (id.equals("main"))
        {
            for (int i = 0; i < instances.length; i++)
            {
                reg[i] = instances[i].getMyRegion();
            }
        }
        else
        {
            int iid = Integer.parseInt(id);
            for (int i = 0; i < instances.length; i++)
            {
                reg[i] = instances[i].getSubRegion().get(iid);
            }
        }
        return reg;
    }

    private Location[] getTriggerLocations(int id)
    {
        Location[] reg = new Location[instances.length];
        for (int i = 0; i < instances.length; i++)
        {
            reg[i] = instances[i].getTriggerLocation().get(id);
        }
        return reg;
    }

    private boolean createInstances()
    {
        File folder = new File(Instances.getInstance().getDataFolder() + "/instances/" + instanceName);
        if (!folder.exists())
        {
            folder.mkdir();
            return false;
        }
        List<String> result = new ArrayList<>();
        Utility.search(".*\\.yml", folder, result);
        if (result.size() > 0)
        {
            instances = new PlayerInstance[result.size()];
            for (int i = 0; i < result.size(); i++)
            {
                if (instancesType == EInstancesType.Waves)
                {
                    instances[i] = new PlayerInstanceWave(this, i, result.get(i));
                }

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
        for (PlayerInstance pi : instances)
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

    public boolean isPlayerInRunningInstance(Player p)
    {
        for (PlayerInstance pi : instances)
        {
            if (pi.isInUse())
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
        }
        return false;
    }

    public PlayerInstance getPlayerInstance(Player p)
    {
        for (PlayerInstance pi : instances)
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

    public void removePlayer(Player p, boolean disconnect)
    {
        for (PlayerInstance pi : instances)
        {
            for (Player pl : pi.getMyGroup().getGroup())
            {
                if (pl != null)
                {
                    if (pl.getUniqueId() == p.getUniqueId())
                    {
                        pi.getMyGroup().removePlayer(pl, disconnect);
                        return;
                    }
                }
            }
        }
    }

    public void clearEnemyList()
    {
        if (instancesType == EInstancesType.Waves)
        {
            for (PlayerInstance pi : instances)
            {
                ((PlayerInstanceWave) pi).clearEnemyList();
            }
        }
    }

    public boolean idExist(int id)
    {
        for (PlayerInstance pi : instances)
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
        for (PlayerInstance pi : instances)
        {
            if (pi.getId() == id)
            {
                return pi;
            }
        }
        return null;
    }

    public boolean checkTriggerPIE(PlayerInteractEvent event)
    {
        for (Trigger t : trigger)
        {
            if (t.getMyTrigger() == ETriggerType.OnBlockClick)
            {
                if (isInstanceOfTriggerLocationRunning(Objects.requireNonNull(event.getClickedBlock()).getLocation()) && isTriggerUsed(t.getId()))
                {
                    if (event.getAction() == ((TriggerOnBlockClick) t).getClickAction() && ((TriggerOnBlockClick) t).getMyBlock() == event.getMaterial())
                    {
                        if (t.getMyEffect().isHasPos())
                        {
                            t.runEffect(event.getClickedBlock().getLocation());
                        }
                        else
                        {
                            t.runEffect(getPlayerOfInstance(event.getClickedBlock().getLocation()));
                        }
                        return true;
                    }
                }
            }
            else if (t.getMyTrigger() == ETriggerType.OnClickWithItem)
            {
                if (isInstanceOfTriggerLocationRunning(Objects.requireNonNull(event.getPlayer().getLocation())) && isTriggerUsed(t.getId()))
                {
                    //TODO implement OnClickWithItemTrigger
                }
            }
            else if (t.getMyTrigger() == ETriggerType.OnLeverChange)
            {
                if (isInstanceOfTriggerLocationRunning(Objects.requireNonNull(event.getClickedBlock()).getLocation()) && isTriggerUsed(t.getId()))
                {
                    //TODO implement OnLeverChangeTrigger
                }
            }
            else if (t.getMyTrigger() == ETriggerType.OnPressurePlateActivate)
            {
                if (isInstanceOfTriggerLocationRunning(Objects.requireNonNull(event.getClickedBlock()).getLocation()) && isTriggerUsed(t.getId()))
                {
                    //TODO implement OnPressurePlateActivateTrigger
                }
            }
            else if (t.getMyTrigger() == ETriggerType.OnButtonClick)
            {
                if (isInstanceOfTriggerLocationRunning(Objects.requireNonNull(event.getClickedBlock()).getLocation()) && isTriggerUsed(t.getId()))
                {
                    //TODO implement OnButtonClickTrigger
                }
            }
        }
        return false;
    }

    private Player[] getPlayerOfInstance(Location loc)
    {
        for (PlayerInstance pi : instances)
        {
            for (Location lo : pi.getTriggerLocation())
            {
                if (lo.equals(loc))
                {
                    return pi.getMyGroup().getGroup();
                }
            }
        }
        return null;
    }

    private Player[] getPlayerOfInstance(String regionname)
    {
        //TODO implement getPlayerOfInstance region
        return null;
    }

    private boolean isTriggerUsed(int id)
    {
        for (PlayerInstance pi : instances)
        {
            if (pi.isTriggerUsed(id))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isInstanceOfRegionRunning(String regionname)
    {
        for (PlayerInstance pi : instances)
        {
            if (pi.isInUse())
            {
                for (ProtectedRegion pr : pi.getSubRegion())
                {
                    if (pr.getId().equals(regionname))
                    {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean isInstanceOfTriggerLocationRunning(Location loc)
    {
        for (PlayerInstance pi : instances)
        {
            if (pi.isInUse())
            {
                for (Location lo : pi.getTriggerLocation())
                {
                    if (lo.equals(loc))
                    {
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
