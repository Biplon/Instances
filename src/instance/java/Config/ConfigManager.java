package instance.java.Config;

import instance.java.GUI.GUIManager;

public class ConfigManager
{
    private static ConfigManager instance;

    public static ConfigManager getInstance()
    {
        return instance;
    }

    public ConfigManager()
    {
        instance = this;
    }

}
