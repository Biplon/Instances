package instance.java.Struct;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InstanceVisit
{
    public final String instanceName;

    private final List<UUID> visitors = new ArrayList<>();

    public InstanceVisit(String instanceName)
    {
        this.instanceName = instanceName;
    }

    public void addVisitor(UUID pid)
    {
        visitors.add(pid);
    }

    public int getVisits(Player p)
    {
        int count = 0;
        for (UUID id: visitors)
        {
            if (id == p.getUniqueId())
            {
                count++;
            }
        }
        return count;
    }

    public void clearVisitors()
    {
        visitors.clear();
    }
}
