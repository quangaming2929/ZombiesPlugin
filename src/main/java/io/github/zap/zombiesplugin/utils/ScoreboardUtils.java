package io.github.zap.zombiesplugin.utils;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardUtils {
    public static Team createTeam (Scoreboard sb, String name, String entry) {
        Team team = sb.registerNewTeam(name);
        team.addEntry(entry);
        team.setSuffix("");
        team.setPrefix("");
        return team;
    }

    public static Score getBlankLine(Objective obj, int count) {
        StringBuilder bd = new StringBuilder();
        for (int i = 0; i < count; i++) {
            bd.append(' ');
        }
        return obj.getScore(bd.toString());
    }
}
