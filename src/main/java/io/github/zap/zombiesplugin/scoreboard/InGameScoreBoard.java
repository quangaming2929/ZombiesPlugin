package io.github.zap.zombiesplugin.scoreboard;

import com.destroystokyo.paper.Title;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Update interval 0.5 sec
public class InGameScoreBoard extends BukkitRunnable implements IInGameScoreboard {
    protected final String strToday;
    protected final GameManager manager;
    protected final ScoreboardManager sbManager;
    protected final Title joinTitle;

    protected IngameStatePanel pnlInGame;
    protected PreGameSidePanel pnlPre;

    protected int currentRound = 1;
    protected int zombiesLeft = 1;
    protected float cdTimer;
    protected float igTimer = 0;
    protected GameState state;

    public InGameScoreBoard(GameManager manager) {
        this.manager = manager;
        this.sbManager = Bukkit.getScoreboardManager();

        strToday = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        runTaskTimer(ZombiesPlugin.instance, 0, 10);

        pnlPre = new PreGameSidePanel(strToday, "manager.getMap()", 4);
        joinTitle = new Title(
                ChatColor.BOLD + "" + ChatColor.YELLOW + "Test Game",
                ChatColor.WHITE + "Submit bug and feature request at our github issue",
                10,
                70,
                20 );
    }

    @Override
    public GameManager getManager() {
        return manager;
    }

    @Override
    public void onPlayerJoinGame(User user) {
        if (state == GameState.PREGAME || state == GameState.COUNTDOWN) {
            pnlPre.show(user.getPlayer());
            manager.getPlayerManager().displayTitle(joinTitle);
            setPlayerCount(manager.getPlayerManager().getPlayers().size());
        }
    }

    @Override
    public void onPlayerLeave(User user) {
        if (state == GameState.PREGAME || state == GameState.COUNTDOWN) {
            pnlPre.show(null);
            setPlayerCount(manager.getPlayerManager().getPlayers().size());
        }
    }

    @Override
    public void setPlayerCount(int count) {
        pnlPre.setPlayerCount(count);
    }

    @Override
    public void setGameState(GameState state) {
        if (state == GameState.PREGAME || state == GameState.COUNTDOWN) {
            for(User user : manager.getPlayerManager().getPlayers()) {
                pnlPre.show(user.getPlayer());
            }
            cdTimer = 20;

            if (state == GameState.PREGAME) {
                // TODO: Remove test code
                manager.getPlayerManager().displayTitle(joinTitle);
                pnlPre.setState(false);
            }
            else if (state == GameState.COUNTDOWN) {
                pnlPre.setState(true);
                pnlPre.setCountDownTime(20);
                manager.getPlayerManager().broadcast(ChatColor.YELLOW + "The game starts in 20 seconds!", false);
            }
        } else if (state == GameState.STARTED) {
            pnlInGame = new IngameStatePanel(manager.getPlayerManager().getPlayers(), "Test Map Bro", strToday);
            pnlInGame.show();
            Objective obj = null;
            obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }

        this.state = state;
    }

    @Override
    public void setZombiesCount(int count) {
        this.zombiesLeft = count;
    }

    @Override
    public void setRound(int round) {
        this.currentRound = round;
    }

    @Override
    public void run() {
        if (state == GameState.COUNTDOWN ) {
            if (cdTimer > 0) {
                System.out.println(cdTimer);
                int display = (int) Math.ceil(cdTimer);
                pnlPre.setCountDownTime(display);

                String cdChat = ChatColor.YELLOW + "The game starts in ";
                if (cdTimer == 10) {
                    manager.getPlayerManager().displayTitle(new Title(ChatColor.GOLD + String.valueOf(display)));
                    manager.getPlayerManager().broadcast(cdChat + ChatColor.GOLD + display + ChatColor.YELLOW + " seconds!", false);
                } else if (cdTimer <= 5 && cdTimer > 1 && cdTimer==Math.round(cdTimer)) {
                    manager.getPlayerManager().displayTitle(new Title(ChatColor.RED + String.valueOf(display)));
                    manager.getPlayerManager().broadcast(cdChat + ChatColor.RED + display + ChatColor.YELLOW + " seconds!", false);
                } else if (cdTimer == 1) {
                    manager.getPlayerManager().displayTitle(new Title(ChatColor.RED + String.valueOf(display), "", 10, 10, 0));
                    manager.getPlayerManager().broadcast(cdChat + ChatColor.RED + display + ChatColor.YELLOW + " second!", false);
                }
                cdTimer -= 0.5f;
            } else{
                manager.setState(GameState.STARTED);
            }

        }
        else if (state == GameState.STARTED) {
            pnlInGame.setTime(getTimeStringFromSeconds(igTimer));
            for (User user : manager.getPlayerManager().getPlayers()) {
                switch (user.getState()) {
                    case ALIVE:
                        pnlInGame.setPlayerStats(user, ChatColor.GOLD + "" + user.getGold());
                        break;
                    case KNOCKED_DOWN:
                        pnlInGame.setPlayerStats(user, ChatColor.YELLOW + "REVIVE");
                        break;
                    case DEAD:
                        pnlInGame.setPlayerStats(user, ChatColor.RED + "DEAD");
                        break;
                    case QUIT:
                        pnlInGame.setPlayerStats(user, ChatColor.RED + "QUIT");
                }
            }

            igTimer += 0.5f;
        }
    }

    private String getTimeStringFromSeconds(float seconds) {
        int hour = (int) Math.floor(seconds / 3600);
        float remainder = seconds - hour * 3600;
        int min = (int) Math.floor(remainder / 60);
        remainder -= min * 60;
        return hour < 1 ? String.format("%02d:%02d", min, (int)remainder) : String.format("%02d:%02d:%02d", hour, min, (int)remainder);

    }
}
