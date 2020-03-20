package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.ai.AStar;
import io.github.zap.zombiesplugin.ai.Path;
import io.github.zap.zombiesplugin.navmesh.Direction;
import io.github.zap.zombiesplugin.navmesh.MeshBlock;
import io.github.zap.zombiesplugin.navmesh.Navmesh;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerManager implements Listener {
    public final int GAME_SIZE = 4;

    private GameManager gameManager;

    private Player[] players = new Player[GAME_SIZE];
    private List<Player> spectators;

    //DEBUG
    NavmeshGenerator generator;
    Navmesh navmesh = null;
    AStar astar = new AStar();

    Vector startVector = null;
    Vector goalVector = null;
    //END DEBUG

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;

        System.out.println("PlayerManager instance created.");
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
        if(event.getHand() == EquipmentSlot.HAND) {
            System.out.println("onPlayerUse: "+event.getAction().toString());

            if(CollectionUtils.ReferenceContains(players, player)) {
                //right click code here
            }

            ItemStack heldItem = player.getInventory().getItemInMainHand();

            if(event.getClickedBlock() == null) {
                System.out.println("No target block.");
                return;
            }

            //TESTING CODE
            if(heldItem.getType() == Material.STICK && navmesh == null) {
                Vector origin = MathUtils.pushVectorAlong(event.getClickedBlock().getLocation().toVector(), Direction.UP, 1);
                generator = new NavmeshGenerator(event.getPlayer().getWorld(), 3,
                        new Vector(origin.getBlockX() - 200, origin.getBlockY() - 200, origin.getBlockZ() - 200),
                        new Vector(origin.getBlockX() + 200, origin.getBlockY() + 200, origin.getBlockZ() + 200));

                navmesh = generator.generateNavmesh(origin);
            }
            else if(heldItem.getType() == Material.CARROT_ON_A_STICK) {
                if(navmesh != null && startVector == null) {
                    startVector = MathUtils.pushVectorAlong(event.getClickedBlock().getLocation().toVector(), Direction.UP, 1);
                }
                else if(navmesh != null && goalVector == null) {
                    goalVector = MathUtils.pushVectorAlong(event.getClickedBlock().getLocation().toVector(), Direction.UP, 1);
                    Path result = astar.navigateTo(navmesh.getMeshBlocks().get(startVector).getNode(), navmesh.getMeshBlocks().get(goalVector).getNode(), 2);

                    startVector = null;
                    goalVector = null;

                    if(result == null) {
                        System.out.println("Path is impossible or an error occurred.");
                        return;
                    }

                    Path.Segment segment = result.getFirst();
                    do {
                        player.getWorld().getBlockAt(segment.start.getBlockX(), segment.start.getBlockY(), segment.start.getBlockZ()).setType(Material.IRON_BLOCK);
                        segment = segment.next;
                    }
                    while(segment != null);
                }
            }
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