package io.github.zap.zombiesplugin.manager;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.arenaproviders.MultiverseArena;
import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

public class MultiverseHook extends GameManagerHook implements Listener {
    protected final MultiverseArena arenaProvider  = (MultiverseArena) ZombiesPlugin.instance.getDimensionManager().getArenaProvider();

    public MultiverseHook(GameManager manager) {
        super(manager);
        Bukkit.getPluginManager().registerEvents(this, ZombiesPlugin.instance); // Self register
    }

    @EventHandler
    public void onPlayerChangeWorld (PlayerChangedWorldEvent e) {
        User user = getUserManager().getAssociatedUser(e.getPlayer());
        if (user != null) {
            MultiverseWorld arena = arenaProvider.getRoomArena(getManager().getContainingRoom());

            if (arena != null) {
                if (e.getFrom() == arena.getCBWorld()) {
                    quitGame(user);
                } else if (e.getPlayer().getWorld() == arena.getCBWorld()) {
                    rejoinGame(user);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveServer (PlayerQuitEvent e) {
        User user = getUserManager().getAssociatedUser(e.getPlayer());
        if (user != null) {
            quitGame(user);
        }
    }

    @EventHandler
    public void onPlayerJoinServer (PlayerJoinEvent e) {
        User user = getUserManager().getAssociatedUser(e.getPlayer());
        MultiverseWorld arena = arenaProvider.getRoomArena(getManager().getContainingRoom());
        if (arena != null && user != null) {
            if (e.getPlayer().getWorld() == arena.getCBWorld()) {
                rejoinGame(user);
            }
        }
    }

    private void quitGame (User user) {
        user.setState(PlayerState.QUIT);
        getManager().getScoreboard().invalidateScoreboards();
    }

    private void rejoinGame (User user) {
        user.setState(PlayerState.DEAD);
        getManager().getScoreboard().invalidateScoreboards();
    }

    @Override
    protected void onGameEnded() {
        HandlerList.unregisterAll(this); // Unregister this hook
    }
}
