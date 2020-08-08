package instance.java;

import instance.java.Command.CommandJoinInstance;
import instance.java.Command.CommandLeaveInstance;
import instance.java.Command.CommandStartPreReadyCheck;
import instance.java.Config.ConfigManager;
import instance.java.Config.LanguageManager;
import instance.java.Listener.*;
import instance.java.GUI.GUIManager;
import instance.java.Manager.EffectPresetManager;
import instance.java.Manager.InstancesManager;
import instance.java.Manager.PlayerVisitInstanceManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Instances extends JavaPlugin
{

    private static Instances instance;

    public static Instances getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
        new EffectPresetManager();
        new InstancesManager();
        new PlayerVisitInstanceManager();
        new GUIManager();
        new ConfigManager();
        ConfigManager.getInstance().loadConfig();
        new LanguageManager();
        EffectPresetManager.getInstance().loadEffectPresets();
        LanguageManager.getInstance().loadLang();
        InstancesManager.getInstance().loadInstances();
        regCommands();
        regEvents();
    }

    private void regCommands()
    {
        this.getCommand("ijoin").setExecutor(new CommandJoinInstance());
        this.getCommand("ileave").setExecutor(new CommandLeaveInstance());
        this.getCommand("iready").setExecutor(new CommandLeaveInstance());
        this.getCommand("istartreadycheck").setExecutor(new CommandStartPreReadyCheck());
    }

    private void regEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnEntityDeath(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new OnPlayerQuit(), this);
        pm.registerEvents(new OnEntityGetDamage(), this);
        pm.registerEvents(new OnPlayerInteract(), this);
    }

    @Override
    public void onDisable()
    {

    }
}
