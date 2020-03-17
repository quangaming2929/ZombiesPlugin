package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.utils.CollectionUtils;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerManager implements Listener {
    public final int GAME_SIZE = 4;

    private GameManager gameManager;

    private Player[] players = new Player[GAME_SIZE];
    private List<Player> spectators;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;

        //self-register
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    /**
     * Returns true if the player is successfully added; returns false otherwise.
     * @param player The player to add
     * @return Whether or not the player was added
     */
    public boolean addPlayer(Player player) {
        for(int i = 0; i < players.length; i++) {
            Player playerSample = players[i];
            if(playerSample == null) {
                players[i] = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the player is successfully removed; returns false otherwise.
     * @param player The player to remove
     * @return Whether or not the player was removed
     */
    public boolean removePlayer(Player player) {
        for(int i = 0; i < players.length; i++) {
            Player playerSample = players[i];
            if(playerSample == player) {
                players[i] = null;
                return true;
            }
        }
        return false;
    }

    /*
     * Currently this event is invoked every time ANY player right-clicks, which is obviously not
     * ideal. Might be a way to have the event only apply to the players in the "players" array without
     * having to filter manually, but I wasn't able to find one.
     */
    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(CollectionUtils.ReferenceContains(players, player)) {
            //right click code here
        }
    }

    /** Gets all players in the game
     *
     * @return The players
     */
    public Player[] getActivePlayers() {
        // TODO: Implement
        return null; // TODO: Placeholder
    }

    public Player[] getPlayers() {
        return players;
    }
}