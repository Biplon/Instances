package instance.java.Command;

import instance.java.Config.LanguageManager;
import instance.java.GUI.GUIManager;
import instance.java.ManageInstances.InstancesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoinInstance implements CommandExecutor
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
                if (args.length == 1)
                {
                    if (InstancesManager.getInstance().canJoin(player, args[0]))
                    {
                        GUIManager.getInstance().openJoinInstanceGUI(player, args[0]);
                    }
                    else
                    {
                        player.sendMessage(LanguageManager.getInstance().canNotJoinText);
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
            if (args.length == 2)
            {
                if (InstancesManager.getInstance().canJoin(Bukkit.getPlayer(args[1]), args[0]))
                {
                    GUIManager.getInstance().openJoinInstanceGUI(Bukkit.getPlayer(args[1]), args[0]);
                }
                else
                {
                    commandSender.sendMessage(LanguageManager.getInstance().canNotJoinTextConsoleText);
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
