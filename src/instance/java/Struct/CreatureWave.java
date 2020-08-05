package instance.java.Struct;

import java.util.ArrayList;

public class CreatureWave
{
    public final int waveid;

    public final double waveprecountdown;

    public final boolean autostart;

    public final ArrayList<CreatureWaveEntity> waveMonsters = new ArrayList();

    public CreatureWave(int waveid, double waveprecountdown, boolean autostart)
    {
        this.waveid = waveid;
        this.waveprecountdown = waveprecountdown;
        this.autostart = autostart;
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
                ", waveprecountdown=" + waveprecountdown +
                ", autostart=" + autostart +
                '}';
    }
}
