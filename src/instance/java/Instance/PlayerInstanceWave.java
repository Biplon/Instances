package instance.java.Instance;

import instance.java.Config.LanguageManager;
import instance.java.Enum.EEventType;
import instance.java.Instances;
import instance.java.Struct.CreatureSpawnPoint;
import instance.java.Struct.CreatureWaveEntity;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Task.*;
import instance.java.Utility.Utility;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerInstanceWave extends PlayerInstance
{
    boolean isTaskActive = false;

    public ArrayList<Entity> enemylist = new ArrayList();

    int taskCount = 0;

    public PlayerInstanceWave(PlayerInstanceConfig config, int id, String path)
    {
        super(config, id, path);
    }

    @Override
    public void prepareStart()
    {
        if (myGroup.isFull())
        {
            super.prepareStart();
            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startInstance, 2);
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
                    Utility.sendActionbarMessage(LanguageManager.waveStartTimeText.replace("%time%", ((TaskCreatureWave) myConfig.getTasks().get(taskCount)).preCountdown + ""), myGroup);
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

    @Override
    public void startInstance()
    {
        super.startInstance();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::initTaskStart, 200);
    }

    @Override
    public void resetInstance()
    {
        super.resetInstance();
        killEnemyList();
        taskCount = 0;
        isTaskActive = false;
    }

    private void killEnemyList()
    {
        for (Entity m : enemylist)
        {
            m.remove();
        }
        enemylist.clear();
    }

    public void startTaskEvent()
    {
        EEventType typ = ((TaskEvent) myConfig.getTasks().get(taskCount)).getEEventType();
        switch (typ)
        {
            case ChangePlayerSpawn:
                activePlayerSpawn = playerSpawnPoints.get(((TaskEventChangePlayerSpawn) myConfig.getTasks().get(taskCount)).getSpawnPointId());
                break;
            case SendMessage:
                if (((TaskEventSendMessage) myConfig.getTasks().get(taskCount)).isActionbar())
                {
                    Utility.sendActionbarMessage((((TaskEventSendMessage) myConfig.getTasks().get(taskCount)).getText()), myGroup);
                }
                else
                {
                    Utility.sendMessage(((TaskEventSendMessage) myConfig.getTasks().get(taskCount)).getText(), myGroup);
                }
                break;
            case ExecuteCommand:
                String command = ((TaskEventExecuteCommand) myConfig.getTasks().get(taskCount)).getCommand();
                if (((TaskEventExecuteCommand) myConfig.getTasks().get(taskCount)).isPlayerCommand())
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
                break;
        }
        taskCount++;
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
                        if (csp.id == wm.creatureSpawnPointId)
                        {
                            sp = csp;
                            break;
                        }
                    }
                    data.put("type", wm.mobName);
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
                Utility.sendActionbarMessage(LanguageManager.waveStartedText, myGroup);
            }
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
        for (Entity e : enemylist)
        {
            if (!e.isDead())
            {
                tmp.add(e);
            }
        }
        enemylist = tmp;
        checkIsWaveClear();
    }

    public void checkIsWaveClear()
    {
        if (enemylist.size() == 0 && isTaskActive)
        {
            isTaskActive = false;
            Utility.sendActionbarMessage(LanguageManager.waveClearedText, myGroup);
            if (taskCount >= myConfig.getTasks().size())
            {
                endInstance(true);
                resetInstance();
            }
            else
            {
                initTaskStart();
            }
        }
    }
}
