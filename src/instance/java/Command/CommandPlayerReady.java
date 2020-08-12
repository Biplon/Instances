package instance.java.Command;

import instance.java.GUI.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandPlayerReady implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (!player.hasPermission("instances.player"))
            {
                return false;
            }
            else
            {
                GUIManager.getInstance().openReadyCheckGUI(player);
                return true;
            }
        }
        else
        {
            if (args.length == 1)
            {
                if (Bukkit.getPlayer(args[0]) != null)
                {
                    GUIManager.getInstance().openReadyCheckGUI(Objects.requireNonNull(Bukkit.getPlayer(args[0])));
                }
                return true;
            }
        }
        return false;
    }
}
