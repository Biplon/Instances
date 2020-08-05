package instance.java.ManageInstances;

import instance.java.Enum.InstancesTyp;
import instance.java.Group.Group;
import instance.java.Instances;
import instance.java.Struct.CreatureSpawnPoint;
import instance.java.Struct.CreatureWaveEntity;
import instance.java.Struct.PlayerInstanceConfig;
import instance.java.Struct.PlayerSpawnpoint;
import instance.java.Task.TaskCreatureWave;
import instance.java.Task.TaskEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerInstance
{
    private PlayerInstanceConfig myConfig;

    private World world;

    private Group group;

    private boolean istaskactive = false;

    private final ArrayList<CreatureSpawnPoint> creatureSpawnPoints = new ArrayList();

    private final ArrayList<PlayerSpawnpoint> playerSpawnpoints = new ArrayList();

    public ArrayList<Entity> enemylist = new ArrayList();

    private boolean inuse = false;

    private int grouplivescurrent;

    private PlayerSpawnpoint activePlayerSpawn;

    private int taskcount = 0;

    public boolean isInuse()
    {
        return inuse;
    }

    public void setInuse()
    {
        inuse = true;
    }

    public Group getGroup()
    {
        return group;
    }

    public PlayerSpawnpoint getActivePlayerSpawn()
    {
        return activePlayerSpawn;
    }

    public PlayerInstance(String path)
    {
    }


    public boolean prepareStart()
    {
        if (group.isFull())
        {
            inuse = true;

            if (!myConfig.getPlayerOwnInventory())
            {
                group.saveInventory();
            }
            group.savePlayerloc();
            teleportPlayerToInstance();

            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startInstance, 2);
            return true;
        }
        return false;
    }

    public void startInstance()
    {
        for (Player p : group.getGroup())
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
        if (myConfig.getInstancesTyp() == InstancesTyp.Waves)
        {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::initWaveStart, 200);
        }

    }

    public void initWaveStart()
    {
        if (myConfig.getTasks().get(taskcount) instanceof TaskCreatureWave)
        {
            if (((TaskCreatureWave)myConfig.getTasks().get(taskcount)).autostart)
            {
                sendActionbarMessage("Wave start in " + ((TaskCreatureWave)myConfig.getTasks().get(taskcount)).precountdown + " sec");
                Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startTaskCreatureWave, (long) (((TaskCreatureWave)myConfig.getTasks().get(taskcount)).precountdown * 20));
            }
        }
        else if (myConfig.getTasks().get(taskcount) instanceof TaskEvent)
        {
            if (((TaskEvent)myConfig.getTasks().get(taskcount)).autostart)
            {
                Bukkit.getScheduler().runTaskLaterAsynchronously(Instances.getInstance(), this::startTaskEvent, (long) (((TaskEvent)myConfig.getTasks().get(taskcount)).precountdown * 20));
            }
        }
    }

    public void startTaskEvent()
    {

    }

    public void startTaskCreatureWave()
    {
        if (!istaskactive)
        {
            if (myConfig.getTasks().get(taskcount) instanceof TaskCreatureWave)
            {
                Map<String, String> data = new HashMap<String, String>();
                CreatureSpawnPoint sp = null;
                for (CreatureWaveEntity wm :((TaskCreatureWave)myConfig.getTasks().get(taskcount)).waveMonsters)
                {

                    for (CreatureSpawnPoint csp: creatureSpawnPoints)
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
                    data.put("world", world.getName());
                    String formattedString = StrSubstitutor.replace("mo lspawn ${type} ${number} ${x} ${y} ${z} ${world}", data);
                    Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedString));
                    data.clear();
                }
                taskcount++;
                Bukkit.getScheduler().callSyncMethod(Instances.getInstance(), this::getEnemys);
                istaskactive = true;
                sendActionbarMessage("Wave started");
            }
        }
    }

    public void resetHuntingGround()
    {

        inuse = false;

        taskcount = 0;
        istaskactive = false;
        grouplivescurrent = myConfig.getGroupLives();
        activePlayerSpawn = playerSpawnpoints.get(0);
        killEnemyList();
        group.clearGroup();

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
        if (enemylist.size() == 0 && istaskactive)
        {
            istaskactive = false;
            sendActionbarMessage("wave clear");
            if (taskcount >= myConfig.getTasks().size())
            {
                sendActionbarMessage("hg clear");
                endinstance(true);
            }
            else
            {
                initWaveStart();
            }
        }
    }

    private void killEnemyList()
    {
        for (Entity m: enemylist)
        {
            m.remove();
        }
        enemylist.clear();
    }

    public void reduceGroupLive(Player p)
    {
        if (myConfig.getGroupLives() > 0)
        {
            grouplivescurrent--;
            if (grouplivescurrent <= 0)
            {
                endinstance(false);
            }
        }
    }

    public void endinstance(boolean win)
    {
        teleportPlayersBack();
        if (!myConfig.getPlayerOwnInventory())
        {
            group.restoreInventory();
        }
        for (Player p : group.getGroup())
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
            sendMessage("win");
        }
        else
        {
            sendMessage("lose");
        }
        resetHuntingGround();
    }

    public void teleportPlayerToInstance()
    {
        for (Player p : group.getGroup())
        {
            if (p != null)
            {
                p.teleport(playerSpawnpoints.get(0).loc);
            }
        }
    }

    public void teleportPlayersBack()
    {
        for (int i = 0; i < group.getGroup().length; i++)
        {
            if (group.getGroup()[i] != null)
            {
                group.getGroup()[i].teleport(group.getPlayerLocation()[i]);
            }
        }
    }

    public void teleportPlayerBack(Player p, Location loc)
    {
        p.teleport(loc);
    }

    public void sendMessage(String msg)
    {
        for (Player p : group.getGroup())
        {
            if (p != null)
            {
                p.sendMessage(msg);
            }
        }
    }

    public void sendActionbarMessage(String msg)
    {
        for (Player p : group.getGroup())
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
