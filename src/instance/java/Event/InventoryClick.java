package instance.java.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryClick implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e)
    {
        if (e.getView().getTitle().contains("In:"))
        {
            e.setCancelled(true);
        }
    }
}
