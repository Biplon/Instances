package instance.java.Task;

import instance.java.Enum.EEventType;

public class TaskEventChangePlayerSpawn extends TaskEvent
{
    private final int spawnPointId;

    public int getSpawnPointId()
    {
        return spawnPointId;
    }

    public TaskEventChangePlayerSpawn(int number, double wavePreCountdown, boolean autostart, EEventType EEventType, int spawnPointId)
    {
        super(number, wavePreCountdown, autostart, EEventType);
        this.spawnPointId = spawnPointId;
    }
}
