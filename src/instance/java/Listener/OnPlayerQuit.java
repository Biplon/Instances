package instance.java.Listener;

import instance.java.ManageInstances.InstancesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onDisconnect(final PlayerQuitEvent event)
    {
        InstancesManager.getInstance().leavePlayer(event.getPlayer(), true);
    }
}