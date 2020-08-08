package instance.java.Manager;

import instance.java.Effect.Effect;

import java.util.ArrayList;
import java.util.List;

public class EffectPresetManager
{
    private static EffectPresetManager instance;

    public static EffectPresetManager getInstance()
    {
        return instance;
    }

    private final List<Effect> effectList = new ArrayList<>();

    public List<Effect> getEffectList()
    {
        return effectList;
    }

    public EffectPresetManager()
    {
        instance = this;
    }

    public void loadEffectPresets()
    {
        //load stuff
    }

    public Effect getEffect(int id)
    {
        return null;
    }
}
