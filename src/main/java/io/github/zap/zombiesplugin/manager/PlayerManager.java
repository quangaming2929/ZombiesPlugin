package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.navmesh.Direction;
import io.github.zap.zombiesplugin.navmesh.NavmeshGenerator;
import io.github.zap.zombiesplugin.utils.CollectionUtils;
import java.util.List;
import java.util.logging.Level;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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
        System.out.println("onPlayerUse");
        if(CollectionUtils.ReferenceContains(players, player)) {
            //right click code here
        }

        //navmesh testing
        /*Vector origin = MathUtils.pushVectorAlong(event.getClickedBlock().getLocation().toVector(), Direction.UP, 1);
        NavmeshGenerator generator = new NavmeshGenerator(event.getPlayer().getWorld(), 3,
                new Vector(origin.getBlockX() - 50, origin.getBlockY() - 50, origin.getBlockZ() - 50),
                new Vector(origin.getBlockX() + 50, origin.getBlockY() + 50,origin.getBlockZ() + 50));

        generator.generateNavmesh(origin, null);*/
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        System.out.println("onPlayerBreakBlock");
        if(event.getBlock().getType() == Material.BARREL) {
            Vector vector = event.getBlock().getLocation().toVector();

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