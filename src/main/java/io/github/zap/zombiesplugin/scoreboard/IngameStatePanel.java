package io.github.zap.zombiesplugin.scoreboard;

import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static io.github.zap.zombiesplugin.utils.ScoreboardUtils.createTeam;
import static io.github.zap.zombiesplugin.utils.ScoreboardUtils.getBlankLine;

public class IngameStatePanel {
    private static final String TEAM_ZOMBIE_KILL = "zombieKillsLine";
    private static final String TEAM_TIME = "timeLine";
    private static final String TEAM_ZOMBIES_LEFT = "zombieLeftLine";
    private static final String TEAM_ROUND = "roundLine";

    protected final ScoreboardManager manager;
    protected final String strToday;
    protected final String mapName;
    protected Hashtable<User, Scoreboard> users;

    public IngameStatePanel(List<User> users, String mapName, String today) {
        this.manager = Bukkit.getScoreboardManager();
        this.mapName = mapName;
        this.strToday = today;
        prepareScoreboards(users);
    }

    private void prepareScoreboards(List<User> users) {
        this.users = new Hashtable<User, Scoreboard>();

        for (User user : users ) {
            Scoreboard sb = manager.getNewScoreboard();
            this.users.putIfAbsent(user, sb);
            Objective obj =  sb.registerNewObjective("sb_zombies_main", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "ZOMBIES");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            obj.getScore(ChatColor.YELLOW + "Closed Beta Test 1").setScore(1);
            getBlankLine(obj, 0).setScore(2);
            obj.getScore("" + ChatColor.WHITE + "Map: " + ChatColor.GREEN + mapName).setScore(3);

            String timeEntry = "" + ChatColor.WHITE + "Time: ";
            Team timeLine = createTeam(sb, TEAM_TIME, timeEntry);
            obj.getScore(timeEntry).setScore(4);

            String zKillsEntry = "" + ChatColor.WHITE + "Zombies Kills: ";
            Team zombieKillsLine = createTeam(sb, TEAM_ZOMBIE_KILL, zKillsEntry);
            obj.getScore(zKillsEntry).setScore(5);

            getBlankLine(obj, 1).setScore(6);

            int scoreCounter = 7;
            for (User userSB : users) {
                String teamName = "p_" + userSB.getPlayer().getDisplayName();
                String entry = "" + ChatColor.GRAY + userSB.getPlayer().getDisplayName() + ": ";
                System.out.println(teamName + ", " + user.getPlayer().getDisplayName());
                createTeam(sb, teamName, entry);
                obj.getScore(entry).setScore(scoreCounter);
                setPlayerStats(userSB, ChatColor.GOLD + "0");
                scoreCounter ++;
            }
            getBlankLine(obj, 2).setScore(scoreCounter);
            scoreCounter++;

            String zLeftEntry = "" + ChatColor.WHITE + "Zombies Left: ";
            Team zombieLeftLine = createTeam(sb, TEAM_ZOMBIES_LEFT, zLeftEntry);
            obj.getScore(zLeftEntry).setScore(scoreCounter);
            scoreCounter++;

            String roundEntry = "" +  ChatColor.RED + ChatColor.BOLD;
            Team roundLine = createTeam(sb, TEAM_ROUND, roundEntry);
            roundLine.setSuffix("Round ");
            obj.getScore(roundEntry).setScore(scoreCounter);
            scoreCounter++;

            getBlankLine(obj, 3).setScore(scoreCounter);
            scoreCounter++;
            obj.getScore("" + ChatColor.DARK_GRAY + this.strToday).setScore(scoreCounter);
            scoreCounter++;
        }
    }

    public void show () {
        for (Map.Entry<User, Scoreboard> sbs : users.entrySet()) {
            sbs.getKey().getPlayer().setScoreboard(sbs.getValue());
        }
    }

    public void hide() {
        for (Map.Entry<User, Scoreboard> sbs : users.entrySet()) {
            Player player = sbs.getKey().getPlayer();
            if(player.getScoreboard() == sbs.getValue()) {
                player.setScoreboard(null);
            }
        }
    }

    public void setTime(String time) {
        for (Scoreboard playerSb : users.values()) {
            playerSb.getTeam(TEAM_TIME).setSuffix(ChatColor.GREEN + time);
        }
    }

    public void setZombiesKills (User user, int kills) {
        Scoreboard sb = users.get(user);
        sb.getTeam(TEAM_ZOMBIE_KILL).setSuffix(ChatColor.GREEN + String.valueOf(kills));
    }

    public void setPlayerStats (User userToSet, String stats) {
        for (Scoreboard playerSb : users.values()) {
            playerSb.getTeam("p_" + userToSet.getPlayer().getDisplayName()).setSuffix(stats);
        }
    }

    public void setZombiesLeft (int count) {
        for (Scoreboard playerSb : users.values()) {
            playerSb.getTeam(TEAM_ZOMBIES_LEFT).setSuffix(ChatColor.GREEN + String.valueOf(count));
        }
    }

    public void setRound (int round) {
        for (Scoreboard playerSb : users.values()) {
            playerSb.getTeam(TEAM_ROUND).setSuffix("Round " + String.valueOf(round));
        }
    }

    public void setGameOver () {
        for (Scoreboard sb : users.values()) {
            Team a = sb.getTeam(TEAM_ROUND);
            a.setSuffix("Game Over!");
        }

    }

    private Objective getMainObjective(Scoreboard sb) {
        return sb.getObjective("sb_zombies_main");
    }
}
