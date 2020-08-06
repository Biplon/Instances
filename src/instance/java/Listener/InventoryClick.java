package instance.java.Listener;

import instance.java.Config.LanguageManager;
import instance.java.ManageInstances.InstancesManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryClick implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (!e.getView().getTitle().contains("IN:"))
        {
            return;
        }
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
        {
            return;
        }

        final Player p = (Player) e.getWhoClicked();
        String[] values;
        String name = "";
        int id = 0;

        if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().contains(":"))
        {
            values = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().split(":");
            name = values[0];
            id = Integer.parseInt(values[1]);
            if (InstancesManager.getInstance().getPlayerInstanceConfig(name).idExist(id))
            {

                if (!InstancesManager.getInstance().getPlayerInstanceConfig(name).getInstanceById(id).getMyGroup().isFull())
                {
                    p.closeInventory();
                    InstancesManager.getInstance().getPlayerInstanceConfig(name).getInstanceById(id).getMyGroup().addPlayer(p);
                }
                else
                {
                    p.sendMessage(LanguageManager.getInstance().groupFullText + " " + name);
                    e.setCancelled(true);
                }
            }
        }
        else
        {
            name = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
        }

        if (name.equalsIgnoreCase(LanguageManager.getInstance().readyText))
        {
            InstancesManager.getInstance().setPlayerReady(p);
            p.closeInventory();
        }
        else if (name.equalsIgnoreCase(LanguageManager.getInstance().notReadyText))
        {
            p.sendMessage(LanguageManager.getInstance().ifReadyText + " /iready");
            p.closeInventory();
        }
        else if (name.equalsIgnoreCase(LanguageManager.getInstance().yesLeaveText))
        {
            InstancesManager.getInstance().leavePlayer(p, false);
            p.closeInventory();
        }
        else if (name.equalsIgnoreCase(LanguageManager.getInstance().noLeaveText))
        {
            p.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e)
    {
        if (e.getView().getTitle().contains("IN:"))
        {
            e.setCancelled(true);
        }
    }
}
