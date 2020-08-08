package instance.java.Listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnPlayerInteract implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
        }
        else if (event.getAction() == Action.PHYSICAL)
        {

        }
    }
}