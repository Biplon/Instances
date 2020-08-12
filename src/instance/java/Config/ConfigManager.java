package instance.java.Config;

import instance.java.Instances;

import java.io.File;

public class ConfigManager
{
    public static void loadConfig()
    {
        File configFile = new File("plugins" + File.separator + Instances.getInstance().getName() + File.separator + "config.yml");
        if (!configFile.exists())
        {
            Instances.getInstance().getLogger().info("Creating config ...");
            Instances.getInstance().saveDefaultConfig();
        }
        try
        {
            Instances.getInstance().getLogger().info("Loading the config ...");
            Instances.getInstance().getConfig().load(configFile);
        }
        catch (Exception e)
        {
            Instances.getInstance().getLogger().severe("Could not load the config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
