package instance.java.Group;

import instance.java.Config.LanguageManager;
import instance.java.GUI.GUIManager;
import instance.java.Instance.PlayerInstance;
import instance.java.Instance.PlayerInstanceKillSpecificCreature;
import instance.java.Instance.PlayerInstanceReachObject;
import instance.java.Instance.PlayerInstanceWave;
import instance.java.Utility.Utility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pts.java.api.PlayerManagement;

import java.util.Arrays;

public class Group
{
    private final PlayerInstance myInstance;

    private final int minSize;

    private final Player[] group;

    public boolean[] ready;

    private final Location[] loc;

    public Player[] getGroup()
    {
        return group;
    }

    public Location[] getPlayerLocation()
    {
        return loc;
    }

    public Group(PlayerInstance instance, int minSize, int groupSize)
    {
        this.myInstance = instance;
        this.minSize = minSize;
        group = new Player[groupSize];
        ready = new boolean[groupSize];
        loc = new Location[groupSize];
        Arrays.fill(ready, false);
    }

    public void addPlayer(Player p)
    {
        if (!isFull())
        {
            for (int i = 0; i < group.length; i++)
            {
                if (group[i] == null)
                {
                    group[i] = p;
                    if (group.length > 1)
                    {
                        Utility.sendMessage(p.getDisplayName() + " " + LanguageManager.joinedGroupText, this);
                        Utility.sendMessage(LanguageManager.groupText + " " + getFullSlots() + "/" + getGroupSize(), this);
                        Utility.sendMessage(LanguageManager.playerNeedText.replace("%Slots%", getFreeGroupSlots() + ""), this);
                    }
                    if (groupMinSizeReached())
                    {
                        Utility.sendMessageWithClickEvent(LanguageManager.preStartText, "/istartreadycheck", this, true);
                    }
                    if (isFull())
                    {
                        readyCheck();
                    }
                    return;
                }
            }
        }
    }

    public void savePlayerLoc()
    {
        for (int i = 0; i < group.length; i++)
        {
            loc[i] = group[i].getLocation();
        }
    }

    public void removePlayer(Player p, boolean disconnect)
    {
        for (int i = 0; i < group.length; i++)
        {
            if (group[i].getUniqueId().equals(p.getUniqueId()))
            {
                if (myInstance.isInUse())
                {
                    myInstance.teleportPlayerBack(p, loc[i]);
                    if (!myInstance.getMyConfig().getPlayerOwnInventory())
                    {
                        if (!disconnect)
                        {
                            PlayerManagement.getInstance().loadPlayerIgnoreDisableSync(p);
                        }
                        PlayerManagement.getInstance().enablePlayerLoad(p);
                        PlayerManagement.getInstance().enablePlayerSave(p);
                    }
                }
                group[i] = null;
                ready[i] = false;
                loc[i] = null;
                if (group.length > 1)
                {
                    Utility.sendMessage(p.getDisplayName() + " " + LanguageManager.playerLeaveInstanceText, this);
                    if (!myInstance.isInUse())
                    {
                        Utility.sendMessage(LanguageManager.groupText + " " + getFullSlots() + "/" + getGroupSize(), this);
                        Utility.sendMessage(LanguageManager.playerNeedText.replace("%Slots%", getFreeGroupSlots() + ""), this);
                    }
                }
                if (getFreeGroupSlots() == getGroupSize())
                {
                    if (myInstance instanceof PlayerInstanceWave)
                    {
                        myInstance.resetInstance();
                    }
                    else if (myInstance instanceof PlayerInstanceKillSpecificCreature)
                    {
                        myInstance.resetInstance();
                    }
                    else if (myInstance instanceof PlayerInstanceReachObject)
                    {
                        myInstance.resetInstance();
                    }
                }
                return;
            }
        }
    }

    public void readyCheck()
    {
        for (Player p : group)
        {
            GUIManager.getInstance().openReadyCheckGUI(p);
        }
    }

    public boolean setPlayerReady(Player p)
    {
        for (int i = 0; i < group.length; i++)
        {
            if (group[i] == p)
            {
                ready[i] = true;
            }
        }
        for (boolean b : ready)
        {
            if (!b)
            {
                return false;
            }
        }
        return true;
    }

    public void saveInventory()
    {
        for (Player p : group)
        {
            PlayerManagement.getInstance().savePlayerIgnoreDisableSync(p);
            PlayerManagement.getInstance().disablePlayerSave(p);
            PlayerManagement.getInstance().disablePlayerLoad(p);
        }
    }


    public void restoreInventory()
    {
        for (Player p : group)
        {
            PlayerManagement.getInstance().loadPlayerIgnoreDisableSync(p);
            PlayerManagement.getInstance().enablePlayerLoad(p);
            PlayerManagement.getInstance().enablePlayerSave(p);
        }
    }

    public boolean isPlayerInGroup(Player p)
    {
        for (Player pl : group)
        {
            if (pl != null)
            {
                if (pl.getUniqueId() == p.getUniqueId())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean groupMinSizeReached()
    {
        return minSize < getGroupSize() && getFullSlots() >= minSize;
    }

    public void clearGroup()
    {
        Arrays.fill(group, null);
        Arrays.fill(ready, false);
        Arrays.fill(loc, null);
    }

    public boolean isFull()
    {
        for (Player player : group)
        {
            if (player == null)
            {
                return false;
            }
        }
        return true;
    }

    public int getFullSlots()
    {
        int full = 0;
        for (Player player : group)
        {
            if (player != null)
            {
                full++;
            }
        }
        return full;
    }

    public int getGroupSize()
    {
        return group.length;
    }

    public int getFreeGroupSlots()
    {
        int freeslots = 0;
        for (Player player : group)
        {
            if (player == null)
            {
                freeslots++;
            }
        }
        return freeslots;
    }
}
