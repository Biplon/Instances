package instance.java;

import instance.java.Command.CommandJoinInstance;
import instance.java.Command.CommandLeaveInstance;
import instance.java.Command.CommandPlayerReady;
import instance.java.Command.CommandStartPreReadyCheck;
import instance.java.Config.ConfigManager;
import instance.java.Config.LanguageManager;
import instance.java.Listener.*;
import instance.java.GUI.GUIManager;
import instance.java.Manager.EffectPresetManager;
import instance.java.Manager.InstancesManager;
import instance.java.Manager.PlayerVisitInstanceManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
        ConfigManager.loadConfig();
        EffectPresetManager.getInstance().loadEffectPresets();
        LanguageManager.loadLang();
        InstancesManager.getInstance().loadInstances();
        regCommands();
        regEvents();
    }

    private void regCommands()
    {
        Objects.requireNonNull(this.getCommand("ijoin")).setExecutor(new CommandJoinInstance());
        Objects.requireNonNull(this.getCommand("ileave")).setExecutor(new CommandLeaveInstance());
        Objects.requireNonNull(this.getCommand("iready")).setExecutor(new CommandPlayerReady());
        Objects.requireNonNull(this.getCommand("istartreadycheck")).setExecutor(new CommandStartPreReadyCheck());
    }

    private void regEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnEntityDeath(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new OnPlayerQuit(), this);
        pm.registerEvents(new OnEntityGetDamage(), this);
        pm.registerEvents(new OnPlayerInteract(), this);
        //TODO implement events for triggers
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
    }
}
