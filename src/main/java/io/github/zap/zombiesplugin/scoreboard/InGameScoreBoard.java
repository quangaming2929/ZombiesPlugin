package io.github.zap.zombiesplugin.scoreboard;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InGameScoreBoard extends BukkitRunnable implements IInGameScoreboard {
    protected final String strToday;
    protected final GameManager manager;
    protected final ScoreboardManager sbManager;
    protected int currentRound = 1;
    protected int zombiesLeft = 1;
    protected GameState state;

    public InGameScoreBoard(GameManager manager) {
        this.manager = manager;
        this.sbManager = Bukkit.getScoreboardManager();

        strToday = LocalDate.now().format(DateTimeFormatter.ofPattern("MM dd yy"));

        runTaskTimer(ZombiesPlugin.instance, 0, 10);
    }

    @Override
    public GameManager getManager() {
        return manager;
    }

    @Override
    public void onPlayerJoinGame(User user) {

    }

    @Override
    public void onPlayerLeave(User user) {

    }

    @Override
    public void setPlayerCount(int count) {

    }

    @Override
    public void setGameState(GameState state) {

    }

    @Override
    public void setZombiesCount(int count) {

    }

    @Override
    public void setRound(int round) {

    }

    @Override
    public void run() {

    }
}
