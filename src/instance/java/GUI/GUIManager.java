package instance.java.GUI;

import instance.java.ManageInstances.InstancesManager;
import org.bukkit.entity.Player;

public class GUIManager
{
    private static GUIManager instance;

    public static GUIManager getInstance()
    {
        return instance;
    }

    public GUIManager()
    {
        instance = this;
    }


    public void openJoinInstanceGUI(Player p,String instance)
    {

    }

    public void openLeaveInstanceGUI(Player p,String instance)
    {

    }
}
