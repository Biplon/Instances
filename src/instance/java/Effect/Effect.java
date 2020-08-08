package instance.java.Effect;

import javax.xml.stream.Location;

public abstract class Effect
{
    final int id;

    final boolean havePos;

    final boolean allPlayer;

    public int getId()
    {
        return id;
    }

    public boolean isAllPlayer()
    {
        return allPlayer;
    }

    public boolean isHavePos()
    {
        return havePos;
    }

    public Effect(int id, boolean havePos, boolean allPlayer)
    {
        this.id = id;
        this.havePos = havePos;
        this.allPlayer = allPlayer;
    }

    public void startEffect()
    {

    }

    public void startEffect(Location pos)
    {

    }
}
