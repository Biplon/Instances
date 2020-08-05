package instance.java.ManageInstances;

import instance.java.Instances;

public class InstancesManager
{
    private static InstancesManager instance;

    public static InstancesManager getInstance()
    {
        return instance;
    }

    public InstancesManager()
    {
        instance = this;
    }

    public String getInstanceOfPlayer(String playerName)
    {
        return "";
    }
}
