package instance.java.Command;

import instance.java.Config.LanguageManager;
import instance.java.GUI.GUIManager;
import instance.java.Manager.InstancesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandLeaveInstance implements CommandExecutor
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
                if (args.length == 0)
                {
                    if (InstancesManager.getInstance().getInstanceOfPlayer(player) != null)
                    {
                        GUIManager.getInstance().openLeaveInstanceGUI(player);
                    }
                    else
                    {
                        commandSender.sendMessage(LanguageManager.notInInstanceText);
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            if (args.length == 1)
            {
                if (InstancesManager.getInstance().getInstanceOfPlayer(Bukkit.getPlayer(args[0])) != null)
                {
                    GUIManager.getInstance().openLeaveInstanceGUI(Objects.requireNonNull(Bukkit.getPlayer(args[0])));
                }
                else
                {
                    commandSender.sendMessage(LanguageManager.notInInstanceConsoleText);
                }
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
