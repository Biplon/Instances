package instance.java.Task;

import instance.java.Struct.CreatureWaveEntity;

import java.util.ArrayList;

public class TaskCreatureWave extends Task
{
    public final int waveId;

    public final ArrayList<CreatureWaveEntity> waveMonsters = new ArrayList();

    public TaskCreatureWave(int waveId, double wavePreCountdown, boolean autostart, int number)
    {
        this.waveId = waveId;
        this.preCountdown = wavePreCountdown;
        this.autostart = autostart;
        this.number = number;
    }

    public void addCreatureWaveEntity(CreatureWaveEntity waveMonster)
    {
        waveMonsters.add(waveMonster);
    }

    @Override
    public String toString()
    {
        return "Wave{" +
                "waveId=" + waveId +
                ", wavePreCountdown=" + preCountdown +
                ", autostart=" + autostart +
                '}';
    }
}
