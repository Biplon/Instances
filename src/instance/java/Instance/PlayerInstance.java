package instance.java.Instance;

import com.mysql.jdbc.Util;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import instance.java.Config.LanguageManager;
import instance.java.Group.Group;
import instance.java.Instances;
import instance.java.Manager.PlayerVisitInstanceManager;
import instance.java.Repetitive.Repetitive;
import instance.java.Repetitive.RepetitiveExecuteCommand;
import instance.java.Repetitive.RepetitiveSendMassage;
import instance.java.Repetitive.RepetitiveSpawnCreature;
import instance.java.Struct.CreatureSpawnPoint;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Struct.PlayerSpawnPoint;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import instance.java.Utility.Utility;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerInstance
{
    PlayerInstanceConfig myConfig;

    World myWorld;

    private int id;

    private ProtectedRegion myRegion;

    final ArrayList<CreatureSpawnPoint> creatureSpawnPoints = new ArrayList();

    final ArrayList<PlayerSpawnPoint> playerSpawnPoints = new ArrayList();

    ArrayList<ProtectedRegion> subRegion = new ArrayList<>();

    final ArrayList<Location> triggerLocation = new ArrayList<>();

    public ArrayList<Repetitive> repetitives = new ArrayList();

    public ArrayList<Integer> triggerused = new ArrayList();

    private int[] repeitivesBukkitTask;

    PlayerSpawnPoint activePlayerSpawn;

    Group myGroup;

    public ArrayList<Location> getTriggerLocation()
    {
        return triggerLocation;
    }

    public Group getMyGroup()
    {
        return myGroup;
    }

    public ProtectedRegion getMyRegion()
    {
        return myRegion;
    }

    int groupLivesCurrent;

    boolean inUse = false;

    public boolean isInUse()
    {
        return inUse;
    }

    public PlayerSpawnPoint getActivePlayerSpawn()
    {
        return activePlayerSpawn;
    }

    public PlayerInstanceConfig getMyConfig()
    {
        return myConfig;
    }

    public ArrayList<ProtectedRegion> getSubRegion()
    {
        return subRegion;
    }

    public int getId()
    {
        return id;
    }

    public PlayerInstance(PlayerInstanceConfig config, int id, String path)
    {
        try
        {
        File f = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        this.id = id;
        myConfig = config;
        myGroup = new Group(this, config.getGroupMinSize(), config.getGroupSize());
        myWorld = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("general.worldname")));
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        assert myWorld != null;
        RegionManager regions = container.get(BukkitAdapter.adapt(myWorld));
        assert regions != null;
        myRegion = regions.getRegion(Objects.requireNonNull(cfg.getString("general.regionname")));
        boolean isnext = true;
        int count = 0;
        String[] coords;
        while (isnext)
        {
            if (cfg.getString("creatureSpawnPoints." + count + ".coords") != null)
            {
                coords = cfg.getString("creatureSpawnPoints." + count + ".coords").split(",");
                creatureSpawnPoints.add(new CreatureSpawnPoint(count, myWorld, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
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
            if (cfg.getString("playerSpawnpoints." + count + ".coords") != null)
            {
                coords = cfg.getString("playerSpawnpoints." + count + ".coords").split(",");

                playerSpawnPoints.add(new PlayerSpawnPoint(count, myWorld, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
                count++;
            }
            else
            {
                isnext = false;
            }
        }
        activePlayerSpawn = playerSpawnPoints.get(0);
        isnext = true;
        count = 0;
        String region;
        while (isnext)
        {
            if (cfg.getString("subregion." + count + ".name") != null)
            {
                region = cfg.getString("subregion." + count + ".name");
                assert region != null;
                subRegion.add(regions.getRegion(region));
                count++;
            }
            else
            {
                isnext = false;
            }
        }

        isnext = true;
        count = 0;
        String[] tcoords;
        while (isnext)
        {
            if (cfg.getString("triggerpos." + count + ".coords") != null)
            {
                tcoords = cfg.getString("triggerpos." + count + ".coords").split(",");
                int x = Integer.parseInt(tcoords[0]);
                int y = Integer.parseInt(tcoords[1]);
                int z = Integer.parseInt(tcoords[2]);
                triggerLocation.add(new Location(myWorld,x,y,z));
                count++;
            }
            else
            {
                isnext = false;
            }
        }
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void setTriggerUsed(int id)
    {
        triggerused.add(id);
    }
    public boolean isTriggerUsed(int id)
    {
        return triggerused.contains(id);
    }

    private void clearUsedTrigger()
    {
        triggerused.clear();
    }

    public void prepareStart()
    {
        inUse = true;

        if (!myConfig.getPlayerOwnInventory())
        {
            myGroup.saveInventory();
        }
        myGroup.savePlayerLoc();
        teleportPlayerToInstance();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startRepetitives, 2);
    }

    private void startRepetitives()
    {
        if (repetitives.size() > 0)
        {
            repeitivesBukkitTask = new int[repetitives.size()];
            for (int i = 0; i < repeitivesBukkitTask.length; i++)
            {
                int finalI = i;
                repeitivesBukkitTask[i] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Instances.getInstance(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        executeRepetitive(finalI);
                    }
                }, (long) repetitives.get(i).getTimer() * 20, (long) repetitives.get(i).getTimer() * 20);
            }
        }
    }

    public void reduceGroupLive()
    {
        if (myConfig.getGroupLives() > 0)
        {
            groupLivesCurrent--;
            if (groupLivesCurrent <= 0)
            {
                endInstance(false);
            }
        }
    }

    private void executeRepetitive(int repetitiveid)
    {
        Repetitive repetitive = repetitives.get(repetitiveid);
        switch (repetitive.getType())
        {
            case SpawnCreature:
                Map<String, String> data = new HashMap<String, String>();
                CreatureSpawnPoint sp = null;
                for (CreatureSpawnPoint csp : creatureSpawnPoints)
                {
                    if (csp.id == ((RepetitiveSpawnCreature) repetitive).getSp())
                    {
                        sp = csp;
                        break;
                    }
                }
                data.put("type", ((RepetitiveSpawnCreature) repetitive).getCreature());
                data.put("number", "" + ((RepetitiveSpawnCreature) repetitive).getAmount());
                data.put("x", "" + sp.loc.getBlockX());
                data.put("y", "" + sp.loc.getBlockY());
                data.put("z", "" + sp.loc.getBlockZ());
                data.put("world", myWorld.getName());
                String formattedString = StrSubstitutor.replace("mo lspawn ${type} ${number} ${x} ${y} ${z} ${world}", data);
                Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedString));
                data.clear();
                return;
            case SendMassage:
                if (((RepetitiveSendMassage) repetitive).isActionbar())
                {
                   Utility.sendActionbarMessage(((RepetitiveSendMassage) repetitive).getText(),myGroup);
                }
                else
                {
                    Utility.sendMessage(((RepetitiveSendMassage) repetitive).getText(),myGroup);
                }
                return;
            case ExecuteCommand:
                String command = ((RepetitiveExecuteCommand) repetitive).getCommand();
                if (((RepetitiveExecuteCommand) repetitive).isPlayerCommand())
                {
                    for (Player p : myGroup.getGroup())
                    {
                        if (p != null)
                        {
                            Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName())));
                        }
                    }
                }
                else
                {
                    Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                }

        }
    }

    public void startInstance()
    {
        for (Player p : myGroup.getGroup())
        {
            if (!myConfig.getPlayerOwnInventory())
            {
                p.getInventory().clear();
            }
            for (String s : myConfig.getInstancesStartCommands())
            {
                Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName())));
            }
        }
    }

    public void resetInstance()
    {
        inUse = false;
        groupLivesCurrent = myConfig.getGroupLives();
        activePlayerSpawn = playerSpawnPoints.get(0);
        clearUsedTrigger();
        for (int value : repeitivesBukkitTask)
        {
            Bukkit.getScheduler().cancelTask(value);
        }
        myGroup.clearGroup();
    }

    public void endInstance(boolean win)
    {
        teleportPlayersBack();
        if (!myConfig.getPlayerOwnInventory())
        {
            myGroup.restoreInventory();
        }
        for (Player p : myGroup.getGroup())
        {
            if (win)
            {
                for (String s : myConfig.getInstancesWinCommands())
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()));
                }
            }
            else
            {
                for (String s : myConfig.getInstancesLoseCommands())
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()));
                }
            }
            PlayerVisitInstanceManager.getInstance().addPlayer(p.getUniqueId(), myConfig.getInstanceName());
        }
        if (win)
        {
            Utility.sendMessage(LanguageManager.getInstance().winText,myGroup);
        }
        else
        {
            Utility.sendMessage(LanguageManager.getInstance().loseText,myGroup);
        }
    }

    public void teleportPlayerToInstance()
    {
        for (Player p : myGroup.getGroup())
        {
            if (p != null)
            {
                p.teleport(playerSpawnPoints.get(0).loc);
            }
        }
    }

    public void teleportPlayersBack()
    {
        for (int i = 0; i < myGroup.getGroup().length; i++)
        {
            if (myGroup.getGroup()[i] != null)
            {
                myGroup.getGroup()[i].teleport(myGroup.getPlayerLocation()[i]);
            }
        }
    }

    public void teleportPlayerBack(Player p, Location loc)
    {
        p.teleport(loc);
    }


}
