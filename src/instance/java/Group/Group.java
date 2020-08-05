package instance.java.Group;

import instance.java.GUI.GUIManager;
import instance.java.ManageInstances.PlayerInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pts.java.api.PlayerManagement;

import java.util.Arrays;

public class Group
{
    private final PlayerInstance myinstance;

    private final int minsize;

    private Player[] group;

    public boolean[] ready;

    private Location[] loc;

    public Player[] getGroup()
    {
        return group;
    }

    public Location[] getPlayerLocation()
    {
        return loc;
    }

    public Group(PlayerInstance instance,int minsize,int groupSize)
    {
        this.myinstance = instance;
        this.minsize = minsize;
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
                        myinstance.sendMessage(p.getDisplayName() +" joint the group");
                        myinstance.sendMessage( "Group: " + getFullSlots()+ "/" + getGroupSize());
                        myinstance.sendMessage( "You Need: " + getFreeGroupSlots()+ " Player to start");
                    }
                    if (groupMinSizeReached())
                    {
                        myinstance.sendMessageWithClickEvent("You have the min Group size. Do you want to start ?","/hgstartreadycheck",true);
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

    public void savePlayerloc()
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
                if (myinstance.isInuse())
                {
                    myinstance.teleportPlayerBack(p,loc[i]);
                    if (!disconnect)
                    {
                        PlayerManagement.getInstance().loadPlayerIgnoreDisableSync(p);
                    }
                    PlayerManagement.getInstance().enablePlayerLoad(p);
                    PlayerManagement.getInstance().enablePlayerSave(p);
                }
                group[i] = null;
                ready[i] = false;
                loc[i] = null;
                if (group.length > 1)
                {
                    myinstance.sendMessage(p.getDisplayName() + "Leave the hunting group");
                    if (!myinstance.isInuse())
                    {
                        myinstance.sendMessage( "Group: " + getFullSlots()+ "/" + getGroupSize());
                        myinstance.sendMessage( "You Need: " + getFreeGroupSlots()+ " Player to start");
                    }
                }
                if (getFreeGroupSlots() == getGroupSize())
                {
                    myinstance.resetHuntingGround();
                }
                return;
            }
        }
    }

    public void readyCheck()
    {
        for (Player p: group)
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
        if (minsize < getGroupSize() && getFullSlots() >= minsize)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void clearGroup()
    {
        Arrays.fill(group, null);
        Arrays.fill(ready, false);
        Arrays.fill(loc,null);
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
