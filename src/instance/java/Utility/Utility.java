package instance.java.Utility;

import instance.java.Config.LanguageManager;
import instance.java.Group.Group;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Utility
{
    public static void search(final String pattern, final File folder, List<String> result)
    {
        try
        {
            for (final File f : Objects.requireNonNull(folder.listFiles()))
            {
                if (f.isFile())
                {
                    if (f.getName().matches(pattern))
                    {
                        result.add(f.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception ignored)
        {

        }
    }

    public static void sendMessage(String msg, Group g)
    {
        for (Player p : g.getGroup())
        {
            if (p != null)
            {
                p.sendMessage(msg);
            }
        }
    }

    public static void sendActionbarMessage(String msg, Group g)
    {
        for (Player p : g.getGroup())
        {
            if (p != null)
            {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            }
        }
    }

    public static void sendMessageWithClickEvent(String clickableText, String command, Group g, boolean leader)
    {
        TextComponent message = new TextComponent(LanguageManager.preStartTextClick);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        ComponentBuilder tmp = new ComponentBuilder();
        tmp.append(clickableText);
        tmp.append(message);
        for (Player p : g.getGroup())
        {
            if (p != null)
            {
                p.spigot().sendMessage(tmp.create());
                if (leader)
                {
                    return;
                }
            }
        }
    }
}
