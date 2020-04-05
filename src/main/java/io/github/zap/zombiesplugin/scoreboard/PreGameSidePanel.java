package io.github.zap.zombiesplugin.scoreboard;

import static io.github.zap.zombiesplugin.utils.ScoreboardUtils.createTeam;
import static io.github.zap.zombiesplugin.utils.ScoreboardUtils.getBlankLine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class PreGameSidePanel {
    private static final String TEAM_GAME_STATS = "gameStats";
    private static final String TEAM_PLAYERS = "players";

    protected final ScoreboardManager manager;
    protected final String strToday;
    protected final String mapName;
    protected final int playerCap;

    protected Scoreboard scoreboard;

    public PreGameSidePanel(String strToday, String map, int playerCap) {
        this.manager = Bukkit.getScoreboardManager();
        this.strToday = strToday;
        this.mapName = map;
        this.playerCap = playerCap;

        prepareScoreBoard();
    }

    private void prepareScoreBoard() {
        Scoreboard sb = manager.getNewScoreboard();
        this.scoreboard = sb;

        Objective obj =  sb.registerNewObjective("sb_zombies_pre", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "ZOMBIES");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.YELLOW + "Closed Beta Test 1").setScore(1);

        getBlankLine(obj, 0).setScore(2);

        String statsEntry = " " + ChatColor.GREEN;
        Team statsTeam = createTeam(sb, TEAM_GAME_STATS, statsEntry);
        setState(false);
        obj.getScore(statsEntry).setScore(3);

        getBlankLine(obj, 1).setScore(4);

        String statsPlayers = "" + ChatColor.WHITE + "Players: " + ChatColor.GREEN;
        Team playerTeam = createTeam(sb, TEAM_PLAYERS, statsPlayers);
        setPlayerCount(0);
        obj.getScore(statsPlayers).setScore(5);

        obj.getScore("" + ChatColor.WHITE + "Map: " + ChatColor.GREEN + mapName).setScore(6);

        getBlankLine(obj, 2).setScore(7);

        obj.getScore("" + ChatColor.GRAY + strToday).setScore(8);
    }

    public void setState(boolean isStarting) {
        Team team = scoreboard.getTeam(TEAM_GAME_STATS);
        if (isStarting) {
            team.setPrefix("" + ChatColor.WHITE + "Starting in");
        } else {
            team.setPrefix("" + ChatColor.WHITE + "Waiting...");
            team.setSuffix("");

        }
    }

    public void setPlayerCount(int count) {
        scoreboard.getTeam(TEAM_PLAYERS).setSuffix(count + "/" + playerCap);
    }

    public void show (Player player) {
        player.setScoreboard(scoreboard);
    }

    public void setCountDownTime(int time) {
        Team team = scoreboard.getTeam(TEAM_GAME_STATS);
        team.setSuffix(String.valueOf(time) + "s");
    }
}
