package instance.java.Task;

import instance.java.Struct.CreatureWaveEntity;

import java.util.ArrayList;

public class TaskCreatureWave extends Task
{
    public final int waveid;

    public final ArrayList<CreatureWaveEntity> waveMonsters = new ArrayList();

    public TaskCreatureWave(int waveid, double waveprecountdown, boolean autostart,int number)
    {
        this.waveid = waveid;
        this.precountdown = waveprecountdown;
        this.autostart = autostart;
        this.number = number;
    }

    public void addCreatureWaveEntity(CreatureWaveEntity wavemonster)
    {
        waveMonsters.add(wavemonster);
    }

    @Override
    public String toString()
    {
        return "Wave{" +
                "waveid=" + waveid +
                ", waveprecountdown=" + precountdown +
                ", autostart=" + autostart +
                '}';
    }
}
