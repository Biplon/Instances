package instance.java.Listener;

import instance.java.Manager.InstancesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnPlayerInteract implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        if (InstancesManager.getInstance().isPlayerInRunningInstance(p))
        {
            InstancesManager.getInstance().checkReachObjectInstances(event);
            InstancesManager.getInstance().checkTriggerPIE(event);
        }
    }
}