package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.GunUser;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.utils.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager implements Listener {


    private GameManager gameManager;

    private int gameSize = 4;
    private List<User> players = new ArrayList<>();
    private ArrayList<Player> spectators;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;

        //self-register
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public List<User> getPlayers() {
        return players;
    }

    /**
     * Returns true if the player is successfully added; returns false otherwise.
     * @param player The player to add
     * @return Whether or not the player was added
     */
    public boolean addPlayer(Player player) {
        if(players.size() - 1 < getGameSize())
        {
            for(User user : players) {
                if(user.getPlayer() == player) // Check if the player already exist in this manager
                    return false;
            }
        }

        players.add(new User(player));
        return  true;
    }

    /**
     * Returns true if the player is successfully removed; returns false otherwise.
     * @param player The player to remove
     * @return Whether or not the player was removed
     */
    public boolean removePlayer(Player player) {
        User associatedPlayer = getAssociatedUser(player);
        if(associatedPlayer != null) {
            this.players.remove(associatedPlayer);
            return true;
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
        User user = getAssociatedUser(event.getPlayer());
        if(user != null) {
            Gun gun = user.getGunUser().getGunByItemStack(user.getPlayer().getInventory().getItemInMainHand());
            if(gun != null ) {
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() ==  Action.RIGHT_CLICK_BLOCK)
                    gun.shoot();
                else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() ==  Action.LEFT_CLICK_BLOCK)
                    gun.reload();
            }

            // Pass the event to hotbar manager
            user.getHotbar().processEvent(event);
        }
    }

    @EventHandler
    public void onPlayerSwitchHeldItem(PlayerItemHeldEvent event) {
        User user = getAssociatedUser(event.getPlayer());
        if(user != null) {


            // Pass the event to hotbar manager
            user.getHotbar().processEvent(event);
        }
    }

    public User getAssociatedUser(Player player) {
        for (User p : players) {
            if (p.getPlayer() == player) {
                return p;
            }
        }

        return null;
    }
}