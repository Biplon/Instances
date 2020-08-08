package instance.java.GUI;

import instance.java.Config.LanguageManager;
import instance.java.Manager.InstancesManager;
import instance.java.Instance.PlayerInstance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIManager
{
    private static GUIManager instance;

    public static GUIManager getInstance()
    {
        return instance;
    }

    private static final ItemStack defaultGuiItem = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " ", "");

    public GUIManager()
    {
        instance = this;
    }


    public void openJoinInstanceGUI(Player p, String instance)
    {
        Inventory inv = Bukkit.createInventory(null, 45, "IN:" + LanguageManager.getInstance().joinWindowHeadText + " " + instance);
        for (int i = 0; i < 9; i++)
        {
            inv.setItem(i, defaultGuiItem);
            inv.setItem(i + 36, defaultGuiItem);
        }
        setInstance(inv, instance);
        p.openInventory(inv);
    }

    public void openLeaveInstanceGUI(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 27, "IN: " + LanguageManager.getInstance().leaveWindowHeadText);
        createDefaultLayoutSmall(inv);
        setLeave(inv);
        p.openInventory(inv);
    }

    public void openReadyCheckGUI(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 27, "IN: " + LanguageManager.getInstance().readyWindowHeadText);
        createDefaultLayoutSmall(inv);
        setReady(inv);
        p.openInventory(inv);
    }

    private void setLeave(Inventory inv)
    {
        inv.setItem(12, createGuiItem(Material.GREEN_WOOL, LanguageManager.getInstance().yesLeaveText));
        inv.setItem(14, createGuiItem(Material.RED_WOOL, LanguageManager.getInstance().noLeaveText));
    }

    private void setReady(Inventory inv)
    {
        inv.setItem(12, createGuiItem(Material.GREEN_WOOL, LanguageManager.getInstance().readyText));
        inv.setItem(14, createGuiItem(Material.RED_WOOL, LanguageManager.getInstance().notReadyText));
    }

    private void setInstance(Inventory inv, String instance)
    {
        int count = 9;
        if (InstancesManager.getInstance().getPlayerInstanceConfig(instance).getInstances() != null)
        {
            for (PlayerInstance pi : InstancesManager.getInstance().getPlayerInstanceConfig(instance).getInstances())
            {
                if (!pi.isInUse())
                {
                    inv.setItem(count, createGuiItem(Material.GREEN_DYE, pi.getMyConfig().getInstanceName() + ":" + pi.getId(), pi.getMyGroup().getFullSlots() + "/" + pi.getMyGroup().getGroupSize() + " " + LanguageManager.getInstance().playerText, LanguageManager.getInstance().ownInvText + ": " + pi.getMyConfig().getPlayerOwnInventory()));
                }
                else
                {
                    inv.setItem(count, createGuiItem(Material.GREEN_DYE, pi.getMyConfig().getInstanceName() + ":" + pi.getId(), pi.getMyGroup().getFullSlots() + "/" + pi.getMyGroup().getGroupSize() + " " + LanguageManager.getInstance().playerText, LanguageManager.getInstance().ownInvText + ": " + pi.getMyConfig().getPlayerOwnInventory()));
                }
                count++;
            }
        }
    }

    private void createDefaultLayoutSmall(Inventory inv)
    {
        for (int i = 0; i < 9; i++)
        {
            inv.setItem(i, defaultGuiItem);
            inv.setItem(i + 18, defaultGuiItem);
        }
        inv.setItem(13, defaultGuiItem);
        for (int i = 9; i < 12; i++)
        {
            inv.setItem(i, defaultGuiItem);
            inv.setItem(i + 6, defaultGuiItem);
        }
    }

    private static ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack createGuiItem(final Material material, int amount, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}
