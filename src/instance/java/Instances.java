package instance.java;

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
    }

    private void regCommands()
    {

    }

    private void regEvents()
    {

    }

    @Override
    public void onDisable()
    {

    }
}
