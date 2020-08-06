package instance.java.ManageInstances;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import instance.java.Config.LanguageManager;
import instance.java.Enum.InstancesType;
import instance.java.Group.Group;
import instance.java.Instances;
import instance.java.Repetitive.Repetitive;
import instance.java.Repetitive.RepetitiveExecuteCommand;
import instance.java.Repetitive.RepetitiveSendMassage;
import instance.java.Repetitive.RepetitiveSpawnCreature;
import instance.java.Struct.CreatureSpawnPoint;
import instance.java.Struct.CreatureWaveEntity;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Struct.PlayerSpawnPoint;
import instance.java.Task.TaskCreatureWave;
import instance.java.Task.TaskEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerInstance
{
    private final PlayerInstanceConfig myConfig;

    private final World myWorld;

    private final Group myGroup;

    private final int id;

    private ProtectedRegion myRegion;

    private boolean isTaskActive = false;

    private final ArrayList<CreatureSpawnPoint> creatureSpawnPoints = new ArrayList();

    private final ArrayList<PlayerSpawnPoint> playerSpawnPoints = new ArrayList();

    public ArrayList<Entity> enemylist = new ArrayList();

    public ArrayList<Repetitive> repetitives = new ArrayList();

    private int[] repeitivesBukkitTask;

    private boolean inUse = false;

    private int groupLivesCurrent;

    private PlayerSpawnPoint activePlayerSpawn;

    private int taskCount = 0;

    public boolean isInUse()
    {
        return inUse;
    }

    public Group getMyGroup()
    {
        return myGroup;
    }

    public PlayerSpawnPoint getActivePlayerSpawn()
    {
        return activePlayerSpawn;
    }

    public PlayerInstanceConfig getMyConfig()
    {
        return myConfig;
    }

    public int getId()
    {
        return id;
    }

    public PlayerInstance(PlayerInstanceConfig config, int id, String path)
    {
        File f = new File(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        this.id = id;
        myConfig = config;
        myWorld = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("general.worldname")));
        myGroup = new Group(this, config.getGroupMinSize(), config.getGroupSize());
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

            activePlayerSpawn = playerSpawnPoints.get(0);
        }
    }

    public void prepareStart()
    {
        if (myGroup.isFull())
        {
            inUse = true;

            if (!myConfig.getPlayerOwnInventory())
            {
                myGroup.saveInventory();
            }
            myGroup.savePlayerLoc();
            teleportPlayerToInstance();

            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startInstance, 2);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startRepetitives, 2);
        }
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
                    sendActionbarMessage(((RepetitiveSendMassage) repetitive).getText());
                }
                else
                {
                    sendMessage(((RepetitiveSendMassage) repetitive).getText());
                }
                return;
            case ExecuteCommand:
                String command = ((RepetitiveExecuteCommand) repetitive).getCommand();
                for (Player p : myGroup.getGroup())
                {
                    if (p != null)
                    {
                        Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName())));
                    }
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
        if (myConfig.getInstancesType() == InstancesType.Waves)
        {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::initTaskStart, 200);
        }
    }

    public void initTaskStart()
    {
        if (inUse)
        {
            if (myConfig.getTasks().get(taskCount) instanceof TaskCreatureWave)
            {
                if (((TaskCreatureWave) myConfig.getTasks().get(taskCount)).autostart)
                {
                    sendActionbarMessage(LanguageManager.getInstance().waveStartTimeText.replace("%time%", ((TaskCreatureWave) myConfig.getTasks().get(taskCount)).preCountdown + ""));
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startTaskCreatureWave, (long) (((TaskCreatureWave) myConfig.getTasks().get(taskCount)).preCountdown * 20));
                }
            }
            else if (myConfig.getTasks().get(taskCount) instanceof TaskEvent)
            {
                if (((TaskEvent) myConfig.getTasks().get(taskCount)).autostart)
                {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startTaskEvent, (long) (((TaskEvent) myConfig.getTasks().get(taskCount)).preCountdown * 20));
                }
            }
        }
    }

    public void startTaskEvent()
    {
        //magic
        initTaskStart();
    }

    public void startTaskCreatureWave()
    {
        if (!isTaskActive && inUse)
        {
            if (myConfig.getTasks().get(taskCount) instanceof TaskCreatureWave)
            {
                Map<String, String> data = new HashMap<String, String>();
                CreatureSpawnPoint sp = null;
                for (CreatureWaveEntity wm : ((TaskCreatureWave) myConfig.getTasks().get(taskCount)).waveMonsters)
                {
                    for (CreatureSpawnPoint csp : creatureSpawnPoints)
                    {
                        if (csp.id == wm.creaturespawnpointid)
                        {
                            sp = csp;
                            break;
                        }
                    }
                    data.put("type", wm.mobname);
                    data.put("number", "" + wm.amount);
                    data.put("x", "" + sp.loc.getBlockX());
                    data.put("y", "" + sp.loc.getBlockY());
                    data.put("z", "" + sp.loc.getBlockZ());
                    data.put("world", myWorld.getName());
                    String formattedString = StrSubstitutor.replace("mo lspawn ${type} ${number} ${x} ${y} ${z} ${world}", data);
                    Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedString));
                    data.clear();
                }
                taskCount++;
                Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), this::getEnemys);
                isTaskActive = true;
                sendActionbarMessage(LanguageManager.getInstance().waveStartedText);
            }
        }
    }

    public void resetInstance()
    {
        inUse = false;
        taskCount = 0;
        isTaskActive = false;
        groupLivesCurrent = myConfig.getGroupLives();
        activePlayerSpawn = playerSpawnPoints.get(0);
        killEnemyList();
        myGroup.clearGroup();
        for (int i = 0; i < repeitivesBukkitTask.length; i++)
        {
            Bukkit.getScheduler().cancelTask(repeitivesBukkitTask[i]);
        }
    }

    private boolean getEnemys()
    {
        for (CreatureSpawnPoint sp : creatureSpawnPoints)
        {
            List<Entity> nearbyEntities = (List<Entity>) Objects.requireNonNull(sp.loc.getWorld()).getNearbyEntities(sp.loc, 1, 1, 1);
            for (Entity e : nearbyEntities)
            {
                if (!(e instanceof Player))
                {
                    enemylist.add(e);
                }
            }
        }
        return true;
    }

    public void clearEnemyList()
    {
        ArrayList<Entity> tmp = new ArrayList<>();
        checkIsWaveClear();
        for (Entity e : enemylist)
        {
            if (!e.isDead())
            {
                tmp.add(e);
            }
        }
        enemylist = tmp;
    }

    public void checkIsWaveClear()
    {
        if (enemylist.size() == 0 && isTaskActive)
        {
            isTaskActive = false;
            sendActionbarMessage(LanguageManager.getInstance().waveClearedText);
            if (taskCount >= myConfig.getTasks().size())
            {
                endInstance(true);
            }
            else
            {
                initTaskStart();
            }
        }
    }

    private void killEnemyList()
    {
        for (Entity m : enemylist)
        {
            m.remove();
        }
        enemylist.clear();
    }

    public void reduceGroupLive(Player p)
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
            sendMessage(LanguageManager.getInstance().winText);
        }
        else
        {
            sendMessage(LanguageManager.getInstance().loseText);
        }
        resetInstance();
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

    public void sendMessage(String msg)
    {
        for (Player p : myGroup.getGroup())
        {
            if (p != null)
            {
                p.sendMessage(msg);
            }
        }
    }

    public void sendActionbarMessage(String msg)
    {
        for (Player p : myGroup.getGroup())
        {
            if (p != null)
            {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            }
        }
    }

    public void sendMessageWithClickEvent(String clickabletext, String command, boolean leader)
    {
        TextComponent message = new TextComponent(clickabletext);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }
}
