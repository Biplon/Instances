package instance.java.Command;

import instance.java.GUI.GUIManager;
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
                    GUIManager.getInstance().openJoinInstanceGUI(player,args[0]);
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
                GUIManager.getInstance().openJoinInstanceGUI(Bukkit.getPlayer(args[1]),args[0]);
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
