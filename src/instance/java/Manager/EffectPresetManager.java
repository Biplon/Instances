package instance.java.Manager;

import instance.java.Effect.*;
import instance.java.Enum.EEffectType;
import instance.java.Instances;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        try
        {
            File file = new File( Instances.getInstance().getDataFolder()  + File.separator + "presets" + File.separator + "effect.txt");
            if (!file.exists())
            {
                file.mkdir();
            }
            BufferedReader reader = null;

            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null)
            {
                String[] tmp = text.split(",");
                createEffect(tmp);
            }
            reader.close();
        }
        catch (Exception ec)
        {
            ec.getMessage();
        }

    }

    private void createEffect(String[] values)
    {
        EEffectType etmp = EEffectType.valueOf(values[1]);
        switch (etmp)
        {
            case SpawnCreature:
                effectList.add(new EffectSpawnCreature(Integer.parseInt(values[0]),true,false,Integer.parseInt(values[3]),values[4],Integer.parseInt(values[5])));
                break;
            case ChangeBlock:
                effectList.add(new EffectChangeBlock(Integer.parseInt(values[0]),true,false,Integer.parseInt(values[3]), Material.valueOf(values[4])));
                break;
            case SetBlock:
                effectList.add(new EffectSetBlock(Integer.parseInt(values[0]),true,false,Integer.parseInt(values[3]), Material.valueOf(values[4])));
                break;
            case PlayerSound:
                effectList.add(new EffectPlayerSound(Integer.parseInt(values[0]),false,Boolean.parseBoolean(values[2]),Integer.parseInt(values[3]), Sound.valueOf(values[4])));
                break;
            case Particle:
                effectList.add(new EffectParticle(Integer.parseInt(values[0]),true,false,Integer.parseInt(values[3]), Particle.valueOf(values[4])));
                break;
            case GivePlayerPotionEffect:
                effectList.add(new EffectGivePlayerPotionEffect(Integer.parseInt(values[0]),false,Boolean.parseBoolean(values[2]),Integer.parseInt(values[3]), PotionType.valueOf(values[4])));
                break;
            case GivePlayerItem:
                effectList.add(new EffectGivePlayerItem(Integer.parseInt(values[0]),false,Boolean.parseBoolean(values[2]),Integer.parseInt(values[3]), Material.valueOf(values[4]),Integer.parseInt(values[5])));
                break;
            case CreateExplosion:
                effectList.add(new EffectCreateExplosion(Integer.parseInt(values[0]),true,false,Integer.parseInt(values[3]), Boolean.parseBoolean(values[4])));
                break;
        }
    }

    public Effect getEffect(int id)
    {
        for (Effect e:effectList)
        {
            if (e.getId() == id)
            {
                return e;
            }
        }
        Bukkit.getLogger().warning("Effect prefab with id: " + id + " not found !");
        return null;
    }

    public Effect createEffect(FileConfiguration cfg, int count)
    {
        //TODO implement
        return null;
    }
}
