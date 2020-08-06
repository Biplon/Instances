package instance.java.Task;

import instance.java.Enum.EventType;

public class TaskEventChangePlayerSpawn extends TaskEvent
{
    private final int spawnPointId;

    public int getSpawnPointId()
    {
        return spawnPointId;
    }

    public TaskEventChangePlayerSpawn(int number, double wavePreCountdown, boolean autostart, EventType eventType, int spawnPointId)
    {
        super(number, wavePreCountdown, autostart, eventType);
        this.spawnPointId = spawnPointId;
    }
}
