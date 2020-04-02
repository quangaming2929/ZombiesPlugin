package io.github.zap.zombiesplugin.manager;

import com.destroystokyo.paper.Title;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachine;
import io.github.zap.zombiesplugin.utils.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
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
     * Send messages to all player in this PlayerManager
     * @param str
     */
    public void broadcast(String str, boolean sentToQuitter) {
        // TODO: Filter Send message for quitter
        for (User user : getPlayers()) {
            user.getPlayer().sendMessage(str);
        }
    }

    public void displayTitle(Title title) {
        for (User user : getPlayers()) {
            user.getPlayer().sendTitle(title);
        }
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
                if(user.getPlayer() == player) { // Check if the player already exist in this manager
                    if (gameManager.getScoreboard() != null) {
                        gameManager.getScoreboard().onPlayerJoinGame(user);
                    }
                    return false;
                }
            }
        }

        User newUser = new User(player);
        players.add(newUser);
        if (gameManager.getScoreboard() != null) {
            gameManager.getScoreboard().onPlayerJoinGame(newUser);
        }
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

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent event) {
        if (ZombiesPlugin.instance.tm.contains(event.getClickedInventory())) {
            ZombiesPlugin.instance.tm.processClick(event, getAssociatedUser((Player) event.getWhoClicked()));
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