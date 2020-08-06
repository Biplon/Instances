package instance.java.Config;

import instance.java.Instances;
import instance.java.Task.TaskCreatureWave;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager
{
    static LanguageManager instance;

    public LanguageManager()
    {
        instance = this;
    }

    public static LanguageManager getInstance()
    {
        return instance;
    }

    public String joinWindowHeadText;
    public String playerText;
    public String ownInvText;

    public String readyWindowHeadText;
    public String readyText;
    public String notReadyText;

    public String leaveWindowHeadText;
    public String yesLeaveText;
    public String noLeaveText;

    public String canNotJoinText;
    public String canNotJoinTextConsoleText;
    public String notInInstanceText;
    public String notInInstanceConsoleText;

    public String groupFullText;
    public String ifReadyText;

    public String joinedGroupText;
    public String groupText;
    public String playerNeedText;
    public String preStartText;

    public String playerLeaveInstanceText;

    public String waveStartTimeText;
    public String waveStartedText;
    public String waveClearedText;
    public String winText;
    public String loseText;

    public String canNotStartReadyCheckText;

    public void loadLang()
    {
        File configFile = new File("plugins" + File.separator + Instances.getInstance().getName() + File.separator + Instances.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

        joinWindowHeadText = cfg.getString("joinWindowHeadText") != null ? cfg.getString("joinWindowHeadText") : "Select instance";
        playerText = cfg.getString("playerText") != null ? cfg.getString("playerText") : "player";
        ownInvText = cfg.getString("ownInvText") != null ? cfg.getString("ownInvText") : "Own inventory:";
        readyWindowHeadText = cfg.getString("readyWindowHeadText") != null ? cfg.getString("readyWindowHeadText") : "Ready check";
        readyText = cfg.getString("readyText") != null ? cfg.getString("readyText") : "Ready";
        notReadyText = cfg.getString("notReadyText") != null ? cfg.getString("notReadyText") : "Not ready";
        leaveWindowHeadText = cfg.getString("leaveWindowHeadText") != null ? cfg.getString("leaveWindowHeadText") : "Leave instance";
        yesLeaveText = cfg.getString("yesLeaveText") != null ? cfg.getString("yesLeaveText") : "Yes leave";
        noLeaveText = cfg.getString("noLeaveText") != null ? cfg.getString("noLeaveText") : "No";

        canNotJoinText = cfg.getString("canNotJoinText") != null ? cfg.getString("canNotJoinText") : "Can not join instance. Max visits per hour or already in a group!";
        canNotJoinTextConsoleText = cfg.getString("canNotJoinTextConsoleText") != null ? cfg.getString("canNotJoinTextConsoleText") : "Player can not join instance. Max visits per hour or already in a group!";

        notInInstanceText = cfg.getString("notInInstanceText") != null ? cfg.getString("notInInstanceText") : "You are in no instance";
        notInInstanceConsoleText = cfg.getString("notInInstanceConsoleText") != null ? cfg.getString("notInInstanceConsoleText") : "Player in no instance";

        groupFullText = cfg.getString("groupFullText") != null ? cfg.getString("groupFullText") : "Group full! For:";
        ifReadyText = cfg.getString("ifReadyText") != null ? cfg.getString("ifReadyText") : "If you ready type:";

        joinedGroupText = cfg.getString("joinedGroupText") != null ? cfg.getString("joinedGroupText") : "joined the group!";
        groupText = cfg.getString("groupText") != null ? cfg.getString("groupText") : "Group";
        playerNeedText = cfg.getString("playerNeedText") != null ? cfg.getString("playerNeedText") : "You Need: %Slots% Player to start";
        preStartText = cfg.getString("preStartText") != null ? cfg.getString("preStartText") : "You have the min Group size. Do you want to start ?";
        playerLeaveInstanceText = cfg.getString("playerLeaveInstanceText") != null ? cfg.getString("playerLeaveInstanceText") : "leaved the instance group!";

        waveStartTimeText = cfg.getString("waveStartTimeText") != null ? cfg.getString("waveStartTimeText") : "Wave start in %time% sec!";
        waveStartedText = cfg.getString("waveStartedText") != null ? cfg.getString("waveStartedText") : "Wave started!";
        waveClearedText = cfg.getString("waveClearedText") != null ? cfg.getString("waveClearedText") : "Wave cleared!";
        winText = cfg.getString("winText") != null ? cfg.getString("winText") : "Instance finished!";
        loseText = cfg.getString("loseText") != null ? cfg.getString("loseText") : "Instance failed!";

        canNotStartReadyCheckText = cfg.getString("canNotStartReadyCheckText") != null ? cfg.getString("canNotStartReadyCheckText") : "Cant start ready check";
    }
}
