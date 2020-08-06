package instance.java.Command;

import instance.java.Config.LanguageManager;
import instance.java.GUI.GUIManager;
import instance.java.ManageInstances.InstancesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStartPreReadyCheck implements CommandExecutor
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
                if (InstancesManager.getInstance().getInstanceOfPlayer(player) !=null)
                {
                    if (InstancesManager.getInstance().getInstanceOfPlayer(player).getMyGroup().groupMinSizeReached())
                    {
                        InstancesManager.getInstance().getInstanceOfPlayer(player).getMyGroup().readyCheck();
                    }
                    else
                    {
                        player.sendMessage(LanguageManager.getInstance().canNotStartReadyCheckText);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
