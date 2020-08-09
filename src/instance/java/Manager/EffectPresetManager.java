package instance.java.Manager;

import instance.java.Effect.Effect;
import instance.java.Instances;

import java.io.File;
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
        File folder = new File(Instances.getInstance().getDataFolder() + "/presets");
        if (!folder.exists())
        {
            folder.mkdir();
        }
        List<String> result = new ArrayList<>();

        //load stuff
    }

    public Effect getEffect(int id)
    {
        return null;
    }

}
